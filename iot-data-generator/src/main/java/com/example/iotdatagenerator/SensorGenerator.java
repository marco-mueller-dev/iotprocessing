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

    private  long rateMillis = 1;

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
                rateMillis += rateMillis;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void generate() {
        SensorData data = new SensorData(
                UUID.randomUUID().toString(),
                ThreadLocalRandom.current().nextDouble(10, 30),
                Instant.now()
        );

        repository.save(data);
    }
}
