<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="today" value="<%= java.time.LocalDateTime.now() %>" />
<%@ include file="header.jsp" %>
<html>
<head>
    <title>Quản lý phim</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/movieManager.css">
</head>

<div id="messageBox" class="popup-message"></div>
<c:if test="${not empty message}">
    <script>
        window.addEventListener('DOMContentLoaded', () => {
            const box = document.getElementById('messageBox');
            box.textContent = '${message}';
            box.classList.add('show');

            setTimeout(() => {
                box.classList.remove('show');
            }, 3000);
        });
    </script>
    <c:remove var="message" scope="session" />
</c:if>
<body>
<div class="container">
    <div class="movie-card">
        <h2 class="mangeTitle">🎬 Quản lý phim</h2>

        <button class="btn btn-add" onclick="openPopup()">➕ Thêm Movie</button>

        <c:choose>
            <c:when test="${empty movies}">
                <p class="text-center text-muted">Chưa có phim nào trong hệ thống.</p>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Poster</th>
                            <th>Tên phim</th>
                            <th>Thể loại</th>
                            <th>Độ tuổi</th>
                            <th>Thời lượng</th>
                            <th>Mô tả</th>
                            <th>Diễn viên</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="m" items="${movies}">
                            <tr>
                                <td>${m.id}</td>
                                <td><img src="${m.posterUrl}" alt="${m.title}" class="poster"></td>
                                <td>${m.title}</td>
                                <td>${m.genre}</td>
                                <td>${m.ageLimit}</td>
                                <td>${m.duration} phút</td>
                                <td>${m.description}</td>
                                <td>${m.actor}</td>
                                <td>
                                        <span class="badge
                                            <c:choose>
                                                <c:when test="${m.releaseDate le today}">bg-success</c:when>
                                                <c:otherwise>bg-warning</c:otherwise>
                                            </c:choose>">
                                            <c:choose>
                                                <c:when test="${m.releaseDate le today}">Đang chiếu</c:when>
                                                <c:otherwise>Sắp chiếu</c:otherwise>
                                            </c:choose>
                                        </span>
                                </td>
                                <td class="text-center">
                                    <!-- Form Edit -->
                                    <button class="btn btn-edit" onclick="openPopup(${m.id}, '${m.title}', '${m.posterUrl}', '${m.trailerUrl}', '${m.ageLimit}', ${m.duration}, '${m.language}', '${m.description}', '${m.genre}', '${m.releaseDate}', '${m.actor}')">✏️</button>
                                    <!-- Form Delete -->
                                    <form action="${pageContext.request.contextPath}/manageMovie" method="post" style="display:inline;"
                                          onsubmit="return confirm('Bạn có chắc muốn xóa phim này không?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${m.id}">
                                        <button type="submit" class="btn btn-delete" title="Xóa phim">🗑️</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<!-- Popup -->
<div id="popup" class="popup">
    <div class="popup-content">
        <h3 id="popup-title">Thêm Movie</h3>
        <form id="movieForm" action="${pageContext.request.contextPath}/manageMovie" method="post">
            <input type="hidden" name="action" value="add" id="form-action">
            <input type="hidden" name="id" value="" id="movie-id">
            <label>Title</label>
            <input type="text" name="title" id="movie-title" required>

            <label>Poster URL</label>
            <input type="text" name="poster" id="movie-poster">

            <label>Trailer URL</label>
            <input type="text" name="trailer" id="movie-trailer">

            <label>Age Limit</label>
            <select name="ageLimit" id="movie-ageLimit">
                <option value="P">P - Mọi lứa tuổi</option>
                <option value="13+">13+</option>
                <option value="16+">16+</option>
                <option value="18+">18+</option>
            </select>
            <label>Genre (thể loại)</label>
            <input type="text" name="genre" id="movie-genre" placeholder="Action, Drama, Comedy..." />

            <label>Diễn viên</label>
            <input type="text" name="actor" id="movie-actor">

            <div class="form-group">
                <label for="movie-releaseDate">Ngày phát hành</label>
                <input type="date" id="movie-releaseDate" name="releaseDate" required>
            </div>

            <label>Duration (phút)</label>
            <input type="number" name="duration" id="movie-duration" min="1">

            <label>Language</label>
            <input type="text" name="language" id="movie-language">

            <label>Description</label>
            <textarea name="description" id="movie-description"></textarea>

            <div class="popup-buttons">
                <button type="submit" class="btn btn-save">💾 Lưu</button>
                <button type="button" class="btn btn-cancel" onclick="closePopup()">❌ Hủy</button>
            </div>
        </form>
    </div>
</div>
<script>
    function openPopup(id, title, poster, trailer, ageLimit, duration, language, description, genre, releaseDate, actor) {
        const popup = document.getElementById("popup");
        const form = document.getElementById("movieForm");
        const popupTitle = document.getElementById("popup-title");
        const actionInput = document.getElementById("form-action");
        const movieId = document.getElementById("movie-id")

        if (id) {
            popupTitle.textContent = "Chỉnh sửa Movie";
            actionInput.value = "edit";
            movieId.value = id;

            document.getElementById("movie-title").value = title || '';
            document.getElementById("movie-poster").value = poster || '';
            document.getElementById("movie-trailer").value = trailer || '';
            document.getElementById("movie-ageLimit").value = ageLimit || '';
            document.getElementById("movie-duration").value = duration || '';
            document.getElementById("movie-language").value = language || '';
            document.getElementById("movie-description").value = description || '';
            document.getElementById("movie-genre").value = (Array.isArray(genre) ? genre.join(', ') : genre.replace(/[\[\]]/g, '')) || '';
            document.getElementById("movie-releaseDate").value = releaseDate ? releaseDate.substring(0, 10) : '';
            document.getElementById("movie-actor").value = actor || '';
        } else {
            popupTitle.textContent = "Thêm Movie";
            actionInput.value = "add";
            form.reset();
        }
        popup.style.display = "flex";
    }

    function closePopup() {
        document.getElementById("popup").style.display = "none";
    }

    window.onclick = function(e) {
        if (e.target === document.getElementById("popup")) {
            closePopup();
        }
    }
</script>
</body>
</html>
