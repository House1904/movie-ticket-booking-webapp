<%@ page contentType="text/html; charset=UTF-8" %>
<footer class="mv-footer">
    <div class="mv-container">

        <!-- LEFT: Company -->
        <div class="mv-company">
            <div class="mv-brand">
                <div class="mv-logo">Mv</div>
                <div class="mv-meta">
                    <h4>CÔNG TY TNHH [TÊN CÔNG TY]</h4>
                    <p>
                        Số ĐKKD: 0315367026 <br>
                        Nơi cấp: Sở kế hoạch và đầu tư Tp. Hồ Chí Minh<br>
                        Đăng ký lần đầu ngày 01/11/2018<br>
                        Địa chỉ: 33 Nguyễn Trung Trực, P.5, Q. Bình Thạnh, Tp. Hồ Chí Minh<br>
                        <a href="${pageContext.request.contextPath}/about-us">Về chúng tôi</a> ·
                        <a href="${pageContext.request.contextPath}/privacy">Chính sách bảo mật</a> ·
                        <a href="${pageContext.request.contextPath}/support">Hỗ trợ</a> ·
                        <a href="${pageContext.request.contextPath}/contact">Liên hệ</a> · v8.1
                    </p>
                </div>
            </div>
        </div>

        <!-- RIGHT: Partners -->
        <div class="mv-partners">
            <h5>ĐỐI TÁC</h5>
            <div class="mv-logos">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/beta-cineplex-v2.jpg" data-src="https://cdn.moveek.com/bundles/ornweb/partners/beta-cineplex-v2.jpg" alt="Beta" loading="lazy">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/mega-gs-cinemas.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/mega-gs-cinemas.png" alt="MegaGS" loading="lazy">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/cinestar.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/cinestar.png" alt="Cinestar" loading="lazy">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/dcine.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/dcine.png" alt="Dcine" loading="lazy">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/cinemax.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/cinemax.png"  alt="Cinemax" loading="lazy">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/starlight.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/starlight.png" alt="Starlight" loading="lazy">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/rio.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/rio.png" alt="Rio" loading="lazy">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/dong-da-cinemas.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/dong-da-cinemas.png" alt="Đống Đa" loading="lazy">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/touch-cinemas.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/touch-cinemas.png" alt="Touch" loading="lazy">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/payoo.jpg" data-src="https://cdn.moveek.com/bundles/ornweb/partners/payoo.jpg" alt="Payoo" loading="lazy">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/momo.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/momo.png" alt="MoMo" loading="lazy">
                <img src="https://cdn.moveek.com/bundles/ornweb/partners/zalopay-icon.png" data-src="https://cdn.moveek.com/bundles/ornweb/partners/zalopay-icon.png" alt="ZaloPay" loading="lazy">
                <a rel="noreferrer" href="http://online.gov.vn/Home/WebDetails/52309" target="_blank">
                <img class="mv-badge" src="https://cdn.moveek.com/bundles/ornweb/img/20150827110756-dathongbao.png" data-src="https://cdn.moveek.com/bundles/ornweb/img/20150827110756-dathongbao.png" alt="Bộ Công Thương" loading="lazy">
</a>
</div>
</div>
</div>

<div class="mv-bottom">© <span id="y"></span> [TÊN CÔNG TY]. All rights reserved.</div>
</footer>

<script>
    document.getElementById('y').textContent = new Date().getFullYear();
</script>

<style>
    .mv-footer { background:#eef3fb; border-top:1px solid #e3ebf7; font-family:sans-serif; padding:20px 0 10px; }
    .mv-container { max-width:1200px; margin:0 auto; padding:0 20px; display:flex; justify-content:space-between; align-items:flex-start; gap:20px; }

    /* left */
    .mv-brand { display:flex; gap:14px; }
    .mv-logo { width:80px; height:80px; border-radius:50%; background:#e11d48; color:#fff;
        display:flex; align-items:center; justify-content:center; font-size:30px; font-weight:bold; }
    .mv-meta h4 { margin:0 0 6px; color:#6b7280; }
    .mv-meta p { margin:0; color:#6b7280; line-height:1.55; font-size:14px; width:450px}
    .mv-meta a { color:#6b7280; text-decoration:none; }
    .mv-meta a:hover { text-decoration:underline; }

    /* right */
    .mv-partners h5 { margin:0 0 10px; color:#6b7280; font-size:15px; }
    .mv-logos { display:flex; flex-wrap:wrap; gap:12px; align-items:center; width:600px}
    .mv-logos img { width:44px; height:44px; border-radius:50%; background:#fff; object-fit:contain; box-shadow:0 1px 3px rgba(0,0,0,.08); }
    .mv-logos .mv-badge { border-radius:0; width:auto; height:44px; box-shadow:none; background:none; }

    /* bottom */
    .mv-bottom { max-width:1200px; margin:15px auto 0; padding:8px 20px; font-size:14px; color:#6b7280; border-top:1px dashed #dce4f5; }

    /* responsive */
    @media(max-width:900px){
        .mv-container { flex-direction:column; align-items:flex-start; }
        .mv-logos { justify-content:flex-start; }
    }
</style>