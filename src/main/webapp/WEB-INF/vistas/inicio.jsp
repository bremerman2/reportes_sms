<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Inicio" />
<c:set var="menuActivo" value="inicio" />

<%@ include file="/WEB-INF/vistas/layout/header.jspf" %>

<h1 class="h3 fw-bold mb-1">Sistema de gestión</h1>
<p class="text-body-secondary mb-4">Elegí una sección para empezar.</p>

<div class="row g-3">
    <div class="col-12 col-md-6 col-lg-4">
        <a href="${ctx}/reportes" class="card h-100 border-0 shadow-sm text-decoration-none">
            <div class="card-body">
                <h2 class="h5 fw-bold">Reportes</h2>
                <p class="text-body-secondary mb-0">Registrar, confirmar o marcar falsos positivos, filtrar por estado y ver estadísticas.</p>
            </div>
        </a>
    </div>
    <div class="col-12 col-md-6 col-lg-4">
        <a href="${ctx}/numeros-oficiales" class="card h-100 border-0 shadow-sm text-decoration-none">
            <div class="card-body">
                <h2 class="h5 fw-bold">Números oficiales</h2>
                <p class="text-body-secondary mb-0">Alta, edición, baja y listado de números de entidades oficiales.</p>
            </div>
        </a>
    </div>
    <div class="col-12 col-md-6 col-lg-4">
        <a href="${ctx}/prefijos" class="card h-100 border-0 shadow-sm text-decoration-none">
            <div class="card-body">
                <h2 class="h5 fw-bold">Prefijos</h2>
                <p class="text-body-secondary mb-0">Prefijos telefónicos por país.</p>
            </div>
        </a>
    </div>
    <div class="col-12 col-md-6 col-lg-4">
        <a href="${ctx}/usuarios" class="card h-100 border-0 shadow-sm text-decoration-none">
            <div class="card-body">
                <h2 class="h5 fw-bold">Usuarios</h2>
                <p class="text-body-secondary mb-0">Buscar, bloquear o eliminar usuarios y ver estadísticas.</p>
            </div>
        </a>
    </div>
    <div class="col-12 col-md-6 col-lg-4">
        <a href="${ctx}/administradores" class="card h-100 border-0 shadow-sm text-decoration-none">
            <div class="card-body">
                <h2 class="h5 fw-bold">Administradores</h2>
                <p class="text-body-secondary mb-0">Registrar un nuevo administrador.</p>
            </div>
        </a>
    </div>
</div>

<%@ include file="/WEB-INF/vistas/layout/footer.jspf" %>
