<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="u" value="${usuarioPerfil}" />
<c:set var="tituloPagina" value="${u.nombre} ${u.apellido}" />
<c:set var="menuActivo" value="usuarios" />

<%@ include file="/WEB-INF/vistas/layout/admin-inicio.jspf" %>

<p class="text-body-secondary small mb-1"><a href="${ctx}/usuarios" class="text-reset">Usuarios</a> / Perfil</p>
<div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
    <div>
        <h1 class="h3 fw-bold mb-1">
            <c:out value="${u.nombre} ${u.apellido}" />
            <c:choose>
                <c:when test="${u.bloqueado}"><span class="badge text-bg-danger align-middle fs-6">Bloqueado</span></c:when>
                <c:otherwise><span class="badge text-bg-success align-middle fs-6">Activo</span></c:otherwise>
            </c:choose>
        </h1>
        <p class="text-body-secondary mb-0">Usuario #${u.id_usuario}</p>
    </div>
    <a href="${ctx}/usuarios" class="btn btn-outline-secondary">&larr; Volver a usuarios</a>
</div>

<%-- Datos del usuario --%>
<div class="card border-0 shadow-sm mb-4">
    <div class="card-body">
        <dl class="row mb-0">
            <dt class="col-sm-3">Email</dt><dd class="col-sm-9" style="overflow-wrap: anywhere;"><c:out value="${u.email}" /></dd>
            <dt class="col-sm-3">Teléfono</dt><dd class="col-sm-9"><c:out value="${u.telefono}" /></dd>
            <dt class="col-sm-3">Reportes realizados</dt><dd class="col-sm-9">${totalRegistros}${not empty filtroEstado ? ' (con el filtro aplicado)' : ''}</dd>
        </dl>
        <div class="d-flex flex-wrap gap-2 mt-3 pt-3 border-top">
            <c:if test="${not u.bloqueado}">
                <form action="${ctx}/usuarios" method="post" class="m-0"
                      onsubmit="return confirm('¿Desea bloquear este usuario?');">
                    <input type="hidden" name="accion" value="bloquear">
                    <input type="hidden" name="id" value="${u.id_usuario}">
                    <input type="hidden" name="origen" value="perfil">
                    <button type="submit" class="btn btn-outline-warning btn-sm">Bloquear</button>
                </form>
            </c:if>
            <form action="${ctx}/usuarios" method="post" class="m-0"
                  onsubmit="return confirm('¿Está seguro que desea eliminarlo?');">
                <input type="hidden" name="accion" value="eliminar">
                <input type="hidden" name="id" value="${u.id_usuario}">
                <button type="submit" class="btn btn-outline-danger btn-sm">Eliminar</button>
            </form>
        </div>
    </div>
</div>

<%-- Reportes realizados por el usuario --%>
<div class="d-flex flex-wrap justify-content-between align-items-end gap-2 mb-3">
    <h2 class="h5 fw-bold mb-0">Reportes realizados</h2>
    <form action="${ctx}/usuarios" method="get" class="d-flex align-items-end gap-2">
        <input type="hidden" name="accion" value="ver">
        <input type="hidden" name="id" value="${u.id_usuario}">
        <div>
            <label for="estado" class="form-label fw-semibold mb-1 small">Estado</label>
            <select class="form-select form-select-sm" id="estado" name="estado" onchange="this.form.submit()">
                <option value="">Todos</option>
                <option value="pendiente" ${filtroEstado == 'pendiente' ? 'selected' : ''}>Pendiente</option>
                <option value="confirmado" ${filtroEstado == 'confirmado' ? 'selected' : ''}>Confirmado</option>
                <option value="falso_positivo" ${filtroEstado == 'falso_positivo' ? 'selected' : ''}>Falso positivo</option>
            </select>
        </div>
        <noscript><button type="submit" class="btn btn-outline-secondary btn-sm">Filtrar</button></noscript>
    </form>
</div>

<div class="card border-0 shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-striped table-hover align-middle mb-0 as-tabla-cards as-tabla-reportes">
                <thead class="table-light">
                <tr>
                    <th scope="col" class="ps-4">Reporte</th>
                    <th scope="col">Número</th>
                    <th scope="col">SMS</th>
                    <th scope="col">Estado</th>
                    <th scope="col" class="text-end pe-4">Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="r" items="${reportesUsuario}">
                    <tr>
                        <td class="ps-4 as-celda-titulo">
                            <a class="fw-semibold" href="${ctx}/reportes?accion=ver&amp;id=${r.idReporte}">#${r.idReporte}</a>
                            <div class="small text-body-secondary text-nowrap"><c:out value="${r.fechaTexto}" /></div>
                        </td>
                        <td data-label="Número" class="text-nowrap"><c:out value="${r.numeroTelefono}" /></td>
                        <td class="as-celda-bloque as-celda-sms" data-label="SMS">
                            <a class="as-sms-resumen" href="${ctx}/reportes?accion=ver&amp;id=${r.idReporte}"><c:out value="${r.cuerpoSms}" /></a>
                        </td>
                        <td data-label="Estado">
                            <c:choose>
                                <c:when test="${r.estado == 'confirmado'}"><span class="badge text-bg-danger">Confirmado</span></c:when>
                                <c:when test="${r.estado == 'falso_positivo'}"><span class="badge text-bg-secondary">Falso positivo</span></c:when>
                                <c:otherwise><span class="badge text-bg-warning">Pendiente</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-end pe-4 as-celda-acciones">
                            <a href="${ctx}/reportes?accion=ver&amp;id=${r.idReporte}" class="btn btn-outline-primary btn-sm">Ver</a>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty reportesUsuario}">
                    <tr>
                        <td colspan="5" class="text-center py-5">
                            <p class="fw-semibold mb-0">${empty filtroEstado ? 'Este usuario todavía no hizo reportes' : 'No hay reportes con ese estado'}</p>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/paginacion-admin.jspf" %>

<%@ include file="/WEB-INF/vistas/layout/admin-fin.jspf" %>
