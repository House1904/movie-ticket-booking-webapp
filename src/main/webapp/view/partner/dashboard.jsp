<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard Đối Tác</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboardPartner.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .filters { display: flex; gap: 20px; margin-bottom: 20px; }
        .chart-container { margin-bottom: 30px; }
        .table-container { margin-top: 20px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .currency { text-align: right; }
    </style>
</head>
<body>
<%@ include file="headerPartner.jsp" %>

<div class="dashboard-container">
    <div class="main-content">
        <!-- Filters -->
        <div class="filters">
            <form action="${pageContext.request.contextPath}/dashboard" method="get">
                <label for="dateRange">Thời gian:</label>
                <select id="dateRange" name="dateRange">
                    <option value="today" ${dateRange == 'today' ? 'selected' : ''}>Hôm nay</option>
                    <option value="week" ${dateRange == 'week' ? 'selected' : ''}>Tuần này</option>
                    <option value="month" ${dateRange == 'month' ? 'selected' : ''}>Tháng này</option>
                </select>
                <label for="cinemaId">Rạp:</label>
                <select id="cinemaId" name="cinemaId">
                    <option value="">Tất cả rạp</option>
                    <c:forEach var="cinema" items="${cinemas}">
                        <option value="${cinema.id}" ${cinema.id == selectedCinemaId ? 'selected' : ''}>${cinema.name}</option>
                    </c:forEach>
                </select>
                <button type="submit">Lọc</button>
            </form>
        </div>

        <!-- Summary -->
        <div class="summary">
            <h4>Tổng quan</h4>
            <p>Tổng doanh thu: <fmt:formatNumber value="${totalRevenue}" type="currency" currencyCode="VND"/></p>
            <p>Tổng vé bán: <fmt:formatNumber value="${totalTickets}" type="number"/></p>
        </div>

        <!-- Charts -->
        <div class="charts">
            <div class="chart-container">
                <h4>Doanh Thu Theo Ngày</h4>
                <canvas id="revenueChart"></canvas>
            </div>
            <div class="chart-container">
                <h4>Doanh Thu Theo Rạp</h4>
                <canvas id="cinemaChart"></canvas>
            </div>
            <div class="chart-container">
                <h4>Top 5 Phim Hot</h4>
                <canvas id="movieChart"></canvas>
            </div>
        </div>

        <!-- Table -->
        <div class="table-container">
            <h4>Top 5 Phim Bán Chạy</h4>
            <table>
                <thead>
                <tr>
                    <th>Phim</th>
                    <th>Vé Bán</th>
                    <th>Doanh Thu</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="movie" items="${topMovies}">
                    <tr>
                        <td>${movie.title}</td>
                        <td><fmt:formatNumber value="${movie.getTicketsSold(dateRange)}" type="number"/></td>
                        <td class="currency"><fmt:formatNumber value="${movie.getRevenue(dateRange)}" type="currency" currencyCode="VND"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
    new Chart(document.getElementById('revenueChart'), {
        type: 'bar',
        data: {
            labels: [<c:forEach var="day" items="${revenueByDay}">'${day.date}',</c:forEach>],
            datasets: [{
                label: 'Doanh Thu (VND)',
                data: [<c:forEach var="day" items="${revenueByDay}">${day.revenue},</c:forEach>],
                backgroundColor: ['#62b5b1', '#7561b6', '#3966b5', '#eada7b', '#ff6b6b']
            }]
        },
        options: {
            scales: { y: { beginAtZero: true } }
        }
    });

    new Chart(document.getElementById('cinemaChart'), {
        type: 'bar',
        data: {
            labels: [<c:forEach var="cinema" items="${revenueByCinema}">'${cinema.cinemaName}',</c:forEach>],
            datasets: [{
                label: 'Doanh Thu (VND)',
                data: [<c:forEach var="cinema" items="${revenueByCinema}">${cinema.revenue},</c:forEach>],
                backgroundColor: ['#62b5b1', '#7561b6', '#3966b5', '#eada7b', '#ff6b6b']
            }]
        },
        options: {
            scales: { y: { beginAtZero: true } }
        }
    });

    new Chart(document.getElementById('movieChart'), {
        type: 'pie',
        data: {
            labels: [<c:forEach var="movie" items="${topMovies}">'${movie.title}',</c:forEach>],
            datasets: [{
                label: 'Vé Bán',
                data: [<c:forEach var="movie" items="${topMovies}">${movie.getTicketsSold(dateRange)},</c:forEach>],
                backgroundColor: ['#62b5b1', '#7561b6', '#3966b5', '#eada7b', '#ff6b6b']
            }]
        },
        options: {
            responsive: true
        }
    });
</script>

<%@ include file="../../common/footer.jsp" %>
</body>
</html>