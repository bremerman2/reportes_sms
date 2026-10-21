<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Reportar SMS" />
<c:set var="menuActivo" value="reportar" />
<%@ include file="/WEB-INF/vistas/layout/header.jspf" %>
<div class="row justify-content-center"><div class="col-12 col-lg-8">
  <div class="mb-4"><h1 class="h3 fw-bold">Reportar un SMS sospechoso</h1><p class="text-body-secondary">El reporte será visible para la comunidad y comenzará con estado pendiente de revisión.</p></div>
  <div class="card border-0 shadow-sm"><div class="card-body p-4">
    <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}" /></div></c:if>
    <form id="formReportar" action="${ctx}/reportar" method="post">
      <div class="mb-3"><label for="id_prefijo" class="form-label fw-semibold">País / prefijo</label>
        <select class="form-select" id="id_prefijo" name="id_prefijo" required><option value="">Seleccioná un prefijo</option>
          <c:forEach var="p" items="${prefijos}"><option value="${p.id_prefijo}" ${param.id_prefijo == p.id_prefijo ? 'selected' : ''}><c:out value="${p.nombre_pais}" /> (<c:out value="${p.prefijo}" />)</option></c:forEach>
        </select>
        <c:if test="${empty prefijos}"><div class="form-text text-danger">No hay prefijos cargados. Contactá al administrador.</div></c:if>
      </div>
      <div class="mb-3"><label for="numero_telefono" class="form-label fw-semibold">Número telefónico</label><input class="form-control" type="tel" id="numero_telefono" name="numero_telefono" maxlength="20" value="<c:out value='${telefono}' />" placeholder="Ej.: 099123456" required></div>
      <div class="mb-3"><label for="cuerpo_sms" class="form-label fw-semibold">Contenido del SMS</label><textarea class="form-control" id="cuerpo_sms" name="cuerpo_sms" rows="6" maxlength="10000" required><c:out value="${cuerpoSms}" /></textarea><div class="form-text">No incluyas contraseñas ni datos personales tuyos o de terceros.</div></div>
      <div class="mb-4">
        <label for="captura_pantalla" class="form-label fw-semibold">Captura de pantalla (opcional)</label>
        <input class="form-control" type="file" id="captura_pantalla" accept="image/png,image/jpeg,image/webp,image/gif,image/avif">
        <input type="hidden" id="captura_url" name="captura_url" value="<c:out value='${capturaUrl}' />">
        <input type="hidden" id="captura_public_id" name="captura_public_id" value="<c:out value='${capturaPublicId}' />">
        <div class="form-text">Elegí una imagen desde tu PC. Formatos admitidos: JPG, PNG, WEBP, GIF y AVIF. Máximo 5 MB.</div>
        <c:if test="${not cloudinaryConfigurada}"><div class="form-text text-danger">Cloudinary todavía no está configurado. Podés publicar sin imagen; para adjuntar capturas, configurá cloudinary.properties.</div></c:if>
        <div id="capturaNombre" class="form-text" aria-live="polite"></div>
        <img id="capturaVistaPrevia" class="img-fluid rounded mt-2 d-none" alt="Vista previa de la captura seleccionada" style="max-height: 240px;">
      </div>
      <div class="d-flex gap-2"><button class="btn btn-primary" type="submit">Publicar reporte</button><a class="btn btn-outline-secondary" href="${ctx}/inicio">Cancelar</a></div>
    </form>
  </div></div>
</div></div>
<script>
  const formulario = document.getElementById('formReportar');
  const campoCaptura = document.getElementById('captura_pantalla');
  const nombreCaptura = document.getElementById('capturaNombre');
  const vistaCaptura = document.getElementById('capturaVistaPrevia');
  const capturaUrl = document.getElementById('captura_url');
  const capturaPublicId = document.getElementById('captura_public_id');
  const cloudName = '<c:out value="${cloudinaryCloudName}" />';
  const uploadPreset = '<c:out value="${cloudinaryUploadPreset}" />';
  let enviandoTrasSubida = false;

  campoCaptura.addEventListener('change', function () {
    const archivo = this.files && this.files[0];
    capturaUrl.value = '';
    capturaPublicId.value = '';
    if (!archivo) {
      nombreCaptura.textContent = '';
      vistaCaptura.classList.add('d-none');
      vistaCaptura.removeAttribute('src');
      return;
    }
    nombreCaptura.textContent = archivo.name + ' (' + (archivo.size / 1024 / 1024).toFixed(2) + ' MB)';
    if (archivo.type.startsWith('image/')) {
      vistaCaptura.src = URL.createObjectURL(archivo);
      vistaCaptura.classList.remove('d-none');
    } else {
      vistaCaptura.classList.add('d-none');
      vistaCaptura.removeAttribute('src');
    }
  });

  formulario.addEventListener('submit', async function (evento) {
    if (enviandoTrasSubida) return;
    const archivo = campoCaptura.files && campoCaptura.files[0];
    if (!archivo) return; // Si ya se había subido la imagen y el servidor devolvió un error, conserva URL/public_id.

    evento.preventDefault();
    if (!cloudName || !uploadPreset || uploadPreset.startsWith('TU_')) {
      alert('Cloudinary no está configurado. Revisá src/main/resources/cloudinary.properties.');
      return;
    }
    if (!archivo.type.startsWith('image/')) {
      alert('Seleccioná un archivo de imagen válido.');
      return;
    }
    if (archivo.size > 5 * 1024 * 1024) {
      alert('La imagen no puede superar los 5 MB.');
      return;
    }

    const boton = formulario.querySelector('button[type="submit"]');
    const textoOriginal = boton.textContent;
    boton.disabled = true;
    boton.textContent = 'Subiendo imagen…';
    try {
      const datos = new FormData();
      const publicId = 'reportes_sms_' + crypto.randomUUID().replaceAll('-', '');
      datos.append('file', archivo);
      datos.append('upload_preset', uploadPreset);
      datos.append('public_id', publicId);
      const respuesta = await fetch('https://api.cloudinary.com/v1_1/' + encodeURIComponent(cloudName) + '/image/upload', {
        method: 'POST',
        body: datos
      });
      const resultado = await respuesta.json();
      if (!respuesta.ok || !resultado.secure_url || !resultado.public_id) {
        throw new Error((resultado.error && resultado.error.message) || 'Cloudinary no pudo recibir la imagen.');
      }
      capturaUrl.value = resultado.secure_url;
      capturaPublicId.value = resultado.public_id;
      nombreCaptura.textContent = 'Imagen subida correctamente. Publicando reporte…';
      enviandoTrasSubida = true;
      formulario.submit();
    } catch (error) {
      alert('No se pudo subir la imagen: ' + error.message);
      boton.disabled = false;
      boton.textContent = textoOriginal;
    }
  });
</script>
<%@ include file="/WEB-INF/vistas/layout/footer.jspf" %>
