package PozoristeAplikacija;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RadnikPozorista {
    private int id;
    private int pozoriste_id;
    private String ime;
    private String prezime;
    private String korisnicko_ime;
    private String lozinka;
    
    public static ArrayList<RadnikPozorista> sveIzRadnik = new ArrayList<>();

    public RadnikPozorista(int id, int pozoriste_id, String ime, String prezime, String korisnicko_ime,
            String lozinka) {
        this.id = id;
        this.pozoriste_id = pozoriste_id;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnicko_ime = korisnicko_ime;
        this.lozinka = lozinka;

        boolean sadrzano = false;
        for(RadnikPozorista r : sveIzRadnik){
            if(r.korisnicko_ime.equals(this.korisnicko_ime))
                sadrzano = true;
        }
        if(sadrzano == false)
            sveIzRadnik.add(this);
    }
    
    public RadnikPozorista() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            Statement stmt = conn.createStatement();
        
            String strSelect = "select * from radnik_pozorista";
            ResultSet rSet = stmt.executeQuery(strSelect);
            while(rSet.next()) {
                new RadnikPozorista(
                    rSet.getInt("id"),
                    rSet.getInt("pozoriste_id"),
                    rSet.getString("ime"),
                    rSet.getString("prezime"),
                    rSet.getString("korisnicko_ime"),
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

    public int getPozoriste_id() {
        return pozoriste_id;
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

    
    public static String sveInformacijeRadnik(RadnikPozorista radnik) {
        String info = "Име: " + radnik.ime + "\n" +
                        "Презиме: " + radnik.prezime + "\n" +
                        "Корисничко име: " + radnik.korisnicko_ime + "\n" +
                        "Лозинка: " + radnik.lozinka + "\n";
                        
        return info;
    }

    @Override
    public String toString() {
        return "Име: " + this.ime + "\n" +
        "Презиме: " + this.prezime + "\n" +
        "Корисничко име: " + this.korisnicko_ime + "\n" +
        "Лозинка: " + this.lozinka + "\n";
    }
}
