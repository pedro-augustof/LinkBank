package com.fourcamp.linkbank.repository;

import com.fourcamp.linkbank.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    Boolean existsByNumber(String number);

    @Query(value = " select c from Card c where c.number =:number ")
    Card findByNumber(@Param("number") String number);
}
