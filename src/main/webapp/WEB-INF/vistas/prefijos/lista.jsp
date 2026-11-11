<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Prefijos" />
<c:set var="menuActivo" value="prefijos" />

<%@ include file="/WEB-INF/vistas/layout/admin-inicio.jspf" %>

<div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
    <div>
        <h1 class="h3 fw-bold mb-0">Prefijos</h1>
    </div>
    <a href="${ctx}/prefijos?accion=nuevo" class="btn btn-primary">+ Nuevo prefijo</a>
</div>

<%-- Búsqueda --%>
<form action="${ctx}/prefijos" method="get" class="d-flex flex-wrap align-items-end gap-2 mb-3" role="search">
    <div class="flex-grow-1" style="max-width: 420px;">
        <label for="q" class="form-label fw-semibold mb-1">Buscar por país o prefijo</label>
        <input type="search" class="form-control" id="q" name="q" maxlength="100"
               placeholder="Ej.: Uruguay o 598" value="<c:out value='${busqueda}' />">
    </div>
    <button type="submit" class="btn btn-outline-primary">Buscar</button>
    <c:if test="${not empty busqueda}">
        <a href="${ctx}/prefijos" class="btn btn-link">Limpiar</a>
    </c:if>
</form>

<div class="card border-0 shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-striped table-hover align-middle mb-0 as-tabla-cards">
                <thead class="table-light">
                <tr>
                    <th scope="col" class="ps-4">ID</th>
                    <th scope="col">Prefijo</th>
                    <th scope="col">País</th>
                    <th scope="col" class="text-end pe-4">Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${listaPrefijos}">
                    <tr>
                        <td class="ps-4 text-body-secondary" data-label="ID"><c:out value="${p.id_prefijo}" /></td>
                        <td class="fw-medium" data-label="Prefijo"><c:out value="${p.prefijo}" /></td>
                        <td class="as-celda-titulo"><c:out value="${p.nombre_pais}" /></td>
                        <td class="text-end pe-4 text-nowrap as-celda-acciones">
                            <a href="${ctx}/prefijos?accion=editar&id=${p.id_prefijo}" class="btn btn-outline-primary btn-sm">Editar</a>
                            <form action="${ctx}/prefijos" method="post" class="d-inline"
                                  onsubmit="return confirm('¿Eliminar el prefijo de ' + this.dataset.pais + '?');"
                                  data-pais="<c:out value='${p.nombre_pais}' />">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="id" value="${p.id_prefijo}">
                                <button type="submit" class="btn btn-outline-danger btn-sm">Eliminar</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty listaPrefijos}">
                    <tr>
                        <td colspan="4" class="text-center py-5">
                            <p class="fw-semibold mb-1">${empty busqueda ? 'Todavía no hay prefijos' : 'Ningún país coincide con la búsqueda'}</p>
                            <p class="text-body-secondary mb-0">${empty busqueda ? 'Creá el primero con el botón de arriba.' : 'Probá con otro nombre o prefijo.'}</p>
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
