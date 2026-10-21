<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Comunidad" />
<c:set var="menuActivo" value="inicio" />
<%@ include file="/WEB-INF/vistas/layout/header.jspf" %>

<div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
  <div>
    <h1 class="h3 fw-bold mb-1">Reportes de la comunidad</h1>
    <p class="text-body-secondary mb-0">Compartí experiencias y ayudá a identificar mensajes sospechosos. Los reportes nuevos quedan pendientes de revisión.</p>
  </div>
  <a href="${ctx}/reportar" class="btn btn-primary">+ Reportar un SMS</a>
</div>

<c:if test="${empty reportesComunidad}">
  <div class="card border-0 shadow-sm"><div class="card-body text-center py-5">
    <h2 class="h5">Todavía no hay reportes</h2><p class="text-body-secondary">Sé la primera persona en compartir un SMS sospechoso con la comunidad.</p>
    <a class="btn btn-primary" href="${ctx}/reportar">Reportar un SMS</a>
  </div></div>
</c:if>

<div class="row justify-content-center g-4">
<c:forEach var="r" items="${reportesComunidad}">
  <div class="col-12 col-lg-9" id="reporte-${r.idReporte}">
    <article class="card border-0 shadow-sm">
      <div class="card-body p-4">
        <div class="d-flex justify-content-between align-items-start gap-3 mb-3">
          <div>
            <div class="fw-semibold"><c:out value="${r.nombreUsuario}" /></div>
            <div class="text-body-secondary small"><c:out value="${r.fechaTexto}" /></div>
          </div>
          <c:choose>
            <c:when test="${r.estado == 'confirmado'}"><span class="badge text-bg-danger">Confirmado</span></c:when>
            <c:when test="${r.estado == 'falso_positivo'}"><span class="badge text-bg-secondary">Falso positivo</span></c:when>
            <c:otherwise><span class="badge text-bg-warning">Pendiente de revisión</span></c:otherwise>
          </c:choose>
        </div>
        <div class="rounded-3 bg-body-tertiary p-3 mb-3">
          <div class="small text-body-secondary mb-1">Número reportado</div>
          <div class="fw-semibold"><c:out value="${r.numeroTelefono}" /></div>
          <div class="small text-body-secondary">Reportes asociados a este número: <c:out value="${r.cantidadReportesNumero}" /></div>
        </div>
        <h2 class="h6 fw-bold">Contenido del SMS</h2>
        <p class="mb-3" style="white-space: pre-wrap; overflow-wrap: anywhere;"><c:out value="${r.cuerpoSms}" /></p>
<c:if test="${not empty r.capturaPantalla}">
  <div class="mb-3">
    <h3 class="h6 fw-bold mb-2">Captura del SMS</h3>

    <a href="<c:out value='${r.capturaPantalla}' />"
       target="_blank"
       rel="noopener noreferrer">
      <img
        src="<c:out value='${r.capturaPantalla}' />"
        alt="Captura del SMS reportado"
        class="img-fluid rounded border"
        loading="lazy"
        style="max-width: 100%; max-height: 420px; object-fit: contain;">
    </a>
  </div>
</c:if>
        <div class="border-top pt-3 mt-3">
          <h3 class="h6 fw-bold mb-3"><span class="me-1">Comentarios</span><span class="badge rounded-pill text-bg-light"><c:out value="${r.cantidadComentarios}" /></span></h3>
          <c:forEach var="c" items="${r.comentarios}">
            <div class="border-start border-3 ps-3 mb-3">
              <div class="d-flex flex-wrap justify-content-between gap-2"><span class="fw-semibold small"><c:out value="${c.nombreUsuario}" /></span><span class="text-body-secondary small"><c:out value="${c.fechaTexto}" /></span></div>
              <p class="mb-0 mt-1" style="white-space: pre-wrap; overflow-wrap: anywhere;"><c:out value="${c.comentario}" /></p>
            </div>
          </c:forEach>
          <c:if test="${empty r.comentarios}"><p class="text-body-secondary small">Todavía no hay comentarios. Compartí información útil y respetuosa.</p></c:if>
          <form action="${ctx}/comentar" method="post" class="mt-3">
            <input type="hidden" name="id_reporte" value="${r.idReporte}">
            <label class="form-label small fw-semibold" for="comentario-${r.idReporte}">Agregar un comentario</label>
            <textarea class="form-control mb-2" id="comentario-${r.idReporte}" name="comentario" rows="2" maxlength="2000" placeholder="¿Recibiste un mensaje similar? Compartí tu experiencia…" required></textarea>
            <div class="text-end"><button class="btn btn-outline-primary btn-sm" type="submit">Publicar comentario</button></div>
          </form>
        </div>
      </div>
    </article>
  </div>
</c:forEach>
</div>
<%@ include file="/WEB-INF/vistas/layout/footer.jspf" %>
