package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Reemplaza "1. Gestion de Administradores" del menú de consola
@WebServlet("/administradores")
public class AdministradorServlet extends HttpServlet {

    private static final String VISTA = "/WEB-INF/vistas/administradores/registrar.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(VISTA).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nombre = UtilWeb.texto(request, "nombre");
        String apellido = UtilWeb.texto(request, "apellido");
        String telefono = UtilWeb.texto(request, "telefono");
        String email = UtilWeb.texto(request, "email").toLowerCase();
        String password = UtilWeb.texto(request, "password");

        AdministradorDAO administradorDAO = new AdministradorDAO();

        // mismas validaciones que tenía la consola
        String error = null;
        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || email.isEmpty() || password.isEmpty()) {
            error = "Todos los campos son obligatorios";
        } else if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            error = "Ingresá un email con formato válido (nombre@gmail.com)";
        } else if (administradorDAO.existeEmail(email)) {
            error = "Ya existe un administrador registrado con ese email";
        } else if (password.length() < 6) {
            error = "La contraseña debe tener al menos 6 caracteres";
        }

        if (error != null) {
            // se vuelve a mostrar el formulario con lo que había escrito (menos la contraseña)
            request.setAttribute("error", error);
            request.setAttribute("nombre", nombre);
            request.setAttribute("apellido", apellido);
            request.setAttribute("telefono", telefono);
            request.setAttribute("email", email);
            request.getRequestDispatcher(VISTA).forward(request, response);
            return;
        }

        Administrador administrador = new Administrador(0, email, password, nombre, apellido, telefono);

        if (administradorDAO.registrarAdministrador(administrador)) {
            UtilWeb.mensajeExito(request, "Administrador registrado correctamente");
        } else {
            UtilWeb.mensajeError(request, "No se pudo registrar el administrador");
        }
        response.sendRedirect(request.getContextPath() + "/administradores");
    }
}
