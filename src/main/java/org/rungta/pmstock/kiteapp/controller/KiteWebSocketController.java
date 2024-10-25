package org.rungta.pmstock.kiteapp.controller;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.*;
import com.zerodhatech.models.Order;


import com.zerodhatech.models.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;

public class KiteWebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(KiteWebSocketController.class);

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
                            logger.info("Last traded price: {}", tick.getLastTradedPrice());
                            logger.info("Tick Object: {}", tick.toString());
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

