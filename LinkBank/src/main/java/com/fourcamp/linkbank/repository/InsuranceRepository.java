package com.fourcamp.linkbank.repository;


import com.fourcamp.linkbank.model.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository  extends JpaRepository<Insurance, Long> {
}
