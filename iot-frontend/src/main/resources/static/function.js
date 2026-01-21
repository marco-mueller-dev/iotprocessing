function updateRate() {
    const rate = document.getElementById("rate").value;
    fetch(`http://localhost:8080/config/rate?millis=${rate}`, {
        method: "POST"
    }).then(() => alert("Rate geändert!"));
}