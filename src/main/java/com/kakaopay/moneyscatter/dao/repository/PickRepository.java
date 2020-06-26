package com.kakaopay.moneyscatter.dao.repository;

import com.kakaopay.moneyscatter.dao.entity.Pick;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PickRepository extends JpaRepository<Pick, Integer> {
    @Override
    Optional<Pick> findById(Integer pickId);
}
