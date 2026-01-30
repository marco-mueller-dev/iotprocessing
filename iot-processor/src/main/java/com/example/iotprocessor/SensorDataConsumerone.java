package com.example.iotprocessor;

import com.example.common.dto.SensorDataDTO;
import com.example.common.entity.SensorData;
import com.example.common.repository.SensorDataRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class SensorDataConsumerone {

    private final SensorDataRepository repository;
    private final ObjectMapper objectMapper;

    public SensorDataConsumerone(
            SensorDataRepository repository,
            ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;  //wir haben nicht in den klassen gestern geändert oder
    }

    @KafkaListener(topics = "sensor-data")
    public void consume(byte[] message) {
        try {
            SensorDataDTO data =
                    objectMapper.readValue(message, SensorDataDTO.class);

            if (data.getTemperature() < 10 || data.getTemperature() > 30) {
                System.out.println("Anomalie erkannt: " + data);
            }

            SensorData entity = new SensorData(
                    data.getSensorId(),
                    data.getTemperature(),
                    data.getTimestamp()
            );

            repository.save(entity);

        } catch (Exception e) {
            System.err.println("Kafka consume error: " + e.getMessage());
        }
    }
}
