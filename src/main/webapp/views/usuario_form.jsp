<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${empty sessionScope.locale}">
    <c:set var="locale" value="es" scope="session" />
</c:if>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<jsp:include page="templates/header.jsp">
    <jsp:param name="titulo" value="${param.accion == 'editar' ? 'Editar Personal Médico' : 'Nuevo Personal Médico'}" />
    <jsp:param name="menu" value="usuarios" />
    <jsp:param name="icono" value="fas fa-user-md" />
</jsp:include>

<!-- Formulario de Usuario -->
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary">
            ${usuario.id > 0 ? 'Editar Personal Médico' : 'Registrar Nuevo Personal Médico'}
        </h6>
    </div>
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/usuarios" method="post" id="formUsuario">
            <!-- ID oculto para edición -->
            <c:if test="${usuario.id > 0}">
                <input type="hidden" name="id" value="${usuario.id}" />
            </c:if>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="nombres" class="form-label">Nombres <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="nombres" name="nombres" value="${usuario.nombres}" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="apellidos" class="form-label">Apellidos <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="apellidos" name="apellidos" value="${usuario.apellidos}" required>
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="documento" class="form-label">Documento de Identidad <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="documento" name="documento" value="${usuario.documento}" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="email" class="form-label">Correo Electrónico <span class="text-danger">*</span></label>
                        <input type="email" class="form-control" id="email" name="email" value="${usuario.email}" required>
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="username" class="form-label">Nombre de Usuario <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="username" name="username" value="${usuario.username}" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="password" class="form-label">Contraseña <span class="text-danger">*</span></label>
                        <input type="password" class="form-control" id="password" name="password" value="${usuario.password}" 
                               ${usuario.id > 0 ? 'placeholder="Dejar en blanco para mantener la actual"' : 'required'}>
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-4">
                    <div class="mb-3">
                        <label for="rol" class="form-label">Rol <span class="text-danger">*</span></label>
                        <select class="form-select" id="rol" name="rol" required>
                            <option value="" disabled ${empty usuario.rol ? 'selected' : ''}>Seleccione un rol</option>
                            <option value="MEDICO" ${usuario.rol == 'MEDICO' ? 'selected' : ''}>Médico</option>
                            <option value="ENFERMERO" ${usuario.rol == 'ENFERMERO' ? 'selected' : ''}>Enfermero/a</option>
                        </select>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="mb-3">
                        <label for="especialidad" class="form-label">Especialidad</label>
                        <input type="text" class="form-control" id="especialidad" name="especialidad" value="${usuario.especialidad}">
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="mb-3">
                        <label for="institucion" class="form-label">Institución <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="institucion" name="institucion" value="${usuario.institucion}" required>
                    </div>
                </div>
            </div>
            
            <div class="mt-4 text-center">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Guardar
                </button>
                <a href="${pageContext.request.contextPath}/usuarios" class="btn btn-secondary ms-2">
                    <i class="fas fa-arrow-left"></i> Cancelar
                </a>
            </div>
        </form>
    </div>
</div>

<script>
    document.getElementById('formUsuario').addEventListener('submit', function(event) {
        // Validación básica del formulario
        const nombres = document.getElementById('nombres').value.trim();
        const apellidos = document.getElementById('apellidos').value.trim();
        const documento = document.getElementById('documento').value.trim();
        const email = document.getElementById('email').value.trim();
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        const rol = document.getElementById('rol').value;
        const institucion = document.getElementById('institucion').value.trim();
        
        const isEdit = ${usuario.id > 0 ? 'true' : 'false'};
        
        if (!nombres || !apellidos || !documento || !email || !username || (!isEdit && !password) || !rol || !institucion) {
            event.preventDefault();
            alert('Por favor, complete todos los campos obligatorios.');
            return false;
        }
        
        // Validar formato de correo electrónico
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email)) {
            event.preventDefault();
            alert('El formato del correo electrónico no es válido.');
            return false;
        }
        
        // Validar formato de documento (solo números)
        if (!/^\d+$/.test(documento)) {
            event.preventDefault();
            alert('El documento debe contener solo números.');
            return false;
        }
        
        // Validar longitud de contraseña
        if (!isEdit && password.length < 6) {
            event.preventDefault();
            alert('La contraseña debe tener al menos 6 caracteres.');
            return false;
        }
        
        return true;
    });
</script>

<jsp:include page="templates/footer.jsp" />
