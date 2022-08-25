package com.fourcamp.linkbank.model;


public class TransactionTransfer extends Transaction {

    private Account receiverAccount;


    public TransactionTransfer(Double value, String date, Long id, Account receiverAccount) {
        super(value, date, id);
        this.receiverAccount = receiverAccount;
    }

    public TransactionTransfer(Account receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    public TransactionTransfer() {
    }



    public Account getReceiverAccount() {
        return receiverAccount;
    }

    public void setReceiverAccount(Account receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    @Override
    public String toString() {
        return "Transferência {" +
                "Conta destino =" + receiverAccount +
                '}';
    }
}
