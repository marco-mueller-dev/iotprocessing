package com.example.iotdatagenerator;

import com.example.common.dto.SensorDataDTO;
import com.example.common.entity.SensorData;
import com.example.common.repository.SensorDataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class SensorGenerator {

    private final RestTemplate restTemplate;
    private final long rateMillis = 3000;
    private final String processorUrl = "http://localhost:8081/sensor-data";

    public SensorGenerator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void start() {
        Thread t = new Thread(this::loop, "sensor-generator");
        t.setDaemon(true);
        t.start();
    }

    private void loop() {
        while (true) {
            try {
                sendData();
                Thread.sleep(rateMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void sendData() {
        SensorDataDTO dto = new SensorDataDTO();
        dto.setSensorId("Device-" + ThreadLocalRandom.current().nextInt(0, 10));
        dto.setTemperature(randTemp());
        dto.setTimestamp(Instant.now());

        try {
            restTemplate.postForEntity(processorUrl, dto, Void.class);
        } catch (Exception e) {
            System.err.println("Fehler beim Senden: " + e.getMessage());
        }
    }

    private double randTemp() {
        boolean anomaly = ThreadLocalRandom.current().nextInt(0, 3) == 2;
        double value = anomaly
                ? ThreadLocalRandom.current().nextDouble(-1000, 1000)
                : ThreadLocalRandom.current().nextDouble(10, 30);
        return Math.round(value * 100.0) / 100.0;
    }
}




