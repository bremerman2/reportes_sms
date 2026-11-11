<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Usuarios" />
<c:set var="menuActivo" value="usuarios" />

<%@ include file="/WEB-INF/vistas/layout/admin-inicio.jspf" %>

<h1 class="h3 fw-bold mb-4">Usuarios</h1>

<%-- Estadísticas de usuarios --%>
<div class="row g-3 mb-4">
    <div class="col-12 col-sm-4">
        <div class="card border-0 shadow-sm h-100"><div class="card-body">
            <div class="text-body-secondary small">Total registrados</div>
            <div class="fs-3 fw-bold">${totalUsuarios}</div>
        </div></div>
    </div>
    <div class="col-12 col-sm-4">
        <div class="card border-0 shadow-sm h-100"><div class="card-body">
            <div class="text-body-secondary small">Activos</div>
            <div class="fs-3 fw-bold text-success">${activos}</div>
        </div></div>
    </div>
    <div class="col-12 col-sm-4">
        <div class="card border-0 shadow-sm h-100"><div class="card-body">
            <div class="text-body-secondary small">Bloqueados</div>
            <div class="fs-3 fw-bold text-danger">${bloqueados}</div>
        </div></div>
    </div>
</div>

<%-- Buscar usuario por nombre --%>
<form action="${ctx}/usuarios" method="get" class="d-flex flex-wrap align-items-end gap-2 mb-3" role="search">
    <div class="flex-grow-1" style="max-width: 420px;">
        <label for="q" class="form-label fw-semibold mb-1">Buscar por nombre</label>
        <input type="search" class="form-control" id="q" name="q" maxlength="100"
               placeholder="Ej.: Agustina o Acosta" value="<c:out value='${busqueda}' />">
    </div>
    <button type="submit" class="btn btn-outline-primary">Buscar</button>
    <c:if test="${not empty busqueda}">
        <a href="${ctx}/usuarios" class="btn btn-link">Limpiar</a>
    </c:if>
</form>

<div class="card border-0 shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-striped table-hover align-middle mb-0 as-tabla-cards">
                <thead class="table-light">
                <tr>
                    <th scope="col" class="ps-4">ID</th>
                    <th scope="col">Nombre</th>
                    <th scope="col">Email</th>
                    <th scope="col">Teléfono</th>
                    <th scope="col">Estado</th>
                    <th scope="col" class="text-end pe-4">Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="usuario" items="${listaUsuarios}">
                    <tr>
                        <td class="ps-4 text-body-secondary" data-label="ID"><c:out value="${usuario.id_usuario}" /></td>
                        <td class="fw-medium as-celda-titulo"><a href="${ctx}/usuarios?accion=ver&amp;id=${usuario.id_usuario}" title="Ver perfil y reportes"><c:out value="${usuario.nombre} ${usuario.apellido}" /></a></td>
                        <td data-label="Email"><c:out value="${usuario.email}" /></td>
                        <td class="text-nowrap" data-label="Teléfono"><c:out value="${usuario.telefono}" /></td>
                        <td data-label="Estado">
                            <c:choose>
                                <c:when test="${usuario.bloqueado}"><span class="badge text-bg-danger">Bloqueado</span></c:when>
                                <c:otherwise><span class="badge text-bg-success">Activo</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-end pe-4 text-nowrap as-celda-acciones">
                            <c:if test="${not usuario.bloqueado}">
                                <form action="${ctx}/usuarios" method="post" class="d-inline"
                                      onsubmit="return confirm('¿Desea bloquear este usuario?');">
                                    <input type="hidden" name="accion" value="bloquear">
                                    <input type="hidden" name="id" value="${usuario.id_usuario}">
                                    <button type="submit" class="btn btn-outline-warning btn-sm">Bloquear</button>
                                </form>
                            </c:if>
                            <form action="${ctx}/usuarios" method="post" class="d-inline"
                                  onsubmit="return confirm('¿Está seguro que desea eliminarlo?');">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="id" value="${usuario.id_usuario}">
                                <button type="submit" class="btn btn-outline-danger btn-sm">Eliminar</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty listaUsuarios}">
                    <tr>
                        <td colspan="6" class="text-center py-5">
                            <p class="fw-semibold mb-0">${empty busqueda ? 'No hay usuarios registrados' : 'Ningún usuario coincide con ese nombre'}</p>
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
