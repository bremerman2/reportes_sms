<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
                <a class="as-boton as-boton-primario" href="${ctx}/reportar">+ Reportar SMS</a>
            </div>

            <%-- 4 · Búsqueda y filtros --%>
            <form class="as-filtros" action="${ctx}/inicio" method="get" role="search">
                <div class="as-campo as-campo-ancho">
                    <label for="numero">Buscar por número</label>
                    <input type="search" id="numero" name="numero" inputmode="tel" maxlength="20"
                           placeholder="Ej.: 91 204" value="<c:out value='${filtroNumero}' />">
                </div>
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
                    <button class="as-boton as-boton-secundario" type="submit">Buscar</button>
                    <a class="as-link-rojo" href="${ctx}/inicio">Limpiar</a>
                </div>
            </form>

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
                            <th scope="col">Estado</th>
                            <th scope="col">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="r" items="${reportes}">
                            <tr>
                                <td class="as-col-fecha"><c:out value="${r.fechaTexto}" /></td>
                                <td class="as-col-numero"><c:out value="${r.numeroTelefono}" /></td>
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

                    <%-- Paginación: los links conservan la búsqueda y el filtro --%>
                    <c:url var="urlAnterior" value="/inicio">
                        <c:param name="numero" value="${filtroNumero}" />
                        <c:param name="estado" value="${filtroEstado}" />
                        <c:param name="pagina" value="${paginaActual - 1}" />
                    </c:url>
                    <c:url var="urlSiguiente" value="/inicio">
                        <c:param name="numero" value="${filtroNumero}" />
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
