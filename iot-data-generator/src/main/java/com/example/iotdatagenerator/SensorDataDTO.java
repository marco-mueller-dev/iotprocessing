package com.example.iotdatagenerator;

import java.time.Instant;

public record SensorDataDTO(String deviceID, double tempature, Instant timmestamp) {
}
