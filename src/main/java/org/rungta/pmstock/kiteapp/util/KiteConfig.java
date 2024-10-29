package org.rungta.pmstock.kiteapp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KiteConfig {

    @Value("${kite.apiKey}")
    private String apiKey;

    @Value("${kite.apiSecret}")
    private String apiSecret;

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }
}