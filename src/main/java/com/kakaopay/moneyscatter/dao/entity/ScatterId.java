package com.kakaopay.moneyscatter.dao.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ScatterId implements Serializable {

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "token")
    private String token;

    public ScatterId() {

    }
    public ScatterId(String roomId, String token) {
        this.roomId = roomId;
        this.token = token;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScatterId scatterId = (ScatterId) o;
        return roomId.equals(scatterId.roomId) &&
                token.equals(scatterId.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, token);
    }

    @Override
    public String toString() {
        return "ScatterId[" +
                "roomId='" + roomId +
                ", token='" + token +
                ']';
    }
}
