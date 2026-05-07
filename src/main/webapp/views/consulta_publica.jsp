<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${empty sessionScope.locale}">
    <c:set var="locale" value="es" scope="session" />
</c:if>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${sessionScope.locale.language}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="consulta.titulo" /> - <fmt:message key="app.nombre" /></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Sora:wght@300;400;600;700;800&family=DM+Sans:wght@300;400;500;700&display=swap" rel="stylesheet">

    <style>
        :root {
            --sb-primary: #1A5276;
            --sb-dark: #154360;
            --sb-sena: #39A900;
            --sb-celeste: #2E86C1;
            --sb-bg: #EAF0F7;
            --sb-white: #FFFFFF;
            --sb-text: #2C3E50;
            --sb-muted: #7F8C8D;
            --sb-red: #E74C3C;
            --font-h: 'Sora', sans-serif;
            --font-b: 'DM Sans', sans-serif;
        }

        * {
            box-sizing: border-box;
        }

        body {
            background: var(--sb-bg);
            font-family: var(--font-b);
            color: var(--sb-text);
            min-height: 100vh;
        }

        .consulta-wrapper {
            max-width: 920px;
            margin: 0 auto;
            padding: 45px 18px;
        }

        .consulta-card {
            background: var(--sb-white);
            border-radius: 24px;
            box-shadow: 0 18px 45px rgba(16, 42, 67, 0.12);
            overflow: hidden;
        }

        .consulta-hero {
            background: linear-gradient(130deg, var(--sb-primary) 0%, #1A72A7 55%, var(--sb-celeste) 100%);
            padding: 42px 36px 36px;
            position: relative;
            overflow: hidden;
            color: #fff;
            text-align: center;
        }

        .consulta-hero::before {
            content: '';
            position: absolute;
            right: -70px;
            top: -70px;
            width: 260px;
            height: 260px;
            border-radius: 50%;
            border: 42px solid rgba(255,255,255,.07);
        }

        .consulta-brand {
            position: relative;
            z-index: 1;
            display: inline-flex;
            align-items: center;
            gap: 14px;
            background: rgba(255,255,255,.14);
            border: 1px solid rgba(255,255,255,.25);
            border-radius: 22px;
            padding: 14px 22px;
            margin-bottom: 24px;
            backdrop-filter: blur(8px);
            box-shadow: 0 12px 30px rgba(0,0,0,.10);
        }

        .consulta-brand-icon {
            width: 58px;
            height: 58px;
            background: var(--sb-sena);
            border-radius: 16px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .consulta-brand-icon i {
            color: #fff;
            font-size: 28px;
        }

        .consulta-brand-title {
            font-family: var(--font-h);
            font-size: 24px;
            font-weight: 800;
            color: #fff;
            line-height: 1;
            text-align: left;
        }

        .consulta-brand-sub {
            margin-top: 5px;
            font-size: 11px;
            letter-spacing: 1px;
            color: rgba(255,255,255,.75);
            font-weight: 700;
            text-align: left;
        }

        .consulta-title {
            position: relative;
            z-index: 1;
            margin: 0;
            font-family: var(--font-h);
            font-weight: 800;
            font-size: 30px;
            letter-spacing: -0.8px;
            color: #fff;
        }

        .consulta-subtitle {
            position: relative;
            z-index: 1;
            margin-top: 10px;
            color: rgba(255,255,255,.78);
            font-size: 15px;
        }

        .consulta-body {
            padding: 28px;
        }

        .card {
            border: none;
            border-radius: 18px;
            overflow: hidden;
            box-shadow: 0 10px 26px rgba(16,42,67,.08);
        }

        .card-header-custom {
            background: #F0F5FA;
            border-bottom: 1px solid #DDE8F5;
            padding: 18px 22px;
        }

        .card-header-custom h5 {
            margin: 0;
            font-family: var(--font-h);
            font-weight: 800;
            color: var(--sb-primary);
            font-size: 16px;
        }

        .card-body {
            padding: 24px;
        }

        .form-label {
            font-weight: 700;
            color: var(--sb-dark);
            font-size: 14px;
        }

        .form-control {
            border-radius: 12px;
            border: 1px solid #D8E4EF;
            padding: 12px 14px;
            box-shadow: none !important;
        }

        .form-control:focus {
            border-color: var(--sb-celeste);
        }

        .input-group-text {
            border-radius: 12px 0 0 12px;
            border: 1px solid #D8E4EF;
            background: #F4F8FC;
            color: var(--sb-primary);
        }

        .captcha-container {
            margin-bottom: 15px;
            text-align: center;
            background: #F4F8FC;
            border: 1px solid #DDE8F5;
            border-radius: 16px;
            padding: 18px;
        }

        .captcha-img {
            border: 1px solid #D8E4EF;
            border-radius: 10px;
            max-width: 100%;
            margin-bottom: 12px;
            background: #fff;
        }

        .btn-refresh {
            color: var(--sb-celeste);
            cursor: pointer;
            font-size: 22px;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 42px;
            height: 42px;
            border-radius: 50%;
            background: #fff;
            border: 1px solid #DDE8F5;
            transition: all .2s ease;
        }

        .btn-refresh:hover {
            color: #fff;
            background: var(--sb-celeste);
            transform: rotate(90deg);
        }

        .btn {
            border-radius: 12px;
            font-weight: 800;
            font-family: var(--font-h);
            transition: all .2s ease;
        }

        .btn:hover {
            transform: translateY(-2px);
        }

        .btn-primary {
            background: var(--sb-primary);
            border-color: var(--sb-primary);
            padding: 12px;
        }

        .btn-primary:hover {
            background: var(--sb-dark);
            border-color: var(--sb-dark);
        }

        .btn-outline-primary {
            color: var(--sb-primary);
            border-color: var(--sb-primary);
        }

        .btn-outline-primary:hover {
            background: var(--sb-primary);
            border-color: var(--sb-primary);
        }

        .btn-outline-secondary {
            border-radius: 12px;
        }

        .resultado-container {
            margin-top: 30px;
        }

        .patient-info p {
            margin-bottom: 10px;
            font-size: 15px;
        }

        .patient-info strong {
            color: var(--sb-primary);
        }

        .table {
            margin-bottom: 0;
        }

        .table thead {
            background: var(--sb-primary);
            color: #fff;
        }

        .table th {
            border: none;
            font-family: var(--font-h);
            font-size: 12px;
            text-transform: uppercase;
            letter-spacing: .5px;
        }

        .table td {
            vertical-align: middle;
            font-size: 14px;
        }

        .table-striped>tbody>tr:nth-of-type(odd)>* {
            --bs-table-accent-bg: #F7FAFD;
        }

        .alert {
            border: none;
            border-radius: 14px;
            font-weight: 600;
        }

        .login-link {
            margin-top: 25px;
            text-align: center;
            color: var(--sb-muted);
        }

        .login-link a {
            color: var(--sb-primary);
            font-weight: 800;
            text-decoration: none;
        }

        .login-link a:hover {
            text-decoration: underline;
        }

        @media (max-width: 640px) {
            .consulta-wrapper {
                padding: 20px 12px;
            }

            .consulta-hero {
                padding: 28px 20px;
            }

            .consulta-title {
                font-size: 23px;
            }

            .consulta-brand {
                padding: 12px 16px;
                gap: 12px;
            }

            .consulta-brand-icon {
                width: 48px;
                height: 48px;
            }

            .consulta-brand-title {
                font-size: 19px;
            }

            .consulta-body {
                padding: 18px;
            }

            .card-body {
                padding: 18px;
            }
        }
    </style>
</head>

<body>
    <div class="consulta-wrapper">
        <div class="consulta-card">

            <div class="consulta-hero">
                <div class="consulta-brand">
                    <div class="consulta-brand-icon">
                        <i class="fas fa-shield-virus"></i>
                    </div>
                    <div>
                        <div class="consulta-brand-title">SaludBoyaca</div>
                        <div class="consulta-brand-sub">VACUNACION SEGURA</div>
                    </div>
                </div>

                <h2 class="consulta-title">
                    <fmt:message key="consulta.titulo" />
                </h2>
                <div class="consulta-subtitle">
                    <fmt:message key="consulta.subtitulo" />
                </div>
            </div>

            <div class="consulta-body">

                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i>${error}
                    </div>
                </c:if>

                <c:if test="${empty paciente}">
                    <div class="card">
                        <div class="card-header card-header-custom">
                            <h5>
                                <i class="fas fa-search me-2"></i>
                                <fmt:message key="consulta.instruccion" />
                            </h5>
                        </div>

                        <div class="card-body">
                            <form action="${pageContext.request.contextPath}/consulta" method="post">

                                <div class="mb-3">
                                    <label for="documento" class="form-label">
                                        <fmt:message key="consulta.documento" />
                                    </label>
                                    <div class="input-group">
                                        <span class="input-group-text">
                                            <i class="fas fa-id-card"></i>
                                        </span>
                                        <input type="text" class="form-control" id="documento" name="documento" required autofocus>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">CAPTCHA</label>

                                    <div class="captcha-container">
                                        <img src="${captchaImage}" alt="CAPTCHA" class="captcha-img">
                                        <br>
                                        <a href="${pageContext.request.contextPath}/consulta" class="btn-refresh" title="Generar nuevo CAPTCHA">
                                            <i class="fas fa-sync-alt"></i>
                                        </a>
                                    </div>

                                    <input type="text"
                                           class="form-control"
                                           id="captcha"
                                           name="captcha"
                                           placeholder="<fmt:message key='login.captcha.placeholder' />"
                                           required>
                                </div>

                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-search me-2"></i>
                                        <fmt:message key="consulta.consultar" />
                                    </button>
                                </div>

                            </form>
                        </div>
                    </div>
                </c:if>

                <c:if test="${not empty paciente}">
                    <div class="resultado-container">

                        <div class="card mb-4">
                            <div class="card-header card-header-custom">
                                <h5>
                                    <i class="fas fa-user me-2"></i>
                                    <fmt:message key="consulta.datos.paciente" />
                                </h5>
                            </div>

                            <div class="card-body patient-info">
                                <p><strong>Nombre:</strong> ${paciente.nombreCompleto}</p>
                                <p><strong>Documento:</strong> ${paciente.documento}</p>
                                <p>
                                    <strong>Fecha de Nacimiento:</strong>
                                    <fmt:formatDate value="${paciente.fechaNacimiento}" pattern="dd/MM/yyyy"/>
                                </p>
                            </div>
                        </div>

                        <div class="card">
                            <div class="card-header card-header-custom">
                                <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
                                    <h5>
                                        <i class="fas fa-syringe me-2"></i>
                                        <fmt:message key="consulta.historial.vacunacion" />
                                    </h5>

                                    <a href="${pageContext.request.contextPath}/consulta?accion=descargar" class="btn btn-outline-primary btn-sm">
                                        <i class="fas fa-download"></i>
                                        <fmt:message key="consulta.certificado" />
                                    </a>
                                </div>
                            </div>

                            <div class="card-body">
                                <c:if test="${empty registros}">
                                    <div class="alert alert-info" role="alert">
                                        <i class="fas fa-info-circle me-2"></i>
                                        <fmt:message key="consulta.no.registros" />
                                    </div>
                                </c:if>

                                <c:if test="${not empty registros}">
                                    <div class="table-responsive">
                                        <table class="table table-striped table-hover">
                                            <thead>
                                                <tr>
                                                    <th><fmt:message key="consulta.vacuna" /></th>
                                                    <th>Lote</th>
                                                    <th>Laboratorio</th>
                                                    <th><fmt:message key="consulta.fecha" /></th>
                                                    <th>Lugar</th>
                                                    <th>Personal Médico</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="registro" items="${registros}">
                                                    <tr>
                                                        <td>${registro.nombreVacuna}</td>
                                                        <td>${registro.loteVacuna}</td>
                                                        <td>${registro.laboratorio}</td>
                                                        <td>
                                                            <fmt:formatDate value="${registro.fechaVacunacion}" pattern="dd/MM/yyyy"/>
                                                        </td>
                                                        <td>${registro.lugarVacunacion}</td>
                                                        <td>${registro.nombrePersonalMedico}</td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:if>
                            </div>
                        </div>

                        <div class="mt-4 text-center">
                            <a href="${pageContext.request.contextPath}/consulta" class="btn btn-outline-secondary">
                                <i class="fas fa-search"></i>
                                Nueva Consulta
                            </a>
                        </div>

                    </div>
                </c:if>

                <div class="login-link">
                    <p>
                        ¿Es personal médico?
                        <a href="${pageContext.request.contextPath}/login">Iniciar sesión</a>
                    </p>
                </div>

            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>