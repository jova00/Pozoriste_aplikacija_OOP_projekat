package PozoristeAplikacija;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Predstava {
    private int id;
    private String naziv;
    private int zanr;

    public static ArrayList<Predstava> sveIzPredstave = new ArrayList<>();
    
    public Predstava(int id, String naziv, int zanr) {
        this.id = id;
        this.naziv = naziv;
        this.zanr = zanr;

        boolean sadrzano = false;
        for(Predstava p: sveIzPredstave){
            if(p.naziv.equals(this.naziv) && p.zanr == this.zanr)
                sadrzano = true;
        }
        if(sadrzano == false)
            sveIzPredstave.add(this);
    }

    public Predstava() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            Statement stmt = conn.createStatement();
        
            String strSelect = "select * from predstava";
            ResultSet rSet = stmt.executeQuery(strSelect);
            while(rSet.next()) {
                new Predstava(
                    rSet.getInt("id"),
                    rSet.getString("naziv"),
                    rSet.getInt("zanr")
                );

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public int getZanr() {
        return zanr;
    }

    public static Predstava predstavaPrekoID(int ID) {
        for(Predstava p : sveIzPredstave){
            if(p.id == ID)
                return p;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Назив представе: " + this.naziv;
    }
    
    public static String sveInformacijePredstava(Predstava predstava){
        String info = "Позориште: " + predstava.naziv + "\n";
        info += "Жанр: " + Zanr.izBroj(predstava.zanr).toString() + "\n";
        info += IzvodjenjePredstave.izvodjenjePrekoID(predstava.id).sveInformacijeIzvodjenje() + "\n";
        return info;
    }
}
