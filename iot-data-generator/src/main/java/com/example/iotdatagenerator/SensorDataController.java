package com.example.iotdatagenerator;

import com.example.common.repository.SensorDataRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sensor")
@CrossOrigin(origins = "*")
public class SensorDataController {

    private final SensorDataRepository repository;

    public SensorDataController(SensorDataRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/latest")
    public List<SensorDataDTO> getLatestPerDevice() {
        return repository.findLatestPerDevice().stream()
                .map(sd -> new SensorDataDTO(
                        sd.getSensorId(),
                        sd.getTemperature()
                ))
                .toList();
    }

    public record SensorDataDTO(String sensorId, double temperature) {}
}




