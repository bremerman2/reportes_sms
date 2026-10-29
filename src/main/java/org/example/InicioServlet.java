package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet({"/inicio", ""})
public class InicioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        //usuario comun logueado
        if (session != null && session.getAttribute("usuarioLogueado") instanceof Usuario) {
            mostrarInicioUsuario(request, response);
            return;
        }

        //Administrador
        ReportesDAO reportesDAO = new ReportesDAO();
        request.setAttribute("reportesHoy", reportesDAO.contarReportesHoy());
        request.setAttribute("reportesPendientes", reportesDAO.contarReportesPendientes());
        request.setAttribute("estadisticas", reportesDAO.consultarEstadisticas());

        request.getRequestDispatcher("/WEB-INF/vistas/inicio.jsp").forward(request, response);
    }

    // Cantidad de reportes por página en el Inicio del usuario
    private static final int POR_PAGINA = 10;
    private static final List<String> ESTADOS = List.of("pendiente", "confirmado", "falso_positivo");

    // Inicio del usuario: botón "Reportar SMS" + listado de reportes recientes
    private void mostrarInicioUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numero = UtilWeb.texto(request, "numero");
        String estado = UtilWeb.texto(request, "estado");
        if (!ESTADOS.contains(estado)) estado = "";   // solo se aceptan los estados conocidos
        if (numero.length() > 20) numero = numero.substring(0, 20);

        ComunidadDAO dao = new ComunidadDAO();
        int total = dao.contarRecientes(numero, estado);
        int totalPaginas = Math.max(1, (int) Math.ceil(total / (double) POR_PAGINA));

        Integer pagina = UtilWeb.entero(request, "pagina");
        int paginaActual = (pagina == null || pagina < 1) ? 1 : Math.min(pagina, totalPaginas);
        int desde = (paginaActual - 1) * POR_PAGINA;

        request.setAttribute("reportes", dao.listarRecientes(numero, estado, desde, POR_PAGINA));
        request.setAttribute("totalRegistros", total);
        request.setAttribute("paginaActual", paginaActual);
        request.setAttribute("totalPaginas", totalPaginas);
        request.setAttribute("desdeRegistro", total == 0 ? 0 : desde + 1);
        request.setAttribute("hastaRegistro", Math.min(desde + POR_PAGINA, total));
        request.setAttribute("filtroNumero", numero);
        request.setAttribute("filtroEstado", estado);
        request.setAttribute("hayFiltros", !numero.isEmpty() || !estado.isEmpty());
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/inicio.jsp").forward(request, response);
    }
}
