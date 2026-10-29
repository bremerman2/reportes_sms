package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/consulta-prefijos")
public class ConsultaPrefijosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "listaPrefijosPublicos",
                new Pais_prefijoDAO().obtenerPrefijos()
        );

        request.getRequestDispatcher(
                "/WEB-INF/vistas/usuarios/prefijos.jsp"
        ).forward(request, response);
    }
}
