package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/mis-reportes")
public class MisReportesServlet extends HttpServlet {

    private static final int POR_PAGINA = 10;
    private static final List<String> ESTADOS = List.of("pendiente", "confirmado", "falso_positivo");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession(false).getAttribute("usuarioLogueado");

        String estado = UtilWeb.texto(request, "estado");
        if (!ESTADOS.contains(estado)) estado = "";   // solo se aceptan los estados conocidos

        ComunidadDAO dao = new ComunidadDAO();
        int total = dao.contarMisReportes(usuario.getId_usuario(), estado);
        int totalPaginas = Math.max(1, (int) Math.ceil(total / (double) POR_PAGINA));

        Integer pagina = UtilWeb.entero(request, "pagina");
        int paginaActual = (pagina == null || pagina < 1) ? 1 : Math.min(pagina, totalPaginas);
        int desde = (paginaActual - 1) * POR_PAGINA;

        request.setAttribute("reportes", dao.listarMisReportes(usuario.getId_usuario(), estado, desde, POR_PAGINA));
        request.setAttribute("totalRegistros", total);
        request.setAttribute("paginaActual", paginaActual);
        request.setAttribute("totalPaginas", totalPaginas);
        request.setAttribute("desdeRegistro", total == 0 ? 0 : desde + 1);
        request.setAttribute("hastaRegistro", Math.min(desde + POR_PAGINA, total));
        request.setAttribute("filtroEstado", estado);
        request.setAttribute("hayFiltros", !estado.isEmpty());
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/mis-reportes.jsp").forward(request, response);
    }
}