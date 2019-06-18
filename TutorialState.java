import java.awt.*;
import java.awt.image.*;
/**
 * Der Tutorial-State ist der State, in dem sich das Spiel befindet, wenn die Anleitung aufgerufen wird.
 * @author Ares Zühlke, Cepehr Bromand, Jakob Kleine
 * @version 0.03 (28.05.2019)
 * @since 0.01 (22.05.2019
 */
public class TutorialState extends State
{
    private static final int ANZAHL_PAGES = 2; //Die Anzahl der Seiten in der Anleitung
    private Font überschrift; //Ein Font für Überschriften
    private Font unterüberschrift; //Ein Font für Unterüberschriften im Text
    private Font standardSchrift; //Ein Font für regulären Text
    private Color schriftFarbe; 
    private int pageNr; //Die Seitennummer, bei der man sich gerade in der Anleitung befindet.
    /**
     * Erstellt einen neuen Tutrial-State
     * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
     * @since 22.05.2019
     */
    public TutorialState() {
        überschrift = new Font("Futura", Font.BOLD, 40);
        unterüberschrift = new Font("Futura", Font.BOLD, 25);
        standardSchrift = new Font("Futura", Font.PLAIN, 18);
        schriftFarbe = new Color(65, 41, 31);
        pageNr = 0;
    }
    
    /**
     * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
     * @since 22.05.2019
     */
    public void render(Graphics g)
    {
        g.drawImage(MainMenuState.menuBackground, 0, 0, null);
            Utils.fontFestlegen(g,überschrift);
            g.setColor(schriftFarbe.darker().darker()); //Eine dunklere Schriftfarbe ist hier leichter zu lesen.
            /* 
             * Speichert die Zeilenhöhe, sodass beim einfügen von Text die Zeilenhöhe automatisch angepasst wird, indem bei jedem Anzeigen von Text die Zeilenhöhe
             * um den gewünschten Abstand nach oben inkrementiert wird.
             */
            int zeilenHoehe = 100; 
            Utils.centerText(g, "ANLEITUNG", Game.SCREEN_WIDTH, zeilenHoehe);
            switch(pageNr) { //Hier wird die jeweils anzuzeigende Seite gerendert. Das wird nicht genauer kommentiert, das Ergebnis ist im Spiel einzusehen
                case 0: //Seite 1:
                    g.setFont(unterüberschrift);
                    Utils.centerText(g, "Ziel des Spiels", Game.SCREEN_WIDTH, zeilenHoehe+=40); 
                    g.setFont(standardSchrift); //Font-Festlegen muss nur einmal aufgerufen werden; dann sind die RenderingHints für Text bereits gesetzt
                    g.drawString("In diesem Informatik-orientierten Spiel geht es darum einen möglichst", 40, zeilenHoehe+=35);
                    g.drawString("hohen Score zu erzielen, indem man Gegner besiegt, die auf dem ", 40, zeilenHoehe+=20);
                    g.drawString("Spielfeld erscheinen und Items einsammelt, die einem dabei.", 40, zeilenHoehe+=20);
                    g.drawString("helfen.", 40, zeilenHoehe+=20);
                    g.setFont(unterüberschrift);
                    Utils.centerText(g, "Steuerung", Game.SCREEN_WIDTH, zeilenHoehe+=40);
                    g.setFont(standardSchrift);
                    g.drawString("Die Spielfigur wird mit den Tasten W (oben), A (links), S (unten)", 40, zeilenHoehe+=40);
                    g.drawString("und D  (rechts) oder den Pfeiltasten in die entsprechende Richtung", 40, zeilenHoehe+=20);
                    g.drawString("bewegt.", 40, zeilenHoehe+=20);
                    g.drawString("Ein Angriff wird mit Leertaste oder Enter gestartet und erfolgt", 40, zeilenHoehe+=20);
                    g.drawString("immer in Blickrichtung der Spielfigur. Während dem Angreifen kann", 40, zeilenHoehe+=20);
                    g.drawString("sich die Spielfigur nicht bewegen.", 40, zeilenHoehe+=20);
                    g.setFont(unterüberschrift);
                    Utils.centerText(g, "Gegner", Game.SCREEN_WIDTH, zeilenHoehe+=40);
                    g.setFont(standardSchrift);
                    g.drawString("Auf dem Spielfeld werden in zufälligen Abständen zufällige Gegner auf", 40, zeilenHoehe+=40);
                    g.drawString("zufälligen Feldern gespawnt. Sie fügen der Spielfigur Schaden zu und", 40, zeilenHoehe+=20);
                    g.drawString("werfen sie ein Stück zurück (geben Knockback).", 40, zeilenHoehe+=20);
                    g.drawString("Bisher gibt es folgende Gegener:", 40, zeilenHoehe+=20);
                    g.drawString("Seiten-Effekt: Der Seiten-Effekt (dargestellt mit einem Krebs) läuft", 40, zeilenHoehe+=30);
                    g.drawString("ausschließlich seitlich und greift bei Berührung der Spielfigur mit", 40, zeilenHoehe+=20);
                    g.drawString("seinen Scheren an.", 40, zeilenHoehe+=20);
                    g.drawString("Virus: Der Virus ist verfolgt die Spielfigur. Er fügt ihr Schaden zu,", 40, zeilenHoehe+=30);
                    g.drawString("indem er seine Waffe (momentan ein Cursor) auf sie wirft.", 40, zeilenHoehe+=20);
                    g.drawString("Momentan erfolgt der Angriff des Virus nur nach oben und unten.", 40, zeilenHoehe+=20);
            break;
            case 1: //Seite 2:
                    g.setFont(unterüberschrift);
                    Utils.centerText(g, "Items", Game.SCREEN_WIDTH, zeilenHoehe+=40);
                    g.setFont(standardSchrift);
                    g.drawString("Items sind Gegenstände, die zufällig auf dem Spielfeld gespanwnt", 40, zeilenHoehe+=40);
                    g.drawString("werden. Sie sind nützliche Power-Ups, die dem Spieler helfen,", 40, zeilenHoehe+=20);
                    g.drawString("gegen die Gegner zu kämpfen.", 40, zeilenHoehe+=20);
                    g.drawString("Wenn die Spielfigur ein Item berührt sammelt sie es ein. Momentan", 40, zeilenHoehe+=20);
                    g.drawString("gibt es folgende Items:", 40, zeilenHoehe+=20);
                    
                    g.drawString("Cursor: Der Cursor ist eine Waffe, die auf Gegner geworfen werden", 40, zeilenHoehe+=30);
                    g.drawString("kann. Nach seinem Angriff kommt er zur Spielfigur zurück.", 40, zeilenHoehe+=20);
                    g.drawString("Kaffee: Kaffee erhöht die Geschwindigkeit der Spielfigur, aber", 40, zeilenHoehe+=30);
                    g.drawString("fügt ihr im Gegenzug Schaden zu. Je mehr Kaffee getrunken wird,", 40, zeilenHoehe+=20);
                    g.drawString("desto geringer ist der Geschwindigkeits-Boost, aber desto", 40, zeilenHoehe+=20);
                    g.drawString("höher ist der zugefügte Schaden.", 40, zeilenHoehe+=20);
                    g.drawString("Pizza: Pizza erhöht die Lebenpunkte der Spielfigur", 40, zeilenHoehe+=30);
                    
                    g.setFont(unterüberschrift);
                    Utils.centerText(g, "Score", Game.SCREEN_WIDTH, zeilenHoehe+=40);
                    g.setFont(standardSchrift);
                    g.drawString("Das Töten von Gegnern bringt Punkte ein. Jeder Gegner-Typ bringt", 40, zeilenHoehe+=40);
                    g.drawString("dabei eine eigene Anzahl an Score-Punkten. ", 40, zeilenHoehe+=20);
                    g.drawString("Der Score wird in der oberen rechten Ecke des Spiels angezeigt. ", 40, zeilenHoehe+=20);
                    g.drawString("Die 10 besten Scores werden gespeichert und können im Menü", 40, zeilenHoehe+=20);
                    g.drawString("unter dem Reiter Bestenliste eingesehen werden. Zusätzlich zu", 40, zeilenHoehe+=20);
                    g.drawString("der erreichten Punktzahl wird dabei auch der Name des Spielers", 40, zeilenHoehe+=20);
                    g.drawString("und das Datum des Spiels gespeichert. Das Ändern des gespeicherten", 40, zeilenHoehe+=20);
                    g.drawString("Namens ist allerdings momentan nicht möglich.", 40, zeilenHoehe+=20);
            break;
        }

        g.setColor(Color.white);
        g.setFont(standardSchrift.deriveFont(Font.ITALIC, 15)); 
        String hinweis = "Seite "+(pageNr+1)+"; Blättern mit w&s / a&d oder Pfeiltasten. Zum Hauptmenü: bitte ESCAPE drücken.";
        Utils.centerText(g, hinweis, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT-15);
    }
    /**
     * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
     * @since 28.05.2019
     */
    public void update(KeyManager keyManager)
    {
        if(keyManager.downEinmal() || keyManager.rightEinmal()) { //unten/rechts -> Scroll-Seite wird erhöht
            if(pageNr < ANZAHL_PAGES-1)  //Wenn nicht auf letzter Seite
                pageNr++;
            else //Sonst zurück auf erste Seite
                pageNr = 0;
        }
        else if(keyManager.upEinmal() || keyManager.leftEinmal()) {//oben/links -> Scroll-Seite wird verringert
            if(pageNr > 0) //Wenn nicht auf erster Seite
                pageNr--;
            else //Sonst auf letzte Seite
                pageNr = ANZAHL_PAGES-1;
        }
        if(keyManager.escapeEinmal()) //Durch Drücken von ESC / BACKSPACE wird zum Hauptmenü zurückgekehrt
            setState(StateNames.MENU);
    }
}