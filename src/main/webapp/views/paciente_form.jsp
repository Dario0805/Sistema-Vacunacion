<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${empty sessionScope.locale}">
    <c:set var="locale" value="es" scope="session" />
</c:if>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<jsp:include page="templates/header.jsp">
    <jsp:param name="titulo" value="${param.accion == 'editar' ? 'Editar Paciente' : 'Nuevo Paciente'}" />
    <jsp:param name="menu" value="pacientes" />
    <jsp:param name="icono" value="fas fa-user-edit" />
</jsp:include>

<!-- Formulario de Paciente -->
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary">
            ${paciente.id > 0 ? 'Editar Paciente' : 'Registrar Nuevo Paciente'}
        </h6>
    </div>
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/pacientes" method="post" id="formPaciente">
            <!-- ID oculto para edición -->
            <c:if test="${paciente.id > 0}">
                <input type="hidden" name="id" value="${paciente.id}" />
            </c:if>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="nombres" class="form-label">Nombres <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="nombres" name="nombres" value="${paciente.nombres}" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="apellidos" class="form-label">Apellidos <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="apellidos" name="apellidos" value="${paciente.apellidos}" required>
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="documento" class="form-label">Documento de Identidad <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="documento" name="documento" value="${paciente.documento}" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="fechaNacimiento" class="form-label">Fecha de Nacimiento <span class="text-danger">*</span></label>
                        <input type="date" class="form-control" id="fechaNacimiento" name="fechaNacimiento" 
                               value="<fmt:formatDate value="${paciente.fechaNacimiento}" pattern="yyyy-MM-dd" />" required>
                    </div>
                </div>
            </div>
            
            <div class="mt-4 text-center">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Guardar
                </button>
                <a href="${pageContext.request.contextPath}/pacientes" class="btn btn-secondary ms-2">
                    <i class="fas fa-arrow-left"></i> Cancelar
                </a>
            </div>
        </form>
    </div>
</div>

<script>
    document.getElementById('formPaciente').addEventListener('submit', function(event) {
        // Validación básica del formulario
        const nombres = document.getElementById('nombres').value.trim();
        const apellidos = document.getElementById('apellidos').value.trim();
        const documento = document.getElementById('documento').value.trim();
        const fechaNacimiento = document.getElementById('fechaNacimiento').value;
        
        if (!nombres || !apellidos || !documento || !fechaNacimiento) {
            event.preventDefault();
            alert('Por favor, complete todos los campos obligatorios.');
            return false;
        }
        
        // Validar formato de documento (solo números)
        if (!/^\d+$/.test(documento)) {
            event.preventDefault();
            alert('El documento debe contener solo números.');
            return false;
        }
        
        return true;
    });
</script>

<jsp:include page="templates/footer.jsp" />
