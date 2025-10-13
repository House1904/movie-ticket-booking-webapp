<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý rạp chiếu phim</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f5f6fa; margin: 0; padding: 0; }
        .container { width: 50%; margin: 50px auto; text-align: center; }
        h2 { color: #2f3640; }
        .btn { display: inline-block; padding: 10px 20px; background-color: #0984e3; color: white; border: none; border-radius: 6px; text-decoration: none; }
        .btn:hover { background-color: #74b9ff; }
    </style>
</head>
<body>
<div class="container">
    <h2>Chào mừng đến với hệ thống quản lý rạp chiếu phim</h2>
    <a class="btn" href="${pageContext.request.contextPath}/CinemaController?action=list">Quản lý rạp</a>
</div>
</body>
</html>