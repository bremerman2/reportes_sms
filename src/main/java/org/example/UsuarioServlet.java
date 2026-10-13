package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Reemplaza "5. Gestion de Usuarios" del menú de consola
//   GET  /usuarios          -> estadísticas + listado
//   GET  /usuarios?id=3     -> además muestra el detalle del usuario buscado
//   POST /usuarios  accion=bloquear | eliminar
@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UsuarioDAO dao = new UsuarioDAO();

        // buscar usuario por ID
        String idTexto = UtilWeb.texto(request, "id");
        if (!idTexto.isEmpty()) {
            Integer id = UtilWeb.entero(request, "id");
            Usuario encontrado = id == null ? null : dao.obtenerUsuarioPorId(id);

            request.setAttribute("idBuscado", idTexto);
            if (encontrado != null) {
                request.setAttribute("usuarioEncontrado", encontrado);
            } else {
                request.setAttribute("error", id == null ? "El ID debe ser un número entero" : "No existe un usuario con ese ID");
            }
        }

        int[] datos = dao.obtenerEstadisticasUsuarios();
        request.setAttribute("totalUsuarios", datos[0]);
        request.setAttribute("bloqueados", datos[1]);
        request.setAttribute("activos", datos[2]);

        request.setAttribute("listaUsuarios", dao.listarUsuarios());
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/lista.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UsuarioDAO dao = new UsuarioDAO();
        String accion = UtilWeb.texto(request, "accion");
        Integer id = UtilWeb.entero(request, "id");
        Usuario usuario = id == null ? null : dao.obtenerUsuarioPorId(id);

        if (usuario == null) {
            UtilWeb.mensajeError(request, "No existe un usuario con ese ID");

        } else if (accion.equals("bloquear")) {
            if (usuario.isBloqueado()) {
                UtilWeb.mensajeError(request, "El usuario ya se encuentra bloqueado");
            } else if (dao.bloquearUsuario(id)) {
                UtilWeb.mensajeExito(request, "Usuario bloqueado correctamente");
            } else {
                UtilWeb.mensajeError(request, "No se pudo bloquear el usuario");
            }

        } else if (accion.equals("eliminar")) {
            if (dao.eliminarUsuario(id)) {
                UtilWeb.mensajeExito(request, "Usuario eliminado correctamente");
            } else {
                UtilWeb.mensajeError(request, "No se pudo eliminar el usuario (puede que tenga reportes asociados)");
            }
        }

        response.sendRedirect(request.getContextPath() + "/usuarios");
    }
}
