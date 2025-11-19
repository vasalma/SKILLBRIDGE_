package main;

import front.login;

public class Main {

    public static void main(String[] args) {

        // Solo se debe abrir el LOGIN
        java.awt.EventQueue.invokeLater(() -> {
            login ms = new login();
            ms.setVisible(true);
            ms.setLocationRelativeTo(null);
        });
    }
}

