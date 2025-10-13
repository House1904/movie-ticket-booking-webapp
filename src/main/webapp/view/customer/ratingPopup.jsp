<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/rating.css">

<div id="rating-popup" class="rating-popup" style="display:none;">
    <div class="popup-content">
        <span class="close-popup">&times;</span>
        <h3>Đánh giá từ người xem</h3>
        <c:choose>
            <c:when test="${not empty ratings}">
                <c:forEach var="r" items="${ratings}">
                    <div class="rating-item">
                        <div class="rating-header">
                            <strong>${r.customer.fullName}</strong>
                            <span class="stars">
                                <c:forEach begin="1" end="${r.rating}" var="s">⭐</c:forEach>
                            </span>
                            <span class="rating-date">
                                <fmt:formatDate value="${r.createdAtDate}" pattern="dd/MM/yyyy HH:mm"/>
                            </span>
                        </div>
                        <div class="rating-body">
                            <p>${r.content}</p>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>Chưa có đánh giá nào.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>
