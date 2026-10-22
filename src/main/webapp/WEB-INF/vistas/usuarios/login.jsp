<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Ingresar · AlertaSMS</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Archivo:wght@400;500;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${ctx}/estilos/alertasms.css">
</head>
<body>
<div class="as-app">

    <header class="as-encabezado">
        <a class="as-marca" href="${ctx}/login-usuario">AlertaSMS<span>Mi cuenta</span></a>
        <div class="as-sesion">
            <a class="as-link-rojo" href="${ctx}/registro-usuario">Crear cuenta</a>
        </div>
    </header>

    <main class="as-login">
        <div class="as-login-caja">
            <h1>Ingresar</h1>
            <p class="as-descripcion">Accedé a tu cuenta para reportar SMS sospechosos y ver los reportes recientes.</p>

            <c:if test="${not empty error}">
                <div class="as-aviso as-aviso-error" role="alert"><c:out value="${error}" /></div>
            </c:if>
            <c:if test="${param.bloqueado == '1'}">
                <div class="as-aviso as-aviso-error" role="alert">La cuenta no está disponible. Si creés que es un error, contactá al administrador.</div>
            </c:if>

            <form class="as-login-form" action="${ctx}/login-usuario" method="post">
                <div class="as-campo">
                    <label for="email">Email <span class="as-obligatorio">*</span></label>
                    <input type="email" id="email" name="email" value="<c:out value='${email}' />"
                           autocomplete="email" maxlength="254" required autofocus>
                </div>
                <div class="as-campo">
                    <label for="password">Contraseña <span class="as-obligatorio">*</span></label>
                    <input type="password" id="password" name="password" autocomplete="current-password" required>
                </div>
                <button class="as-boton as-boton-primario" type="submit">Ingresar</button>
            </form>

            <p class="as-login-pie">¿Todavía no tenés cuenta? <a href="${ctx}/registro-usuario">Registrate</a></p>
            <p class="as-login-admin"><a href="${ctx}/login">Acceso de administrador</a></p>
        </div>
    </main>
</div>
</body>
</html>
