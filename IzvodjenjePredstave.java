package PozoristeAplikacija;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class IzvodjenjePredstave {
    private int id;
    private int predstava_id;
    private int pozoriste_id;
    private double cijena;
    private Date datum_i_vrijeme;

    public static ArrayList<IzvodjenjePredstave> sveIzIzvodjenja = new ArrayList<>();
    
    public IzvodjenjePredstave(int id, int predstava_id, int pozoriste_id, double cijena,
            Date datum_i_vrijeme) {
        this.id = id;
        this.predstava_id = predstava_id;
        this.pozoriste_id = pozoriste_id;
        this.cijena = cijena;
        this.datum_i_vrijeme = datum_i_vrijeme;

        boolean sadrzano = false;
        for(IzvodjenjePredstave i : sveIzIzvodjenja) {
            if(i.id == this.id || (i.pozoriste_id == this.pozoriste_id && i.predstava_id == this.predstava_id))
                sadrzano = true;
        }
        if(sadrzano == false)
            sveIzIzvodjenja.add(this);
    }

    public IzvodjenjePredstave() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            Statement stmt = conn.createStatement();
        
            String strSelect = "select * from izvodjenje_predstave";
            ResultSet rSet = stmt.executeQuery(strSelect);
            while(rSet.next()) {
                new IzvodjenjePredstave(
                    rSet.getInt("id"),
                    rSet.getInt("predstava_id"),
                    rSet.getInt("pozoriste_id"),
                    rSet.getDouble("cijena"),
                    rSet.getDate("datum_i_vrijeme")
                );
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public int getPredstava_id() {
        return predstava_id;
    }

    public int getPozoriste_id() {
        return pozoriste_id;
    }

    public double getCijena() {
        return cijena;
    }

    public Date getDatum_i_vrijeme() {
        return datum_i_vrijeme;
    }

    public static IzvodjenjePredstave izvodjenjePrekoID(int ID) {
        for(IzvodjenjePredstave i : sveIzIzvodjenja) {
            if(i.getId() == ID)
                return i;
        }
        return null;
    }

    public static int izvodjenjePPID(Pozoriste pozoriste, Predstava predstava){
        int trenutniID = 1000;
        for(IzvodjenjePredstave i : sveIzIzvodjenja){
            if(i.getPozoriste_id() == pozoriste.getId() && i.getPredstava_id() == predstava.getId())
                return i.getId();
        }
        return trenutniID;
    }
    
    @Override
    public String toString() {
        return "Цијена: " + this.cijena + ", датум и вријеме: " + this.datum_i_vrijeme;
    }

    public String sveInformacijeIzvodjenje() {
        String info = "Цијена карте: " + this.cijena + "\n" +
                        "Датум и вријеме извођења: " + this.datum_i_vrijeme + "\n";
        return info;
    }
}
