<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Quản lý Đối tác</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/partner.css">
</head>
<body>
<div class="container">
    <h2>Quản lý Đối tác</h2>

    <div class="form-section">
        <!-- Add/Edit Partner Form -->
        <div class="add-form">
            <h3>${partner == null ? 'Thêm Đối tác Mới' : 'Chỉnh sửa Đối tác'}</h3>
            <form action="partner" method="post">
                <input type="hidden" name="action" value="${partner == null ? 'add' : 'update'}">
                <input type="hidden" name="id" value="${partner.id}">
                <input type="text" name="fullName" value="${partner.fullName}" placeholder="Họ và Tên" required>
                <input type="email" name="email" value="${partner.email}" placeholder="Email" required>
                <input type="text" name="phone" value="${partner.phone}" placeholder="Số điện thoại" required>
                <select name="brand" required>
                    <option value="" ${partner.brand == null ? 'selected' : ''}>Thương hiệu</option>
                    <option value="CGV" ${partner.brand == 'CGV' ? 'selected' : ''}>CGV</option>
                    <option value="Lotte" ${partner.brand == 'Lotte' ? 'selected' : ''}>Lotte</option>
                    <option value="Galaxy" ${partner.brand == 'Galaxy' ? 'selected' : ''}>Galaxy</option>
                    <option value="BHD" ${partner.brand == 'BHD' ? 'selected' : ''}>BHD</option>
                    <option value="Beta" ${partner.brand == 'Beta' ? 'selected' : ''}>Beta</option>
                    <option value="Cinestar" ${partner.brand == 'Cinestar' ? 'selected' : ''}>Cinestar</option>
                    <option value="DCINE" ${partner.brand == 'DCINE' ? 'selected' : ''}>DCINE</option>
                    <option value="Đống Đa" ${partner.brand == 'Đống Đa' ? 'selected' : ''}>Đống Đa</option>
                    <option value="Mega GS" ${partner.brand == 'Mega GS' ? 'selected' : ''}>Mega GS</option>
                </select>
                <button type="submit">Lưu</button>
            </form>
        </div>

        <!-- Partner List -->
        <div class="partner-list">
            <div class="search-box">
                <input type="text" id="searchInput" placeholder="Tìm kiếm Đối tác...">
                <button onclick="searchPartners()">🔍</button>
            </div>
            <div class="cards" id="cardsContainer">
                <c:forEach var="p" items="${partners}">
                    <div class="card">
                        <div class="actions">
                            <a href="partner?action=edit&id=${p.id}" title="Chỉnh sửa">✏️</a>
                            <form action="partner" method="post" style="display: inline;">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${p.id}">
                                <button type="submit" onclick="return confirm('Xóa đối tác này?')" style="margin: 0; padding: 0;">🗑️</button>
                            </form>
                        </div>
                        <div class="brand-title">${p.brand}</div>
                        <h4>${p.fullName}</h4>
                        <small>${p.email}</small>
                        <small>${p.phone}</small>
                        <div class="switch">
                            <label>
                                <input type="checkbox" ${p.is_activate ? 'checked' : ''}>
                                <span class="slider"></span>
                            </label>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<script>
    // Chức năng tìm kiếm theo thời gian thực
    document.getElementById('searchInput').addEventListener('input', function() {
        searchPartners();
    });

    // Chức năng tìm kiếm khi nhấn nút
    function searchPartners() {
        const searchValue = document.getElementById('searchInput').value.toLowerCase();
        const cards = document.querySelectorAll('.card');

        cards.forEach(function(card) {
            const brand = card.querySelector('.brand-title').textContent.toLowerCase();
            const fullName = card.querySelector('h4').textContent.toLowerCase();
            const email = card.querySelector('small').textContent.toLowerCase();
            const phone = card.querySelectorAll('small')[1].textContent.toLowerCase();

            // Kiểm tra nếu bất kỳ trường nào khớp với từ khóa tìm kiếm
            if (brand.includes(searchValue) || fullName.includes(searchValue) || email.includes(searchValue) || phone.includes(searchValue)) {
                card.classList.remove('hidden');
            } else {
                card.classList.add('hidden');
            }
        });
    }
</script>
</body>
</html>