package com.example.iotprocessor.repository;

import com.example.iotprocessor.entity.IotData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IotDataRepository
        extends JpaRepository<IotData, Long> {
}