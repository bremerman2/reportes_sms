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

<%-- Búsqueda por número o texto del SMS + filtro por estado --%>
<form action="${ctx}/reportes" method="get" class="d-flex flex-wrap align-items-end gap-2 mb-3" role="search">
    <div class="flex-grow-1" style="max-width: 420px;">
        <label for="q" class="form-label fw-semibold mb-1">Buscar por número o texto del SMS</label>
        <input type="search" class="form-control" id="q" name="q" maxlength="100"
               placeholder="Ej.: 91 204 o cuenta bloqueada" value="<c:out value='${busqueda}' />">
    </div>
    <div>
        <label for="estado" class="form-label fw-semibold mb-1">Estado</label>
        <select class="form-select" id="estado" name="estado" onchange="this.form.submit()">
            <option value="">Todos</option>
            <c:forEach var="e" items="${estados}">
                <option value="${e}" ${estadoFiltro == e.name() ? 'selected' : ''}>${e}</option>
            </c:forEach>
        </select>
    </div>
    <button type="submit" class="btn btn-outline-primary">Buscar</button>
    <c:if test="${not empty busqueda or not empty estadoFiltro}">
        <a href="${ctx}/reportes" class="btn btn-link">Limpiar</a>
    </c:if>
</form>

<div class="card border-0 shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-striped table-hover align-middle mb-0 as-tabla-cards as-tabla-reportes">
                <thead class="table-light">
                <tr>
                    <th scope="col" class="ps-4">Reporte</th>
                    <th scope="col">Número</th>
                    <th scope="col">Reportado por</th>
                    <th scope="col">SMS</th>
                    <th scope="col">Estado</th>
                    <th scope="col" class="text-end pe-4">Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="r" items="${listaReportes}">
                    <c:url var="urlDetalle" value="/reportes">
                        <c:param name="accion" value="ver" />
                        <c:param name="id" value="${r.idReporte}" />
                        <c:if test="${not empty estadoFiltro}"><c:param name="estadoFiltro" value="${estadoFiltro}" /></c:if>
                    </c:url>
                    <tr>
                        <td class="ps-4 as-celda-titulo">
                            <a class="fw-semibold" href="${urlDetalle}" title="Ver reporte completo">#${r.idReporte}</a>
                            <div class="small text-body-secondary text-nowrap"><c:out value="${r.fechaTexto}" /></div>
                        </td>
                        <td data-label="Número">
                            <span class="text-nowrap"><c:out value="${r.numeroTelefono}" /></span>
                            <c:url var="urlMismoNumero" value="/reportes"><c:param name="q" value="${r.numeroTelefono}" /></c:url>
                            <div class="small"><a class="text-body-secondary" href="${urlMismoNumero}" title="Ver todos los reportes de este número">${r.cantidadReportesNumero} ${r.cantidadReportesNumero == 1 ? 'reporte' : 'reportes'}</a></div>
                        </td>
                        <td data-label="Reportado por">
                            <a href="${ctx}/usuarios?accion=ver&amp;id=${r.idUsuario}" title="Ver perfil del usuario"><c:out value="${r.nombreUsuario}" /></a>
                        </td>
                        <td class="as-celda-bloque as-celda-sms" data-label="SMS">
                            <a class="as-sms-resumen" href="${urlDetalle}"><c:out value="${r.cuerpoSms}" /></a>
                        </td>
                        <td data-label="Estado">
                            <c:choose>
                                <c:when test="${r.estado == 'confirmado'}"><span class="badge text-bg-danger">Confirmado</span></c:when>
                                <c:when test="${r.estado == 'falso_positivo'}"><span class="badge text-bg-secondary">Falso positivo</span></c:when>
                                <c:otherwise><span class="badge text-bg-warning">Pendiente</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-end pe-4 as-celda-acciones">
                            <div class="d-flex flex-column align-items-stretch gap-1">
                                <a href="${urlDetalle}" class="btn btn-outline-primary btn-sm">Ver</a>
                                <c:if test="${r.estado == 'pendiente'}">
                                    <form action="${ctx}/reportes" method="post" class="m-0">
                                        <input type="hidden" name="accion" value="confirmar">
                                        <input type="hidden" name="id" value="${r.idReporte}">
                                        <input type="hidden" name="estadoFiltro" value="${estadoFiltro}">
                                        <button type="submit" class="btn btn-outline-danger btn-sm w-100">Confirmar</button>
                                    </form>
                                </c:if>
                                <c:if test="${r.estado != 'falso_positivo'}">
                                    <form action="${ctx}/reportes" method="post" class="m-0">
                                        <input type="hidden" name="accion" value="falso-positivo">
                                        <input type="hidden" name="id" value="${r.idReporte}">
                                        <input type="hidden" name="estadoFiltro" value="${estadoFiltro}">
                                        <button type="submit" class="btn btn-outline-secondary btn-sm w-100 text-nowrap">Falso positivo</button>
                                    </form>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty listaReportes}">
                    <tr>
                        <td colspan="6" class="text-center py-5">
                            <p class="fw-semibold mb-0">No se encontraron reportes</p>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/paginacion-admin.jspf" %>

<div class="mb-4"></div>

<%-- Estadísticas: lo que antes imprimía consultarEstadisticas() --%>
<div class="card border-0 shadow-sm">
    <div class="card-header bg-white py-3">
        <h2 class="h6 fw-bold mb-0">Números más reportados (top 10)</h2>
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
                <c:forEach var="e" items="${estadisticas}" end="9">
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
