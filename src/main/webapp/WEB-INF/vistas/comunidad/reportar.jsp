<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Reportar SMS" />
<c:set var="menuActivo" value="reportar" />
<%@ include file="/WEB-INF/vistas/layout/usuario-inicio.jspf" %>

<p class="as-ruta"><a href="${ctx}/inicio">Inicio</a><span class="as-sep">/</span>Reportar SMS</p>

<div class="as-titulo">
    <div>
        <h1>Reportar SMS</h1>
        <p class="as-descripcion" style="max-width: 560px;">
            Completá los datos del mensaje sospechoso. Los campos marcados con <span class="as-obligatorio">*</span> son obligatorios.
            El reporte va a ser visible para la comunidad y queda pendiente de revisión.
        </p>
    </div>
</div>

<c:if test="${not empty error}">
    <div class="as-aviso as-aviso-error" role="alert"><c:out value="${error}" /></div>
</c:if>

<form id="formReportar" class="as-form" action="${ctx}/reportar" method="post">

    <%-- 6 · Campos agrupados por tema --%>
    <fieldset class="as-seccion">
        <legend>Remitente</legend>
        <div class="as-grilla">
            <div class="as-campo">
                <label for="id_prefijo">Prefijo <span class="as-obligatorio">*</span></label>
                <select id="id_prefijo" name="id_prefijo" required>
                    <option value="">Seleccioná un prefijo</option>
                    <c:forEach var="p" items="${prefijos}">
                        <option value="${p.id_prefijo}" ${param.id_prefijo == p.id_prefijo ? 'selected' : ''}><c:out value="${p.nombre_pais}" /> (<c:out value="${p.prefijo}" />)</option>
                    </c:forEach>
                </select>
                <c:if test="${empty prefijos}">
                    <p class="as-ayuda as-ayuda-error">No hay prefijos cargados. Contactá al administrador.</p>
                </c:if>
            </div>
            <div class="as-campo">
                <label for="numero_telefono">Número de teléfono <span class="as-obligatorio">*</span></label>
                <input type="tel" id="numero_telefono" name="numero_telefono" maxlength="20"
                       value="<c:out value='${telefono}' />" placeholder="Ej.: 91 204 773" required>
            </div>
        </div>
    </fieldset>

    <fieldset class="as-seccion">
        <legend>Mensaje</legend>
        <div class="as-campo">
            <label for="cuerpo_sms">Cuerpo del SMS <span class="as-obligatorio">*</span></label>
            <textarea id="cuerpo_sms" name="cuerpo_sms" rows="6" maxlength="10000"
                      placeholder="Pegá el texto del mensaje" required><c:out value="${cuerpoSms}" /></textarea>
            <p class="as-ayuda">No incluyas contraseñas ni datos personales tuyos o de terceros.</p>
        </div>
    </fieldset>

    <fieldset class="as-seccion">
        <legend>Captura <small>(opcional)</small></legend>
        <div class="as-campo">
            <label for="captura_pantalla">Captura de pantalla del SMS</label>
            <input type="file" id="captura_pantalla" accept="image/png,image/jpeg,image/webp,image/gif,image/avif">
            <input type="hidden" id="captura_url" name="captura_url" value="<c:out value='${capturaUrl}' />">
            <input type="hidden" id="captura_public_id" name="captura_public_id" value="<c:out value='${capturaPublicId}' />">
            <p class="as-ayuda">Formatos: JPG, PNG, WEBP, GIF y AVIF. Máximo 5 MB.</p>
            <c:if test="${not cloudinaryConfigurada}">
                <p class="as-ayuda as-ayuda-error">Cloudinary todavía no está configurado. Podés publicar sin imagen; para adjuntar capturas, configurá cloudinary.properties.</p>
            </c:if>
            <p id="capturaNombre" class="as-ayuda" aria-live="polite"></p>
            <p id="capturaError" class="as-ayuda as-ayuda-error" role="alert"></p>
            <img id="capturaVistaPrevia" class="as-vista-previa" alt="Vista previa de la captura seleccionada" hidden>
        </div>
    </fieldset>
        <fieldset class="as-seccion">
            <legend class="as-legend-inline">
                <span>Notificaciones</span>
                <input type="checkbox"
                       id="notificar_actualizaciones"
                       name="notificar_actualizaciones"
                       value="true"
                       class="as-checkbox-chico"
                ${param.notificar_actualizaciones == null || param.notificar_actualizaciones == 'true' ? 'checked' : ''}>
            </legend>
            <div class="as-campo">
                <label for="notificar_actualizaciones" class="as-texto-descriptivo">
                    Quiero recibir notificaciones sobre las actualizaciones de este reporte.
                </label>
            </div>
        </fieldset>
    <%-- 7 · Acciones: Guardar y Cancelar al final --%>
    <div class="as-acciones">
        <button class="as-boton as-boton-primario" type="submit">Publicar reporte</button>
        <a class="as-boton as-boton-secundario" href="${ctx}/inicio">Cancelar</a>
    </div>
</form>

<script>
    const formulario = document.getElementById('formReportar');
    const campoCaptura = document.getElementById('captura_pantalla');
    const nombreCaptura = document.getElementById('capturaNombre');
    const errorCaptura = document.getElementById('capturaError');
    const vistaCaptura = document.getElementById('capturaVistaPrevia');
    const capturaUrl = document.getElementById('captura_url');
    const capturaPublicId = document.getElementById('captura_public_id');
    const cloudName = '<c:out value="${cloudinaryCloudName}" />';
    const uploadPreset = '<c:out value="${cloudinaryUploadPreset}" />';
    let enviandoTrasSubida = false;

    function mostrarError(texto) {
        errorCaptura.textContent = texto;
    }

    campoCaptura.addEventListener('change', function () {
        const archivo = this.files && this.files[0];
        capturaUrl.value = '';
        capturaPublicId.value = '';
        mostrarError('');
        if (!archivo) {
            nombreCaptura.textContent = '';
            vistaCaptura.hidden = true;
            vistaCaptura.removeAttribute('src');
            return;
        }
        nombreCaptura.textContent = archivo.name + ' (' + (archivo.size / 1024 / 1024).toFixed(2) + ' MB)';
        if (archivo.type.startsWith('image/')) {
            vistaCaptura.src = URL.createObjectURL(archivo);
            vistaCaptura.hidden = false;
        } else {
            vistaCaptura.hidden = true;
            vistaCaptura.removeAttribute('src');
        }
    });

    formulario.addEventListener('submit', async function (evento) {
        if (enviandoTrasSubida) return;
        const archivo = campoCaptura.files && campoCaptura.files[0];
        if (!archivo) return; // Si ya se había subido la imagen y el servidor devolvió un error, conserva URL/public_id.

        evento.preventDefault();
        mostrarError('');
        if (!cloudName || !uploadPreset || uploadPreset.startsWith('TU_')) {
            mostrarError('Cloudinary no está configurado. Quitá la imagen para publicar sin captura.');
            return;
        }
        if (!archivo.type.startsWith('image/')) {
            mostrarError('Seleccioná un archivo de imagen válido.');
            return;
        }
        if (archivo.size > 5 * 1024 * 1024) {
            mostrarError('La imagen no puede superar los 5 MB.');
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
            mostrarError('No se pudo subir la imagen: ' + error.message);
            boton.disabled = false;
            boton.textContent = textoOriginal;
        }
    });
</script>
<%@ include file="/WEB-INF/vistas/layout/usuario-fin.jspf" %>
