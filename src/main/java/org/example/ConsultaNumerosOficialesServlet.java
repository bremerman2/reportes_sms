package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/consulta-numeros-oficiales")
public class ConsultaNumerosOficialesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "listaNumerosOficialesPublicos",
                new Numero_oficialDAO().listarNumOficiales()
        );

        request.getRequestDispatcher(
                "/WEB-INF/vistas/usuarios/numeros-oficiales.jsp"
        ).forward(request, response);
    }
}
