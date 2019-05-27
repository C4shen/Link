import java.awt.Rectangle;

/**
 * Kaffee ist ein PowerUp, das der Spieler einsammeln kann, um seine Stärke zu verbessern.
 * Der Kaffee hilft dem Spieler, indem er ihn schneller macht. Der Spieler bekommt also einen Koffein-Schock.
 * Allerdings ist der Konsum von Drogen natürlich schädlich für den Körper, weshalb das Einsammeln von Kaffee auch einen kleinen Schaden verursacht.
 * <br>
 * Wie bei jeder Droge lässt die Wirkung bei anhaltendem Konsum nach und die Auswirkungen auf den Körper verschlimmern sich. 
 * Je mehr Kaffee also eingesammelt wird, desto geringer fällt der Geschwindigkeitsboost aus, und desto größer wird der zugefügte Schaden.
 * 
 * @author Jakob Kleine, Janni Röbbecke
 * @since 26.05.2019
 */
public class Kaffee extends Item
{
    private static final SpriteSheet DEFAULT_SPRITE_SHEET = new SpriteSheet("/res/sprites/items/kaffee.png", 1 /*moves*/, 1 /*directions*/, 32 /*width*/, 26 /*height*/);
    private static final int DEFAULT_WIDTH = 32, DEFAULT_HEIGHT = 26; //Die Standard-Größe eines Kaffees
    
    private static int bereitsGesammelt; //Speichert die Anzahl der Kaffees, die bereits eingesammelt wurden
    
    private static final int KB_AMOUNT = 5; //Die Standard-Weite des Kaffee-Knockbacks
    private static final double KB_STRENGTH = 2; //Die Standard-Stärke des Kaffee-Knockbacks
    private static final int DEFAULT_DAMAGE = 5; //Der Standard-Schaden des Kaffees
    
    /**
     * Erzeugt einen neuen Kaffee.
     * @param xKoordinate die xKoordinate, bei der das Item entstehen soll
     * @param yKoordinate die yKoordinate, bei der das Item entstehen soll
     * @author Janni Röbbecke, Jakob Kleine
     * @since 26.05.2019
     */
    public Kaffee(int xKoordinate, int yKoordinate) {
        super("Cursor-Item", DEFAULT_SPRITE_SHEET, xKoordinate, yKoordinate, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    /**
     * Kaffee verschnelltert den Spieler. Die Wirkung lässt allerdings nach, je mehr Kaffes bereits eingesammelt wurden.
     * <br>Außerdem fügt das Einsammeln von Kaffee dem Spieler Schaden zu. Dieser Schaden wird gößer je mehr Kaffees bereits eingesammelt wurden.
     * @param p der Player, der den Kaffee eingesammelt hat
     * @author Jakob Kleine, Janni Röbbecke
     * @since 26.05.2019
     */
    public void affect(Player p) {
        bereitsGesammelt++; 
        p.changeSpeedBy(0.125 + 0.125 / bereitsGesammelt); //Die zusätzl. Geschwindigkeit beträgt immer 0.125 ist größer, je weniger Kaffe eingesammelt wurde
        p.startBeingAttacked((int) Math.round(DEFAULT_DAMAGE + DEFAULT_DAMAGE * (0.125 * bereitsGesammelt)), //Der Schaden ist min. DefDam. aber ist größer je mehr gesammelt wurde
                                new Knockback(KB_AMOUNT, KB_STRENGTH, -p.xMove, -p.yMove)); //Wirft den Spieler entgegen seiner Richtung zurück
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 26.05.2019
     */
    public Rectangle getHitbox() { 
        return new Rectangle((int) Math.round(entityX), (int) Math.round(entityY), width, height);
    }
}