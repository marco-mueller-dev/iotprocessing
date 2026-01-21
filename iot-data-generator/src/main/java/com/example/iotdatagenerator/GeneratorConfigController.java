package com.example.iotdatagenerator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class GeneratorConfigController {

    private final SensorGeneratorone generator;

    public GeneratorConfigController(SensorGeneratorone generator) {
        this.generator = generator;
    }

    @PostMapping("/rate")
    public ResponseEntity<Void> updateRate(@RequestParam long millis) {
        if (millis < 100) {
            return ResponseEntity.badRequest().build();
        }
        generator.setRateMillis(millis);
        return ResponseEntity.ok().build();
    }
}

