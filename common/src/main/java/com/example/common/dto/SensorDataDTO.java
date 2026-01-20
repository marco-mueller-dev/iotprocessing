package com.example.common.dto;

import java.time.Instant;

public record SensorDataDTO(String deviceID, double tempature, Instant timmestamp) {
}
