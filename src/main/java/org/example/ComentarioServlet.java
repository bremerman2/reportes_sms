package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

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
        } else {
            UtilWeb.mensajeError(request, "No se pudo publicar el comentario.");
        }
        response.sendRedirect(request.getContextPath() + "/inicio#reporte-" + (idReporte == null ? "comunidad" : idReporte));
    }
}
