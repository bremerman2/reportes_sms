package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/suscripcion")
public class SuscripcionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Leemos el parámetro directamente del request
        String idReporteParam = request.getParameter("id_reporte");
        String referer = request.getHeader("Referer");
        String destino = (referer != null) ? referer : request.getContextPath() + "/comunidad";

        if (idReporteParam != null && !idReporteParam.isBlank()) {
            try {
                int idReporte = Integer.parseInt(idReporteParam.trim());
                int idUsuario = usuario.getId_usuario();
                SuscripcionDAO dao = new SuscripcionDAO();

                if (dao.estaSuscripto(idUsuario, idReporte)) {
                    if (dao.eliminarSuscripcion(idUsuario, idReporte)) {
                        session.setAttribute("mensajeExito", "Dejaste de seguir este reporte.");
                    } else {
                        session.setAttribute("mensajeError", "No se pudo eliminar la suscripción.");
                    }
                } else {
                    Suscripcion suscripcion = new Suscripcion(idUsuario, idReporte);
                    if (dao.registrarSuscripcion(suscripcion)) {
                        session.setAttribute("mensajeExito", "Ahora estás siguiendo este reporte.");
                    } else {
                        session.setAttribute("mensajeError", "Error al guardar la suscripción en la BD.");
                    }
                }
            } catch (NumberFormatException e) {
                session.setAttribute("mensajeError", "ID de reporte inválido.");
            }
        } else {
            session.setAttribute("mensajeError", "No se recibió el identificador del reporte.");
        }

        response.sendRedirect(destino);
    }
}