let chart;

function updateRate() {
    const rate = document.getElementById("rate").value;

    fetch("http://localhost:8082/config/rate?millis=" + rate, {
        method: "POST"
    })
        .then(() => alert("Rate geändert!"))
        .catch(err => console.error(err));
}

async function loadSensorData() {
    const response = await fetch("http://localhost:8082/api/sensor/latest");
    const data = await response.json();

    const labels = data.map(d => d.sensorId);
    const values = data.map(d => d.temperature);

    if (!chart) {
        const ctx = document.getElementById("sensorChart").getContext("2d");

        chart = new Chart(ctx, {
            type: "bar",
            data: {
                labels: labels,
                datasets: [{
                    label: "Temperatur (°C)",
                    data: values
                }]
            },
            options: {
                responsive: true,
                scales: {
                    x: {
                        title: { display: true, text: "Geräte" }
                    },
                    y: {
                        title: { display: true, text: "Temperatur (°C)" }
                    }
                }
            }
        });
    } else {
        chart.data.labels = labels;
        chart.data.datasets[0].data = values;
        chart.update();
    }
}

// initial + refresh
loadSensorData();
setInterval(loadSensorData, 5000);
