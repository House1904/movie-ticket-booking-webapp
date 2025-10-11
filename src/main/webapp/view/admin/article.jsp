<%--
  Created by IntelliJ IDEA.
  User: Thanh Truc
  Date: 10/9/2025
  Time: 3:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý tin tức</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-article-banner.css">
</head>
<body>
<div class="admin-strip"></div>
<div class="admin-container">
    <!-- Phần tiêu đề -->
    <div class="admin-header">
        <h1>QUẢN LÝ TIN TỨC</h1>
    </div>

    <!-- Hiển thị thông báo lỗi -->
    <c:if test="${not empty errorMessage}">
        <div style="color: red; margin-bottom: 20px;">${errorMessage}</div>
    </c:if>

    <!-- Form chỉnh sửa (nếu có article trong request) -->
    <c:if test="${not empty article}">
        <section class="form-section">
            <h2>Chỉnh sửa tin tức</h2>
            <form action="${pageContext.request.contextPath}/admin/articles" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="url" value="${article.url}">
                <div class="form-group">
                    <label for="title">Tiêu đề bài viết:</label>
                    <input type="text" id="title" name="title" value="${article.title}" required>
                </div>
                <div class="form-group">
                    <label for="content">Nội dung bài viết:</label>
                    <textarea id="content" name="content" required>${article.content}</textarea>
                </div>
                <div class="form-group">
                    <label for="imageUrl">URL hình ảnh:</label>
                    <input type="url" id="imageUrl" name="imageUrl" value="${article.image}">
                </div>
                <button type="submit" class="submit-button">Cập nhật bài viết</button>
            </form>
        </section>
    </c:if>

    <!-- Phần danh sách tin tức -->
    <div class="list-section">
        <h2>Danh sách tin tức</h2>
        <c:choose>
            <c:when test="${empty articles or articles == null}">
                <div style="color: #666; margin-bottom: 20px;">Không có bài viết nào để hiển thị hoặc lỗi kết nối database.</div>
            </c:when>
            <c:otherwise>
                <table class="article-list">
                    <thead>
                    <tr>
                        <th>Tiêu đề</th>
                        <th>Nội dung sơ lược</th>
                        <th>Link URL</th>
                        <th>Thời gian tạo</th>
                        <th>Ảnh bìa</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="article" items="${articles}">
                        <tr>
                            <td>${article.title}</td>
                            <td>${article.content}</td>
                            <td><a href="${article.url}" target="_blank">${article.url}</a></td>
                            <td>${article.created_at}</td>
                            <td>
                                <c:if test="${not empty article.image}">
                                    <img src="${article.image}" alt="Ảnh bìa" width="50">
                                </c:if>
                            </td>
                            <td class="action-buttons">
                                <a href="${pageContext.request.contextPath}/admin/articles?action=edit&url=${article.url}">
                                    <button>Chỉnh sửa</button>
                                </a>
                                <form action="${pageContext.request.contextPath}/admin/articles" method="post" style="display:inline;" onsubmit="return confirm('Bạn có chắc chắn muốn xóa bài viết này?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="url" value="${article.url}">
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

    <!-- Form thêm tin tức -->
    <section class="form-section">
        <h2>Thêm tin tức mới</h2>
        <form action="${pageContext.request.contextPath}/admin/articles" method="post">
            <div class="form-group">
                <label for="title">Tiêu đề bài viết:</label>
                <input type="text" id="title" name="title" placeholder="Nhập tiêu đề..." required>
            </div>
            <div class="form-group">
                <label for="content">Nội dung bài viết:</label>
                <textarea id="content" name="content" placeholder="Nhập nội dung..." required></textarea>
            </div>
            <div class="form-group">
                <label for="url">URL bài viết:</label>
                <input type="text" id="url" name="url" placeholder="Nhập URL (ví dụ: https://example.com/article1)" required>
            </div>
            <div class="form-group">
                <label for="imageUrl">URL hình ảnh:</label>
                <input type="url" id="imageUrl" name="imageUrl" placeholder="Dán link ảnh vào đây...">
            </div>
            <button type="submit" class="submit-button">Thêm bài viết</button>
        </form>
    </section>
</div>
</body>
</html>