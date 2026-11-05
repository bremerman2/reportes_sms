<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="tituloPagina" value="Números oficiales" />
<c:set var="menuActivo" value="numerosPublicos" />

<%@ include file="/WEB-INF/vistas/layout/usuario-inicio.jspf" %>

<div class="as-titulo">
    <div>
        <h1>Números oficiales</h1>
        <p class="as-descripcion">
            Consultá números telefónicos publicados por entidades oficiales
            para ayudar a reconocer comunicaciones legítimas.
        </p>
    </div>
</div>

<form class="as-filtros" action="${ctx}/consulta-numeros-oficiales" method="get" role="search">
    <div class="as-campo as-campo-ancho">
        <label for="q">Buscar por número o entidad</label>
        <input type="search" id="q" name="q" maxlength="100"
               placeholder="Ej.: 0800 o Banco" value="<c:out value='${busqueda}' />">
    </div>
    <div class="as-filtros-acciones">
        <button class="as-boton as-boton-secundario" type="submit">Buscar</button>
        <a class="as-link-rojo" href="${ctx}/consulta-numeros-oficiales">Limpiar</a>
    </div>
</form>

<div class="as-resultados-cabecera">
    <strong>${totalRegistros} ${totalRegistros == 1 ? 'número' : 'números'}</strong>
</div>

<div class="as-tabla-contenedor">
    <c:choose>
        <c:when test="${empty listaNumerosOficialesPublicos}">
            <div class="as-vacio">
                <c:choose>
                    <c:when test="${not empty busqueda}">
                        <h2>No hay números que coincidan</h2>
                        <p>Probá con otra parte del número o de la entidad, o tocá Limpiar para ver todos.</p>
                    </c:when>
                    <c:otherwise>
                        <h2>No hay números oficiales registrados</h2>
                        <p>Los números oficiales que se registren aparecerán en esta lista.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:when>

        <c:otherwise>
            <table class="as-tabla as-tabla-cards">
                <thead>
                    <tr>
                        <th scope="col">Número telefónico</th>
                        <th scope="col">Entidad</th>
                        <th scope="col">País</th>
                        <th scope="col">Fecha de registro</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="n" items="${listaNumerosOficialesPublicos}">
                        <tr>
                            <td class="as-celda-titulo">
                                <strong>
                                    <c:out value="${n.pais_prefijo.prefijo}" />
                                    <c:out value="${n.numero_telefono}" />
                                </strong>
                            </td>
                            <td data-label="Entidad"><c:out value="${n.nombre_entidad}" /></td>
                            <td data-label="País"><c:out value="${n.pais_prefijo.nombre_pais}" /></td>
                            <td data-label="Fecha de registro"><c:out value="${n.fecha_carga_texto}" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/vistas/layout/paginacion-usuario.jspf" %>
<%@ include file="/WEB-INF/vistas/layout/usuario-fin.jspf" %>
