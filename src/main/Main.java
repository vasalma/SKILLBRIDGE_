package main;

import front.login;
import frontEs.dashboard;

public class Main {

    public static void main(String[] args) {
        login ms = new login(); //ola
        ms.setVisible(true);
        ms.setLocationRelativeTo(null);
        DBConnection.getConnection();
        
        java.awt.EventQueue.invokeLater(() -> {
            System.out.println("📌 Creando dashboard...");
            dashboard d = new dashboard();
            System.out.println("📌 Dashboard creado, aplicando setVisible(true)");
            d.setVisible(true);
        });

    }
}
