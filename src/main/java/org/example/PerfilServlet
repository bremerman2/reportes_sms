package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario sesion = (Usuario) request.getSession(false).getAttribute("usuarioLogueado");
        Usuario actualizado = new UsuarioDAO().obtenerUsuarioPorId(sesion.getId_usuario());
        if (actualizado == null || actualizado.isBloqueado()) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login-usuario?bloqueado=1");
            return;
        }
        request.getSession().setAttribute("usuarioLogueado", actualizado);
        request.setAttribute("usuario", actualizado);
        request.setAttribute("tituloPagina", "Mi perfil");
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/perfil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario sesion = (Usuario) request.getSession(false).getAttribute("usuarioLogueado");
        String nombre = UtilWeb.texto(request, "nombre");
        String apellido = UtilWeb.texto(request, "apellido");
        String telefono = UtilWeb.texto(request, "telefono");
        String email = UtilWeb.texto(request, "email");
        String password = request.getParameter("password");
        String confirmar = request.getParameter("confirmarPassword");
        if (password == null) password = "";
        if (confirmar == null) confirmar = "";

        UsuarioDAO dao = new UsuarioDAO();
        String error = null;
        if (nombre.isBlank() || apellido.isBlank() || telefono.isBlank() || email.isBlank()) {
            error = "Nombre, apellido, teléfono y email son obligatorios.";
        } else if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            error = "Ingresá un email válido.";
        } else if (dao.existeEmail(email, sesion.getId_usuario())) {
            error = "Ese email ya está registrado por otra cuenta.";
        } else if (!password.isEmpty() && password.length() < 8) {
            error = "La nueva contraseña debe tener al menos 8 caracteres.";
        } else if (!password.equals(confirmar)) {
            error = "Las contraseñas no coinciden.";
        }

        Usuario usuario = new Usuario();
        usuario.setId_usuario(sesion.getId_usuario());
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setTelefono(telefono);
        usuario.setEmail(email);
        usuario.setPassword(password);

        if (error == null && dao.actualizarPerfil(usuario, !password.isEmpty())) {
            Usuario actualizado = dao.obtenerUsuarioPorId(sesion.getId_usuario());
            if (actualizado != null) request.getSession().setAttribute("usuarioLogueado", actualizado);
            UtilWeb.mensajeExito(request, "Tu perfil se actualizó correctamente.");
            response.sendRedirect(request.getContextPath() + "/perfil");
            return;
        }
        if (error == null) error = "No se pudieron guardar los cambios. Revisá los datos e intentá nuevamente.";
        request.setAttribute("error", error);
        request.setAttribute("usuario", usuario);
        request.setAttribute("tituloPagina", "Mi perfil");
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/perfil.jsp").forward(request, response);
    }
}
