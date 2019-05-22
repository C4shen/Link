import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
/**
 * Die zentrale Klasse des Programms. Hier wird die Anzeige und Funktionalität des Spiels verwaltet.
 * @author Cashen Adkins, Cepehr Bromand, Janni Röbbecke, Jakob Kleine, www.quizdroid.wordpress.com
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
    public static final long maxLoopTime = 1000 / FPS;
    
    /**
     * Die Höhe des Bereichs, in dem die HP, der Score usw. angezeigt wird
     */
    public static final int HP_BAR_HEIGHT = 100;
    
    /**
     * Die Breite des Spielfensters in Pixel
     */
    public static final int SCREEN_WIDTH = 10*TileSet.TILE_WIDTH; // Das Spiel hat eine Breite von 10 Tiles
    /**
     * Die Höhe des Spielfensters in Pixel
     */
    public static final int SCREEN_HEIGHT = 10*TileSet.TILE_HEIGHT + HP_BAR_HEIGHT; //Das Spiel hat erstmal eine Höhe von 10 Tiles und einem Platz für u.a. Leben und Punktzahl (10*64px + 100px = 740px).
    private Screen screen; //Der Screen, auf dem das Spiel visualisiert wird
    private boolean running = true; //Gibt an, ob das Spiel momentan läuft (beendet ggf. die Game-Loop)
    private KeyManager keyManager; //Der KeyManager, der die Eingaben über die Tastatur verwaltet.
    private Graphics g; //Die Graphics, mit denen die Figuren gemalt werden.
    
    /**
     * Startet ein neues Spiel
     * 
     * @author Cashen Adkins, Cepehr Bromand, www.quizdroid.wordpress.com
     * @since 0.01 (10.05.2019)
     */
    public static void main(String[] arg) 
    {
        //Es wird ein neues Objekt der Klasse Game erstellt.
        Game game = new Game();
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
        //Es werden zwei Attribute zur Überprüfung der vegrangegen Berechnungszeit erstellt.
        long timestamp;
        long oldTimestamp;
        //Es wird ein neues Fenster ertsellt mit dem Namen des Spiels als Titel und der Höhe und Breite der vorher angegebenen Attribute.
        screen = new Screen("LINK - Prototyp 1: Version 0.01", SCREEN_WIDTH, SCREEN_HEIGHT);
        keyManager = new KeyManager();
        screen.getFrame().addKeyListener(keyManager);
        State gameState = new GameState(this);
        State menuState = new MenuState(this);
        State.setState(gameState);
        //Solange das Spiel läuft wird die Gameloop wiederholt/ausgeführt. 
        while(running) 
        {
            //Die Zeit, vor Berechnung der neuen Werte, wird gespeichert.
            oldTimestamp = System.currentTimeMillis();
            //Überprüft ob gerade Escape gedrückt wird und ändert die State entweder von gameState zu menuState oder von menuState zu gameState.
            if(keyManager.escape())
            {
                if(State.getState()==gameState)
                {
                    State.setState(menuState);
                }
                else
                {
                    State.setState(gameState);
                }
            }
            //Die Update-Methode wird aufgerufen, die u.a. die Positionen der Spielcharaktere neu berechnet.
            update();
            //Die Zeit, nach Berechnung der neuen Werte, wird gespeichert.
            timestamp = System.currentTimeMillis();
            //Die benötigte Zeit für die Neuberechnung wird mit der maximal erlaubten Berechenzeit verglichen.
            if(timestamp-oldTimestamp <= maxLoopTime) {
                //Die berechneten Werte werden neu grafisch angezeigt.
                render();
                //Die benötigte Zeit für Updaten und Rendern wird berechnet.
                timestamp = System.currentTimeMillis();
                //Wenn das Rendern und Updaten schneller absolviert ist, als die maximal erlaubte Zeit, schläft der Thread für die restliche Zeit.
                if(timestamp-oldTimestamp <= maxLoopTime) {
                    try {
                        Thread.sleep(maxLoopTime - (timestamp-oldTimestamp) );
                    } 
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                //Falls die Berechnung zu lange dauert, wird die neu berechneten Werte zunächst nicht "gerendert", sondern die Schleife beginnt von vorne.
                System.out.println("Wir sind zu spät!");
            }
        }
    }
    
    /**
     * Aktualisiert die Spielmechanik durch Aufrufen der Update-Methode der momentanen State
     * @author Cashen Adkins, Jakob Kleine, www.quizdroid.wordpress.com
     * @since 0.01 (9.05.2019)
     */
    private void update() 
    {
        keyManager.update();
        State.getState().update();
    }
    
    /**
     * Holt vom Key-Manager ein, welche Beweguns-Tasten gedückt werden
     * @author Janni Röbbecke, Ares Zühlke, Cashen Adkins, www.quizdroid.wordpress.com
     * @since 0.02 (11.05.2019)
     * @return ein Punkt, der die Bewegung in x- und y-Richtung angibt
     */
    public Point getInput(){
        int xMove = 0;
        int yMove = 0;
        if(keyManager.up())
            yMove = -1;
        if(keyManager.down())
            yMove += 1; //+=, sodass keine Bewegung erfolgt, wenn beide Pfeile in gegensätzlicher Richtung gedrückt werden
        if(keyManager.left())
            xMove = -1;
        if(keyManager.right())
            xMove += 1; //+=, sodass keine Bewegung erfolgt, wenn beide Pfeile in gegensätzlicher Richtung gedrückt werden
        return new Point(xMove, yMove);
    }
    
    /**
     * Aktualisiert die Anzeige des Spiels
     * @author Cashen Adkins, Jakob Kleine, www.quizdroid.wordpress.com
     * @since 0.01 (9.05.2019)
     */
    private void render() 
    {
        State.getState().render(g);
    }
    
    public KeyManager getKeyManager()
    {
        return keyManager;
    }
    
    public Canvas getCanvas()
    {
        return screen.getCanvas();
    }
    
}