import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.awt.Font;
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
    private State currentState;
    private State gameState;
    private State menuState;
    
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
        screen = new Screen("LINK - Legend of INformatik Kurs", SCREEN_WIDTH, SCREEN_HEIGHT);
        keyManager = new KeyManager();
        screen.getFrame().addKeyListener(keyManager);
        gameState = new GameState();
        menuState = new BreakMenuState();
        currentState = gameState;
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
        //Überprüft ob gerade Escape gedrückt wird und ändert die State entweder von gameState zu menuState oder von menuState zu gameState.
        if(keyManager.escapeEinmal()) {
            if(currentState == gameState)
                currentState = menuState;
            else if(currentState == menuState)
                currentState = gameState;
        }
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
        private int score;
        private Player player; //Die Spielfigur des Spielers
        private LinkedList<Enemy> gegnerListe; //Eine Liste mit allen Gegnern im Spiel
        private LinkedList<Weapon> attackingWeapons; //Die Waffen, die sich gerade im Angriff befinden
        private Border[] roomBorders; //Die Wände des Raums
        private Room room; //Der Raum, der gerade gespielt wird
        private CollisionDetector collisionDet;
        public GameState()
        {
            SpriteSheet playerSprite = new SpriteSheet("/res/sprites/creatures/player.png", 3 /*moves*/, 4 /*directions*/, 64 /*width*/, 64 /*height*/);
            player = new Player(320, 320, playerSprite);
            gegnerListe = new LinkedList<Enemy>();
            attackingWeapons = new LinkedList<Weapon>();
            TileSet tileSet = new TileSet("/res/tilesets/standard-raum-ts.png", 3, 3);
            room = new Room("/res/rooms/standard-raum.txt", tileSet);
            SpriteSheet krebsSprite = new SpriteSheet("/res/sprites/creatures/krebs.png", 3 /*moves*/, 4 /*directions*/, 64 /*width*/, 64 /*height*/);
            gegnerListe.add(new SideEffect(new java.util.Random().nextInt(400)+200, new java.util.Random().nextInt(400)+200, krebsSprite));
            
            roomBorders = new Border[] {
                new Border(                    0,    Game.HP_BAR_HEIGHT,   Game.SCREEN_WIDTH, Border.BORDER_WIDTH), //links oben -> rechts oben
                new Border(                    0, Game.SCREEN_HEIGHT-10,   Game.SCREEN_WIDTH, Border.BORDER_WIDTH), //links unten -> rechts unten
                new Border(                    0,    Game.HP_BAR_HEIGHT, Border.BORDER_WIDTH,   Game.SCREEN_WIDTH), //links oben -> links unten
                new Border( Game.SCREEN_WIDTH-10,    Game.HP_BAR_HEIGHT, Border.BORDER_WIDTH,   Game.SCREEN_WIDTH) //rechts oben -> rechts unten
            };
            
            collisionDet = new CollisionDetector();
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
                g = bs.getDrawGraphics();
                //Clear Screen
                g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                room.renderMap(g); // Erst die Spielfläche ...
                player.render(g); // ... und darauf die Spielfigur
                for(Enemy e :  gegnerListe)
                    e.render(g);
                /*
                hitBoxAnzeigen(player, g);
                for(Enemy e :  gegnerListe)
                    hitBoxAnzeigen(e, g);
                for(Weapon w : attackingWeapons) 
                    hitBoxAnzeigen(w, g);
                for(Border b : roomBorders) 
                    hitBoxAnzeigen(b, g);
                */
                Graphics2D g2d = (Graphics2D) g; //Damit RenderingHints gesetzt werden können, muss das Graphics-Objekt in ein Graphics2D-Objekt gecastet werden
                g2d.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING, //Text-Anti-Aliasing -> weichere Kanten
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(
                    RenderingHints.KEY_FRACTIONALMETRICS, //Fractional-Metrics -> konsistente Buchstabengröße
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 40)); //Comic Sans ist nur ein Beispiel, sorry Cepehr 
                g.drawString("Score: "+score, 10, Game.HP_BAR_HEIGHT-40);
                bs.show();
                g.dispose();
            }
        }
          
        private void hitBoxAnzeigen(Entity e, Graphics g) {
            g.drawRect(e.getHitbox().x, e.getHitbox().y, e.getHitbox().width, e.getHitbox().height); //Nur temporär, um die Hitbox anzuzeigen
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
            for(Enemy e : gegnerListe) {
                if(e.isAlive())
                    e.update();
                else {
                    // e.die();
                    score += e.getScoreValue();
                    gegnerListe.remove(e);
                }
            }
            
            collisionDet.update();
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
                for(Weapon w : attackingWeapons) {
                    if(!w.isAttacking())
                        attackingWeapons.remove(w);
                    else {//Auf Kollision prüfen
                        LinkedList<Entity> getroffeneGegner = collidesWith(w, gegnerListe);
                        for(Entity e : getroffeneGegner) {
                            ((Enemy) e).startBeingAttacked(w); 
                            w.notifySuccess();
                        }
                    }
                }
                
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
                if(collision(e, roomBorders[0])) //Border Oben
                    e.setEntityY(Game.HP_BAR_HEIGHT + Border.BORDER_WIDTH);
                else if(collision(e, roomBorders[1])) //Border Unten
                    e.setEntityY(Game.SCREEN_HEIGHT - (int) e.getHitbox().getHeight() - Border.BORDER_WIDTH); //Idee für Glitch -> -Borderwidth weglassen
                //Kein else hier, weil auch 2 Borders getroffen werden können
                if(collision(e, roomBorders[2])) //Border Links
                    e.setEntityX(Border.BORDER_WIDTH);
                else if(collision(e, roomBorders[3])) //Border Rechts
                    e.setEntityX(Game.SCREEN_WIDTH - (int) e.getHitbox().getWidth() - Border.BORDER_WIDTH);
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
     * @version 0.02 (22.05.2019)
     * @since 0.01 (22.05.2019
     */
    public class BreakMenuState implements State 
    {
        public BreakMenuState() {
            
        }
        
        @Override
        public void render(Graphics g)  {
            
        }
        
        @Override
        public void update() {
            
        }
    }

}