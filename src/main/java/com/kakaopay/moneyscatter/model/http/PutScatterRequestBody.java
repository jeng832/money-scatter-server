package com.kakaopay.moneyscatter.model.http;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PutScatterRequestBody {

    @JsonProperty("cost")
    private int cost;

    @JsonProperty("number_of_person")
    private int nPerson;

    public PutScatterRequestBody(int cost, int nPerson) {
        this.cost = cost;
        this.nPerson = nPerson;
    }

    public int getCost() {
        return cost;
    }

    public int getNumberOfPerson() {
        return nPerson;
    }

    @Override
    public String toString() {
        return "PutScatterRequestBody[" +
                "cost=" + cost +
                ", nPerson=" + nPerson +
                ']';
    }
}
