package com.kakaopay.moneyscatter.model.vo;

import org.springframework.lang.Nullable;

public class PickVo {

    private Integer userId;
    private int cost;
    private String token;

    public PickVo(Integer userId, int cost, String token) {
        this.userId = userId;
        this.cost = cost;
        this.token = token;
    }

    public PickVo(int cost, String token) {
        this.userId = null;
        this.cost = cost;
        this.token = token;
    }

    @Nullable
    public Integer getUserId() {
        return userId;
    }

    public int getCost() {
        return cost;
    }

    public String getToken() {
        return token;
    }

    public boolean isPicked() {
        return userId != null;
    }

    public void pick(int uid) {
        userId = uid;
    }

    @Override
    public String toString() {
        return "PickVo[" +
                "userId=" + userId +
                ", cost=" + cost +
                ", token='" + token +
                ']';
    }
}
