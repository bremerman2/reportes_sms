package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet({"/inicio", ""})
public class InicioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        //usuario comun logueado
        if (session != null && session.getAttribute("usuarioLogueado") instanceof Usuario) {
            mostrarInicioUsuario(request, response);
            return;
        }

        //Administrador
        ReportesDAO reportesDAO = new ReportesDAO();
        request.setAttribute("reportesHoy", reportesDAO.contarReportesHoy());
        request.setAttribute("reportesPendientes", reportesDAO.contarReportesPendientes());
        request.setAttribute("estadisticas", reportesDAO.consultarEstadisticas());

        request.getRequestDispatcher("/WEB-INF/vistas/inicio.jsp").forward(request, response);
    }

    private static final List<String> ESTADOS = List.of("pendiente", "confirmado", "falso_positivo");

    // Inicio del usuario: botón "Reportar SMS" + listado de reportes recientes.
    // Se pueden elegir varios estados a la vez (?estado=pendiente&estado=confirmado).
    private void mostrarInicioUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numero = UtilWeb.texto(request, "numero");
        if (numero.length() > 20) numero = numero.substring(0, 20);

        // solo se aceptan los estados conocidos, sin repetir y en el orden de ESTADOS
        String[] pedidos = request.getParameterValues("estado");
        List<String> listaPedidos = pedidos == null ? List.of() : Arrays.asList(pedidos);
        List<String> estados = ESTADOS.stream().filter(listaPedidos::contains).toList();
        if (estados.size() == ESTADOS.size()) estados = List.of();   // los tres juntos es lo mismo que "Todos"

        ComunidadDAO dao = new ComunidadDAO();
        int total = dao.contarRecientes(numero, estados);
        int desde = Paginacion.calcular(request, total, Paginacion.POR_PAGINA);

        request.setAttribute("reportes", dao.listarRecientes(numero, estados, desde, Paginacion.POR_PAGINA));
        request.setAttribute("filtroNumero", numero);
        request.setAttribute("estadosElegidos", estados);
        request.setAttribute("segmentosEstado", armarSegmentos(request, numero, estados));
        request.setAttribute("hayFiltros", !numero.isEmpty() || !estados.isEmpty());
        request.getRequestDispatcher("/WEB-INF/vistas/usuarios/inicio.jsp").forward(request, response);
    }

    private static final Map<String, String> ETIQUETAS = Map.of(
            "pendiente", "Pendientes", "confirmado", "Confirmados", "falso_positivo", "Falsos positivos");

    // Botones del filtro de estado: "Todos" limpia la selección y cada estado se agrega o se quita
    // de la selección actual. Cada uno trae su URL ya armada (conserva el número buscado).
    private List<Map<String, Object>> armarSegmentos(HttpServletRequest request, String numero, List<String> elegidos) {
        List<Map<String, Object>> segmentos = new ArrayList<>();
        segmentos.add(Map.of("etiqueta", "Todos", "activo", elegidos.isEmpty(),
                "url", urlInicio(request, numero, List.of())));
        for (String estado : ESTADOS) {
            boolean activo = elegidos.contains(estado);
            List<String> nuevos = ESTADOS.stream()
                    .filter(e -> e.equals(estado) ? !activo : elegidos.contains(e))
                    .toList();
            if (nuevos.size() == ESTADOS.size()) nuevos = List.of();
            segmentos.add(Map.of("etiqueta", ETIQUETAS.get(estado), "activo", activo,
                    "url", urlInicio(request, numero, nuevos)));
        }
        return segmentos;
    }

    private String urlInicio(HttpServletRequest request, String numero, List<String> estados) {
        StringBuilder url = new StringBuilder(request.getContextPath() + "/inicio");
        String separador = "?";
        if (!numero.isEmpty()) {
            url.append(separador).append("numero=").append(URLEncoder.encode(numero, StandardCharsets.UTF_8));
            separador = "&";
        }
        for (String estado : estados) {
            url.append(separador).append("estado=").append(estado);
            separador = "&";
        }
        return url.toString();
    }
}
