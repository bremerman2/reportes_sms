package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/reportar")
public class ReportarSmsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("tituloPagina", "Reportar SMS");
        request.setAttribute("menuActivo", "reportar");
        request.setAttribute("prefijos", new Pais_prefijoDAO().obtenerPrefijos());
        request.setAttribute("cloudinaryConfigurada", ConfiguracionCloudinary.isConfigurada());
        request.setAttribute("cloudinaryCloudName", ConfiguracionCloudinary.getCloudName());
        request.setAttribute("cloudinaryUploadPreset", ConfiguracionCloudinary.getUploadPreset());
        request.getRequestDispatcher("/WEB-INF/vistas/comunidad/reportar.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession(false).getAttribute("usuarioLogueado");
        String telefono = UtilWeb.texto(request, "numero_telefono");
        String cuerpo = UtilWeb.texto(request, "cuerpo_sms");
        Integer idPrefijo = UtilWeb.entero(request, "id_prefijo");
        String capturaUrl = UtilWeb.texto(request, "captura_url");
        String capturaPublicId = UtilWeb.texto(request, "captura_public_id");
        //capturar notificaciones
        boolean quiereNotificaciones = request.getParameter("notificar_actualizaciones") != null;
        String error = null;

        if (telefono.isBlank() || cuerpo.isBlank() || idPrefijo == null) {
            error = "Ingresá el número, el prefijo y el contenido del SMS.";
        } else if (telefono.length() > 20 || !telefono.matches("[+0-9() .-]{5,20}")) {
            error = "El número telefónico no tiene un formato válido.";
        } else if (cuerpo.length() > 10000) {
            error = "El contenido del SMS es demasiado largo.";
        }

        if (error == null) {
            try {
                ImagenCloudinary.validar(capturaUrl, capturaPublicId);
            } catch (IllegalArgumentException e) {
                error = e.getMessage();
            }
        }

        String captura = capturaUrl == null || capturaUrl.isBlank() ? null : capturaUrl;

        //recibe id y se guarda en suscripcion
        if (error == null) {
            int idReporteGenerado = new ComunidadDAO().registrarReporte(usuario.getId_usuario(), telefono, idPrefijo, cuerpo, captura);

            if (idReporteGenerado > 0) {
                if (quiereNotificaciones) {
                    Suscripcion suscripcion = new Suscripcion(usuario.getId_usuario(), idReporteGenerado);
                    new SuscripcionDAO().registrarSuscripcion(suscripcion);
                }

                UtilWeb.mensajeExito(request, "Tu reporte se publicó en la comunidad y quedó pendiente de revisión.");
                response.sendRedirect(request.getContextPath() + "/inicio");
                return;
            }
        }

        if (error == null) error = "No se pudo guardar el reporte. Verificá el prefijo seleccionado y la estructura de la base de datos.";
        request.setAttribute("error", error);
        request.setAttribute("telefono", telefono);
        request.setAttribute("cuerpoSms", cuerpo);
        request.setAttribute("capturaUrl", capturaUrl);
        request.setAttribute("capturaPublicId", capturaPublicId);
        request.setAttribute("prefijos", new Pais_prefijoDAO().obtenerPrefijos());
        request.setAttribute("tituloPagina", "Reportar SMS");
        request.setAttribute("menuActivo", "reportar");
        request.setAttribute("cloudinaryConfigurada", ConfiguracionCloudinary.isConfigurada());
        request.setAttribute("cloudinaryCloudName", ConfiguracionCloudinary.getCloudName());
        request.setAttribute("cloudinaryUploadPreset", ConfiguracionCloudinary.getUploadPreset());
        request.getRequestDispatcher("/WEB-INF/vistas/comunidad/reportar.jsp").forward(request, response);
    }
}
