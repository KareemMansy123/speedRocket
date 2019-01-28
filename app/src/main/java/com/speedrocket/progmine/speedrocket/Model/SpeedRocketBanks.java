package com.speedrocket.progmine.speedrocket.Model;

/**
 * Created by Ibrahim on 9/2/2018.
 */

public class SpeedRocketBanks {
int id ;
String name , bankAccount ,bankAddress ,swift  , accountName;

public  SpeedRocketBanks(){}

public SpeedRocketBanks(int id , String name , String bankAccount , String bankAddress , String swift ,String accountName){
    this .id = id ;
    this .name = name ;
    this .bankAccount = bankAccount ;
    this .bankAddress = bankAddress ;
    this .swift = swift;
    this.accountName = accountName ;

}

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }
}
