package org.belajar.springbootapp;



public class ResponseFromApi {
    String success;
    String message;
    Data data;
    static class Data {
        String account_number;
        String account_holder;
        String account_bank;
    }

}
