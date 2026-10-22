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
            <c:choose>
                <c:when test="${r.estado == 'confirmado'}"><span class="as-estado as-estado-confirmado">Confirmado</span></c:when>
                <c:when test="${r.estado == 'falso_positivo'}"><span class="as-estado as-estado-falso">Falso positivo</span></c:when>
                <c:otherwise><span class="as-estado as-estado-pendiente">Pendiente</span></c:otherwise>
            </c:choose>
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
