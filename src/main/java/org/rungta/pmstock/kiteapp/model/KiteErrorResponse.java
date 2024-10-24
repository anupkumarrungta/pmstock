package org.rungta.pmstock.kiteapp.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents an error response from the Kite API.
 * <p>
 * This class encapsulates the status, message, and type of error
 * typically returned by the Kite API in response to failed requests.
 * <p>
 * HTTP/1.1 500 Server error
 * Content-Type: application/json
 * <p>
 * {
 * "status": "error",
 * "message": "Error message",
 * "error_type": "GeneralException"
 * }
 * A failure response is preceded by the corresponding 40x or 50x HTTP header. The status key in the response envelope contains the value error.
 * The message key contains a textual description of the error and error_type contains the name of the exception.
 * There may be an optional data key with additional payload.
 */
public class KiteErrorResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("error_type")
    private ErrorTypeEnum errorType;

    public enum ErrorTypeEnum {
        TokenException,
        UserException,
        OrderException,
        InputException,
        MarginException,
        HoldingException,
        NetworkException,
        DataException,
        GeneralException;

        @JsonCreator
        public static ErrorTypeEnum fromString(String key) {
            return key == null ? null : ErrorTypeEnum.valueOf(key);
        }

        @JsonValue
        public String toString() {
            return this.name();
        }
    }

    @JsonProperty("data")
    private Map<String, Object> data;

// Getters and Setters

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    // Getters and Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorType() {
        return errorType.toString();
    }

    public void setErrorType(ErrorTypeEnum errorType) {
        this.errorType = errorType;
    }

    // Serialize the object to JSON
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    // Deserialize JSON to object
    public static KiteErrorResponse fromJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, KiteErrorResponse.class);
    }

}
    

