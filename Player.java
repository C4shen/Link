import java.awt.image.BufferedImage;
import java.awt.Graphics;
/**
 * Der Player ist die Spielfigur, die vom Spieler gesteuert wird.
 * @author Janni Rübbecke, Ares Zühlke, www.quizdroid.wordpress.com
 * @version 0.01 (10.05.2019)
 * @since 0.01 (10.05.2019)
 */
public class Player extends Creature {
    /**
     * Standard-Lebenspunkte, die der Spielcharakter zu Beginn des Spiels hat
     */
    public static final int DEFAULT_HEALTH = 100;
    /**
     * Standard-Geschwindigkeit des Spielcharakters
     */
    public static final double DEFAULT_SPEED = 1.5;
    
    public Weapon weapon;
    
    /**
     * Erzeugt eine Spielfigur
     * @author Ares Zühlke, Janni Röbbecke, www.qizdroid.wordpress.com
     * @param x die x-Position der Spielfigur
     * @param y die y-Position der Spielfigur
     * @param playerSprite ein Spritesheet, das das Aussehen der Spielfigur bestimmt
     * @since 0.01 (10.05.2019)
     */
    public Player(int x, int y, SpriteSheet playerSprite) {
        super("Player", playerSprite, x, y, Entity.DEFAULT_WIDTH, Entity.DEFAULT_HEIGHT, Player.DEFAULT_HEALTH, Player.DEFAULT_SPEED);
        weapon = new Cursor(new SpriteSheet("/res/sprites/weapons/cursor.png",4,4,16,16), x+7, y+30, Player.DEFAULT_SPEED);
    }
    
    public void startAttack() {
        weapon.startAttack(new java.awt.Point(xMove,yMove));
    }
    
    /**
     * Berechnet die neue Position des Spielcharakters
     * @author Ares Zühlke, Janni Röbbecke, www.qizdroid.wordpress.com
     * @since 0.01 (10.05.2019)
     */
    @Override
    public void update() {
        move();
        weapon.update();
    }
    
    @Override 
    public void render(Graphics g) {
        super.render(g);
        weapon.render(g);
    }
    
    public void setMove(java.awt.Point p ){
        super.setMove(p);
        weapon.setMove(p);
    }
}