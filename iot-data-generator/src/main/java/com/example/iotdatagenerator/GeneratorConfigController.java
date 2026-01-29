package com.example.iotdatagenerator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
        if (millis > 30000) {
            return ResponseEntity.badRequest().build();
        }
        generator.setRateMillis(millis);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rate")
    public ResponseEntity<Map<String, Long>> getCurrentRate() {
        Map<String, Long> response = new HashMap<>();
        response.put("rate", generator.getRateMillis().get()); // Hole die Rate vom Generator
        return ResponseEntity.ok(response);
    }
}