package org.rungta.pmstock.kiteapp;

import com.zerodhatech.kiteconnect.KiteConnect;
import jakarta.servlet.http.HttpServletRequest;
import org.rungta.pmstock.kiteapp.controller.KiteWebSocketController;
import org.rungta.pmstock.kiteapp.login.KiteLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KiteController {

    private static final Logger logger = LoggerFactory.getLogger(KiteController.class);
    KiteConnect kiteSdk = new KiteConnect("your_apiKey");

    @RequestMapping("/")
    public String handleRootRequest() {
        return "<p>Please sign in <a href=\"/getLoginURL\">Stock</a> and provide requestToken by calling <b>/generateAccessToken?accessToken=XXXX </b> to generate accessToken. </p>";
    }

    // GetLoginURL Controller
    @GetMapping("/getLoginURL")
    public String getLoginURL() {
        KiteLoginService kls = new KiteLoginService(KiteConstants.apiKey, KiteConstants.apiSecret);
        String redirectParams = "/generateAccessToken"; // Optional "some=X&more=Y"
        return kls.getLoginUrl(redirectParams);
    }

    // generateAccessToken Controller with access_token parameter
    @GetMapping("/generateAccessToken")
    public String generateAccessToken(@RequestParam("requestToken") String requestToken) {
        if (requestToken == null) {
            logger.error("requestToken is null");
            return "requestToken is null. Login to kite via /getLoginURL and provide a valid request token";
        }

        try {
            KiteLoginService kls = new KiteLoginService(KiteConstants.apiKey, KiteConstants.apiSecret);
            return kls.generateAccessToken(requestToken);
        } catch (Exception e) {
            logger.error("Failed to generateAccessToken. Encountered exception {}", e.getMessage(), e);
            return "Error generating access token. try again later.";
        }
    }

    // Healthcheck Controller
    @GetMapping("/healthcheck")
    public String healthCheck() {
        return "Health check passed.";
    }

    // Home Controller
    @GetMapping("/home")
    public String home() {
        return "Welcome to the Kite App Home!";
    }

/*    // Error Handling Controller
    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        return "An error occurred, please try again later.";
    }*/



    //Implement the methods for invoking an Kite API Request and saving the stock


    
}
