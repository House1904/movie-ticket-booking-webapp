<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thanh toán thất bại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/fail.css">
</head>
<body>
<div class="container fail">
    <div class="icon">❌</div>
    <h2>Thanh toán thất bại</h2>
    <p>Rất tiếc, giao dịch của bạn chưa được xử lý thành công.</p>
    <p>Vui lòng thử lại hoặc chọn phương thức thanh toán khác.</p>

    <div class="btn-group">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-home">🏠 Về trang chủ</a>
        <a href="javascript:history.back()" class="btn btn-retry">🔁 Thử lại</a>
    </div>
</div>
</body>
</html>
