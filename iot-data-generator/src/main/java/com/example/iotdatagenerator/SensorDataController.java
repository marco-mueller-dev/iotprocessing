package com.example.iotdatagenerator;

import com.example.common.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sensor")
public class SensorDataController {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestData() {
        return ResponseEntity.ok(sensorDataRepository.findLatestPerDevice());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(sensorDataRepository.count());
    }

    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<Map<String, Integer>> deleteOldestEntries(@RequestParam int count) {
        if (count < 1) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Hole die IDs der ältesten Einträge
            var oldestEntries = sensorDataRepository.findOldestEntries(PageRequest.of(0, count));

            // Lösche diese Einträge
            sensorDataRepository.deleteAllById(oldestEntries);

            Map<String, Integer> result = new HashMap<>();
            result.put("deleted", oldestEntries.size());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}