package com.example.iotdatagenerator;

import com.example.iotdatagenerator.SensorData;
import com.example.iotdatagenerator.SensorDataRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class SensorGenerator {

    private final SensorDataRepository repository;

    public long getRateMillis() {
        return rateMillis;
    }

    private  long rateMillis = 3000;

    public SensorGenerator(SensorDataRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void start() {
        Thread t = new Thread(this::loop, "sensor-generator");
        t.setDaemon(true);
        t.start();
    }

    private void loop() {
        while (true) {
            try {
                generate();
                Thread.sleep(rateMillis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @PostConstruct
    public void startone() {
        Thread u = new Thread(this::loopone, "sensor-generatorone");
        u.setDaemon(true);
        u.start();
    }

    private void loopone() {
        while (true) {
            try {
                Thread.sleep(rateMillis);
               // rateMillis += rateMillis;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void generate() {
        SensorData data = new SensorData(
                "Device-" + ThreadLocalRandom.current().nextInt(0, 10),
                randTemp(),
                Instant.now()
        );


        // Maximal 10 Einträge halten
        long count = repository.count();
        if (count >= 10) {
            SensorData oldest = repository.findAllByOrderByTimestampAsc().get(0);
            repository.delete(oldest);
        }

        repository.save(data);
    }


    private double randTemp () {
        int a = ThreadLocalRandom.current().nextInt(0, 3);
        if (a == 2) {
            double anomalieTemp = ThreadLocalRandom.current().nextDouble(-1000, 1000);
            return anomalieTemp = Math.round(anomalieTemp * 100.0) / 100.0;
        } else {
            double temp = ThreadLocalRandom.current().nextDouble(10, 30);
            return temp = Math.round(temp * 100.0) / 100.0;
        }
    }
}
