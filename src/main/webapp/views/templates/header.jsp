<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${empty sessionScope.locale}">
    <c:set var="locale" value="es" scope="session" />
</c:if>

<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<c:choose>
    <c:when test="${not empty param.titulo}"><c:set var="tituloPagina" value="${param.titulo}" /></c:when>
    <c:when test="${not empty param.tituloKey}"><fmt:message key="${param.tituloKey}" var="tituloPagina" /></c:when>
    <c:otherwise><c:set var="tituloPagina" value="Panel" /></c:otherwise>
</c:choose>

<!DOCTYPE html>
<html lang="${sessionScope.locale.language}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${tituloPagina} - <fmt:message key="app.nombre" /></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/dataTables.bootstrap5.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Sora:wght@300;400;600;700;800&family=DM+Sans:wght@300;400;500;700&display=swap" rel="stylesheet">

    <style>
        :root {
            --sb-primary:  #1A5276;
            --sb-dark:     #154360;
            --sb-sena:     #39A900;
            --sb-celeste:  #2E86C1;
            --sb-bg:       #EAF0F7;
            --sb-white:    #FFFFFF;
            --sb-text:     #2C3E50;
            --sb-muted:    #7F8C8D;
            --sb-amber:    #F39C12;
            --sb-red:      #E74C3C;
            --sb-purple:   #6C3483;
            --font-h: 'Sora', sans-serif;
            --font-b: 'DM Sans', sans-serif;
        }

        * { box-sizing: border-box; }

        body {
            font-family: var(--font-b);
            background: var(--sb-bg);
            color: var(--sb-text);
            min-height: 100vh;
        }

        .sb-navbar {
            background: var(--sb-primary);
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 28px;
            min-height: 64px;
            position: sticky;
            top: 0;
            z-index: 200;
            box-shadow: 0 4px 24px rgba(16,42,67,.22);
        }

        .sb-logo {
            display: flex;
            align-items: center;
            gap: 12px;
            text-decoration: none;
            flex-shrink: 0;
        }

        .sb-logo-icon {
            width: 38px;
            height: 38px;
            background: var(--sb-sena);
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .sb-logo-icon i { color: #fff; font-size: 18px; }
        .sb-logo-name { font-family: var(--font-h); font-weight: 800; font-size: 17px; color: #fff; letter-spacing: -.4px; line-height: 1.1; }
        .sb-logo-sub { font-size: 10px; font-weight: 300; color: rgba(255,255,255,.6); letter-spacing: .6px; }

        .sb-nav-links { display: flex; align-items: center; gap: 2px; flex-wrap: wrap; justify-content: center; }
        .sb-nav-link {
            color: rgba(255,255,255,.75);
            font-size: 13px;
            font-weight: 500;
            padding: 8px 14px;
            border-radius: 8px;
            text-decoration: none;
            display: flex;
            align-items: center;
            gap: 7px;
            transition: all .18s;
            font-family: var(--font-b);
        }
        .sb-nav-link:hover, .sb-nav-link.active { background: rgba(255,255,255,.14); color: #fff; }

        .sb-nav-right { display: flex; align-items: center; gap: 12px; flex-shrink: 0; }
        .sb-lang-badge {
            background: rgba(255,255,255,.12);
            border: 1px solid rgba(255,255,255,.2);
            border-radius: 8px;
            padding: 5px 12px;
            font-size: 12px;
            color: rgba(255,255,255,.85);
            display: flex;
            gap: 8px;
            align-items: center;
            font-family: var(--font-b);
        }
        .sb-lang-badge a { color: inherit; text-decoration: none; }

        .sb-user-pill {
            display: flex;
            align-items: center;
            gap: 9px;
            background: rgba(255,255,255,.1);
            border: 1px solid rgba(255,255,255,.15);
            border-radius: 26px;
            padding: 5px 14px 5px 5px;
        }
        .sb-avatar {
            width: 32px;
            height: 32px;
            background: var(--sb-sena);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 12px;
            font-weight: 700;
            color: #fff;
            font-family: var(--font-h);
        }
        .sb-user-name { font-size: 13px; color: #fff; font-weight: 600; font-family: var(--font-h); line-height: 1.2; }
        .sb-user-role { font-size: 10px; color: rgba(255,255,255,.6); text-transform: uppercase; letter-spacing: .5px; }

        .sb-logout-btn {
            background: rgba(231,76,60,.25);
            border: 1px solid rgba(231,76,60,.4);
            border-radius: 8px;
            padding: 7px 13px;
            font-size: 12px;
            color: #FADBD8;
            font-weight: 600;
            font-family: var(--font-b);
            text-decoration: none;
            display: flex;
            align-items: center;
            gap: 6px;
            transition: all .15s;
        }
        .sb-logout-btn:hover { background: rgba(231,76,60,.45); color: #fff; }

        .sb-main { max-width: 1280px; margin: 0 auto; padding: 28px 28px 50px; }
        .sb-page-hero {
            background: linear-gradient(130deg, var(--sb-primary) 0%, #1A72A7 55%, var(--sb-celeste) 100%);
            border-radius: 24px;
            padding: 28px 34px;
            margin-bottom: 26px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            overflow: hidden;
            position: relative;
            box-shadow: 0 16px 40px rgba(26,82,118,.18);
        }
        .sb-page-hero::before {
            content: '';
            position: absolute;
            right: -70px;
            top: -70px;
            width: 280px;
            height: 280px;
            border-radius: 50%;
            border: 45px solid rgba(255,255,255,.06);
        }
        .sb-page-hero h1 {
            position: relative;
            z-index: 1;
            font-family: var(--font-h);
            font-size: 24px;
            font-weight: 800;
            color: #fff;
            margin: 0 0 7px;
            letter-spacing: -.5px;
        }
        .sb-page-hero p { position: relative; z-index: 1; font-size: 14px; color: rgba(255,255,255,.75); margin: 0; }
        .sb-action-new { position: relative; z-index: 1; }

        .alert { border: none; border-radius: 16px; box-shadow: 0 8px 22px rgba(0,0,0,.08); }
        .card {
            border: 0;
            border-radius: 18px;
            box-shadow: 0 10px 30px rgba(16,42,67,.08);
            overflow: hidden;
            transition: all .22s ease;
        }
        .card:hover { transform: translateY(-3px); box-shadow: 0 16px 38px rgba(16,42,67,.13); }
        .card-header { background: #fff; border-bottom: 1px solid #E6EEF6; padding: 18px 22px !important; }
        .card-header h6, .card-header .m-0 { font-family: var(--font-h); color: var(--sb-primary) !important; font-weight: 800 !important; }
        .card-body { padding: 24px; }

        .form-label { font-weight: 700; color: var(--sb-dark); font-size: 13px; }
        .form-control, .form-select {
            border-radius: 12px;
            border: 1px solid #D8E4EF;
            padding: 10px 13px;
            box-shadow: none !important;
        }
        .form-control:focus, .form-select:focus { border-color: var(--sb-celeste); }

        .btn { border-radius: 12px; font-weight: 700; transition: all .2s ease; }
        .btn:hover { transform: translateY(-2px); }
        .btn-primary { background: var(--sb-primary); border-color: var(--sb-primary); }
        .btn-primary:hover { background: var(--sb-dark); border-color: var(--sb-dark); }
        .btn-success { background: var(--sb-sena); border-color: var(--sb-sena); }
        .btn-info { background: var(--sb-celeste); border-color: var(--sb-celeste); color: #fff; }
        .btn-warning { background: var(--sb-amber); border-color: var(--sb-amber); color: #fff; }
        .btn-danger { background: var(--sb-red); border-color: var(--sb-red); }
        .btn-secondary { background: #6c7a89; border-color: #6c7a89; }

        .table { margin-bottom: 0; }
        .table thead { background: var(--sb-primary); color: #fff; }
        .table th { border: none; font-size: 13px; font-weight: 800; }
        .table td { vertical-align: middle; }
        .table-striped>tbody>tr:nth-of-type(odd)>* { --bs-table-accent-bg: #F7FAFD; }
        .table-hover tbody tr:hover { background: #ECF5FC; }

        .dataTables_wrapper .dataTables_length,
        .dataTables_wrapper .dataTables_filter,
        .dataTables_wrapper .dataTables_info,
        .dataTables_wrapper .dataTables_paginate { color: var(--sb-muted); margin-bottom: 12px; font-size: 13px; }
        .page-item.active .page-link { background: var(--sb-primary); border-color: var(--sb-primary); }
        .page-link { color: var(--sb-primary); border-radius: 10px; margin: 0 2px; }

        .modal-content { border: none; border-radius: 20px; overflow: hidden; box-shadow: 0 22px 60px rgba(16,42,67,.22); }
        .modal-header { background: var(--sb-primary); color: #fff; border: none; }
        .modal-header .btn-close { filter: invert(1); }

        @media (max-width: 1100px) {
            .sb-navbar { flex-wrap: wrap; height: auto; padding: 12px 16px; gap: 12px; }
            .sb-nav-links { order: 3; width: 100%; justify-content: flex-start; }
        }
        @media (max-width: 768px) {
            .sb-main { padding: 18px 14px 38px; }
            .sb-page-hero { align-items: flex-start; flex-direction: column; gap: 16px; padding: 24px; }
            .sb-user-pill { display: none; }
            .sb-nav-link { font-size: 12px; padding: 7px 10px; }
        }
    </style>
</head>

<body>
<nav class="sb-navbar">
    <a href="${pageContext.request.contextPath}/dashboard" class="sb-logo">
        <div class="sb-logo-icon"><i class="fas fa-hospital-alt"></i></div>
        <div>
            <div class="sb-logo-name">SaludBoyaca</div>
            <div class="sb-logo-sub">GESTION DE CITAS MEDICAS</div>
        </div>
    </a>

    <div class="sb-nav-links">
        <a href="${pageContext.request.contextPath}/dashboard" class="sb-nav-link ${param.menu == 'dashboard' ? 'active' : ''}"><i class="fas fa-th-large"></i> Panel de Control</a>
        <a href="${pageContext.request.contextPath}/pacientes" class="sb-nav-link ${param.menu == 'pacientes' ? 'active' : ''}"><i class="fas fa-users"></i> Pacientes</a>
        <a href="${pageContext.request.contextPath}/registros" class="sb-nav-link ${param.menu == 'registros' ? 'active' : ''}"><i class="fas fa-calendar-check"></i> Citas Medicas</a>
        <a href="${pageContext.request.contextPath}/vacunas" class="sb-nav-link ${param.menu == 'vacunas' ? 'active' : ''}"><i class="fas fa-clock"></i> Horarios</a>
        <a href="${pageContext.request.contextPath}/usuarios" class="sb-nav-link ${param.menu == 'usuarios' ? 'active' : ''}"><i class="fas fa-user-md"></i> Usuarios</a>
        <a href="${pageContext.request.contextPath}/consulta" target="_blank" class="sb-nav-link"><i class="fas fa-search"></i> Consulta Publica</a>
    </div>

    <div class="sb-nav-right">
        <div class="sb-lang-badge">
            <a href="?lang=es">&#127464;&#127476; ES</a><span style="opacity:.4;">|</span>
            <a href="?lang=en">&#127482;&#127480; EN</a><span style="opacity:.4;">|</span>
            <a href="?lang=fr">&#127467;&#127479; FR</a><span style="opacity:.4;">|</span>
            <a href="?lang=it">&#127470;&#127481; IT</a>
        </div>
        <div class="sb-user-pill">
            <div class="sb-avatar">AD</div>
            <div>
                <div class="sb-user-name">${sessionScope.usuarioNombre}</div>
                <div class="sb-user-role">${sessionScope.usuarioRol}</div>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="sb-logout-btn"><i class="fas fa-sign-out-alt"></i> Salir</a>
    </div>
</nav>

<main class="sb-main">
    <c:if test="${not empty mensaje}">
        <div class="alert alert-success mb-4"><i class="fas fa-check-circle"></i> ${mensaje}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger mb-4"><i class="fas fa-exclamation-circle"></i> ${error}</div>
    </c:if>

    <section class="sb-page-hero">
        <div>
            <h1><i class="${param.icono} me-2"></i>${tituloPagina}</h1>
            <p>Centro de Salud Municipal de Paipa, Boyaca &middot; Sistema de Gestion</p>
        </div>
        <c:if test="${not empty param.accionNuevo}">
            <a href="${param.accionNuevo}" class="btn btn-success sb-action-new">
                <i class="fas fa-plus"></i>
                <c:choose>
                    <c:when test="${not empty param.textoNuevoKey}"><fmt:message key="${param.textoNuevoKey}" /></c:when>
                    <c:otherwise>Nuevo</c:otherwise>
                </c:choose>
            </a>
        </c:if>
    </section>
