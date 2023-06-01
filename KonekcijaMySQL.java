package PozoristeAplikacija;

import java.sql.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KonekcijaMySQL extends Application{

    @Override
    public void start(Stage stage){
        
        try {
            //Uvoz.uveziIZvodjenjePredstaveKljucevi();
            Uvoz.uveziIzvodjenjePredstave();
            Uvoz.uveziKarte();
            Uvoz.uveziOsoblje();
            Uvoz.uveziOsobljePredstave();
            Uvoz.uveziPosjetiocaPozorista();
            Uvoz.uveziPozoriste();
            Uvoz.uveziPredstavu();
            Uvoz.uveziRadnikaPozorista();
            Parent root = FXMLLoader.load(getClass().getResource("JavaFX.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            //stage.centerOnScreen();
            stage.show();

            /* FXMLLoader loader = new FXMLLoader(getClass().getResource("JavaFX.fxml"));
            Parent root = loader.load();
            KonekcijaMySQL controller = loader.getController();
            stage.setScene(new Scene(root)); */
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Connection con = null;
    public static Connection Konekcija() {
        
        try {   
            
            Connection con=DriverManager.getConnection(  
            "jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");  

            /* //DODATNI KOD ZA PROVJERU:
            Statement stmt=con.createStatement();  
            ResultSet rs=stmt.executeQuery("select * from osoblje");  */
            return con;

/* PROVJERA DA LI KONEKCIJA ISPRAVNO RADI:
            while(rs.next())  {
                System.out.println(rs.getInt("id") + ", "
                                    +rs.getString("ime") + ", "
                                    +rs.getString("prezime") + ", "
                                    +rs.getInt("tip") + ", ");  
            } */
        }
            catch(SQLException e){ 
                e.printStackTrace();
            }
            return null;

    }
    
    public static void main(String[] args) {
        Konekcija();
        Application.launch(args);
    }
}
