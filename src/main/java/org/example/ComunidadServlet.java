package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;

@WebServlet("/comunidad")
public class ComunidadServlet extends HttpServlet {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", new Locale("es", "UY"));

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("tituloPagina", "Comunidad");
        request.setAttribute("menuActivo", "comunidad");
        String busqueda = UtilWeb.texto(request, "q");
        if (busqueda.length() > 100) busqueda = busqueda.substring(0, 100);

        String periodo = UtilWeb.texto(request, "periodo");
        if (!List.of("dia", "semana", "mes", "anio").contains(periodo)) periodo = "dia";
        String fechaTexto = UtilWeb.texto(request, "fecha");
        LocalDate desde = null;
        LocalDate hasta = null;
        String periodoTexto = "";
        if (!fechaTexto.isBlank()) {
            try {
                LocalDate fecha = LocalDate.parse(fechaTexto);
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
                LocalDate ultimoDia = hasta.minusDays(1);
                periodoTexto = desde.equals(ultimoDia)
                        ? "Mostrando reportes del " + desde.format(FORMATO_FECHA) + "."
                        : "Mostrando reportes del " + desde.format(FORMATO_FECHA) + " al " + ultimoDia.format(FORMATO_FECHA) + ".";
            } catch (DateTimeParseException e) {
                fechaTexto = "";
                request.setAttribute("errorFecha", "La fecha seleccionada no es válida.");
            }
        }

        request.setAttribute("busqueda", busqueda);
        request.setAttribute("periodoFiltro", periodo);
        request.setAttribute("fechaFiltro", fechaTexto);
        request.setAttribute("periodoTexto", periodoTexto);

        List<ReporteComunidad> listaReportes = Paginacion.paginar(request,
                new ComunidadDAO().listarReportesComunidad(busqueda, desde, hasta));

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") instanceof Usuario usuario) {
            SuscripcionDAO susDAO = new SuscripcionDAO();
            for (ReporteComunidad r : listaReportes) {
                r.setSuscrito(susDAO.estaSuscripto(usuario.getId_usuario(), r.getIdReporte()));
            }
        }
        request.setAttribute("reportesComunidad", listaReportes);
        request.getRequestDispatcher("/WEB-INF/vistas/comunidad/inicio.jsp").forward(request, response);
    }
}
