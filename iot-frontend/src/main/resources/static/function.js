// ------------------------------
// Konfiguration
// ------------------------------
const BACKEND_URL = "/api/sensor/latest";
const DB_COUNT_URL = "/api/sensor/count";
const DELETE_URL = "/api/sensor/delete";
const RATE_URL = "/config/rate"; // Endpoint für aktuelle Rate
const REFRESH_INTERVAL_MS = 5000;

const MIN_TEMP = -500;
const MAX_TEMP = 500;
const ANOMALY_THRESHOLD = 100;

const devices = [
    "Device-0", "Device-1", "Device-2", "Device-3", "Device-4",
    "Device-5", "Device-6", "Device-7", "Device-8", "Device-9"
];

const baseColors = [
    "#ff6384", "#36a2eb", "#ffce56", "#4bc0c0", "#9966ff",
    "#ff9f40", "#2ecc71", "#e74c3c", "#3498db", "#9b59b6"
];

const anomalyColor = "#ff0000";

const temperatures = new Array(10).fill(0);
let barColors = [...baseColors];
let sensorChart = null;
let deleteCountPending = 0;

// ------------------------------
// Chart Initialisierung
// ------------------------------
function initChart() {
    const ctx = document.getElementById("sensorChart").getContext("2d");

    sensorChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: devices,
            datasets: [{
                label: "Temperatur (°C)",
                data: temperatures,
                backgroundColor: barColors
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            animation: false,
            transitions: {
                active: {
                    animation: {
                        duration: 800,
                        easing: "easeInOutQuart"
                    }
                }
            },
            scales: {
                y: {
                    min: MIN_TEMP,
                    max: MAX_TEMP,
                    title: {
                        display: true,
                        text: "Temperatur"
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: "Geräte"
                    }
                }
            }
        }
    });
}

// ------------------------------
// Daten laden & Anomalien erkennen
// ------------------------------
async function loadSensorData() {
    try {
        const response = await fetch(BACKEND_URL);
        const data = await response.json();

        temperatures.fill(0);
        barColors = [...baseColors];

        data.forEach(entry => {
            const index = devices.indexOf(entry.sensorId);
            if (index !== -1) {
                temperatures[index] = entry.temperature;

                if (
                    entry.temperature > ANOMALY_THRESHOLD ||
                    entry.temperature < -ANOMALY_THRESHOLD
                ) {
                    barColors[index] = anomalyColor;
                }
            }
        });

        sensorChart.data.datasets[0].backgroundColor = barColors;
        sensorChart.update("active");

    } catch (error) {
        console.error("Fehler beim Laden der Sensordaten:", error);
    }
}

// ------------------------------
// Datenbank-Zähler laden
// ------------------------------
async function loadDbCount() {
    try {
        const response = await fetch(DB_COUNT_URL);
        const count = await response.json();
        document.getElementById("dbCount").textContent = count.toLocaleString('de-DE');
    } catch (error) {
        console.error("Fehler beim Laden der DB-Anzahl:", error);
        document.getElementById("dbCount").textContent = "Fehler";
    }
}

// ------------------------------
// Aktuelle Rate laden
// ------------------------------
async function loadCurrentRate() {
    try {
        const response = await fetch(RATE_URL);
        const data = await response.json();
        document.getElementById("currentRate").textContent = data.rate || data.millis || "-";
    } catch (error) {
        console.error("Fehler beim Laden der Rate:", error);
        document.getElementById("currentRate").textContent = "Fehler";
    }
}

// ------------------------------
// Lösch-Anfrage
// ------------------------------
function requestDelete() {
    const inputValue = document.getElementById("deleteCount").value;
    const count = parseInt(inputValue);

    // Bessere Validierung
    if (!inputValue || isNaN(count) || count < 1) {
        alert("Bitte gib eine gültige Anzahl ein (mindestens 1).");
        return;
    }

    deleteCountPending = count;
    document.getElementById("confirmMessage").textContent =
        `Möchtest du wirklich ${count.toLocaleString('de-DE')} Einträge löschen? Diese Aktion kann nicht rückgängig gemacht werden.`;
    document.getElementById("confirmModal").style.display = "block";
}

// ------------------------------
// Löschen bestätigen
// ------------------------------
async function confirmDelete() {
    const countToDelete = deleteCountPending; // Wert VORHER speichern!
    closeModal(); // Jetzt kann Modal geschlossen werden

    try {
        const response = await fetch(`${DELETE_URL}?count=${countToDelete}`, {
            method: "DELETE"
        });

        if (response.ok) {
            const result = await response.json();
            alert(`Erfolgreich ${result.deleted} Einträge gelöscht.`);
            document.getElementById("deleteCount").value = "";

            // SOFORT aktualisieren
            await loadDbCount();
        } else {
            alert("Fehler beim Löschen der Einträge.");
        }
    } catch (error) {
        console.error("Fehler beim Löschen:", error);
        alert("Fehler beim Löschen der Einträge.");
    }
}

// ------------------------------
// Modal schließen
// ------------------------------
function closeModal() {
    document.getElementById("confirmModal").style.display = "none";
    deleteCountPending = 0;
}

// ------------------------------
// Generator Rate ändern
// ------------------------------
async function updateRate() {
    const rate = document.getElementById("rate").value;

    try {
        await fetch(`/config/rate?millis=${rate}`, {
            method: "POST"
        });
        alert("Rate geändert!");

        // Aktualisiere die Anzeige sofort
        await loadCurrentRate();
    } catch (err) {
        console.error("Rate-Update fehlgeschlagen", err);
        alert("Fehler beim Ändern der Rate.");
    }
}

// ------------------------------
// Start
// ------------------------------
window.onload = () => {
    initChart();
    loadSensorData();
    loadDbCount();
    loadCurrentRate();

    setInterval(loadSensorData, REFRESH_INTERVAL_MS);
    setInterval(loadDbCount, REFRESH_INTERVAL_MS);
    setInterval(loadCurrentRate, REFRESH_INTERVAL_MS);
};

// Modal schließen bei Klick außerhalb
window.onclick = function(event) {
    const modal = document.getElementById("confirmModal");
    if (event.target === modal) {
        closeModal();
    }
};