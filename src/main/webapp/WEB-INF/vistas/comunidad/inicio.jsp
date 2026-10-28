<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Comunidad" />
<c:set var="menuActivo" value="comunidad" />
<%@ include file="/WEB-INF/vistas/layout/usuario-inicio.jspf" %>

<p class="as-ruta"><a href="${ctx}/inicio">Inicio</a><span class="as-sep">/</span>Comunidad</p>

<div class="as-titulo">
    <div>
        <h1>Comunidad</h1>
        <p class="as-descripcion">Reportes completos con sus comentarios. Compartí si recibiste un mensaje parecido; los reportes nuevos quedan pendientes de revisión.</p>
    </div>
    <a class="as-boton as-boton-primario" href="${ctx}/reportar">+ Reportar SMS</a>
</div>

<%-- Búsqueda por número o por texto del SMS --%>
<form class="as-filtros" action="${ctx}/comunidad" method="get" role="search">
    <div class="as-campo as-campo-ancho">
        <label for="q">Buscar por número o texto del SMS</label>
        <input type="search" id="q" name="q" maxlength="100"
               placeholder="Ej.: 91 204 o cuenta bloqueada" value="<c:out value='${busqueda}' />">
    </div>
    <div class="as-filtros-acciones">
        <button class="as-boton as-boton-secundario" type="submit">Buscar</button>
        <a class="as-link-rojo" href="${ctx}/comunidad">Limpiar</a>
    </div>
</form>

<c:if test="${not empty busqueda}">
    <div class="as-resultados-cabecera">
        <strong>${reportesComunidad.size()} ${reportesComunidad.size() == 1 ? 'resultado' : 'resultados'} para “<c:out value="${busqueda}" />”</strong>
        <span>Orden: Fecha, más recientes primero</span>
    </div>
</c:if>

<c:if test="${empty reportesComunidad}">
    <div class="as-vacio">
        <c:choose>
            <c:when test="${not empty busqueda}">
                <h2>No hay reportes que coincidan</h2>
                <p>Probá con otra palabra del mensaje o con parte del número, o tocá Limpiar para ver todos.</p>
            </c:when>
            <c:otherwise>
                <h2>Todavía no hay reportes</h2>
                <p>Sé la primera persona en compartir un SMS sospechoso con la comunidad.</p>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<div class="as-muro">
    <c:forEach var="r" items="${reportesComunidad}">
        <article class="as-tarjeta" id="reporte-${r.idReporte}">

            <div class="as-tarjeta-cabecera">
                <div>
                    <h2 class="as-tarjeta-numero"><c:out value="${r.numeroTelefono}" /></h2>
                    <p class="as-tarjeta-meta">
                        Reportado por <c:out value="${r.nombreUsuario}" /> · <c:out value="${r.fechaTexto}" /> ·
                            ${r.cantidadReportesNumero} ${r.cantidadReportesNumero == 1 ? 'reporte' : 'reportes'} de este número
                    </p>
                </div>

                <div style="display: flex; align-items: center; gap: 10px;">
                    <c:if test="${r.idUsuario != sessionScope.usuarioLogueado.id_usuario}">
                        <form action="${ctx}/suscripcion" method="post" style="margin: 0;">
                            <input type="hidden" name="id_reporte" value="${r.idReporte}">
                            <c:choose>
                                <c:when test="${r.suscrito}">
                                    <button type="submit" title="Dejar de seguir" style="background: none; border: none; padding: 4px; cursor: pointer; color: #cf2a14; display: inline-flex; align-items: center; gap: 4px; font-size: 12px; font-weight: bold;">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bell-fill" viewBox="0 0 16 16">
                                            <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zm.995-14.901a1 1 0 1 0-1.99 0A5 5 0 0 0 3 6c0 1.098-.5 6-2 7h14c-1.5-1-2-5.902-2-7 0-2.42-1.72-4.44-4.005-4.901z"/>
                                        </svg>
                                        <span>Siguiendo</span>
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button type="submit" title="Recibir novedades de este reporte" style="background: none; border: none; padding: 4px; cursor: pointer; color: #777; display: inline-flex; align-items: center; gap: 4px; font-size: 12px;">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bell" viewBox="0 0 16 16">
                                            <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zM8 1.918l-.797.161A4 4 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4 4 0 0 0-3.203-3.92L8 1.917zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6a5 5 0 0 1 10 0c0 .88.32 4.2 1.22 6z"/>
                                        </svg>
                                        <span>Notificarme</span>
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </c:if>

                    <c:choose>
                        <c:when test="${r.estado == 'confirmado'}"><span class="as-estado as-estado-confirmado">Confirmado</span></c:when>
                        <c:when test="${r.estado == 'falso_positivo'}"><span class="as-estado as-estado-falso">Falso positivo</span></c:when>
                        <c:otherwise><span class="as-estado as-estado-pendiente">Pendiente</span></c:otherwise>
                    </c:choose>
                </div>
            </div>

            <h3 class="as-subtitulo">Contenido del SMS</h3>
            <p class="as-sms"><c:out value="${r.cuerpoSms}" /></p>

            <c:if test="${not empty r.capturaPantalla}">
                <div class="as-captura">
                    <h3 class="as-subtitulo">Captura del SMS</h3>
                    <a href="<c:out value='${r.capturaPantalla}' />" target="_blank" rel="noopener noreferrer">
                        <img src="<c:out value='${r.capturaPantalla}' />" alt="Captura del SMS reportado" loading="lazy">
                    </a>
                </div>
            </c:if>

            <section class="as-comentarios" aria-label="Comentarios">
                <h3 class="as-subtitulo">Comentarios (${r.cantidadComentarios})</h3>

                <c:forEach var="com" items="${r.comentarios}">
                    <div class="as-comentario">
                        <div class="as-comentario-cabecera">
                            <strong><c:out value="${com.nombreUsuario}" /></strong>
                            <span><c:out value="${com.fechaTexto}" /></span>
                        </div>
                        <p><c:out value="${com.comentario}" /></p>
                    </div>
                </c:forEach>
                <c:if test="${empty r.comentarios}">
                    <p class="as-ayuda">Todavía no hay comentarios. Compartí información útil y respetuosa.</p>
                </c:if>

                <form class="as-form as-comentar" action="${ctx}/comentar" method="post">
                    <input type="hidden" name="id_reporte" value="${r.idReporte}">
                    <div class="as-campo">
                        <label for="comentario-${r.idReporte}">Agregar un comentario</label>
                        <textarea id="comentario-${r.idReporte}" name="comentario" rows="2" maxlength="2000"
                                  placeholder="¿Recibiste un mensaje similar? Contalo acá…" required></textarea>
                    </div>
                    <div class="as-comentar-acciones">
                        <button class="as-boton as-boton-secundario" type="submit">Publicar comentario</button>
                    </div>
                </form>
            </section>
        </article>
    </c:forEach>
</div>
<%@ include file="/WEB-INF/vistas/layout/usuario-fin.jspf" %>
