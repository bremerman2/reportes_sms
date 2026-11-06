package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// Reemplaza "4. Gestion de Reportes" del menú de consola
//   GET  /reportes                       -> listado + estadísticas
//   GET  /reportes?estado=PENDIENTE      -> listado filtrado por estado
//   GET  /reportes?accion=nuevo          -> formulario para registrar
//   GET  /reportes?accion=ver&id=N       -> detalle de un reporte
//   POST /reportes  accion=crear | confirmar | falso-positivo
@WebServlet("/reportes")
public class ReporteServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/vistas/reportes/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/vistas/reportes/formulario.jsp";
    private static final String DETALLE = "/WEB-INF/vistas/reportes/detalle.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = UtilWeb.texto(request, "accion");

        if (accion.equals("nuevo")) {
            mostrarFormulario(request, response);
            return;
        }
        if (accion.equals("ver")) {
            mostrarDetalle(request, response);
            return;
        }

        ReportesDAO dao = new ReportesDAO();

        // filtro por estado (si viene uno válido)
        String estadoTexto = UtilWeb.texto(request, "estado").toUpperCase();
        estado_reporte estado = null;
        try {
            if (!estadoTexto.isEmpty()) {
                estado = estado_reporte.valueOf(estadoTexto);
            }
        } catch (IllegalArgumentException e) {
            UtilWeb.mensajeError(request, "Estado inválido");
        }

        // Búsqueda por número o texto del SMS + filtro por estado.
        String busqueda = UtilWeb.texto(request, "q");
        if (busqueda.length() > 100) busqueda = busqueda.substring(0, 100);

        String estadoBD = estado == null ? "" : estado.name().toLowerCase();
        ComunidadDAO comunidadDAO = new ComunidadDAO();

        // Filtro por fecha: día, semana, mes o año.
        String periodo = UtilWeb.texto(request, "periodo").toLowerCase();
        if (!List.of("dia", "semana", "mes", "anio").contains(periodo)) {
            periodo = "dia";
        }

        String fechaTexto = UtilWeb.texto(request, "fecha");
        LocalDate fechaElegida = null;
        LocalDate fechaDesde = null;
        LocalDate fechaHasta = null;
        String descripcionPeriodo = "Se están mostrando todos los reportes.";

        if (!fechaTexto.isEmpty()) {
            try {
                fechaElegida = LocalDate.parse(fechaTexto);

                switch (periodo) {
                    case "semana" -> {
                        fechaDesde = fechaElegida.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                        fechaHasta = fechaDesde.plusWeeks(1);
                    }
                    case "mes" -> {
                        fechaDesde = fechaElegida.withDayOfMonth(1);
                        fechaHasta = fechaDesde.plusMonths(1);
                    }
                    case "anio" -> {
                        fechaDesde = fechaElegida.withDayOfYear(1);
                        fechaHasta = fechaDesde.plusYears(1);
                    }
                    default -> {
                        periodo = "dia";
                        fechaDesde = fechaElegida;
                        fechaHasta = fechaElegida.plusDays(1);
                    }
                }

                DateTimeFormatter formato = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "UY"));
                DateTimeFormatter formatoMes = DateTimeFormatter.ofPattern("MMMM 'de' yyyy", new Locale("es", "UY"));

                switch (periodo) {
                    case "semana" -> {
                        LocalDate ultimoDia = fechaHasta.minusDays(1);
                        if (fechaDesde.getMonth() == ultimoDia.getMonth()
                                && fechaDesde.getYear() == ultimoDia.getYear()) {
                            descripcionPeriodo = "Mostrando reportes del "
                                    + fechaDesde.getDayOfMonth() + " al "
                                    + ultimoDia.format(formato) + ".";
                        } else {
                            descripcionPeriodo = "Mostrando reportes del "
                                    + fechaDesde.format(formato) + " al "
                                    + ultimoDia.format(formato) + ".";
                        }
                    }
                    case "mes" -> descripcionPeriodo = "Mostrando reportes de " + fechaElegida.format(formatoMes) + ".";
                    case "anio" -> descripcionPeriodo = "Mostrando reportes del año " + fechaElegida.getYear() + ".";
                    default -> descripcionPeriodo = "Mostrando reportes del " + fechaElegida.format(formato) + ".";
                }
            } catch (DateTimeException e) {
                fechaTexto = "";
                fechaDesde = null;
                fechaHasta = null;
                UtilWeb.mensajeError(request, "La fecha seleccionada no es válida.");
            }
        }

        int total = comunidadDAO.contarReportesAdmin(busqueda, estadoBD, fechaDesde, fechaHasta);
        int desde = Paginacion.calcular(request, total, Paginacion.POR_PAGINA);

        request.setAttribute("listaReportes", comunidadDAO.listarReportesAdmin(
                busqueda, estadoBD, fechaDesde, fechaHasta, desde, Paginacion.POR_PAGINA));
        request.setAttribute("busqueda", busqueda);
        request.setAttribute("estadoFiltro", estado == null ? "" : estado.name());
        request.setAttribute("periodoFiltro", periodo);
        request.setAttribute("fechaFiltro", fechaTexto);
        request.setAttribute("descripcionPeriodo", descripcionPeriodo);
        request.setAttribute("estados", estado_reporte.values());
        request.setAttribute("estadisticas", dao.consultarEstadisticas());
        request.getRequestDispatcher(LISTA).forward(request, response);
    }

    // un reporte completo (SMS entero, captura, comentarios) con las acciones de confirmar / falso positivo
    private void mostrarDetalle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = UtilWeb.entero(request, "id");
        ReporteComunidad reporte = id == null ? null : new ComunidadDAO().obtenerReporteComunidad(id);
        if (reporte == null) {
            UtilWeb.mensajeError(request, "El reporte no existe");
            volverAlListado(request, response);
            return;
        }
        request.setAttribute("reporte", reporte);
        request.setAttribute("estadoFiltro", UtilWeb.texto(request, "estadoFiltro"));
        request.getRequestDispatcher(DETALLE).forward(request, response);
    }

    // el formulario necesita la lista de usuarios para el desplegable
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("listaUsuarios", new UsuarioDAO().listarUsuarios());
        request.getRequestDispatcher(FORMULARIO).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = UtilWeb.texto(request, "accion");

        switch (accion) {
            case "crear" -> registrar(request, response);
            case "confirmar" -> confirmar(request, response);
            case "falso-positivo" -> falsoPositivo(request, response);
            default -> response.sendRedirect(request.getContextPath() + "/reportes");
        }
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String cuerpoSms = UtilWeb.texto(request, "cuerpo_sms");
        boolean esManual = request.getParameter("es_manual") != null;   // checkbox: viene solo si está marcado
        String capturaPantalla = esManual ? UtilWeb.texto(request, "captura_pantalla") : null;
        Integer idUsuario = UtilWeb.entero(request, "id_usuario");
        Integer idNumReportado = UtilWeb.entero(request, "id_num_reportado");

        // controlar campos obligatorios (la captura solo si es manual)
        if (cuerpoSms.isEmpty() || idUsuario == null || idNumReportado == null || (esManual && capturaPantalla.isEmpty())) {
            request.setAttribute("error", "Debe ingresar valores válidos en los campos obligatorios");
            mostrarFormulario(request, response);
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setId_usuario(idUsuario);

        Numero_reportado numeroReportado = new Numero_reportado();
        numeroReportado.setId_numero_reportado(idNumReportado);

        // id, fecha_hora y estado se resuelven dentro del DAO (autoincremental, ahora y pendiente)
        Reportes reporte = new Reportes(0, LocalDateTime.now(), cuerpoSms, capturaPantalla, esManual, estado_reporte.PENDIENTE, usuario, numeroReportado);

        if (new ReportesDAO().registrarReporte(reporte)) {
            UtilWeb.mensajeExito(request, "Reporte registrado correctamente (ID: " + reporte.getId_reporte() + ")");
        } else {
            UtilWeb.mensajeError(request, "No se pudo registrar el reporte (verificá que el usuario y el número reportado existan)");
        }
        response.sendRedirect(request.getContextPath() + "/reportes");
    }

    private void confirmar(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Integer id = UtilWeb.entero(request, "id");

        if (id != null && new ReportesDAO().confirmarReporte(id)) {
            UtilWeb.mensajeExito(request, "Reporte confirmado correctamente");

            //Enviar notificaciones a los seguidores
            notificarSuscriptores(request, id, "El reporte que esta suscrito fue verificado y marcado como CONFIRMADO.");
        } else {
            UtilWeb.mensajeError(request, "No se pudo confirmar el reporte (verificar que exista y esté pendiente)");
        }
        volverAlListado(request, response);
    }

    private void falsoPositivo(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Integer id = UtilWeb.entero(request, "id");

        if (id != null && new ReportesDAO().gestionarFalsoPositivo(id)) {
            UtilWeb.mensajeExito(request, "Reporte marcado como falso positivo");

            notificarSuscriptores(request, id, "El reporte que esta suscrito fue marcado como FALSO POSITIVO.");
        } else {
            UtilWeb.mensajeError(request, "No se pudo actualizar el reporte");
        }
        volverAlListado(request, response);
    }

    //vuelve al listado manteniendo el filtro que estaba aplicado
    //(o al detalle, si la acción se hizo desde la página del reporte)
    private void volverAlListado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String estado = UtilWeb.texto(request, "estadoFiltro");
        Integer id = UtilWeb.entero(request, "id");
        if (id != null && "detalle".equals(UtilWeb.texto(request, "origen"))) {
            response.sendRedirect(request.getContextPath() + "/reportes?accion=ver&id=" + id
                    + (estado.isEmpty() ? "" : "&estadoFiltro=" + estado));
            return;
        }
        String url = request.getContextPath() + "/reportes" + (estado.isEmpty() ? "" : "?estado=" + estado);
        response.sendRedirect(url);
    }

    private void notificarSuscriptores(HttpServletRequest request, int idReporte, String mensaje) {
        SuscripcionDAO suscripcionDAO = new SuscripcionDAO();
        NotificacionDAO notificacionDAO = new NotificacionDAO();
        List<Integer> idsSuscriptos = suscripcionDAO.obtenerUsuariosSuscriptos(idReporte);

        for (Integer idUsuario : idsSuscriptos) {
            Notificacion notif = new Notificacion();
            notif.setId_usuario(idUsuario);
            notif.setMensaje(mensaje);
            notif.setLeido(false);
            notif.setEnlace(request.getContextPath() + "/ver-reporte?id=" + idReporte);

            notificacionDAO.registrarNotificacion(notif);
        }
    }
}
