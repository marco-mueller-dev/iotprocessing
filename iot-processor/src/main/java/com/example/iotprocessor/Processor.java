package com.example.iotprocessor;

import com.example.common.entity.SensorData;
import com.example.common.repository.SensorDataRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Processor {

    private final SensorDataRepository repository;

    public Processor(SensorDataRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 5000)
    public void process() {
        // Alle Daten abholen und prüfen
        List<SensorData> data = repository.findAll();

        for (SensorData d : data) {
            if (isAnomaly(d)) {
                System.out.println("ANOMALIE entdeckt: " + d.getSensorId()
                        + " Temp: " + d.getTemperature());
                // Hier könntest du Alarm, Logging, Aggregation etc. einbauen
            }
            // Kein processed mehr → nichts zu speichern
        }
    }

    private boolean isAnomaly(SensorData data) {
        return data.getTemperature() < -50 || data.getTemperature() > 100;
    }
}
