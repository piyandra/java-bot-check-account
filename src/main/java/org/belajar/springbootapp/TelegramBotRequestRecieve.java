package org.belajar.springbootapp;

public class TelegramBotRequestRecieve {

    String account_bank;
    String account_number;

    public TelegramBotRequestRecieve(String bank, String account) {
        this.account_bank = bank;
        this.account_number = account;
    }
}
