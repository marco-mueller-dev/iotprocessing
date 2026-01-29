package com.example.common.repository;

import com.example.common.entity.SensorData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    @Query("""
SELECT sd FROM SensorData sd
WHERE sd.timestamp = (
  SELECT MAX(s.timestamp)
  FROM SensorData s
  WHERE s.sensorId = sd.sensorId
)
""")
    List<SensorData> findLatestPerDevice();

    @Query("SELECT sd.id FROM SensorData sd ORDER BY sd.timestamp ASC")
    List<Long> findOldestEntries(PageRequest pageable);

}
