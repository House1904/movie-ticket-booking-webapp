function togglePassword() {
    const pwd = document.getElementById("password");
    const btn = document.querySelector(".toggle-password");
    if (pwd.type === "password") {
        pwd.type = "text";
        btn.textContent = "🖊️"; // đổi icon
    } else {
        pwd.type = "password";
        btn.textContent = "👁";
    }
}
