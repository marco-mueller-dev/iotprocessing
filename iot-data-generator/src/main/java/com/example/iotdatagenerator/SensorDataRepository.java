package com.example.iotdatagenerator;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    List<SensorData> findAllByOrderByTimestampAsc();

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM SensorData s WHERE s.id IN (SELECT s2.id FROM SensorData s2 ORDER BY s2.timestamp ASC LIMIT ?1)", nativeQuery = true)
    void deleteOldest(int count);


}
