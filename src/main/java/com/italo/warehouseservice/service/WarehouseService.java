package com.italo.warehouseservice.service;

import com.italo.warehouseservice.model.SensorMeasurement;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.italo.warehouseservice.config.RabbitMQConfig.EXCHANGE_NAME;
import static com.italo.warehouseservice.config.RabbitMQConfig.ROUTING_KEY;

@Service
public class WarehouseService {
    private final RabbitTemplate rabbitTemplate;

    public WarehouseService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishMeasurement(SensorMeasurement measurement) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, measurement);
        System.out.println("[WarehouseService] Published measurement: " + measurement.getSensorId()
                + " value=" + measurement.getValue()
                + " type=" + measurement.getType());
    }
}
