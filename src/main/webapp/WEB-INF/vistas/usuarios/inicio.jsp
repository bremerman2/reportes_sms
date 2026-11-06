<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="tituloPagina" value="Inicio" />
<c:set var="menuActivo" value="inicio" />
<%@ include file="/WEB-INF/vistas/layout/usuario-inicio.jspf" %>

            <p class="as-ruta">Inicio</p>

            <%-- 3 · Título y acción principal --%>
            <div class="as-titulo">
                <div>
                    <h1>Inicio</h1>
                    <p class="as-descripcion">Reportes recientes de toda la comunidad. No se muestra quién reportó cada uno.</p>
                </div>
                                <div class="as-titulo-acciones">
                                    <a class="as-boton as-boton-primario" href="${ctx}/reportar">+ Reportar SMS</a>
                                    <a class="as-boton as-boton-gris" href="${ctx}/mis-reportes">Mis reportes</a>
                                </div>
            </div>

            <%-- 4 · Búsqueda y filtros --%>
            <form class="as-filtros" action="${ctx}/inicio" method="get" role="search">
                <div class="as-campo as-campo-ancho">
                    <label for="numero">Buscar por número</label>
                    <input type="search" id="numero" name="numero" inputmode="tel" maxlength="20"
                           placeholder="Ej.: 91 204" value="<c:out value='${filtroNumero}' />">
                </div>
                <div class="as-campo">
                    <label for="periodo">Filtrar por</label>
                    <select id="periodo" name="periodo">
                        <option value="dia" ${periodoFiltro == 'dia' ? 'selected' : ''}>Día</option>
                        <option value="semana" ${periodoFiltro == 'semana' ? 'selected' : ''}>Semana</option>
                        <option value="mes" ${periodoFiltro == 'mes' ? 'selected' : ''}>Mes</option>
                        <option value="anio" ${periodoFiltro == 'anio' ? 'selected' : ''}>Año</option>
                    </select>
                </div>
                <div class="as-campo">
                    <label for="fecha">Fecha de referencia</label>
                    <input type="date" id="fecha" name="fecha" value="<c:out value='${fechaFiltro}' />">
                </div>
                <c:forEach var="e" items="${estadosElegidos}"><input type="hidden" name="estado" value="${e}"></c:forEach>
                <div class="as-filtros-acciones">
                    <button class="as-boton as-boton-secundario" type="submit">Buscar</button>
                    <a class="as-boton as-boton-secundario" href="${ctx}/inicio">Limpiar</a>
                </div>
                <%@ include file="/WEB-INF/vistas/layout/filtro-estados.jspf" %>
            </form>
            <c:if test="${not empty errorFecha}"><p class="as-error"><c:out value="${errorFecha}" /></p></c:if>
            <c:if test="${not empty periodoTexto}">
                <div class="as-resultados-cabecera"><strong><c:out value="${periodoTexto}" /></strong></div>
            </c:if>

            <%-- 5 · Resultados --%>
            <div class="as-resultados-cabecera">
                <strong>${totalRegistros} ${totalRegistros == 1 ? 'registro' : 'registros'}</strong>
                <span>Orden: Fecha, más recientes primero</span>
            </div>

            <c:choose>
                <c:when test="${empty reportes}">
                    <div class="as-vacio">
                        <c:choose>
                            <c:when test="${hayFiltros}">
                                <h2>No hay reportes que coincidan</h2>
                                <p>Probá con otro número o estado, o tocá Limpiar para ver todos.</p>
                            </c:when>
                            <c:otherwise>
                                <h2>Todavía no hay reportes</h2>
                                <p>Cuando alguien reporte un SMS sospechoso va a aparecer acá.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="as-tabla">
                        <thead>
                        <tr>
                            <th scope="col">Fecha <span class="as-flecha" aria-hidden="true">↓</span></th>
                            <th scope="col">Número reportado</th>
                            <th scope="col">Mensaje</th>
                            <th scope="col">Estado</th>
                            <th scope="col">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="r" items="${reportes}">
                            <tr>
                                <td class="as-col-fecha"><c:out value="${r.fechaTexto}" /></td>
                                <td class="as-col-numero"><c:out value="${r.numeroTelefono}" /></td>
                                <td class="as-col-mensaje">
                                    <c:choose>
                                        <c:when test="${fn:length(r.cuerpoSms) > 60}"><c:out value="${fn:substring(r.cuerpoSms, 0, 60)}" />…</c:when>
                                        <c:otherwise><c:out value="${r.cuerpoSms}" /></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="as-col-estado">
                                    <c:choose>
                                        <c:when test="${r.estado == 'confirmado'}"><span class="as-estado as-estado-confirmado">Confirmado</span></c:when>
                                        <c:when test="${r.estado == 'falso_positivo'}"><span class="as-estado as-estado-falso">Falso positivo</span></c:when>
                                        <c:otherwise><span class="as-estado as-estado-pendiente">Pendiente</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="as-col-acciones">
                                    <a class="as-ver" href="${ctx}/ver-reporte?id=${r.idReporte}">Ver</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <%@ include file="/WEB-INF/vistas/layout/paginacion-usuario.jspf" %>
                </c:otherwise>
            </c:choose>
<%@ include file="/WEB-INF/vistas/layout/usuario-fin.jspf" %>
