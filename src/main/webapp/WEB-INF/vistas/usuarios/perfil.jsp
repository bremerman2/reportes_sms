<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Mis datos" />
<c:set var="menuActivo" value="perfil" />
<%@ include file="/WEB-INF/vistas/layout/usuario-inicio.jspf" %>

<%-- Se muestra el formulario si se tocó "Editar" (?editar=1) o si el servidor devolvió un error al guardar --%>
<c:set var="modoEdicion" value="${param.editar == '1' or not empty error}" />

<c:if test="${param.registro == 'ok'}">
    <div class="as-aviso as-aviso-exito" role="status">¡Tu cuenta fue creada correctamente!</div>
</c:if>

<c:choose>

    <%-- ===================== Consulta ===================== --%>
    <c:when test="${not modoEdicion}">
        <p class="as-ruta"><a href="${ctx}/inicio">Inicio</a><span class="as-sep">/</span>Mis datos</p>

        <span class="as-chip">Activa</span>
        <div class="as-titulo">
            <div>
                <h1>Mis datos</h1>
                <p class="as-descripcion">Cuenta activa · ID de usuario <c:out value="${usuario.id_usuario}" /></p>
            </div>
        </div>

        <div class="as-ficha">
            <section>
                <h2 class="as-seccion-titulo">Datos personales</h2>
                <dl>
                    <div><dt>Nombre y apellido</dt><dd><c:out value="${usuario.nombre} ${usuario.apellido}" /></dd></div>
                    <div><dt>Número de teléfono</dt><dd><c:out value="${usuario.telefono}" /></dd></div>
                    <div><dt>Email</dt><dd><c:out value="${usuario.email}" /></dd></div>
                </dl>
            </section>
            <section>
                <h2 class="as-seccion-titulo">Cuenta</h2>
                <dl>
                    <div><dt>ID de usuario</dt><dd><c:out value="${usuario.id_usuario}" /></dd></div>
                    <div><dt>Contraseña</dt><dd>••••••••</dd></div>
                    <div><dt>Estado</dt><dd><span class="as-estado as-estado-pendiente">Activa</span></dd></div>
                </dl>
            </section>
        </div>

        <div class="as-acciones">
            <a class="as-boton as-boton-primario" href="${ctx}/perfil?editar=1">Editar</a>
        </div>
    </c:when>

    <%-- ===================== Edición ===================== --%>
    <c:otherwise>
        <p class="as-ruta">
            <a href="${ctx}/inicio">Inicio</a><span class="as-sep">/</span><a href="${ctx}/perfil">Mis datos</a><span class="as-sep">/</span>Editar
        </p>

        <div class="as-titulo">
            <div>
                <h1>Editar mis datos</h1>
                <p class="as-descripcion" style="max-width: 560px;">Modificá los datos y guardá los cambios. Los campos marcados con <span class="as-obligatorio">*</span> son obligatorios.</p>
            </div>
        </div>

        <c:if test="${not empty error}">
            <div class="as-aviso as-aviso-error" role="alert"><c:out value="${error}" /></div>
        </c:if>

        <form class="as-form" action="${ctx}/perfil" method="post">
            <fieldset class="as-seccion">
                <legend>Datos personales</legend>
                <div class="as-grilla">
                    <div class="as-campo">
                        <label for="nombre">Nombre <span class="as-obligatorio">*</span></label>
                        <input id="nombre" name="nombre" value="<c:out value='${usuario.nombre}' />" maxlength="100" autocomplete="given-name" required>
                    </div>
                    <div class="as-campo">
                        <label for="apellido">Apellido <span class="as-obligatorio">*</span></label>
                        <input id="apellido" name="apellido" value="<c:out value='${usuario.apellido}' />" maxlength="100" autocomplete="family-name" required>
                    </div>
                    <div class="as-campo as-campo-completo">
                        <label for="telefono">Número de teléfono <span class="as-obligatorio">*</span></label>
                        <input id="telefono" name="telefono" type="tel" value="<c:out value='${usuario.telefono}' />" maxlength="30" autocomplete="tel" required>
                        <p class="as-ayuda">Es el número desde el que usás la app.</p>
                    </div>
                </div>
            </fieldset>

            <fieldset class="as-seccion">
                <legend>Acceso</legend>
                <div class="as-campo">
                    <label for="email">Email <span class="as-obligatorio">*</span></label>
                    <input id="email" name="email" type="email" value="<c:out value='${usuario.email}' />" maxlength="254" autocomplete="email" required>
                </div>
            </fieldset>

            <fieldset class="as-seccion">
                <legend>Cambiar contraseña <small>(opcional)</small></legend>
                <div class="as-grilla">
                    <div class="as-campo">
                        <label for="password">Nueva contraseña</label>
                        <input id="password" name="password" type="password" minlength="8" autocomplete="new-password">
                    </div>
                    <div class="as-campo">
                        <label for="confirmarPassword">Confirmar nueva contraseña</label>
                        <input id="confirmarPassword" name="confirmarPassword" type="password" minlength="8" autocomplete="new-password">
                    </div>
                </div>
                <p class="as-ayuda">Dejá estos campos vacíos para conservar tu contraseña actual. Mínimo 8 caracteres.</p>
            </fieldset>

            <div class="as-acciones">
                <button class="as-boton as-boton-primario" type="submit">Guardar cambios</button>
                <a class="as-boton as-boton-secundario" href="${ctx}/perfil">Cancelar</a>
            </div>
        </form>
    </c:otherwise>
</c:choose>
<%@ include file="/WEB-INF/vistas/layout/usuario-fin.jspf" %>
