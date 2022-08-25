package com.fourcamp.linkbank.service;

import com.fourcamp.linkbank.enums.ExtractEnum;
import com.fourcamp.linkbank.enums.ExtractTypeEnum;
import com.fourcamp.linkbank.model.Account;
import com.fourcamp.linkbank.model.Extract;
import com.fourcamp.linkbank.repository.ExtractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExtractService {

    @Autowired
    private ExtractRepository extractRepository;

    public List<Extract> listById(Long id){

        return extractRepository.findExtractByAccount_id(id);
    }

    public void insertDeposit(Account account, Double value){
        Extract extract = new Extract();

        extract.setDescription(ExtractTypeEnum.DEPOSIT.getKey());
        extract.setValue(value);
        extract.setAccount_id(account.getId());
        extract.setType(ExtractEnum.RECIEVED.getKey());

        extractRepository.save(extract);
    }

    public void insertBill(Account account, Double value){
        Extract extract = new Extract();

        extract.setDescription(ExtractTypeEnum.BILL.getKey());
        extract.setValue(value);
        extract.setAccount_id(account.getId());
        extract.setType(ExtractEnum.PAID.getKey());

        extractRepository.save(extract);
    }

    public void insertCardPay(Account account, Double value) {
        Extract extract = new Extract();

        extract.setDescription(ExtractTypeEnum.DEBIT.getKey());
        extract.setValue(value);
        extract.setAccount_id(account.getId());
        extract.setType(ExtractEnum.PAID.getKey());

        extractRepository.save(extract);
    }

    public void insertCreditPay(Account account, Double value) {
        Extract extract = new Extract();

        extract.setDescription(ExtractTypeEnum.CREDIT.getKey());
        extract.setValue(value);
        extract.setAccount_id(account.getId());
        extract.setType(ExtractEnum.PAID.getKey());

        extractRepository.save(extract);
    }

    public void insertMonthlyBill(Account account, Double value) {
        Extract extract = new Extract();

        extract.setDescription(ExtractTypeEnum.MONTHLY_BILL.getKey());
        extract.setValue(value);
        extract.setAccount_id(account.getId());
        extract.setType(ExtractEnum.PAID.getKey());

        extractRepository.save(extract);
    }
}
