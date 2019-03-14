package sample;


import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public  class Symulator extends Parent {


    public static boolean czyTrwaSymulacja;
    private boolean czyTrwaAnimacja;
    private boolean czyPrzekroczonoSciane;

    EventHandler<ActionEvent> przestawianieStatusu;
    TranslateTransition animacjaNaprzod;
    RotateTransition obrotWPrawo;
    RotateTransition obrotWLewo;

    LinkedList<Integer> rozkazy;
    LinkedList<Double> czasyTrwaniaLacznie;
    LinkedList<Double> czasyTrwania;
    int ktoryRozkaz;

    RadioButton przyciskAuto;

    Group napisyIPrzyciskiDoStatystyk;
    Label ustawCzasTrwania;
    Label ustawCzasNaprzod;
    Label ustawCzasObrot;
    Label ustawCzasIleProcent;

    Label napis_czasTrwaniaNaprzod;
    Button przycisk_jazdaNaprzod_dodawanie;
    Button przycisk_jazdaNaprzod_odejmowanie;
    Label napis_czasTrwaniaObrot;
    Button przycisk_obrot_dodawanie;
    Button przycisk_obrot_odejmowanie;
    Label napis_ileProcentSzybciej;
    Button przycisk_naprzodZRzedu_dodawanie;
    Button przycisk_naprzodZRzedu_odejmowanie;
    double czasTrwaniaNaprzod;
    double czasTrwaniaObrot;
    double ileProcent;

    Group statystyki_graficznie;
    Label napis_ileRozkazow;
    Label napis_pozX;
    Label napis_pozY;
    Label napis_lacznyCzas;
    Label napis_ostatniCzas;
    Label napis_lacznieNaprzod;
    Label napis_lacznieObrotow;
    int lacznieNaprzod;
    int lacznieObrotow;

    public int[][] segmenty;


    private Robot robot;

    Symulator(){
        robot = new Robot(Segment.ROZMIAR_LABIRYNTU-1, 0, Segment.GORA);

        przestawianieStatusu = e -> {
            czyTrwaAnimacja = false;
            if(!przyciskAuto.isSelected() && czyTrwaSymulacja && !czyPrzekroczonoSciane)
                wykonajRozkaz();
        };
        animacjaNaprzod = new TranslateTransition();
        animacjaNaprzod.setNode(robot.ikona);
        animacjaNaprzod.setByX(0);
        animacjaNaprzod.setByY(0);
        animacjaNaprzod.setOnFinished(przestawianieStatusu);
        obrotWPrawo = new RotateTransition();
        obrotWPrawo.setNode(robot.ikona);
        obrotWPrawo.setByAngle(90);
        obrotWPrawo.setOnFinished(przestawianieStatusu);
        obrotWLewo = new RotateTransition();
        obrotWLewo.setNode(robot.ikona);
        obrotWLewo.setByAngle(-90);
        obrotWLewo.setOnFinished(przestawianieStatusu);
        rozkazy = new LinkedList<>();
        czasyTrwaniaLacznie = new LinkedList<>();
        czasyTrwania = new LinkedList<>();
        ktoryRozkaz = -1;

        przyciskAuto = new RadioButton("Ręczne sterowanie symulacją");
        przyciskAuto.setSelected(false);
        przyciskAuto.setLayoutX(750);
        przyciskAuto.setLayoutY(460);

        napisyIPrzyciskiDoStatystyk = new Group();

        ustawCzasTrwania = new Label("Ustaw czas trwania poszczególnych ruchów:");
        ustawCzasTrwania.setLayoutX(760);
        ustawCzasTrwania.setLayoutY(490);
        ustawCzasNaprzod = new Label("Ruch naprzód:");
        ustawCzasNaprzod.setLayoutX(828);
        ustawCzasNaprzod.setLayoutY(520);
        przycisk_jazdaNaprzod_odejmowanie = new Button("-");
        przycisk_jazdaNaprzod_odejmowanie.setLayoutX(820);
        przycisk_jazdaNaprzod_odejmowanie.setLayoutY(540);
        przycisk_jazdaNaprzod_odejmowanie.setMinWidth(23);
        przycisk_jazdaNaprzod_dodawanie = new Button("+");
        przycisk_jazdaNaprzod_dodawanie.setLayoutX(893);
        przycisk_jazdaNaprzod_dodawanie.setLayoutY(540);
        przycisk_jazdaNaprzod_dodawanie.setMinWidth(23);
        czasTrwaniaNaprzod = 1.00;
        napis_czasTrwaniaNaprzod = new Label();
        napis_czasTrwaniaNaprzod.setLayoutX(855);
        napis_czasTrwaniaNaprzod.setLayoutY(543);

        ustawCzasObrot = new Label("Obrót:");
        ustawCzasObrot.setLayoutX(850);
        ustawCzasObrot.setLayoutY(570);
        przycisk_obrot_odejmowanie = new Button("-");
        przycisk_obrot_odejmowanie.setLayoutX(820);
        przycisk_obrot_odejmowanie.setLayoutY(590);
        przycisk_obrot_odejmowanie.setMinWidth(23);
        przycisk_obrot_dodawanie = new Button("+");
        przycisk_obrot_dodawanie.setLayoutX(893);
        przycisk_obrot_dodawanie.setLayoutY(590);
        przycisk_obrot_dodawanie.setMinWidth(23);
        czasTrwaniaObrot = 1.00;
        napis_czasTrwaniaObrot = new Label();
        napis_czasTrwaniaObrot.setLayoutX(855);
        napis_czasTrwaniaObrot.setLayoutY(593);

        ustawCzasIleProcent = new Label("O ile % szybciej jadąc ciągle prosto:");
        ustawCzasIleProcent.setLayoutX(780);
        ustawCzasIleProcent.setLayoutY(625);
        przycisk_naprzodZRzedu_odejmowanie = new Button("-");
        przycisk_naprzodZRzedu_odejmowanie.setLayoutX(820);
        przycisk_naprzodZRzedu_odejmowanie.setLayoutY(650);
        przycisk_naprzodZRzedu_odejmowanie.setMinWidth(23);
        przycisk_naprzodZRzedu_dodawanie = new Button("+");
        przycisk_naprzodZRzedu_dodawanie.setLayoutX(893);
        przycisk_naprzodZRzedu_dodawanie.setLayoutY(650);
        przycisk_naprzodZRzedu_dodawanie.setMinWidth(23);
        ileProcent = 20;
        napis_ileProcentSzybciej = new Label();
        napis_ileProcentSzybciej.setLayoutX(855);
        napis_ileProcentSzybciej.setLayoutY(653);

        ZaktualizujNapisyDoStatystyk();

        napisyIPrzyciskiDoStatystyk.getChildren().addAll(ustawCzasTrwania, ustawCzasNaprzod, przycisk_jazdaNaprzod_dodawanie,
                przycisk_jazdaNaprzod_odejmowanie, napis_czasTrwaniaNaprzod, ustawCzasObrot, przycisk_obrot_dodawanie,
                przycisk_obrot_odejmowanie, napis_czasTrwaniaObrot, ustawCzasIleProcent, przycisk_naprzodZRzedu_dodawanie,
                przycisk_naprzodZRzedu_odejmowanie, napis_ileProcentSzybciej);
        napisyIPrzyciskiDoStatystyk.setLayoutX(-10);
        this.getChildren().add(napisyIPrzyciskiDoStatystyk);

        przycisk_jazdaNaprzod_dodawanie.setOnAction(e -> { czasTrwaniaNaprzod += 0.05; ZaktualizujNapisyDoStatystyk();});
        przycisk_jazdaNaprzod_odejmowanie.setOnAction(e -> { if(czasTrwaniaNaprzod > 0.05)czasTrwaniaNaprzod -= 0.05;
        ZaktualizujNapisyDoStatystyk();});
        przycisk_obrot_dodawanie.setOnAction(e -> { czasTrwaniaObrot += 0.05; ZaktualizujNapisyDoStatystyk();});
        przycisk_obrot_odejmowanie.setOnAction(e -> { if(czasTrwaniaObrot > 0.05) czasTrwaniaObrot -= 0.05;
        ZaktualizujNapisyDoStatystyk();});
        przycisk_naprzodZRzedu_dodawanie.setOnAction(e -> { if(ileProcent < 99.5) ileProcent += 0.5; ZaktualizujNapisyDoStatystyk();});
        przycisk_naprzodZRzedu_odejmowanie.setOnAction(e -> { if(ileProcent > 0.5) ileProcent -= 0.5;
        ZaktualizujNapisyDoStatystyk();});

        statystyki_graficznie = new Group();
        napis_ileRozkazow = new Label("Wykonano 0/0 rozkazów");
        napis_ileRozkazow.setLayoutX(1000);
        napis_ileRozkazow.setLayoutY(440);
        napis_pozX = new Label("X: ?");
        napis_pozX.setLayoutX(1000);
        napis_pozX.setLayoutY(460);
        napis_pozY = new Label("Y: ?");
        napis_pozY.setLayoutX(1000);
        napis_pozY.setLayoutY(480);
        napis_lacznyCzas = new Label("Łączny czas jazdy: ?s");
        napis_lacznyCzas.setLayoutX(1000);
        napis_lacznyCzas.setLayoutY(520);
        napis_ostatniCzas = new Label("Czas ostatniego ruchu: ?s+");
        napis_ostatniCzas.setLayoutX(1000);
        napis_ostatniCzas.setLayoutY(540);
        napis_lacznieNaprzod = new Label("Łącznie ruchów naprzód: ?");
        napis_lacznieNaprzod.setLayoutX(1000);
        napis_lacznieNaprzod.setLayoutY(580);
        napis_lacznieObrotow = new Label("Łącznie obrotów: ?");
        napis_lacznieObrotow.setLayoutX(1000);
        napis_lacznieObrotow.setLayoutY(600);
        lacznieNaprzod = 0;
        lacznieObrotow = 0;

        statystyki_graficznie.getChildren().addAll(napis_ileRozkazow, napis_pozX, napis_pozY, napis_lacznyCzas, napis_ostatniCzas,
                napis_lacznieNaprzod, napis_lacznieObrotow);
        this.getChildren().add(statystyki_graficznie);
        statystyki_graficznie.setDisable(true);

        segmenty = new int[Segment.ROZMIAR_LABIRYNTU][Segment.ROZMIAR_LABIRYNTU];

        this.getChildren().add(robot.ikona);
        this.getChildren().add(przyciskAuto);
        czyTrwaSymulacja = false;
        this.czyTrwaAnimacja = false;
        this.czyPrzekroczonoSciane = false;
    }


    public void WlaczSymulacje(){
        if(ktoryRozkaz == -1) {
            ZaktualizujCzasTrwaniaAnimacji();
            robot.ikona.setVisible(true);
            ktoryRozkaz = 0;
            przyciskAuto.setDisable(true);
            napisyIPrzyciskiDoStatystyk.setDisable(true);
            statystyki_graficznie.setDisable(false);
            czyTrwaSymulacja = true;

            if(!zaladujRozkazy()) {
                czyTrwaAnimacja = true; // to jest po to, żeby przyciski do następnych kroków nie działąły jeśli
                return;                 // nie uda się załadować rozkazów
            }
            ZaktualizujStatystykiLive();

            if(!przyciskAuto.isSelected()) {
                wykonajRozkaz();
            }
        }
    }

    public void WylaczSymulacje(){
        czyTrwaSymulacja = false;

        robot.ikona.setVisible(false);
        // tworzenie robota od nowa, bo paradoksalnie łatwiej to zrobić niż go ustawić
        robot = new Robot(Segment.ROZMIAR_LABIRYNTU-1, 0, Segment.GORA);
        animacjaNaprzod.setNode(robot.ikona);
        obrotWPrawo.setNode(robot.ikona);
        obrotWLewo.setNode(robot.ikona);
        this.getChildren().add(robot.ikona);
        rozkazy.clear();
        czasyTrwaniaLacznie.clear();
        czasyTrwania.clear();
        ktoryRozkaz = -1;
        przyciskAuto.setDisable(false);
        napisyIPrzyciskiDoStatystyk.setDisable(false);
        SchowajStatystyki();

        czyTrwaAnimacja = false;
        czyPrzekroczonoSciane = false;

    }

    boolean zaladujRozkazy(){

        FileChooser fileLoader = new FileChooser();
        fileLoader.setTitle("Wybierz plik rozkazków do wczytania...");
        fileLoader.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"));
        fileLoader.setInitialDirectory(new File(System.getProperty("user.home")));
        File plik = fileLoader.showOpenDialog(this.getScene().getWindow());

        try( BufferedReader plikDoWczytywania = new BufferedReader(new FileReader(plik)) ) {
            // trzeba zrobić sprawdzanie, czy dane na pliku są ok
            // sprawdzanie błędów

            int liczba = -1;
            boolean czyZnalezionoZero = false;  // żeby się upewnić że plik kończy się zerem
            String linia = plikDoWczytywania.readLine(); // wczytujemy linię jako String
            while(linia != null){
                liczba = Integer.parseInt(linia); // tym sprawdzamy czy wczytana linia jest liczbą
                                                  // jeżeli nie jest to wyrzuci wyjątek
                if(liczba < 0 || liczba > 3)      // czy liczba jest w dobrym zakresie
                    throw new Exception();
                if( liczba == 0) czyZnalezionoZero = true;
                linia = plikDoWczytywania.readLine();
            }
            if(!czyZnalezionoZero) throw new Exception(); // jak nie ma zera to koniec

            BufferedReader plikDoWczytywania2 = new BufferedReader(new FileReader(plik)); // ładujemy plik od nowa
            // bo rewinda nie ma ;c

            czasyTrwaniaLacznie.add(0.00);
            czasyTrwania.add(0.00);
            while(true) {
                int wczyt = Integer.parseInt(plikDoWczytywania2.readLine());
                rozkazy.add(wczyt);
                double czas = 0;
                switch(wczyt){
                    case 0: break;
                    case 1:
                        if(czasyTrwaniaLacznie.size() > 0 && czasyTrwaniaLacznie.size() != 1 && rozkazy.get(czasyTrwaniaLacznie.size()-2) == 1 )
                            czas = czasTrwaniaNaprzod * ((100-ileProcent)/100);
                        else czas = czasTrwaniaNaprzod;
                        break;
                    case 2: czas = czasTrwaniaObrot; break;
                    case 3: czas = czasTrwaniaObrot; break;
                }
                czasyTrwania.add(czas);
                if(czasyTrwaniaLacznie.size() == 0) czasyTrwaniaLacznie.add(czas);
                else czasyTrwaniaLacznie.add(czas + czasyTrwaniaLacznie.get(czasyTrwaniaLacznie.size()-1));
                if(wczyt == 0) break;
            }
            return true;
        }
        catch(Exception e){
            FunkcjePomocniczne.wyrzucKomunikat("Nie udało się załadować rozkazów\n              lub są błędne!", (Stage)this.getScene().getWindow(), 80, 60);
            return false;
        }
    }

    void wykonajRozkaz(){
        switch (rozkazy.get(ktoryRozkaz)){
            case 0:
                czyTrwaAnimacja = false;
                ktoryRozkaz--;
                break;
            case 1:
                robot.naprzod();
                lacznieNaprzod++;
                break;
            case 2:
                robot.obrocWLewo();
                lacznieObrotow++;
                break;
            case 3:
                robot.obrocWPrawo();
                lacznieObrotow++;
                break;
        }
        ktoryRozkaz++;
        ZaktualizujStatystykiLive();
    }

    void wykonajRozkazWstecz(){
        ktoryRozkaz--;
        switch (rozkazy.get(ktoryRozkaz)){
            case 0: break;
            case 1:
                robot.cofnij();
                lacznieNaprzod--;
                break;
            case 2:
                robot.obrocWPrawo();
                lacznieObrotow--;
                break;
            case 3:
                robot.obrocWLewo();
                lacznieObrotow--;
                break;
        }
        ZaktualizujStatystykiLive();
    }

    public void NastepnyKrok(){
        if(przyciskAuto.isSelected() && !czyTrwaAnimacja && czyTrwaSymulacja && !czyPrzekroczonoSciane) {
            czyTrwaAnimacja = true;
            wykonajRozkaz();
        }
    }

    public void PoprzedniKrok(){
        if(przyciskAuto.isSelected() && !czyTrwaAnimacja && czyTrwaSymulacja && ktoryRozkaz > 0) {
            czyTrwaAnimacja = true;
            czyPrzekroczonoSciane = false;
            wykonajRozkazWstecz();
        }
    }

    private void ZaktualizujCzasTrwaniaAnimacji(){
        animacjaNaprzod.setDuration(Duration.seconds(czasTrwaniaNaprzod/4));
        obrotWPrawo.setDuration(Duration.seconds(czasTrwaniaObrot/4));
        obrotWLewo.setDuration(Duration.seconds(czasTrwaniaObrot/4));
    }

    private void ZaktualizujNapisyDoStatystyk(){
        napis_czasTrwaniaNaprzod.setText(String.format("%.2f", czasTrwaniaNaprzod));
        napis_czasTrwaniaObrot.setText(String.format("%.2f", czasTrwaniaObrot));
        napis_ileProcentSzybciej.setText(ileProcent + "%");
    }

    private void ZaktualizujStatystykiLive(){
        napis_ileRozkazow.setText("Wykonano " + ktoryRozkaz + "/" + (rozkazy.size()-1) + " rozkazów");
        napis_pozX.setText("X: " + (robot.poz_y + 1));
        napis_pozY.setText("Y: " + (robot.poz_x + 1));
        napis_lacznyCzas.setText("Łączny czas jazdy: " + String.format("%.2f", czasyTrwaniaLacznie.get(ktoryRozkaz)) + "s");
        napis_ostatniCzas.setText("Czas ostatniego ruchu: " + String.format("%.2f", czasyTrwania.get(ktoryRozkaz)) + "s+");
        napis_lacznieNaprzod.setText("Łącznie ruchów naprzód: " + lacznieNaprzod);
        napis_lacznieObrotow.setText("Łącznie obrotów: " + lacznieObrotow);
    }

    private void SchowajStatystyki(){
        lacznieNaprzod = 0;
        lacznieObrotow = 0;
        napis_ileRozkazow.setText("Wykonano ?/? rozkazów");
        napis_pozX.setText("X: ?");
        napis_pozY.setText("Y: ?");
        napis_lacznyCzas.setText("Łączny czas jazdy: ?s");
        napis_ostatniCzas.setText("Czas ostatniego ruchu: ?s+");
        napis_lacznieNaprzod.setText("Łącznie ruchów naprzód: ?");
        napis_lacznieObrotow.setText("Łącznie obrotów: ?");

        statystyki_graficznie.setDisable(true);
    }

    private void PrzekroczonoSciane(){
        czyPrzekroczonoSciane = true;
        FunkcjePomocniczne.wyrzucKomunikat("Przekroczono ścianę!", (Stage)this.getScene().getWindow(),120,70);
    }

    public class Robot {

        int poz_x;
        int poz_y;
        int zwrot;
        Polygon ikona;

        Robot(int x, int y, int zwrot) {
            this.poz_x = x;
            this.poz_y = y;
            this.zwrot = zwrot;
            ikona = new Polygon();
            ikona.getPoints().addAll(40 + y*40 + 20.00, 40 +  x*40 + 10.00,
                    40 + y*40 + 10.00, 40 + x*40 + 30.00,
                    40 + y*40 + 20.00, 40 + x*40 + 25.00,
                    40 + y*40 + 30.00, 40 + x*40 + 30.00);
            ikona.setFill(Color.YELLOW);
            ikona.setStroke(Color.BLACK);
            ikona.setStrokeWidth(1);
            ikona.setVisible(false);
        }

        void naprzod(){
            if(zwrot == Segment.GORA){
                animacjaNaprzod.setByY(-40);
                if((segmenty[poz_x][poz_y] & Segment.GORA_BIT) != 0)
                    PrzekroczonoSciane();

                --poz_x;
            }
            else if(zwrot == Segment.PRAWO){
                animacjaNaprzod.setByX(40);
                if((segmenty[poz_x][poz_y] & Segment.PRAWO_BIT) != 0)
                    PrzekroczonoSciane();

                ++poz_y;
            }
            else if(zwrot == Segment.DOL){
                animacjaNaprzod.setByY(40);
                if((segmenty[poz_x][poz_y] & Segment.DOL_BIT) != 0)
                    PrzekroczonoSciane();

                ++poz_x;
            }
            else{
                animacjaNaprzod.setByX(-40);
                if((segmenty[poz_x][poz_y] & Segment.LEWO_BIT) != 0)
                    PrzekroczonoSciane();

                --poz_y;
            }
            animacjaNaprzod.play();
            animacjaNaprzod.setByX(0);
            animacjaNaprzod.setByY(0);
        }

        void cofnij(){
            if(zwrot == Segment.GORA){
                animacjaNaprzod.setByY(40);
                ++poz_x;
            }
            else if(zwrot == Segment.PRAWO){
                animacjaNaprzod.setByX(-40);
                --poz_y;
            }
            else if(zwrot == Segment.DOL){
                animacjaNaprzod.setByY(-40);
                --poz_x;
            }
            else{
                animacjaNaprzod.setByX(40);
                ++poz_y;
            }
            animacjaNaprzod.play();
            animacjaNaprzod.setByX(0);
            animacjaNaprzod.setByY(0);
        }

        void obrocWPrawo(){
            if(zwrot < 4)
                zwrot++;
            else zwrot = Segment.GORA;

            obrotWPrawo.play();
        }

        void obrocWLewo(){
            if(zwrot > 1)
                zwrot--;
            else zwrot = Segment.LEWO;

            obrotWLewo.play();
        }

    }

}
