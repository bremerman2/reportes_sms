package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Se ejecuta antes de TODOS los servlets.
// Así no hay que repetir el "¿está logueado?" en cada servlet.
@WebFilter("/*")
public class FiltroSesion extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {

        // para que los acentos y la ñ de los formularios lleguen bien
        request.setCharacterEncoding("UTF-8");

        String ruta = request.getRequestURI().substring(request.getContextPath().length());

        // rutas que se pueden ver sin iniciar sesión
        boolean esPublica = ruta.equals("/login") || ruta.startsWith("/estilos/");

        HttpSession session = request.getSession(false);
        boolean estaLogueado = session != null && session.getAttribute("adminLogueado") != null;

        if (esPublica || estaLogueado) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
