<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Prefijos" />
<c:set var="menuActivo" value="prefijos" />

<%@ include file="/WEB-INF/vistas/layout/admin-inicio.jspf" %>

<div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
    <div>
        <h1 class="h3 fw-bold mb-1">Prefijos</h1>
        <p class="text-body-secondary mb-0">Prefijos telefónicos registrados por país.</p>
    </div>
    <a href="${ctx}/prefijos?accion=nuevo" class="btn btn-primary">+ Nuevo prefijo</a>
</div>

<div class="card border-0 shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-striped table-hover align-middle mb-0">
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
                        <td class="ps-4 text-body-secondary"><c:out value="${p.id_prefijo}" /></td>
                        <td class="fw-medium"><c:out value="${p.prefijo}" /></td>
                        <td><c:out value="${p.nombre_pais}" /></td>
                        <td class="text-end pe-4 text-nowrap">
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
                            <p class="fw-semibold mb-1">Todavía no hay prefijos</p>
                            <p class="text-body-secondary mb-0">Creá el primero con el botón de arriba.</p>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/admin-fin.jspf" %>
