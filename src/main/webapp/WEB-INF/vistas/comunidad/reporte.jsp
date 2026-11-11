<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Reporte" />
<c:set var="menuActivo" value="comunidad" />
<%@ include file="/WEB-INF/vistas/layout/usuario-inicio.jspf" %>


<div class="as-titulo">
    <div>
        <h1>Reporte de <c:out value="${reporte.numeroTelefono}" /></h1>
        <p class="as-descripcion">Detalle del SMS reportado y los comentarios de la comunidad.</p>
    </div>
    <div class="as-titulo-acciones">
        <a class="as-boton as-boton-gris" href="${ctx}/inicio"
           onclick="if (history.length > 1) { history.back(); return false; }">← Volver</a>
    </div>
</div>

<div class="as-muro">
    <c:set var="r" value="${reporte}" />
    <c:set var="vistaReporte" value="${true}" />
    <%@ include file="/WEB-INF/vistas/comunidad/tarjeta-reporte.jspf" %>
</div>
<%@ include file="/WEB-INF/vistas/layout/usuario-fin.jspf" %>
