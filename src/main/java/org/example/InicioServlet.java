package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/inicio", ""})
public class InicioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") instanceof Usuario) {
            request.setAttribute("tituloPagina", "Comunidad");
            request.setAttribute("menuActivo", "inicio");
            request.setAttribute("reportesComunidad", new ComunidadDAO().listarReportesComunidad());
            request.getRequestDispatcher("/WEB-INF/vistas/comunidad/inicio.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/WEB-INF/vistas/inicio.jsp").forward(request, response);
    }
}
