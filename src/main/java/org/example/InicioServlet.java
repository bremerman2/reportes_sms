package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet({"/inicio", ""})
public class InicioServlet extends HttpServlet {
    private static final List<String> ESTADOS = List.of("pendiente", "confirmado", "falso_positivo");
    private static final Map<String, String> ETIQUETAS = Map.of(
            "pendiente", "Pendientes", "confirmado", "Confirmados", "falso_positivo", "Falsos positivos");
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "UY"));

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") instanceof Usuario) {
            mostrarInicioUsuario(request, response);
            return;
        }
        ReportesDAO reportesDAO = new ReportesDAO();
        request.setAttribute("reportesHoy", reportesDAO.contarReportesHoy());
        request.setAttribute("reportesPendientes", reportesDAO.contarReportesPendientes());
        request.setAttribute("estadisticas", reportesDAO.consultarEstadisticas());
        request.getRequestDispatcher("/WEB-INF/vistas/inicio.jsp").forward(request, response);
    }

    private void mostrarInicioUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numero = UtilWeb.texto(request, "numero");
        if (numero.length() > 20) numero = numero.substring(0, 20);

        String[] pedidos = request.getParameterValues("estado");
        List<String> listaPedidos = pedidos == null ? List.of() : Arrays.asList(pedidos);
        List<String> estados = ESTADOS.stream().filter(listaPedidos::contains).toList();
        if (estados.size() == ESTADOS.size()) estados = List.of();

        String periodo = periodoValido(UtilWeb.texto(request, "periodo"));
        String fechaTexto = UtilWeb.texto(request, "fecha");
        LocalDate fechaDesde = null;
        LocalDate fechaHasta = null;
        String periodoTexto = "";
        if (!fechaTexto.isBlank()) {
            try {
                LocalDate fecha = LocalDate.parse(fechaTexto);
                LocalDate[] rango = calcularRango(fecha, periodo);
                fechaDesde = rango[0];
                fechaHasta = rango[1];
                periodoTexto = textoRango(fechaDesde, fechaHasta.minusDays(1));
            } catch (DateTimeParseException e) {
                fechaTexto = "";
                request.setAttribute("errorFecha", "La fecha seleccionada no es válida.");
            }
        }

        ComunidadDAO dao = new ComunidadDAO();
        int total = dao.contarRecientes(numero, estados, fechaDesde, fechaHasta);
        int desde = Paginacion.calcular(request, total, Paginacion.POR_PAGINA);
        request.setAttribute("reportes", dao.listarRecientes(numero, estados, fechaDesde, fechaHasta, desde, Paginacion.POR_PAGINA));
        request.setAttribute("filtroNumero", numero);
        request.setAttribute("estadosElegidos", estados);
        request.setAttribute("periodoFiltro", periodo);
        request.setAttribute("fechaFiltro", fechaTexto);
        request.setAttribute("periodoTexto", periodoTexto);
        request.setAttribute("segmentosEstado", armarSegmentos(request, numero, estados, periodo, fechaTexto));
        request.setAttribute("hayFiltros", !numero.isEmpty() || !estados.isEmpty() || !fechaTexto.isEmpty());
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/inicio.jsp").forward(request, response);
    }

    private String periodoValido(String periodo) {
        return List.of("dia", "semana", "mes", "anio").contains(periodo) ? periodo : "dia";
    }

    private LocalDate[] calcularRango(LocalDate fecha, String periodo) {
        LocalDate desde;
        LocalDate hasta;
        switch (periodo) {
            case "semana":
                desde = fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                hasta = desde.plusWeeks(1);
                break;
            case "mes":
                desde = fecha.withDayOfMonth(1);
                hasta = desde.plusMonths(1);
                break;
            case "anio":
                desde = fecha.withDayOfYear(1);
                hasta = desde.plusYears(1);
                break;
            default:
                desde = fecha;
                hasta = fecha.plusDays(1);
        }
        return new LocalDate[]{desde, hasta};
    }

    private String textoRango(LocalDate desde, LocalDate hasta) {
        if (desde.equals(hasta)) return "Mostrando reportes del " + desde.format(FORMATO_FECHA) + ".";
        return "Mostrando reportes del " + desde.format(FORMATO_FECHA) + " al " + hasta.format(FORMATO_FECHA) + ".";
    }

    private List<Map<String, Object>> armarSegmentos(HttpServletRequest request, String numero, List<String> elegidos,
                                                     String periodo, String fecha) {
        List<Map<String, Object>> segmentos = new ArrayList<>();
        segmentos.add(Map.of("etiqueta", "Todos", "activo", elegidos.isEmpty(), "url", urlInicio(request, numero, List.of(), periodo, fecha)));
        for (String estado : ESTADOS) {
            boolean activo = elegidos.contains(estado);
            List<String> nuevos = ESTADOS.stream().filter(e -> e.equals(estado) ? !activo : elegidos.contains(e)).toList();
            if (nuevos.size() == ESTADOS.size()) nuevos = List.of();
            segmentos.add(Map.of("etiqueta", ETIQUETAS.get(estado), "activo", activo,
                    "url", urlInicio(request, numero, nuevos, periodo, fecha)));
        }
        return segmentos;
    }

    private String urlInicio(HttpServletRequest request, String numero, List<String> estados, String periodo, String fecha) {
        StringBuilder url = new StringBuilder(request.getContextPath() + "/inicio");
        String separador = "?";
        if (!numero.isEmpty()) { url.append(separador).append("numero=").append(URLEncoder.encode(numero, StandardCharsets.UTF_8)); separador = "&"; }
        for (String estado : estados) { url.append(separador).append("estado=").append(estado); separador = "&"; }
        if (fecha != null && !fecha.isBlank()) {
            url.append(separador).append("periodo=").append(periodo);
            url.append("&fecha=").append(URLEncoder.encode(fecha, StandardCharsets.UTF_8));
        }
        return url.toString();
    }
}
