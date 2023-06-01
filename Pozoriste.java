package PozoristeAplikacija;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Pozoriste {
    private int id;
    private String naziv;
    private String grad;
    private int broj_sjedista;

    public static ArrayList<Pozoriste> sveIzPozorista = new ArrayList<>();
    public ArrayList<Predstava> predstavaIzabranogPozorista = new ArrayList<>();

    
    public Pozoriste(int id, String naziv, String grad, int broj_sjedista) {
        this.id = id;
        this.naziv = naziv;
        this.grad = grad;
        this.broj_sjedista = broj_sjedista;

        boolean sadrzano = false;
        for(Pozoriste p : sveIzPozorista){
            if(p.naziv.equals(this.naziv) && p.grad.equals(this.grad)){
                sadrzano = true;
            }
        }
        if(sadrzano == false)
            sveIzPozorista.add(this);
    }

    public Pozoriste() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            Statement stmt = conn.createStatement();
        
            String strSelect = "select * from pozoriste";
            ResultSet rSet = stmt.executeQuery(strSelect);
            while(rSet.next()) {
                new Pozoriste(
                    rSet.getInt("id"),
                    rSet.getString("naziv"),
                    rSet.getString("grad"),
                    rSet.getInt("broj_sjedista")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodajPredstavu(Predstava predstava) {
        boolean sadrzano = false;
        for(Predstava p: predstavaIzabranogPozorista){
            if(p.getNaziv().equals(predstava.getNaziv()))
                sadrzano = true;
        }
        if(sadrzano == false)
            predstavaIzabranogPozorista.add(predstava);
    }

   
    public ArrayList<Predstava> getPredstavaIzabranogPozorista(){
        return predstavaIzabranogPozorista;
    }
    public static ArrayList<Pozoriste> getSveIzPozorista(){
        return sveIzPozorista;
    }


    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getGrad() {
        return grad;
    }

    public int getBroj_sjedista() {
        return broj_sjedista;
    }

    public static Pozoriste pozoristePrekoID(int ID){
        for(Pozoriste p : sveIzPozorista){
            if(p.id == ID)
                return p;
        }
        return null;
    }

    public static String sveInformacijePozoriste (Pozoriste pozoriste) {
        String info = "Позориште: " + pozoriste.naziv + "\n" +
                        "Град: " + pozoriste.grad + "\n" +
                        "Број сједишта: " + pozoriste.broj_sjedista + "\n" +
                        "Репертоар: \n";
                        for(Predstava p : pozoriste.predstavaIzabranogPozorista) {
                            info+= p.getNaziv() + "\n";
                        }
        return info;
    }
    
    @Override
    public String toString() {
        return "Позориште: " + this.naziv + ", град: " + this.grad + ", број сједишта: " + this.broj_sjedista;
    }
}
