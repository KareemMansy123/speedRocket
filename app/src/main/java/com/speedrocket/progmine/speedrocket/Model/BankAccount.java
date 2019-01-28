package com.speedrocket.progmine.speedrocket.Model;



public class BankAccount
{
    int   id,swift ,checked;
    String bankAddress , name, bankAccount ;

    public  BankAccount () {} // zero constructor

    public BankAccount(int swift, int id, String bankAccount, String bankAddress, String name ,int  checked) {
        this.swift = swift;
        this.id = id;
        this.bankAccount = bankAccount;
        this.bankAddress = bankAddress;
        this.name = name;
        this.checked = checked ;
    } // param constructor

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getSwift() {
        return swift;
    }

    public void setSwift(int swift) {
        this.swift = swift;
    }



    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
} // class of BankAccount
