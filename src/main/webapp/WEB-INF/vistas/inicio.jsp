<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Inicio" />
<c:set var="menuActivo" value="inicio" />

<%@ include file="/WEB-INF/vistas/layout/admin-inicio.jspf" %>

<h1 class="h3 fw-bold mb-1">Sistema de gestión</h1>
<p class="text-body-secondary mb-4">Panel de administración con datos actuales y accesos directos.</p>

<div class="row g-3 mb-4">
    <div class="col-12 col-md-6">
        <div class="card border-0 shadow-sm border-start border-primary border-4 h-100">
            <div class="card-body">
                <span class="text-uppercase fw-bold text-muted small">Reportes de hoy</span>
                <h2 class="display-5 fw-bold text-primary my-1"><c:out value="${reportesHoy}" default="0" /></h2>
                <p class="text-body-secondary small mb-0">Nuevos avisos ingresados en la fecha actual.</p>
            </div>
        </div>
    </div>

    <div class="col-12 col-md-6">
        <div class="card border-0 shadow-sm border-start border-warning border-4 h-100">
            <div class="card-body d-flex flex-column justify-content-between">
                <div>
                    <span class="text-uppercase fw-bold text-muted small">Sin confirmar (Pendientes)</span>
                    <h2 class="display-5 fw-bold text-warning my-1"><c:out value="${reportesPendientes}" default="0" /></h2>
                    <p class="text-body-secondary small mb-0">Reportes pendientes.</p>
                </div>
                <div class="mt-2">
                    <a href="${ctx}/reportes?estado=PENDIENTE" class="btn btn-sm btn-outline-warning text-dark fw-semibold">
                        Revisar pendientes &rarr;
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="card border-0 shadow-sm mb-4">
    <div class="card-body">
        <h2 class="h5 fw-bold mb-3">Números con más reportes</h2>
        <c:if test="${empty estadisticas}">
            <p class="text-body-secondary mb-0">No hay registros de reportes para mostrar aún.</p>
        </c:if>
        <c:if test="${not empty estadisticas}">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                    <tr>
                        <th>Número de teléfono</th>
                        <th class="text-end">Total reportes</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="stat" items="${estadisticas}">
                        <tr>
                            <td class="fw-bold"><c:out value="${stat.numero_telefono}" /></td>
                            <td class="text-end">
                                    <span class="badge bg-secondary rounded-pill px-3 py-2">
                                        <c:out value="${stat.cantidad_reportes}" />
                                    </span>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>
</div>
<h2 class="h5 fw-bold mb-3">Secciones de administración</h2>
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

<%@ include file="/WEB-INF/vistas/layout/admin-fin.jspf" %>
