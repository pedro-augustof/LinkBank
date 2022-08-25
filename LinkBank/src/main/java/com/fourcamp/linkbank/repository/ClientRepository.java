package com.fourcamp.linkbank.repository;

import com.fourcamp.linkbank.model.Card;
import com.fourcamp.linkbank.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findClientByCpf(String cpf);

    Boolean existsByCpf(String cpf);
}
