<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crear cuenta · AlertaSMS</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Archivo:wght@400;500;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/estilos/alertasms.css?v=${applicationScope.versionEstilos}">
</head>
<body>
<div class="as-app">

    <header class="as-encabezado">
        <a class="as-marca" href="${ctx}/inicio" style="display: inline-flex; align-items: center; gap: 2px;">
            <img src="https://res.cloudinary.com/umxpdr0z/image/upload/v1794002274/logo.j_1.png" alt="Logo" height="25" style="width: auto;">
            AlertaSMS<span>Mi cuenta</span>
        </a>
        <div class="as-sesion">
            <a class="as-link-rojo" href="${ctx}/login-usuario">Ingresar</a>
        </div>
    </header>

    <main class="as-login">
        <div class="as-login-caja as-login-caja-ancha">
            <h1>Crear cuenta</h1>
            <p class="as-descripcion">Registrate para reportar SMS sospechosos y seguir el estado de tus reportes.</p>

            <c:if test="${not empty error}">
                <div class="as-aviso as-aviso-error" role="alert"><c:out value="${error}" /></div>
            </c:if>

            <form class="as-login-form" action="${ctx}/registro-usuario" method="post">
                <div class="as-grilla">
                    <div class="as-campo">
                        <label for="nombre">Nombre <span class="as-obligatorio">*</span></label>
                        <input type="text" id="nombre" name="nombre" value="<c:out value='${nombre}' />"
                               maxlength="100" autocomplete="given-name" required autofocus>
                    </div>
                    <div class="as-campo">
                        <label for="apellido">Apellido <span class="as-obligatorio">*</span></label>
                        <input type="text" id="apellido" name="apellido" value="<c:out value='${apellido}' />"
                               maxlength="100" autocomplete="family-name" required>
                    </div>
                </div>
                <div class="as-campo">
                    <label for="telefono">Teléfono <span class="as-obligatorio">*</span></label>
                    <input type="tel" id="telefono" name="telefono" value="<c:out value='${telefono}' />"
                           maxlength="30" autocomplete="tel" required>
                </div>
                <div class="as-campo">
                    <label for="email">Email <span class="as-obligatorio">*</span></label>
                    <input type="email" id="email" name="email" value="<c:out value='${email}' />"
                           maxlength="254" autocomplete="email" required>
                </div>
                <div class="as-campo">
                    <label for="password">Contraseña <span class="as-obligatorio">*</span></label>
                    <input type="password" id="password" name="password" minlength="8"
                           autocomplete="new-password" aria-describedby="ayudaPassword" required>
                    <p id="ayudaPassword" class="as-ayuda">Debe tener al menos 8 caracteres.</p>
                </div>
                <div class="as-campo">
                    <label for="confirmarPassword">Confirmar contraseña <span class="as-obligatorio">*</span></label>
                    <input type="password" id="confirmarPassword" name="confirmarPassword" minlength="8"
                           autocomplete="new-password" required>
                </div>
                <button class="as-boton as-boton-primario" type="submit">Crear cuenta</button>
            </form>

            <p class="as-login-pie">¿Ya tenés cuenta? <a href="${ctx}/login-usuario">Iniciá sesión</a></p>
        </div>
    </main>
</div>
</body>
</html>
