<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Usuarios" />
<c:set var="menuActivo" value="usuarios" />

<%@ include file="/WEB-INF/vistas/layout/header.jspf" %>

<h1 class="h3 fw-bold mb-1">Usuarios</h1>
<p class="text-body-secondary mb-4">Usuarios registrados en la aplicación.</p>

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

<%-- Buscar usuario por ID --%>
<form action="${ctx}/usuarios" method="get" class="d-flex flex-wrap align-items-end gap-2 mb-3">
    <div>
        <label for="id" class="form-label fw-semibold mb-1">Buscar por ID</label>
        <input type="number" class="form-control" id="id" name="id" min="1" step="1"
               value="<c:out value='${idBuscado}' />" required>
    </div>
    <button type="submit" class="btn btn-outline-primary">Buscar</button>
    <c:if test="${not empty idBuscado}">
        <a href="${ctx}/usuarios" class="btn btn-link">Limpiar</a>
    </c:if>
</form>

<c:if test="${not empty error}">
    <div class="alert alert-warning"><c:out value="${error}" /></div>
</c:if>

<c:if test="${not empty usuarioEncontrado}">
    <c:set var="u" value="${usuarioEncontrado}" />
    <div class="card border-0 shadow-sm mb-4">
        <div class="card-header bg-white py-3">
            <h2 class="h6 fw-bold mb-0">Usuario encontrado</h2>
        </div>
        <div class="card-body">
            <dl class="row mb-0">
                <dt class="col-sm-3">ID</dt><dd class="col-sm-9"><c:out value="${u.id_usuario}" /></dd>
                <dt class="col-sm-3">Nombre</dt><dd class="col-sm-9"><c:out value="${u.nombre} ${u.apellido}" /></dd>
                <dt class="col-sm-3">Teléfono</dt><dd class="col-sm-9"><c:out value="${u.telefono}" /></dd>
                <dt class="col-sm-3">Email</dt><dd class="col-sm-9"><c:out value="${u.email}" /></dd>
                <dt class="col-sm-3">Bloqueado</dt><dd class="col-sm-9">${u.bloqueado ? 'Sí' : 'No'}</dd>
                <c:if test="${not empty u.administrador and u.administrador.id_administrador > 0}">
                    <dt class="col-sm-3">ID administrador</dt><dd class="col-sm-9"><c:out value="${u.administrador.id_administrador}" /></dd>
                </c:if>
            </dl>
        </div>
    </div>
</c:if>

<div class="card border-0 shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-striped table-hover align-middle mb-0">
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
                        <td class="ps-4 text-body-secondary"><c:out value="${usuario.id_usuario}" /></td>
                        <td class="fw-medium"><c:out value="${usuario.nombre} ${usuario.apellido}" /></td>
                        <td><c:out value="${usuario.email}" /></td>
                        <td class="text-nowrap"><c:out value="${usuario.telefono}" /></td>
                        <td>
                            <c:choose>
                                <c:when test="${usuario.bloqueado}"><span class="badge text-bg-danger">Bloqueado</span></c:when>
                                <c:otherwise><span class="badge text-bg-success">Activo</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-end pe-4 text-nowrap">
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
                            <p class="fw-semibold mb-0">No hay usuarios registrados</p>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/footer.jspf" %>
