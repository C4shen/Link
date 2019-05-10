/**
 * @author Cashen Adkins, Janni Röbbecke, quizdroid.wordpress.com
 * @version 0.01 10.05.2019
 */
public class Game implements Runnable {
    /**
     * Dieses Attribut speichert die Bildfrequenz, die angibt, wie oft das Bild des Spieles innerhalb einer Sekunde aktualisiert wird (Frames per second).
     */
    public static final int FPS = 60;
    /**
     * Dieses Attribut speichert die maximale Zeit, die eine Berechnung der Loop dauern sollte/darf.
     */
    public static final long maxLoopTime = 1000 / FPS;
    /**
     * Dieses Attribut speichert die Breite des Spielfensters in Pixel.
     */
    public static final int SCREEN_WIDTH = 640; // Das Spiel hat eine Breite von 10 Tiles (10*64px = 640px).
    /**
     * Dieses Attribut speichert die Höhe des Spielfensters in Pixel.
     */
    public static final int SCREEN_HEIGHT = 740; //Das Spiel hat erstmal eine Höhe von 10 Tiles und einem Platz für u.a. Leben und Punktzahl (10*64px + 100px = 740px).
    /**
     * Attribut, das angiebt, ob das Spiel momentan läuft.
     */
    public boolean running = true;
    public static void main(String[] arg) 
    {
        //Es wird ein neues Objekt der Klasse Game erstellt.
        Game game = new Game();
        //Es wird ein neues Fenster ertsellt mit dem Namen des Spiels als Titel und der Höhe und Breite der vorher angegebenen Attribute.
        new Screen("LINK", SCREEN_WIDTH, SCREEN_HEIGHT);
        //Es wird ein neues Thread unabhängig von der Main-Thread erstellt, dass die run-Methode im Objekt "game" ausführt.
        new Thread(game).start();
    }
  
    public void run() 
    {
        //Es werden zwei Attribute zur Überprüfung der Berechnungszeit erstellt.
        long timestamp;
        long oldTimestamp;
        //Solange das Spiel läuft wird die Gameloop wiederholt/ausgeführt. 
        while(running) 
        {
            //Die Zeit, vor Berechnung der neuen Werte, wird gespeichert.
            oldTimestamp = System.currentTimeMillis();
            //Die Update-Methode wird aufgerufen, die u.a. die Positionen der Spielcharaktere neu berechnet.
            update();
            //Die Zeit, nach Berechnung der neuen Werte, wird gespeichert.
            timestamp = System.currentTimeMillis();
            //Die benötigte Zeit für die Neuberechnung wird mit der maximal erlaubten Berechenzeit verglichen.
            if(timestamp-oldTimestamp <= maxLoopTime) 
            {
                //Die berechneten Werte werden neu grafisch angezeigt.
                render();
                //Die benötigte Zeit für Updaten und Rendern wird berechnet.
                timestamp = System.currentTimeMillis();
                System.out.println(maxLoopTime + " : " + (timestamp-oldTimestamp));
                //Wenn das Rendern und Updaten schneller absolviert ist, als die maximal erlaubte Zeit, schläft der Thread für die restliche Zeit.
                if(timestamp-oldTimestamp <= maxLoopTime) 
                {
                    try 
                    {
                        Thread.sleep(maxLoopTime - (timestamp-oldTimestamp) );
                    } 
                    catch (InterruptedException e) 
                    {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
               //Falls die Berechnung zu lange dauert, wird die neu berechneten Werte zunächst nicht "gerendert", sondern die Schleife beginnt von vorne.
                System.out.println("Wir sind zu spät!");
            }
        }
    }
    static void update() 
    {
    }
    void render() 
    { }
}