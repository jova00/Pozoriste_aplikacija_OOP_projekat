package PozoristeAplikacija;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.sql.Date;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class KontrolerFX {

    private Stage stage;
    private Scene scene;
    //private Parent root;


    //mysql poveznica
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    Pozoriste sviIzTabele = new Pozoriste();
    String radnikLOGINIME;


    //region funkcije

    public static String trenutniKorisnik = "";
    public static String trenutniRadnik = "";
    public static String trenutnoPozoriste = "";
    //public Pozoriste trenutnPozoriste;
    public int randomID = 1111;

    public static int datumPoredjenje(Date datum) {
        long vrijeme = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(vrijeme);

        if(datum.compareTo(date) < 0)
            return 0;
        else 
            return 1;
    }

    public static int slobodneKarte(Predstava predstava, Pozoriste pozoriste) {
        int predstavaID = predstava.getId();
        int pozoristeID = pozoriste.getId();
        int izvodjenjePredstaveID  = 1;
        int ukupnoMjesta = pozoriste.getBroj_sjedista();

        for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja){
            if(i.getPozoriste_id() == pozoristeID && i.getPredstava_id() == predstavaID ){
                izvodjenjePredstaveID = i.getId();
                break;
            }
        }
        
        int zauzetaMjesta = 0;
        for(Karta k : Karta.sveIzKarte){
            if(k.getIzvodjenje_predstave_id() == izvodjenjePredstaveID)
                zauzetaMjesta = zauzetaMjesta + 1;
        }
        int slobodnaMjesta = ukupnoMjesta - zauzetaMjesta;
        return slobodnaMjesta;
    }


    public static int brojKarataKorisnika(Pozoriste pozoriste, Predstava predstava, String korisnik){
        int korisnikID = PosjetilacPozorista.trenutniKorisnikPrekoID(korisnik);
        int izvodjenjePredstaveID = IzvodjenjePredstave.izvodjenjePPID(pozoriste, predstava);
        Uvoz.uveziKarte();
        int brojKarata = 0;
        for(Karta k : Karta.sveIzKarte){
            if(k.getIzvodjenje_predstave_id() == izvodjenjePredstaveID && k.getPosjetilac_pozorista_id() == korisnikID)
                return k.getBroj_karata();
        }
        return brojKarata;
    }

    public static int rezervacija48(Date d){
        try {
            String datum = d.toString();
            long vrijeme = System.currentTimeMillis();
            java.sql.Date trenutno = new java.sql.Date(vrijeme);
            
            LocalDate d1 = LocalDate.parse(datum.toString());
            LocalDate d2 = LocalDate.parse(trenutno.toString());

            long razlika = ChronoUnit.DAYS.between(d2, d1);
            
            return (int) razlika;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    

    //endregion

    //region pocetnaStana
    @FXML
    private Button posjetilacBtn;

    @FXML
    private Button radnikBtn;

    @FXML
    private Button registrujBtn;
    //endregion

    //region registracija

    @FXML
    private Button naPocetnuBtn;

    @FXML
    private Button regBtn;

    @FXML
    private TextField registrujIme;

    @FXML
    private TextField registrujKorisnicko;

    @FXML
    private PasswordField registrujLozinka;

    @FXML
    private TextField registrujPrezime;

    //endregion

    //region prijavaRadnika

    @FXML
    private TextField radnikLogIme;


    @FXML
    private PasswordField radnikLogLozinka;

    @FXML
    private Button prijaviRadnikBtn;
    //endregion

    //region prijavaPosjetioca

    @FXML
    private Button prijaviPosjetilacBtn;

    @FXML
    private TextField PosPrijavaIme;

    @FXML
    private PasswordField PosPrijavaLozinka;

    //endregion

    //region posjetilacPocetnaStrana

    @FXML
    private Button posjetilacPocetnaOdjavaBtn;
    
    @FXML
    private Button posjetilacPocetnaPozoristaBtn;

    @FXML
    private Button posjetilacPocetnaRezervacijaBtn;

    @FXML
    private Button posjetilacPocetnaGlumciBtn;

    @FXML
    private Label posjetilacPocetnaKorisnicko;
    //endregion

    //region posjetilacPozoristaStrana

    @FXML
    private TextField brojKarataTF;

    @FXML
    private TextField slobodnaTF;

    @FXML
    private Button terminiBTN;

    @FXML
    private ListView<IzvodjenjePredstave> terminiList;

    @FXML
    private ListView<Osoblje> posjetilacPozoristaListOsoblje; // provjeri

    @FXML
    private TextArea posjetilacPozoristeINFOTA;

    @FXML
    private Button PosjetilacOsobljeInfoDodatnoBtn;

    @FXML
    private ListView<Pozoriste> posjetilacPozoristaList;

    
    @FXML
    private ListView<Predstava> posjetilacPozoristaListPredstave;


    @FXML
    private Button nazadPosjetilacPocetna;

    @FXML
    private Button PosPredRezervisiBtn;

    @FXML
    private Button PosjetilacOsobljeInfoBtn;

    @FXML
    private Button PosjetilacPozoristeInfoBtn;

    @FXML
    private Button PosjetilacPredstavaInfoBtn;


    //endregion

    //region posjetilacRezervacijeStrana
    @FXML
    private Button PosRezOtkaziBtn;

    @FXML
    private ListView<Karta> posjetilacKarteBuduceLW;

    @FXML
    private ListView<Karta> posjetilacKarteProsleLW;

    @FXML
    private Button ucitajKarteBTN;

    //endregion

    //region posjetilacGlumciStrana
    @FXML
    private TextArea PosGlumciArea;

    @FXML
    private ListView<Osoblje> PosjetilacOsobljeList;

    @FXML
    private Button PosjetilacGlumciInformacija;

    @FXML
    private TextArea PosjetilacOsobljeInfoArea;

    @FXML
    private Button PosjetilacOsobljeUcitajBtn;

    //endregion

    //region radnikOstaloStrana

    //obrisi suvisne kad ih pronadjes
    @FXML
    private TextField radnikPozoristeCijenaTF;

    @FXML
    private Button radnikPozoristeDodajTerminBTN;

    @FXML
    private TextField radnikPozoristeTerminTF;

    @FXML
    private ChoiceBox<Predstava> radnikPozoristaCB;

    @FXML
    private TextField radnikRadniciTF;

    @FXML
    private Button radnikRadniciInfoBTN;

    @FXML
    private Button radnikRadniciPozoristeBTN;

    @FXML
    private TextArea radnikRadniciTA;

    @FXML
    private TextField noviRadnikIDTF;

    @FXML
    private TextField noviRadnikImeTF;

    @FXML
    private TextField noviRadnikKorisnickoF;

    @FXML
    private TextField noviRadnikLozinkaTF;

    @FXML
    private TextField noviRadnikPrezimeTF;

    @FXML
    private TextField promijeniLozinkuTF;

    @FXML
    private Button radnikDodajPozoristeBtn;

    @FXML
    private Button radnikDodajPredstavuBtn;

    @FXML
    private Button radnikDodajRadnikaBtn;

    @FXML
    private Button radnikDodatneInfoBtn;

    @FXML
    private Button radnikGlumciDetaljnoBtn;

    @FXML
    private TextArea radnikInfoRadnikTA;

    @FXML
    private Button radnikLozinkaBtn;

    @FXML
    private TextField radnikNovaPredstavaTF;

    @FXML
    private TextField radnikNovoPozoristeImeTF;

    @FXML
    private TextField radnikNovoPozoristeGradTF;

    @FXML
    private TextField radnikNovoPozoristeSjedisteTF;

    @FXML
    private TextField radnikNovaPredstavazanrTF;

    @FXML
    private Button radnikPocetnaInfoBtn;

    @FXML
    private Button radnikPredstavaInfoBtn;

    @FXML
    private Button radnikRezervacijeBtn;

    @FXML
    private ChoiceBox<String> radnikSpisakGlumacaBox;

    @FXML
    private Button radnikUcitajOdigrneBtn;

    @FXML
    private Button radnikUcitajPredstojeceBtn;

    @FXML
    private ChoiceBox<String> radnikPredstaveBox;

    @FXML
    private TextArea radnikArea;

    @FXML
    private TextField promijeniLozinkuStara;

    
    @FXML
    private TextField imeRadnikaTF;

    
    @FXML
    private ChoiceBox<String> naredneLista;

    
    @FXML
    private ChoiceBox<String> prethodneLista;

    @FXML
    private Button radnikNazadBTN;

    @FXML
    private Button radnikPozoristeInfoBTN;

    @FXML
    private ListView<Predstava> radnikPozoristeLV;

    @FXML
    private Button radnikPozoristeNaredneBTN;

    @FXML
    private Button radnikPozoristePrethodneBTN;

    @FXML
    private TextArea radnikPozoristeTA;



    //endregion

    //region radnikPocetnaStrana

    @FXML
    private Button radnikPocetnaPozoritaBTN;

    @FXML
    private Button radnikPocetnaRadniciBTN;

    @FXML
    private Button radnikPocetnaRezervacijieBTN;

    //endregion
 
    //region radnikKarte

    @FXML
    private TextField radnikSlobodneKarteTF;

    @FXML
    private TextField radnikKarteBrojKartiTF;

    @FXML
    private TextField radnikKarteKorisnickoTF;

    @FXML
    private ListView<Karta> radnikKarteLIJEVOLV;

    @FXML
    private Button radnikKarteOtkaziBTN;

    @FXML
    private Button radnikKartePredajBTN;

    @FXML
    private ChoiceBox<Predstava> radnikKartePredstaveBlagajnaCB;

    @FXML
    private ChoiceBox<Predstava> radnikKartePredstaveCB;

    @FXML
    private Button radnikKarteProdajBTN;

    @FXML
    private ChoiceBox<IzvodjenjePredstave> radnikKarteTerminBlagajnaCB;

    @FXML
    private ChoiceBox<IzvodjenjePredstave> radnikKarteTerminCB;

    @FXML
    private Button radnikUcitajKarteBTN;


    //endregion
 
 
    //region PocetnaStranaDugmad
    @FXML
    void posjetilacBtnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("posjetilacPristupNalogu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void radnikBtnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("radnikPristupNalogu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void registrujBtnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("registrujSe.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //endregion

    //region registracijaDugmad

    @FXML
    void pocetnaBtnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("JavaFX.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void regBtnClick(ActionEvent event) throws ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            pst = con.prepareStatement("insert into posjetilac_pozorista (ime, prezime, korisincko_ime, lozinka) values(?,?,?,?)");
            pst.setString(1, registrujIme.getText());
            pst.setString(2, registrujPrezime.getText());
            pst.setString(3, registrujKorisnicko.getText());
            pst.setString(4, registrujLozinka.getText());
            pst.execute();

            JOptionPane.showMessageDialog(null, "Корисник успјешно регистрован.");

            registrujIme.setText("");
            registrujPrezime.setText("");
            registrujKorisnicko.setText("");
            registrujLozinka.setText("");
            registrujIme.requestFocus();

    }
        
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    //endregion

    //region prijavaRadnikaDugmad

    @FXML
    void prijaviRadnikBtnClick(ActionEvent event) throws SQLException, IOException {
        String username = radnikLogIme.getText().trim();
        String password = radnikLogLozinka.getText();

        if(username.equals("") && password.equals(""))
            JOptionPane.showMessageDialog(null, "Молим Вас да унесете одговарајуће податке.");
        else{
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");

                pst = con.prepareStatement("select * from radnik_pozorista where korisnicko_ime=? and lozinka=?");

                pst.setString(1, username);
                pst.setString(2, password);

                rs = pst.executeQuery();

                if(rs.next()) {
                    Parent root = FXMLLoader.load(getClass().getResource("radnikPocetna.fxml"));
                    trenutniRadnik = username;
                    for(Pozoriste p : Pozoriste.sveIzPozorista){
                        for(RadnikPozorista r : RadnikPozorista.sveIzRadnik){
                            if(r.getKorisnicko_ime().equals(trenutniRadnik) && r.getPozoriste_id() == p.getId()){
                                trenutnoPozoriste = p.getNaziv();
                            }
                        }
                    }
                    //trenutnPozoriste = Pozoriste.pozoristePrekoID(rs.getInt("pozoriste_id"));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Корисничко име или лозинка нису исправни, покушајте поново.");

                    radnikLogIme.setText("");
                    radnikLogLozinka.setText("");
                    radnikLogIme.requestFocus();

                }

                radnikLOGINIME = radnikLogIme.getText();

                //posjetilacPocetnaKorisnicko.setText(PosPrijavaIme.getText());

            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    //endregion
    
    //region prijavaPosjetiocaDugmad

    @FXML
    void prijaviPosjetilacBtnClick(ActionEvent event) throws IOException, SQLException {
        String username = PosPrijavaIme.getText();
        String password = PosPrijavaLozinka.getText();

        if(username.equals("") && password.equals(""))
            JOptionPane.showMessageDialog(null, "Молим Вас да унесете одговарајуће податке.");
        else{
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");

                pst = con.prepareStatement("select * from posjetilac_pozorista where korisincko_ime=? and lozinka=?");

                pst.setString(1, username);
                pst.setString(2, password);

                rs = pst.executeQuery();

                if(rs.next()) {
                    Parent root = FXMLLoader.load(getClass().getResource("posjetilacPocetna.fxml"));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    trenutniKorisnik = PosPrijavaIme.getText().trim();
                    //usernametf.setText(trenutniKorisnik);
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Корисничко име или лозинка нису исправни, покушајте поново.");

                    PosPrijavaIme.setText("");
                    PosPrijavaLozinka.setText("");
                    PosPrijavaIme.requestFocus();

                }

                //posjetilacPocetnaKorisnicko.setText(PosPrijavaIme.getText());

            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    //endregion
   
    //region posjetilacPocetnaDugmad
    
    @FXML
    void posjetilacPocetnaOdjavaBtnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("JavaFX.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void posjetilacPocetnaPozoristaBtnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pozorista_predstave.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void posjetilacPocetnaRezervacijeBtnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("posjetilacRezervacije.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void posjetilacPocetnaGlumciBtnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("posjetilacGlumci.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    //endregion

    //region posjetilacPozoristaDugmad

    @FXML
    private Button probabtn;

    @FXML
    void probaklik(ActionEvent event) {
        ObservableList<Pozoriste> pozoristaItems = FXCollections.observableArrayList();
        for(Pozoriste p: Pozoriste.getSveIzPozorista()) {
            pozoristaItems.add(p);
        }
        posjetilacPozoristaList.setItems(pozoristaItems);
    }


    @FXML
    void PosjetilacOsobljeInfoBtnClicked(ActionEvent event) {
        Predstava izabranaPredstava = posjetilacPozoristaListPredstave.getSelectionModel().getSelectedItem();
        ArrayList<Osoblje> listaOsoblja = new ArrayList<>();
        int trenutniIDpredstave = izabranaPredstava.getId();
        posjetilacPozoristaListOsoblje.getItems().clear();

        if(posjetilacPozoristaListPredstave.getSelectionModel().getSelectedItem() == null){
            Alert alert1 = new Alert(Alert.AlertType.WARNING);
            alert1.setContentText("Прво изаберите неко од понуђених представа!");
            alert1.show();
        }
        else{
            for(OsobljePredstave o : OsobljePredstave.sveIzOsobljePredstave){
                if(o.getPredstava_id() == trenutniIDpredstave)
                    listaOsoblja.add(Osoblje.osobljePrekoID(o.getOsoblje_id()));
            }
            posjetilacPozoristaListOsoblje.getItems().addAll(listaOsoblja);
        }


    }

    @FXML
    void PosjetilacPozoristeInfoBtnClicked(ActionEvent event) {
        int trenutniID;
        posjetilacPozoristaListPredstave.getItems().clear();
        posjetilacPozoristaListOsoblje.getItems().clear();
        terminiList.getItems().clear();
        posjetilacPozoristeINFOTA.clear();


        ArrayList<Predstava> listaPredstave = new ArrayList<>();
        if(posjetilacPozoristaList.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Прво изаберите неко од понуђених позоришта!");
            alert.show();
        }
        else{
            Pozoriste izabranoPozoriste = posjetilacPozoristaList.getSelectionModel().getSelectedItem();
            trenutniID = izabranoPozoriste.getId();
            for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja){
                if(i.getPozoriste_id() == trenutniID)
                    listaPredstave.add(Predstava.predstavaPrekoID(i.getPredstava_id()));
            }
            posjetilacPozoristaListPredstave.getItems().addAll(listaPredstave);
        }

    }

    @FXML
    void terminiBTNclick(ActionEvent event) {
        int pozoristeID;
        int predstavaID;
        terminiList.getItems().clear();
        //ObservableList <IzvodjenjePredstave> listaTermina = FXCollections.observableArrayList();
        ArrayList<IzvodjenjePredstave> listaTermina = new ArrayList<>();
        if(posjetilacPozoristaList.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Прво изаберите неко од понуђених позоришта!");
            alert.show();
            if(posjetilacPozoristaListPredstave.getSelectionModel().getSelectedItem() == null){
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setContentText("Прво изаберите неко од понуђених представа!");
                alert1.show();
            }
        }
        else{
            Pozoriste izabranoPozoriste = posjetilacPozoristaList.getSelectionModel().getSelectedItem();
            pozoristeID = izabranoPozoriste.getId();
            Predstava izabranaPredstava = posjetilacPozoristaListPredstave.getSelectionModel().getSelectedItem();
            predstavaID = izabranaPredstava.getId();
            for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja) {
                if(i.getPozoriste_id() == pozoristeID && i.getPredstava_id() == predstavaID)
                    listaTermina.add(IzvodjenjePredstave.izvodjenjePrekoID(i.getPredstava_id()));
            }
            terminiList.getItems().addAll(listaTermina);

            //za mjesta
            int brojMjesta = slobodneKarte(izabranaPredstava, izabranoPozoriste);
            slobodnaTF.setText(Integer.toString(brojMjesta));
        }
        

    }

    @FXML
    void PosjetilacPredstavaInfoBtnClicked(ActionEvent event) {
        posjetilacPozoristeINFOTA.clear();
        Predstava izabranaPredstava = posjetilacPozoristaListPredstave.getSelectionModel().getSelectedItem();
        for(Predstava p : Predstava.sveIzPredstave){
            if(p.getNaziv().equals(izabranaPredstava.getNaziv()))
                posjetilacPozoristeINFOTA.setText(Predstava.sveInformacijePredstava(izabranaPredstava));
        }
    }


    @FXML
    void nazadPosjetilacPocetna(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("posjetilacPocetna.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void PoPredRezervisiBtnClcked(ActionEvent event) {

        if(terminiList.getItems() == null && brojKarataTF.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Изеберите термин извођења и жељени број карата.");
        }
        else if(slobodneKarte(posjetilacPozoristaListPredstave.getSelectionModel().getSelectedItem(),posjetilacPozoristaList.getSelectionModel().getSelectedItem())<Integer.parseInt(slobodnaTF.getText())) {
            JOptionPane.showMessageDialog(null, "За одабрани термин тренутно нема слободних мјеста.");
        }
        else if(brojKarataTF.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Унесите жељени број карата.");
        }
        else if(terminiList.getItems() == null){
            JOptionPane.showMessageDialog(null, "Изаберите датум и вријеме извођења.");
        }
        else if(Integer.parseInt(brojKarataTF.getText()) <= 0){
            JOptionPane.showMessageDialog(null, "Унесите валидан број карата.");
        }

        else{
            int trenutniID = 1;
            Uvoz.uveziPosjetiocaPozorista();
            for(PosjetilacPozorista p : PosjetilacPozorista.sveIzPosjetilac){
                if(p.getKorisnicko_ime().equals(trenutniKorisnik)){
                    trenutniID = p.getId();
                    //break;
                }
            }
            int izvodjenjePredstaveID = IzvodjenjePredstave.izvodjenjePPID(posjetilacPozoristaList.getSelectionModel().getSelectedItem(), posjetilacPozoristaListPredstave.getSelectionModel().getSelectedItem());
            IzvodjenjePredstave izvodjenje = IzvodjenjePredstave.izvodjenjePrekoID(izvodjenjePredstaveID);
            Date datumIzvodjenja = izvodjenje.getDatum_i_vrijeme();
            if(rezervacija48(datumIzvodjenja) < 0){
                JOptionPane.showMessageDialog(null,"Представа се већ одиграла, молимо да сачекате нови термин извођења.");
                brojKarataTF.clear();
            }
            else{
                JOptionPane.showMessageDialog(null, "Резервација успјешна!");
                String sadrzaj = "Позориште: " + posjetilacPozoristaList.getSelectionModel().getSelectedItem().getNaziv() +
                                    "\n Представа: " +posjetilacPozoristaListPredstave.getSelectionModel().getSelectedItem().getNaziv() +
                                    "\n Термин: " + terminiList.getSelectionModel().getSelectedItem() +
                                    "\n Број карата: " + brojKarataTF.getText() ;
                                  // + "\n Број преосталих слободних сједишта: " + slobodneKarte(posjetilacPozoristaListPredstave.getSelectionModel().getSelectedItem(), posjetilacPozoristaList.getSelectionModel().getSelectedItem());

                JOptionPane.showMessageDialog(null, sadrzaj);
                for(int i = 0; i < Integer.parseInt(brojKarataTF.getText());i++){
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
                pst = con.prepareStatement("insert into karta(izvodjenje_predstave_id, status, posjetilac_id, broj_karta) values (?, ?, ?, ?)");
                pst.setInt(1, terminiList.getSelectionModel().getSelectedItem().getId());
                pst.setInt(2, 2);
                pst.setInt(3, trenutniID);
                pst.setInt(4, Karta.sledeciSlobodanBroj(terminiList.getSelectionModel().getSelectedItem().getId(), posjetilacPozoristaList.getSelectionModel().getSelectedItem().getBroj_sjedista()));
                pst.execute();
                Uvoz.uveziKarte();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                brojKarataTF.clear();
                posjetilacPozoristaList.getItems().clear();
                posjetilacPozoristaListOsoblje.getItems().clear();
                posjetilacPozoristaListPredstave.getItems().clear();
                terminiList.getItems().clear();
                posjetilacPozoristeINFOTA.clear();
            }
        }
    }    



    @FXML
    void PosjetilacOsobljeInfoDodatnoBtnClicked(ActionEvent event) {
        posjetilacPozoristeINFOTA.clear();
        Osoblje izabranoOsoblje = posjetilacPozoristaListOsoblje.getSelectionModel().getSelectedItem();
        for(Osoblje o : Osoblje.sveIzOsoblje){
            if(o.getId() == izabranoOsoblje.getId())
            posjetilacPozoristeINFOTA.setText(Osoblje.sveInformacijeOsoblje(izabranoOsoblje));
        }
    }
    //napisati za choicebox kod!!!

    //endregion

    //region PosjetilaRezervacijeDugmad

    @FXML
    void PosRezOtkaziBtnClicked(ActionEvent event) {
        if(posjetilacKarteBuduceLW.getSelectionModel().getSelectedItem() == null)
            JOptionPane.showMessageDialog(null, "Изаберите карту коју желите отказати.");
        else{
                Karta izabrane = posjetilacKarteBuduceLW.getSelectionModel().getSelectedItem();
                for(Karta k : Karta.sveIzKarte){
                    if(k.getId() == izabrane.getId()){
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
    
                            pst = con.prepareStatement("delete from karta where id = ?");
                            pst.setInt(1, k.getId());
    
                            pst.executeUpdate();
    
                            Karta.sveIzKarte.remove(k);
    
                            JOptionPane.showMessageDialog(null, "Успјешно отказивање!");
                            posjetilacKarteBuduceLW.getItems().clear();
                            posjetilacKarteProsleLW.getItems().clear();
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    }
                    
                }
        }
    }

    @FXML
    void ucitajKarteClicked(ActionEvent event) {
        posjetilacKarteBuduceLW.getItems().clear();
        posjetilacKarteProsleLW.getItems().clear();
        ArrayList<Karta> predstojece = new ArrayList<>();
        ArrayList<Karta> prethodne = new ArrayList<>();

        PosjetilacPozorista posjetilac = PosjetilacPozorista.trenutniKorisnikPrekoString(trenutniKorisnik);
        for(Karta k : Karta.sveIzKarte){
            if(k.getPosjetilac_pozorista_id() == posjetilac.getId() && k.getStatus() != 1) {
                IzvodjenjePredstave izvodjenje = IzvodjenjePredstave.izvodjenjePrekoID(k.getIzvodjenje_predstave_id());
                Date datumIzvodjenja = izvodjenje.getDatum_i_vrijeme();
                if(rezervacija48(datumIzvodjenja) > 0)
                    predstojece.add(k);
                else
                    prethodne.add(k);
            }
        }
        posjetilacKarteBuduceLW.getItems().addAll(predstojece);
        posjetilacKarteProsleLW.getItems().addAll(prethodne);
    }

    //endregion

    //region PosjetilacGlumciDugmad

    /* @FXML
    void PosGlumciBoxClicked(MouseEvent event) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            ResultSet rs = con.createStatement().executeQuery("select ime, prezime from osoblje");
            ObservableList<String> data = FXCollections.observableArrayList();
            while(rs.next()) {
                data.add(new String(rs.getString(1)));
            }
            PosGlumciBox.setItems(data);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    } */

    @FXML
    void PosjetilacOSobljeUcitajKlik(ActionEvent event) {
        ObservableList<Osoblje> osobljeItems = FXCollections.observableArrayList();
        for(Osoblje o : Osoblje.getSveIzOsoblje()){
            osobljeItems.add(o);
        }
        PosjetilacOsobljeList.setItems(osobljeItems);
    }

    @FXML
    void PosjetilacGlumciInformacijaClicked(ActionEvent event) {
        Osoblje izabranoOsoblje = PosjetilacOsobljeList.getSelectionModel().getSelectedItem();
        PosjetilacOsobljeInfoArea.setText(Osoblje.sveInformacijeOsoblje(izabranoOsoblje));
    }

    //endregion

    //region radnikPocetnaDugmad


    @FXML
    void radnikNazadBTNClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("radnikPocetna.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //endregion

    //region radnikPozorista

    //lijeva strana

    @FXML
    void radnikPozoristeInfoBTNClicked(ActionEvent event) {
        Predstava izabranaPredstava = radnikPozoristeLV.getSelectionModel().getSelectedItem();
        for(Predstava p : Predstava.sveIzPredstave) {
            if(p.getNaziv().equals(izabranaPredstava.getNaziv()))
                radnikPozoristeTA.setText(Predstava.sveInformacijePredstava(p));
        }
    }

    @FXML
    void radnikPozoristeNaredneBTNClicked(ActionEvent event) {
        radnikPozoristeLV.getItems().clear();
        ObservableList<Predstava> predstavaItems = FXCollections.observableArrayList();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for(Pozoriste poz : Pozoriste.sveIzPozorista){
            if(poz.getNaziv().equals(trenutnoPozoriste)){
                for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja){
                    if(i.getDatum_i_vrijeme().after(timestamp)){
                        for(Predstava p: Predstava.sveIzPredstave) {
                            if(IzvodjenjePredstave.izvodjenjePPID(poz, p) == i.getId()){
                                predstavaItems.add(p);
                            }
                        }
                    }
                }
            }
        }
        radnikPozoristeLV.setItems(predstavaItems);
        radnikPozoristeTA.clear();
    }

    @FXML
    void radnikPozoristePrethodneBTNClicked(ActionEvent event) {
        radnikPozoristeLV.getItems().clear();
        ObservableList<Predstava> predstavaItems = FXCollections.observableArrayList();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for(Pozoriste poz : Pozoriste.sveIzPozorista){
            if(poz.getNaziv().equals(trenutnoPozoriste)){
                for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja){
                    if(i.getDatum_i_vrijeme().before(timestamp)){
                        for(Predstava p: Predstava.sveIzPredstave) {
                            if(IzvodjenjePredstave.izvodjenjePPID(poz, p) == i.getId()){
                                predstavaItems.add(p);
                            }
                        }
                    }
                }
            }
        }
        radnikPozoristeLV.setItems(predstavaItems);
        radnikPozoristeTA.clear();
    }

    //desna strana

    @FXML
    void radnikPozoristeDodajTerminBTNClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        int cijena = Integer.parseInt(radnikPozoristeCijenaTF.getText());
        String termin = radnikPozoristeTerminTF.getText();
        Predstava izabranaPredstava = radnikPozoristaCB.getSelectionModel().getSelectedItem();

        for(Pozoriste p : Pozoriste.sveIzPozorista){
           
            if(p.getNaziv().equals(trenutnoPozoriste)){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                java.util.Date parsed;
                Timestamp ts;
                try {
                    parsed = df.parse(termin);
                    ts = new Timestamp(parsed.getTime());
                    //Timestamp trenutno = new Timestamp(System.currentTimeMillis());
                    if(cijena <= 0){
                        JOptionPane.showMessageDialog(null, "Унесите валидну цијену.");
                        radnikPozoristeCijenaTF.clear();
                        radnikPozoristeCijenaTF.requestFocus();
                    }
                    else if(termin.equals("")){
                        JOptionPane.showMessageDialog(null, "Унесите термин.");
                        radnikPozoristeTerminTF.requestFocus();
                    }
                    /* else if(ts.before(trenutno)){
                        JOptionPane.showMessageDialog(null, "Унесите датум који није прошао.");
                        radnikPozoristeTerminTF.clear();
                        radnikPozoristeTerminTF.requestFocus();
                    } */
                    else{
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
                        pst = con.prepareStatement("insert into izvodjenje_predstave(predstava_id, pozoriste_id, cijena, datum_i_vrijeme) values (?, ?, ?, ?)");
                        pst.setInt(1, izabranaPredstava.getId());
                        pst.setInt(2, p.getId());
                        pst.setInt(3, cijena);
                        pst.setTimestamp(4, ts);
                        pst.execute();
                
                        JOptionPane.showMessageDialog(null, "Термин успјешно додан.");
                
                        radnikPozoristeCijenaTF.setText("");
                        radnikPozoristeTerminTF.setText("");
                        radnikPozoristaCB.getItems().clear();
                    }
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null,"Унесите термин у формату : *ГГГГ-ММ-ДД СС:ММ:СС*");
                    radnikPozoristeTerminTF.clear();
                    e.printStackTrace();
                }
                
            }
                
        }

    }

    @FXML
    void radnikPozoristeCBClicked(MouseEvent event) {
        radnikPozoristaCB.getItems().clear();
            ObservableList<Predstava> listaPredstave = FXCollections.observableArrayList();
            for(Pozoriste p : Pozoriste.sveIzPozorista){
                if(p.getNaziv().equals(trenutnoPozoriste)){
                    for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja){
                        if(i.getPozoriste_id() == p.getId())
                            listaPredstave.add(Predstava.predstavaPrekoID(i.getPredstava_id()));
                    }
                }
            }
            radnikPozoristaCB.setItems(listaPredstave);    
    }

    @FXML
    void radnikDodajPozoristeBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
        pst = con.prepareStatement("insert into pozoriste(naziv, grad, broj_sjedista) values(?, ?, ?)");
        pst.setString(1, radnikNovoPozoristeImeTF.getText());
        pst.setString(2, radnikNovoPozoristeGradTF.getText());
        pst.setInt(3, Integer.parseInt(radnikNovoPozoristeSjedisteTF.getText()));
        pst.execute();

        JOptionPane.showMessageDialog(null, "Позориште успјешно додано.");

        radnikNovoPozoristeImeTF.setText("");
        radnikNovoPozoristeGradTF.setText("");
        radnikNovoPozoristeSjedisteTF.setText("");
        radnikNovoPozoristeImeTF.requestFocus();    
    }

    @FXML
    void radnikDodajPredstavuBtnClicked(ActionEvent event) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
        pst = con.prepareStatement("insert into predstava(naziv, zanr) values(?, ?)");
        pst.setString(1, radnikNovaPredstavaTF.getText());
        pst.setInt(2, Integer.parseInt(radnikNovaPredstavazanrTF.getText()));
        pst.execute();

        JOptionPane.showMessageDialog(null, "Представа успјешно додана.");

        radnikNovaPredstavaTF.setText("");
        radnikNovaPredstavazanrTF.setText("");
        radnikNovaPredstavaTF.requestFocus(); 
    }

    //endregion

    //region radnikRadnik

    //lijeva strana

    @FXML
    void radnikRadniciInfoBTNClicked(ActionEvent event) {
        Uvoz.uveziRadnikaPozorista();
        for(RadnikPozorista p : RadnikPozorista.sveIzRadnik){
            if(p.getKorisnicko_ime().equals(trenutniRadnik))
                radnikRadniciTA.setText(RadnikPozorista.sveInformacijeRadnik(p));
        }
    }

    @FXML
    void radnikRadniciPozoristeBTNClicked(ActionEvent event) {
        for(RadnikPozorista r : RadnikPozorista.sveIzRadnik){
            if(r.getKorisnicko_ime().equals(trenutniRadnik)){
                for(Pozoriste p : Pozoriste.sveIzPozorista){
                    if(r.getPozoriste_id() == p.getId())
                        radnikRadniciTA.setText(Pozoriste.sveInformacijePozoriste(p));
                }
            }
        }

    }

    @FXML
    void radnikLozinkaBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            pst = con.prepareStatement("update radnik_pozorista set lozinka = ? where lozinka = ?");
            pst.setString(1, promijeniLozinkuTF.getText());
            pst.setString(2, promijeniLozinkuStara.getText());
            pst.execute();

            JOptionPane.showMessageDialog(null, "Лозинка успјешно промијењена.");

            promijeniLozinkuStara.setText("");
            promijeniLozinkuTF.setText("");
            promijeniLozinkuStara.requestFocus();
            radnikRadniciTA.clear();
            Uvoz.uveziRadnikaPozorista();
    }

    //desna strana

    @FXML
    void radnikDodajRadnikaBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            pst = con.prepareStatement("insert into radnik_pozorista (ime, prezime, korisnicko_ime, lozinka, pozoriste_id) values(?,?,?,?,?)");
            pst.setString(1, noviRadnikImeTF.getText());
            pst.setString(2, noviRadnikPrezimeTF.getText());
            pst.setString(3, noviRadnikKorisnickoF.getText());
            pst.setString(4, noviRadnikLozinkaTF.getText());
            pst.setString(5, noviRadnikIDTF.getText());
            pst.execute();

            JOptionPane.showMessageDialog(null, "Радник успјешно регистрован.");

            noviRadnikImeTF.setText("");
            noviRadnikPrezimeTF.setText("");
            noviRadnikKorisnickoF.setText("");
            noviRadnikLozinkaTF.setText("");
            noviRadnikIDTF.setText("");
            noviRadnikImeTF.requestFocus();
    }

    @FXML
    void radnikDodatneInfoBtnClicked(ActionEvent event) {

    }

    @FXML
    void radnikGlumciDetaljnoBtnClicked(ActionEvent event) {
        String trenuto = radnikSpisakGlumacaBox.getValue();
        radnikArea.clear();
        radnikArea.setText("Изабрано особље је : "+ trenuto + "\t"); 
    }

    

    @FXML
    void radnikPocetnaInfoBtnClick(ActionEvent event) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            pst = con.prepareStatement("select ime from radnik_pozorista where ime = ?");
            pst.setString(1, imeRadnikaTF.getText());
            pst.execute();
            radnikInfoRadnikTA.appendText("Име радника : " + pst + "\t");
    }

    @FXML
    void radnikPredstavaInfoBtnClicked(ActionEvent event) {
        String trenutoPozoriste = radnikPredstaveBox.getValue();
        radnikArea.clear();
        radnikArea.setText("Изабрано позориште је : "+ trenutoPozoriste + "\t"); 
    }

    @FXML
    void radnikRezervacijeBtnClicked(ActionEvent event) {

    }

    @FXML
    void radnikUcitajOdigrneBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            ResultSet rs = con.createStatement().executeQuery("SELECT naziv from predstava a where exists (select 1 from izvodjenje_predstave b where b.datum_i_vrijeme < CURRENT_TIMESTAMP and a.id = b.pozoriste_id);");
            ObservableList<String> data = FXCollections.observableArrayList();
            while(rs.next()) {
                data.add(new String(rs.getString(1)));
            }
            prethodneLista.setItems(data);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void radnikUcitajPredstojeceBtnClicked(ActionEvent event) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            ResultSet rs = con.createStatement().executeQuery("SELECT naziv from predstava a where exists (select 1 from izvodjenje_predstave b where b.datum_i_vrijeme > CURRENT_TIMESTAMP and a.id = b.pozoriste_id);");
            ObservableList<String> data = FXCollections.observableArrayList();
            while(rs.next()) {
                data.add(new String(rs.getString(1)));
            }
            naredneLista.setItems(data);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void radnikPredstaveBoxClicked(MouseEvent event) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            ResultSet rs = con.createStatement().executeQuery("select naziv from predstava");
            ObservableList<String> data = FXCollections.observableArrayList();
            while(rs.next()) {
                data.add(new String(rs.getString(1)));
            }
            radnikPredstaveBox.setItems(data);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void radnikSpisakGlumacaBoxClicked(MouseEvent event) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            ResultSet rs = con.createStatement().executeQuery("select ime, prezime from osoblje");
            ObservableList<String> data = FXCollections.observableArrayList();
            while(rs.next()) {
                data.add(new String(rs.getString(1)));
            }
            radnikSpisakGlumacaBox.setItems(data);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //endregion

    //region radnikKarte

    @FXML
    void radnikKarteOtkaziBTNClicked(ActionEvent event) {
        Karta izabrane = radnikKarteLIJEVOLV.getSelectionModel().getSelectedItem();
        if(izabrane == null)
            JOptionPane.showMessageDialog(null, "Изаберите карту коју желите отказати.");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja){
            if(i.getId() == izabrane.getIzvodjenje_predstave_id()){
                if(i.getDatum_i_vrijeme().before(timestamp))
                    JOptionPane.showMessageDialog(null, "Представе је завршена, не можете предати карте.");
                else{
                
                    for(Karta k : Karta.sveIzKarte){
                        if(k.getId() == izabrane.getId()){
                            try {
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
    
                                pst = con.prepareStatement("delete from karta where id = ?");
                                pst.setInt(1, k.getId());
    
                                pst.executeUpdate();
    
                                Karta.sveIzKarte.remove(k);
    
                                JOptionPane.showMessageDialog(null, "Успјешно отказивање!");
                                radnikKarteLIJEVOLV.getItems().clear();
                            } catch (Exception e) {
                                //e.printStackTrace();
                            }
                        }
                    
                    }
                }
            }
        }
        
    }

    @FXML
    void radnikKartePredajBTNClicked(ActionEvent event) {
        Karta izabranaKarta = radnikKarteLIJEVOLV.getSelectionModel().getSelectedItem();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja){
            if(izabranaKarta == null){
                JOptionPane.showMessageDialog(null, "Изаберите прво карту.");
            }
            if(i.getId() == izabranaKarta.getIzvodjenje_predstave_id()){
                if(i.getDatum_i_vrijeme().before(timestamp))
                    JOptionPane.showMessageDialog(null, "Представе је завршена, не можете предати карте.");
                else if(izabranaKarta.getStatus() == 3)
                    JOptionPane.showMessageDialog(null, "Изабрана карта је већ предана.");
                else if(izabranaKarta.getStatus() == 1)
                    JOptionPane.showMessageDialog(null, "Изабрана карта је купљена и предана на благајни.");
                else{
                    for(Karta k : Karta.sveIzKarte){
                        if(k.getId() == izabranaKarta.getId()){
                            try {
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
            
                                pst = con.prepareStatement("update karta set status = ? where id = ?");
                                pst.setInt(1, 3);
                                pst.setInt(2, k.getId());
            
                                pst.executeUpdate();
                                k.setStatus(3);
                                Uvoz.uveziKarte();
        
                                JOptionPane.showMessageDialog(null, "Карта успјешно предана.");
                                radnikKarteLIJEVOLV.getItems().clear();
                            } catch (Exception e) {
                                //e.printStackTrace();
                            }
                        }
                    }
                }
                
            }
        }
        
    }

    @FXML
    void radnikKartePredstaveBlagajnaCBClicked(MouseEvent event) {
        radnikKartePredstaveBlagajnaCB.getItems().clear();
            ObservableList<Predstava> listaPredstave = FXCollections.observableArrayList();
            for(Pozoriste p : Pozoriste.sveIzPozorista){
                if(p.getNaziv().equals(trenutnoPozoriste)){
                    for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja){
                        if(i.getPozoriste_id() == p.getId())
                            listaPredstave.add(Predstava.predstavaPrekoID(i.getPredstava_id()));
                    }
                }
            }
            radnikKartePredstaveBlagajnaCB.setItems(listaPredstave);    
            radnikKarteTerminBlagajnaCB.getItems().clear();
    }

    @FXML
    void radnikKartePredstaveCBClicked(MouseEvent event) {
            radnikKartePredstaveCB.getItems().clear();
            ObservableList<Predstava> listaPredstave = FXCollections.observableArrayList();
            for(Pozoriste p : Pozoriste.sveIzPozorista){
                if(p.getNaziv().equals(trenutnoPozoriste)){
                    for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja){
                        if(i.getPozoriste_id() == p.getId())
                            listaPredstave.add(Predstava.predstavaPrekoID(i.getPredstava_id()));
                    }
                }
            }
            radnikKartePredstaveCB.setItems(listaPredstave);    
            radnikKarteTerminCB.getItems().clear();
    }

    @FXML
    void radnikKarteProdajBTNClicked(ActionEvent event) {
        if(radnikKarteTerminBlagajnaCB.getItems() == null && radnikSlobodneKarteTF.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Изеберите термин извођења и жељени број карата.");
        }
        for(Pozoriste pozoriste : Pozoriste.sveIzPozorista){
            if(pozoriste.getNaziv().equals(trenutnoPozoriste)){
                if(slobodneKarte(radnikKartePredstaveBlagajnaCB.getSelectionModel().getSelectedItem(),pozoriste)<Integer.parseInt(radnikSlobodneKarteTF.getText())) {
                    JOptionPane.showMessageDialog(null, "За одабрани термин тренутно нема слободних мјеста.");
                }
                else if(radnikSlobodneKarteTF.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Унесите жељени број карата.");
                }
                else if(radnikKarteTerminBlagajnaCB.getItems() == null){
                    JOptionPane.showMessageDialog(null, "Изаберите датум и вријеме извођења.");
                }
                else if(Integer.parseInt(radnikSlobodneKarteTF.getText()) <= 0){
                    JOptionPane.showMessageDialog(null, "Унесите валидан број карата.");
                }
        
                else{
                    int trenutniID = 1;
                    Uvoz.uveziPosjetiocaPozorista();
                    for(PosjetilacPozorista p : PosjetilacPozorista.sveIzPosjetilac){
                        if(p.getKorisnicko_ime().equals(radnikKarteKorisnickoTF.getText())){
                            trenutniID = p.getId();
                            //break;
                        }
                    }
                    int izvodjenjePredstaveID = IzvodjenjePredstave.izvodjenjePPID(pozoriste, radnikKartePredstaveBlagajnaCB.getSelectionModel().getSelectedItem());
                    IzvodjenjePredstave izvodjenje = IzvodjenjePredstave.izvodjenjePrekoID(izvodjenjePredstaveID);
                    Date datumIzvodjenja = izvodjenje.getDatum_i_vrijeme();
                    if(rezervacija48(datumIzvodjenja) < 0){
                        JOptionPane.showMessageDialog(null,"Представа се већ одиграла, молимо да сачекате нови термин извођења.");
                        
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Резервација успјешна!");
                        String sadrzaj = "Позориште: " + pozoriste.getNaziv() +
                                            "\n Представа: " + radnikKartePredstaveBlagajnaCB.getSelectionModel().getSelectedItem().getNaziv() +
                                            "\n Термин: " + radnikKarteTerminBlagajnaCB.getSelectionModel().getSelectedItem() +
                                            "\n Број карата: " + radnikKarteBrojKartiTF.getText() ;
                                          // + "\n Број преосталих слободних сједишта: " + slobodneKarte(posjetilacPozoristaListPredstave.getSelectionModel().getSelectedItem(), posjetilacPozoristaList.getSelectionModel().getSelectedItem());
        
                        JOptionPane.showMessageDialog(null, sadrzaj);
                        for(int i = 0; i < Integer.parseInt(radnikKarteBrojKartiTF.getText());i++){
                            try {
                                Class.forName("com.mysql.cj.jdbc.Driver");
                                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/seminarski_oop_sj","root","admin");
                                pst = con.prepareStatement("insert into karta(izvodjenje_predstave_id, status, posjetilac_id, broj_karta) values (?, ?, ?, ?)");
                                pst.setInt(1, radnikKarteTerminBlagajnaCB.getSelectionModel().getSelectedItem().getId());
                                pst.setInt(2, 1);
                                pst.setInt(3, trenutniID);
                                pst.setInt(4, Karta.sledeciSlobodanBroj(radnikKarteTerminBlagajnaCB.getSelectionModel().getSelectedItem().getId(), pozoriste.getBroj_sjedista()));
                                pst.execute();
                                Uvoz.uveziKarte();
        
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        radnikKarteKorisnickoTF.clear();
                        radnikKartePredstaveBlagajnaCB.getItems().clear();
                        radnikKarteTerminBlagajnaCB.getItems().clear();
                        radnikKarteBrojKartiTF.clear();
                        radnikSlobodneKarteTF.clear();
                    }
                }
            }
        }
        
    }

    @FXML
    void radnikKarteTerminBlagajnaCBClicked(MouseEvent event) {
        int izvodjenjePredstaveID ;
        IzvodjenjePredstave izvodjenje;
        Date datumIzvodjenja;
        radnikKarteTerminBlagajnaCB.getItems().clear();
        ObservableList<IzvodjenjePredstave> izvodjenjeItems = FXCollections.observableArrayList();

        for(Pozoriste poz : Pozoriste.sveIzPozorista){
            if(poz.getNaziv().equals(trenutnoPozoriste)){
                izvodjenjePredstaveID = IzvodjenjePredstave.izvodjenjePPID(poz, radnikKartePredstaveBlagajnaCB.getSelectionModel().getSelectedItem());
                izvodjenje = IzvodjenjePredstave.izvodjenjePrekoID(izvodjenjePredstaveID);
                datumIzvodjenja = izvodjenje.getDatum_i_vrijeme();
                if(rezervacija48(datumIzvodjenja) > 0){
                    Predstava izabranaPredstava = radnikKartePredstaveBlagajnaCB.getSelectionModel().getSelectedItem();
                    int predstavaID = izabranaPredstava.getId();
                    for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja) {
                        if(i.getPozoriste_id() == poz.getId() && i.getPredstava_id() == predstavaID){
                            izvodjenjeItems.add(IzvodjenjePredstave.izvodjenjePrekoID(i.getPredstava_id()));
                            int brojMjesta = slobodneKarte(izabranaPredstava, poz);
                            radnikSlobodneKarteTF.setText(Integer.toString(brojMjesta));
                        }
                            
                    }
                }
            }
            
        }
        radnikKarteTerminBlagajnaCB.setItems(izvodjenjeItems);
    }

    @FXML
    void radnikKarteTerminCBClicked(MouseEvent event) {
        radnikKarteTerminCB.getItems().clear();
        ObservableList<IzvodjenjePredstave> izvodjenjeItems = FXCollections.observableArrayList();
        for(Pozoriste poz : Pozoriste.sveIzPozorista){
            if(poz.getNaziv().equals(trenutnoPozoriste)){
                Predstava izabranaPredstava = radnikKartePredstaveCB.getSelectionModel().getSelectedItem();
                int predstavaID = izabranaPredstava.getId();
                for(IzvodjenjePredstave i : IzvodjenjePredstave.sveIzIzvodjenja) {
                    if(i.getPozoriste_id() == poz.getId() && i.getPredstava_id() == predstavaID)
                        izvodjenjeItems.add(IzvodjenjePredstave.izvodjenjePrekoID(i.getPredstava_id()));
                }
            }
        }
        radnikKarteTerminCB.setItems(izvodjenjeItems);
    }

    @FXML
    void radnikUcitajKarteClicked(ActionEvent event) {
        radnikKarteLIJEVOLV.getItems().clear();
        Predstava izabranaPredstava = radnikKartePredstaveCB.getSelectionModel().getSelectedItem();
        IzvodjenjePredstave izabraniTermin = radnikKarteTerminCB.getSelectionModel().getSelectedItem();
        ObservableList<Karta> kartaItems = FXCollections.observableArrayList();

        if(izabranaPredstava == null && izabraniTermin == null){
            JOptionPane.showMessageDialog(null, "Одаберите представу и термин извођења.");
        }
        else{
            for(Karta k : Karta.sveIzKarte){
                if(k.getIzvodjenje_predstave_id() == izabraniTermin.getId())
                    kartaItems.add(k);
            }
        }
        radnikKarteLIJEVOLV.setItems(kartaItems);

    }
    
    //endregion

    //region radnikPocetnaStranaDugmad

    @FXML
    void radnikPocetnaPozoritaBTNClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("radnikPozoriste.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void radnikPocetnaRadniciBTNClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("radnikRadnici.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void radnikPocetnaRezervacijieBTNClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("radnikRezervacije.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    //endregion

   
}