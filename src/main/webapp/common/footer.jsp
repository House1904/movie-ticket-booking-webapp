<%@ page contentType="text/html; charset=UTF-8" %>
<footer class="footer">
    <div class="footer-container">
        <!-- LEFT: Company Info -->
        <div class="footer-company">
            <div class="footer-logo">
                <img src="${pageContext.request.contextPath}/assets/images/LogoWeb.png" alt="Tenfinity Logo">
                <div class="footer-info">
                    <h4>TENFINITY VIỆT NAM</h4>
                    <p>
                        Số ĐKKD: 0315367026<br>
                        Cấp bởi: Sở KH&ĐT TP.HCM<br>
                        Ngày cấp: 01/11/2018<br>
                        Địa chỉ: Số 1 Võ Văn Ngân, Phường Thủ Đức, TP.HCM<br>
                        <a href="${pageContext.request.contextPath}/page?action=about">Về chúng tôi</a> ·
                        <a href="${pageContext.request.contextPath}/page?action=privacy">Chính sách bảo mật</a> ·
                        <a href="${pageContext.request.contextPath}/page?action=support">Hỗ trợ</a>
                    </p>
                </div>
            </div>
        </div>

        <!-- RIGHT: Partners -->
        <div class="footer-partners">
            <h5>ĐỐI TÁC</h5>
            <div class="partner-logos">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/beta-cineplex-v2.jpg" data-src="https://cdn.moveek.com/bundles/ornweb/partners/beta-cineplex-v2.jpg" alt="Beta">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/mega-gs-cinemas.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/mega-gs-cinemas.png" alt="MegaGS">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/cinestar.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/cinestar.png" alt="Cinestar">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/dcine.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/dcine.png" alt="Dcine">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/cinemax.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/cinemax.png"  alt="Cinemax">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/starlight.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/starlight.png" alt="Starlight">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/rio.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/rio.png" alt="Rio">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/dong-da-cinemas.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/dong-da-cinemas.png" alt="Đống Đa">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/touch-cinemas.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/touch-cinemas.png" alt="Touch">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/payoo.jpg" data-src="https://cdn.moveek.com/bundles/ornweb/partners/payoo.jpg" alt="Payoo">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/momo.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/momo.png" alt="MoMo">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/zalopay-icon.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/zalopay-icon.png" alt="ZaloPay">
                <a href="http://online.gov.vn/Home/WebDetails/52309" target="_blank" rel="noreferrer">
                    <img class="mv-badge" src="https://cdn.moveek.com/bundles/ornweb/img/20150827110756-dathongbao.png" alt="Đã thông báo Bộ Công Thương">
                </a>
            </div>
        </div>
    </div>

    <div class="footer-bottom">© <span id="year"></span> TENFINITY VIETNAM. All rights reserved.</div>
</footer>

<script>
    document.getElementById('year').textContent = new Date().getFullYear();
</script>

<style>
    /* ======= FOOTER DARK STYLE ======= */
    .footer {
        background: linear-gradient(145deg, #0d1b2a, #162032);
        border-top: 1px solid rgba(255,255,255,0.1);
        color: #cdd6f4;
        font-family: "Segoe UI", sans-serif;
        padding: 40px 0 10px;
    }

    .footer-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 30px;
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        gap: 30px;
    }

    /* LEFT: COMPANY INFO */
    .footer-logo {
        display: flex;
        align-items: flex-start;
        gap: 16px;
    }
    .footer-logo img {
        width: 100px;
        height: 100px;
        border-radius: 10px;
    }
    .footer-info h4 {
        margin: 0 0 8px;
        color: #00d8ff;
        font-weight: 700;
    }
    .footer-info p {
        margin: 0;
        line-height: 1.6;
        color: #aab5d0;
        font-size: 14px;
        width: 400px;
    }
    .footer-info a {
        color: #5cd3ff;
        text-decoration: none;
    }
    .footer-info a:hover {
        text-decoration: underline;
    }

    /* RIGHT: PARTNERS */
    .footer-partners h5 {
        margin: 0 0 10px;
        font-size: 15px;
        color: #66c0ff;
    }
    .partner-logos {
        display: flex;
        flex-wrap: wrap;
        gap: 12px;
        align-items: center;
    }
    .partner-logos img {
        width: 45px;
        height: 45px;
        border-radius: 50%;
        background: #fff;
        object-fit: contain;
        transition: 0.3s;
    }
    .partner-logos img:hover {
        transform: scale(1.1);
        box-shadow: 0 0 8px rgba(0, 255, 255, 0.4);
    }
    .partner-logos .mv-badge {
        border-radius: 0;
        width: auto;
        height: 45px;
        background: none;
        box-shadow: none;
    }

    /* BOTTOM */
    .footer-bottom {
        text-align: center;
        padding-top: 15px;
        margin-top: 30px;
        font-size: 13.5px;
        border-top: 1px solid rgba(255,255,255,0.08);
        color: #7c859a;
    }

    /* RESPONSIVE */
    @media (max-width: 900px) {
        .footer-container {
            flex-direction: column;
            gap: 20px;
        }
        .footer-info p {
            width: auto;
        }
        .partner-logos {
            justify-content: flex-start;
        }
    }
</style>