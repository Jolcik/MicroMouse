package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.RadioButton;
import javafx.scene.shape.Rectangle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Polygon;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;


public class Main extends Application {

    final int ROZMIAR_LABIRYNTU = 16;


    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Kreator labiryntów MicroMouse v1.00 -  KNR Bionik 2019");
        Pane root = new Pane();

        Rectangle tlo = new Rectangle(0, 0, 1400, 800);
        tlo.setFill(Color.rgb(220,248,244));
        root.getChildren().add(tlo);

        Segment[][] segmenty = new Segment[ROZMIAR_LABIRYNTU][ROZMIAR_LABIRYNTU];
        for (int i = 0; i < ROZMIAR_LABIRYNTU; ++i)
            for(int j = 0; j < ROZMIAR_LABIRYNTU; ++j) {
                segmenty[i][j] = new Segment(i, j);
                root.getChildren().add(segmenty[i][j]);
            }

        /////////////////////////////////////////////////////////////////////
        //                           SYMULATOR                             //

        Symulator symulator = new Symulator();
        root.getChildren().add(symulator);

        symulator.czyTrwaSymulacja = false;

        //// PRZYCISKI
        // przycisk do włączania symulacji
        Button przyciskWlaczSymulacje = new Button("");
        przyciskWlaczSymulacje.setGraphic(new ImageView(new Image("file:src/sample/PrzyciskRozpocznijSymulacje.png")));
        przyciskWlaczSymulacje.setLayoutX(750);
        przyciskWlaczSymulacje.setLayoutY(240);
        root.getChildren().add(przyciskWlaczSymulacje);

        // wylaczanie symulacji
        Button przyciskWylaczSymulacje = new Button("");
        przyciskWylaczSymulacje.setGraphic(new ImageView(new Image("file:src/sample/PrzyciskZatrzymajSymulacje.png")));
        przyciskWylaczSymulacje.setLayoutX(1000);
        przyciskWylaczSymulacje.setLayoutY(240);
        przyciskWylaczSymulacje.setDisable(true);
        root.getChildren().add(przyciskWylaczSymulacje);

        // nastepny krok
        Button przyciskNastepny = new Button("");
        przyciskNastepny.setGraphic(new ImageView(new Image("file:src/sample/PrzyciskNastepny.png")));
        przyciskNastepny.setLayoutX(1000);
        przyciskNastepny.setLayoutY(340);
        przyciskNastepny.setDisable(true);
        root.getChildren().add(przyciskNastepny);

        // poprzedni krok
        Button przyciskPoprzedni = new Button("");
        przyciskPoprzedni.setGraphic(new ImageView(new Image("file:src/sample/PrzyciskPoprzedni.png")));
        przyciskPoprzedni.setLayoutX(750);
        przyciskPoprzedni.setLayoutY(340);
        przyciskPoprzedni.setDisable(true);
        root.getChildren().add(przyciskPoprzedni);

        //                                                                 //
        /////////////////////////////////////////////////////////////////////

        //obsluga klikniecia w segment na poziomie sasiednich segmentow
        EventHandler<MouseEvent> klikniecieSciany = e ->{
            if(!symulator.czyTrwaSymulacja) // nie pozwól na zmienę segmentów, jeśli trwa symulacja
            try {
                int ind1 = ((Segment) ((Polygon) e.getTarget()).getParent()).index1;
                int ind2 = ((Segment) ((Polygon) e.getTarget()).getParent()).index2;

                if (ind1 != 0 && segmenty[ind1 - 1][ind2].getDol() != segmenty[ind1][ind2].getGora())
                    segmenty[ind1 - 1][ind2].przelacz(Segment.DOL);
                else if (ind1 != ROZMIAR_LABIRYNTU-1 && segmenty[ind1 + 1][ind2].getGora() != segmenty[ind1][ind2].getDol())
                    segmenty[ind1 + 1][ind2].przelacz(Segment.GORA);
                else if (ind2 != 0 && segmenty[ind1][ind2 - 1].getPrawo() != segmenty[ind1][ind2].getLewo())
                    segmenty[ind1][ind2 - 1].przelacz(Segment.PRAWO);
                else if (ind2 != ROZMIAR_LABIRYNTU-1 && segmenty[ind1][ind2 + 1].getLewo() != segmenty[ind1][ind2].getPrawo())
                    segmenty[ind1][ind2 + 1].przelacz(Segment.LEWO);
            }
            catch (Exception wydarzenie){ // zapobiega błędom przy przesuwaniu
                System.out.println(wydarzenie.toString()); // nie miało to żadnych efektów, ale obsługujemy to tak czy inaczej
            }
        };
        primaryStage.addEventHandler( MouseEvent.MOUSE_CLICKED, klikniecieSciany );


        //przycisk do sprawdzania bledow
        Button przyciskDoSprawdzaniaBledow = new Button("");
        przyciskDoSprawdzaniaBledow.setGraphic(new ImageView(new Image("file:src/sample/Przycisk.png")));
        //przyciskDoSprawdzaniaBledow.setMinSize(200, 60);
        przyciskDoSprawdzaniaBledow.setLayoutX(750);
        przyciskDoSprawdzaniaBledow.setLayoutY(40);
        root.getChildren().add(przyciskDoSprawdzaniaBledow);

        //radio button do zapisywania
        RadioButton czyZapisywacZBledami = new RadioButton("Zapisz mapę, nawet jeśli ma błędy");
        czyZapisywacZBledami.setSelected(false);
        czyZapisywacZBledami.setLayoutX(750);
        czyZapisywacZBledami.setLayoutY(440);
        root.getChildren().add(czyZapisywacZBledami);

        // przycisk do zapisywania
        Button przyciskZapisz = new Button("");
        przyciskZapisz.setGraphic(new ImageView(new Image("file:src/sample/PrzyciskZapisz.png")));
        przyciskZapisz.setLayoutX(750);
        przyciskZapisz.setLayoutY(140);
        root.getChildren().add(przyciskZapisz);

        // przycisk do generowania
        Button przyciskGeneruj = new Button("");
        przyciskGeneruj.setGraphic(new ImageView(new Image("file:src/sample/PrzyciskGeneruj.png")));
        przyciskGeneruj.setLayoutX(1000);
        przyciskGeneruj.setLayoutY(40);
        root.getChildren().add(przyciskGeneruj);


        // przycisk do wczytywania
        Button przyciskWczytaj = new Button("");
        przyciskWczytaj.setGraphic(new ImageView(new Image("file:src/sample/PrzyciskWczytaj.png")));
        przyciskWczytaj.setLayoutX(1000);
        przyciskWczytaj.setLayoutY(140);
        root.getChildren().add(przyciskWczytaj);




        // DZIAŁANIE PRZYCISKÓW
        przyciskNastepny.setOnAction( e -> symulator.NastepnyKrok());
        przyciskPoprzedni.setOnAction( e -> symulator.PoprzedniKrok());
        przyciskDoSprawdzaniaBledow.setOnAction(e -> FunkcjePomocniczne.sprawdzBledy( segmenty, primaryStage, false ));
        przyciskZapisz.setOnAction( e -> FunkcjePomocniczne.zapiszStanLabiryntu(segmenty, primaryStage,
                FunkcjePomocniczne.sprawdzBledy( segmenty, primaryStage, true ), !czyZapisywacZBledami.isSelected() ));
        przyciskGeneruj.setOnAction( e -> FunkcjePomocniczne.generujLosowyLabirynt(segmenty));
        przyciskWczytaj.setOnAction( e -> FunkcjePomocniczne.wczytajLabirynt(segmenty, primaryStage ));

        // WYŁĄCZANIE PRZYCISKÓW
        przyciskWlaczSymulacje.setOnAction( e -> {
            przyciskWlaczSymulacje.setDisable(true);
            przyciskWylaczSymulacje.setDisable(false);
            przyciskDoSprawdzaniaBledow.setDisable(true);
            czyZapisywacZBledami.setDisable(true);
            przyciskZapisz.setDisable(true);
            przyciskGeneruj.setDisable(true);
            przyciskWczytaj.setDisable(true);
            if(symulator.przyciskAuto.isSelected()){
                przyciskNastepny.setDisable(false);
                przyciskPoprzedni.setDisable(false);
            }

            for(int i = 0; i < ROZMIAR_LABIRYNTU; ++i)
                for(int j = 0; j < ROZMIAR_LABIRYNTU; ++j)
                    symulator.segmenty[i][j] = segmenty[i][j].scianyBitowo();

            symulator.WlaczSymulacje();
        });
        przyciskWylaczSymulacje.setOnAction( e -> {
            przyciskWlaczSymulacje.setDisable(false);
            przyciskWylaczSymulacje.setDisable(true);
            przyciskDoSprawdzaniaBledow.setDisable(false);
            czyZapisywacZBledami.setDisable(false);
            przyciskZapisz.setDisable(false);
            przyciskGeneruj.setDisable(false);
            przyciskWczytaj.setDisable(false);
            przyciskNastepny.setDisable(true);
            przyciskPoprzedni.setDisable(true);
            symulator.WylaczSymulacje();
        });


        Scene glownaScena = new Scene(root, 1280, 720);
        primaryStage.getIcons().add(new Image("file:ikona.png"));
        primaryStage.setResizable( false );
        //glownaScena.setFill(Color.YELLOW);
        primaryStage.setScene( glownaScena );
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
