import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;

/**
 * Die zentrale Klasse des Programms. Hier wird die Anzeige und Funktionalität des Spiels verwaltet.
 * @author Cashen Adkins, Cepehr Bromand, Janni Röbbecke, Jakob Kleine, Ares Zühlke www.quizdroid.wordpress.com
 * @version 0.02 (12.05.2019)
 * @since 0.01 (09.05.2019)
 */
public class Game implements Runnable {
    /**
     * Die Bildfrequenz, die angibt, wie oft das Bild des Spieles innerhalb einer Sekunde aktualisiert wird (Frames per second).
     */
    public static final int FPS = 60;
    /**
     * Die maximale Zeit, die eine Berechnung der Loop dauern sollte/darf.
     */
    private static final long maxLoopTime = 1000 / FPS;
    
    /**
     * Die Breite des Spielfensters in Pixel
     */
    public static final int SCREEN_WIDTH = 10*TileSet.TILE_WIDTH; // Das Spiel hat eine Breite von 10 Tiles
    /**
     * Die Höhe des Spielfensters in Pixel
     */
    public static final int SCREEN_HEIGHT = 10*TileSet.TILE_HEIGHT + GameState.HP_BAR_HEIGHT; //Das Spiel hat ersteine Höhe von 10 Tiles und Platz für die HP-Bar (10*64px + 100px = 740px).
    
    private Screen screen; //Der Screen, auf dem das Spiel visualisiert wird
    private boolean running = true; //Gibt an, ob das Spiel momentan läuft (beendet ggf. die Game-Loop)
    private KeyManager keyManager; //Der KeyManager, der die Eingaben über die Tastatur verwaltet.
    
    /**
     * Startet ein neues Spiel
     * 
     * @author Cashen Adkins, Cepehr Bromand, www.quizdroid.wordpress.com
     * @since 0.01 (10.05.2019)
     */
    public static void main(String[] arg) 
    {
        Game game = new Game(); //Es wird ein neues Objekt der Klasse Game erstellt.
        //Es wird ein neuer Thread außerhalb des Main-Threads erstellt, in dem die run-Methode im Game-Objekt ausführt wird.
        new Thread(game).start();
    }
  
    /**
     * Enthält die Game-Loop, in der das Spiel dauerhaft durchlaufen wird, bis es beendet werden soll
     * 
     * @author Cashen Adkins, Cepehr Bromand, www.quizdroid.wordpress.com
     * @since 0.01 (10.05.2019)
     */
    @Override
    public void run() 
    {
        screen = new Screen("LINK - Legend of INformatik Kurs", SCREEN_WIDTH, SCREEN_HEIGHT);
        
        keyManager = new KeyManager();
        screen.getFrame().addKeyListener(keyManager);
        WindowListener screenListener = new WindowAdapter() { //Verwaltet das Schließen des Screens. 
            @Override
            public void windowClosing(WindowEvent e) {
                State.getState().endGame();
            }
        };
        screen.getFrame().addWindowListener(screenListener); 
        State.initialise(); //Lädt alle States und legt den Menü-State als Anfangsstate fest
        //Es werden zwei Attribute zur Überprüfung der vegrangegen Berechnungszeit deklariert.
        long timestamp;
        long oldTimestamp;
        //Solange das Spiel läuft wird die Gameloop wiederholt/ausgeführt. 
        while(running) 
        {
            oldTimestamp = System.currentTimeMillis(); //Die Zeit, vor Berechnung der neuen Werte, wird gespeichert.
            update(); //Zuerst wird die Spielmechanik aktualisiert
            timestamp = System.currentTimeMillis(); //Die Zeit, nach Berechnung der neuen Werte, wird gespeichert.
            
            if(timestamp-oldTimestamp <= maxLoopTime) { //Das aktuelle Bild wird nur dann angezeigt, wenn auch Zeit dafür ist.
                render(); //Die berechneten Werte werden angezeigt.
                timestamp = System.currentTimeMillis(); //Die benötigte Zeit für Updaten und Rendern wird berechnet.
                //Wenn das Rendern und Updaten schneller absolviert ist, als die maximal erlaubte Zeit, schläft der Thread für die restliche Zeit.
                if(timestamp-oldTimestamp <= maxLoopTime) {
                    try {
                        Thread.sleep(maxLoopTime - (timestamp-oldTimestamp));
                    } 
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //else: Falls die Berechnung zu lange dauert, wird die neu berechneten Werte zunächst nicht "gerendert", sondern die Schleife beginnt von vorne.
        }
    }
    
    /**
     * Aktualisiert die Spielmechanik durch Aufrufen der Update-Methode des momentanen States
     * @author Cashen Adkins, Jakob Kleine, www.quizdroid.wordpress.com
     * @since 0.01 (9.05.2019)
     */
    private void update() 
    {
        keyManager.update(); //Der KeyManager aktualisiert, welche Tasten im Moment gedrückt werden
        State.getState().update(keyManager); //Es wird derjenige State aktualisiert, in dem sich das Spiel gerade befindet
    }
    
    /**
     * Aktualisiert die Anzeige des Spiels
     * @author Cashen Adkins, Jakob Kleine, www.quizdroid.wordpress.com
     * @since 0.01 (9.05.2019)
     */
    private void render() 
    {
        BufferStrategy bs = screen.getCanvas().getBufferStrategy(); //Das Malen auf der Leinwand ist gebuffered
        if(bs == null) //Wenn es keine Buffer-Strategy gibt, muss diese erst erzeugt werden. In diesem Durchlauf wird nichts gerendert.
            screen.getCanvas().createBufferStrategy(3);
        else {
            Graphics g = bs.getDrawGraphics();
            g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT); //Die Leinwand wird geleert und im Folgenden mit dem aktuellen Bild bemalt
            
            State.getState().render(g); //Je nachdem, in welchem State sich das Spiel befindet, wird nun etwas anderes angezeigt
            
            bs.show(); //Das gebufferte Malen ist beendet; das Ergebnis wird jetzt angezeigt
            g.dispose(); //Die Graphics werden nicht mehr benötigt und verworfen
        }
    }
}