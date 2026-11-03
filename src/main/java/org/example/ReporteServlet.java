package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import java.io.IOException;
import java.time.LocalDateTime;

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

        request.setAttribute("listaReportes", estado == null ? dao.listarReportes() : dao.buscarPorEstado(estado));
        request.setAttribute("estadoFiltro", estado == null ? "" : estado.name());
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
