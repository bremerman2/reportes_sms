<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Reportes" />
<c:set var="menuActivo" value="reportes" />

<%@ include file="/WEB-INF/vistas/layout/admin-inicio.jspf" %>

<div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
    <div>
        <h1 class="h3 fw-bold mb-1">Reportes</h1>
        <p class="text-body-secondary mb-0">Reportes de SMS sospechosos enviados por los usuarios.</p>
    </div>
    <a href="${ctx}/reportes?accion=nuevo" class="btn btn-primary">+ Registrar reporte</a>
</div>

<%-- Filtro por estado --%>
<form action="${ctx}/reportes" method="get" class="d-flex flex-wrap align-items-end gap-2 mb-3">
    <div>
        <label for="estado" class="form-label fw-semibold mb-1">Estado</label>
        <select class="form-select" id="estado" name="estado" onchange="this.form.submit()">
            <option value="">Todos</option>
            <c:forEach var="e" items="${estados}">
                <option value="${e}" ${estadoFiltro == e.name() ? 'selected' : ''}>${e}</option>
            </c:forEach>
        </select>
    </div>
    <noscript><button type="submit" class="btn btn-outline-secondary">Filtrar</button></noscript>
</form>

<div class="card border-0 shadow-sm mb-4">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-striped table-hover align-middle mb-0 as-tabla-cards">
                <thead class="table-light">
                <tr>
                    <th scope="col" class="ps-4">ID</th>
                    <th scope="col">Fecha</th>
                    <th scope="col">Estado</th>
                    <th scope="col">Manual</th>
                    <th scope="col">Usuario</th>
                    <th scope="col">Nº reportado</th>
                    <th scope="col">SMS</th>
                    <th scope="col" class="text-end pe-4">Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="r" items="${listaReportes}">
                    <c:url var="urlDetalle" value="/reportes">
                        <c:param name="accion" value="ver" />
                        <c:param name="id" value="${r.id_reporte}" />
                        <c:if test="${not empty estadoFiltro}"><c:param name="estadoFiltro" value="${estadoFiltro}" /></c:if>
                    </c:url>
                    <tr>
                        <td class="ps-4" data-label="ID"><a class="fw-semibold" href="${urlDetalle}" title="Ver reporte completo">#<c:out value="${r.id_reporte}" /></a></td>
                        <td class="text-nowrap" data-label="Fecha"><c:out value="${r.fecha_hora_texto}" /></td>
                        <td data-label="Estado">
                            <c:choose>
                                <c:when test="${r.estado_reporte == 'CONFIRMADO'}"><span class="badge text-bg-danger">Confirmado</span></c:when>
                                <c:when test="${r.estado_reporte == 'PENDIENTE'}"><span class="badge text-bg-warning">Pendiente</span></c:when>
                                <c:otherwise><span class="badge text-bg-secondary">Falso positivo</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td data-label="Manual">${r.es_manual ? 'Sí' : 'No'}</td>
                        <td data-label="Usuario"><c:out value="${r.usuario.id_usuario}" /></td>
                        <td data-label="Nº reportado"><c:out value="${r.numero_reportado.id_numero_reportado}" /></td>
                        <td class="as-celda-bloque as-celda-sms" data-label="SMS">
                            <a class="as-sms-resumen" href="${urlDetalle}" title="<c:out value='${r.cuerpo_sms}' />"><c:out value="${r.cuerpo_sms}" /></a>
                        </td>
                        <td class="text-end pe-4 text-nowrap as-celda-acciones">
                            <a href="${urlDetalle}" class="btn btn-outline-primary btn-sm">Ver</a>
                            <c:if test="${r.estado_reporte == 'PENDIENTE'}">
                                <form action="${ctx}/reportes" method="post" class="d-inline">
                                    <input type="hidden" name="accion" value="confirmar">
                                    <input type="hidden" name="id" value="${r.id_reporte}">
                                    <input type="hidden" name="estadoFiltro" value="${estadoFiltro}">
                                    <button type="submit" class="btn btn-outline-danger btn-sm">Confirmar</button>
                                </form>
                            </c:if>
                            <c:if test="${r.estado_reporte != 'FALSO_POSITIVO'}">
                                <form action="${ctx}/reportes" method="post" class="d-inline">
                                    <input type="hidden" name="accion" value="falso-positivo">
                                    <input type="hidden" name="id" value="${r.id_reporte}">
                                    <input type="hidden" name="estadoFiltro" value="${estadoFiltro}">
                                    <button type="submit" class="btn btn-outline-secondary btn-sm">Falso positivo</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty listaReportes}">
                    <tr>
                        <td colspan="8" class="text-center py-5">
                            <p class="fw-semibold mb-0">No se encontraron reportes</p>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%-- Estadísticas: lo que antes imprimía consultarEstadisticas() --%>
<div class="card border-0 shadow-sm">
    <div class="card-header bg-white py-3">
        <h2 class="h6 fw-bold mb-0">Números más reportados</h2>
    </div>
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table align-middle mb-0 as-tabla-cards">
                <thead class="table-light">
                <tr>
                    <th scope="col" class="ps-4">Número</th>
                    <th scope="col" class="text-end pe-4">Cantidad de reportes</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="e" items="${estadisticas}">
                    <tr>
                        <td class="ps-4 fw-medium as-celda-titulo"><c:out value="${e.numero_telefono}" /></td>
                        <td class="text-end pe-4" data-label="Cantidad de reportes"><c:out value="${e.cantidad_reportes}" /></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty estadisticas}">
                    <tr><td colspan="2" class="text-center text-body-secondary py-4">Todavía no hay datos</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/admin-fin.jspf" %>
