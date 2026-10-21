<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Mi perfil" />
<%@ include file="/WEB-INF/vistas/layout/header.jspf" %>

<div class="row justify-content-center">
  <div class="col-12 col-lg-8">
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-4">
      <div><h1 class="h3 fw-bold mb-1">Mi perfil</h1><p class="text-body-secondary mb-0">Actualizá tus datos personales y, si querés, tu contraseña.</p></div>
      <span class="badge text-bg-light">ID de usuario: <c:out value="${usuario.id_usuario}"/></span>
    </div>
    <c:if test="${param.registro == 'ok'}"><div class="alert alert-success">¡Tu cuenta fue creada correctamente!</div></c:if>
    <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
    <div class="card border-0 shadow-sm"><div class="card-body p-4">
      <form action="${pageContext.request.contextPath}/perfil" method="post">
        <div class="row">
          <div class="col-md-6 mb-3"><label for="nombre" class="form-label">Nombre</label><input class="form-control" id="nombre" name="nombre" value="<c:out value='${usuario.nombre}'/>" maxlength="100" required></div>
          <div class="col-md-6 mb-3"><label for="apellido" class="form-label">Apellido</label><input class="form-control" id="apellido" name="apellido" value="<c:out value='${usuario.apellido}'/>" maxlength="100" required></div>
        </div>
        <div class="mb-3"><label for="telefono" class="form-label">Teléfono</label><input class="form-control" id="telefono" name="telefono" type="tel" value="<c:out value='${usuario.telefono}'/>" maxlength="30" required></div>
        <div class="mb-4"><label for="email" class="form-label">Email</label><input class="form-control" id="email" name="email" type="email" value="<c:out value='${usuario.email}'/>" maxlength="254" required></div>
        <hr><h2 class="h5">Cambiar contraseña</h2><p class="text-body-secondary small">Dejá estos campos vacíos para conservar tu contraseña actual.</p>
        <div class="row">
          <div class="col-md-6 mb-3"><label for="password" class="form-label">Nueva contraseña</label><input class="form-control" id="password" name="password" type="password" minlength="8" autocomplete="new-password"></div>
          <div class="col-md-6 mb-4"><label for="confirmarPassword" class="form-label">Confirmar nueva contraseña</label><input class="form-control" id="confirmarPassword" name="confirmarPassword" type="password" minlength="8" autocomplete="new-password"></div>
        </div>
        <div class="d-flex flex-wrap gap-2"><button type="submit" class="btn btn-primary">Guardar cambios</button><a href="${pageContext.request.contextPath}/logout-usuario" class="btn btn-outline-secondary">Cerrar sesión</a></div>
      </form>
    </div></div>
  </div>
</div>
<%@ include file="/WEB-INF/vistas/layout/footer.jspf" %>
