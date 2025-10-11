<%--
  Created by IntelliJ IDEA.
  User: Thanh Truc
  Date: 10/11/2025
  Time: 11:19 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý banner</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-article-banner.css">
</head>
<body>
<div class="admin-strip"></div>
<div class="admin-container">
    <!-- Phần tiêu đề -->
    <div class="admin-header">
        <h1>QUẢN LÝ BANNER</h1>
    </div>

    <!-- Hiển thị thông báo lỗi -->
    <c:if test="${not empty errorMessage}">
        <div style="color: red; margin-bottom: 20px;">${errorMessage}</div>
    </c:if>

    <!-- Form chỉnh sửa (nếu có banner trong request) -->
    <c:if test="${not empty banner}">
        <section class="form-section">
            <h2>Chỉnh sửa banner</h2>
            <form action="${pageContext.request.contextPath}/admin/banners" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="link_url" value="${banner.link_url}">
                <div class="form-group">
                    <label for="title">Tiêu đề banner:</label>
                    <input type="text" id="title" name="title" value="${banner.title}" required>
                </div>
                <div class="form-group">
                    <label for="image_url">URL hình ảnh:</label>
                    <input type="url" id="image_url" name="image_url" value="${banner.image_url}">
                </div>
                <div class="form-group">
                    <label for="start_at">Thời gian bắt đầu:</label>
                    <input type="datetime-local" id="start_at" name="start_at" value="${banner.start_at}" required>
                </div>
                <div class="form-group">
                    <label for="end_at">Thời gian kết thúc:</label>
                    <input type="datetime-local" id="end_at" name="end_at" value="${banner.end_at}" required>
                </div>
                <button type="submit" class="submit-button">Cập nhật banner</button>
            </form>
        </section>
    </c:if>

    <!-- Phần danh sách banner -->
    <div class="list-section">
        <h2>Danh sách banner</h2>
        <c:choose>
            <c:when test="${empty banners or banners == null}">
                <div style="color: #666; margin-bottom: 20px;">Không có banner nào để hiển thị hoặc lỗi kết nối database.</div>
            </c:when>
            <c:otherwise>
                <table class="article-list">
                    <thead>
                    <tr>
                        <th>Tiêu đề</th>
                        <th>Link URL</th>
                        <th>Thời gian bắt đầu</th>
                        <th>Thời gian kết thúc</th>
                        <th>Thời gian tạo</th>
                        <th>Ảnh bìa</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="banner" items="${banners}">
                        <tr>
                            <td>${banner.title}</td>
                            <td><a href="${banner.link_url}" target="_blank">${banner.link_url}</a></td>
                            <td>${banner.start_at}</td>
                            <td>${banner.end_at}</td>
                            <td>${banner.created_at}</td>
                            <td>
                                <c:if test="${not empty banner.image_url}">
                                    <img src="${banner.image_url}" alt="Ảnh banner" width="50">
                                </c:if>
                            </td>
                            <td class="action-buttons">
                                <a href="${pageContext.request.contextPath}/admin/banners?action=edit&link_url=${banner.link_url}">
                                    <button>Chỉnh sửa</button>
                                </a>
                                <form action="${pageContext.request.contextPath}/admin/banners" method="post" style="display:inline;" onsubmit="return confirm('Bạn có chắc chắn muốn xóa banner này?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="link_url" value="${banner.link_url}">
                                    <button type="submit">Xóa</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Form thêm banner -->
    <section class="form-section">
        <h2>Thêm banner mới</h2>
        <form action="${pageContext.request.contextPath}/admin/banners" method="post">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label for="title">Tiêu đề banner:</label>
                <input type="text" id="title" name="title" placeholder="Nhập tiêu đề..." required>
            </div>
            <div class="form-group">
                <label for="image_url">URL hình ảnh:</label>
                <input type="url" id="image_url" name="image_url" placeholder="Dán link ảnh vào đây...">
            </div>
            <div class="form-group">
                <label for="link_url">Link URL:</label>
                <input type="text" id="link_url" name="link_url" placeholder="Nhập URL (ví dụ: https://example.com)" required>
            </div>
            <div class="form-group">
                <label for="start_at">Thời gian bắt đầu:</label>
                <input type="datetime-local" id="start_at" name="start_at" required>
            </div>
            <div class="form-group">
                <label for="end_at">Thời gian kết thúc:</label>
                <input type="datetime-local" id="end_at" name="end_at" required>
            </div>
            <button type="submit" class="submit-button">Thêm banner</button>
        </form>
    </section>
</div>
</body>
</html>