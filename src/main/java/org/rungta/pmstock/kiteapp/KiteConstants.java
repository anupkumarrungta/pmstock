package org.rungta.pmstock.kiteapp;

public class KiteConstants {

    public static final String KITE_ROOT_ENDPOINT = "https://api.kite.trade";
    public static final String X_KITE_VERSION = "3";


    //Login Settings
    public static final String LOGIN_URL_FORMAT = "https://kite.zerodha.com/connect/login?v=3&api_key=%s&redirect_params=%s";
    public static final String TOKEN_URL = "https://api.kite.trade/session/token";

    public static final String apiKey = "your_api_key";
    public static final String apiSecret = "your_api_secret";


    //URL Endpoints
    public static final String KITE_ROOT_URL = "https://api.kite.trade/";
    public static final String KITE_INSTRUMENTS = KITE_ROOT_URL + "/instruments";



    //WEB SOCKET details
    private static final int WEBSOCKET_MAX_INSTRUMENTS_SUPPORT_IN_A_WEBSOCKET = 3000;
    private static final int WEBSOCKET_MAX_WEBSOCKET_CONNECTIONS = 3;
    public static final int WEBSOCKET_MAX_RETRIES = 5;
    public static final int WEBSOCKET_MAX_RETRY_INTERVAL = 10;


    //DDB Storage format
    public static final String TICK_STORAGE_TABLE_NAME_FORMAT = "Instrument_%s";    //Format for storing Tick data in DDB Tables in AWS Region. variable is replaced by InstrumentToken
    public static final String DDB_MARKET_INSTRUMENTS = "MarketInstruments";



}
