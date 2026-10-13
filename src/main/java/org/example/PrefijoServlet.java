package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Reemplaza "3. Gestion de Prefijos" del menú de consola
//   GET  /prefijos                       -> listado
//   GET  /prefijos?accion=nuevo          -> formulario vacío
//   GET  /prefijos?accion=editar&id=5    -> formulario con los datos
//   POST /prefijos  accion=crear | editar | eliminar
@WebServlet("/prefijos")
public class PrefijoServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/vistas/prefijos/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/vistas/prefijos/formulario.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Pais_prefijoDAO dao = new Pais_prefijoDAO();
        String accion = UtilWeb.texto(request, "accion");

        if (accion.equals("nuevo")) {
            request.getRequestDispatcher(FORMULARIO).forward(request, response);

        } else if (accion.equals("editar")) {
            Integer id = UtilWeb.entero(request, "id");
            Pais_prefijo prefijo = id == null ? null : dao.obtenerPrefijoPorId(id);

            if (prefijo == null) {
                UtilWeb.mensajeError(request, "El prefijo no existe");
                response.sendRedirect(request.getContextPath() + "/prefijos");
                return;
            }
            request.setAttribute("prefijo", prefijo);
            request.getRequestDispatcher(FORMULARIO).forward(request, response);

        } else {
            request.setAttribute("listaPrefijos", dao.obtenerPrefijos());
            request.getRequestDispatcher(LISTA).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = UtilWeb.texto(request, "accion");

        switch (accion) {
            case "crear" -> guardar(request, response, false);
            case "editar" -> guardar(request, response, true);
            case "eliminar" -> eliminar(request, response);
            default -> response.sendRedirect(request.getContextPath() + "/prefijos");
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response, boolean esEdicion) throws ServletException, IOException {

        Pais_prefijoDAO dao = new Pais_prefijoDAO();

        Integer idParametro = UtilWeb.entero(request, "id");
        int id = (esEdicion && idParametro != null) ? idParametro : 0;
        String prefijo = UtilWeb.texto(request, "prefijo");
        String nombrePais = UtilWeb.texto(request, "nombre_pais");

        // si no se ingresó el '+' se agrega
        if (!prefijo.isEmpty() && !prefijo.startsWith("+")) {
            prefijo = "+" + prefijo;
        }

        Pais_prefijo paisPrefijo = new Pais_prefijo(id, prefijo, nombrePais);

        String error = null;
        if (prefijo.isEmpty() || prefijo.equals("+") || nombrePais.isEmpty()) {
            error = "Debe ingresar valores válidos en ambos campos";
        } else if (!esEdicion && dao.existePais(nombrePais)) {
            error = "El país ya se encuentra registrado en el sistema";
        } else if (esEdicion && dao.existePaisExcluyendoId(nombrePais, id)) {
            error = "El país ya se encuentra registrado con otro prefijo";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("prefijo", paisPrefijo);
            request.getRequestDispatcher(FORMULARIO).forward(request, response);
            return;
        }

        boolean ok = esEdicion ? dao.editarPrefijo(paisPrefijo) : dao.registrarPaisPrefijo(paisPrefijo);

        if (ok) {
            UtilWeb.mensajeExito(request, esEdicion ? "Prefijo editado correctamente" : "Prefijo registrado correctamente");
        } else {
            UtilWeb.mensajeError(request, esEdicion ? "No se pudo editar el prefijo" : "No se pudo registrar el prefijo");
        }
        response.sendRedirect(request.getContextPath() + "/prefijos");
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Integer id = UtilWeb.entero(request, "id");
        Pais_prefijoDAO dao = new Pais_prefijoDAO();

        if (id == null || dao.obtenerPrefijoPorId(id) == null) {
            UtilWeb.mensajeError(request, "El prefijo no existe");
        } else if (dao.eliminarPrefijo(id)) {
            UtilWeb.mensajeExito(request, "Prefijo eliminado correctamente");
        } else {
            // lo más común: hay números oficiales que usan ese prefijo
            UtilWeb.mensajeError(request, "No se pudo eliminar el prefijo (puede que esté en uso)");
        }
        response.sendRedirect(request.getContextPath() + "/prefijos");
    }
}
