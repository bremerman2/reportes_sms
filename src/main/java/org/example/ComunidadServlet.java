package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Muro de la comunidad (reportes completos con comentarios).
// Antes se veía en /inicio; ahora /inicio muestra el listado de reportes recientes
// y el "Ver" de cada fila trae al usuario acá, directo al reporte (#reporte-ID).
@WebServlet("/comunidad")
public class ComunidadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("tituloPagina", "Comunidad");
        request.setAttribute("menuActivo", "comunidad");
        String busqueda = UtilWeb.texto(request, "q");
        if (busqueda.length() > 100) busqueda = busqueda.substring(0, 100);
        request.setAttribute("busqueda", busqueda);
        request.setAttribute("reportesComunidad", new ComunidadDAO().listarReportesComunidad(busqueda));
        request.getRequestDispatcher("/WEB-INF/vistas/comunidad/inicio.jsp").forward(request, response);
    }
}
