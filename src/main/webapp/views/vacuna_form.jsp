<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${empty sessionScope.locale}">
    <c:set var="locale" value="es" scope="session" />
</c:if>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<jsp:include page="templates/header.jsp">
    <jsp:param name="titulo" value="${param.accion == 'editar' ? 'Editar Vacuna' : 'Nueva Vacuna'}" />
    <jsp:param name="menu" value="vacunas" />
    <jsp:param name="icono" value="fas fa-syringe" />
</jsp:include>

<!-- Formulario de Vacuna -->
<div class="card shadow mb-4">
    <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary">
            ${vacuna.id > 0 ? 'Editar Vacuna' : 'Registrar Nueva Vacuna'}
        </h6>
    </div>
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/vacunas" method="post" id="formVacuna">
            <!-- ID oculto para edición -->
            <c:if test="${vacuna.id > 0}">
                <input type="hidden" name="id" value="${vacuna.id}" />
            </c:if>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="nombre" class="form-label">Nombre de la Vacuna <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="nombre" name="nombre" value="${vacuna.nombre}" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="lote" class="form-label">Número de Lote <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="lote" name="lote" value="${vacuna.lote}" required>
                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="laboratorio" class="form-label">Laboratorio <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="laboratorio" name="laboratorio" value="${vacuna.laboratorio}" required>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="fechaVencimiento" class="form-label">Fecha de Vencimiento <span class="text-danger">*</span></label>
                        <input type="date" class="form-control" id="fechaVencimiento" name="fechaVencimiento" 
                               value="${vacuna.fechaVencimiento}" required>
                    </div>
                </div>
            </div>
            
            <div class="mt-4 text-center">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Guardar
                </button>
                <a href="${pageContext.request.contextPath}/vacunas" class="btn btn-secondary ms-2">
                    <i class="fas fa-arrow-left"></i> Cancelar
                </a>
            </div>
        </form>
    </div>
</div>

<script>
    document.getElementById('formVacuna').addEventListener('submit', function(event) {
        // Validación básica del formulario
        const nombre = document.getElementById('nombre').value.trim();
        const lote = document.getElementById('lote').value.trim();
        const laboratorio = document.getElementById('laboratorio').value.trim();
        const fechaVencimiento = document.getElementById('fechaVencimiento').value;
        
        if (!nombre || !lote || !laboratorio || !fechaVencimiento) {
            event.preventDefault();
            alert('Por favor, complete todos los campos obligatorios.');
            return false;
        }
        
        // Validar que la fecha de vencimiento sea futura
        const hoy = new Date();
        const fechaVenc = new Date(fechaVencimiento);
        
        if (fechaVenc < hoy) {
            event.preventDefault();
            alert('La fecha de vencimiento debe ser futura.');
            return false;
        }
        
        return true;
    });
</script>

<jsp:include page="templates/footer.jsp" />
