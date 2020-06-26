package com.kakaopay.moneyscatter.model.http;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Picker {
    @JsonProperty("user_id")
    Integer userId;

    @JsonProperty("cost")
    int cost;

    public Picker(Integer userId, int cost) {
        this.userId = userId;
        this.cost = cost;
    }


}
