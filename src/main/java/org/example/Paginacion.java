package org.example;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;

// Paginación y búsqueda que comparten todos los listados.
// Deja en el request lo que usan layout/paginacion-usuario.jspf y layout/paginacion-admin.jspf:
//   totalRegistros, paginaActual, totalPaginas, desdeRegistro, hastaRegistro y urlPagina
//   (la URL actual con todos sus filtros, terminada en "pagina=" para agregarle el número).
public final class Paginacion {

    public static final int POR_PAGINA = 10;

    private Paginacion() {
    }

    // Para listados que se paginan en SQL: recibe el total y devuelve el "desde" (OFFSET) de la página pedida
    public static int calcular(HttpServletRequest request, int total, int porPagina) {
        int totalPaginas = Math.max(1, (int) Math.ceil(total / (double) porPagina));
        Integer pagina = UtilWeb.entero(request, "pagina");
        int paginaActual = (pagina == null || pagina < 1) ? 1 : Math.min(pagina, totalPaginas);
        int desde = (paginaActual - 1) * porPagina;

        request.setAttribute("totalRegistros", total);
        request.setAttribute("paginaActual", paginaActual);
        request.setAttribute("totalPaginas", totalPaginas);
        request.setAttribute("desdeRegistro", total == 0 ? 0 : desde + 1);
        request.setAttribute("hastaRegistro", Math.min(desde + porPagina, total));
        request.setAttribute("urlPagina", urlSinPagina(request));
        return desde;
    }

    // Para listados que ya vienen completos (y filtrados) del DAO: devuelve solo la página pedida
    public static <T> List<T> paginar(HttpServletRequest request, List<T> lista) {
        int desde = calcular(request, lista.size(), POR_PAGINA);
        return lista.subList(desde, Math.min(desde + POR_PAGINA, lista.size()));
    }

    // URL actual con todos los parámetros menos "pagina", lista para agregarle el número
    private static String urlSinPagina(HttpServletRequest request) {
        StringBuilder url = new StringBuilder(request.getContextPath() + request.getServletPath() + "?");
        for (Map.Entry<String, String[]> parametro : request.getParameterMap().entrySet()) {
            if (parametro.getKey().equals("pagina")) continue;
            for (String valor : parametro.getValue()) {
                if (valor.isBlank()) continue;
                url.append(codificar(parametro.getKey())).append('=').append(codificar(valor)).append('&');
            }
        }
        return url.append("pagina=").toString();
    }

    private static String codificar(String texto) {
        return URLEncoder.encode(texto, StandardCharsets.UTF_8);
    }

    // ---------- Búsqueda ----------

    // true si el texto contiene la búsqueda, sin importar mayúsculas ni tildes ("peru" encuentra "Perú")
    public static boolean contiene(String texto, String busqueda) {
        if (busqueda == null || busqueda.isBlank()) return true;
        return normalizar(texto).contains(normalizar(busqueda.trim()));
    }

    // true si el número contiene los dígitos buscados, sin importar espacios, guiones ni "+"
    public static boolean contieneNumero(String numero, String busqueda) {
        String digitos = busqueda == null ? "" : busqueda.replaceAll("\\D", "");
        if (digitos.isEmpty()) return false;
        return numero != null && numero.replaceAll("\\D", "").contains(digitos);
    }

    private static String normalizar(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
    }
}
