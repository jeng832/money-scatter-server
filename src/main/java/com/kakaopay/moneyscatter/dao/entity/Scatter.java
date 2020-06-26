package com.kakaopay.moneyscatter.dao.entity;

import org.hibernate.collection.internal.PersistentSet;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Scatter implements Serializable {

    @EmbeddedId
    private ScatterId scatterId;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Pick> picks = new ArrayList<>();

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "number_of_person", nullable = false)
    private int numberOfPerson;

    @Column(name = "total_cost", nullable = false)
    private int totalCost;

    @Column(name = "created_time", nullable = false)
    private Timestamp createdTime;

    public Scatter() {

    }
    public Scatter(ScatterId scatterId, Integer userId, int numberOfPerson, int totalCost, Timestamp createdTime) {
        this.scatterId = scatterId;
        this.userId = userId;
        this.numberOfPerson = numberOfPerson;
        this.totalCost = totalCost;
        this.createdTime = createdTime;
    }
    public Scatter(ScatterId scatterId, List<Pick> picks, Integer userId, int numberOfPerson, int totalCost, Timestamp createdTime) {
        this.scatterId = scatterId;
        this.picks = picks;
        this.userId = userId;
        this.numberOfPerson = numberOfPerson;
        this.totalCost = totalCost;
        this.createdTime = createdTime;
    }
    public Scatter(ScatterId scatterId, Pick pick, Integer userId, int numberOfPerson, int totalCost, Timestamp createdTime) {
        this.scatterId = scatterId;
        this.picks.add(pick);
        this.userId = userId;
        this.numberOfPerson = numberOfPerson;
        this.totalCost = totalCost;
        this.createdTime = createdTime;
    }

    public ScatterId getScatterId() {
        return scatterId;
    }

    public List<Pick> getPicks() {
        return picks;
    }

    public int getUserId() {
        return userId;
    }

    public int getNumberOfPerson() {
        return numberOfPerson;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scatter scatter = (Scatter) o;
        return userId == scatter.userId &&
                numberOfPerson == scatter.numberOfPerson &&
                totalCost == scatter.totalCost &&
                scatterId.equals(scatter.scatterId) &&
                picks.equals(scatter.picks) &&
                createdTime.equals(scatter.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scatterId, picks, userId, numberOfPerson, totalCost, createdTime);
    }

    @Override
    public String toString() {
        return "Scatter[" +
                "scatterId=" + scatterId +
                ", picks=" + picks +
                ", userId=" + userId +
                ", numberOfPerson=" + numberOfPerson +
                ", totalCost=" + totalCost +
                ", createdTime=" + createdTime +
                ']';
    }
}
