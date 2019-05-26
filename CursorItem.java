import java.awt.Rectangle;

public class CursorItem extends Item
{
    private static final SpriteSheet DEFAULT_SPRITE_SHEET = new SpriteSheet("/res/sprites/weapons/cursor.png", 3 /*moves*/, 8 /*directions*/, 16 /*width*/, 16 /*height*/);
    private static final int DEFAULT_WIDTH = 16, DEFAULT_HEIGHT = 16; //Die Standard-Größe eines Cursor-Items
    public CursorItem(int xKoordinate, int yKoordinate) {
        super("Cursor-Item", DEFAULT_SPRITE_SHEET, xKoordinate, yKoordinate, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public void affect(Player p) {
        p.setWeapon(new Cursor((int) Math.round(p.entityX), (int) Math.round(p.entityY), Player.DEFAULT_SPEED, true));
    }
    
    public Rectangle getHitbox() {
        return new Rectangle((int) Math.round(entityX), (int) Math.round(entityY), width, height);
    }
}