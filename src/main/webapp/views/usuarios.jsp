<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${empty sessionScope.locale}">
    <c:set var="locale" value="es" scope="session" />
</c:if>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<jsp:include page="templates/header.jsp">
    <jsp:param name="tituloKey" value="usuarios.titulo" />
    <jsp:param name="menu" value="usuarios" />
    <jsp:param name="icono" value="fas fa-user-md" />
    <jsp:param name="accionNuevo" value="${pageContext.request.contextPath}/usuarios?accion=nuevo" />
    <jsp:param name="textoNuevoKey" value="usuarios.nuevo" />
</jsp:include>

<!-- Lista de Personal Médico -->
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary"><fmt:message key="usuarios.lista" /></h6>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover datatable">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th><fmt:message key="usuarios.nombre.completo" /></th>
                        <th><fmt:message key="pacientes.numero.documento" /></th>
                        <th><fmt:message key="login.usuario" /></th>
                        <th><fmt:message key="usuarios.rol" /></th>
                        <th><fmt:message key="usuarios.especialidad" /></th>
                        <th><fmt:message key="usuarios.institucion" /></th>
                        <th><fmt:message key="app.acciones" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="usuario" items="${usuarios}">
                        <tr>
                            <td>${usuario.id}</td>
                            <td>${usuario.nombreCompleto}</td>
                            <td>${usuario.documento}</td>
                            <td>${usuario.username}</td>
                            <td>${usuario.rol}</td>
                            <td>${usuario.especialidad}</td>
                            <td>${usuario.institucion}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/usuarios?accion=editar&id=${usuario.id}" class="btn btn-warning btn-sm" title="<fmt:message key='app.editar' />">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <a href="#" class="btn btn-danger btn-sm" title="<fmt:message key='app.eliminar' />" onclick="confirmarEliminar(${usuario.id}, '${usuario.nombreCompleto}')">
                                    <i class="fas fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Modal de confirmación para eliminar -->
<div class="modal fade" id="eliminarModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><i class="fas fa-trash"></i> <fmt:message key="app.eliminar" /></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p><fmt:message key="app.confirmar.eliminar" /></p>
                <p class="text-danger"><strong id="nombreUsuario"></strong></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message key="app.cancelar" /></button>
                <a href="#" id="btnConfirmarEliminar" class="btn btn-danger"><fmt:message key="app.eliminar" /></a>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmarEliminar(id, nombre) {
        document.getElementById('nombreUsuario').textContent = nombre;
        document.getElementById('btnConfirmarEliminar').href = '${pageContext.request.contextPath}/usuarios?accion=eliminar&id=' + id;
        new bootstrap.Modal(document.getElementById('eliminarModal')).show();
    }
</script>

<jsp:include page="templates/footer.jsp" />
