<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Reporte #${reporte.idReporte}" />
<c:set var="menuActivo" value="reportes" />

<%@ include file="/WEB-INF/vistas/layout/admin-inicio.jspf" %>

<%-- urlListado lo arma ReporteServlet con el estado, período y fecha con que se venía filtrando --%>

<div class="as-detalle">

    <%-- Encabezado: título y estado --%>
    <div class="as-detalle-cabecera">
        <div>
            <h1 class="as-detalle-titulo">Reporte #${reporte.idReporte}</h1>
        </div>
        <c:choose>
            <c:when test="${reporte.estado == 'confirmado'}"><span class="as-pastilla as-pastilla-confirmado">Confirmado</span></c:when>
            <c:when test="${reporte.estado == 'falso_positivo'}"><span class="as-pastilla as-pastilla-falso">Falso positivo</span></c:when>
            <c:otherwise><span class="as-pastilla as-pastilla-pendiente">Pendiente</span></c:otherwise>
        </c:choose>
    </div>

    <div class="as-detalle-columnas">

        <%-- 1 · Información del reporte --%>
        <section class="as-detalle-panel" aria-labelledby="titulo-info">
            <h2 id="titulo-info" class="as-detalle-panel-titulo">Información del reporte</h2>

            <dl class="as-detalle-datos">
                <dt>Número reportado</dt>
                <%-- El número y la pastilla llevan al listado de reportes filtrado por ese número --%>
                <c:url var="urlMismoNumero" value="/reportes"><c:param name="q" value="${reporte.numeroTelefono}" /><c:param name="fecha" value="" /></c:url>
                <dd class="as-detalle-numero">
                    <a class="as-link-numero" href="${urlMismoNumero}" title="Ver todos los reportes de este número"><c:out value="${reporte.numeroTelefono}" /></a>
                    <a class="as-pastilla as-pastilla-roja as-pastilla-link" href="${urlMismoNumero}">${reporte.cantidadReportesNumero} ${reporte.cantidadReportesNumero == 1 ? 'reporte' : 'reportes'}</a>
                </dd>
                <dt>Reportado por</dt>
                <dd><a href="${ctx}/usuarios?accion=ver&amp;id=${reporte.idUsuario}"><c:out value="${reporte.nombreUsuario}" /></a></dd>
                <dt>Fecha</dt>
                <dd><c:out value="${reporte.fechaLarga}" /></dd>
            </dl>

            <hr class="as-detalle-separador">

            <h2 class="as-detalle-panel-titulo">Contenido del SMS</h2>
            <div class="as-detalle-sms">
                <p class="as-detalle-sms-etiqueta">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                        <path d="M21 11.5a8.4 8.4 0 0 1-9 8.4 9 9 0 0 1-4-.9L3 20l1.1-4A8.4 8.4 0 0 1 12 3.1a8.4 8.4 0 0 1 9 8.4z"/>
                    </svg>
                    Mensaje denunciado
                </p>
                <%-- Los enlaces se resaltan pero no se pueden abrir con un clic: son del SMS sospechoso --%>
                <p class="as-detalle-sms-texto"><c:forEach var="parte" items="${reporte.partesSms}"><c:choose><c:when test="${parte.enlace}"><span class="as-enlace-sospechoso" title="Enlace del SMS (no se abre desde acá)"><c:out value="${parte.texto}" /></span></c:when><c:otherwise><c:out value="${parte.texto}" /></c:otherwise></c:choose></c:forEach></p>
            </div>

            <c:if test="${reporte.tieneEnlace}">
                <p class="as-detalle-aviso">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                        <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
                    </svg>
                    <span>El mensaje contiene un enlace externo. No abrirlo sin verificar su destino.</span>
                </p>
            </c:if>

            <c:if test="${not empty reporte.capturaPantalla}">
                <h2 class="as-detalle-panel-titulo mt-4">Captura del SMS</h2>
                <a href="<c:out value='${reporte.capturaPantalla}' />" target="_blank" rel="noopener noreferrer">
                    <img src="<c:out value='${reporte.capturaPantalla}' />" alt="Captura del SMS reportado"
                         class="as-detalle-captura" loading="lazy">
                </a>
            </c:if>
        </section>

        <%-- 2 · Comentarios de usuarios --%>
        <section class="as-detalle-panel" aria-labelledby="titulo-comentarios">
            <div class="as-detalle-panel-cabecera">
                <h2 id="titulo-comentarios" class="as-detalle-panel-titulo">Comentarios de usuarios</h2>
                <span class="as-contador">${reporte.cantidadComentarios}</span>
            </div>

            <c:forEach var="com" items="${reporte.comentarios}">
                <div class="as-detalle-comentario">
                    <div class="as-detalle-comentario-cabecera">
                        <strong><c:out value="${com.nombreUsuario}" /></strong>
                        <span><c:out value="${com.fechaTexto}" /></span>
                    </div>
                    <p><c:out value="${com.comentario}" /></p>
                </div>
            </c:forEach>
            <c:if test="${empty reporte.comentarios}">
                <p class="text-body-secondary mb-0">Este reporte no tiene comentarios.</p>
            </c:if>
        </section>
    </div>

    <%-- Acciones --%>
    <div class="as-detalle-acciones">
        <a href="${urlListado}" class="btn btn-outline-secondary">&larr; Volver al listado</a>
        <c:if test="${reporte.estado != 'falso_positivo'}">
            <div class="d-flex flex-wrap gap-2">
                <form action="${ctx}/reportes" method="post" class="m-0">
                    <input type="hidden" name="accion" value="falso-positivo">
                    <input type="hidden" name="id" value="${reporte.idReporte}">
                    <input type="hidden" name="origen" value="detalle">
                    <input type="hidden" name="estadoFiltro" value="<c:out value='${estadoFiltro}' />">
                    <c:if test="${fechaFiltro != null}">
                        <input type="hidden" name="periodoFiltro" value="<c:out value='${periodoFiltro}' />">
                        <input type="hidden" name="fechaFiltro" value="<c:out value='${fechaFiltro}' />">
                    </c:if>
                    <button type="submit" class="btn btn-outline-secondary">Marcar falso positivo</button>
                </form>
                <c:if test="${reporte.estado == 'pendiente'}">
                    <form action="${ctx}/reportes" method="post" class="m-0">
                        <input type="hidden" name="accion" value="confirmar">
                        <input type="hidden" name="id" value="${reporte.idReporte}">
                        <input type="hidden" name="origen" value="detalle">
                        <input type="hidden" name="estadoFiltro" value="<c:out value='${estadoFiltro}' />">
                        <c:if test="${fechaFiltro != null}">
                            <input type="hidden" name="periodoFiltro" value="<c:out value='${periodoFiltro}' />">
                            <input type="hidden" name="fechaFiltro" value="<c:out value='${fechaFiltro}' />">
                        </c:if>
                        <button type="submit" class="btn btn-primary">Confirmar reporte</button>
                    </form>
                </c:if>
            </div>
        </c:if>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/admin-fin.jspf" %>
