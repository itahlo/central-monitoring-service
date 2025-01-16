package com.italo.warehouseservice.service;

import com.italo.warehouseservice.model.SensorMeasurement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CentralMonitoringServiceTest {
    @Autowired
    private CentralMonitoringService centralMonitoringService;


    @Test
    void testTemperatureAboveThresholdTriggersAlarm() {
        SensorMeasurement measurement = new SensorMeasurement("t1", 55.0, "temperature");
        String result = centralMonitoringService.processMeasurement(measurement);
        assertTrue(result.contains("ALARM TRIGGERED"), "Expected alarm to be triggered for temperature.");
    }

    @Test
    void testTemperatureExactlyAtThresholdNoAlarm() {
        SensorMeasurement measurement = new SensorMeasurement("t2", 35.0, "temperature");
        String result = centralMonitoringService.processMeasurement(measurement);
        assertFalse(result.contains("ALARM TRIGGERED"), "No alarm should be triggered for temperature at threshold.");
    }

    @Test
    void testHumidityBelowThresholdNoAlarm() {
        SensorMeasurement measurement = new SensorMeasurement("h1", 40.0, "humidity");
        String result = centralMonitoringService.processMeasurement(measurement);
        assertFalse(result.contains("ALARM TRIGGERED"), "No alarm should be triggered for humidity below threshold.");
    }
}