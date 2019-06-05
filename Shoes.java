import java.awt.Rectangle;

/**
 * Shoes ist ein Item, das der Spieler einsammeln kann, um Bewegungsgeschwindigkeit zu verbessern.
 * 
 * @author Cashen Adkins, Janni Röbbecke
 * @since 26.05.2019
 */
public class Shoes extends Item
{
    private static final SpriteSheet DEFAULT_SPRITE_SHEET = new SpriteSheet("/res/sprites/items/shoes.png", 4 /*moves*/, 1 /*directions*/, 32 /*width*/, 32 /*height*/);
    private static final int DEFAULT_WIDTH = 32, DEFAULT_HEIGHT = 32; //Die Standard-Größe eines Schuhes
    
    /**
     * Erzeugt ein neues Schuhpaar.
     * @param xKoordinate die xKoordinate, bei der das Item entstehen soll
     * @param yKoordinate die yKoordinate, bei der das Item entstehen soll
     * @author Cashen Adkins, Jakob Kleine
     * @since 28.05.2019
     */
    public Shoes(int xKoordinate, int yKoordinate) {
        super(DEFAULT_SPRITE_SHEET, xKoordinate, yKoordinate, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    /**
     * Shoes erhöht die Bewegungsgeschwindikeit des Spielers.
     * @param p der Spieler, der die Shoes eingesammelt hat
     * @author Cashen Adkins, Jakob Kleine
     * @since 28.05.2019
     */
    public void affect(Player p) {
        p.changeSpeedBy(0.5);
    }
    
    /**
     * @author Cashen Adkins, Janni Röbbecke
     * @since 26.05.2019
     */
    public Rectangle getHitbox() { 
        return new Rectangle((int) Math.round(entityX), (int) Math.round(entityY), width, height);
    }
}