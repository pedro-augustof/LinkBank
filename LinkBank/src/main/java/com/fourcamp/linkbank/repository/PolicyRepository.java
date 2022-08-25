package com.fourcamp.linkbank.repository;


import com.fourcamp.linkbank.model.Card;
import com.fourcamp.linkbank.model.Policy;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    Policy findPolicyByCard(Card card);



}
