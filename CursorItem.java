import java.awt.Rectangle;

/**
 * Das Cursor-Item ist ein Item, das gespawnt wird um dem Spieler zu ermöglichen, einen Cursor als Waffe einzusammeln.
 * Es beinflusst den Spieler also so, dass es seine Waffe zu einem neuen Waffen-Cursor-Objekt verändert.
 * @author Janni Röbbecke, Jakob Kleine
 * @since 26.05.2019
 */
public class CursorItem extends Item
{
    private static final SpriteSheet DEFAULT_SPRITE_SHEET = new SpriteSheet("/res/sprites/weapons/cursor.png", 3 /*moves*/, 8 /*directions*/, 16 /*width*/, 16 /*height*/);
    private static final int DEFAULT_WIDTH = 16, DEFAULT_HEIGHT = 16; //Die Standard-Größe eines Cursor-Items
    /**
     * Erzeugt ein neues Cursor-Item.
     * @param xKoordinate die xKoordinate, bei der das Item entstehen soll
     * @param yKoordinate die yKoordinate, bei der das Item entstehen soll
     * @author Janni Röbbecke, Jakob Kleine
     * @since 26.05.2019
     */
    public CursorItem(int xKoordinate, int yKoordinate) {
        super("Cursor-Item", DEFAULT_SPRITE_SHEET, xKoordinate, yKoordinate, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    /**
     * Das Cursor-Item gibt dem Spieler einen neuen Cursor als Waffe
     * @param p der Spieler, der das Item eingesammelt hat.
     * @author Janni Röbbecke, Jakob Kleine
     * @since 26.05.2019
     */
    public void affect(Player p) {
        p.setWeapon(new Cursor((int) Math.round(p.entityX), (int) Math.round(p.entityY), true));
    }
    
    /**
     * @author Janni Röbbecke, Jakob Kleine
     * @since 26.05.2019
     */
    public Rectangle getHitbox() {
        return new Rectangle((int) Math.round(entityX), (int) Math.round(entityY), width, height);
    }
}