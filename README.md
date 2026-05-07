# Sistema de Gestión de Vacunación / Vaccination Management System

Un sistema web desarrollado en Java (Jakarta EE, JSP, Servlets) con MySQL para gestionar registros de vacunación. 
Incluye autenticación con CAPTCHA, gestión de pacientes, vacunas y registros, además de un módulo 
de consulta pública con generación de certificados en PDF.

**Soporte Multiidioma**: La aplicación está completamente internacionalizada y soporta **4 idiomas**:
- Español (es)
- English (en)  
- Français (fr)
- Italiano (it)

## Características

- **Autenticación Segura**: Login con verificación CAPTCHA para prevenir ataques automatizados
- **Gestión Completa**: CRUD completo para pacientes, vacunas y registros de vacunación
- **Consulta Pública**: Módulo para que pacientes consulten su historial con su documento de identidad
- **Generación de Documentos**: Certificados de vacunación en PDF y exportación a Excel
- **Interfaz Responsiva**: Diseño adaptable usando Bootstrap 5 y CSS personalizado
- **Multiidioma (i18n)**: Internacionalización completa con JSTL fmt tags y resource bundles
- **Seguridad**: Control de sesiones y validaciones en frontend y backend

## Requisitos Previos

- JDK 14 o superior
- Apache Tomcat 9.0 o superior
- MySQL 8.0 o superior
- Maven 3.6 o superior

## Instalación

1. **Clonar el repositorio**:
   ```
   git clone https://github.com/tu-usuario/sistema-vacunacion.git
   ```

2. **Configurar la base de datos**:
   - Crear una base de datos MySQL
   - Ejecutar el script `src/main/resources/vacunacion_db.sql`
   - Modificar los datos de conexión en `src/main/java/sena/adso/captcha/model/Conexion.java` si es necesario

3. **Compilar el proyecto**:
   ```
   mvn clean package
   ```

4. **Desplegar el archivo WAR**:
   - Copiar el archivo `target/captcha-1.0.war` al directorio `webapps` de Tomcat
   - Iniciar o reiniciar Tomcat

## Uso del Sistema

### Acceso al Sistema

- URL: `http://localhost:8080/captcha/`
- **Credenciales por defecto**:
  - Usuario: `admin`
  - Contraseña: `admin123`

### Módulos del Sistema

1. **Dashboard**: Vista general con estadísticas y accesos rápidos
2. **Pacientes**: Gestión de datos de personas a vacunar
3. **Vacunas**: Control de vacunas disponibles
4. **Registros de Vacunación**: Gestión de aplicaciones de vacunas
5. **Personal Médico**: Administración de usuarios del sistema
6. **Consulta Pública**: Acceso para pacientes sin credenciales

### Flujo de Trabajo Típico

1. Registrar un nuevo paciente
2. Registrar vacunas disponibles
3. Crear un nuevo registro de vacunación
4. Generar certificado para el paciente

## Capturas de Pantalla

- Página de Login con CAPTCHA
- Dashboard principal
- Formulario de registro de vacunación
- Módulo de consulta pública
- Ejemplo de certificado PDF

## Internacionalización (i18n)

El sistema utiliza **JSTL Internationalization Tags** para soporte multiidioma.

### Arquitectura i18n

```
├── src/main/resources/
│   ├── messages.properties          # Español (default)
│   ├── messages_en.properties       # English
│   ├── messages_fr.properties       # Français
│   └── messages_it.properties       # Italiano
├── src/main/java/sena/adso/captcha/util/
│   └── LocaleFilter.java            # Filtro de cambio de idioma
└── src/main/webapp/views/
    ├── login.jsp                    # Selector de idioma
    ├── dashboard.jsp                # Textos con <fmt:message>
    └── templates/header.jsp         # Menú traducible
```

### Componentes Principales

| Componente | Descripción |
|------------|-------------|
| `LocaleFilter` | Intercepta peticiones y gestiona el locale en sesión |
| `messages_*.properties` | Archivos de recursos con traducciones |
| `<fmt:setLocale>` | Establece el idioma en cada JSP |
| `<fmt:message>` | Muestra texto traducido |

### Cambiar Idioma

Los usuarios pueden cambiar el idioma en cualquier momento mediante el selector ubicado en:
- **Login**: Selector desplegable en la barra superior
- **Header**: Selector en la barra de navegación (todas las páginas protegidas)

El idioma seleccionado se almacena en la sesión y persiste durante toda la navegación.

### Agregar Nuevas Traducciones

1. **Editar archivo de recursos** (ej. `messages_en.properties`):
   ```properties
   # General
   app.titulo=Vaccination Management System
   app.nuevo=New
   app.guardar=Save
   
   # Módulos
   pacientes.titulo=Patient Management
   vacunas.titulo=Vaccine Management
   ```

2. **Usar en JSP**:
   ```jsp
   <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
   
   <fmt:setLocale value="${sessionScope.locale}" />
   <fmt:setBundle basename="messages" />
   
   <h1><fmt:message key="app.titulo" /></h1>
   ```

### Soporte DataTables

Los componentes DataTables también se traducen dinámicamente según el idioma seleccionado mediante archivos JSON de CDN:
- Español: `es-ES.json`
- English: `en-GB.json`  
- Français: `fr-FR.json`
- Italiano: `it-IT.json`

## Licencia

Este proyecto está licenciado bajo la Licencia MIT.

## Contacto / Contact

Para preguntas o soporte técnico, contactar a: 
- Email: oaranguren@sena.edu.co
