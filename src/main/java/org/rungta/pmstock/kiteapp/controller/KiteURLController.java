package org.rungta.pmstock.kiteapp.controller;

import com.zerodhatech.kiteconnect.kitehttp.KiteRequestHandler;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.rungta.pmstock.kiteapp.login.KiteLoginService;
import org.rungta.pmstock.kiteapp.util.MarketInstrumentStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.rungta.pmstock.kiteapp.KiteConstants.apiKey;

public class KiteURLController {
    private static final Logger logger = LoggerFactory.getLogger(KiteURLController.class);

    public KiteURLController() {}

    public void getAndPersistAllMarketInstruments() {
        String accessToken = KiteLoginService.getAccessToken();
        if(accessToken == null) {
            logger.error("Access Token is null. Login is required prior to invoking any api calls.");
            return;
        }

        try {
            KiteRequestHandler kiteRequestHandler = new KiteRequestHandler(null);
            String marketInstruments = kiteRequestHandler.getCSVRequest("market.instruments", apiKey, accessToken);

            // Store the items in DynamoDB table named "MarketInstruments"
            MarketInstrumentStorageService.storeMarketInstrument(marketInstruments);
        } catch (KiteException e) {
            logger.error("KiteException encountered while calling /instruments", e);
        } catch (Exception e) {
            logger.error("Exception encountered while calling /instruments", e);
        }
    }

    //Skipped. Implement methods for /quote, /quote/ohlc, /quote/ltp https://kite.trade/docs/connect/v3/market-quotes/#instruments

}
