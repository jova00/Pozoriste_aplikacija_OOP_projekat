package PozoristeAplikacija;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PosjetilacPozorista {
    private int id;
    private String ime;
    private String prezime;
    private String korisnicko_ime;
    private String lozinka;
    
    public static ArrayList<PosjetilacPozorista> sveIzPosjetilac = new ArrayList<>();

    public PosjetilacPozorista(int id, String ime, String prezime, String korisnicko_ime, String lozinka) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnicko_ime = korisnicko_ime;
        this.lozinka = lozinka;

        boolean sadrzano = false;
        for(PosjetilacPozorista p : sveIzPosjetilac) {
            if(p.korisnicko_ime.equals(this.korisnicko_ime))
                sadrzano = true;
        }
        if(sadrzano == false)
            sveIzPosjetilac.add(this);
    }

    public PosjetilacPozorista() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            Statement stmt = conn.createStatement();
        
            String strSelect = "select * from posjetilac_pozorista";
            ResultSet rSet = stmt.executeQuery(strSelect);
            while(rSet.next()) {
                new PosjetilacPozorista(
                    rSet.getInt("id"),
                    rSet.getString("ime"),
                    rSet.getString("prezime"),
                    rSet.getString("korisincko_ime"),
                    rSet.getString("lozinka")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public String getLozinka() {
        return lozinka;
    }
    
    public static PosjetilacPozorista posjetilacPrekoID(int ID) {
        for(PosjetilacPozorista p : sveIzPosjetilac){
            if(p.getId() == ID)
                return p;
        }
        return null;
    }

    public static int trenutniKorisnikPrekoID(String korisnicko){
        for(PosjetilacPozorista p : sveIzPosjetilac){
            if(p.getKorisnicko_ime().equals(korisnicko))
                return p.getId();
        }
        return 1000;
    }

    public static PosjetilacPozorista trenutniKorisnikPrekoString(String korisnik){
        for(PosjetilacPozorista p : sveIzPosjetilac){
            if(p.getKorisnicko_ime().equals(korisnik))
                return p;
        }
        return null;
    }
}
