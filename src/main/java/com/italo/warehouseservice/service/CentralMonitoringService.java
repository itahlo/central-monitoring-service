package com.italo.warehouseservice.service;

import com.italo.warehouseservice.model.SensorMeasurement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The CentralMonitoringService processes each incoming measurement
 * and triggers an alarm if thresholds are exceeded.
 */
@Service
public class CentralMonitoringService {

    @Value("${warehouse.temperatureThreshold}")
    private double temperatureThreshold;

    @Value("${warehouse.humidityThreshold}")
    private double humidityThreshold;

    public String processMeasurement(SensorMeasurement measurement) {
        switch (measurement.getType()) {
            case "temperature":
                if (measurement.getValue() > temperatureThreshold) {
                    return raiseAlarm(measurement, "Temperature threshold exceeded!");
                }
                break;
            case "humidity":
                if (measurement.getValue() > humidityThreshold) {
                    return raiseAlarm(measurement, "Humidity threshold exceeded!");
                }
                break;
            default:
                return "[CentralMonitoring] Unknown sensor type: " + measurement.getType();
        }
        return "";
    }

    private String raiseAlarm(SensorMeasurement measurement, String reason) {
        return "=== ALARM TRIGGERED ===" + "\n" +
                "Sensor: " + measurement.getSensorId() +
                " (" + measurement.getType() + ")" + "\n" +
                "Value: " + measurement.getValue() + "\n" +
                "Reason: " + reason + "\n" +
                "=======================";
    }
}
