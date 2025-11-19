package main;

import front.login;

public class Main {

    public static void main(String[] args) {
        login ms = new login(); //ola
        ms.setVisible(true);
        ms.setLocationRelativeTo(null);
        DBConnection.getConnection();
    }
}

