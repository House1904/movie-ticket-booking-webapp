<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="model.enums.Status" %>
<html>
<head>
    <title>Lịch sử vé đã mua</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ticketHistory.css">

</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow-sm p-4">
        <h2 class="text-center mb-4 text-primary">🎟️ Lịch sử vé đã mua</h2>

        <c:choose>
            <c:when test="${empty tickets}">
                <p class="text-center text-muted">Bạn chưa mua vé nào.</p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered align-middle">
                        <thead class="table-primary">
                        <tr>
                            <th>#</th>
                            <th>Phim</th>
                            <th>Rạp</th>
                            <th>Ngày chiếu</th>
                            <th>Giờ chiếu</th>
                            <th>Ghế</th>
                            <th>Giá vé</th>
                            <th>Trạng thái</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="t" items="${tickets}" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>${t.showtime.movie.title}</td>
                                <td>${t.showtime.auditorium.cinema.name}</td>
                                <td><fmt:formatDate value="${t.showtime.starttime}" pattern="dd/MM/yyyy"/></td>
                                <td>${t.showtime.starttime}</td>
                                <td><span class="badge bg-secondary">${t.seat.rowLabel}${t.seat.seatNumber}</span></td>
                                <td><fmt:formatNumber value="${t.price}" type="currency" currencySymbol="₫"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${t.status == Status.ISSUED}">
                                            <span class="badge bg-success">Đã thanh toán</span>
                                        </c:when>
                                        <c:when test="${t.status == Status.USED}">
                                            <span class="badge bg-success">Đã sử dụng</span>
                                        </c:when>
                                        <c:when test="${t.status == Status.CANCELLED}">
                                            <span class="badge bg-success">Đã hủy</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-warning text-dark">Đang xử lý</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="text-center mt-3">
            <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-primary">⬅️ Quay lại hồ sơ</a>
        </div>
    </div>
</div>
</body>
</html>
