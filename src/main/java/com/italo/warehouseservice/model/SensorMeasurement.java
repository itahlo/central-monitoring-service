package com.italo.warehouseservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SensorMeasurement {
    private String sensorId;
    private double value;
    private String type;
}
