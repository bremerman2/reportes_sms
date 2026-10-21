package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class FiltroSesion extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String ruta = request.getRequestURI().substring(request.getContextPath().length());

        boolean esPublica = ruta.equals("/login") || ruta.equals("/login-usuario")
                || ruta.equals("/registro-usuario") || ruta.startsWith("/estilos/")
                || ruta.equals("/favicon.ico");

        HttpSession session = request.getSession(false);
        boolean admin = session != null && session.getAttribute("adminLogueado") != null;
        boolean usuario = session != null && session.getAttribute("usuarioLogueado") != null;

        boolean rutaAdmin = ruta.equals("/inicio") || ruta.startsWith("/reportes")
                || ruta.startsWith("/numeros-oficiales") || ruta.startsWith("/prefijos")
                || ruta.startsWith("/usuarios") || ruta.startsWith("/administradores");

        boolean rutaUsuario = ruta.equals("/perfil") || ruta.equals("/logout-usuario");

        if (esPublica || (admin && !usuario && (rutaAdmin || ruta.equals("/logout") || ruta.equals("/")))
                || (usuario && !admin && rutaUsuario)) {
            chain.doFilter(request, response);
        } else if (admin && !usuario) {
            response.sendRedirect(request.getContextPath() + "/inicio");
        } else if (usuario && !admin) {
            response.sendRedirect(request.getContextPath() + "/perfil");
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
