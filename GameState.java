import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
/**
 * Der GameState ist der State, in dem sich das Spiel während dem Spielen befindet. Hier werden die Figuen aktualisiert und neu gerendert
 * @author Cashen Adkins, Janni Röbbecke, Jakob Kleine, www.quizdroid.wordpress.com
 * @version 0.02 (28.05.2019)
 * @since 0.01 (22.05.2019)
 */
public class GameState extends State 
{
    /**
     * Die Standard-Dicke der Wand (Border) des Spiel-Raums 
     */
    public static final int BORDER_WIDTH = 10;
    
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
            new Rectangle(               0,    Game.HP_BAR_HEIGHT, Game.SCREEN_WIDTH, BORDER_WIDTH), //links oben -> rechts oben
            new Rectangle(               0, Game.SCREEN_HEIGHT-10, Game.SCREEN_WIDTH, BORDER_WIDTH), //links unten -> rechts unten
            new Rectangle(               0,    Game.HP_BAR_HEIGHT, BORDER_WIDTH, Game.SCREEN_WIDTH), //links oben -> links unten
            new Rectangle( Game.SCREEN_WIDTH-10,    Game.HP_BAR_HEIGHT, BORDER_WIDTH, Game.SCREEN_WIDTH)  //rechts oben -> rechts unten
        };
        try{
            hpBarBackground = ImageIO.read(Utils.absoluteFileOf("/res/tilesets/hpbarBackground.png"));
        }
        catch(IOException e) { e.printStackTrace(); }
        
        collisionDet = new CollisionDetector();
        
        //Die Konstruktoren der Gegner und Items werden gespeichert
        enemyConstructors = new Constructor[2];
        itemConstructors = new Constructor[4];
        try{
            enemyConstructors[0] = Class.forName("Virus").getConstructor(int.class, int.class);
            enemyConstructors[1] = Class.forName("SideEffect").getConstructor(int.class, int.class);

            itemConstructors[0] = Class.forName("CursorItem").getConstructor(int.class, int.class);
            itemConstructors[1] = Class.forName("Coffee").getConstructor(int.class, int.class);
            itemConstructors[2] = Class.forName("Pizza").getConstructor(int.class, int.class);
            itemConstructors[3] = Class.forName("Shoes").getConstructor(int.class, int.class);
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
        g.drawImage(hpBarBackground, 0, 0, null);
        room.renderMap(g); // Erst die Spielfläche ...
        player.render(g); // ... und darauf die Spielfigur
        for(Enemy e :  gegnerListe)
            e.render(g);
        for(Item i : spawnedItems) 
            i.render(g);
        
        //Am oberen Bildrand werden die Lebenspunkte und der Score des Spielers angezeigt
        Utils.fontFestlegen(g, new Font("American Typewriter", Font.BOLD, 40)); 
        g.setColor(new Color(215, 7, 7));
        g.drawString("HP: "+player.health, 10, (Game.HP_BAR_HEIGHT+40)/2);
        g.setColor(new Color(204, 146, 12));
        g.drawString(""+score, 500, (Game.HP_BAR_HEIGHT+40)/2);
    }
    
    /**
     * Berechnet die neuen Positionen und Werte aller Figuren und Gegenstände auf dem Spielfeld
     */
    @Override
    public void update(KeyManager keyManager) 
    {
        player.reciveKeyInput(getInput(keyManager)); //Bewegt den Spieler entsprechend der Eingabe über die Tasten
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
            //Sollte der Spieler keine Lebenspunkte mehr haben wird sein Socre im scoreManeger festgehalten und es wird zum Hauptmenü gewechselt
            ((HighscoresState) getStates().get(StateNames.SCORES)).addScore(score);
            getStates().put(StateNames.GAME, null); //Damit das Spiel nicht mehr weitergespielt werden kann, wird der Game-State auf null gesetzt
            setState(StateNames.MENU);
        }
        
        //Wenn Escape gedrückt wird, ändert sich die State in die MenuState
        if(keyManager.escapeEinmal()) {
            ((MainMenuState) getStates().get(StateNames.MENU)).setMenuItem(1); //Es wird das zweite Menü-Item ausgewählt (weiterspielen)
            setState(StateNames.MENU);
        }
    }
    
    /**
     * Holt vom Key-Manager ein, welche Beweguns-Tasten gedückt werden
     * @author Janni Röbbecke, Ares Zühlke, Cashen Adkins, www.quizdroid.wordpress.com
     * @since 0.02 (11.05.2019)
     * @return ein Punkt, der die Bewegung in x- und y-Richtung angibt
     */
    public Point getInput(KeyManager keyManager){
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
     * Spawnt einen zufälligen Gegener an einer zufälligen Stelle auf dem Spielfleld
     * @author Jakob Kleine, Janni Röbbecke 
     * @since 27.05.2019
     */
    private void spawnRandomEnemy() {
        try { //Es wird ein zufälliger Konstruktor gewählt (-> zufäflliger Gegner-Typ) und es werden als Konstruktor-Parameter zufällige Koordinaten übergeben
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
        try { //Es wird ein zufälliger Konstruktor gewählt (-> zufäflliger Item-Typ) und es werden als Konstruktor-Parameter zufällige Koordinaten übergeben
            spawnedItems.add((Item) itemConstructors[Utils.random(0,3)].newInstance(Utils.random(10, 550), Utils.random(110, 650)));
        }
        catch(InstantiationException e) { e.printStackTrace(); }
        catch(IllegalAccessException e) { e.printStackTrace(); }
        catch(IllegalArgumentException e) { e.printStackTrace(); }
        catch(InvocationTargetException e) { e.printStackTrace(); }
    }
    
    /**
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
        /**
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
    
        /**
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