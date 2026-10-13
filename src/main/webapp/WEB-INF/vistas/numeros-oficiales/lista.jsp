<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Números oficiales" />
<c:set var="menuActivo" value="numeros" />

<%@ include file="/WEB-INF/vistas/layout/header.jspf" %>

<div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
    <div>
        <h1 class="h3 fw-bold mb-1">Números oficiales</h1>
        <p class="text-body-secondary mb-0">Números de entidades oficiales registrados.</p>
    </div>
    <a href="${ctx}/numeros-oficiales?accion=nuevo" class="btn btn-primary">+ Nuevo número oficial</a>
</div>

<div class="card border-0 shadow-sm">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-striped table-hover align-middle mb-0">
                <thead class="table-light">
                <tr>
                    <th scope="col" class="ps-4">ID</th>
                    <th scope="col">Número</th>
                    <th scope="col">Entidad</th>
                    <th scope="col">País</th>
                    <th scope="col">Fecha de carga</th>
                    <th scope="col">Cargado por</th>
                    <th scope="col" class="text-end pe-4">Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="n" items="${listaNumeros}">
                    <tr>
                        <td class="ps-4 text-body-secondary"><c:out value="${n.id_numero_oficial}" /></td>
                        <td class="fw-medium text-nowrap">
                            <c:out value="${n.pais_prefijo.prefijo}" /> <c:out value="${n.numero_telefono}" />
                        </td>
                        <td><c:out value="${n.nombre_entidad}" /></td>
                        <td><c:out value="${n.pais_prefijo.nombre_pais}" /></td>
                        <td class="text-nowrap"><c:out value="${n.fecha_carga_texto}" /></td>
                        <td><c:out value="${n.administrador.nombre}" /></td>
                        <td class="text-end pe-4 text-nowrap">
                            <a href="${ctx}/numeros-oficiales?accion=editar&id=${n.id_numero_oficial}" class="btn btn-outline-primary btn-sm">Editar</a>
                            <form action="${ctx}/numeros-oficiales" method="post" class="d-inline"
                                  onsubmit="return confirm('¿Eliminar este número oficial?');">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="id" value="${n.id_numero_oficial}">
                                <button type="submit" class="btn btn-outline-danger btn-sm">Eliminar</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty listaNumeros}">
                    <tr>
                        <td colspan="7" class="text-center py-5">
                            <p class="fw-semibold mb-1">No hay números oficiales registrados</p>
                            <p class="text-body-secondary mb-0">Cargá el primero con el botón de arriba.</p>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/footer.jspf" %>
