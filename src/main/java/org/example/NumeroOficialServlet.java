package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

// Reemplaza "2. Gestion de Numeros Oficiales" del menú de consola
//   GET  /numeros-oficiales                       -> listado
//   GET  /numeros-oficiales?accion=nuevo          -> formulario vacío
//   GET  /numeros-oficiales?accion=editar&id=5    -> formulario con los datos
//   POST /numeros-oficiales  accion=crear | editar | eliminar
@WebServlet("/numeros-oficiales")
public class NumeroOficialServlet extends HttpServlet {

    private static final String LISTA = "/WEB-INF/vistas/numeros-oficiales/lista.jsp";
    private static final String FORMULARIO = "/WEB-INF/vistas/numeros-oficiales/formulario.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Numero_oficialDAO dao = new Numero_oficialDAO();
        String accion = UtilWeb.texto(request, "accion");

        if (accion.equals("nuevo")) {
            mostrarFormulario(request, response, null);

        } else if (accion.equals("editar")) {
            Integer id = UtilWeb.entero(request, "id");
            Numero_oficial numero = id == null ? null : dao.obtenerPorId(id);

            if (numero == null) {
                UtilWeb.mensajeError(request, "El número oficial no existe");
                response.sendRedirect(request.getContextPath() + "/numeros-oficiales");
                return;
            }
            mostrarFormulario(request, response, numero);

        } else {
            request.setAttribute("listaNumeros", dao.listarNumOficiales());
            request.getRequestDispatcher(LISTA).forward(request, response);
        }
    }

    // el formulario necesita la lista de prefijos para el desplegable
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Numero_oficial numero) throws ServletException, IOException {
        request.setAttribute("numero", numero);
        request.setAttribute("listaPrefijos", new Pais_prefijoDAO().obtenerPrefijos());
        request.getRequestDispatcher(FORMULARIO).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String accion = UtilWeb.texto(request, "accion");

        switch (accion) {
            case "crear" -> guardar(request, response, false);
            case "editar" -> guardar(request, response, true);
            case "eliminar" -> eliminar(request, response);
            default -> response.sendRedirect(request.getContextPath() + "/numeros-oficiales");
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response, boolean esEdicion) throws ServletException, IOException {

        Numero_oficialDAO dao = new Numero_oficialDAO();

        String telefono = UtilWeb.texto(request, "numero_telefono");
        String nombreEntidad = UtilWeb.texto(request, "nombre_entidad");
        Integer idPrefijo = UtilWeb.entero(request, "id_prefijo");

        Pais_prefijo prefijo = new Pais_prefijo();
        prefijo.setId_prefijo(idPrefijo == null ? 0 : idPrefijo);

        Numero_oficial numero;
        if (esEdicion) {
            Integer id = UtilWeb.entero(request, "id");
            numero = id == null ? null : dao.obtenerPorId(id);
            if (numero == null) {
                UtilWeb.mensajeError(request, "El número oficial no existe");
                response.sendRedirect(request.getContextPath() + "/numeros-oficiales");
                return;
            }
            // al editar se mantienen la fecha de carga y el administrador que lo cargó
            numero.setNumero_telefono(telefono);
            numero.setNombre_entidad(nombreEntidad);
            numero.setPais_prefijo(prefijo);
        } else {
            // al crear: fecha de hoy y el administrador que inició sesión
            numero = new Numero_oficial(0, telefono, nombreEntidad, LocalDate.now(), prefijo, UtilWeb.adminLogueado(request));
        }

        String error = null;
        if (telefono.isEmpty() || nombreEntidad.isEmpty() || idPrefijo == null) {
            error = "Todos los campos son obligatorios";
        } else if (!esEdicion && Numero_oficialDAO.existeNumeroOficial(telefono)) {
            error = "Ya existe un número oficial registrado con ese número";
        } else if (esEdicion && dao.existeNumeroOficialExcluyendoId(telefono, numero.getId_numero_oficial())) {
            error = "Ya existe otro número oficial registrado con ese número";
        }

        if (error != null) {
            request.setAttribute("error", error);
            mostrarFormulario(request, response, numero);
            return;
        }

        boolean ok = esEdicion ? dao.editarNumOficial(numero) : dao.registrarNumOficial(numero);

        if (ok) {
            UtilWeb.mensajeExito(request, esEdicion ? "Número oficial editado con éxito" : "Número oficial registrado con éxito");
        } else {
            UtilWeb.mensajeError(request, esEdicion ? "No se pudo editar el número oficial" : "No se pudo registrar el número oficial");
        }
        response.sendRedirect(request.getContextPath() + "/numeros-oficiales");
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Integer id = UtilWeb.entero(request, "id");

        if (id != null && new Numero_oficialDAO().eliminarNumOficial(id)) {
            UtilWeb.mensajeExito(request, "Número oficial eliminado con éxito");
        } else {
            UtilWeb.mensajeError(request, "No se pudo eliminar el número oficial");
        }
        response.sendRedirect(request.getContextPath() + "/numeros-oficiales");
    }
}
