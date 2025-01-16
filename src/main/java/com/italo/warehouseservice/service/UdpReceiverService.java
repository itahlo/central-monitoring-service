package com.italo.warehouseservice.service;

import com.italo.warehouseservice.model.SensorMeasurement;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

@Service
public class UdpReceiverService {
    @Value("${warehouse.udp.temperaturePort}")
    private int temperaturePort;

    @Value("${warehouse.udp.humidityPort}")
    private int humidityPort;

    private final WarehouseService warehouseService;

    public UdpReceiverService(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostConstruct
    public void startUDPListeners() {
        // Start a listener for temperature measurements
        new Thread(() -> listenUDP(temperaturePort, "temperature")).start();

        // Start a listener for humidity measurements
        new Thread(() -> listenUDP(humidityPort, "humidity")).start();
    }
    // this can be improved
    private void listenUDP(int port, String type) {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] buffer = new byte[256];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength()).trim();

                // Expected format: sensor_id=t1; value=30
                // or sensor_id=h1; value=40
                // For example: "sensor_id=t1; value=30"
                String[] parts = received.split(";");
                if (parts.length == 2) {
                    // parse sensor_id
                    String sensorIdPart = parts[0].split("=")[1];
                    // parse value
                    String valuePart = parts[1].split("=")[1];

                    String sensorId = sensorIdPart.trim();
                    double value = Double.parseDouble(valuePart.trim());

                    SensorMeasurement measurement = new SensorMeasurement(sensorId, value, type);
                    // Publish to RabbitMQ
                    warehouseService.publishMeasurement(measurement);
                }
            }
        } catch (SocketException e) {
            System.err.println("UDP socket could not be opened on port " + port + ": " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}