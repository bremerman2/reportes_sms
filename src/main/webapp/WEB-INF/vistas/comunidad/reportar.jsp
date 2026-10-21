<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Reportar SMS" />
<c:set var="menuActivo" value="reportar" />
<%@ include file="/WEB-INF/vistas/layout/header.jspf" %>
<div class="row justify-content-center"><div class="col-12 col-lg-8">
  <div class="mb-4"><h1 class="h3 fw-bold">Reportar un SMS sospechoso</h1><p class="text-body-secondary">El reporte será visible para la comunidad y comenzará con estado pendiente de revisión.</p></div>
  <div class="card border-0 shadow-sm"><div class="card-body p-4">
    <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}" /></div></c:if>
    <form action="${ctx}/reportar" method="post">
      <div class="mb-3"><label for="id_prefijo" class="form-label fw-semibold">País / prefijo</label>
        <select class="form-select" id="id_prefijo" name="id_prefijo" required><option value="">Seleccioná un prefijo</option>
          <c:forEach var="p" items="${prefijos}"><option value="${p.id_prefijo}" ${param.id_prefijo == p.id_prefijo ? 'selected' : ''}><c:out value="${p.nombre_pais}" /> (<c:out value="${p.prefijo}" />)</option></c:forEach>
        </select>
        <c:if test="${empty prefijos}"><div class="form-text text-danger">No hay prefijos cargados. Contactá al administrador.</div></c:if>
      </div>
      <div class="mb-3"><label for="numero_telefono" class="form-label fw-semibold">Número telefónico</label><input class="form-control" type="tel" id="numero_telefono" name="numero_telefono" maxlength="20" value="<c:out value='${telefono}' />" placeholder="Ej.: 099123456" required></div>
      <div class="mb-3"><label for="cuerpo_sms" class="form-label fw-semibold">Contenido del SMS</label><textarea class="form-control" id="cuerpo_sms" name="cuerpo_sms" rows="6" maxlength="10000" required><c:out value="${cuerpoSms}" /></textarea><div class="form-text">No incluyas contraseñas ni datos personales tuyos o de terceros.</div></div>
      <div class="mb-4"><label for="captura_pantalla" class="form-label fw-semibold">Referencia de captura (opcional)</label><input class="form-control" type="text" id="captura_pantalla" name="captura_pantalla" maxlength="255" value="<c:out value='${captura}' />" placeholder="Nombre o ruta de una captura, si ya está disponible"></div>
      <div class="d-flex gap-2"><button class="btn btn-primary" type="submit">Publicar reporte</button><a class="btn btn-outline-secondary" href="${ctx}/inicio">Cancelar</a></div>
    </form>
  </div></div>
</div></div>
<%@ include file="/WEB-INF/vistas/layout/footer.jspf" %>
