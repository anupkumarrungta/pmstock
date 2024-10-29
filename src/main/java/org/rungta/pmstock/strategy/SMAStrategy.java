package org.rungta.pmstock.strategy;


import com.amazonaws.regions.Regions;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;
import java.util.List;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class SMAStrategy {

    private static final int SHORT_TERM_PERIOD = 5; // Example for short-term moving average
    private static final int LONG_TERM_PERIOD = 20; // Example for long-term moving average

    public enum Recommendation {
        BUY, SELL, NO_ACTION
    }

    public static Recommendation analyze(List<NiftyIndexOHLC> ohlcData) {
        if (ohlcData.size() < LONG_TERM_PERIOD) {
            return Recommendation.NO_ACTION;
        }

        double shortTermMovingAverage = calculateMovingAverage(ohlcData, SHORT_TERM_PERIOD);
        double longTermMovingAverage = calculateMovingAverage(ohlcData, LONG_TERM_PERIOD);

        if (shortTermMovingAverage > longTermMovingAverage) {
            return Recommendation.BUY;
        } else if (shortTermMovingAverage < longTermMovingAverage) {
            return Recommendation.SELL;
        } else {
            return Recommendation.NO_ACTION;
        }
    }

    private static double calculateMovingAverage(List<NiftyIndexOHLC> ohlcData, int period) {
        int startIndex = ohlcData.size() - period;
        double sum = 0.0;
        for (int i = startIndex; i < ohlcData.size(); i++) {
            sum += ohlcData.get(i).getClose();
        }
        return sum / period;
    }




    public static void readDatafromCSV(){
        String csvFile = "C:\\Users\\anupk\\Downloads\\Nifty Historical Data\\NIFTY 50-01-01-2021-to-31-12-2021.csv";
        String line = "";
        String csvSplitBy = ",";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");

        List<NiftyIndexOHLC> ohlcData = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {



                // use comma as separator
                String[] ohlc = line.split(csvSplitBy);

                // Trim any extra whitespace or invisible characters from the date string
                String dateString = ohlc[0].trim();
                ohlc[1].trim();
                ohlc[2].trim();
                ohlc[3].trim();
                ohlc[4].trim();
                ohlc[5].trim();
                ohlc[6].trim();


                try {
                    Date date = dateFormat.parse(ohlc[0]);
                    double open = Double.parseDouble(ohlc[1]);
                    double high = Double.parseDouble(ohlc[2]);
                    double low = Double.parseDouble(ohlc[3]);
                    double close = Double.parseDouble(ohlc[4]);
                    double sharesTraded = Double.parseDouble(ohlc[5]);
                    double turnover = Double.parseDouble(ohlc[6]);

                    NiftyIndexOHLC ohlcEntry = new NiftyIndexOHLC(date, open, high, low, close, sharesTraded, turnover);
                    ohlcData.add(ohlcEntry);
                } catch (ParseException e) {
                    System.err.println("Error parsing date: " + dateString);
                    System.err.println("Error in line --   " + line);
                }
            }

        } catch (IOException  e) {
            e.printStackTrace();
        }

        // Call the analyze method with the populated ohlcData list
        Recommendation recommendation = analyze(ohlcData);
        System.out.println("Recommendation: " + recommendation);
    }

    public static void main(String[] args) {

        readDatafromCSV();

        // Example data for demonstration purposes
        
/*        List<NiftyIndexOHLC> ohlcData = List.of(
                new Nif(100, 105, 95, 100),
                new OHLC(102, 107, 99, 104),
                new OHLC(98, 103, 95, 101),
                new OHLC(96, 110, 100, 107),
                new OHLC(107, 112, 102, 112),
                new OHLC(109, 115, 105, 113),
                new OHLC(111, 115, 105, 114),
                new OHLC(112, 115, 105, 115),
                new OHLC(113, 115, 105, 116),
                new OHLC(114, 115, 105, 117),
                new OHLC(115, 115, 105, 118),
                new OHLC(116, 115, 105, 119),
                new OHLC(117, 115, 105, 120),
                new OHLC(118, 115, 105, 121),
                new OHLC(119, 115, 105, 122),
                new OHLC(120, 115, 105, 123),
                new OHLC(121, 115, 105, 124)

                // Add more data points to fill up to 20 or more OHLC entries for realistic analysis
        );*/


/*

        // Create a DynamoDB client
        String region = Regions.AP_SOUTH_1.getName();
        DynamoDbClient ddbClient = DynamoDbClient.create();

        // Create a request to scan the NiftyIndexOHLCTable
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName("NiftyIndexOHLCTable")
                .build();

        // Scan the table
        ScanResponse scanResponse = ddbClient.scan(scanRequest);

        // Initialize the OHLC data list
        List<NiftyIndexOHLC> ohlcData = new ArrayList<>();

        // Populate the ohlcData list from the scan response
        for (Map<String, AttributeValue> item : scanResponse.items()) {
            Date date = new Date(Long.parseLong(item.get("date").n()));
            double open = Double.parseDouble(item.get("Open").n());
            double high = Double.parseDouble(item.get("High").n());
            double low = Double.parseDouble(item.get("Low").n());
            double close = Double.parseDouble(item.get("Close").n());
            double sharesTraded = Double.parseDouble(item.get("SharesTraded").n());
            double turnover = Double.parseDouble(item.get("Turnover").n());

            NiftyIndexOHLC ohlcEntry = new NiftyIndexOHLC(date, open, high, low, close, sharesTraded, turnover);
            ohlcData.add(ohlcEntry);
        }

        // Close the DynamoDB client
        ddbClient.close();

        // Call the analyze method with the populated ohlcData list
        Recommendation recommendation = analyze(ohlcData);
        System.out.println("Recommendation: " + recommendation);
*/

    }
}