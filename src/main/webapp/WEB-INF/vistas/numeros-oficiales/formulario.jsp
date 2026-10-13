<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Si viene un número con id, es edición; si no, es alta --%>
<c:set var="esEdicion" value="${not empty numero and numero.id_numero_oficial > 0}" />
<c:set var="tituloPagina" value="${esEdicion ? 'Editar número oficial' : 'Nuevo número oficial'}" />
<c:set var="menuActivo" value="numeros" />

<%@ include file="/WEB-INF/vistas/layout/header.jspf" %>

<div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">

        <a href="${ctx}/numeros-oficiales" class="d-inline-block text-decoration-none mb-3">&larr; Volver al listado</a>

        <h1 class="h3 fw-bold mb-4"><c:out value="${tituloPagina}" /></h1>

        <div class="card border-0 shadow-sm">
            <div class="card-body p-4">

                <c:if test="${not empty error}">
                    <div class="alert alert-danger"><c:out value="${error}" /></div>
                </c:if>

                <c:if test="${empty listaPrefijos}">
                    <div class="alert alert-warning">
                        Primero tenés que <a href="${ctx}/prefijos?accion=nuevo">registrar un prefijo</a>.
                    </div>
                </c:if>

                <form action="${ctx}/numeros-oficiales" method="post" class="m-0">
                    <input type="hidden" name="accion" value="${esEdicion ? 'editar' : 'crear'}">
                    <c:if test="${esEdicion}">
                        <input type="hidden" name="id" value="${numero.id_numero_oficial}">
                    </c:if>

                    <div class="row g-3 mb-3">
                        <div class="col-12 col-sm-5">
                            <label for="id_prefijo" class="form-label fw-semibold">País / prefijo</label>
                            <select class="form-select" id="id_prefijo" name="id_prefijo" required>
                                <option value="">Elegí…</option>
                                <c:forEach var="p" items="${listaPrefijos}">
                                    <option value="${p.id_prefijo}" ${numero.pais_prefijo.id_prefijo == p.id_prefijo ? 'selected' : ''}>
                                        <c:out value="${p.nombre_pais}" /> (<c:out value="${p.prefijo}" />)
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-12 col-sm-7">
                            <label for="numero_telefono" class="form-label fw-semibold">Número de teléfono</label>
                            <input type="text" class="form-control" id="numero_telefono" name="numero_telefono" maxlength="30"
                                   value="<c:out value='${numero.numero_telefono}' />" required>
                        </div>
                    </div>
                    <div class="mb-4">
                        <label for="nombre_entidad" class="form-label fw-semibold">Nombre de la entidad</label>
                        <input type="text" class="form-control" id="nombre_entidad" name="nombre_entidad" maxlength="150"
                               placeholder="Ej.: BROU" value="<c:out value='${numero.nombre_entidad}' />" required>
                        <c:if test="${not esEdicion}">
                            <div class="form-text">Se registra con la fecha de hoy y a tu nombre.</div>
                        </c:if>
                    </div>

                    <div class="d-flex flex-column flex-sm-row gap-2 border-top pt-4">
                        <button type="submit" class="btn btn-primary">${esEdicion ? 'Guardar cambios' : 'Registrar número'}</button>
                        <a href="${ctx}/numeros-oficiales" class="btn btn-outline-secondary">Cancelar</a>
                    </div>
                </form>

            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/footer.jspf" %>
