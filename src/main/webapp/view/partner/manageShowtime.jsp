<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý suất chiếu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/movie.css">
</head>
<body class="bg-gray-100">

<div class="container mx-auto p-6">

    <h1 class="text-2xl font-bold mb-4">Thêm suất chiếu</h1>

    <!-- ⚠️ Hiển thị lỗi -->
    <c:if test="${not empty error}">
        <div style="color: red; font-weight: bold; margin-bottom: 10px;">
                ${error}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/manageShowtime" method="post">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="showtimeId" value="${showtime != null ? showtime.id : ''}">

        <div class="mb-4">
            <label>Rạp</label>
            <select name="cinemaId" id="cinemaId" onchange="filterAuditoriums()" required>
                <option value="">Chọn rạp</option>
                <c:forEach var="cinema" items="${cinemas}">
                    <option value="${cinema.id}"
                        ${showtime != null && showtime.auditorium.cinema.id == cinema.id ? 'selected' : ''}>
                            ${cinema.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-4">
            <label>Phim</label>
            <select name="movieId" required>
                <option value="">Chọn phim</option>
                <c:forEach var="movie" items="${movies}">
                    <option value="${movie.id}"
                        ${showtime != null && showtime.movie.id == movie.id ? 'selected' : ''}>
                            ${movie.title}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-4">
            <label>Phòng chiếu</label>
            <select name="auditoriumId" id="auditoriumId" required>
                <option value="">Chọn phòng</option>
                <c:forEach var="auditorium" items="${auditoriums}">
                    <option value="${auditorium.id}"
                            data-cinema-id="${auditorium.cinema.id}"
                        ${showtime != null && showtime.auditorium.id == auditorium.id ? 'selected' : ''}>
                            ${auditorium.name} (${auditorium.format})
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-4">
            <label>Thời gian bắt đầu</label>
            <input type="datetime-local" name="startTime"
                   value="${showtime != null ? showtime.startTime : ''}" required>
        </div>

        <div class="mb-4">
            <label>Ngôn ngữ</label>
            <select name="language" required>
                <option value="Vietsub" ${showtime != null && showtime.language == 'Vietsub' ? 'selected' : ''}>Vietsub</option>
                <option value="Lồng tiếng" ${showtime != null && showtime.language == 'Lồng tiếng' ? 'selected' : ''}>Lồng tiếng</option>
                <option value="Không phụ đề" ${showtime != null && showtime.language == 'Không phụ đề' ? 'selected' : ''}>Không phụ đề</option>
            </select>
        </div>

        <button type="submit" class="bg-blue-500 text-white p-2 rounded">
            ${showtime != null ? 'Cập nhật' : 'Thêm'} suất chiếu
        </button>
    </form>

    <!-- Danh sách suất chiếu -->
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
</div>

<script>
    function filterAuditoriums() {
        const cinemaId = document.getElementById('cinemaId').value;
        const auditoriumSelect = document.getElementById('auditoriumId');
        const options = auditoriumSelect.querySelectorAll('option[data-cinema-id]');
        options.forEach(option => {
            if (cinemaId === '' || option.getAttribute('data-cinema-id') === cinemaId) {
                option.style.display = 'block';
            } else {
                option.style.display = 'none';
            }
        });
        auditoriumSelect.value = '';
    }

    // Khi tải lại trang (chế độ sửa), tự lọc phòng theo rạp
    window.onload = filterAuditoriums;
</script>

</body>
</html>
