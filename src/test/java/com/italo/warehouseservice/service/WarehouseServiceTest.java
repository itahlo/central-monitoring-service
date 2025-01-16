package com.italo.warehouseservice.service;

import com.italo.warehouseservice.model.SensorMeasurement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.italo.warehouseservice.config.RabbitMQConfig.EXCHANGE_NAME;
import static com.italo.warehouseservice.config.RabbitMQConfig.ROUTING_KEY;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class WarehouseServiceTest {

    @Autowired
    private WarehouseService warehouseService;
    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void testPublishMeasurement() {
        SensorMeasurement measurement = new SensorMeasurement("t1", 30.0, "temperature");

        warehouseService.publishMeasurement(measurement);

        // check if rabbit mq received the message
        ArgumentCaptor<SensorMeasurement> captor = ArgumentCaptor.forClass(SensorMeasurement.class);
        verify(rabbitTemplate, times(1))
                .convertAndSend(Mockito.eq(EXCHANGE_NAME), Mockito.eq(ROUTING_KEY), captor.capture());

        SensorMeasurement capturedMeasurement = captor.getValue();
        Assertions.assertEquals("t1", capturedMeasurement.getSensorId());
        Assertions.assertEquals(30.0, capturedMeasurement.getValue());
        Assertions.assertEquals("temperature", capturedMeasurement.getType());
    }
}
