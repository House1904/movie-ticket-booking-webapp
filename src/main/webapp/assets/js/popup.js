
function openPopup() {
    document.getElementById("rapPopup").style.display = "flex";
}

function closePopup() {
    document.getElementById("rapPopup").style.display = "none";
}

// Cho phép click ra ngoài để đóng popup
window.addEventListener('click', function(e) {
    const popup = document.getElementById("rapPopup");
    if (e.target === popup) {
        popup.style.display = "none";
    }
});