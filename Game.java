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
    public static final int SCREEN_HEIGHT = 10*TileSet.TILE_HEIGHT + HP_BAR_HEIGHT; //Das Spiel hat ersteine Höhe von 10 Tiles und Platz für die HP-Bar (10*64px + 100px = 740px).
    private Screen screen; //Der Screen, auf dem das Spiel visualisiert wird
    private boolean running = true; //Gibt an, ob das Spiel momentan läuft (beendet ggf. die Game-Loop)
    private KeyManager keyManager; //Der KeyManager, der die Eingaben über die Tastatur verwaltet.
    private Graphics g; //Die Graphics, mit denen die Figuren gemalt werden.
    private State currentState;
    private GameState gameState;
    private MainMenuState mainMenuState;
    private HighscoresState highscoresState;
    private HighScoreManager scoreManager;
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
        //Es wird ein neues Fenster ertsellt mit dem Namen des Spiels als Titel und der Höhe und Breite der vorher angegebenen Attribute.
        WindowListener screenListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                endGame();
            }
        };
        playerName = "Player101";
        screen = new Screen("LINK - Legend of INformatik Kurs", SCREEN_WIDTH, SCREEN_HEIGHT, screenListener);
        keyManager = new KeyManager();
        screen.getFrame().addKeyListener(keyManager);
        mainMenuState = new MainMenuState();
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
    
    private void newGame() {
        gameState = new GameState();
        Kaffee.resetSelectedAmount();
        currentState = gameState;
    }
    
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
     * @author Cashen Adkins,Janni Röbbecke, Jakob Kleine, www.quizdroid.wordpress.com
     * @version 0.02 (22.05.2019)
     * @since 0.01 (22.05.2019
     */
    public class GameState implements State 
    {
        private static final int BORDER_WIDTH = 10;
        
        private BufferedImage hpBarBackground;
        private int score;
        private Player player; //Die Spielfigur des Spielers
        private LinkedList<Enemy> gegnerListe; //Eine Liste mit allen Gegnern im Spiel
        private LinkedList<Weapon> attackingWeapons; //Die Waffen, die sich gerade im Angriff befinden
        private LinkedList<Item> spawnedItems; //Die Items, die gespawnt aber noch nicht eingesammelt wurden
        private Rectangle[] roomBorders; //Die Wände des Raums
        private Room room; //Der Raum, der gerade gespielt wird
        private CollisionDetector collisionDet;
        
        private Constructor[] enemyConstructors;
        private int enemySpawnDelay;
        
        private Constructor[] itemConstructors;
        private int itemSpawnDelay;
        public GameState()
        {
            player = new Player(320, 320);
            gegnerListe = new LinkedList<Enemy>();
            attackingWeapons = new LinkedList<Weapon>();
            spawnedItems = new LinkedList<Item>();
            
            TileSet tileSet = new TileSet("/res/tilesets/standard-raum-ts.png", 3 /*Anzahl Tiles x*/, 3/*Anzahl Tiles y*/, 3/*Abstand zwischen Tiles*/);
            room = new Room("/res/rooms/standard-raum.txt", tileSet);
                        
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
            
            enemyConstructors = new Constructor[2];
            itemConstructors = new Constructor[2];
            try{
                enemyConstructors[0] = Class.forName("Virus").getConstructor(int.class, int.class);
                enemyConstructors[1] = Class.forName("SideEffect").getConstructor(int.class, int.class);

                itemConstructors[0] = Class.forName("Kaffee").getConstructor(int.class, int.class);
                itemConstructors[1] = Class.forName("CursorItem").getConstructor(int.class, int.class);
            } 
            catch(ClassNotFoundException e) { e.printStackTrace(); }
            catch(NoSuchMethodException e) { e.printStackTrace(); }
            
            spawnedItems.add(new CursorItem(Utils.random(10, 550), Utils.random(110, 650)));
        }   
        
        @Override
        public void render(Graphics g) 
        {
            Canvas c = screen.getCanvas();
            //c.setBackground(Color.blue);
            BufferStrategy bs = c.getBufferStrategy();
            if(bs == null)
                c.createBufferStrategy(3);
            else{
                Color color = new Color(255,255,255); //Eine Farbe wird festgelegt
                c.setBackground(color); //Farbe wird als Hintergrundfarbe dargestellt
                g = bs.getDrawGraphics();
                //Clear Screen
                g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                g.drawImage(hpBarBackground, 0, 0, null);
                room.renderMap(g); // Erst die Spielfläche ...
                player.render(g); // ... und darauf die Spielfigur
                for(Enemy e :  gegnerListe)
                    e.render(g);
                    
                for(Item i : spawnedItems) 
                    i.render(g);
                
                fontFestlegen(g, new Font("American Typewriter", Font.BOLD, 40)); 
                g.setColor(new Color(215, 7, 7));
                g.drawString("HP: "+player.health, 10, (HP_BAR_HEIGHT+40)/2);
                g.setColor(new Color(204, 146, 12));
                g.drawString(""+score, 500, (HP_BAR_HEIGHT+40)/2);
                bs.show();
                g.dispose();
            }
        }
        
        @Override
        public void update() 
        {
            player.setMove(getInput()); //Bewegt den Spieler entsprechend der Eingabe über die Tasten
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
                    Weapon enemyWeapon = e.target(player);
                    if(enemyWeapon!=null) 
                        attackingWeapons.add(enemyWeapon);
                    e.update();
                }
                else {
                    score += e.getScoreValue();
                    if(e.weapon != null && e.weapon.isAttacking()) 
                        waffenGestorbenerGegner.add(e.weapon);
                    nichtMehrLebendeGegner.add(e);
                }
            }
            attackingWeapons.removeAll(waffenGestorbenerGegner);
            gegnerListe.removeAll(nichtMehrLebendeGegner);
            
            for(Item i : spawnedItems) 
                i.update();
            
            collisionDet.update();
            
            if(enemySpawnDelay-- <= 0) {
                spawnRandomEnemy(); 
                enemySpawnDelay = Utils.random(100, 1000);
            }
            
            if(itemSpawnDelay-- <= 0) {
                spawnRandomItem(); 
                itemSpawnDelay = Utils.random(100, 1000);
            }

            if(!player.isAlive()) {
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
        
        private void spawnRandomEnemy() {
            try {
                gegnerListe.add((Enemy) enemyConstructors[Utils.random(0,1)].newInstance(Utils.random(10, 550), Utils.random(110, 650)));
            }
            catch(InstantiationException e) { e.printStackTrace(); }
            catch(IllegalAccessException e) { e.printStackTrace(); }
            catch(IllegalArgumentException e) { e.printStackTrace(); }
            catch(InvocationTargetException e) { e.printStackTrace(); }
        }
        
        private void spawnRandomItem() {
            try {
                spawnedItems.add((Item) itemConstructors[Utils.random(0,1)].newInstance(Utils.random(10, 550), Utils.random(110, 650)));
            }
            catch(InstantiationException e) { e.printStackTrace(); }
            catch(IllegalAccessException e) { e.printStackTrace(); }
            catch(IllegalArgumentException e) { e.printStackTrace(); }
            catch(InvocationTargetException e) { e.printStackTrace(); }
        }
        
        /*
         * Zuständig, um die Entitäten auf dem Spielfeld auf Kollisionen zu überprüfen, und diese zu verwalten.
         * Es wäreauch möglich, die Methoden direkt in die Klasse GameState zu schreiben, mit dieser inneren Klasse
         * wird allerdings der Kollision-Teil separiert, um die Kohäsion und Übersichtlichkeit zu verbessern
         * [Hinweis: weil diese innere Klasse privat ist, wird sie mit allen ihren Methoden nicht in JavaDoc angezeigt, weswegen
         *  das hier zwar angedeutet, aber nicht verwendet wurde]
         *  @author Jakob Kleine, Janni Röbbecke, Cepehr Bromand, Ares Zühlke
         */
        private class CollisionDetector {
            /*
             * Aktualisiert den CollisionDetector, der daraufhin alle Entitäten auf dem Spielfeld auf Kollision überprüft und 
             * diese Kollision auswertet
             */
            public void update() {
                LinkedList<Weapon> nichtMehrAttackierendeWaffen = new LinkedList<Weapon>(); //Um eine ConcurrentModificationException zu verhindern, werden die Elemente erst nach der Iteration über die Liste entfernt
                for(Weapon w : attackingWeapons) {
                    if(!w.isAttacking())
                        nichtMehrAttackierendeWaffen.add(w);
                    else {//Auf Kollision prüfen
                        if(w.isFriendly()){
                            LinkedList<Entity> getroffeneGegner = collidesWith(w, gegnerListe);
                            for(Entity e : getroffeneGegner) {
                                ((Enemy) e).startBeingAttacked(w); 
                                w.notifySuccess();
                            }
                        }
                        else {
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
             * Hält eine bewegbare Entity, die eventuell aus dem Spielfeld gelaufen ist, innerhalb des Spielfelds
             * @author Jakob Kleine, Janni Röbbecke, Cepehr Bromand, Ares Zühlke
             * @since 24.05.2019
             * @param e die bewegbare Entität, für die sichergestellt werden soll, dass sie nicht mit den Mauern am Rand kollidiert
             */
            private void keepInside(Movable e) {
                int xNeu = -1;
                int yNeu = -1;
                if(collision(e, roomBorders[0])) //Border Oben
                    yNeu = Game.HP_BAR_HEIGHT + BORDER_WIDTH;
                else if(collision(e, roomBorders[1])) //Border Unten
                    yNeu = Game.SCREEN_HEIGHT - (int) e.getHitbox().getHeight() - BORDER_WIDTH;
                //Kein else hier, weil auch 2 Borders getroffen werden können
                if(collision(e, roomBorders[2])) //Border Links
                    xNeu = BORDER_WIDTH;
                else if(collision(e, roomBorders[3])) //Border Rechts
                    xNeu = Game.SCREEN_WIDTH - (int) e.getHitbox().getWidth() - BORDER_WIDTH;
                    
                if(xNeu != -1) //Wenn die Position geändert werden soll
                    e.setEntityX(xNeu);
                if(yNeu != -1) 
                    e.setEntityY(yNeu);
                    
                if(xNeu != -1 || yNeu != -1) { //wenn mind. eine Position geändert wurde, soll das (wenn vorhanden) Knockback zurückgesetzt werden, weil sonst die Kreatur immer wieder gegen die Wand geworfen wird
                    if(e instanceof Creature) //Nur Kreaturen haben Knockback
                        ((Creature) e).resetKnockback();
                }
            }
        
            /*
             * Überprüft, ob zwei Entitäten miteinander kollidieren
             * @author Jakob Kleine, Janni Röbbecke, Cepehr Bromand, Ares Zühlke
             * @since 24.05.2019
             * @param e1 die eine zu überprüfende Entität
             * @param e2 die andere zu überprüfende Entität
             * @return true, wenn sich die Hitboxen von e1 und e2 schneiden, sonst false
             */
            private boolean collision(Entity e1, Entity e2) {
                return e1.getHitbox().intersects(e2.getHitbox());
            }
        
            /*
             * Überprüft, ob zwei Elemente miteinander kollidieren
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
                     * Aus einem uns nicht verständlichem Grund, ist es nicht möglich entity als Parameter
                     * der LinkedList anzugeben, und dann z.B. eine List<Enemy> zu übergeben, also wird eine "rohe" LinkedList verwendet,
                     * weswegen die Objekte gecastet werden müssen
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
     * Der Break-Menu-State ist der State, in dem sich das Spiel befindet, wenn eine Pause gemacht wird
     * @author Cashen Adkins, Janni Röbbecke, www.quizdroid.wordpress.com
     * @version 0.03 (26.05.2019)
     * @since 0.01 (22.05.2019
     */
    public class MainMenuState implements State 
    {
        private static final int ANZAHL_MENU_ITEMS = 6;
        private int menuItem; //Variable, die speichert, bei welchem Menüpunkt sich der Spieler gerade befindet.
        private BufferedImage menuItemFrame; //Der Rahmen, der sich um den ausgewählten Menüpunkt befindet.
        private Font font; //Das Font, welches für den Text in den Buttons benutzt wird.
        private BufferedImage menuBackground;
        public MainMenuState() 
        {
            menuItem = 0; //Zu Beginn des Menüs ist der ausgewählte Knopf der erste.
            try {
                 menuItemFrame = ImageIO.read(Utils.absoluteFileOf("/res/tilesets/menuitemframe.png")); //Der Rahmen wird gelesen und als BufferedImage gespeichert.
                 menuBackground = ImageIO.read(Utils.absoluteFileOf("/res/tilesets/menuBackground.jpg")); //Der Rahmen wird gelesen und als BufferedImage gespeichert.
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
            font = new Font("Futura", Font.BOLD, 40);
        }
        
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
                g.setColor(new Color(65, 41, 31));
                g.drawString("Neues Spiel", SCREEN_WIDTH/2-120, 200);
                g.drawString("Weiterspielen", SCREEN_WIDTH/2-120, 280);
                g.drawString("Anleitung", SCREEN_WIDTH/2-120, 360);
                g.drawString("Optionen", SCREEN_WIDTH/2-120, 440);
                g.drawString("Bestenliste", SCREEN_WIDTH/2-120, 520);
                g.drawString("Beenden", SCREEN_WIDTH/2-120, 600);
                g.drawImage(menuItemFrame, SCREEN_WIDTH/2-190, 140 + menuItem * 80, 384, 96, null);
                bs.show();
                g.dispose();
            }
        }
           
        @Override
        public void update() 
        {
            if(keyManager.downEinmal()) {
                if(menuItem < ANZAHL_MENU_ITEMS-1) 
                    menuItem++;
                else
                    menuItem = 0;
            }
            else if(keyManager.upEinmal()) {
                if(menuItem > 0) 
                    menuItem--;
                else
                    menuItem = ANZAHL_MENU_ITEMS-1;
            }
            else if(keyManager.attackEinmal()) {
                switch(menuItem) {
                    case 0:
                        newGame();
                    break;
                    case 1: 
                        resumeGame();
                    break; 
                    case 2:
                    
                    break;
                    case 3: 
                        
                    break;
                    case 4: 
                        currentState = highscoresState;
                    break;
                    default:
                        endGame();
                }
            }
            
            if(keyManager.escapeEinmal())
                resumeGame();
        }
        
        public void setMenuItem(int item) {
            if(item >= 0 && item < ANZAHL_MENU_ITEMS)
                menuItem = item;
        }
    }
    
    private class HighscoresState implements State {
        private Font font;
        public HighscoresState() {
            font = new Font("Futura", Font.BOLD, 40);
        }
        
        public void update() {
            if(keyManager.escapeEinmal())
                currentState = mainMenuState;
        }
        
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
                fontFestlegen(g,font.deriveFont(Font.BOLD));
                g.setColor(new Color(127, 101, 73));
                
                Utils.centerText(g, "BESTENLISTE", SCREEN_WIDTH, 100);
                
                g.setFont(font.deriveFont(Font.PLAIN, 30));
                ArrayList<Score> scores = scoreManager.getScores();
                for(int i=0; i<scores.size(); i++) {
                    g.drawString((i<9?"0":"")+(i+1)+": "+scores.get(i), 30, 175+55*i);
                }
               
                g.setFont(font.deriveFont(Font.ITALIC, 15)); 
                String hinweis = "Um zum Hauptmenü zurückzukehren bitte ESCAPE drücken.";
                Utils.centerText(g, "Um zum Hauptmenü zurückzukehren bitte ESCAPE drücken.", SCREEN_WIDTH, SCREEN_HEIGHT-15);
                bs.show();
                g.dispose();
            }
        }
    }
    
}