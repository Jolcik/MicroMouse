package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import java.awt.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class FunkcjePomocniczne {

    public static boolean sprawdzBledy( Segment[][] segmenty, Stage stage, boolean czyZapis ){

        int licznikMet = 0; // moze byc sytuacja kiedy bedzie jedna, czy wiecej met, takze zadnej
        for (int i = 0; i < Segment.ROZMIAR_LABIRYNTU-1; ++i)
            for(int j = 0; j < Segment.ROZMIAR_LABIRYNTU-1; ++j) {
                if(!segmenty[i][j].getDol() && !segmenty[i][j].getPrawo() && // ten if sprawdza czy jest taki kwadrat 2x2 z segmentow, ze po srodku nie ma scian
                        !segmenty[i+1][j].getGora() && !segmenty[i+1][j].getPrawo() &&
                        !segmenty[i+1][j+1].getGora() && !segmenty[i+1][j+1].getLewo() &&
                        !segmenty[i][j+1].getDol() && !segmenty[i][j+1].getLewo()) {
                    int licznikScian = segmenty[i][j].iloscScian() + // jezeli nie ma scian wewnatrz, to zobacz ile ma z zewnatrz
                            segmenty[i+1][j].iloscScian() +
                            segmenty[i][j+1].iloscScian() +
                            segmenty[i+1][j+1].iloscScian();

                    if(licznikScian != 7){          // wejscie powinno byc tylko jedno, czyli 7 scian
                                                    // jezeli wyjsc jest mniej/wiecej to jest jakis ewidentny blad
                        wyrzucKomunikat("Wykryto pustą przestrzeń!\n     Współrzędne: [ " + (i + 1) + ", " + (j + 1) + " ]",
                                stage, 100, 50); // albo to nie kwadrat 2x2 czyli jeszcze gorzej
                        return true;                 // jezeli jest taka sytuacja to przerwij bo labirynt jest na pewno zly
                    }
                    else ++licznikMet;
                }

            }
        if(licznikMet == 0) {  // gdy nic nie znajdzie
            wyrzucKomunikat("Brak mety!", stage, 160, 65);
            return true;
        }
        else if(licznikMet > 1){ // kiedy znajdzie wiecej
            wyrzucKomunikat("Wykryto więcej niż jedną metę!", stage, 120, 65 );
            return true;
        }

        // zalewanie
        segmenty[Segment.ROZMIAR_LABIRYNTU-1][Segment.ROZMIAR_LABIRYNTU-1].zalej( segmenty ); // zalewamy pierwszy segment, reszta wykona się rekurencyjnie
        int licznikNiezalanych = 0; // mam nadzieję, że to tylko referencja jest przekazywana
        for(int i = 0; i < Segment.ROZMIAR_LABIRYNTU; ++i) // lecimy po wszystkich segmentach i szukamy niezalanych
            for(int j = 0; j < Segment.ROZMIAR_LABIRYNTU; ++j){
                if(!segmenty[i][j].czyZalany) ++licznikNiezalanych; // jeżeli wykryje niezalany to inkrementuj
                segmenty[i][j].czyZalany = false; //trzeba wszystkie segementy "osuszyć" przed następnym zalaniem
            }
        if(licznikNiezalanych > 0){ // jak coś jest niezalane to znaczy, że nie można tam się dostać
            wyrzucKomunikat("Są niedostępne miejsca!", stage, 120, 65);
            return true;
        }

        if(!czyZapis) wyrzucKomunikat("Nie wykryto błędów!", stage, 120, 65);
        return false;
    }

    public static void wyrzucKomunikat( String komunikat, Stage stage, int x, int y ){
        Stage okno = new Stage();
        okno.initOwner(stage);
        okno.initModality(Modality.WINDOW_MODAL);

        Pane root = new Pane();
        Rectangle tlo = new Rectangle(0,0 , 500, 400);
        tlo.setFill(Color.rgb(220,248,244 ));
        root.getChildren().add(tlo);

        Text tekst = new Text(x, y, komunikat);
        tekst.setFont(Font.font("Bahnschrift Condensed", 18));
        tekst.setFill(Color.BLACK);
        Button but = new Button("Zamknij");
        but.setMinSize( 50, 20);
        but.setLayoutX(175);
        but.setLayoutY(120);
        but.setOnAction(e -> okno.close() );
        root.getChildren().add(tekst);
        root.getChildren().add(but);

        okno.setScene(new Scene(root, 400, 150));
        okno.setTitle("Komunikat");
        okno.getIcons().add(new Image("file:ikona.png"));
        okno.setResizable(false);
        okno.show();

        System.out.println(komunikat);
    }

    public static void zapiszStanLabiryntu( Segment[][] segmenty, Stage stage, boolean czyLabiryntMaBledy, boolean nieZapisujZBledami ){

        if(czyLabiryntMaBledy && nieZapisujZBledami) return;

        FileChooser fileSaver = new FileChooser();
        fileSaver.setTitle("Zapisywanie labiryntu...");
        fileSaver.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"));
        fileSaver.setInitialDirectory( new File(System.getProperty("user.home")));

        try( FileWriter plikDoZapisu = new FileWriter(fileSaver.showSaveDialog(stage)) ) {

            for (int i = 0; i < Segment.ROZMIAR_LABIRYNTU; ++i) {
                //String linia = "";
                for (int j = 0; j < Segment.ROZMIAR_LABIRYNTU; ++j) {
                    //linia = linia + segmenty[i][j].scianyBitowo() + " ";
                    plikDoZapisu.write(segmenty[i][j].scianyBitowo() + "\r\n");
                }
                //plikDoZapisu.write(linia.substring(0, linia.length()-1) + "\r\n");
            }
            //plikDoZapisu.close(); //kompilator mowi ze to niepotrzebne
            wyrzucKomunikat("Pomyślnie zapisano!", stage, 120, 65);
        }
        catch(Exception e){
            System.out.println(e.toString());
            wyrzucKomunikat("Nie udało się zapisać!", stage, 120, 65);
        }
    }

    public static void wczytajLabirynt( Segment[][] segmenty, Stage stage) {

        FileChooser fileLoader = new FileChooser();
        fileLoader.setTitle("Wczytywanie labiryntu...");
        fileLoader.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"));
        fileLoader.setInitialDirectory(new File(System.getProperty("user.home")));
        File plik = fileLoader.showOpenDialog(stage);

        try (BufferedReader plikDoWczytywania = new BufferedReader(new FileReader(plik))) {
            // sprawdzanie pliku
            int liczba = -1;
            int licznik = 0;
            String linia = plikDoWczytywania.readLine(); // wczytujemy linię jako String
            while(linia != null){
                liczba = Integer.parseInt(linia); // tym sprawdzamy czy wczytana linia jest liczbą
                // jeżeli nie jest to wyrzuci wyjątek
                if(liczba < 0 || liczba > 15)      // czy liczba jest w dobrym zakresie, od razu sprawdza czy nie ma zamknietych segmentów
                    throw new Exception();
                licznik++; // musi być równo 256 liczb
                linia = plikDoWczytywania.readLine();
            }
            if(licznik != 256) throw new Exception();
            // to powyżej sprawdza poprawność formalną wczyatnych danych
            // trzeba jeszcze sprawdzić czy nie ma przypadkiem kolidujących ścian, ale to niżej

            // wczytujemy kolejne liczby i przenosimy na labirynt
            BufferedReader plikDoWczytywania2 = new BufferedReader(new FileReader(plik)); // reset
            for (int i = 0; i < Segment.ROZMIAR_LABIRYNTU; ++i) {
                for (int j = 0; j < Segment.ROZMIAR_LABIRYNTU; ++j) {
                    segmenty[i][j].ustawZWczytywania(Integer.parseInt(plikDoWczytywania2.readLine()));
                }
            }

            // sprawdzanie czy istnieją kolidujące ściany
            try {
                for (int i = 0; i < Segment.ROZMIAR_LABIRYNTU; ++i) {
                    for (int j = 0; j < Segment.ROZMIAR_LABIRYNTU; ++j) {
                        if (i > 0 && (segmenty[i][j].getGora() != segmenty[i - 1][j].getDol()))
                            throw new Exception(); // gora
                        else if(i == 0) if(!segmenty[i][j].getGora()) throw new Exception();

                        if (j < Segment.ROZMIAR_LABIRYNTU-1 && (segmenty[i][j].getPrawo() != segmenty[i][j + 1].getLewo()))
                            throw new Exception(); // prawo
                        else if(j == Segment.ROZMIAR_LABIRYNTU-1) if(!segmenty[i][j].getPrawo()) throw new Exception();

                        if (i < Segment.ROZMIAR_LABIRYNTU-1 && (segmenty[i][j].getDol() != segmenty[i + 1][j].getGora()))
                            throw new Exception(); // dol
                        else if(i == Segment.ROZMIAR_LABIRYNTU-1) if(!segmenty[i][j].getDol()) throw new Exception();

                        if (j > 0 && (segmenty[i][j].getLewo() != segmenty[i][j - 1].getPrawo()))
                            throw new Exception(); // lewo
                        else if(j == 0) if(!segmenty[i][j].getLewo()) throw new Exception();
                    }
                }
            } catch(Exception e){
                for (int i = 0; i < Segment.ROZMIAR_LABIRYNTU; ++i) { // jeżeli coś nie tak, wyczyść cały labirynt
                    for (int j = 0; j < Segment.ROZMIAR_LABIRYNTU; ++j) {
                        segmenty[i][j].wyczyscSegment();
                    }
                }
                throw new Exception();
            }

            wyrzucKomunikat("Pomyślnie wczytano!", stage, 120, 65);
        } catch (Exception e) {
            System.out.println(e.toString());
            wyrzucKomunikat("Nie udało się wczytać!", stage, 120, 65);
        }
    }

    public static void generujLosowyLabirynt( Segment[][] segmenty ){

        for(int i = 0; i < Segment.ROZMIAR_LABIRYNTU; ++i)         // zamykanie wszystkich segmentów
            for(int j = 0; j < Segment.ROZMIAR_LABIRYNTU; ++j)
                segmenty[i][j].zamknijSegment();


        Deque<DanaNaStosie> stos = new ArrayDeque<DanaNaStosie>();
        DanaNaStosie gdzieJestesmy = new DanaNaStosie(Segment.ROZMIAR_LABIRYNTU-2,0 );

        //żeby pierwszy segment był zwrócony w górę
        segmenty[Segment.ROZMIAR_LABIRYNTU-1][0].czySprawdzony = true;
        segmenty[Segment.ROZMIAR_LABIRYNTU-1][0].sprawdzany = true;
        segmenty[Segment.ROZMIAR_LABIRYNTU-1][0].przelacz(Segment.GORA);
        segmenty[Segment.ROZMIAR_LABIRYNTU-2][0].przelacz(Segment.DOL);


        do {
            if(segmenty[gdzieJestesmy.x][gdzieJestesmy.y].czySprawdzony == false && gdzieJestesmy.ileSprawdzono < 4 ){
                int ktory = gdzieJestesmy.ileSprawdzono;
                gdzieJestesmy.ileSprawdzono++;
                stos.push(gdzieJestesmy);
                segmenty[gdzieJestesmy.x][gdzieJestesmy.y].sprawdzany = true;
                switch (gdzieJestesmy.kombinacja[ktory]) {
                    case 0:
                        if( gdzieJestesmy.x > 0 && !segmenty[gdzieJestesmy.x-1][gdzieJestesmy.y].sprawdzany){
                            segmenty[gdzieJestesmy.x][gdzieJestesmy.y].przelacz(Segment.GORA);
                            segmenty[gdzieJestesmy.x-1][gdzieJestesmy.y].przelacz(Segment.DOL);
                            gdzieJestesmy = new DanaNaStosie(gdzieJestesmy.x-1, gdzieJestesmy.y);
                        }
                        break;
                    case 1:
                        if( gdzieJestesmy.y < Segment.ROZMIAR_LABIRYNTU-1 && !segmenty[gdzieJestesmy.x][gdzieJestesmy.y+1].sprawdzany){
                            segmenty[gdzieJestesmy.x][gdzieJestesmy.y].przelacz(Segment.PRAWO);
                            segmenty[gdzieJestesmy.x][gdzieJestesmy.y+1].przelacz(Segment.LEWO);
                            gdzieJestesmy = new DanaNaStosie(gdzieJestesmy.x, gdzieJestesmy.y+1);
                        }
                        break;
                    case 2:
                        if( gdzieJestesmy.x < Segment.ROZMIAR_LABIRYNTU-1 && !segmenty[gdzieJestesmy.x+1][gdzieJestesmy.y].sprawdzany){
                            segmenty[gdzieJestesmy.x][gdzieJestesmy.y].przelacz(Segment.DOL);
                            segmenty[gdzieJestesmy.x+1][gdzieJestesmy.y].przelacz(Segment.GORA);
                            gdzieJestesmy = new DanaNaStosie(gdzieJestesmy.x+1, gdzieJestesmy.y);
                        }
                        break;
                    case 3:
                        if( gdzieJestesmy.y > 0 && !segmenty[gdzieJestesmy.x][gdzieJestesmy.y-1].sprawdzany){
                            segmenty[gdzieJestesmy.x][gdzieJestesmy.y].przelacz(Segment.LEWO);
                            segmenty[gdzieJestesmy.x][gdzieJestesmy.y-1].przelacz(Segment.PRAWO);
                            gdzieJestesmy = new DanaNaStosie(gdzieJestesmy.x, gdzieJestesmy.y-1);
                        }
                        break;
                }
                if(gdzieJestesmy.ileSprawdzono == 4)
                    segmenty[gdzieJestesmy.x][gdzieJestesmy.y].czySprawdzony = true;
            }
            else{
                gdzieJestesmy = stos.pop();
            }
        } while(!stos.isEmpty());


        for(int i = 0; i < Segment.ROZMIAR_LABIRYNTU; ++i)         // aktualizowanie graficznego stanu ścian
            for(int j = 0; j < Segment.ROZMIAR_LABIRYNTU; ++j){
                segmenty[i][j].aktualizujKolory();
                segmenty[i][j].sprawdzany = false;
                segmenty[i][j].czySprawdzony = false;
            }
    }
}

class DanaNaStosie{
    public int x;
    public int y;
    public int ileSprawdzono;
    public int[] kombinacja;

    DanaNaStosie(int index_x, int index_y){
        ileSprawdzono = 0;
        this.x = index_x;
        this.y = index_y;
        kombinacja = new int[4];
        generujKombinacje();
    }

    public void generujKombinacje(){
        Random rand = new Random();

        for(int i = 0; i < 4;){
            int liczba = rand.nextInt(4);
            boolean znacznik = false;
            for(int j = 0; j < i; ++j)
                if(kombinacja[j] == liczba) znacznik = true;

            if(!znacznik) {
                kombinacja[i] = liczba;
                ++i;
            }
        }
    }
}


