package com.kakaopay.moneyscatter.dao.repository;

import com.kakaopay.moneyscatter.dao.entity.Scatter;

import com.kakaopay.moneyscatter.dao.entity.ScatterId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScatterRepository extends JpaRepository<Scatter, ScatterId> {
    @Override
    Optional<Scatter> findById(ScatterId scatterId);
    Optional<Scatter> findByUserId(Integer userId);
}
