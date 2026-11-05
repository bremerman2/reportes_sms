<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Prefijos de países" />
<c:set var="menuActivo" value="prefijosPublicos" />

<%@ include file="/WEB-INF/vistas/layout/usuario-inicio.jspf" %>

<div class="as-titulo">
    <div>
        <h1>Prefijos de países</h1>
        <p class="as-descripcion">
            Consultá los prefijos telefónicos de los países registrados en AlertaSMS.
        </p>
    </div>
</div>

<form class="as-filtros" action="${ctx}/consulta-prefijos" method="get" role="search">
    <div class="as-campo as-campo-ancho">
        <label for="q">Buscar por país o prefijo</label>
        <input type="search" id="q" name="q" maxlength="60"
               placeholder="Ej.: Uruguay o 598" value="<c:out value='${busqueda}' />">
    </div>
    <div class="as-filtros-acciones">
        <button class="as-boton as-boton-secundario" type="submit">Buscar</button>
        <a class="as-link-rojo" href="${ctx}/consulta-prefijos">Limpiar</a>
    </div>
</form>

<div class="as-resultados-cabecera">
    <strong>${totalRegistros} ${totalRegistros == 1 ? 'país' : 'países'}</strong>
</div>

<div class="as-tabla-contenedor">
    <c:choose>
        <c:when test="${empty listaPrefijosPublicos}">
            <div class="as-vacio">
                <c:choose>
                    <c:when test="${not empty busqueda}">
                        <h2>No hay países que coincidan</h2>
                        <p>Probá con otro nombre o prefijo, o tocá Limpiar para ver todos.</p>
                    </c:when>
                    <c:otherwise>
                        <h2>No hay prefijos registrados</h2>
                        <p>Cuando se agreguen prefijos, aparecerán en esta lista.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:when>

        <c:otherwise>
            <table class="as-tabla as-tabla-cards">
                <thead>
                    <tr>
                        <th scope="col">País</th>
                        <th scope="col">Prefijo telefónico</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="p" items="${listaPrefijosPublicos}">
                        <tr>
                            <td class="as-celda-titulo">
                                <strong>
                                    <c:out value="${p.nombre_pais}" />
                                </strong>
                            </td>
                            <td data-label="Prefijo">
                                <c:out value="${p.prefijo}" />
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/vistas/layout/paginacion-usuario.jspf" %>
<%@ include file="/WEB-INF/vistas/layout/usuario-fin.jspf" %>
