package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/comentar")
public class ComentarioServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession(false).getAttribute("usuarioLogueado");
        Integer idReporte = UtilWeb.entero(request, "id_reporte");
        String comentario = UtilWeb.texto(request, "comentario");
        if (idReporte == null || comentario.isBlank() || comentario.length() > 2000) {
            UtilWeb.mensajeError(request, "El comentario no puede estar vacío ni superar los 2000 caracteres.");
        } else if (new ComunidadDAO().agregarComentario(idReporte, usuario.getId_usuario(), comentario)) {
            UtilWeb.mensajeExito(request, "Comentario publicado.");

            //Notificar a usuarios de nuevo comentario en reporte
            SuscripcionDAO susDAO = new SuscripcionDAO();
            NotificacionDAO notifDAO = new NotificacionDAO();
            List<Integer> suscriptores = susDAO.obtenerUsuariosSuscriptos(idReporte);

            for (Integer idSuscriptor : suscriptores) {
                if (idSuscriptor != usuario.getId_usuario()) {
                    Notificacion notif = new Notificacion();
                    notif.setId_usuario(idSuscriptor);
                    notif.setMensaje("Alguien comentó en un reporte de tus seguidos.");
                    notif.setLeido(false);
                    notif.setEnlace(request.getContextPath() + "/comunidad#reporte-" + idReporte);
                    notifDAO.registrarNotificacion(notif);
                }
            }
        } else {
            UtilWeb.mensajeError(request, "No se pudo publicar el comentario.");
        }
        response.sendRedirect(request.getContextPath() + "/comunidad#reporte-" + (idReporte == null ? "comunidad" : idReporte));
    }
}
