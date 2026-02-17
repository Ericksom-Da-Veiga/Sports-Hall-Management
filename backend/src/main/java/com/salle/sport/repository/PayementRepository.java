package com.salle.sport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salle.sport.entites.Payement;

public interface PayementRepository extends JpaRepository<Payement, Long> {

    @Query(value = "call ShowMoneyForCurrentMonth()", nativeQuery = true)
    Long CountMoneyForCurrentMonth();
    
    @Query(value = "call ShowTotalMoney()", nativeQuery = true)
    Long CountTotalMoney(); 
}
