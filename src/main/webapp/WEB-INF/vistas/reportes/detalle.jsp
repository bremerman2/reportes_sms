<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Reporte #${reporte.idReporte}" />
<c:set var="menuActivo" value="reportes" />

<%@ include file="/WEB-INF/vistas/layout/admin-inicio.jspf" %>

<c:url var="urlListado" value="/reportes">
    <c:if test="${not empty estadoFiltro}"><c:param name="estado" value="${estadoFiltro}" /></c:if>
</c:url>

<div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
    <div>
        <h1 class="h3 fw-bold mb-1">Reporte #${reporte.idReporte}</h1>
        <p class="text-body-secondary mb-0"><c:out value="${reporte.numeroTelefono}" /></p>
    </div>
    <a href="${urlListado}" class="btn btn-outline-secondary">&larr; Volver al listado</a>
</div>

<div class="card border-0 shadow-sm mb-4">
    <div class="card-body">
        <dl class="row mb-0">
            <dt class="col-sm-4 col-lg-3">Estado</dt>
            <dd class="col-sm-8 col-lg-9">
                <c:choose>
                    <c:when test="${reporte.estado == 'confirmado'}"><span class="badge text-bg-danger">Confirmado</span></c:when>
                    <c:when test="${reporte.estado == 'falso_positivo'}"><span class="badge text-bg-secondary">Falso positivo</span></c:when>
                    <c:otherwise><span class="badge text-bg-warning">Pendiente</span></c:otherwise>
                </c:choose>
            </dd>
            <dt class="col-sm-4 col-lg-3">Fecha</dt>
            <dd class="col-sm-8 col-lg-9"><c:out value="${reporte.fechaTexto}" /></dd>
            <dt class="col-sm-4 col-lg-3">Número reportado</dt>
            <dd class="col-sm-8 col-lg-9">
                <c:out value="${reporte.numeroTelefono}" />
                <span class="text-body-secondary">(${reporte.cantidadReportesNumero} ${reporte.cantidadReportesNumero == 1 ? 'reporte' : 'reportes'})</span>
            </dd>
            <dt class="col-sm-4 col-lg-3">Reportado por</dt>
            <dd class="col-sm-8 col-lg-9"><c:out value="${reporte.nombreUsuario}" /> <span class="text-body-secondary">(ID ${reporte.idUsuario})</span></dd>
        </dl>

        <h2 class="h6 fw-bold mt-4 mb-2">Contenido del SMS</h2>
        <p class="border p-3 mb-0" style="white-space: pre-wrap; overflow-wrap: anywhere;"><c:out value="${reporte.cuerpoSms}" /></p>

        <c:if test="${not empty reporte.capturaPantalla}">
            <h2 class="h6 fw-bold mt-4 mb-2">Captura del SMS</h2>
            <a href="<c:out value='${reporte.capturaPantalla}' />" target="_blank" rel="noopener noreferrer">
                <img src="<c:out value='${reporte.capturaPantalla}' />" alt="Captura del SMS reportado"
                     class="img-fluid border" style="max-height: 420px;" loading="lazy">
            </a>
        </c:if>

        <c:if test="${reporte.estado != 'falso_positivo'}">
            <div class="d-flex flex-wrap gap-2 mt-4 pt-3 border-top">
                <c:if test="${reporte.estado == 'pendiente'}">
                    <form action="${ctx}/reportes" method="post" class="d-inline">
                        <input type="hidden" name="accion" value="confirmar">
                        <input type="hidden" name="id" value="${reporte.idReporte}">
                        <input type="hidden" name="origen" value="detalle">
                        <input type="hidden" name="estadoFiltro" value="<c:out value='${estadoFiltro}' />">
                        <button type="submit" class="btn btn-outline-danger">Confirmar</button>
                    </form>
                </c:if>
                <form action="${ctx}/reportes" method="post" class="d-inline">
                    <input type="hidden" name="accion" value="falso-positivo">
                    <input type="hidden" name="id" value="${reporte.idReporte}">
                    <input type="hidden" name="origen" value="detalle">
                    <input type="hidden" name="estadoFiltro" value="<c:out value='${estadoFiltro}' />">
                    <button type="submit" class="btn btn-outline-secondary">Falso positivo</button>
                </form>
            </div>
        </c:if>
    </div>
</div>

<div class="card border-0 shadow-sm">
    <div class="card-body">
        <h2 class="h6 fw-bold mb-3">Comentarios (${reporte.cantidadComentarios})</h2>
        <c:forEach var="com" items="${reporte.comentarios}">
            <div class="border-bottom pb-2 mb-2">
                <div class="d-flex justify-content-between gap-2 small">
                    <strong><c:out value="${com.nombreUsuario}" /></strong>
                    <span class="text-body-secondary"><c:out value="${com.fechaTexto}" /></span>
                </div>
                <p class="mb-0" style="overflow-wrap: anywhere;"><c:out value="${com.comentario}" /></p>
            </div>
        </c:forEach>
        <c:if test="${empty reporte.comentarios}">
            <p class="text-body-secondary mb-0">Este reporte no tiene comentarios.</p>
        </c:if>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/admin-fin.jspf" %>
