package com.example.iotdatagenerator;

import com.example.common.dto.SensorDataDTO;

import jakarta.annotation.PostConstruct;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class SensorGeneratorone {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic = "sensor-data";
    private final long rateMillis = 3000;

    public SensorGeneratorone(
            KafkaTemplate<String, byte[]> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
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

    private double randTemp() {
        boolean anomaly = ThreadLocalRandom.current().nextInt(0, 3) == 2;
        double value = anomaly
                ? ThreadLocalRandom.current().nextDouble(-1000, 1000)
                : ThreadLocalRandom.current().nextDouble(10, 30);
        return Math.round(value * 100.0) / 100.0;
    }

    private void sendData() {
        SensorDataDTO dto = new SensorDataDTO();
        dto.setSensorId("Device-" + ThreadLocalRandom.current().nextInt(0, 10));
        dto.setTemperature(randTemp());
        dto.setTimestamp(Instant.now());

        try {
            byte[] payload = objectMapper.writeValueAsBytes(dto);
            kafkaTemplate.send(topic, dto.getSensorId(), payload);
        } catch (Exception e) {
            System.err.println("Kafka send error: " + e.getMessage());
        }
    }
}
