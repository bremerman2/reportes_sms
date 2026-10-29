<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Acceso de administrador · AlertaSMS</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Archivo:wght@400;500;700;800&display=swap"
          rel="stylesheet">

    <link rel="stylesheet" href="${ctx}/estilos/alertasms.css">
</head>
<body>
<div class="as-app">

    <header class="as-encabezado">
        <a class="as-marca" href="${ctx}/login">AlertaSMS
            <span>Administración</span>
        </a>

        <div class="as-sesion">
            <a class="as-link-rojo" href="${ctx}/login-usuario">
                Acceso de usuario
            </a>
        </div>
    </header>

    <main class="as-login">
        <div class="as-login-caja">

            <h1>Acceso de administrador</h1>

            <p class="as-descripcion">
                Ingresá con tu cuenta de administrador para gestionar
                los reportes, los números oficiales, los prefijos y los usuarios.
            </p>

            <c:if test="${not empty error}">
                <div class="as-aviso as-aviso-error" role="alert">
                    <c:out value="${error}" />
                </div>
            </c:if>

            <form class="as-login-form" action="${ctx}/login" method="post">

                <div class="as-campo">
                    <label for="email">
                        Email <span class="as-obligatorio">*</span>
                    </label>

                    <input
                        type="email"
                        id="email"
                        name="email"
                        value="<c:out value='${email}' />"
                        autocomplete="username"
                        maxlength="254"
                        required
                        autofocus>
                </div>

                <div class="as-campo">
                    <label for="password">
                        Contraseña <span class="as-obligatorio">*</span>
                    </label>

                    <input
                        type="password"
                        id="password"
                        name="password"
                        autocomplete="current-password"
                        required>
                </div>

                <button class="as-boton as-boton-primario" type="submit">
                    Ingresar
                </button>

            </form>

            <p class="as-login-pie">
                ¿Querés ingresar como usuario?
                <a href="${ctx}/login-usuario">Iniciá sesión aquí</a>
            </p>

        </div>
    </main>

</div>
</body>
</html>
