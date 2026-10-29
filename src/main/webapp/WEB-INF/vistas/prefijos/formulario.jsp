<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Si viene un prefijo con id, es edición; si no, es alta --%>
<c:set var="esEdicion" value="${not empty prefijo and prefijo.id_prefijo > 0}" />
<c:set var="tituloPagina" value="${esEdicion ? 'Editar prefijo' : 'Nuevo prefijo'}" />
<c:set var="menuActivo" value="prefijos" />

<%@ include file="/WEB-INF/vistas/layout/admin-inicio.jspf" %>

<div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">

        <a href="${ctx}/prefijos" class="d-inline-block text-decoration-none mb-3">&larr; Volver al listado</a>

        <h1 class="h3 fw-bold mb-4"><c:out value="${tituloPagina}" /></h1>

        <div class="card border-0 shadow-sm">
            <div class="card-body p-4">

                <c:if test="${not empty error}">
                    <div class="alert alert-danger"><c:out value="${error}" /></div>
                </c:if>

                <form action="${ctx}/prefijos" method="post" class="m-0">
                    <input type="hidden" name="accion" value="${esEdicion ? 'editar' : 'crear'}">
                    <c:if test="${esEdicion}">
                        <input type="hidden" name="id" value="${prefijo.id_prefijo}">
                    </c:if>

                    <div class="row g-3 mb-4">
                        <div class="col-12 col-sm-4">
                            <label for="prefijo" class="form-label fw-semibold">Prefijo</label>
                            <input type="text" class="form-control" id="prefijo" name="prefijo" maxlength="10"
                                   placeholder="+598" value="<c:out value='${prefijo.prefijo}' />" required>
                            <div class="form-text">El + se agrega solo.</div>
                        </div>
                        <div class="col-12 col-sm-8">
                            <label for="nombre_pais" class="form-label fw-semibold">País</label>
                            <input type="text" class="form-control" id="nombre_pais" name="nombre_pais" maxlength="100"
                                   placeholder="Uruguay" value="<c:out value='${prefijo.nombre_pais}' />" required>
                        </div>
                    </div>

                    <div class="d-flex flex-column flex-sm-row gap-2 border-top pt-4">
                        <button type="submit" class="btn btn-primary">${esEdicion ? 'Guardar cambios' : 'Registrar prefijo'}</button>
                        <a href="${ctx}/prefijos" class="btn btn-outline-secondary">Cancelar</a>
                    </div>
                </form>

            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/admin-fin.jspf" %>
