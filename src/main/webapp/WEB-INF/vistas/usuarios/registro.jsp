<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Crear cuenta" />
<%@ include file="/WEB-INF/vistas/layout/header.jspf" %>

<div class="row justify-content-center mt-3">
  <div class="col-12 col-md-8 col-lg-6">
    <h1 class="h3 fw-bold text-center">Crear cuenta</h1>
    <p class="text-body-secondary text-center mb-4">Registrate para utilizar Reportes SMS.</p>
    <div class="card border-0 shadow-sm"><div class="card-body p-4">
      <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
      <form action="${pageContext.request.contextPath}/registro-usuario" method="post">
        <div class="row">
          <div class="col-md-6 mb-3"><label for="nombre" class="form-label">Nombre</label><input class="form-control" id="nombre" name="nombre" value="<c:out value='${nombre}'/>" maxlength="100" required></div>
          <div class="col-md-6 mb-3"><label for="apellido" class="form-label">Apellido</label><input class="form-control" id="apellido" name="apellido" value="<c:out value='${apellido}'/>" maxlength="100" required></div>
        </div>
        <div class="mb-3"><label for="telefono" class="form-label">Teléfono</label><input class="form-control" id="telefono" name="telefono" type="tel" value="<c:out value='${telefono}'/>" maxlength="30" autocomplete="tel" required></div>
        <div class="mb-3"><label for="email" class="form-label">Email</label><input class="form-control" id="email" name="email" type="email" value="<c:out value='${email}'/>" maxlength="254" autocomplete="email" required></div>
        <div class="mb-3"><label for="password" class="form-label">Contraseña</label><input class="form-control" id="password" name="password" type="password" minlength="8" autocomplete="new-password" required><div class="form-text">Debe tener al menos 8 caracteres.</div></div>
        <div class="mb-4"><label for="confirmarPassword" class="form-label">Confirmar contraseña</label><input class="form-control" id="confirmarPassword" name="confirmarPassword" type="password" minlength="8" autocomplete="new-password" required></div>
        <button class="btn btn-primary w-100" type="submit">Crear cuenta</button>
      </form>
      <hr><p class="mb-0 text-center">¿Ya tenés cuenta? <a href="${pageContext.request.contextPath}/login-usuario">Iniciá sesión</a></p>
    </div></div>
  </div>
</div>
<%@ include file="/WEB-INF/vistas/layout/footer.jspf" %>
