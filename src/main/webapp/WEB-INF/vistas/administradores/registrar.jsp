<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Registrar administrador" />
<c:set var="menuActivo" value="administradores" />

<%@ include file="/WEB-INF/vistas/layout/admin-inicio.jspf" %>

<div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">

        <h1 class="h3 fw-bold mb-1">Registrar administrador</h1>
        <p class="text-body-secondary mb-4">Completá los datos del nuevo administrador.</p>

        <div class="card border-0 shadow-sm">
            <div class="card-body p-4">

                <c:if test="${not empty error}">
                    <div class="alert alert-danger"><c:out value="${error}" /></div>
                </c:if>

                <form action="${ctx}/administradores" method="post" class="m-0">
                    <div class="row g-3 mb-3">
                        <div class="col-12 col-sm-6">
                            <label for="nombre" class="form-label fw-semibold">Nombre</label>
                            <input type="text" class="form-control" id="nombre" name="nombre" maxlength="100"
                                   value="<c:out value='${nombre}' />" required>
                        </div>
                        <div class="col-12 col-sm-6">
                            <label for="apellido" class="form-label fw-semibold">Apellido</label>
                            <input type="text" class="form-control" id="apellido" name="apellido" maxlength="100"
                                   value="<c:out value='${apellido}' />" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="telefono" class="form-label fw-semibold">Teléfono</label>
                        <input type="tel" class="form-control" id="telefono" name="telefono" maxlength="30"
                               value="<c:out value='${telefono}' />" required>
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label fw-semibold">Email</label>
                        <input type="email" class="form-control" id="email" name="email" maxlength="150"
                               placeholder="nombre@gmail.com" value="<c:out value='${email}' />" required>
                    </div>
                    <div class="mb-4">
                        <label for="password" class="form-label fw-semibold">Contraseña</label>
                        <input type="password" class="form-control" id="password" name="password" minlength="6"
                               aria-describedby="ayudaPassword" required>
                        <div id="ayudaPassword" class="form-text">Al menos 6 caracteres.</div>
                    </div>

                    <div class="d-flex flex-column flex-sm-row gap-2 border-top pt-4">
                        <button type="submit" class="btn btn-primary">Registrar administrador</button>
                        <a href="${ctx}/inicio" class="btn btn-outline-secondary">Cancelar</a>
                    </div>
                </form>

            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/admin-fin.jspf" %>
