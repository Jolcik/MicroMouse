package sample;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Segment extends Parent{

    static final int  ROZMIAR_LABIRYNTU = 16;

    public boolean czySprawdzony;
    public boolean sprawdzany;

    private int licznik;

    final int przesuniecieX = 40;
    final int przesuniecieY = 40;

    private boolean gora;
    private boolean dol;
    private boolean lewo;
    private boolean prawo;

    int index1;
    int index2;

    static final int GORA = 1;
    static final int PRAWO = 2;
    static final int DOL = 3;
    static final int LEWO = 4;

    static final int GORA_BIT = 8;
    static final int DOL_BIT = 2;
    static final int PRAWO_BIT = 4;
    static final int LEWO_BIT = 1;

    Polygon trojkatGorny;
    Polygon trojkatDolny;
    Polygon trojkatLewy;
    Polygon trojkatPrawy;

    Polygon bokGorny;
    Polygon bokDolny;
    Polygon bokLewy;
    Polygon bokPrawy;

    public boolean czyZalany;

    @Override
    public String toString(){
        return "Segment ( " + index1 + ", " + index2 + "): Sciany -> " + gora + dol + lewo + prawo;
    }

    // moznaby to zrobic bitowo, ale ze wzgledu na dydaktyke i dobry sprzet, nie przejmujemy sie tym
    Segment( int index1, int index2 ){ // inicjalizacja wlasciwosci segmentu
        czySprawdzony = false;
        sprawdzany = false;

        this.licznik = 0;

        this.czyZalany = false;
        this.index1 = index1;
        this.index2 = index2;

        if( index1 != 0 ) gora = false; // jezeli pole jest na gorze to na gorze jest sciana
            else gora = true;
        if( index1 != ROZMIAR_LABIRYNTU-1 ) dol = false; // itd. tylko dla inncyh przypadkow
            else dol = true;
        if( index2 != 0 ) lewo = false;
            else lewo = true;
        if( index2 != ROZMIAR_LABIRYNTU-1 ) prawo = false;
            else prawo = true;

        // reczne tworzenie czworokatow skladajacych sie na segment
        // to sa boki akurat
        bokGorny = new Polygon();
        bokGorny.getPoints().addAll(przesuniecieX + 0.00 + 40*index2, przesuniecieY + 0.00 + 40*index1,
                przesuniecieX + 40.00 + 40*index2, przesuniecieY +  0.00 + 40*index1,
                przesuniecieX + 35.00 + 40*index2, przesuniecieY +  5.00 + 40*index1,
                przesuniecieX + 5.00 + 40*index2, przesuniecieY +  5.00 + 40*index1 );
        bokDolny = new Polygon();
        bokDolny.getPoints().addAll(przesuniecieX + 0.00 + 40*index2, przesuniecieY + 40.00 + 40*index1,
                przesuniecieX + 40.00 + 40*index2, przesuniecieY +  40.00 + 40*index1,
                przesuniecieX + 35.00 + 40*index2, przesuniecieY +  35.00 + 40*index1,
                przesuniecieX + 5.00 + 40*index2, przesuniecieY +  35.00 + 40*index1 );
        bokLewy = new Polygon();
        bokLewy.getPoints().addAll(przesuniecieX + 0.00 + 40*index2, przesuniecieY + 0.00 + 40*index1,
                przesuniecieX + 0.00 + 40*index2, przesuniecieY +  40.00 + 40*index1,
                przesuniecieX + 5.00 + 40*index2, przesuniecieY +  35.00 + 40*index1,
                przesuniecieX + 5.00 + 40*index2, przesuniecieY +  5.00 + 40*index1 );
        bokPrawy = new Polygon();
        bokPrawy.getPoints().addAll(przesuniecieX + 40.00 + 40*index2, przesuniecieY + 0.00 + 40*index1,
                przesuniecieX + 40.00 + 40*index2, przesuniecieY +  40.00 + 40*index1,
                przesuniecieX + 35.00 + 40*index2, przesuniecieY +  35.00 + 40*index1,
                przesuniecieX + 35.00 + 40*index2, przesuniecieY +  5.00 + 40*index1 );

        // srodki segmentu
        // rozroznienie jest po to by obie czesci byly klikalne, ale w innych kolorach
        trojkatGorny = new Polygon();
        trojkatGorny.getPoints().addAll(przesuniecieX + 0.00 + 40*index2, przesuniecieY + 0.00 + 40*index1,
                przesuniecieX + 40.00 + 40*index2, przesuniecieY +  0.00 + 40*index1,
                przesuniecieX + 20.00 + 40*index2, przesuniecieY +  20.00 + 40*index1);
        trojkatDolny = new Polygon();
        trojkatDolny.getPoints().addAll(przesuniecieX + 0.00 + 40*index2, przesuniecieY + 40.00 + 40*index1,
                przesuniecieX + 40.00 + 40*index2, przesuniecieY +  40.00 + 40*index1,
                przesuniecieX + 20.00 + 40*index2, przesuniecieY +  20.00 + 40*index1);
        trojkatLewy = new Polygon();
        trojkatLewy.getPoints().addAll(przesuniecieX + 0.00 + 40*index2, przesuniecieY + 0.00 + 40*index1,
                przesuniecieX + 0.00 + 40*index2, przesuniecieY +  40.00 + 40*index1,
                przesuniecieX + 20.00 + 40*index2, przesuniecieY +  20.00 + 40*index1);
        trojkatPrawy = new Polygon();
        trojkatPrawy.getPoints().addAll(przesuniecieX + 40.00 + 40*index2, przesuniecieY + 0.00 + 40*index1,
                przesuniecieX + 40.00 + 40*index2, przesuniecieY +  40.00 + 40*index1,
                przesuniecieX + 20.00 + 40*index2, przesuniecieY +  20.00 + 40*index1);

        this.aktualizujKolory();

        this.getChildren().add( trojkatGorny );
        this.getChildren().add( trojkatDolny );
        this.getChildren().add( trojkatLewy );
        this.getChildren().add( trojkatPrawy );
        this.getChildren().add( bokGorny );
        this.getChildren().add( bokDolny );
        this.getChildren().add( bokPrawy );
        this.getChildren().add( bokLewy );

        trojkatGorny.setFill(Color.WHITE);
        trojkatDolny.setFill(Color.WHITE);
        trojkatLewy.setFill(Color.WHITE);
        trojkatPrawy.setFill(Color.WHITE);

        // klikniecia w odpowiednie czworokaty robi to samo
        trojkatGorny.setOnMouseClicked(e ->  przelacz( GORA ) );
        trojkatDolny.setOnMouseClicked(e ->  przelacz( DOL ) );
        trojkatLewy.setOnMouseClicked(e ->  przelacz( LEWO ) );
        trojkatPrawy.setOnMouseClicked(e ->  przelacz( PRAWO ) );
        bokGorny.setOnMouseClicked(e ->  przelacz( GORA ) );
        bokDolny.setOnMouseClicked(e ->  przelacz( DOL ) );
        bokLewy.setOnMouseClicked(e ->  przelacz( LEWO ) );
        bokPrawy.setOnMouseClicked(e ->  przelacz( PRAWO ) );
    }

    public void przelacz( int wybor ){
        if(Symulator.czyTrwaSymulacja) return;

        switch(wybor){
            case GORA: if(index1 != 0)  gora = !gora;                       break;
            case DOL: if(index1 != ROZMIAR_LABIRYNTU-1) dol = !dol;         break;
            case LEWO: if(index2 != 0)  lewo = !lewo;                       break;
            case PRAWO: if(index2 != ROZMIAR_LABIRYNTU-1) prawo = !prawo;   break;
        }
        this.aktualizujKolory();

        if(wybor == 4 && this.index1 == 14 && this.index2 == 07) licznik++;
        else licznik = 0;
        if(licznik == 20) FunkcjePomocniczne.wyrzucKomunikat("    Jak być poetą w tych czasach?\n Nie da się, bo kompresja fraktalna",
                (Stage)this.getScene().getWindow(), 70, 50);
    }


    public void aktualizujKolory(){
        if( gora == true ) bokGorny.setFill(Color.BLACK);
        else bokGorny.setFill(Color.LIGHTGRAY);

        if( dol == true ) bokDolny.setFill(Color.BLACK);
        else bokDolny.setFill(Color.LIGHTGRAY);

        if( lewo == true ) bokLewy.setFill(Color.BLACK);
        else bokLewy.setFill(Color.LIGHTGRAY);

        if( prawo == true ) bokPrawy.setFill(Color.BLACK);
        else bokPrawy.setFill(Color.LIGHTGRAY);
    }

    public int iloscScian(){
        int licznik = 0;
        if( gora )  ++licznik;
        if( dol )  ++licznik;
        if( lewo )  ++licznik;
        if( prawo )  ++licznik;
        return licznik;
    }

    public int scianyBitowo(){

        byte liczba = 0;

        if( getGora() ) liczba |= GORA_BIT;
        if( getDol() ) liczba |= DOL_BIT;
        if( getLewo() ) liczba |= LEWO_BIT;
        if( getPrawo() ) liczba |= PRAWO_BIT;

        return liczba;

    }

    public void ustawZWczytywania(int wczytLiczba){
        final int GORA_BIT = 8;
        final int DOL_BIT = 2;
        final int PRAWO_BIT = 4;
        final int LEWO_BIT = 1;

        if((wczytLiczba & GORA_BIT) != 0) this.gora = true; else this.gora = false;
        if((wczytLiczba & DOL_BIT) != 0) this.dol = true; else this.dol = false;
        if((wczytLiczba & LEWO_BIT) != 0) this.lewo = true; else this.lewo = false;
        if((wczytLiczba & PRAWO_BIT) != 0) this.prawo = true; else this.prawo = false;

        this.aktualizujKolory();
    }

    public void zalej(Segment[][] segmenty){
        if(!this.czyZalany){
            this.czyZalany = true;
            if(!this.getGora()) segmenty[this.index1-1][this.index2].zalej( segmenty );
            if(!this.getDol()) segmenty[this.index1+1][this.index2].zalej( segmenty );
            if(!this.getPrawo()) segmenty[this.index1][this.index2+1].zalej( segmenty );
            if(!this.getLewo()) segmenty[this.index1][this.index2-1].zalej( segmenty );
        }
        else return;
    }

    public void zamknijSegment(){
        this.gora = true;
        this.dol = true;
        this.lewo = true;
        this.prawo = true;
    }

    public void wyczyscSegment(){
        if(this.index1 != 0) this.gora = false; else this.gora = true;
        if(this.index1 != ROZMIAR_LABIRYNTU-1) this.dol = false; else this.dol = true;
        if(this.index2 != 0) this.lewo = false; else this.lewo = true;
        if(this.index2 != ROZMIAR_LABIRYNTU-1) this.prawo = false; else this.prawo = true;
        this.aktualizujKolory();
    }

    // gettery
    public boolean getDol() {
        return dol;
    }
    public boolean getGora() {
        return gora;
    }
    public boolean getLewo() {
        return lewo;
    }
    public boolean getPrawo() {
        return prawo;
    }

}