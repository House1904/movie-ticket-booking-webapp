<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Dashboard - Doanh thu theo đối tác</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboardAdmin.css">
</head>
<body>
<form action="admin">
    <input type="date" name="date">
</form>

<!-- Biểu đồ đường -->
<div class="chart-container">
    <h2> Doanh thu theo đối tác hôm nay</h2>
    <canvas id="revenueChart" width="400" height="200"></canvas>
</div>

<!-- Biểu đồ cột -->
<div class="chart-container">
    <h2>Top 3 đối tác doanh thu cao nhất</h2>
    <canvas id="topPartnerChart" width="400" height="200"></canvas>
</div>

<!-- Bảng Top 5 phim -->
<div class="chart-container">
    <h2>Top 5 phim doanh thu cao nhất</h2>
    <table>
        <thead>
        <tr><th>Phim</th><th>Doanh thu (VND)</th></tr>
        </thead>
        <tbody>
        <c:forEach var="row" items="${topMovies}">
            <tr>
                <td>${row[0]}</td>
                <td><fmt:formatNumber value="${row[1]}" type="number" groupingUsed="true"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script>
    // Dữ liệu biểu đồ đường
    const lineLabels = [
        <c:forEach var="entry" items="${revenueMap}" varStatus="loop">
        "${entry.key}"<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];
    const lineData = [
        <c:forEach var="entry" items="${revenueMap}" varStatus="loop">
        ${entry.value}<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];

    const ctxLine = document.getElementById('revenueChart').getContext('2d');
    new Chart(ctxLine, {
        type: 'line',
        data: {
            labels: lineLabels,
            datasets: [{
                label: 'Doanh thu (VND)',
                data: lineData,
                borderColor: '#ffd700',
                backgroundColor: 'rgba(255,215,0,0.2)',
                tension: 0.3,
                pointRadius: 5,
                pointBackgroundColor: '#ffd700',
                fill: true
            }]
        },
        options: {
            plugins: { legend: { labels: { color: '#fff' } } },
            scales: {
                x: { ticks: { color: '#fff' }, grid: { color: 'rgba(255,255,255,0.1)' } },
                y: {
                    ticks: { color: '#fff', callback: v => v.toLocaleString('vi-VN') + ' ₫' },
                    grid: { color: 'rgba(255,255,255,0.1)' }
                }
            }
        }
    });

    // Dữ liệu biểu đồ cột (Top 3 đối tác)
    const partnerLabels = [
        <c:forEach var="row" items="${topPartners}" varStatus="loop">
        "${row[0]}"<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];
    const partnerData = [
        <c:forEach var="row" items="${topPartners}" varStatus="loop">
        ${row[1]}<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];

    const ctxBar = document.getElementById('topPartnerChart').getContext('2d');
    new Chart(ctxBar, {
        type: 'bar',
        data: {
            labels: partnerLabels,
            datasets: [{
                label: 'Doanh thu (VND)',
                data: partnerData,
                backgroundColor: ['#ffd700', '#ffb300', '#ff9100']
            }]
        },
        options: {
            plugins: { legend: { labels: { color: '#fff' } } },
            scales: {
                x: { ticks: { color: '#fff' }, grid: { color: 'rgba(255,255,255,0.1)' } },
                y: {
                    ticks: { color: '#fff', callback: v => v.toLocaleString('vi-VN') + ' ₫' },
                    grid: { color: 'rgba(255,255,255,0.1)' }
                }
            }
        }
    });
</script>
</body>
</html>
