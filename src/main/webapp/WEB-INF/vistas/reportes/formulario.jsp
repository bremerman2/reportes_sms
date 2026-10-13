<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Registrar reporte" />
<c:set var="menuActivo" value="reportes" />

<%@ include file="/WEB-INF/vistas/layout/header.jspf" %>

<div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">

        <a href="${ctx}/reportes" class="d-inline-block text-decoration-none mb-3">&larr; Volver al listado</a>

        <h1 class="h3 fw-bold mb-1">Registrar reporte</h1>
        <p class="text-body-secondary mb-4">El reporte se registra con la fecha y hora actual y queda pendiente.</p>

        <div class="card border-0 shadow-sm">
            <div class="card-body p-4">

                <c:if test="${not empty error}">
                    <div class="alert alert-danger"><c:out value="${error}" /></div>
                </c:if>

                <form action="${ctx}/reportes" method="post" class="m-0">
                    <input type="hidden" name="accion" value="crear">

                    <div class="mb-3">
                        <label for="cuerpo_sms" class="form-label fw-semibold">Cuerpo del SMS</label>
                        <textarea class="form-control" id="cuerpo_sms" name="cuerpo_sms" rows="4" required><c:out value="${param.cuerpo_sms}" /></textarea>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-12 col-sm-7">
                            <label for="id_usuario" class="form-label fw-semibold">Usuario que reporta</label>
                            <select class="form-select" id="id_usuario" name="id_usuario" required>
                                <option value="">Elegí…</option>
                                <c:forEach var="u" items="${listaUsuarios}">
                                    <option value="${u.id_usuario}" ${param.id_usuario == u.id_usuario ? 'selected' : ''}>
                                        <c:out value="${u.id_usuario}" /> - <c:out value="${u.nombre} ${u.apellido}" />
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-12 col-sm-5">
                            <label for="id_num_reportado" class="form-label fw-semibold">ID del número reportado</label>
                            <input type="number" class="form-control" id="id_num_reportado" name="id_num_reportado" min="1" step="1"
                                   value="<c:out value='${param.id_num_reportado}' />" required>
                        </div>
                    </div>

                    <div class="form-check mb-3">
                        <input class="form-check-input" type="checkbox" id="es_manual" name="es_manual" value="si"
                               ${not empty param.es_manual ? 'checked' : ''}>
                        <label class="form-check-label" for="es_manual">Es un reporte manual</label>
                    </div>

                    <div class="mb-4" id="bloqueCaptura">
                        <label for="captura_pantalla" class="form-label fw-semibold">Captura de pantalla</label>
                        <input type="text" class="form-control" id="captura_pantalla" name="captura_pantalla" maxlength="255"
                               placeholder="Ruta o nombre del archivo" value="<c:out value='${param.captura_pantalla}' />">
                        <div class="form-text">Obligatoria solo si el reporte es manual.</div>
                    </div>

                    <div class="d-flex flex-column flex-sm-row gap-2 border-top pt-4">
                        <button type="submit" class="btn btn-primary">Registrar reporte</button>
                        <a href="${ctx}/reportes" class="btn btn-outline-secondary">Cancelar</a>
                    </div>
                </form>

            </div>
        </div>
    </div>
</div>

<script>
    // la captura solo se muestra (y es obligatoria) si el reporte es manual
    const checkManual = document.getElementById('es_manual');
    const bloqueCaptura = document.getElementById('bloqueCaptura');
    const inputCaptura = document.getElementById('captura_pantalla');
    function actualizarCaptura() {
        bloqueCaptura.hidden = !checkManual.checked;
        inputCaptura.required = checkManual.checked;
    }
    checkManual.addEventListener('change', actualizarCaptura);
    actualizarCaptura();
</script>

<%@ include file="/WEB-INF/vistas/layout/footer.jspf" %>
