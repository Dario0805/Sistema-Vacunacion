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
    <title><fmt:message key="login.titulo" /> - <fmt:message key="app.nombre" /></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        :root {
            --primary: #0F3D5E;
            --primary-2: #176B87;
            --accent: #39A900;
            --bg: #F3F7FA;
            --text: #263238;
            --muted: #6B7280;
            --border: #DCE6EF;
        }

        * {
            box-sizing: border-box;
        }

        body {
            min-height: 100vh;
            margin: 0;
            background:
                linear-gradient(120deg, rgba(15,61,94,.92), rgba(23,107,135,.78)),
                url("https://images.unsplash.com/photo-1584982751601-97dcc096659c?auto=format&fit=crop&w=1400&q=80");
            background-size: cover;
            background-position: center;
            font-family: "Segoe UI", Arial, sans-serif;
            color: var(--text);
        }

        .login-wrapper {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 35px 15px;
        }

        .login-card {
            width: 100%;
            max-width: 430px;
            background: rgba(255,255,255,.98);
            border-radius: 28px;
            padding: 34px;
            box-shadow: 0 30px 80px rgba(0,0,0,.28);
            border: 1px solid rgba(255,255,255,.65);
        }

        .logo-circle {
            width: 86px;
            height: 86px;
            border-radius: 24px;
            background: linear-gradient(135deg, var(--primary), var(--primary-2));
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 18px;
            font-size: 36px;
            box-shadow: 0 16px 32px rgba(15,61,94,.32);
        }

        .login-title {
            text-align: center;
            color: var(--primary);
            font-weight: 900;
            margin-bottom: 6px;
            letter-spacing: -.4px;
        }

        .login-subtitle {
            text-align: center;
            color: var(--muted);
            margin-bottom: 22px;
            font-size: .95rem;
        }

        .language-selector {
            text-align: center;
            margin-bottom: 20px;
        }

        .language-selector .btn {
            border-radius: 999px;
            padding: 7px 16px;
            font-weight: 700;
            border-color: var(--border);
            color: var(--primary);
            background: #fff;
        }

        .dropdown-menu {
            border-radius: 14px;
            border: none;
            box-shadow: 0 14px 35px rgba(0,0,0,.14);
        }

        .form-label {
            font-weight: 800;
            color: var(--text);
            font-size: 14px;
            margin-bottom: 8px;
        }

        .input-group {
            border-radius: 16px;
            box-shadow: 0 8px 22px rgba(15,61,94,.07);
        }

        .input-group-text {
            background: #F1F6FA;
            color: var(--primary-2);
            border: 1px solid var(--border);
            border-radius: 16px 0 0 16px;
            width: 48px;
            justify-content: center;
        }

        .form-control {
            border: 1px solid var(--border);
            border-radius: 0 16px 16px 0;
            padding: 12px 14px;
            font-weight: 600;
        }

        .form-control:focus {
            border-color: var(--primary-2);
            box-shadow: 0 0 0 .25rem rgba(23,107,135,.16);
        }

        .otp-box {
            background: #F6FAFD;
            border: 1px solid var(--border);
            border-radius: 20px;
            padding: 15px;
            text-align: center;
        }

        .otp-info {
            color: var(--primary-2);
            font-weight: 700;
            font-size: 0.9rem;
            margin-bottom: 10px;
        }

        .btn-return {
            display: inline-flex;
            align-items: center;
            gap: 7px;
            margin-top: 11px;
            color: var(--muted);
            text-decoration: none;
            font-weight: 800;
            font-size: .85rem;
        }

        .btn-return:hover {
            color: var(--primary);
        }

        .otp-input-style {
            border-radius: 16px !important;
            text-align: center;
            font-weight: 800;
            letter-spacing: 10px;
            font-size: 1.5rem;
        }

        .btn-login {
            background: linear-gradient(135deg, var(--accent), #2D8700);
            border: none;
            color: white;
            border-radius: 16px;
            padding: 13px;
            font-weight: 900;
            box-shadow: 0 14px 30px rgba(57,169,0,.30);
            transition: all .2s ease;
        }

        .btn-login:hover {
            color: white;
            transform: translateY(-2px);
            box-shadow: 0 18px 38px rgba(57,169,0,.38);
        }

        .public-link {
            margin-top: 24px;
            padding-top: 20px;
            border-top: 1px solid #E8EEF4;
        }

        .public-link a {
            color: var(--primary);
            text-decoration: none;
            font-weight: 800;
        }

        .public-link a:hover {
            color: var(--accent);
        }

        .alert {
            border: none;
            border-radius: 16px;
            font-weight: 700;
        }

        @media (max-width: 576px) {
            .login-card {
                padding: 28px 22px;
                border-radius: 24px;
            }

            .logo-circle {
                width: 74px;
                height: 74px;
                font-size: 31px;
            }
        }
    </style>
</head>

<body>
<div class="login-wrapper">
    <div class="login-card">

        <div class="logo-circle">
            <i class="fas fa-shield-virus"></i>
        </div>

        <h2 class="login-title"><fmt:message key="login.titulo" /></h2>
        <p class="login-subtitle">
            <fmt:message key="app.nombre" />
        </p>

        <div class="language-selector">
            <div class="dropdown">
                <button class="btn btn-outline-primary btn-sm dropdown-toggle" type="button" id="languageDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                    <i class="fas fa-globe"></i> <fmt:message key="menu.idioma" />
                </button>
                <ul class="dropdown-menu" aria-labelledby="languageDropdown">
                    <li><a class="dropdown-item" href="?lang=es">🇨🇴 <fmt:message key="menu.idioma.es" /></a></li>
                    <li><a class="dropdown-item" href="?lang=en">🇺🇸 <fmt:message key="menu.idioma.en" /></a></li>
                    <li><a class="dropdown-item" href="?lang=fr">🇫🇷 <fmt:message key="menu.idioma.fr" /></a></li>
                    <li><a class="dropdown-item" href="?lang=it">🇮🇹 <fmt:message key="menu.idioma.it" /></a></li>
                </ul>
            </div>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                <i class="fas fa-circle-exclamation me-2"></i>${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <c:choose>
                <%-- PASO 2: VERIFICACIÓN OTP --%>
                <c:when test="${sessionScope.otpPending}">
                    <div class="otp-box mb-3">
                        <div class="otp-info">
                            <i class="fas fa-key me-2"></i>Verificación de Seguridad
                        </div>
                        <p class="small text-muted">Ingrese el código de 6 dígitos enviado a su correo.</p>
                        <input type="text" class="form-control otp-input-style" id="otp" name="otp" 
                               maxlength="6" placeholder="000000" required autofocus>
                        
                        <a href="?reset=true" class="btn-return">
                            <i class="fas fa-arrow-left"></i> Volver al inicio
                        </a>
                    </div>
                    
                    <div class="d-grid gap-2 mt-4">
                        <button type="submit" class="btn btn-login">
                            <i class="fas fa-check-circle me-2"></i>Verificar y Entrar
                        </button>
                    </div>
                </c:when>

                <%-- PASO 1: LOGIN NORMAL --%>
                <c:otherwise>
                    <div class="mb-3">
                        <label for="username" class="form-label">
                            <fmt:message key="login.usuario" />
                        </label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-user"></i></span>
                            <input type="text" class="form-control" id="username" name="username" required autofocus>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">
                            <fmt:message key="login.contrasena" />
                        </label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-lock"></i></span>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                    </div>

                    <div class="d-grid gap-2 mt-4">
                        <button type="submit" class="btn btn-login">
                            <i class="fas fa-right-to-bracket me-2"></i>
                            <fmt:message key="login.ingresar" />
                        </button>
                    </div>
                </c:otherwise>
            </c:choose>
        </form>

        <div class="mt-4 text-center public-link">
            <a href="${pageContext.request.contextPath}/consulta">
                <i class="fas fa-search me-1"></i>
                <fmt:message key="login.consulta.publica" />
            </a>
        </div>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>