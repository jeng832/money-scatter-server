package com.kakaopay.moneyscatter.model.vo;

public class ScatterVo {
    private int userId;
    private String roomId;
    private String token;
    private int cost;
    private int nPerson;
    private long requestedTime;
    private PickVo[] picks;

    public ScatterVo(int userId, String roomId, String token, int cost, int nPerson, long requestedTime) {
        this.userId = userId;
        this.roomId = roomId;
        this.token = token;
        this.cost = cost;
        this.nPerson = nPerson;
        this.requestedTime = requestedTime;
        this.picks = new PickVo[nPerson];
    }

    public ScatterVo(int userId, String roomId, String token, int cost, int nPerson, long requestedTime, PickVo[] picks) {
        this.userId = userId;
        this.roomId = roomId;
        this.token = token;
        this.cost = cost;
        this.nPerson = nPerson;
        this.requestedTime = requestedTime;
        this.picks = picks;
    }

    public int getUserId() {
        return userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getToken() {
        return token;
    }
    public int getCost() {
        return cost;
    }

    public int getPerson() {
        return nPerson;
    }

    public long getRequestedTime() {
        return requestedTime;
    }

    public PickVo[] getPicks() {
        return picks;
    }

    @Override
    public String toString() {
        return "ScatterVo[" +
                "userId=" + userId +
                ", roomId='" + roomId +
                ", token='" + token +
                ", cost=" + cost +
                ", nPerson=" + nPerson +
                ", requestedTime=" + requestedTime +
                ']';
    }
}
