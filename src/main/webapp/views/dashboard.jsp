<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${empty sessionScope.locale}">
    <c:set var="locale" value="es" scope="session" />
</c:if>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<!DOCTYPE html>
<html lang="${sessionScope.locale}">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>SaludBoyaca - Dashboard</title>

  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet"/>
  <link href="https://fonts.googleapis.com/css2?family=Sora:wght@300;400;600;700;800&family=DM+Sans:wght@300;400;500&display=swap" rel="stylesheet"/>

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
      --sb-surface:  #F4F8FC;
      --font-h: 'Sora', sans-serif;
      --font-b: 'DM Sans', sans-serif;
    }

    * { box-sizing: border-box; margin: 0; padding: 0; }

    body {
      font-family: var(--font-b);
      background: var(--sb-bg);
      color: var(--sb-text);
      min-height: 100vh;
    }

    /* ── NAVBAR ── */
    .sb-navbar {
      background: var(--sb-primary);
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 28px;
      height: 64px;
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
    }

    .sb-logo-icon {
      width: 38px; height: 38px;
      background: var(--sb-sena);
      border-radius: 10px;
      display: flex; align-items: center; justify-content: center;
    }

    .sb-logo-icon i { color: #fff; font-size: 18px; }

    .sb-logo-name {
      font-family: var(--font-h);
      font-weight: 800; font-size: 17px;
      color: #fff; letter-spacing: -0.4px; line-height: 1.1;
    }

    .sb-logo-sub {
      font-size: 10px; font-weight: 300;
      color: rgba(255,255,255,.6); letter-spacing: 0.6px;
    }

    .sb-nav-links { display: flex; align-items: center; gap: 2px; }

    .sb-nav-link {
      color: rgba(255,255,255,.75);
      font-size: 13px; font-weight: 500;
      padding: 8px 14px; border-radius: 8px;
      text-decoration: none; display: flex;
      align-items: center; gap: 7px;
      transition: all .18s; font-family: var(--font-b);
    }

    .sb-nav-link:hover, .sb-nav-link.active {
      background: rgba(255,255,255,.14); color: #fff;
    }

    .sb-nav-right { display: flex; align-items: center; gap: 12px; }

    .sb-lang-badge {
      background: rgba(255,255,255,.12);
      border: 1px solid rgba(255,255,255,.2);
      border-radius: 8px; padding: 5px 12px;
      font-size: 12px; color: rgba(255,255,255,.85);
      display: flex; gap: 8px; align-items: center;
      font-family: var(--font-b); cursor: pointer;
    }

    .sb-user-pill {
      display: flex; align-items: center; gap: 9px;
      background: rgba(255,255,255,.1);
      border: 1px solid rgba(255,255,255,.15);
      border-radius: 26px; padding: 5px 14px 5px 5px;
    }

    .sb-avatar {
      width: 32px; height: 32px;
      background: var(--sb-sena); border-radius: 50%;
      display: flex; align-items: center; justify-content: center;
      font-size: 12px; font-weight: 700; color: #fff;
      font-family: var(--font-h);
    }

    .sb-user-name { font-size: 13px; color: #fff; font-weight: 600; font-family: var(--font-h); line-height: 1.2; }
    .sb-user-role { font-size: 10px; color: rgba(255,255,255,.6); text-transform: uppercase; letter-spacing: 0.5px; }

    .sb-logout-btn {
      background: rgba(231,76,60,.25);
      border: 1px solid rgba(231,76,60,.4);
      border-radius: 8px; padding: 7px 13px;
      font-size: 12px; color: #FADBD8;
      font-weight: 600; font-family: var(--font-b);
      text-decoration: none; display: flex; align-items: center; gap: 6px;
      transition: all .15s;
    }

    .sb-logout-btn:hover { background: rgba(231,76,60,.45); color: #fff; }

    /* ── MAIN ── */
    .sb-main { max-width: 1280px; margin: 0 auto; padding: 28px 28px 50px; }

    /* ── HERO ── */
    .sb-hero {
      background: linear-gradient(130deg, var(--sb-primary) 0%, #1A72A7 55%, var(--sb-celeste) 100%);
      border-radius: 24px; padding: 32px 36px;
      margin-bottom: 26px; display: flex;
      align-items: center; justify-content: space-between;
      overflow: hidden; position: relative;
    }

    .sb-hero::before {
      content: ''; position: absolute;
      right: -70px; top: -70px;
      width: 300px; height: 300px; border-radius: 50%;
      border: 45px solid rgba(255,255,255,.06);
    }

    .sb-hero-left { position: relative; z-index: 1; }

    .sb-hero-left h1 {
      font-family: var(--font-h); font-size: 24px;
      font-weight: 800; color: #fff;
      margin-bottom: 7px; letter-spacing: -0.5px;
    }

    .sb-hero-left p { font-size: 14px; color: rgba(255,255,255,.72); font-weight: 300; }

    .sb-hero-chips { display: flex; gap: 10px; margin-top: 20px; flex-wrap: wrap; }

    .sb-chip {
      background: rgba(255,255,255,.13);
      border: 1px solid rgba(255,255,255,.2);
      border-radius: 20px; padding: 5px 14px;
      font-size: 12px; color: rgba(255,255,255,.9);
    }

    .sb-chip.green { background: rgba(57,169,0,.25); border-color: rgba(57,169,0,.45); }

    .sb-hero-right { position: relative; z-index: 1; flex-shrink: 0; margin-left: 24px; }

    .sb-date-box {
      background: rgba(255,255,255,.13);
      border: 1px solid rgba(255,255,255,.2);
      border-radius: 16px; padding: 18px 28px; text-align: center;
    }

    .sb-date-box .day   { font-family: var(--font-h); font-size: 40px; font-weight: 800; color: #fff; line-height: 1; }
    .sb-date-box .month { font-size: 13px; color: rgba(255,255,255,.65); margin-top: 4px; letter-spacing: 1px; text-transform: uppercase; }
    .sb-date-box .dow   { font-size: 11px; color: rgba(255,255,255,.5); margin-top: 3px; }

    /* ── KPI GRID ── */
    .sb-kpi-grid {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 18px; margin-bottom: 26px;
    }

    .sb-kpi {
      background: var(--sb-white); border-radius: 18px;
      padding: 22px 22px 20px 28px;
      position: relative; overflow: hidden;
      transition: transform .2s, box-shadow .2s;
    }

    .sb-kpi:hover { transform: translateY(-3px); box-shadow: 0 14px 36px rgba(16,42,67,.1); }

    .sb-kpi-accent {
      position: absolute; left: 0; top: 0; bottom: 0;
      width: 5px; border-radius: 18px 0 0 18px;
    }

    .sb-kpi-icon {
      width: 46px; height: 46px; border-radius: 12px;
      display: flex; align-items: center; justify-content: center;
      margin-bottom: 18px;
    }

    .sb-kpi-icon i { color: #fff; font-size: 20px; }

    .sb-kpi-num {
      font-family: var(--font-h); font-size: 32px;
      font-weight: 800; line-height: 1; letter-spacing: -1px;
    }

    .sb-kpi-label {
      font-size: 11px; font-weight: 600;
      text-transform: uppercase; letter-spacing: 0.9px;
      color: var(--sb-muted); margin-top: 7px;
    }

    .sb-kpi-trend {
      font-size: 12px; margin-top: 10px;
      display: flex; align-items: center; gap: 5px;
      font-weight: 600; font-family: var(--font-h);
    }

    /* ── 2-COL GRID ── */
    .sb-grid-2 {
      display: grid;
      grid-template-columns: 310px 1fr;
      gap: 18px; margin-bottom: 18px;
    }

    /* ── CARD ── */
    .sb-card { background: var(--sb-white); border-radius: 22px; overflow: hidden; }

    .sb-card-head {
      padding: 18px 24px 15px;
      display: flex; align-items: center; justify-content: space-between;
      border-bottom: 1px solid #EEF2F6;
    }

    .sb-card-title {
      font-family: var(--font-h); font-size: 14px;
      font-weight: 700; color: var(--sb-primary);
      display: flex; align-items: center; gap: 9px;
    }

    .sb-card-title-dot { width: 9px; height: 9px; border-radius: 50%; flex-shrink: 0; }

    .sb-btn-sm {
      font-size: 12px; padding: 6px 16px; border-radius: 20px;
      border: 1.5px solid var(--sb-primary);
      background: transparent; color: var(--sb-primary);
      font-family: var(--font-h); font-weight: 700;
      cursor: pointer; transition: all .15s;
      text-decoration: none; display: inline-flex; align-items: center; gap: 5px;
    }

    .sb-btn-sm:hover { background: var(--sb-primary); color: #fff; }

    .sb-card-body { padding: 18px 22px; }

    /* ── ACCESOS RAPIDOS ── */
    .sb-action-item {
      display: flex; align-items: center; justify-content: space-between;
      padding: 11px 13px; border-radius: 12px;
      background: var(--sb-surface); border: 1px solid #DDE8F5;
      margin-bottom: 10px; cursor: pointer;
      transition: all .15s; text-decoration: none;
    }

    .sb-action-item:last-child { margin-bottom: 0; }

    .sb-action-item:hover {
      background: var(--sb-primary); border-color: var(--sb-primary);
    }

    .sb-action-item:hover .sb-action-label { color: #fff; }
    .sb-action-item:hover .sb-action-arrow { color: rgba(255,255,255,.6); }

    .sb-action-left { display: flex; align-items: center; gap: 12px; }

    .sb-action-icon {
      width: 36px; height: 36px; border-radius: 10px;
      display: flex; align-items: center; justify-content: center;
    }

    .sb-action-icon i { color: #fff; font-size: 15px; }

    .sb-action-label {
      font-size: 13px; font-weight: 600;
      color: var(--sb-text); font-family: var(--font-h);
      transition: color .15s;
    }

    .sb-action-arrow { font-size: 18px; color: var(--sb-muted); transition: color .15s; line-height: 1; }

    /* ── OTP STRIP ── */
    .sb-otp-strip {
      background: linear-gradient(120deg, var(--sb-purple) 0%, #8E44AD 100%);
      border-radius: 12px; padding: 14px 18px;
      display: flex; align-items: center; justify-content: space-between;
      margin: 0 22px 20px;
    }

    .sb-otp-left { display: flex; align-items: center; gap: 12px; }

    .sb-otp-icon {
      width: 42px; height: 42px;
      background: rgba(255,255,255,.15); border-radius: 11px;
      display: flex; align-items: center; justify-content: center;
    }

    .sb-otp-icon i { color: #fff; font-size: 18px; }

    .sb-otp-text h4 { font-family: var(--font-h); font-size: 13px; font-weight: 700; color: #fff; margin-bottom: 2px; }
    .sb-otp-text p  { font-size: 11px; color: rgba(255,255,255,.7); }

    .sb-otp-badge {
      background: rgba(255,255,255,.18);
      border: 1px solid rgba(255,255,255,.28);
      border-radius: 20px; padding: 5px 14px;
      font-size: 11px; font-weight: 700; color: #fff;
      font-family: var(--font-h); white-space: nowrap;
    }

    /* ── TABLE ── */
    .sb-table-wrap { overflow-x: auto; }

    .sb-table { width: 100%; border-collapse: collapse; font-size: 13px; }

    .sb-table thead th {
      background: #F0F5FA; color: var(--sb-primary);
      font-family: var(--font-h); font-size: 11px; font-weight: 700;
      text-transform: uppercase; letter-spacing: 0.8px;
      padding: 12px 16px; border: none; text-align: left; white-space: nowrap;
    }

    .sb-table tbody td {
      padding: 12px 16px; border-bottom: 1px solid #EEF2F6;
      color: var(--sb-text); vertical-align: middle;
    }

    .sb-table tbody tr:last-child td { border-bottom: none; }
    .sb-table tbody tr:hover td { background: #F8FAFC; }

    /* ── BADGES ── */
    .sb-badge {
      display: inline-flex; align-items: center;
      padding: 4px 12px; border-radius: 20px;
      font-size: 11px; font-weight: 700;
      font-family: var(--font-h); white-space: nowrap;
    }

    .sb-badge.programada { background: #FEF3CD; color: #A16E00; }
    .sb-badge.confirmada { background: #D4EDDA; color: #1A6B2E; }
    .sb-badge.atendida   { background: #CCE5FF; color: #0B4E96; }
    .sb-badge.cancelada  { background: #FADBD8; color: #9B2030; }

    .sb-record-id {
      background: #EAF2F8; color: var(--sb-primary);
      border-radius: 999px; padding: 4px 10px;
      font-weight: 700; font-size: 12px;
      font-family: var(--font-h); white-space: nowrap;
    }

    /* ── BTN ICON ── */
    .sb-action-btns { display: flex; gap: 6px; }

    .sb-btn-icon {
      width: 30px; height: 30px; border-radius: 8px;
      border: none; cursor: pointer;
      display: inline-flex; align-items: center; justify-content: center;
      transition: all .15s;
    }

    .sb-btn-icon i  { color: #fff; font-size: 12px; }
    .btn-edit  { background: var(--sb-amber); }
    .btn-edit:hover  { background: #D68910; }
    .btn-del   { background: var(--sb-red); }
    .btn-del:hover   { background: #CB4335; }
    .btn-view  { background: var(--sb-celeste); }
    .btn-view:hover  { background: #2471A3; }

    /* ── SPECIALTY GRID ── */
    .sb-spec-grid { display: grid; grid-template-columns: repeat(3,1fr); gap: 13px; }

    .sb-spec-card {
      background: var(--sb-surface); border: 1px solid #DDE8F5;
      border-radius: 12px; padding: 15px 17px; transition: all .15s;
    }

    .sb-spec-card:hover { border-color: var(--sb-celeste); }

    .sb-spec-lote {
      display: inline-block; color: #fff; font-size: 10px; font-weight: 700;
      padding: 2px 9px; border-radius: 999px; margin-bottom: 8px;
      font-family: var(--font-h); letter-spacing: 0.5px;
    }

    .sb-spec-name { font-family: var(--font-h); font-size: 13px; font-weight: 700; color: var(--sb-primary); margin-bottom: 6px; }

    .sb-spec-row { font-size: 11.5px; color: var(--sb-muted); margin-top: 4px; }

    .sb-spec-add {
      background: #F0F8FF; border: 2px dashed #BDD5EA;
      border-radius: 12px; display: flex; align-items: center;
      justify-content: center; flex-direction: column; gap: 6px;
      cursor: pointer; transition: all .15s; min-height: 108px;
    }

    .sb-spec-add:hover { border-color: var(--sb-celeste); background: #E3F2FD; }
    .sb-spec-add-plus { font-size: 28px; color: var(--sb-celeste); font-weight: 300; line-height: 1; }
    .sb-spec-add-label { font-size: 12px; color: var(--sb-muted); font-weight: 600; font-family: var(--font-h); }

    /* ── SECTION ── */
    .sb-section { margin-bottom: 18px; }

    /* ── FOOTER ── */
    .sb-footer {
      text-align: center; padding: 20px;
      font-size: 11.5px; color: var(--sb-muted);
      border-top: 1px solid #DDE8F5; margin-top: 10px;
    }

    /* ── MODAL ── */
    .modal-header.bg-danger { background: var(--sb-red) !important; }

    /* ── RESPONSIVE ── */
    @media (max-width: 1024px) {
      .sb-kpi-grid { grid-template-columns: repeat(2,1fr); }
      .sb-grid-2   { grid-template-columns: 1fr; }
      .sb-spec-grid { grid-template-columns: repeat(2,1fr); }
    }

    @media (max-width: 640px) {
      .sb-navbar { padding: 0 14px; }
      .sb-nav-links, .sb-lang-badge { display: none; }
      .sb-main { padding: 16px 14px 40px; }
      .sb-kpi-grid { grid-template-columns: 1fr 1fr; gap: 12px; }
      .sb-hero { flex-direction: column; gap: 18px; padding: 22px; }
      .sb-hero-right { margin-left: 0; width: 100%; }
      .sb-spec-grid { grid-template-columns: 1fr; }
    }
  </style>
</head>
<body>

<nav class="sb-navbar">
  <a href="${pageContext.request.contextPath}/dashboard" class="sb-logo">
    <div class="sb-logo-icon">
      <i class="fas fa-hospital-alt"></i>
    </div>
    <div>
      <div class="sb-logo-name">SaludBoyaca</div>
      <div class="sb-logo-sub"><fmt:message key="app.subtitulo" /></div>
    </div>
  </a>

  <div class="sb-nav-links">
    <a href="${pageContext.request.contextPath}/dashboard" class="sb-nav-link active">
      <i class="fas fa-th-large"></i> <fmt:message key="menu.dashboard" />
    </a>
    <a href="${pageContext.request.contextPath}/pacientes" class="sb-nav-link">
      <i class="fas fa-users"></i> <fmt:message key="menu.pacientes" />
    </a>
    <a href="${pageContext.request.contextPath}/registros" class="sb-nav-link">
      <i class="fas fa-calendar-check"></i> <fmt:message key="menu.registros" />
    </a>
    <a href="${pageContext.request.contextPath}/vacunas" class="sb-nav-link">
      <i class="fas fa-clock"></i> <fmt:message key="menu.vacunas" />
    </a>
    <a href="${pageContext.request.contextPath}/consulta" target="_blank" class="sb-nav-link">
      <i class="fas fa-search"></i> <fmt:message key="menu.consulta" />
    </a>
  </div>

  <div class="sb-nav-right">
    <div class="sb-lang-badge">
      <a href="?lang=es" style="text-decoration:none;color:inherit;">ES</a>
      <span style="opacity:.4;">|</span>
      <a href="?lang=en" style="text-decoration:none;color:inherit;">EN</a>
      <span style="opacity:.4;">|</span>
      <a href="?lang=fr" style="text-decoration:none;color:inherit;">FR</a>
      <span style="opacity:.4;">|</span>
      <a href="?lang=it" style="text-decoration:none;color:inherit;">IT</a>
    </div>

    <div class="sb-user-pill">
      <div class="sb-avatar">AD</div>
      <div>
        <div class="sb-user-name">${sessionScope.usuarioNombre}</div>
        <div class="sb-user-role">${sessionScope.usuarioRol}</div>
      </div>
    </div>

    <a href="${pageContext.request.contextPath}/logout" class="sb-logout-btn">
      <i class="fas fa-sign-out-alt"></i> <fmt:message key="menu.salir" />
    </a>
  </div>
</nav>


<main class="sb-main">

  <div class="sb-hero">
    <div class="sb-hero-left">
      <h1><i class="fas fa-hospital-alt me-2"></i>
        <fmt:message key="dashboard.bienvenido" />, ${sessionScope.usuarioNombre}
      </h1>
      <p><fmt:message key="dashboard.subtitulo" /></p>
      <div class="sb-hero-chips">
        <span class="sb-chip green">&#9679; <fmt:message key="dashboard.sistemaActivo" /></span>
        <span class="sb-chip">&#128274; <fmt:message key="dashboard.sesionVerificada" /></span>
        <span class="sb-chip"><fmt:message key="dashboard.rol" />: ${sessionScope.usuarioRol}</span>
      </div>
    </div>
    <div class="sb-hero-right">
      <div class="sb-date-box">
        <div class="day"   id="hero-day">--</div>
        <div class="month" id="hero-month">---</div>
        <div class="dow"   id="hero-dow">---</div>
      </div>
    </div>
  </div>

  <div class="sb-kpi-grid">
    <div class="sb-kpi">
      <div class="sb-kpi-accent" style="background:var(--sb-primary);"></div>
      <div class="sb-kpi-icon" style="background:var(--sb-primary);">
        <i class="fas fa-calendar-day"></i>
      </div>
      <div class="sb-kpi-num" style="color:var(--sb-primary);">
        <c:set var="citasHoy" value="0"/>
        <c:forEach var="registro" items="${registros}">
          <c:set var="fechaReg">
            <fmt:formatDate value="${registro.fechaVacunacion}" pattern="yyyy-MM-dd"/>
          </c:set>
          <c:set var="fechaHoy">
            <fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd"/>
          </c:set>
          <c:if test="${fechaReg eq fechaHoy}">
            <c:set var="citasHoy" value="${citasHoy + 1}"/>
          </c:if>
        </c:forEach>
        ${citasHoy}
      </div>
      <div class="sb-kpi-label"><fmt:message key="dashboard.citasHoy" /></div>
      <div class="sb-kpi-trend" style="color:var(--sb-sena);">
        <i class="fas fa-arrow-up"></i> <fmt:message key="dashboard.alDia" />
      </div>
    </div>

    </div>

  <div class="sb-section">
    <div class="sb-card">
      <div class="sb-card-head">
        <div class="sb-card-title">
          <div class="sb-card-title-dot" style="background:var(--sb-primary);"></div>
          <fmt:message key="menu.registros" /> Recientes
        </div>
        
        <c:if test="${sessionScope.usuarioRol != 'ENFERMERO'}">
            <a href="#" class="sb-btn-sm"><i class="fas fa-plus"></i> <fmt:message key="dashboard.nuevo" /></a>
        </c:if>
      </div>
      
      <div class="sb-card-body">
        <div class="sb-table-wrap">
          <table class="sb-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>PACIENTE</th>
                <th>VACUNA</th>
                <th>FECHA</th>
                <th>ESTADO</th>
                <th>ACCIONES</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="reg" items="${registros}">
                <tr>
                  <td><span class="sb-record-id">#${reg.id}</span></td>
                  <td><strong>${reg.pacienteNombre}</strong></td>
                  <td>${reg.vacunaNombre}</td>
                  <td><fmt:formatDate value="${reg.fechaVacunacion}" pattern="dd/MM/yyyy"/></td>
                  <td><span class="sb-badge confirmada">Atendido</span></td>
                  <td class="sb-action-btns">
                    <button class="sb-btn-icon btn-view"><i class="fas fa-eye"></i></button>
                    
                    <c:if test="${sessionScope.usuarioRol != 'ENFERMERO'}">
                        <button class="sb-btn-icon btn-edit"><i class="fas fa-edit"></i></button>
                        <button class="sb-btn-icon btn-del"><i class="fas fa-trash"></i></button>
                    </c:if>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>

  <div class="sb-section">
    <div class="sb-card">
      <div class="sb-card-head">
        <div class="sb-card-title">
          <div class="sb-card-title-dot" style="background:var(--sb-celeste);"></div>
          Disponibilidad de Biológicos
        </div>
      </div>
      <div class="sb-card-body">
        <div class="sb-spec-grid">
           <div class="sb-spec-card">
              <span class="sb-spec-lote" style="background:var(--sb-sena);">LOTE: AB123</span>
              <div class="sb-spec-name">Pfizer-BioNTech</div>
              <div class="sb-spec-row"><i class="fas fa-box me-1"></i> Stock: 150 dosis</div>
              <div class="sb-spec-row"><i class="fas fa-calendar-times me-1"></i> Vence: 12/2026</div>
           </div>

           <c:if test="${sessionScope.usuarioRol != 'ENFERMERO'}">
               <div class="sb-spec-add">
                  <div class="sb-spec-add-plus">+</div>
                  <div class="sb-spec-add-label">Añadir Biológico</div>
               </div>
           </c:if>
        </div>
      </div>
    </div>
  </div>

</main>

<footer class="sb-footer">
  <div>&copy; 2026 SaludBoyaca - Sistema Integrado de Vacunación</div>
  <div class="mt-1">Secretaría de Salud de Boyacá - Desarrollo ADSO</div>
</footer>

<script>
  // Script para actualizar la fecha del Hero Banner
  const updateHeroDate = () => {
    const now = new Date();
    const days = ['Domingo','Lunes','Martes','Miércoles','Jueves','Viernes','Sábado'];
    const months = ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'];
    
    document.getElementById('hero-day').innerText = now.getDate().toString().padStart(2, '0');
    document.getElementById('hero-month').innerText = months[now.getMonth()];
    document.getElementById('hero-dow').innerText = days[now.getDay()];
  };
  updateHeroDate();
</script>

</body>
</html>
