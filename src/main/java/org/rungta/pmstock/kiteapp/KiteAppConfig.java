package org.rungta.pmstock.kiteapp;

// Additional imports for HTTPS support with WebClient
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import javax.net.ssl.SSLContext;
//import org.apache.hc.core5.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;

import java.io.File;

@Configuration
public class KiteAppConfig {

    /*
    @Bean
    public WebClient webClient() throws Exception {
        HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> {
            try {
                SSLContext sslContext = SSLContextBuilder.create()
                        .loadTrustMaterial() //to be revisited later
                        .build();
                sslContextSpec.sslContext(sslContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
    */


}