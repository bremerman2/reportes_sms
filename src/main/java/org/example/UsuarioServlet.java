package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// Reemplaza "5. Gestion de Usuarios" del menú de consola
//   GET  /usuarios                 -> estadísticas + listado (búsqueda por nombre, paginado)
//   GET  /usuarios?accion=ver&id=3 -> perfil del usuario con los reportes que hizo
//   POST /usuarios  accion=bloquear | eliminar
@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private static final List<String> ESTADOS = List.of("pendiente", "confirmado", "falso_positivo");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UsuarioDAO dao = new UsuarioDAO();

        if (UtilWeb.texto(request, "accion").equals("ver")) {
            mostrarPerfil(request, response, dao);
            return;
        }

        int[] datos = dao.obtenerEstadisticasUsuarios();
        request.setAttribute("totalUsuarios", datos[0]);
        request.setAttribute("bloqueados", datos[1]);
        request.setAttribute("activos", datos[2]);

        // búsqueda por nombre y/o apellido ("ana", "ana gómez", "gomez")
        String busqueda = UtilWeb.texto(request, "q");
        List<Usuario> lista = dao.listarUsuarios();
        if (!busqueda.isBlank()) {
            lista = lista.stream()
                    .filter(u -> Paginacion.contiene(u.getNombre() + " " + u.getApellido(), busqueda))
                    .toList();
        }
        request.setAttribute("busqueda", busqueda);
        request.setAttribute("listaUsuarios", Paginacion.paginar(request, lista));
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/lista.jsp").forward(request, response);
    }

    // datos del usuario + sus reportes (con filtro por estado y paginación)
    private void mostrarPerfil(HttpServletRequest request, HttpServletResponse response, UsuarioDAO dao) throws ServletException, IOException {
        Integer id = UtilWeb.entero(request, "id");
        Usuario usuario = id == null ? null : dao.obtenerUsuarioPorId(id);
        if (usuario == null) {
            UtilWeb.mensajeError(request, "No existe un usuario con ese ID");
            response.sendRedirect(request.getContextPath() + "/usuarios");
            return;
        }

        String estado = UtilWeb.texto(request, "estado");
        if (!ESTADOS.contains(estado)) estado = "";

        ComunidadDAO comunidadDAO = new ComunidadDAO();
        int total = comunidadDAO.contarMisReportes(id, "", estado);
        int desde = Paginacion.calcular(request, total, Paginacion.POR_PAGINA);

        request.setAttribute("usuarioPerfil", usuario);
        request.setAttribute("reportesUsuario", comunidadDAO.listarMisReportes(id, "", estado, desde, Paginacion.POR_PAGINA));
        request.setAttribute("filtroEstado", estado);
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/perfil-admin.jsp").forward(request, response);
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

        // si se bloqueó desde el perfil, se vuelve al perfil; si se eliminó (o vino del listado), al listado
        if (usuario != null && accion.equals("bloquear") && "perfil".equals(UtilWeb.texto(request, "origen"))) {
            response.sendRedirect(request.getContextPath() + "/usuarios?accion=ver&id=" + id);
        } else {
            response.sendRedirect(request.getContextPath() + "/usuarios");
        }
    }
}
