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

    private static final List<String> ESTADOS = List.of("pendiente", "confirmado", "falso_positivo");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession(false).getAttribute("usuarioLogueado");

        String numero = UtilWeb.texto(request, "numero");
        if (numero.length() > 20) numero = numero.substring(0, 20);
        String estado = UtilWeb.texto(request, "estado");
        if (!ESTADOS.contains(estado)) estado = "";   // solo se aceptan los estados conocidos

        ComunidadDAO dao = new ComunidadDAO();
        int total = dao.contarMisReportes(usuario.getId_usuario(), numero, estado);
        int desde = Paginacion.calcular(request, total, Paginacion.POR_PAGINA);

        request.setAttribute("reportes", dao.listarMisReportes(usuario.getId_usuario(), numero, estado, desde, Paginacion.POR_PAGINA));
        request.setAttribute("filtroNumero", numero);
        request.setAttribute("filtroEstado", estado);
        request.setAttribute("hayFiltros", !numero.isEmpty() || !estado.isEmpty());
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/mis-reportes.jsp").forward(request, response);
    }
}
