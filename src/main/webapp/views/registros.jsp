<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${empty sessionScope.locale}">
    <c:set var="locale" value="es" scope="session" />
</c:if>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<jsp:include page="templates/header.jsp">
    <jsp:param name="tituloKey" value="registros.titulo" />
    <jsp:param name="menu" value="registros" />
    <jsp:param name="icono" value="fas fa-clipboard-list" />
    <jsp:param name="accionNuevo" value="${pageContext.request.contextPath}/registros?accion=nuevo" />
    <jsp:param name="textoNuevoKey" value="registros.nuevo" />
</jsp:include>

<!-- Opciones de exportación -->
<div class="mb-3">
    <a href="${pageContext.request.contextPath}/registros?accion=exportar" class="btn btn-success">
        <i class="fas fa-file-excel"></i> Exportar a Excel
    </a>
</div>

<!-- Lista de Registros de Vacunación -->
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary"><fmt:message key="registros.lista" /></h6>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover datatable">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th><fmt:message key="registros.paciente" /></th>
                        <th><fmt:message key="pacientes.numero.documento" /></th>
                        <th><fmt:message key="registros.vacuna" /></th>
                        <th><fmt:message key="registros.lote" /></th>
                        <th><fmt:message key="registros.fecha.aplicacion" /></th>
                        <th><fmt:message key="usuarios.titulo" /></th>
                        <th><fmt:message key="registros.centro.salud" /></th>
                        <th><fmt:message key="app.acciones" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="registro" items="${registros}">
                        <tr>
                            <td>${registro.id}</td>
                            <td>${registro.nombrePaciente}</td>
                            <td>${registro.documentoPaciente}</td>
                            <td>${registro.nombreVacuna}</td>
                            <td>${registro.loteVacuna}</td>
                            <td><fmt:formatDate value="${registro.fechaVacunacion}" pattern="dd/MM/yyyy HH:mm" /></td>
                            <td>${registro.nombrePersonalMedico}</td>
                            <td>${registro.lugarVacunacion}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/registros?accion=editar&id=${registro.id}" class="btn btn-warning btn-sm" title="Editar">
                                    <i class="fas fa-edit"></i>
                                </a>
                                <a href="#" class="btn btn-danger btn-sm" title="Eliminar" onclick="confirmarEliminar(${registro.id}, '${registro.nombrePaciente}')">
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
                <h5 class="modal-title"><i class="fas fa-trash"></i> Confirmar Eliminación</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p>¿Está seguro que desea eliminar el registro de vacunación para el paciente <strong id="nombrePaciente"></strong>?</p>
                <p class="text-danger">Esta acción no se puede deshacer.</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <a href="#" id="btnConfirmarEliminar" class="btn btn-danger">Eliminar</a>
            </div>
        </div>
    </div>
</div>

<script>
    function confirmarEliminar(id, nombre) {
        document.getElementById('nombrePaciente').textContent = nombre;
        document.getElementById('btnConfirmarEliminar').href = '${pageContext.request.contextPath}/registros?accion=eliminar&id=' + id;
        new bootstrap.Modal(document.getElementById('eliminarModal')).show();
    }
</script>

<jsp:include page="templates/footer.jsp" />
