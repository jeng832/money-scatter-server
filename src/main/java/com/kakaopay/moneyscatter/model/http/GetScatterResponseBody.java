package com.kakaopay.moneyscatter.model.http;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetScatterResponseBody {

    @JsonProperty("scattered_time")
    private long scatteredTime;

    @JsonProperty("total_cost")
    private int cost;

    @JsonProperty("picked_cost")
    private int pickedCost;

    @JsonProperty("pick")
    private List<Picker> pick;



    public GetScatterResponseBody(long scatteredTime, int cost, int pickedCost, List<Picker> pick) {
        this.scatteredTime = scatteredTime;
        this.cost = cost;
        this.pickedCost = pickedCost;
        this.pick = pick;
    }
}
