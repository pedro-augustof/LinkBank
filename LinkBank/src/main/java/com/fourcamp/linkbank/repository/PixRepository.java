package com.fourcamp.linkbank.repository;

import com.fourcamp.linkbank.model.Pix;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PixRepository extends JpaRepository<Pix, Long> {

    Pix findByEmail(String email);

    Pix findByCellphone(String cellphone);

    Pix findByCpf(String cpf);
}
