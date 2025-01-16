package com.italo.warehouseservice.listener;

import com.italo.warehouseservice.model.SensorMeasurement;
import com.italo.warehouseservice.service.CentralMonitoringService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.italo.warehouseservice.config.RabbitMQConfig.QUEUE_NAME;

/**
 * Listens on the "measurements-queue" for new sensor data,
 * then forwards it to CentralMonitoringService.
 */
@Component
public class MeasurementListener {

    private final CentralMonitoringService centralMonitoringService;

    public MeasurementListener(CentralMonitoringService centralMonitoringService) {
        this.centralMonitoringService = centralMonitoringService;
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(SensorMeasurement measurement) {
        System.out.println("[MeasurementListener] Received measurement: "
                + measurement.getSensorId() + " = " + measurement.getValue());
        System.out.println(centralMonitoringService.processMeasurement(measurement));
    }
}
