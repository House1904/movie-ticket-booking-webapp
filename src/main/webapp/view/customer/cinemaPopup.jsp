
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- POPUP hiển thị danh sách rạp -->
<div id="rapPopup" class="popup-overlay" style="display: none;">
    <div class="popup-content">
        <span class="close-btn" onclick="closePopup()">&times;</span>
        <h3>🎬 Danh sách rạp phim</h3>

        <div class="cinema-grid">
            <c:forEach var="cinema" items="${cinemas}">
                <div class="cinema-card">
                    <div class="cinema-name">${cinema.name}</div>
                    <div class="cinema-info">
                        <p><strong>Địa chỉ:</strong> ${cinema.address}</p>
                        <p><strong>Liên hệ:</strong> ${cinema.phone}</p>
                    </div>
                    <a href="#" class="cinema-btn">Xem chi tiết</a>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
