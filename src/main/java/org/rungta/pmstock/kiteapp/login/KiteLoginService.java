package org.rungta.pmstock.kiteapp.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rungta.pmstock.kiteapp.KiteConstants;
import org.rungta.pmstock.kiteapp.util.HttpRequestUtil;


public class KiteLoginService {

    private final String apiKey;
    private final String apiSecret;
    private String accessToken;

    public KiteLoginService(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.accessToken = null;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLoginUrl(String redirectParams) {
        return String.format(KiteConstants.LOGIN_URL_FORMAT, apiKey, redirectParams != null ? redirectParams : "");
    }

    public String generateAccessToken(String requestToken) throws Exception {
        String checksum = generateChecksum(apiKey, requestToken, apiSecret);
        String accessToken = postToken(requestToken, checksum);
        this.accessToken = accessToken;

        return accessToken;
    }

    private String generateChecksum(String apiKey, String requestToken, String apiSecret) throws NoSuchAlgorithmException {
        String value = apiKey + requestToken + apiSecret;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String postToken(String requestToken, String checksum) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(KiteConstants.TOKEN_URL);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");

        String body = String.format("api_key=%s&request_token=%s&checksum=%s", apiKey, requestToken, checksum);
        post.setEntity(new org.apache.http.entity.StringEntity(body));

        HttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseString);
        JsonNode accessTokenNode = jsonNode.get("access_token");

        if (accessTokenNode == null) {
            throw new IllegalArgumentException("Response does not contain access_token");
        }

        return accessTokenNode.asText();
    }

    public String makeAuthenticatedRequest(String url) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            org.apache.http.client.methods.HttpGet get = new HttpGet(url);
            HttpRequestUtil.addAuthorizationHeader(get, apiKey, accessToken);
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        }
    }
/*
    public static void main(String[] args) {

        String redirectParams = "some=X&more=Y"; // Optional
        KiteLoginService kiteLoginService = new KiteLoginService(KiteConstants.apiKey, KiteConstants.apiSecret);

        // Step 1: Get login URL and navigate to it in a web browser
        String loginUrl = kiteLoginService.getLoginUrl(redirectParams);
        System.out.println("Navigate to: " + loginUrl);

        // Step 2: After successful login, you'll get a request_token
        // Manually navigate and obtain request_token, then proceed to get access_token:
        try {
            String requestToken = "obtained_request_token";
            String accessToken = kiteLoginService.generateAccessToken(requestToken);
            System.out.println("Access Token: " + accessToken);

            // Example authenticated request
            String url = "https://api.kite.trade/user/profile";
            String response = kiteLoginService.makeAuthenticatedRequest(url);
            System.out.println("Authenticated Response: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
