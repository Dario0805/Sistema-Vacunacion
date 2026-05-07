<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${empty sessionScope.locale}">
    <c:set var="locale" value="es" scope="session" />
</c:if>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<jsp:include page="templates/header.jsp">
    <jsp:param name="tituloKey" value="pacientes.titulo" />
    <jsp:param name="menu" value="pacientes" />
    <jsp:param name="icono" value="fas fa-users" />
    <jsp:param name="accionNuevo" value="${pageContext.request.contextPath}/pacientes?accion=nuevo" />
    <jsp:param name="textoNuevoKey" value="pacientes.nuevo" />
</jsp:include>

<!-- Lista de Pacientes -->
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary"><fmt:message key="pacientes.lista" /></h6>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover datatable">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th><fmt:message key="pacientes.nombre" /></th>
                        <th><fmt:message key="pacientes.apellido" /></th>
                        <th><fmt:message key="pacientes.numero.documento" /></th>
                        <th><fmt:message key="pacientes.fecha.nacimiento" /></th>
                        <th><fmt:message key="app.acciones" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="paciente" items="${pacientes}">
                        <tr>
                            <td>${paciente.id}</td>
                            <td>${paciente.nombres}</td>
                            <td>${paciente.apellidos}</td>
                            <td>${paciente.documento}</td>
                            <td><fmt:formatDate value="${paciente.fechaNacimiento}" pattern="dd/MM/yyyy" /></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/pacientes?accion=editar&id=${paciente.id}" class="btn btn-warning btn-sm" title="<fmt:message key='app.editar' />">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <a href="#" class="btn btn-danger btn-sm" title="<fmt:message key='app.eliminar' />" onclick="confirmarEliminar(${paciente.id}, '${paciente.nombreCompleto}')">
                                    <i class="fas fa-trash"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/registros?idPaciente=${paciente.id}" class="btn btn-info btn-sm" title="<fmt:message key='app.ver' />">
                                    <i class="fas fa-history"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/registros?accion=nuevo&idPaciente=${paciente.id}" class="btn btn-success btn-sm" title="<fmt:message key='registros.nuevo' />">
                                    <i class="fas fa-syringe"></i>
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
                <p class="text-danger"><strong id="nombrePaciente"></strong></p>
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
        document.getElementById('nombrePaciente').textContent = nombre;
        document.getElementById('btnConfirmarEliminar').href = '${pageContext.request.contextPath}/pacientes?accion=eliminar&id=' + id;
        new bootstrap.Modal(document.getElementById('eliminarModal')).show();
    }
</script>

<jsp:include page="templates/footer.jsp" />
