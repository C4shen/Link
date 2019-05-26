import java.awt.Rectangle;

public class Kaffee extends Item
{
    private static final SpriteSheet DEFAULT_SPRITE_SHEET = new SpriteSheet("/res/sprites/items/kaffee.png", 1 /*moves*/, 1 /*directions*/, 32 /*width*/, 26 /*height*/);
    private static final int DEFAULT_WIDTH = 16, DEFAULT_HEIGHT = 16; //Die Standard-Größe eines Cursor-Items
    
    private static int bereitsGesammelt;
    
    private static final int KB_AMOUNT = 5; //Die Standard-Weite des Kaffees
    private static final double KB_STRENGTH = 2; //Die Standard-Stärke des Kaffees
    private static final int DEFAULT_DAMAGE = 5; //Der Standard-Schaden des Kaffees
    
    private Knockback knockback;
    public Kaffee(int xKoordinate, int yKoordinate) {
        super("Cursor-Item", DEFAULT_SPRITE_SHEET, xKoordinate, yKoordinate, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public void affect(Player p) {
        bereitsGesammelt++;
        p.speed += 0.125 + 0.125 / bereitsGesammelt;
        p.startBeingAttacked((int) Math.round(DEFAULT_DAMAGE + DEFAULT_DAMAGE * (0.125 * bereitsGesammelt)), new Knockback(KB_AMOUNT, KB_STRENGTH, -p.xMove, -p.yMove));
    }
    
    public Rectangle getHitbox() { 
        return new Rectangle((int) Math.round(entityX), (int) Math.round(entityY), width, height);
    }
}