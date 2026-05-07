<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${empty sessionScope.locale}">
    <c:set var="locale" value="es" scope="session" />
</c:if>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<jsp:include page="templates/header.jsp">
    <jsp:param name="titulo" value="${param.accion == 'editar' ? 'Editar Registro de Vacunación' : 'Nuevo Registro de Vacunación'}" />
    <jsp:param name="menu" value="registros" />
    <jsp:param name="icono" value="fas fa-syringe" />
</jsp:include>

<!-- Formulario de Registro de Vacunación -->
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary">
            ${registro.id > 0 ? 'Editar Registro de Vacunación' : 'Registrar Nueva Vacunación'}
        </h6>
    </div>
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/registros" method="post" id="formRegistro">
            <!-- ID oculto para edición -->
            <c:if test="${registro.id > 0}">
                <input type="hidden" name="id" value="${registro.id}" />
            </c:if>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="idPaciente" class="form-label">Paciente <span class="text-danger">*</span></label>
                        <select class="form-select" id="idPaciente" name="idPaciente" required>
                            <option value="" disabled selected>Seleccione un paciente</option>
                            <c:forEach var="paciente" items="${pacientes}">
                                <option value="${paciente.id}" ${registro.idPaciente == paciente.id ? 'selected' : ''}>
                                    ${paciente.nombreCompleto} - ${paciente.documento}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="idVacuna" class="form-label">Vacuna <span class="text-danger">*</span></label>
                        <select class="form-select" id="idVacuna" name="idVacuna" required>
                            <option value="" disabled selected>Seleccione una vacuna</option>
                            <c:forEach var="vacuna" items="${vacunas}">
                                <option value="${vacuna.id}" ${registro.idVacuna == vacuna.id ? 'selected' : ''}>
                                    ${vacuna.nombre} - Lote: ${vacuna.lote} (${vacuna.laboratorio})
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="fechaVacunacion" class="form-label">Fecha y Hora de Vacunación <span class="text-danger">*</span></label>
                        <input type="datetime-local" class="form-control" id="fechaVacunacion" name="fechaVacunacion" 
                               value="<c:choose><c:when test="${not empty registro.fechaVacunacion}"><fmt:formatDate value="${registro.fechaVacunacion}" pattern="yyyy-MM-dd'T'HH:mm" /></c:when><c:otherwise>${fechaActual}</c:otherwise></c:choose>" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="lugarVacunacion" class="form-label">Lugar de Vacunación <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="lugarVacunacion" name="lugarVacunacion" value="${registro.lugarVacunacion}" required>
                    </div>
                </div>
            </div>
            
            <div class="mb-3">
                <label for="observaciones" class="form-label">Observaciones</label>
                <textarea class="form-control" id="observaciones" name="observaciones" rows="3">${registro.observaciones}</textarea>
            </div>
            
            <div class="mt-4 text-center">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Guardar
                </button>
                <a href="${pageContext.request.contextPath}/registros" class="btn btn-secondary ms-2">
                    <i class="fas fa-arrow-left"></i> Cancelar
                </a>
            </div>
        </form>
    </div>
</div>

<script>
    document.getElementById('formRegistro').addEventListener('submit', function(event) {
        // Validación básica del formulario
        const idPaciente = document.getElementById('idPaciente').value;
        const idVacuna = document.getElementById('idVacuna').value;
        const fechaVacunacion = document.getElementById('fechaVacunacion').value;
        const lugarVacunacion = document.getElementById('lugarVacunacion').value.trim();
        
        if (!idPaciente || !idVacuna || !fechaVacunacion || !lugarVacunacion) {
            event.preventDefault();
            alert('Por favor, complete todos los campos obligatorios.');
            return false;
        }
        
        return true;
    });
</script>

<jsp:include page="templates/footer.jsp" />
