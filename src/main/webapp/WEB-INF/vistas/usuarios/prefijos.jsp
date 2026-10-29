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

<div class="as-tabla-contenedor">
    <c:choose>
        <c:when test="${empty listaPrefijosPublicos}">
            <div class="as-vacio">
                <h2>No hay prefijos registrados</h2>
                <p>Cuando se agreguen prefijos, aparecerán en esta lista.</p>
            </div>
        </c:when>

        <c:otherwise>
            <table class="as-tabla">
                <thead>
                    <tr>
                        <th scope="col">País</th>
                        <th scope="col">Prefijo telefónico</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="p" items="${listaPrefijosPublicos}">
                        <tr>
                            <td>
                                <strong>
                                    <c:out value="${p.nombre_pais}" />
                                </strong>
                            </td>
                            <td>
                                <c:out value="${p.prefijo}" />
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/vistas/layout/usuario-fin.jspf" %>
