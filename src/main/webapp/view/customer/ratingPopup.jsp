<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="rating-popup" class="rating-popup">
    <div class="popup-content">
        <span class="close-popup">&times;</span>
        <h3>Đánh giá từ người xem</h3>
        <div id="rating-list"></div>
    </div>
</div>

<style>
.rating-popup {
    display: none;
    position: fixed;
    top: 0; left: 0;
    width: 100%; height: 100%;
    background-color: rgba(0,0,0,0.6);
    z-index: 9999;
    justify-content: center;
    align-items: center;
}
.popup-content {
    background: #fff;
    padding: 20px 30px;
    border-radius: 12px;
    width: 450px;
    max-height: 80vh;
    overflow-y: auto;
    position: relative;
    box-shadow: 0 4px 12px rgba(0,0,0,0.2);
}
.close-popup {
    position: absolute;
    top: 10px; right: 15px;
    font-size: 24px;
    cursor: pointer;
}
.rating-item {
    border-bottom: 1px solid #ddd;
    padding: 10px 0;
}
.rating-stars {
    color: gold;
    font-size: 18px;
}
</style>
