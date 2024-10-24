package org.rungta.pmstock.kiteapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class UserProfileModel {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_type")
    private String userType;

    @JsonProperty("email")
    private String email;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_shortname")
    private String userShortname;

    @JsonProperty("broker")
    private String broker;

    @JsonProperty("exchanges")
    private List<String> exchanges;


    @JsonProperty("products")
    private List<String> products;

    @JsonProperty("order_types")
    private List<String> orderTypes;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("meta")
    private JsonNode meta;

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserShortname() {
        return userShortname;
    }

    public void setUserShortname(String userShortname) {
        this.userShortname = userShortname;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public List<String> getExchanges() {
        return exchanges;
    }

    public void setExchanges(List<String> exchanges) {
        this.exchanges = exchanges;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public List<String> getOrderTypes() {
        return orderTypes;
    }

    public void setOrderTypes(List<String> orderTypes) {
        this.orderTypes = orderTypes;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public JsonNode getMeta() {
        return meta;
    }

    public void setMeta(JsonNode meta) {
        this.meta = meta;
    }
}

