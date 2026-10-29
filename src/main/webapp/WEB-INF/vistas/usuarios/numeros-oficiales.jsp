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

<div class="as-tabla-contenedor">
    <c:choose>
        <c:when test="${empty listaNumerosOficialesPublicos}">
            <div class="as-vacio">
                <h2>No hay números oficiales registrados</h2>
                <p>Los números oficiales que se registren aparecerán en esta lista.</p>
            </div>
        </c:when>

        <c:otherwise>
            <table class="as-tabla">
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
                            <td>
                                <strong>
                                    <c:out value="${n.pais_prefijo.prefijo}" />
                                    <c:out value="${n.numero_telefono}" />
                                </strong>
                            </td>
                            <td><c:out value="${n.nombre_entidad}" /></td>
                            <td><c:out value="${n.pais_prefijo.nombre_pais}" /></td>
                            <td><c:out value="${n.fecha_carga_texto}" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/vistas/layout/usuario-fin.jspf" %>
