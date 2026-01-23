package com.example.iotdatagenerator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
@CrossOrigin(origins = "*")
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

