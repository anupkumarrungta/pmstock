package org.rungta.pmstock.kiteapp.util;

import org.apache.http.HttpRequest;
import org.rungta.pmstock.kiteapp.KiteConstants;

public class HttpRequestUtil {

    /**
     * Adds the Authorization header to the given HttpRequest.
     *
     * @param request      The HttpRequest to which the header should be added.
     * @param apiKey       The API key.
     * @param accessToken  The access token.
     */
    public static void addAuthorizationHeader(HttpRequest request, String apiKey, String accessToken) {
        String authHeaderValue = String.format("token %s:%s", apiKey, accessToken);
        request.setHeader("Authorization", authHeaderValue);
        request.setHeader("X-Kite-Version", KiteConstants.X_KITE_VERSION);
    }
}