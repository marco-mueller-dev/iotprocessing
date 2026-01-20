package com.example.common.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "sensor_data")
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sensorId;

    private double temperature;

    private Instant timestamp;

    protected SensorData() {}

    public SensorData(String sensorId, double temperature, Instant timestamp) {
        this.sensorId = sensorId;
        this.temperature = temperature;
        this.timestamp = timestamp;
    }


}
