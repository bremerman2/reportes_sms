<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Ingresar" />

<%@ include file="/WEB-INF/vistas/layout/header.jspf" %>

<div class="row justify-content-center mt-5">
    <div class="col-12 col-sm-10 col-md-6 col-lg-4">

        <h1 class="h3 fw-bold text-center mb-1">Reportes SMS</h1>
        <p class="text-body-secondary text-center mb-4">Ingresá con tu cuenta de administrador</p>

        <div class="card border-0 shadow-sm">
            <div class="card-body p-4">

                <c:if test="${not empty error}">
                    <div class="alert alert-danger"><c:out value="${error}" /></div>
                </c:if>

                <form action="${ctx}/login" method="post" class="m-0">
                    <div class="mb-3">
                        <label for="email" class="form-label fw-semibold">Email</label>
                        <input type="email" class="form-control" id="email" name="email"
                               value="<c:out value='${email}' />" required autofocus>
                    </div>
                    <div class="mb-4">
                        <label for="password" class="form-label fw-semibold">Contraseña</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Ingresar</button>
                </form>

            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/footer.jspf" %>
