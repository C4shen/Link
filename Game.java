import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.awt.Font;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
     * Die Höhe des Bereichs, in dem die HP, der Score usw. angezeigt wird
     */
    public static final int HP_BAR_HEIGHT = 100;
    /**
     * Die Standard-Dicke der Wand (Border) des Spiel-Raums 
     */
    public static final int BORDER_WIDTH = 10;
    /**
     * Die Breite des Spielfensters in Pixel
     */
    public static final int SCREEN_WIDTH = 10*TileSet.TILE_WIDTH; // Das Spiel hat eine Breite von 10 Tiles
    /**
     * Die Höhe des Spielfensters in Pixel
     */
    public static final int SCREEN_HEIGHT = 10*TileSet.TILE_HEIGHT + HP_BAR_HEIGHT; //Das Spiel hat ersteine Höhe von 10 Tiles und Platz für die HP-Bar (10*64px + 100px = 740px).
    
    private Screen screen; //Der Screen, auf dem das Spiel visualisiert wird
    private boolean running = true; //Gibt an, ob das Spiel momentan läuft (beendet ggf. die Game-Loop)
    private KeyManager keyManager; //Der KeyManager, der die Eingaben über die Tastatur verwaltet.
    private Graphics g; //Die Graphics, mit denen die Figuren gemalt werden.
    private State currentState; 
    private GameState gameState;
    private MainMenuState mainMenuState;
    private TutorialState tutorialState;
    private HighscoresState highscoresState;
    private HighScoreManager scoreManager;
    private BufferedImage menuBackground;
    private String playerName;
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
        playerName = "Player101";
        screen = new Screen("LINK - Legend of INformatik Kurs", SCREEN_WIDTH, SCREEN_HEIGHT);
        try {
             menuBackground = ImageIO.read(Utils.absoluteFileOf("/res/tilesets/menuBackground.jpg")); //Der Rahmen wird gelesen und als BufferedImage gespeichert.
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        keyManager = new KeyManager();
        screen.getFrame().addKeyListener(keyManager);
        WindowListener screenListener = new WindowAdapter() { //Verwaltet das Schließen des Screens. 
            @Override
            public void windowClosing(WindowEvent e) {
                endGame();
            }
        };
        screen.getFrame().addWindowListener(screenListener);
        mainMenuState = new MainMenuState();
        tutorialState = new TutorialState();
        highscoresState = new HighscoresState();
        scoreManager = new HighScoreManager();
        currentState = mainMenuState;
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
                // System.out.println("Wir sind zu spät!");
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
        //Nur wenn das Spiel in einer State ist, wird die State-spezifische Methode aufgerufen
        if(currentState != null)
            currentState.update();
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
        currentState.render(g);
    }
    
    /**
     * Erzeugt einen neuen GameState und setzt alle Werte für die nächste Runde des Spiels zurück.
     * @author Jakob Kleine, Cashen Adkins, Janni Röbbecke
     * @since 25.05.2019
     */
    private void newGame() {
        gameState = new GameState();
        Coffee.resetSelectedAmount(); //Die Effekte des Kaffes verändern sich, je mehr Kaffee eingesammelt wurde. Das muss jetzt zurückgesetzt werden
        currentState = gameState;
    }
    
    /**
     * Setzt das Spiel nach einer Pause fort
     * @author Cashen Adkins, Janni Röbbecke
     * @since 25.05.2019
     */
    private void resumeGame() {
        if(gameState != null) 
            currentState = gameState;
    }
    
    /** 
     * Beendet das Spiel
     * @author Cashen Adkins, Jakob Kleine
     * @since 0.01 (26.05.2019)
     */
    private void endGame() {
        scoreManager.saveScores();
        System.exit(0);
    }
    
    /**
     * Stelllt die gewünschte Schriftart ein und sorg dafür, das die Schrift schöner gerendert wird
     * (muss nur einmalvor dem Rendern aufgerufen werden, danach kann setFond benutzt werden)
     * @author Jakob Kleine, Cashen Adkins
     * @param f gewünschte Schriftart
     */
    private void fontFestlegen(Graphics g, Font f) {
        Graphics2D g2d = (Graphics2D) g; //Damit RenderingHints gesetzt werden können, muss das Graphics-Objekt in ein Graphics2D-Objekt gecastet werden
        g2d.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING, //Text-Anti-Aliasing -> weichere Kanten
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
            RenderingHints.KEY_FRACTIONALMETRICS, //Fractional-Metrics -> konsistente Buchstabengröße
            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setFont(f);
    }
    
    /**
     * Ein State ist ein Zustand in dem sich das Programm befinden kann. 
     * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
     * @since 25.05.2019
     * 
     * [Hinweis: weil private innere Klassen/Interfaces mit allen ihren Methoden nicht in JavaDoc angezeigt werden, 
     *           wird javadoc bei den folgenden Klassen zwar angedeutet, aber nicht verwendet ]
     */
    private interface State
    {
        /**
         * Aktualisiert den State.
         * @author Cashen Adkins, Janni Röbbecke, www.quizdroid.wordpress.com
         * @since 0.01 (22.05.2019)
         */
        void update();
        
        /**
         * Rendert den State.
         * @author Cashen Adkins, Janni Röbbecke, www.quizdroid.wordpress.com
         * @since 0.01 (22.05.2019)
         */
        void render(Graphics g);
    }
    
    
    /**
     * Der GameState ist der State, in dem sich das Spiel während dem Spielen befindet. Hier werden die Figuen aktualisiert und neu gerendert
     * @author Cashen Adkins, Janni Röbbecke, Jakob Kleine, www.quizdroid.wordpress.com
     * @version 0.02 (28.05.2019)
     * @since 0.01 (22.05.2019)
     */
    private class GameState implements State 
    {
        private BufferedImage hpBarBackground;
        private int score; //Punkte (score) des Spielers
        private Player player; //Die Spielfigur des Spielers
        private LinkedList<Enemy> gegnerListe; //Eine Liste mit allen Gegnern im Spiel
        private LinkedList<Weapon> attackingWeapons; //Die Waffen, die sich gerade im Angriff befinden
        private LinkedList<Item> spawnedItems; //Die Items, die gespawnt aber noch nicht eingesammelt wurden
        private Rectangle[] roomBorders; //Die Wände des Raums
        private Room room; //Der Raum, der gerade gespielt wird
        private CollisionDetector collisionDet;
        
        private Constructor[] enemyConstructors; //Feld mit allen Konstruktoren für die verschiedenen Gegnertypen
        private int enemySpawnDelay; //Anzahl der Loosp die durchlaufen werden, bis der nächste Gegner gespawnt wird
        
        private Constructor[] itemConstructors; //Feld mit allen Konstruktoren für die verschiedenen Items
        private int itemSpawnDelay; //Anzahl der Loosp die durchlaufen werden, bis das nächste Item gespawnt wird
        /**
         * Ein neuer Gamestate wird erzeugt, in dem ein neues Spiel begonnen werden kann
         * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins, www.quizdroid.wordpress.com
         * @since 22.05.2019
         */
        public GameState()
        {
            player = new Player(320, 320);
            gegnerListe = new LinkedList<Enemy>();
            attackingWeapons = new LinkedList<Weapon>();
            spawnedItems = new LinkedList<Item>();
            spawnedItems.add(new CursorItem(Utils.random(10, 550), Utils.random(110, 650))); //Ein Cursor wird gespawnt, damit der Spieler von anfang an kämpfen kann
            
            //Das TileSet wird eingelesen und es wird ein Raum erzeugt, der aus diesem Tileset den Hintergrund des Spiels zusammensetzt
            TileSet tileSet = new TileSet("/res/tilesets/standard-raum-ts.png", 3 /*Anzahl Tiles x*/, 3/*Anzahl Tiles y*/, 3/*Abstand zwischen Tiles*/);
            room = new Room("/res/rooms/standard-raum.txt", tileSet);
                        
            //Grenzen, an denen der Raum Ended werden festgelegt
            roomBorders = new Rectangle[] {
                new Rectangle(               0,    HP_BAR_HEIGHT, SCREEN_WIDTH, BORDER_WIDTH), //links oben -> rechts oben
                new Rectangle(               0, SCREEN_HEIGHT-10, SCREEN_WIDTH, BORDER_WIDTH), //links unten -> rechts unten
                new Rectangle(               0,    HP_BAR_HEIGHT, BORDER_WIDTH, SCREEN_WIDTH), //links oben -> links unten
                new Rectangle( SCREEN_WIDTH-10,    HP_BAR_HEIGHT, BORDER_WIDTH, SCREEN_WIDTH)  //rechts oben -> rechts unten
            };
            try{
                hpBarBackground = ImageIO.read(Utils.absoluteFileOf("/res/tilesets/hpbarBackground.png"));
            }
            catch(IOException e) { e.printStackTrace(); }
            
            collisionDet = new CollisionDetector();
            
            //Die Konstruktoren der Gegner und Items werden gespeichert
            enemyConstructors = new Constructor[2];
            itemConstructors = new Constructor[3];
            try{
                enemyConstructors[0] = Class.forName("Virus").getConstructor(int.class, int.class);
                enemyConstructors[1] = Class.forName("SideEffect").getConstructor(int.class, int.class);

                itemConstructors[0] = Class.forName("CursorItem").getConstructor(int.class, int.class);
                itemConstructors[1] = Class.forName("Coffee").getConstructor(int.class, int.class);
                itemConstructors[2] = Class.forName("Pizza").getConstructor(int.class, int.class);
            } 
            catch(ClassNotFoundException e) { e.printStackTrace(); }
            catch(NoSuchMethodException e) { e.printStackTrace(); }
        }   
        
        /**
         * Zeichnet das Spielfeld und alle Figuren und Gegenstände darauf
         * @author Jakob Kleine, Janni Röbbecke, Ares Zülke, Cepehr Bromand
         * @since 10.05.2019
         */
        @Override
        public void render(Graphics g) 
        {
            Canvas c = screen.getCanvas();
            //c.setBackground(Color.blue);
            BufferStrategy bs = c.getBufferStrategy();
            if(bs == null)
                //sollte die Canves noch keine BufferStrategy haben wird auf ihr eine Erzeugt, die im nächsten Loop benutzt der den kann.
                c.createBufferStrategy(3);
            else{
                Color color = new Color(255,255,255); //Eine Farbe wird festgelegt
                c.setBackground(color); //Farbe wird als Hintergrundfarbe dargestellt
                g = bs.getDrawGraphics();
                //Der gesamte Screen wird geleert
                g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                g.drawImage(hpBarBackground, 0, 0, null);
                room.renderMap(g); // Erst die Spielfläche ...
                player.render(g); // ... und darauf die Spielfigur
                for(Enemy e :  gegnerListe)
                    e.render(g);
                for(Item i : spawnedItems) 
                    i.render(g);
                
                //Am oberen Bildrand werden die Lebenspunkte und der Score des Spielers angezeigt
                fontFestlegen(g, new Font("American Typewriter", Font.BOLD, 40)); 
                g.setColor(new Color(215, 7, 7));
                g.drawString("HP: "+player.health, 10, (HP_BAR_HEIGHT+40)/2);
                g.setColor(new Color(204, 146, 12));
                g.drawString(""+score, 500, (HP_BAR_HEIGHT+40)/2);
                
                //Nun wird das neue Bild angezeigt
                bs.show();
                g.dispose();
            }
        }
        
        /**
         * Berechnet die neuen Positionen und Werte aller Figuren und Gegenstände auf dem Spielfeld
         */
        @Override
        public void update() 
        {
            player.reciveKeyInput(getInput()); //Bewegt den Spieler entsprechend der Eingabe über die Tasten
            if(keyManager.attack()) { //Wenn die Taste zum Angriff gedrückt wurde, greift der Spieler an
                Weapon attackingWeapon = player.startAttack();
                if(attackingWeapon != null) //Wenn ein neuer Angriff ausgeführt wurde
                    attackingWeapons.add(attackingWeapon); //Speichert die Waffe, um Kollisionen mit Gegnern zu prüfen
            }
            player.update();
            
            LinkedList<Enemy> nichtMehrLebendeGegner = new LinkedList<Enemy>(); //Um eine ConcurrentModificationException zu verhindern, werden die Elemente erst nach der Iteration über die Liste entfernt
            LinkedList<Weapon> waffenGestorbenerGegner = new LinkedList<Weapon>(); 
            for(Enemy e : gegnerListe) {
                if(e.isAlive()){
                    //Sollten die Gegner noch leben, werden sie upgedatet. Wenn sie in diesem Loop einen Angriff starten, wird die Waffe gespeichert, mit der sie dies tun
                    Weapon enemyWeapon = e.target(player);
                    if(enemyWeapon!=null) 
                        attackingWeapons.add(enemyWeapon);
                    e.update();
                }
                else {
                    //Sollten die Gegner im letzten Loop gestorben sein werden sie und hiehre Waffen aus den entsprechenden Listen gelöscht
                    score += e.getScoreValue();
                    if(e.weapon != null && e.weapon.isAttacking()) 
                        waffenGestorbenerGegner.add(e.weapon);
                    nichtMehrLebendeGegner.add(e);
                }
            }
            //Die Gegener und Waffen, die "zu löschende Gegner bzw. Waffen" gespeichert wurden werden nun aus den Listen gelöscht 
            attackingWeapons.removeAll(waffenGestorbenerGegner);
            gegnerListe.removeAll(nichtMehrLebendeGegner);
            
            //Auf die gleiche weise werden alle Items auf dem Spielfeld durchgegangen 
            LinkedList<Item> verfalleneItems = new LinkedList<Item>(); 
            for(Item i : spawnedItems) {
                if(i.exists()){
                    //Sollte das Item noch existieren wird es upgedatet.
                    i.update();
                }
                else {
                    //Sonst wird es in der Liste mit den "zu löschenden Items gespeichert"
                    verfalleneItems.add(i);
                }
            }
            //Alle Items, die als verfallene Items gespeichert wurden, werden aus der Liste gelöscht
            spawnedItems.removeAll(verfalleneItems);
                
                
            collisionDet.update();
            
            if(enemySpawnDelay-- <= 0) {
                //Sollte das enemySpawnDelay abgelaufen sein wird ein neuer Gegner auf dem Fels geswnt und ein neues SpawnDelay wird Zufällig festgelegt
                spawnRandomEnemy(); 
                enemySpawnDelay = Utils.random(100, 1000);
            }
            
            if(itemSpawnDelay-- <= 0) {
                //Sollte das itemSpawnDelay abgelaufen sein wird ein neues Item auf dem Fels geswnt und ein neues SpawnDelay wird Zufällig festgelegt
                spawnRandomItem(); 
                itemSpawnDelay = Utils.random(100, 1000);
            }
            
            if(!player.isAlive()) {
                //Sollte der Spieler keine Lebenspunkte mehr haben wird sein Socre im scoreManeger festgehalten und es wird zum Hauprmenü gewechselt
                scoreManager.addScore(new Score(score,playerName));
                gameState = null;
                currentState = mainMenuState;
            }
            
            //Wenn Escape gedrückt wird, ändert sich die State in die MenuState
            if(keyManager.escapeEinmal()) {
                mainMenuState.setMenuItem(1);
                currentState = mainMenuState;
            }
        }
        
        /**
         * Spawnt einen zufälligen Gegener an einer zufälligen Stelle auf dem Spielfleld
         * @author Jakob Kleine, Janni Röbbecke 
         * @since 27.05.2019
         */
        private void spawnRandomEnemy() {
            try {
                gegnerListe.add((Enemy) enemyConstructors[Utils.random(0,1)].newInstance(Utils.random(10, 550), Utils.random(110, 650)));
            }
            catch(InstantiationException e) { e.printStackTrace(); }
            catch(IllegalAccessException e) { e.printStackTrace(); }
            catch(IllegalArgumentException e) { e.printStackTrace(); }
            catch(InvocationTargetException e) { e.printStackTrace(); }
        }
        
        /**
         * Spawnt einen zufälligen Gegener an einer zufälligen Stelle auf dem Spielfleld
         * @author Jakob Kleine, Janni Röbbecke 
         * @since 27.05.2019
         */
        private void spawnRandomItem() {
            try {
                spawnedItems.add((Item) itemConstructors[Utils.random(0,2)].newInstance(Utils.random(10, 550), Utils.random(110, 650)));
            }
            catch(InstantiationException e) { e.printStackTrace(); }
            catch(IllegalAccessException e) { e.printStackTrace(); }
            catch(IllegalArgumentException e) { e.printStackTrace(); }
            catch(InvocationTargetException e) { e.printStackTrace(); }
        }
        
        /*
         * Zuständig, um die Entitäten auf dem Spielfeld auf Kollisionen zu überprüfen, und diese zu verwalten.
         * Es wäre auch möglich, die Methoden direkt in die Klasse GameState zu schreiben, mit dieser inneren Klasse
         * wird allerdings der Kollision-Teil separiert, um die Kohäsion und Übersichtlichkeit zu verbessern.
         * 
         * Im CollisionDetector werden folgende Kollsionen ausgewertet:
         *      - Kollision von angreifenden Waffen mit Gegnern (wenn freundliche Waffe) oder Spieler (wenn unfreundliche Waffe)
         *          -> Getroffene Entität wird verletzt und Waffe wird benachrichtigt, dass sie getroffen hat.
         *      - Kollision von Spieler mit den gespawnten Items
         *          -> Item wird eingesammelt und benachrichtigt, dass es den Spieler beeinflussen soll
         *      - Kollision mit angreifenden Waffen, Gegnern und dem Spieler und den Rahmen des Raums
         *          -> Entität wird zum Rand des Raums zurückverschoben
         * @author Jakob Kleine, Janni Röbbecke, Cepehr Bromand, Ares Zühlke; inspiriert von https://www.youtube.com/watch?v=BTDcR4smi5A
         * @since 24.05.2019
         */
        private class CollisionDetector {
            /*
             * Aktualisiert den CollisionDetector, der daraufhin alle Entitäten auf dem Spielfeld auf Kollision überprüft und 
             * diese Kollision auswertet.
             * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke, Janni Röbbecke
             */
            public void update() {
                LinkedList<Weapon> nichtMehrAttackierendeWaffen = new LinkedList<Weapon>(); //Um eine ConcurrentModificationException zu verhindern, werden die Elemente erst nach der Iteration über die Liste entfernt
                for(Weapon w : attackingWeapons) { //Die Waffen werden hier duchgegangen und nicht im GameState selbst, weil sie auf Kollision geprüft werden müssen.
                    if(!w.isAttacking())
                        nichtMehrAttackierendeWaffen.add(w);
                    else {//Auf Kollision prüfen
                        if(w.isFriendly()){ //Wenn die Waffe freundlich ist, muss sie nur auf Kollision mit den Gegnern geprüft werden
                            LinkedList<Entity> getroffeneGegner = collidesWith(w, gegnerListe);
                            for(Entity e : getroffeneGegner) {
                                ((Enemy) e).startBeingAttacked(w); //Hier kann ohne try-catch-Block gecastet werden, weil bekannt ist, dass alle Objekte in der Liste Enemy-Objekte sind.
                                w.notifySuccess(); //Der Waffe mitteilen, dass sie getroffen hat.
                            }
                        }
                        else { //Sonst muss sie nur auf Kollision mit dem Spieler geprüft werden. Sie schadet nur ihm
                            if(collision(w, player)){
                                player.startBeingAttacked(w); 
                                w.notifySuccess();
                            }
                        }
                    }
                }
                attackingWeapons.removeAll(nichtMehrAttackierendeWaffen);
                
                
                LinkedList<Item> eingesammelteItems = new LinkedList<Item>(); //Um eine ConcurrentModificationException zu verhindern, werden die Elemente erst nach der Iteration über die Liste entfernt
                for(Item i : spawnedItems) {
                    if(collision(i, player)){
                        i.affect(player);
                        eingesammelteItems.add(i);
                    }
                }
                spawnedItems.removeAll(eingesammelteItems);
                
                //Jetzt muss sichergestellt werden, dass kein Element aus dem Spielfeld geworfen wurde.
                keepInside(player);
                for(Weapon w : attackingWeapons)
                    keepInside(w);
                for(Enemy e : gegnerListe)
                    keepInside(e);
            }
        
            /*
             * Hält eine bewegbare Entittät, die eventuell aus dem Spielfeld gelaufen ist, innerhalb des Spielfelds
             * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
             * @since 24.05.2019
             * @param e die bewegbare Entität, für die sichergestellt werden soll, dass sie nicht mit den Mauern am Rand kollidiert
             */
            private void keepInside(Movable e) {
                int xNeu = -1;
                int yNeu = -1;
                if(collision(e, roomBorders[0])) //Border Oben
                    yNeu = Game.HP_BAR_HEIGHT + BORDER_WIDTH; //Positioniert y am oberen Rand
                else if(collision(e, roomBorders[1])) //Border Unten
                    yNeu = Game.SCREEN_HEIGHT - (int) e.getHitbox().getHeight() - BORDER_WIDTH; //Positioniert y am unteren Rand
                //Kein else hier, weil auch 2 Borders getroffen werden können
                if(collision(e, roomBorders[2])) //Border Links
                    xNeu = BORDER_WIDTH; //Positioniert x am linken Rand
                else if(collision(e, roomBorders[3])) //Border Rechts
                    xNeu = Game.SCREEN_WIDTH - (int) e.getHitbox().getWidth() - BORDER_WIDTH; //Positioniert x am rechten Rand
                    
                if(xNeu != -1) //Wenn die Position geändert werden soll
                    e.setEntityX(xNeu);
                if(yNeu != -1) 
                    e.setEntityY(yNeu);
                    
                //Wenn mind. eine Position geändert wurde, soll das (wenn vorhanden) Knockback zurückgesetzt werden, weil sonst die Kreatur immer wieder gegen die Wand geworfen wird
                if(xNeu != -1 || yNeu != -1) { 
                    if(e instanceof Creature) //Nur Kreaturen haben Knockback
                        ((Creature) e).resetKnockback();
                }
            }
        
            /*
             * Überprüft, ob zwei Entitäten miteinander kollidieren.
             * @author Jakob Kleine, Janni Röbbecke, Cepehr Bromand, Ares Zühlke
             * @since 24.05.2019
             * @param e1 die eine zu überprüfende Entität
             * @param e2 die andere zu überprüfende Entität
             * @return true, wenn sich die Hitboxen von e1 und e2 schneiden, sonst false
             */
            private boolean collision(Entity e1, Entity e2) {
                return collision(e1, e2.getHitbox());
            }
        
            /*
             * Überprüft, ob eine Entität mit der Hitbox eines Elements auf dem Spielfeld kollidiert.
             * @author Jakob Kleine, Janni Röbbecke, Cepehr Bromand, Ares Zühlke
             * @since 24.05.2019
             * @param e1 die eine zu überprüfende Entität
             * @param e2 die Hitbox eines Elements auf dem Spielfeld (z.B. Entity oder Border)
             * @return true, wenn sich die Hitbox von e1 mit e2 schneidet, sonst false
             */
            private boolean collision(Entity e1, Rectangle hitboxE2) {
                return e1.getHitbox().intersects(hitboxE2);
            }
            
            /*
             * Gibt eine Liste mit allen Entitäten aus der angegebenen Liste zurück, mit denen die Angegebene Entität kollidiert
             * @author Jakob Kleine, Janni Röbbecke, Cepehr Bromand, Ares Zühlke
             * @param e die Entität, die auf Kollision geprüft wird
             * @param es eine Liste mit Entitäten, die auf Kollision mit e geprüft werden 
             * @returns eine Liste mit allen Entitäten aus es, mit denen e kollidiert
             */
            public LinkedList<Entity> collidesWith(Entity e, LinkedList es) {
                LinkedList<Entity> collidedEntities = new LinkedList<Entity>();
                for(Object object : es) {
                    /*
                     * Aus einem uns nicht verständlichem Grund, ist es nicht möglich Entity als Typ-Parameter
                     * der LinkedList anzugeben, und dann z.B. eine List<Enemy> zu übergeben, weil diese nicht in eine List<Entity> convertiert werden könne,
                     * also wird eine "rohe" LinkedList verwendet, weswegen die Objekte gecastet werden müssen
                     */
                    Entity ex = (Entity) object; 
                    if(collision(e, ex))
                        collidedEntities.add(ex);
                }
                return collidedEntities;
            }
        }
    }
    
    /**
     * Der Break-Menu-State ist der State, in dem sich das Spiel befindet, wenn eine Pause gemacht wird, oder wenn das Spiel gestartet wird.
     * @author Cashen Adkins, Janni Röbbecke, Jakob Kleine, www.quizdroid.wordpress.com
     * @version 0.03 (26.05.2019)
     * @since 0.01 (22.05.2019
     */
    public class MainMenuState implements State 
    {
        private static final int ANZAHL_MENU_ITEMS = 6; //Die Anzahl an Items in dem Menü
        private int menuItem; //Variable, die speichert, bei welchem Menüpunkt sich der Spieler gerade befindet.
        private BufferedImage menuItemFrame; //Der Rahmen, der sich um den ausgewählten Menüpunkt befindet.
        private Font font; //Das Font, welches für den Text in den Buttons benutzt wird.
        private Color color; //Die Farbe, in der der Text geschrieben werden soll
        /*
         * Erstellt einen neuen Menu-State.
         * @author Cahsen Adkins, Janni Röbbecke, www.quizdroid.wordpress.com
         * @since 22.05.2019
         */
        public MainMenuState() {
            menuItem = 0; //Zu Beginn des Menüs ist der ausgewählte Knopf der erste.
            try {
                 menuItemFrame = ImageIO.read(Utils.absoluteFileOf("/res/tilesets/menuitemframe.png")); //Der Rahmen wird gelesen und als BufferedImage gespeichert.
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
            font = new Font("Futura", Font.BOLD, 40);
            color = new Color(65, 41, 31);
        }
        
        /*
         * @author Cashen Adkins, Jakob Kleine, Janni Röbbecke, www.quizdroid.wordpress.com
         * @since 22.05.2019
         */
        @Override
        public void render(Graphics g) {
            screen.getCanvas().setBackground(Color.white); //Weiß wird als Hintergrundfarbe dargestellt
            BufferStrategy bs = screen.getCanvas().getBufferStrategy(); 
            if(bs == null)
                screen.getCanvas().createBufferStrategy(3);
            else
            {
                g = bs.getDrawGraphics();
                //Clear Screen
                g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                g.drawImage(menuBackground, 0, 0, null);
                fontFestlegen(g,font);
                g.setColor(color);
                g.drawString("Neues Spiel", SCREEN_WIDTH/2-120, 200);
                if(gameState == null) { //Wenn der Game-State null ist, kann gerade nicht weitergespielt werden, also wird das Menüitem dafür heller gemalt
                    g.setColor(color.brighter());
                    g.drawString("Weiterspielen", SCREEN_WIDTH/2-120, 280);
                    g.setColor(color);
                }
                else //Sonst wird es ganz normal angezeigt
                    g.drawString("Weiterspielen", SCREEN_WIDTH/2-120, 280);
                g.drawString("Anleitung", SCREEN_WIDTH/2-120, 360);
                g.setColor(color.brighter()); //Die Optionen haben wir noch nicht implementiert, also werden sie heller angezeigt
                g.drawString("Optionen", SCREEN_WIDTH/2-120, 440);
                g.setColor(color); //Der Rest 
                g.drawString("Bestenliste", SCREEN_WIDTH/2-120, 520);
                g.drawString("Beenden", SCREEN_WIDTH/2-120, 600);
                g.drawImage(menuItemFrame, SCREEN_WIDTH/2-190, 140 + menuItem * 80, 384, 96, null); //Zeichnet den Rahmen um das ausgewählte Item
                bs.show();
                g.dispose();
            }
        }
           
        /*
         * @author Cashen Adkins, Jakob Kleine, Janni Röbbecke
         * @since 27.05.2019
         */
        @Override
        public void update() 
        {
            if(keyManager.downEinmal()) {
                if(menuItem < ANZAHL_MENU_ITEMS-1) //Wenn nicht beim letzten Item -> zum nächsten
                    menuItem++;
                else //Sonst: zum ersten
                    menuItem = 0; 
            }
            else if(keyManager.upEinmal()) {
                if(menuItem > 0) //Wenn nicht beim ersten Item -> zum vorherigen
                    menuItem--;
                else
                    menuItem = ANZAHL_MENU_ITEMS-1; //Sonst: zum letzten Item
            }
            else if(keyManager.attackEinmal()) { //Enter oder Backspace -> Das ausgewählte MenüItem soll "angeclickt" werden
                switch(menuItem) {
                    case 0:
                        newGame();
                    break;
                    case 1: 
                        resumeGame();
                    break; 
                    case 2:
                        currentState = tutorialState;
                    break;
                    case 3: 
                        //hier müsste zum Optionen-State gewechselt werden; so weit sind wir allerdings nicht gekommen
                    break;
                    case 4: 
                        currentState = highscoresState; 
                    break;
                    default: //Das letzte Item ist das Beenden-Item
                        endGame();
                }
            }
            
            if(keyManager.escapeEinmal()) //Escpae oder Backspace -> weiterspielen wenn möglich
                resumeGame();
        }
        
        /* 
         * Ändert das ausgewählte Menü-Item. Aufgerufen, wenn das Spiel pausiert wird, sodass
         * automatisch das Menü-Item "Weiterspielen" ausgewählt wird.
         * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
         * @since 28.05.2019
         */
        public void setMenuItem(int item) {
            if(item >= 0 && item < ANZAHL_MENU_ITEMS)
                menuItem = item;
        }
    }
    /**
     * Der Tutorial-State ist der State, in dem sich das Spiel befindet, wenn die Anleitung aufgerufen wird.
     * @author Ares Zühlke, Cepehr Bromand, Jakob Kleine
     * @version 0.03 (28.05.2019)
     * @since 0.01 (22.05.2019
     */
    public class TutorialState implements State
    {
        private static final int ANZAHL_PAGES = 2; //Die Anzahl der Seiten in der Anleitung
        private Font überschrift; //Ein Font für Überschriften
        private Font unterüberschrift; //Ein Font für Unterüberschriften im Text
        private Font standardSchrift; //Ein Font für regulären Text
        private Color schriftFarbe; 
        private int pageNr; //Die Seitennummer, bei der man sich gerade in der Anleitung befindet.
        /*
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
        
        /*
         * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
         * @since 22.05.2019
         */
        public void render(Graphics g)
        {
            screen.getCanvas().setBackground(Color.white); //Weiß wird als Hintergrundfarbe dargestellt
            BufferStrategy bs = screen.getCanvas().getBufferStrategy(); 
            if(bs == null)
                screen.getCanvas().createBufferStrategy(3);
            else
            {
                g = bs.getDrawGraphics();
                //Clear Screen
                g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                g.drawImage(menuBackground, 0, 0, null);
                fontFestlegen(g,überschrift);
                g.setColor(schriftFarbe.darker().darker()); //Eine dunklere Schriftfarbe ist hier leichter zu lesen.
                /* 
                 * Speichert die Zeilenhöhe, sodass beim einfügen von Text die Zeilenhöhe automatisch angepasst wird, indem bei jedem Anzeigen von Text die Zeilenhöhe
                 * um den gewünschten Abstand nach oben inkrementiert wird.
                 */
                int zeilenHoehe = 100; 
                Utils.centerText(g, "ANLEITUNG", SCREEN_WIDTH, zeilenHoehe);
                switch(pageNr) { //Hier wird die jeweils anzuzeigende Seite gerendert. Das wird nicht genauer kommentiert, das Ergebnis ist im Spiel einzusehen
                    case 0: //Seite 1:
                            g.setFont(unterüberschrift);
                            Utils.centerText(g, "Ziel des Spiels", SCREEN_WIDTH, zeilenHoehe+=40); 
                            g.setFont(standardSchrift); //Font-Festlegen muss nur einmal aufgerufen werden; dann sind die RenderingHints für Text bereits gesetzt
                            g.drawString("In diesem Informatik-orientiertem Spiel geht es darum einen möglichst", 40, zeilenHoehe+=35);
                            g.drawString("großen Score zu erzielen, indem man Gegner besiegt, die auf dem ", 40, zeilenHoehe+=20);
                            g.drawString("Spielfeld erscheinen und Items einsammelt, die einem dabei.", 40, zeilenHoehe+=20);
                            g.drawString("helfen.", 40, zeilenHoehe+=20);
                            g.setFont(unterüberschrift);
                            Utils.centerText(g, "Steuerung", SCREEN_WIDTH, zeilenHoehe+=40);
                            g.setFont(standardSchrift);
                            g.drawString("Die Spielfigur wird mit den Tasten W (oben), A (links), S (unten)", 40, zeilenHoehe+=40);
                            g.drawString("und D  (rechts) oder den Pfeiltasten in die entsprechende Richtung", 40, zeilenHoehe+=20);
                            g.drawString("bewegt.", 40, zeilenHoehe+=20);
                            g.drawString("Ein Angriff wird mit Leertaste oder Enter gestartet und erfolgt", 40, zeilenHoehe+=20);
                            g.drawString("immer in Blickrichtung der Spielfigur. Während dem Angreifen kann", 40, zeilenHoehe+=20);
                            g.drawString("sich die Spielfigur nicht bewegen.", 40, zeilenHoehe+=20);
                            g.setFont(unterüberschrift);
                            Utils.centerText(g, "Gegner", SCREEN_WIDTH, zeilenHoehe+=40);
                            g.setFont(standardSchrift);
                            g.drawString("Auf dem Spielfeld werden in zufälligen Abständen zufällige Gegner auf", 40, zeilenHoehe+=40);
                            g.drawString("zufälligen Feldern gespawnt. Sie fügen der Spielfigur Schaden zu und", 40, zeilenHoehe+=20);
                            g.drawString("werfen sie ein Stück zurück (geben Knockback).", 40, zeilenHoehe+=20);
                            g.drawString("Bisher gibt es folgende Gegener:", 40, zeilenHoehe+=20);
                            g.drawString("Seiten-Effekt: Der Seiten-Effekt (dargestellt durch einen Krebs) läuft", 40, zeilenHoehe+=30);
                            g.drawString("ausschließlich seitlich und greift bei Berührung mit der Spielfigur mit", 40, zeilenHoehe+=20);
                            g.drawString("seinen Scheren an.", 40, zeilenHoehe+=20);
                            g.drawString("Virus: Der Virus ist verfolgt die Spielfigur. Er fügt ihr Schaden zu,", 40, zeilenHoehe+=30);
                            g.drawString("indem er seine Waffe (momentan ein Cursor) auf sie wirft.", 40, zeilenHoehe+=20);
                            g.drawString("Momentan erfolgt der Angriff des Virus nur nach oben und unten.", 40, zeilenHoehe+=20);
                    break;
                    case 1: //Seite 2:
                            g.setFont(unterüberschrift);
                            Utils.centerText(g, "Items", SCREEN_WIDTH, zeilenHoehe+=40);
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
                            Utils.centerText(g, "Score", SCREEN_WIDTH, zeilenHoehe+=40);
                            g.setFont(standardSchrift);
                            g.drawString("Das Töten von Gegnern bringt Punkte ein. Jeder Gegner-Typ bringt", 40, zeilenHoehe+=40);
                            g.drawString("dabei eine eigene Anzahl an Score-Punkten. ", 40, zeilenHoehe+=20);
                            g.drawString("Die 10 besten Scores werden gespeichert und können im Menü", 40, zeilenHoehe+=20);
                            g.drawString("unter dem Reiter Bestenliste eingesehen werden. Zusätzlich zu", 40, zeilenHoehe+=20);
                            g.drawString("der erreichten Punktzahl wird dabei auch der Name des Spielers", 40, zeilenHoehe+=20);
                            g.drawString("und das Datum des Spiels gespeichert. Das Ändern des gespeicherten", 40, zeilenHoehe+=20);
                            g.drawString("Namens ist allerdings momentan nicht möglich.", 40, zeilenHoehe+=20);
                    break;
                }

                g.setFont(standardSchrift.deriveFont(Font.ITALIC, 15)); 
                String hinweis = "Seite "+(pageNr+1)+"; Blättern mit w&s / a&d oder Pfeiltasten. Zum Hauptmenü: bitte ESCAPE drücken.";
                Utils.centerText(g, hinweis, SCREEN_WIDTH, SCREEN_HEIGHT-15);
                bs.show();
                g.dispose();
            }
        }
        /*
         * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
         * @since 28.05.2019
         */
        public void update()
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
            if(keyManager.escapeEinmal()) //Escape/Backspace -> zurück ins Hauptmenü
                currentState = mainMenuState;
        }
    }
    
    /*
     * Der HighscoresState ist der State, in dem sich das Spiel befindet, wenn die Highscores angezeigt werden. 
     * Dann werden die Highscores vom HighscoreManager abgefragt und angezeigt.
     */
    private class HighscoresState implements State {
        private Font basisFont; 
        public HighscoresState() {
            basisFont = new Font("Futura", Font.BOLD, 40);
        }
        
        /*
         * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
         * @since 26.05.2019
         */
        public void update() {
            if(keyManager.escapeEinmal()) //Durch Drücken von ESC / BACKSPACE wird zum Hauptmenü zurückgekehrt
                currentState = mainMenuState;
        }
        
        /*
         * @author Jakob Kleine, Janni Röbbecke
         * @since 26.05.2019
         */
        public void render(Graphics g) {
            screen.getCanvas().setBackground(Color.white); //Weiß wird als Hintergrundfarbe festgelegt
            BufferStrategy bs = screen.getCanvas().getBufferStrategy(); 
            if(bs == null)
                screen.getCanvas().createBufferStrategy(3);
            else
            {
                g = bs.getDrawGraphics();
                //Clear Screen
                g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                g.drawImage(menuBackground, 0, 0, null);
                fontFestlegen(g,basisFont.deriveFont(Font.BOLD));
                g.setColor(new Color(65, 41, 31));
                
                Utils.centerText(g, "BESTENLISTE", SCREEN_WIDTH, 100);
                
                g.setFont(basisFont.deriveFont(Font.PLAIN, 30));
                ArrayList<Score> scores = scoreManager.getScores();
                for(int i=0; i<scores.size(); i++) {
                    /*
                     * (i<9?"0":"") -> Wenn die Score-Nr. 9 ist wird eine 0 vorher angefügt.
                     * (i+1)+": "+scores.get(i) -> [ScoreNr.]: [ScoreWert - ScoreName (ScoreDatum)]
                     * (175+55*i) -> Der erste Score wird bei y=175 platziert, alle weiteren in einem Abstand von 55px
                     */
                    g.drawString((i<9?"0":"")+(i+1)+": "+scores.get(i), 40 /*x-Position*/, 175+55*i);
                }
               
                g.setFont(basisFont.deriveFont(Font.ITALIC, 15)); 
                String hinweis = "Um zum Hauptmenü zurückzukehren bitte ESCAPE drücken.";
                Utils.centerText(g, hinweis, SCREEN_WIDTH, SCREEN_HEIGHT-15);
                bs.show();
                g.dispose();
            }
        }
    }
}