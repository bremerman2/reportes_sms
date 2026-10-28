<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="tituloPagina" value="Mis Notificaciones" />
<c:set var="menuActivo" value="notificaciones" />
<%@ include file="/WEB-INF/vistas/layout/usuario-inicio.jspf" %>

<p class="as-ruta"><a href="${ctx}/inicio">Inicio</a><span class="as-sep">/</span>Notificaciones</p>

<div class="as-titulo">
    <div>
        <h1>Mis Notificaciones</h1>
        <p class="as-descripcion">Avisos y novedades sobre los reportes a los que estás suscripto.</p>
    </div>
</div>

<div class="as-muro">
    <c:if test="${empty notificaciones}">
        <div class="as-vacio">
            <h2>No tenés notificaciones</h2>
            <p>Cuando alguien comente en un reporte que seguís, el aviso aparecerá acá.</p>
        </div>
    </c:if>

    <c:forEach var="n" items="${notificaciones}">
        <article class="as-tarjeta as-tarjeta-notificacion">
            <div class="as-tarjeta-cabecera">
                <div>
                    <h3 class="as-subtitulo" style="margin-bottom: 5px;">Nuevo aviso</h3>
                    <p class="as-tarjeta-meta">
                        <fmt:formatDate value="${n.fecha_creacion}" pattern="dd/MM/yyyy HH:mm" />
                    </p>
                </div>
            </div>

            <p class="as-sms"><c:out value="${n.mensaje}" /></p>

            <div style="margin-top: 15px;">
                <a class="as-boton as-boton-secundario" href="<c:out value='${n.enlace}' />">Ver reporte</a>
            </div>
        </article>
    </c:forEach>
</div>

<%@ include file="/WEB-INF/vistas/layout/usuario-fin.jspf" %>