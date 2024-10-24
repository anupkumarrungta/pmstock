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


}
