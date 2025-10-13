<%@ include file="/common/header.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>

<html>
<head>
    <title>Chọn khuyến mãi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/select_promo.css">
</head>
<body>
<main class="promo-wrapper">
    <div class="container">
        <h2>🎁 Chọn khuyến mãi áp dụng</h2>
        <p class="subtext">
            <b>Tổng tạm tính:</b>
            <fmt:formatNumber value="${totalPrice}" type="number" pattern="#,##0 ₫"/>
        </p>

        <form action="${pageContext.request.contextPath}/payment" method="post" id="promoForm">
            <input type="hidden" name="action" value="confirm"/>

            <table>
                <thead>
                <tr>
                    <th>Chọn</th>
                    <th>Tên khuyến mãi</th>
                    <th>Loại</th>
                    <th>Giảm</th>
                    <th>Điều kiện</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${promotions}">
                    <tr>
                        <td>
                            <input type="checkbox"
                                   name="promotionIds"
                                   value="${p.id}"
                                   data-type="${p.promotionType}"
                                   data-value="${p.discountValue}"
                                   data-min="${p.minTotalPrice}"
                                   data-max="${p.maxTotalPrice}">
                        </td>
                        <td>${p.name}</td>
                        <td>
                            <c:choose>
                                <c:when test="${p.promotionType eq 'PERCENT'}">Giảm %</c:when>
                                <c:otherwise>Giảm tiền</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${p.promotionType eq 'PERCENT'}">
                                    -<fmt:formatNumber value="${p.discountValue}" maxFractionDigits="0"/>%
                                </c:when>
                                <c:otherwise>
                                    -<fmt:formatNumber value="${p.discountValue}" type="number" pattern="#,##0 ₫"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            Từ <fmt:formatNumber value="${p.minTotalPrice}" type="number" pattern="#,##0 ₫"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <div class="total-line">
                Tổng sau giảm: <span id="discountedPrice" class="total-amount">
                    <fmt:formatNumber value="${totalPrice}" type="number" pattern="#,##0 ₫"/>
                </span>
            </div>

            <div class="actions">
                <button type="button" class="btn btn-outline" onclick="history.back()">Quay lại</button>
                <button type="submit" class="btn btn-primary">Tiếp tục thanh toán</button>
            </div>
        </form>
    </div>
</main>

<script>
    const baseTotal = Number("${totalPrice}");
    const boxes = document.querySelectorAll('input[name="promotionIds"]');
    const out = document.getElementById('discountedPrice');

    function formatVND(n){ return n.toLocaleString('vi-VN') + ' ₫'; }

    function recompute(){
        let total = baseTotal;
        boxes.forEach(cb=>{
            if(!cb.checked) return;
            const type = cb.dataset.type;
            const val = parseFloat(cb.dataset.value||'0');
            const min = parseFloat(cb.dataset.min||'0');
            if(baseTotal < min) return;
            if(type === 'PERCENT') total -= baseTotal*(val/100);
            else total -= val;
        });
        if(total < 0) total = 0;
        out.textContent = formatVND(total);
    }

    boxes.forEach(cb=>cb.addEventListener('change', recompute));
    recompute();
</script>
</body>
</html>
