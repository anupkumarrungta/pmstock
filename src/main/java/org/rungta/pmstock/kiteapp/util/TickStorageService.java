package org.rungta.pmstock.kiteapp.util;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zerodhatech.models.Tick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.rungta.pmstock.kiteapp.KiteConstants.TICK_STORAGE_TABLE_NAME_FORMAT;

public class TickStorageService {

    private static DynamoDbClient dynamoDbClient = DynamoDBService.getDynamoDBClient();
    private static final Logger logger = LoggerFactory.getLogger(TickStorageService.class);
    private static ObjectMapper objectMapper = new ObjectMapper(); // Reusing ObjectMapper

    //Stores Kite Tick Object received from Socket to its corresponding DDB Table
    public static void storeTick(Tick tick) {
        if (tick == null) {
            logger.warn("Tick object is null. Skipping the tick persistence action");
            return;
        }

        String tableName = String.format(TICK_STORAGE_TABLE_NAME_FORMAT, tick.getInstrumentToken());
        if (!DynamoDBService.checkTable(tableName)) {
            logger.error("Table'{}' does not exist for instrument token {}. Skipping the tick persistence action", tableName, tick.getInstrumentToken());
            return;
        }
        Map<String, AttributeValue> item = new HashMap<>();
        populateItemMap(item, tick);

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        try {
            dynamoDbClient.putItem(request);
        } catch (DynamoDBMappingException e) {
            logger.error("Failed to Store Tick in DynamoDB due to mapping error: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Failed to Store Tick: {}", e.getMessage(), e);
        }
    }

    private static void populateItemMap(Map<String, AttributeValue> item, Tick tick) {
        item.put("mode", AttributeValue.builder().s(tick.getMode()).build());
        item.put("tradable",AttributeValue.builder().bool(tick.isTradable()).build());
        item.put("token", AttributeValue.builder().n(String.valueOf(tick.getInstrumentToken())).build());
        item.put("lastTradedPrice", AttributeValue.builder().n(String.valueOf(tick.getLastTradedPrice())).build());
        item.put("highPrice", AttributeValue.builder().n(String.valueOf(tick.getHighPrice())).build());
        item.put("lowPrice", AttributeValue.builder().n(String.valueOf(tick.getLowPrice())).build());
        item.put("openPrice", AttributeValue.builder().n(String.valueOf(tick.getOpenPrice())).build());
        item.put("closePrice", AttributeValue.builder().n(String.valueOf(tick.getClosePrice())).build());
        item.put("change", AttributeValue.builder().n(String.valueOf(tick.getChange())).build());
        item.put("lastTradeQuantity", AttributeValue.builder().n(String.valueOf(tick.getLastTradedQuantity())).build());
        item.put("averageTradePrice", AttributeValue.builder().n(String.valueOf(tick.getAverageTradePrice())).build());
        item.put("volumeTradedToday", AttributeValue.builder().n(String.valueOf(tick.getVolumeTradedToday())).build());
        item.put("totalBuyQuantity", AttributeValue.builder().n(String.valueOf(tick.getTotalBuyQuantity())).build());
        item.put("totalSellQuantity", AttributeValue.builder().n(String.valueOf(tick.getTotalSellQuantity())).build());
        item.put("lastTradedTime", AttributeValue.builder().n(String.valueOf(tick.getLastTradedTime())).build());
        item.put("oi", AttributeValue.builder().n(String.valueOf(tick.getOi())).build());
        item.put("openInterestDayHigh", AttributeValue.builder().n(String.valueOf(tick.getOpenInterestDayHigh())).build());
        item.put("openInterestDayLow", AttributeValue.builder().n(String.valueOf(tick.getOpenInterestDayLow())).build());
        item.put("tickTimestamp", AttributeValue.builder().s(Instant.now().toString()).build());

        // Convert the depth Map<String, ArrayList<Depth>> to a JSON string and store it in DynamoDB Table
        // Store the JSON string in the item map

        objectMapper = null; //reset objectMapper prior to reuse
        
        try {
            String depthJson = objectMapper.writeValueAsString(tick.getMarketDepth());
            item.put("depth", AttributeValue.builder().s(depthJson).build());
        } catch (JsonProcessingException e) {
            logger.error("Error converting depth map to JSON for tick with token {}: {} .. skipping the storing of depth key only..", tick.getInstrumentToken(), e.getMessage(), e);
        }
    }

/*

    public static void main(String[] args) {
        // Example usage
        initialize();

        Tick tick = new Tick("123456", 100.0);
        storeTick(tick);
    }
*/
}