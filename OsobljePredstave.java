package PozoristeAplikacija;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OsobljePredstave {
    private int id;
    private int osoblje_id;
    private int predstava_id;

    public static ArrayList<OsobljePredstave> sveIzOsobljePredstave = new ArrayList<>();
    
    public OsobljePredstave(int id, int osoblje_id, int predstava_id) {
        this.id = id;
        this.osoblje_id = osoblje_id;
        this.predstava_id = predstava_id;

        boolean sadrzano = false;
        for(OsobljePredstave o : sveIzOsobljePredstave){
            if(o.osoblje_id == this.osoblje_id && o.predstava_id == this.predstava_id)
                sadrzano = true;
        }
        if(sadrzano == false)
            sveIzOsobljePredstave.add(this);
    }

    public OsobljePredstave() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            Statement stmt = conn.createStatement();
        
            String strSelect = "select * from osoblje_predstave";
            ResultSet rSet = stmt.executeQuery(strSelect);
            while(rSet.next()) {
                new OsobljePredstave(
                    rSet.getInt("id"),
                    rSet.getInt("osoblje_id"),
                    rSet.getInt("predstava_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public int getOsoblje_id() {
        return osoblje_id;
    }

    public int getPredstava_id() {
        return predstava_id;
    }

    public static Osoblje vratiOSobljePredstavePrekoID(OsobljePredstave ID) {
        for(Osoblje o : Osoblje.sveIzOsoblje){
            if(o.getId() == ID.osoblje_id)
                return o;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return Osoblje.osobljePrekoID(this.osoblje_id).toString();
    }
}
