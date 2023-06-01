package PozoristeAplikacija;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Uvoz {
    public static void uveziPozoriste () {
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

    public static void uveziPredstavu () {
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

    public static void uveziIzvodjenjePredstave () {
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

    public static void uveziIZvodjenjePredstaveKljucevi () {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            Statement stmt = conn.createStatement();
        
            String strSelect = "select * from izvodjenje_predstave";
            ResultSet rSet = stmt.executeQuery(strSelect);
            while(rSet.next()) {
                Pozoriste pozoriste = Pozoriste.pozoristePrekoID(rSet.getInt("pozoriste_id"));
                Predstava predstava = Predstava.predstavaPrekoID(rSet.getInt("predstava_id"));
                pozoriste.dodajPredstavu(predstava);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void uveziKarte() {
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

    public static void uveziPosjetiocaPozorista() {
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

    public static void uveziOsoblje() {
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

    public static void uveziOsobljePredstave() {
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

    public static void uveziRadnikaPozorista() {
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
}
