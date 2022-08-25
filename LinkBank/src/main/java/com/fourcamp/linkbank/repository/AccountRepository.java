package com.fourcamp.linkbank.repository;

import com.fourcamp.linkbank.model.Account;
import com.fourcamp.linkbank.model.Card;
import com.fourcamp.linkbank.model.Client;
import com.fourcamp.linkbank.model.Pix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findAccountByClient(Client client);

    Account findAccountByPix(Pix pix);

    Account findAccountByNumber(String number);

    Account findAccountByCard(Card card);
}
