package org.rungta.pmstock.kiteapp.util;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException;
import com.zerodhatech.models.Instrument;
import org.rungta.pmstock.kiteapp.KiteConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class MarketInstrumentStorageService {

    private static DynamoDbClient dynamoDbClient = DynamoDBService.getDynamoDBClient();
    private static final Logger logger = LoggerFactory.getLogger(MarketInstrumentStorageService.class);
    //private static ObjectMapper objectMapper = new ObjectMapper(); // Reusing ObjectMapper

    private static final String[] EXPECTED_MARKET_INSTRUMENT_HEADERS = {
            "instrument_token", "exchange_token", "tradingsymbol", "name",
            "last_price", "expiry", "strike", "tick_size", "lot_size",
            "instrument_type", "segment", "exchange"
    };

    private static boolean validateHeader(String[] headerFields) {
        if (headerFields.length != EXPECTED_MARKET_INSTRUMENT_HEADERS.length) { return false; }

        for (int index = 0; index < EXPECTED_MARKET_INSTRUMENT_HEADERS.length; index++) {
            if (!headerFields[index].trim().equalsIgnoreCase(EXPECTED_MARKET_INSTRUMENT_HEADERS[index])) {
                logger.error("Invalid header at position {}: expected '{}', found '{}'", index, EXPECTED_MARKET_INSTRUMENT_HEADERS[index], headerFields[index]);
                return false;
            }
        }
        return true;
    }

    //Stores Instrument Object received from API Call to its corresponding DDB Table
    public static void storeMarketInstrument(String instruments) {
        if (instruments == null) {
            logger.error("instruments is null. There are no valid instruments to persist.");
            return;
        }

        //Check if the MarketInstruments table exists.
        if (!DynamoDBService.checkTable(KiteConstants.DDB_MARKET_INSTRUMENTS)) {
            logger.error("Table'{}' does not exist. Create the table manually and call again. Skipping the persistence action", KiteConstants.DDB_MARKET_INSTRUMENTS);
            return;
        }

        // Parse instruments string which captures csv dump line by line
        String[] lines = instruments.split("\n");
        if (lines.length < 1) {
            logger.error("Empty CSV data.");
            return;
        }

        // Read the first line (usually header) and validate the header against EXPECTED_MARKET_INSTRUMENT_HEADERS
        String headerLine = lines[0];
        String[] headerFields = headerLine.split(",");
        boolean validation = validateHeader(headerFields);
        if (!validation) {
            logger.error("Validation failed. Skipping further processing of instruments. Please check the headers in the file and try again.");
            return;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Skip the first line (usually headers) and process the rest
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            // Assuming CSV format is consistent and properly ordered
            String[] fields = line.split(",");
            if (fields.length != EXPECTED_MARKET_INSTRUMENT_HEADERS.length) {
                logger.error("Invalid number of CSV fields at line {}: {}", i, line);
                continue;
            }

            Instrument instrument = new Instrument();
            try {
                instrument.setInstrument_token(Long.parseLong(fields[0]));
                instrument.setExchange_token(Long.parseLong(fields[1]));
                instrument.setTradingsymbol(fields[2]);
                instrument.setName(fields[3]);
                instrument.setLast_price(Double.parseDouble(fields[4]));

                LocalDate expiryDate = LocalDate.parse(fields[5], dateFormatter);
                instrument.setExpiry(Date.valueOf(expiryDate));

                instrument.setStrike(fields[6]);
                instrument.setTick_size(Double.parseDouble(fields[7]));
                instrument.setLot_size(Integer.parseInt(fields[8]));
                instrument.setInstrument_type(fields[9]);
                instrument.setSegment(fields[10]);
                instrument.setExchange(fields[11]);
            } catch (NumberFormatException | DateTimeParseException e) {
                logger.error("Invalid date format for expiry: {}. Line: {} encountered while parsing marketInstruments.", fields[5], line);
            } catch (DynamoDBMappingException e) {
                logger.error("Failed to Store Instrument in DynamoDB due to mapping error: {}. Line: {}", e.getMessage(), line, e);
            } catch (Exception e) {
                logger.error("Failed to Store Instrument at line {}: {}", i, e.getMessage(), e);
            }

            // Convert the Instrument object to AttributeValue map format or other operations
            // Based on the rest of your method logic
            Map<String, AttributeValue> instrumentMap = new HashMap<>();
            populateInstrumentMap(instrument, instrumentMap);

            PutItemRequest request = PutItemRequest.builder()
                    .tableName(KiteConstants.DDB_MARKET_INSTRUMENTS)
                    .item(instrumentMap)
                    .build();

            try {
                dynamoDbClient.putItem(request);
            } catch (DynamoDBMappingException e) {
                logger.error("Failed to Store Instrument in DynamoDB due to mapping error: {}", e.getMessage(), e);
            } catch (Exception e) {
                logger.error("Failed to Store Instrument: {}", e.getMessage(), e);
            }
        }
    }

    private static void populateInstrumentMap(Instrument marketInstrument, Map<String, AttributeValue> itemAttributes) {
        itemAttributes.put("instrument_token", AttributeValue.builder().n(marketInstrument.getInstrument_type()).build());
        itemAttributes.put("exchange_token", AttributeValue.builder().s(String.valueOf(marketInstrument.getExchange_token())).build());
        itemAttributes.put("tradingsymbol", AttributeValue.builder().s(marketInstrument.getTradingsymbol()).build());
        itemAttributes.put("name", AttributeValue.builder().s(marketInstrument.getName()).build());
        itemAttributes.put("last_price", AttributeValue.builder().n(String.valueOf(marketInstrument.getLast_price())).build());
        itemAttributes.put("expiry", AttributeValue.builder().s(String.valueOf(marketInstrument.getExpiry())).build());
        itemAttributes.put("strike", AttributeValue.builder().n(String.valueOf(marketInstrument.getStrike())).build());
        itemAttributes.put("tick_size", AttributeValue.builder().n(String.valueOf(marketInstrument.getTick_size())).build());
        itemAttributes.put("lot_size", AttributeValue.builder().n(String.valueOf(marketInstrument.getLot_size())).build());
        itemAttributes.put("instrument_type", AttributeValue.builder().s(marketInstrument.getInstrument_type()).build());
        itemAttributes.put("segment", AttributeValue.builder().s(marketInstrument.getSegment()).build());
        itemAttributes.put("exchange", AttributeValue.builder().s(marketInstrument.getExchange()).build());
    }
}
