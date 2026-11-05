package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// Listado público de prefijos, con búsqueda por país (o prefijo) y paginación
@WebServlet("/consulta-prefijos")
public class ConsultaPrefijosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String busqueda = UtilWeb.texto(request, "q");
        List<Pais_prefijo> lista = PrefijoServlet.filtrar(new Pais_prefijoDAO().obtenerPrefijos(), busqueda);

        request.setAttribute("busqueda", busqueda);
        request.setAttribute("listaPrefijosPublicos", Paginacion.paginar(request, lista));

        request.getRequestDispatcher(
                "/WEB-INF/vistas/usuarios/prefijos.jsp"
        ).forward(request, response);
    }
}
