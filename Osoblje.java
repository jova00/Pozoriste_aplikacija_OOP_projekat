package PozoristeAplikacija;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Osoblje {
    private int id;
    private String ime;
    private String prezime;
    private int tip;

    public static ArrayList<Osoblje> sveIzOsoblje = new ArrayList<>();
    
    public Osoblje(int id, String ime, String prezime, int tip) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.tip = tip;

        boolean sadrzano = false;
        for(Osoblje o : sveIzOsoblje){
            if(o.ime.equals(this.ime) && o.prezime.equals(this.prezime))
                sadrzano = true;
        }
        if(sadrzano == false)
            sveIzOsoblje.add(this);
    }

    public Osoblje() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            Statement stmt = conn.createStatement();
        
            String strSelect = "select * from osoblje";
            ResultSet rSet = stmt.executeQuery(strSelect);
            while(rSet.next()) {
                new Osoblje(
                    rSet.getInt("id"),
                    rSet.getString("ime"),
                    rSet.getString("prezime"),
                    rSet.getInt("tip")
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

    public int getTip() {
        return tip;
    }

    public static ArrayList<Osoblje> getSveIzOsoblje() {
        return sveIzOsoblje;
    }

    public static Osoblje osobljePrekoID(int ID) {
        for(Osoblje o : sveIzOsoblje){
            if(o.getId() == ID)
                return o;
        }
        return null;
    }
    
    public static String sveInformacijeOsoblje(Osoblje osoblje) {
        String info = "Име: " + osoblje.ime + "\n" +
                        "Презиме: " + osoblje.prezime + "\n" +
                        "Представама у којим сурађује: \n";
                        for(OsobljePredstave o : OsobljePredstave.sveIzOsobljePredstave){
                            if(o.getOsoblje_id() == osoblje.id)
                                info += Predstava.predstavaPrekoID(o.getPredstava_id()) + "\n";
                        }
                        
        return info;
    }

    @Override
    public String toString() {
        return "Име: " + this.ime + ", презиме: " + this.prezime;
    }

}
