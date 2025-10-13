<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý suất chiếu</title>
    <!-- ✅ Đường dẫn CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/partnerShowtime.css">
</head>
<body class="bg-gray-100">

<%@ include file="headerPartner.jsp" %>

<div class="container mx-auto p-6">

    <h1 class="text-2xl font-bold mb-4">Thêm suất chiếu</h1>

    <!-- ⚠️ Hiển thị lỗi -->
    <c:if test="${not empty error}">
        <div style="color: red; font-weight: bold; margin-bottom: 10px;">
                ${error}
        </div>
    </c:if>

    <!-- ✅ Form thêm / cập nhật suất chiếu -->
    <form action="${pageContext.request.contextPath}/manageShowtime" method="post">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="showtimeId" value="${showtime != null ? showtime.id : ''}">
        <input type="hidden" name="auditoriumId" value="${auditorium.id}">

        <!-- Rạp (hiển thị thông tin, không chọn) -->
        <div class="mb-4">
            <label>Rạp</label>
            <p>${cinema.name}</p>
        </div>

        <!-- Phòng chiếu (hiển thị thông tin, không chọn) -->
        <div class="mb-4">
            <label>Phòng chiếu</label>
            <p>${auditorium.name} (${auditorium.format})</p>
        </div>

        <!-- Phim -->
        <div class="mb-4">
            <label>Phim</label>
            <select name="movieId" required>
                <option value="">Chọn phim</option>
                <c:forEach var="movie" items="${movies}">
                    <option value="${movie.id}"
                            <c:if test="${showtime != null && showtime.movie.id == movie.id}">
                                selected
                            </c:if>>
                            ${movie.title}
                    </option>
                </c:forEach>
            </select>
        </div>

        <!-- Thời gian bắt đầu -->
        <div class="mb-4">
            <label>Thời gian bắt đầu</label>
            <input type="datetime-local" name="startTime"
                   value="<c:out value='${showtime != null ? showtime.startTime : ""}'/>" required>
        </div>

        <!-- Ngôn ngữ -->
        <div class="mb-4">
            <label>Ngôn ngữ</label>
            <select name="language" required>
                <option value="Vietsub"
                        <c:if test="${showtime != null && showtime.language == 'Vietsub'}">selected</c:if>>
                    Vietsub
                </option>
                <option value="Lồng tiếng"
                        <c:if test="${showtime != null && showtime.language == 'Lồng tiếng'}">selected</c:if>>
                    Lồng tiếng
                </option>
                <option value="Không phụ đề"
                        <c:if test="${showtime != null && showtime.language == 'Không phụ đề'}">selected</c:if>>
                    Không phụ đề
                </option>
            </select>
        </div>

        <button type="submit" class="bg-blue-500 text-white p-2 rounded">
            <c:out value="${showtime != null ? 'Cập nhật' : 'Thêm'}"/> suất chiếu
        </button>
    </form>

    <!-- ✅ Danh sách suất chiếu -->
    <h2 class="text-xl font-bold mb-4 mt-8">Danh sách suất chiếu</h2>

    <table class="w-full border-collapse border">
        <thead>
        <tr class="bg-gray-200">
            <th class="border p-2">ID</th>
            <th class="border p-2">Rạp</th>
            <th class="border p-2">Phòng</th>
            <th class="border p-2">Phim</th>
            <th class="border p-2">Bắt đầu</th>
            <th class="border p-2">Kết thúc</th>
            <th class="border p-2">Ngôn ngữ</th>
            <th class="border p-2">Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="showtime" items="${showtimes}">
            <tr>
                <td class="border p-2">${showtime.id}</td>
                <td class="border p-2">${showtime.auditorium.cinema.name}</td>
                <td class="border p-2">${showtime.auditorium.name} (${showtime.auditorium.format})</td>
                <td class="border p-2">${showtime.movie.title}</td>
                <td class="border p-2">${showtime.startTime}</td>
                <td class="border p-2">${showtime.endTime}</td>
                <td class="border p-2">${showtime.language}</td>
                <td class="border p-2">
                    <a href="${pageContext.request.contextPath}/manageShowtime?action=editShowtime&id=${showtime.id}"
                       class="text-blue-500">Sửa</a>
                    <a href="${pageContext.request.contextPath}/manageShowtime?action=deleteShowtime&id=${showtime.id}"
                       class="text-red-500"
                       onclick="return confirm('Bạn có chắc muốn xóa suất chiếu này?')">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <button class="back-btn" onclick="window.location.href='${pageContext.request.contextPath}/AuditoriumController?action=list'">Quay lại Trang Phòng chiếu</button>
</div>
<script>   //click 1 lần — trình duyệt luôn nhận confirm() ngay lập tức
document.addEventListener("DOMContentLoaded", function() {
    document.querySelectorAll(".delete-btn").forEach(btn => {
        btn.addEventListener("click", function(e) {
            if (!confirm("Bạn có chắc muốn xóa suất chiếu này?")) {
                e.preventDefault();
            }
        });
    });
});
</script>

</body>
</html>