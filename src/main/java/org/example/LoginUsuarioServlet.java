package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login-usuario")
public class LoginUsuarioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            response.sendRedirect(request.getContextPath() + "/inicio");
            return;
        }
        request.setAttribute("tituloPagina", "Ingresar como usuario");
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = UtilWeb.texto(request, "email");
        String password = request.getParameter("password");
        if (password == null) password = "";

        Usuario usuario = new UsuarioDAO().buscarPorCredenciales(email, password);
        if (usuario != null) {
            HttpSession anterior = request.getSession(false);
            if (anterior != null) anterior.invalidate();
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogueado", usuario);
            response.sendRedirect(request.getContextPath() + "/inicio");
        } else {
            request.setAttribute("error", "No se pudo iniciar sesión. Verificá tus datos o comprobá si tu cuenta está bloqueada.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/vistas/usuarios/login.jsp").forward(request, response);
        }
    }
}
