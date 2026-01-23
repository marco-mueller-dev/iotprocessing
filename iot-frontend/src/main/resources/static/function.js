// ------------------------------
// Konfiguration
// ------------------------------
const BACKEND_URL = "http://localhost:8082/api/sensor/latest";
const REFRESH_INTERVAL_MS = 5000;

const MIN_TEMP = -500;
const MAX_TEMP = 500;
const ANOMALY_THRESHOLD = 100;

// Feste Device-Reihenfolge
const devices = [
    "Device-0", "Device-1", "Device-2", "Device-3", "Device-4",
    "Device-5", "Device-6", "Device-7", "Device-8", "Device-9"
];

// Standardfarben pro Device
const baseColors = [
    "#ff6384", "#36a2eb", "#ffce56", "#4bc0c0", "#9966ff",
    "#ff9f40", "#2ecc71", "#e74c3c", "#3498db", "#9b59b6"
];

const anomalyColor = "#ff0000"; // ROT bei Anomalie

const temperatures = new Array(10).fill(0);
let barColors = [...baseColors];

let sensorChart = null;

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

            animation: false, // wichtig: Basisanimation AUS

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

                //  Anomalie-Check
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
// Generator Rate ändern
// ------------------------------
function updateRate() {
    const rate = document.getElementById("rate").value;

    fetch(`http://localhost:8082/config/rate?millis=${rate}`, {
        method: "POST"
    })
        .then(() => alert("Rate geändert!"))
        .catch(err => console.error("Rate-Update fehlgeschlagen", err));
}

// ------------------------------
// Start
// ------------------------------
window.onload = () => {
    initChart();
    loadSensorData();
    setInterval(loadSensorData, REFRESH_INTERVAL_MS);
};
