package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/notificaciones")
public class NotificacionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login-usuario");
            return;
        }

        NotificacionDAO notifDAO = new NotificacionDAO();
        List<Notificacion> listaNotificaciones = notifDAO.obtenerPorUsuario(usuario.getId_usuario(), 20);
        notifDAO.marcarComoLeidas(usuario.getId_usuario());

        request.setAttribute("notificaciones", listaNotificaciones);
        request.setAttribute("tituloPagina", "Mis Notificaciones");
        request.setAttribute("menuActivo", "notificaciones");

        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/notificaciones.jsp").forward(request, response);
    }
}