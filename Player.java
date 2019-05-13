import java.awt.image.BufferedImage;
import java.awt.Point;
/**
 * @author Janni, Ares, www.quizdroid.wordpress.com
 * @version 0.01 (10.05.19)
 */
public class Player extends Creature {
    /**
     * Standard-Lebenspunkte, die der Spielcharakter zu Beginn des Spiels hat
     */
    public static final int DEFAULT_HEALTH = 100;
    /**
     * Standard-Geschwindigkeit des Spielcharakters
     */
    public static final int DEFAULT_SPEED = 20;
    
    /**
     * Erzeugt eine Spielfigur
     * @param x die x-Position der Spielfigur
     * @param y die y-Position der Spielfigur
     * @param playerSprite ein Spritesheet, das das Aussehen der Spielfigur bestimmt
     * @since 0.01 (10.05.2019)
     */
    public Player(int x, int y, SpriteSheet playerSprite) {
        super("Player", playerSprite, x, y, Entity.DEFAULT_WIDTH, Entity.DEFAULT_HEIGHT, Player.DEFAULT_HEALTH, Player.DEFAULT_SPEED);
    }
    
    /**
     * Berechnet die neue Position des Spielcharakters
     * @since 0.01 (10.05.2019)
     */
    @Override
    public void update() {
        move();
    }
    
    /**
     * ?
     * @param p ?
     */
    public void setMove(Point p){
        xMove = p.x;
        yMove = p.y;
    }
}