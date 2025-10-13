function initDateCarousel(containerId, days = 7, defaultDate = null) {
    const container = document.getElementById(containerId);
    const form = document.getElementById("dateForm");
    const hiddenInput = document.getElementById("selectedDate");
    if (!container || !form || !hiddenInput) return;


    if (defaultDate) {
        const activeBtn = container.querySelector(`.date-item[data-date="${defaultDate}"]`);
        if (activeBtn) {
            activeBtn.classList.add("active");
        }
    }

    const today = new Date();
    let selectedDate = null;

    const pad = (n) => (n < 10 ? "0" + n : n);
    const toIso = (d) =>
        `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;

    for (let i = 0; i < days; i++) {
        const d = new Date(today);
        d.setDate(today.getDate() + i);
        const iso = toIso(d);

        const btn = document.createElement("button");
        btn.type = "button";
        btn.className = "date-item";
        btn.textContent = d.toLocaleDateString("vi-VN", {
            weekday: "short",
            day: "numeric",
            month: "short",
        });
        btn.dataset.date = iso;

        // 🔥 Đặt active cho ngày trùng với defaultDate (nếu có)
        if (defaultDate && iso === defaultDate) {
            btn.classList.add("active");
            selectedDate = iso;
            hiddenInput.value = iso;
        }

        // Nếu không có defaultDate, chọn ngày hôm nay
        if (!defaultDate && i === 0) {
            btn.classList.add("active");
            selectedDate = iso;
            hiddenInput.value = iso;
        }

        // Khi click đổi ngày
        btn.addEventListener("click", () => {
            container.querySelectorAll(".date-item").forEach((b) => b.classList.remove("active"));
            btn.classList.add("active");
            selectedDate = btn.dataset.date;
            hiddenInput.value = selectedDate;
            form.submit();
        });

        container.appendChild(btn);
    }
}



document.addEventListener("DOMContentLoaded", () => {
    const preSelectedDate = "${selectedDate != null ? selectedDate : ''}";
    initDateCarousel("dateCarousel", 7, preSelectedDate);
});
