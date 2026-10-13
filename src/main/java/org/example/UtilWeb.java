package org.example;

import jakarta.servlet.http.HttpServletRequest;

// Métodos chicos que usan todos los servlets.
public class UtilWeb {

    private UtilWeb() {
    }

    // Mensaje verde que se muestra en la próxima página (se guarda en sesión porque después se hace redirect)
    public static void mensajeExito(HttpServletRequest request, String texto) {
        request.getSession().setAttribute("mensajeExito", texto);
    }

    // Mensaje rojo que se muestra en la próxima página
    public static void mensajeError(HttpServletRequest request, String texto) {
        request.getSession().setAttribute("mensajeError", texto);
    }

    // Devuelve el parámetro sin espacios al principio y al final, o "" si no vino
    public static String texto(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        return valor == null ? "" : valor.trim();
    }

    // Devuelve el parámetro como entero, o null si no vino o no es un número
    public static Integer entero(HttpServletRequest request, String nombre) {
        try {
            return Integer.parseInt(texto(request, nombre));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // El administrador que inició sesión
    public static Administrador adminLogueado(HttpServletRequest request) {
        return (Administrador) request.getSession().getAttribute("adminLogueado");
    }
}
