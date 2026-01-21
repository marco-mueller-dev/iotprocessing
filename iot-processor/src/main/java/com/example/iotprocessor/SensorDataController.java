package com.example.iotprocessor;

import com.example.common.dto.SensorDataDTO;
import com.example.common.entity.SensorData;
import com.example.common.repository.SensorDataRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sensor-data")
public class SensorDataController {

    private final SensorDataRepository repository;

    public SensorDataController(SensorDataRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Void> receiveData(@RequestBody SensorDataDTO data) {
        SensorData entity = new SensorData(
                data.getSensorId(),
                data.getTemperature(),
                data.getTimestamp()
        );
        repository.save(entity);
        return ResponseEntity.ok().build();
    }
}

