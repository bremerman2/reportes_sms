package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("adminLogueado") != null) {
            response.sendRedirect(request.getContextPath() + "/inicio");
        } else {
            request.getRequestDispatcher("/WEB-INF/vistas/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = UtilWeb.texto(request, "email");
        String password = UtilWeb.texto(request, "password");

        AdministradorDAO administradorDAO = new AdministradorDAO();
        Administrador administrador = administradorDAO.buscarPorCredenciales(email, password);

        if (administrador != null) {
            HttpSession session = request.getSession();
            session.setAttribute("adminLogueado", administrador);
            response.sendRedirect(request.getContextPath() + "/inicio");
        } else {
            request.setAttribute("error", "Email o contraseña incorrectos");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/vistas/login.jsp").forward(request, response);
        }
    }
}
