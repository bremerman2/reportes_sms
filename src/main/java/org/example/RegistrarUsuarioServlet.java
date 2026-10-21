package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/registro-usuario")
public class RegistroUsuarioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            response.sendRedirect(request.getContextPath() + "/perfil");
            return;
        }
        request.setAttribute("tituloPagina", "Crear cuenta");
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = UtilWeb.texto(request, "nombre");
        String apellido = UtilWeb.texto(request, "apellido");
        String telefono = UtilWeb.texto(request, "telefono");
        String email = UtilWeb.texto(request, "email");
        String password = request.getParameter("password");
        String confirmar = request.getParameter("confirmarPassword");
        if (password == null) password = "";
        if (confirmar == null) confirmar = "";

        request.setAttribute("nombre", nombre);
        request.setAttribute("apellido", apellido);
        request.setAttribute("telefono", telefono);
        request.setAttribute("email", email);

        String error = null;
        if (nombre.isBlank() || apellido.isBlank() || telefono.isBlank() || email.isBlank() || password.isBlank()) {
            error = "Completá todos los campos.";
        } else if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            error = "Ingresá un email válido.";
        } else if (password.length() < 8) {
            error = "La contraseña debe tener al menos 8 caracteres.";
        } else if (!password.equals(confirmar)) {
            error = "Las contraseñas no coinciden.";
        }

        UsuarioDAO dao = new UsuarioDAO();
        if (error == null && dao.existeEmail(email, null)) {
            error = "Ya existe una cuenta con ese email.";
        }
        if (error == null) {
            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setTelefono(telefono);
            usuario.setEmail(email);
            usuario.setPassword(password);
            if (dao.registrarUsuario(usuario)) {
                usuario = dao.buscarPorCredenciales(email, password);
                if (usuario != null) {
                    HttpSession anterior = request.getSession(false);
                    if (anterior != null) anterior.invalidate();
                    request.getSession(true).setAttribute("usuarioLogueado", usuario);
                    response.sendRedirect(request.getContextPath() + "/perfil?registro=ok");
                    return;
                }
                request.setAttribute("error", "La cuenta se creó, pero no se pudo iniciar sesión. Ingresá con tus datos.");
                request.getRequestDispatcher("/WEB-INF/vistas/usuarios/login.jsp").forward(request, response);
                return;
            }
            error = "No se pudo crear la cuenta. Verificá que la base de datos permita registrar usuarios sin administrador asociado.";
        }

        request.setAttribute("error", error);
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/registro.jsp").forward(request, response);
    }
}
