<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Test Page - Admin</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        .info-box {
            background: #e8f4fd;
            border: 1px solid #bee5eb;
            border-radius: 5px;
            padding: 15px;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Trang Test Giao Diện Admin</h1>
        
        <div class="info-box">
            <h3>Thông tin trang test:</h3>
            <p>Đây là trang test để kiểm tra giao diện admin.</p>
            <p>Bạn có thể truy cập trang này từ:</p>
            <ul>
                <li>Nút "Dashboard" trong header</li>
                <li>Menu "Quản lý đối tác" trong dropdown "Quản lý hệ thống"</li>
            </ul>
        </div>
        
        <h2>Chức năng có sẵn:</h2>
        <ul>
            <li><a href="banner.jsp">Quản lý Banner</a></li>
            <li><a href="article.jsp">Quản lý Tin tức</a></li>
            <li><a href="home.jsp">Trang chủ Admin</a></li>
        </ul>
    </div>
</body>
</html>

