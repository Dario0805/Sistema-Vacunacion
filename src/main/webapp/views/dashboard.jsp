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

  <!-- Bootstrap 5 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <!-- Font Awesome -->
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet"/>
  <!-- Google Fonts -->
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

<!-- ═══ NAVBAR ═══ -->
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


<!-- ═══ MAIN ═══ -->
<main class="sb-main">

  <!-- HERO BANNER -->
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

  <!-- KPI CARDS -->
  <div class="sb-kpi-grid">

    <div class="sb-kpi">
      <div class="sb-kpi-accent" style="background:var(--sb-primary);"></div>
      <div class="sb-kpi-icon" style="background:var(--sb-primary);">
        <i class="fas fa-calendar-day"></i>
      </div>
      <div class="sb-kpi-num" style="color:var(--sb-primary);">
        <%-- Calcular citas de hoy --%>
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
        <i class="fas fa-arrow-up"></i> <fmt:message key="dashboard.actualizadas" />
      </div>
    </div>

    <div class="sb-kpi">
      <div class="sb-kpi-accent" style="background:var(--sb-amber);"></div>
      <div class="sb-kpi-icon" style="background:var(--sb-amber);">
        <i class="fas fa-clock"></i>
      </div>
      <div class="sb-kpi-num" style="color:var(--sb-amber);">${totalRegistros}</div>
      <div class="sb-kpi-label"><fmt:message key="dashboard.totalRegistros" /></div>
      <div class="sb-kpi-trend" style="color:var(--sb-amber);">
        <i class="fas fa-equals"></i> <fmt:message key="dashboard.acumulado" />
      </div>
    </div>

    <div class="sb-kpi">
      <div class="sb-kpi-accent" style="background:var(--sb-sena);"></div>
      <div class="sb-kpi-icon" style="background:var(--sb-sena);">
        <i class="fas fa-vials"></i>
      </div>
      <div class="sb-kpi-num" style="color:var(--sb-sena);">${totalVacunas}</div>
      <div class="sb-kpi-label"><fmt:message key="dashboard.totalVacunas" /></div>
      <div class="sb-kpi-trend" style="color:var(--sb-sena);">
        <i class="fas fa-check-circle"></i> <fmt:message key="dashboard.inventario" />
      </div>
    </div>

    <div class="sb-kpi">
      <div class="sb-kpi-accent" style="background:var(--sb-celeste);"></div>
      <div class="sb-kpi-icon" style="background:var(--sb-celeste);">
        <i class="fas fa-calendar-alt"></i>
      </div>
      <div class="sb-kpi-num" style="color:var(--sb-celeste); font-size:1.3rem;">
        <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy"/>
      </div>
      <div class="sb-kpi-label"><fmt:message key="dashboard.fechaSistema" /></div>
      <div class="sb-kpi-trend" style="color:var(--sb-celeste);">
        <i class="fas fa-sync-alt"></i> <fmt:message key="dashboard.tiempoReal" />
      </div>
    </div>

  </div>

  <!-- 2-COL SECTION -->
  <div class="sb-grid-2">

    <!-- LEFT: Accesos rapidos + OTP strip -->
    <div class="sb-card">
      <div class="sb-card-head">
        <div class="sb-card-title">
          <div class="sb-card-title-dot" style="background:var(--sb-sena);"></div>
          <fmt:message key="dashboard.modulosPrincipales" />
        </div>
      </div>
      <div class="sb-card-body">

        <a href="${pageContext.request.contextPath}/registros?accion=nuevo" class="sb-action-item">
          <div class="sb-action-left">
            <div class="sb-action-icon" style="background:var(--sb-primary);">
              <i class="fas fa-clipboard-list"></i>
            </div>
            <span class="sb-action-label"><fmt:message key="dashboard.nuevoRegistro" /></span>
          </div>
          <span class="sb-action-arrow">&#8250;</span>
        </a>

        <a href="${pageContext.request.contextPath}/pacientes?accion=nuevo" class="sb-action-item">
          <div class="sb-action-left">
            <div class="sb-action-icon" style="background:var(--sb-sena);">
              <i class="fas fa-user-plus"></i>
            </div>
            <span class="sb-action-label"><fmt:message key="dashboard.nuevoPaciente" /></span>
          </div>
          <span class="sb-action-arrow">&#8250;</span>
        </a>

        <a href="${pageContext.request.contextPath}/vacunas?accion=nuevo" class="sb-action-item">
          <div class="sb-action-left">
            <div class="sb-action-icon" style="background:var(--sb-celeste);">
              <i class="fas fa-syringe"></i>
            </div>
            <span class="sb-action-label"><fmt:message key="dashboard.nuevaVacuna" /></span>
          </div>
          <span class="sb-action-arrow">&#8250;</span>
        </a>

        <a href="${pageContext.request.contextPath}/usuarios?accion=nuevo" class="sb-action-item">
          <div class="sb-action-left">
            <div class="sb-action-icon" style="background:#7F8C8D;">
              <i class="fas fa-user-md"></i>
            </div>
            <span class="sb-action-label"><fmt:message key="dashboard.nuevoUsuario" /></span>
          </div>
          <span class="sb-action-arrow">&#8250;</span>
        </a>

        <a href="${pageContext.request.contextPath}/consulta" target="_blank" class="sb-action-item">
          <div class="sb-action-left">
            <div class="sb-action-icon" style="background:#2C3E50;">
              <i class="fas fa-search"></i>
            </div>
            <span class="sb-action-label"><fmt:message key="menu.consulta" /></span>
          </div>
          <span class="sb-action-arrow">&#8250;</span>
        </a>

      </div>

      <!-- OTP STATUS STRIP -->
      <div class="sb-otp-strip">
        <div class="sb-otp-left">
          <div class="sb-otp-icon">
            <i class="fas fa-shield-alt"></i>
          </div>
          <div class="sb-otp-text">
            <h4><fmt:message key="dashboard.sesionAutenticada" /></h4>
            <p><fmt:message key="dashboard.accesoSeguro" /></p>
          </div>
        </div>
        <div class="sb-otp-badge">&#9679; <fmt:message key="dashboard.activo" /></div>
      </div>
    </div>

    <!-- RIGHT: Tabla de registros recientes -->
    <div class="sb-card">
      <div class="sb-card-head">
        <div class="sb-card-title">
          <div class="sb-card-title-dot" style="background:var(--sb-primary);"></div>
          <fmt:message key="dashboard.ultimosRegistros" />
        </div>
        <a href="${pageContext.request.contextPath}/registros" class="sb-btn-sm">
          <i class="fas fa-eye"></i> <fmt:message key="dashboard.verTodos" />
        </a>
      </div>
      <div class="sb-table-wrap">
        <table class="sb-table">
          <thead>
            <tr>
              <th>ID</th>
              <th><fmt:message key="tabla.paciente" /></th>
              <th><fmt:message key="tabla.documento" /></th>
              <th><fmt:message key="tabla.vacuna" /></th>
              <th><fmt:message key="tabla.fecha" /></th>
              <th><fmt:message key="tabla.acciones" /></th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="registro" items="${registros}">
              <tr>
                <td><span class="sb-record-id">#${registro.id}</span></td>
                <td><strong>${registro.nombrePaciente}</strong></td>
                <td>${registro.documentoPaciente}</td>
                <td>${registro.nombreVacuna} <small style="color:var(--sb-muted);">(${registro.loteVacuna})</small></td>
                <td><fmt:formatDate value="${registro.fechaVacunacion}" pattern="dd/MM/yyyy HH:mm"/></td>
                <td>
                  <div class="sb-action-btns">
                    <a href="${pageContext.request.contextPath}/registros?accion=editar&id=${registro.id}"
                       class="sb-btn-icon btn-edit" title="<fmt:message key='accion.editar' />">
                      <i class="fas fa-edit"></i>
                    </a>
                    <button class="sb-btn-icon btn-del" title="<fmt:message key='accion.eliminar' />"
                            onclick="confirmarEliminar(${registro.id}, '${registro.nombrePaciente}')">
                      <i class="fas fa-trash"></i>
                    </button>
                  </div>
                </td>
              </tr>
            </c:forEach>
            <c:if test="${empty registros}">
              <tr>
                <td colspan="6" style="text-align:center;padding:28px;color:var(--sb-muted);">
                  <i class="fas fa-inbox" style="font-size:24px;display:block;margin-bottom:8px;"></i>
                  <fmt:message key="dashboard.noRegistros" />
                </td>
              </tr>
            </c:if>
          </tbody>
        </table>
      </div>
    </div>

  </div>

  <!-- VACUNAS SECTION -->
  <div class="sb-section">
    <div class="sb-card">
      <div class="sb-card-head">
        <div class="sb-card-title">
          <div class="sb-card-title-dot" style="background:var(--sb-celeste);"></div>
          <fmt:message key="dashboard.inventarioReciente" />
        </div>
        <a href="${pageContext.request.contextPath}/vacunas" class="sb-btn-sm">
          <i class="fas fa-boxes"></i> <fmt:message key="dashboard.gestionar" />
        </a>
      </div>
      <div class="sb-card-body">
        <div class="sb-spec-grid">
          <c:forEach var="vacuna" items="${vacunas}" end="4">
            <div class="sb-spec-card">
              <div class="sb-spec-lote" style="background:var(--sb-primary);">${vacuna.lote}</div>
              <div class="sb-spec-name">${vacuna.nombre}</div>
              <div class="sb-spec-row"><i class="fas fa-flask me-1"></i>${vacuna.laboratorio}</div>
              <div class="sb-spec-row"><i class="fas fa-calendar-times me-1"></i><fmt:message key="dashboard.vence" />: ${vacuna.fechaVencimiento}</div>
            </div>
          </c:forEach>
          <div class="sb-spec-add" onclick="location.href='${pageContext.request.contextPath}/vacunas?accion=nuevo'">
            <div class="sb-spec-add-plus">+</div>
            <div class="sb-spec-add-label"><fmt:message key="dashboard.nuevaVacuna" /></div>
          </div>
        </div>
      </div>
    </div>
  </div>

</main>


<!-- FOOTER -->
<footer class="sb-footer">
  SENA &nbsp;&middot;&nbsp; Centro Industrial de Mantenimiento y Manufactura - CIMM
  &nbsp;&middot;&nbsp; Regional Boyaca &nbsp;&middot;&nbsp; Tecnologo ADSO &nbsp;&middot;&nbsp; 2026
  &nbsp;|&nbsp; SaludBoyaca v1.0
</footer>


<!-- MODAL ELIMINAR -->
<div class="modal fade" id="eliminarModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content border-0 rounded-4">
      <div class="modal-header bg-danger text-white">
        <h5 class="modal-title">
          <i class="fas fa-trash me-2"></i> <fmt:message key="modal.eliminarRegistro" />
        </h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <p><fmt:message key="modal.confirmarEliminar" /></p>
        <p class="text-danger mb-0"><strong id="nombrePaciente"></strong></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message key="accion.cancelar" /></button>
        <a href="#" id="btnConfirmarEliminar" class="btn btn-danger"><fmt:message key="accion.eliminar" /></a>
      </div>
    </div>
  </div>
</div>


<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
  /* Fecha en vivo en el hero */
  (function () {
    var d = new Date();
    var lang = '${sessionScope.locale.language}';

    var dias = {
      es: ['Domingo','Lunes','Martes','Miercoles','Jueves','Viernes','Sabado'],
      en: ['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'],
      fr: ['Dimanche','Lundi','Mardi','Mercredi','Jeudi','Vendredi','Samedi'],
      it: ['Domenica','Lunedi','Martedi','Mercoledi','Giovedi','Venerdi','Sabato']
    };

    var meses = {
      es: ['Enero','Febrero','Marzo','Abril','Mayo','Junio','Julio','Agosto','Septiembre','Octubre','Noviembre','Diciembre'],
      en: ['January','February','March','April','May','June','July','August','September','October','November','December'],
      fr: ['Janvier','Fevrier','Mars','Avril','Mai','Juin','Juillet','Aout','Septembre','Octobre','Novembre','Decembre'],
      it: ['Gennaio','Febbraio','Marzo','Aprile','Maggio','Giugno','Luglio','Agosto','Settembre','Ottobre','Novembre','Dicembre']
    };

    if (!dias[lang]) {
      lang = 'es';
    }

    document.getElementById('hero-day').textContent   = d.getDate();
    document.getElementById('hero-month').textContent = meses[lang][d.getMonth()] + ' ' + d.getFullYear();
    document.getElementById('hero-dow').textContent   = dias[lang][d.getDay()];
  })();

  /* Modal eliminar */
  function confirmarEliminar(id, nombre) {
    document.getElementById('nombrePaciente').textContent = nombre;
    document.getElementById('btnConfirmarEliminar').href =
      '${pageContext.request.contextPath}/registros?accion=eliminar&id=' + id;
    new bootstrap.Modal(document.getElementById('eliminarModal')).show();
  }
</script>

</body>
</html>
