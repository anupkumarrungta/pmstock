package org.rungta.pmstock.kiteapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.*;
import com.zerodhatech.models.Order;
import org.rungta.pmstock.kiteapp.util.TickStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

public class KiteWebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(KiteWebSocketController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void connect(String accessToken, String apiKey) {

        // Initialize the KiteTicker
        KiteTicker kiteTicker = new KiteTicker(accessToken, apiKey);

        // Set the Tick arrival listener
        kiteTicker.setOnTickerArrivalListener(new OnTicks() {
            @Override
            public void onTicks(ArrayList<Tick> ticks) {
                if (ticks != null) {
                    for (Tick tick : ticks) {
                        if (tick != null) {
                            logger.info("Tick received for token: {}", tick.getInstrumentToken());
                            try {
                                // Assuming the message is in JSON format and can be deserialized into a Tick object.
                                TickStorageService.storeTick(tick);  // Registering the storeTick function as a callback
                            } catch (Exception e) {
                                logger.error("Failed to process incoming message - storeTick: {}", e.getMessage());
                            }
                        } else {
                            logger.warn("Received a null tick.");
                        }
                    }
                } else {
                    logger.warn("Received a null ticks list.");
                }
            }
        });

        // Set the WebSocket connection listener
        kiteTicker.setOnConnectedListener(new OnConnect() {
            @Override
            public void onConnected() {
                System.out.println("WebSocket connected");
            }
        });

        // Set the WebSocket disconnection listener
        kiteTicker.setOnDisconnectedListener(new OnDisconnect() {
            @Override
            public void onDisconnected() {
                System.out.println("WebSocket disconnected");
            }
        });

        // Set the error listener
        kiteTicker.setOnErrorListener(new OnError() {
            @Override
            public void onError(Exception exception) {
                System.out.println("WebSocket error: " + exception.getMessage());
            }

            @Override
            public void onError(KiteException exception) {
                System.out.println("WebSocket error: " + exception.getMessage());
            }

            @Override
            public void onError(String exceptionMessage) {
                System.out.println("WebSocket error: " + exceptionMessage);
            }
        });

        // Set the order update listener if needed
        kiteTicker.setOnOrderUpdateListener(new OnOrderUpdate() {
            @Override
            public void onOrderUpdate(Order order) {
                System.out.println("Order update: " + order.orderId);
            }
        });

        // Enable reconnection and set retry parameters if needed
        kiteTicker.setTryReconnection(true); // Enable auto-reconnect
        try {
            kiteTicker.setMaximumRetries(5); // Set maximum retries
            kiteTicker.setMaximumRetryInterval(10); // Set maximum retry interval in seconds
        } catch (
                KiteException e) {
            System.out.println("Error setting retry parameters: " + e.getMessage());
        }

        // Connect to the WebSocket
        kiteTicker.connect();
    }
}

