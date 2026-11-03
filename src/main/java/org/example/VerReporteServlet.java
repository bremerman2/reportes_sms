package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Un solo reporte completo con sus comentarios. Es a donde lleva el "Ver" de Inicio, Mis reportes
// y las notificaciones, así el usuario no cae en el muro entero de la comunidad.
@WebServlet("/ver-reporte")
public class VerReporteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer idReporte = UtilWeb.entero(request, "id");
        ReporteComunidad reporte = idReporte == null ? null : new ComunidadDAO().obtenerReporteComunidad(idReporte);
        if (reporte == null) {
            UtilWeb.mensajeError(request, "El reporte que buscás no existe o fue eliminado.");
            response.sendRedirect(request.getContextPath() + "/inicio");
            return;
        }

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") instanceof Usuario usuario) {
            reporte.setSuscrito(new SuscripcionDAO().estaSuscripto(usuario.getId_usuario(), reporte.getIdReporte()));
        }
        request.setAttribute("reporte", reporte);
        request.getRequestDispatcher("/WEB-INF/vistas/comunidad/reporte.jsp").forward(request, response);
    }
}
