import java.awt.Rectangle;

/**
 * Pizza ist ein Item, das der Spieler einsammeln kann, um seine Lebenspunkte (HP) zu verbessern.
 * 
 * @author Jakob Kleine, Janni Röbbecke
 * @since 26.05.2019
 */
public class Pizza extends Item
{
    private static final SpriteSheet DEFAULT_SPRITE_SHEET = new SpriteSheet("/res/sprites/items/pizza.png", 4 /*moves*/, 1 /*directions*/, 32 /*width*/, 32 /*height*/);
    private static final int DEFAULT_WIDTH = 32, DEFAULT_HEIGHT = 32; //Die Standard-Größe eines Pizzastücks
    
    private static final int DEFAULT_HP = 10; //Die Standardanzahl von Lebenspunkten, die das Pizzastück wiederherrstellt
    
    /**
     * Erzeugt einen neuen Pizzastück.
     * @param xKoordinate die xKoordinate, bei der das Item entstehen soll
     * @param yKoordinate die yKoordinate, bei der das Item entstehen soll
     * @author Janni Röbbecke, Jakob Kleine
     * @since 28.05.2019
     */
    public Pizza(int xKoordinate, int yKoordinate) {
        super(DEFAULT_SPRITE_SHEET, xKoordinate, yKoordinate, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    /**
     * Pizza erhöht die Lebenspunkte des Spielers.
     * @param p der Spieler, der die Pizza eingesammelt hat
     * @author Janni Röbbecke, Jakob Kleine
     * @since 28.05.2019
     */
    public void affect(Player p) {
        p.changeHealthBy(DEFAULT_HP); 
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 26.05.2019
     */
    public Rectangle getHitbox() { 
        return new Rectangle((int) Math.round(entityX), (int) Math.round(entityY), width, height);
    }
}