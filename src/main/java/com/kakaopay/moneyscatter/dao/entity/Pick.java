package com.kakaopay.moneyscatter.dao.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Pick implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_id", nullable = false)
    private Integer pickId;

    @Column(name = "each_cost")
    private int eachCost;


    @Column(name = "user_id")
    private Integer userId;

    public Pick() {

    }

    public Pick(int eachCost) {
        this.eachCost = eachCost;
        this.userId = null;

    }

    public Integer getPickId() {
        return pickId;
    }

    public int getEachCost() {
        return eachCost;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer uid) {
        userId = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pick pick = (Pick) o;
        return eachCost == pick.eachCost &&
                pickId.equals(pick.pickId) &&
                userId.equals(pick.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pickId, eachCost, userId);
    }

    @Override
    public String toString() {
        return "Pick[" +
                "pickId=" + pickId +
                ", eachCost=" + eachCost +
                ", userId=" + userId +
                ']';
    }
}
