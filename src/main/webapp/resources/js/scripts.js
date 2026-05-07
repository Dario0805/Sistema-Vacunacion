/**
 * Scripts personalizados para el Sistema de Gestión de Vacunación
 */

document.addEventListener('DOMContentLoaded', function() {
    // Inicializar tooltips de Bootstrap
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Inicializar validaciones de formularios
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
    
    // Función para formatear fechas en tablas
    formatearFechasEnTablas();
    
    // Auto-cerrar alertas después de 5 segundos
    setTimeout(function() {
        const alertas = document.querySelectorAll('.alert:not(.alert-permanent)');
        alertas.forEach(alerta => {
            const alertObject = new bootstrap.Alert(alerta);
            alertObject.close();
        });
    }, 5000);
});

/**
 * Formatea fechas en el formato local preferido
 */
function formatearFechasEnTablas() {
    const celdas = document.querySelectorAll('.fecha-formateada');
    celdas.forEach(celda => {
        if (celda.textContent) {
            const fecha = new Date(celda.textContent);
            if (!isNaN(fecha)) {
                const opciones = { year: 'numeric', month: '2-digit', day: '2-digit' };
                celda.textContent = fecha.toLocaleDateString('es-ES', opciones);
            }
        }
    });
}

/**
 * Validación personalizada de campos de formulario
 * @param {HTMLElement} campo - El campo a validar
 * @param {string} tipo - El tipo de validación a realizar
 * @returns {boolean} - Resultado de la validación
 */
function validarCampo(campo, tipo) {
    const valor = campo.value.trim();
    
    switch (tipo) {
        case 'documento':
            // Solo números
            return /^\d+$/.test(valor);
        
        case 'email':
            // Formato de correo electrónico
            return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(valor);
        
        case 'texto':
            // No vacío y sin caracteres especiales
            return valor.length > 0 && /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/.test(valor);
        
        default:
            return true;
    }
}

/**
 * Confirma eliminación con modal personalizado
 * @param {string} url - URL para eliminar el elemento
 * @param {string} nombre - Nombre del elemento a eliminar
 * @param {string} tipo - Tipo de elemento (paciente, vacuna, etc.)
 */
function confirmarEliminacion(url, nombre, tipo) {
    // Se asume que existe un modal con id="modalConfirmacion"
    const modal = document.getElementById('modalConfirmacion');
    if (modal) {
        const modalBody = modal.querySelector('.modal-body');
        const btnConfirmar = modal.querySelector('#btnConfirmarEliminar');
        
        modalBody.innerHTML = `¿Está seguro que desea eliminar ${tipo} <strong>${nombre}</strong>?<br>Esta acción no se puede deshacer.`;
        btnConfirmar.href = url;
        
        new bootstrap.Modal(modal).show();
    } else {
        // Fallback si no existe el modal
        if (confirm(`¿Está seguro que desea eliminar ${tipo} ${nombre}? Esta acción no se puede deshacer.`)) {
            window.location.href = url;
        }
    }
}
