<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="tituloPagina" value="Mis reportes" />
<c:set var="menuActivo" value="misReportes" />
<%@ include file="/WEB-INF/vistas/layout/usuario-inicio.jspf" %>

            <p class="as-ruta">Inicio / Mis reportes</p>

            <div class="as-titulo">
                <div>
                    <h1>Mis reportes</h1>
                    <p class="as-descripcion">Historial de los SMS que reportaste y en qué estado está cada uno.</p>
                </div>
                <a class="as-boton as-boton-primario" href="${ctx}/reportar">+ Reportar SMS</a>
            </div>

            <form class="as-filtros" action="${ctx}/mis-reportes" method="get">
                <div class="as-campo">
                    <label for="estado">Estado</label>
                    <select id="estado" name="estado">
                        <option value="">Todos</option>
                        <option value="pendiente" ${filtroEstado == 'pendiente' ? 'selected' : ''}>Pendiente</option>
                        <option value="confirmado" ${filtroEstado == 'confirmado' ? 'selected' : ''}>Confirmado</option>
                        <option value="falso_positivo" ${filtroEstado == 'falso_positivo' ? 'selected' : ''}>Falso positivo</option>
                    </select>
                </div>
                <div class="as-filtros-acciones">
                    <button class="as-boton as-boton-secundario" type="submit">Filtrar</button>
                    <a class="as-link-rojo" href="${ctx}/mis-reportes">Limpiar</a>
                </div>
            </form>

            <div class="as-resultados-cabecera">
                <strong>${totalRegistros} ${totalRegistros == 1 ? 'reporte' : 'reportes'}</strong>
                <span>Orden: Fecha, más recientes primero</span>
            </div>

            <c:choose>
                <c:when test="${empty reportes}">
                    <div class="as-vacio">
                        <c:choose>
                            <c:when test="${hayFiltros}">
                                <h2>No tenés reportes con ese estado</h2>
                                <p>Probá con otro estado o tocá Limpiar para ver todos.</p>
                            </c:when>
                            <c:otherwise>
                                <h2>Todavía no hiciste ningún reporte</h2>
                                <p>Cuando reportes un SMS sospechoso lo vas a ver acá.</p>
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
                                <td>
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
                                    <a class="as-ver" href="${ctx}/comunidad#reporte-${r.idReporte}">Ver</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                    <c:url var="urlAnterior" value="/mis-reportes">
                        <c:param name="estado" value="${filtroEstado}" />
                        <c:param name="pagina" value="${paginaActual - 1}" />
                    </c:url>
                    <c:url var="urlSiguiente" value="/mis-reportes">
                        <c:param name="estado" value="${filtroEstado}" />
                        <c:param name="pagina" value="${paginaActual + 1}" />
                    </c:url>
                    <div class="as-paginacion">
                        <span>Página ${paginaActual} de ${totalPaginas} · ${desdeRegistro}–${hastaRegistro} de ${totalRegistros}</span>
                        <div>
                            <c:choose>
                                <c:when test="${paginaActual > 1}"><a class="as-boton as-boton-secundario" href="${urlAnterior}">Anterior</a></c:when>
                                <c:otherwise><span class="as-boton as-boton-secundario" aria-disabled="true">Anterior</span></c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${paginaActual < totalPaginas}"><a class="as-boton as-boton-secundario" href="${urlSiguiente}">Siguiente</a></c:when>
                                <c:otherwise><span class="as-boton as-boton-secundario" aria-disabled="true">Siguiente</span></c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
<%@ include file="/WEB-INF/vistas/layout/usuario-fin.jspf" %>