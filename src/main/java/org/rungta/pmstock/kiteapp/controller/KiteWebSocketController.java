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
import java.util.List;

import static org.rungta.pmstock.kiteapp.KiteConstants.WEBSOCKET_MAX_RETRIES;
import static org.rungta.pmstock.kiteapp.KiteConstants.WEBSOCKET_MAX_RETRY_INTERVAL;

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
                logger.info("WebSocket connected");
            }
        });

        // Set the WebSocket disconnection listener
        kiteTicker.setOnDisconnectedListener(new OnDisconnect() {
            @Override
            public void onDisconnected() {
                logger.warn("WebSocket disconnected");
            }
        });

        // Set the error listener
        kiteTicker.setOnErrorListener(new OnError() {
            @Override
            public void onError(Exception exception) {
                logger.error("WebSocket error1: {}", exception.getMessage());
            }

            @Override
            public void onError(KiteException exception) {
                logger.error("WebSocket error2: {}", exception.getMessage());
            }

            @Override
            public void onError(String exceptionMessage) {
                logger.error("WebSocket error3: {}", exceptionMessage);
            }

            public void onError(Throwable throwable) {
                logger.error("Unexpected error: {}", throwable.getMessage());
            }
        });

        // Set the order update listener if needed
        kiteTicker.setOnOrderUpdateListener(new OnOrderUpdate() {
            @Override
            public void onOrderUpdate(Order order) {
                logger.error("Order update: {}", order.orderId);
            }
        });

        // Enable reconnection and set retry parameters if needed
        kiteTicker.setTryReconnection(true); // Enable auto-reconnect
        try {
            kiteTicker.setMaximumRetries(WEBSOCKET_MAX_RETRIES); // Set maximum retries
            kiteTicker.setMaximumRetryInterval(WEBSOCKET_MAX_RETRY_INTERVAL); // Set maximum retry interval in seconds
        } catch (KiteException e) {
            logger.error("Error setting retry parameters: {}", e.getMessage());
        }

        //Subscribe for Nifty500 indices to listen to from the socket
        ArrayList<Long> nifty500List = new ArrayList<>(List.of(123L, 21L));


        // Subscribe to each instrument's ticker
        if (nifty500List != null && !nifty500List.isEmpty()) {
            kiteTicker.subscribe(nifty500List);
            logger.info("Subscribing Websocket listener for below indices: {}", nifty500List);
        } else {
            logger.warn("Nifty500 list is null or empty, skipping subscription.");
        }
        // Connect to the WebSocket and start listening
        kiteTicker.connect();
    }
}

