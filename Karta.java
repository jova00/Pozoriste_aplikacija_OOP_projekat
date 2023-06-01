package PozoristeAplikacija;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class Karta {
    private int id;
    private int izvodjenje_predstave_id;
    private int status;
    private int posjetilac_pozorista_id;
    private int broj_karata;

    public static ArrayList<Karta> sveIzKarte = new ArrayList<>();
    
    public Karta(int id, int izvodjenje_predstave_id, int status,
            int posjetilac_pozorista_id, int broj_karata) {
        this.id = id;
        this.izvodjenje_predstave_id = izvodjenje_predstave_id;
        this.status = status;
        this.posjetilac_pozorista_id = posjetilac_pozorista_id;
        this.broj_karata = broj_karata;

        for(int i=0; i<sveIzKarte.size(); i++){
            Karta k = sveIzKarte.get(i);
            if(k.id == this.id) {
                sveIzKarte.remove(i);
            }
        }
        sveIzKarte.add(this);
    }
    
    public Karta() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            Statement stmt = conn.createStatement();
        
            String strSelect = "select * from karta";
            ResultSet rSet = stmt.executeQuery(strSelect);
            while(rSet.next()) {
                new Karta(
                    rSet.getInt("id"),
                    rSet.getInt("izvodjenje_predstave_id"),
                    rSet.getInt("status"),
                    rSet.getInt("posjetilac_id"),
                    rSet.getInt("broj_karta")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public int getIzvodjenje_predstave_id() {
        return izvodjenje_predstave_id;
    }

    public int getStatus() {
        return status;
    }

    public int getPosjetilac_pozorista_id() {
        return posjetilac_pozorista_id;
    }

    public int getBroj_karata() {
        return broj_karata;
    }

    

    public void setStatus(int status) {
        this.status = status;
    }

    public static int kartaPrekoID(Pozoriste pozoriste, Predstava predstava, String korisnik){
        int korisnikID = PosjetilacPozorista.trenutniKorisnikPrekoID(korisnik);
        int izvodjenjePredstaveID = IzvodjenjePredstave.izvodjenjePPID(pozoriste, predstava);
        int kartaID = 1000;
        for(Karta k : sveIzKarte){
            if(k.getIzvodjenje_predstave_id() == izvodjenjePredstaveID && k.getPosjetilac_pozorista_id() == korisnikID)
                return k.getId();
        }
        return kartaID;
    }

    public static int sledeciSlobodanBroj(int izvodjenjePredstaveID, int brojSjedista){
        ArrayList<Integer> brojevi = new ArrayList<>();
        boolean postoji = false;
        for(Karta k : sveIzKarte){
            if(k.getIzvodjenje_predstave_id() == izvodjenjePredstaveID){
                brojevi.add(k.getBroj_karata());
                postoji = true;
            }
        }
        if(!postoji)
            return 1;
        for(int i=1; i<=brojSjedista; i++){
            if(!brojevi.contains(i))
                return i;
        }
        return 1;
    }
    
    @Override
    public String toString() {
        String s = "КАРТА БРОЈ: " + this.getBroj_karata() + "\n";
        if(this.status == 1)
            s+= "Статус карте: купљена и предана на благајни. \n";
        else if(this.status == 2)
            s+= "Статус карте: купљена преко апликације -> НИЈЕ ПРЕУЗЕТА\n";
        else 
            s+="Статус карте: купљена преко апликације -> ПРЕУЗЕТА\n";

        for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja){
            if(this.getIzvodjenje_predstave_id() == i.getId()){
                s+= "Позориште: " + Pozoriste.pozoristePrekoID(i.getPozoriste_id()) + "\n";
                s+= "Представа: " + Predstava.predstavaPrekoID(i.getPredstava_id()) + "\n";
                s+= "Цијена: " + i.getCijena() + "\n";
                s+= "Датум: " + i.getDatum_i_vrijeme();
            }
        }
        return s;
    }
}
