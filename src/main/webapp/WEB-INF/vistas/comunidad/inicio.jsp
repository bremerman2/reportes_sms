<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Comunidad" />
<c:set var="menuActivo" value="comunidad" />
<%@ include file="/WEB-INF/vistas/layout/usuario-inicio.jspf" %>

<p class="as-ruta"><a href="${ctx}/inicio">Inicio</a><span class="as-sep">/</span>Comunidad</p>

<div class="as-titulo">
    <div>
        <h1>Comunidad</h1>
        <p class="as-descripcion">Reportes completos con sus comentarios. Compartí si recibiste un mensaje parecido; los reportes nuevos quedan pendientes de revisión.</p>
    </div>
    <a class="as-boton as-boton-primario" href="${ctx}/reportar">+ Reportar SMS</a>
</div>

<%-- Búsqueda por número o por texto del SMS --%>
<form class="as-filtros" action="${ctx}/comunidad" method="get" role="search">
    <div class="as-campo as-campo-ancho">
        <label for="q">Buscar por número o texto del SMS</label>
        <input type="search" id="q" name="q" maxlength="100"
               placeholder="Ej.: 91 204 o cuenta bloqueada" value="<c:out value='${busqueda}' />">
    </div>
    <div class="as-filtros-acciones">
        <button class="as-boton as-boton-secundario" type="submit">Buscar</button>
        <a class="as-link-rojo" href="${ctx}/comunidad">Limpiar</a>
    </div>
</form>

<c:if test="${not empty busqueda}">
    <div class="as-resultados-cabecera">
        <strong>${reportesComunidad.size()} ${reportesComunidad.size() == 1 ? 'resultado' : 'resultados'} para “<c:out value="${busqueda}" />”</strong>
        <span>Orden: Fecha, más recientes primero</span>
    </div>
</c:if>

<c:if test="${empty reportesComunidad}">
    <div class="as-vacio">
        <c:choose>
            <c:when test="${not empty busqueda}">
                <h2>No hay reportes que coincidan</h2>
                <p>Probá con otra palabra del mensaje o con parte del número, o tocá Limpiar para ver todos.</p>
            </c:when>
            <c:otherwise>
                <h2>Todavía no hay reportes</h2>
                <p>Sé la primera persona en compartir un SMS sospechoso con la comunidad.</p>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<div class="as-muro">
    <c:forEach var="r" items="${reportesComunidad}">
        <%@ include file="/WEB-INF/vistas/comunidad/tarjeta-reporte.jspf" %>
    </c:forEach>
</div>
<%@ include file="/WEB-INF/vistas/layout/usuario-fin.jspf" %>
