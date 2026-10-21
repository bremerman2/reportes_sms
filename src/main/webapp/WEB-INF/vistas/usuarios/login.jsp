<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Ingresar como usuario" />
<%@ include file="/WEB-INF/vistas/layout/header.jspf" %>

<div class="row justify-content-center mt-4">
  <div class="col-12 col-sm-10 col-md-6 col-lg-5">
    <h1 class="h3 fw-bold text-center">Ingresar</h1>
    <p class="text-body-secondary text-center mb-4">Accedé a tu cuenta de Reportes SMS.</p>
    <div class="card border-0 shadow-sm"><div class="card-body p-4">
      <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
      <c:if test="${param.bloqueado == '1'}"><div class="alert alert-warning">La cuenta no está disponible. Si creés que es un error, contactá al administrador.</div></c:if>
      <form action="${pageContext.request.contextPath}/login-usuario" method="post">
        <div class="mb-3"><label for="email" class="form-label">Email</label>
          <input class="form-control" type="email" id="email" name="email" value="<c:out value='${email}'/>" autocomplete="email" required autofocus></div>
        <div class="mb-3"><label for="password" class="form-label">Contraseña</label>
          <input class="form-control" type="password" id="password" name="password" autocomplete="current-password" required></div>
        <button class="btn btn-primary w-100" type="submit">Iniciar sesión</button>
      </form>
      <hr><p class="mb-0 text-center">¿Todavía no tenés cuenta? <a href="${pageContext.request.contextPath}/registro-usuario">Registrate</a></p>
      <p class="mt-3 mb-0 text-center small"><a href="${pageContext.request.contextPath}/login">Acceso de administrador</a></p>
    </div></div>
  </div>
</div>
<%@ include file="/WEB-INF/vistas/layout/footer.jspf" %>
