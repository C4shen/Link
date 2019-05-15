import java.awt.image.BufferedImage;
import java.awt.Point;
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
    public static final int DEFAULT_SPEED = 2;
    
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
    }
    
    /**
     * Berechnet die neue Position des Spielcharakters
     * @author Ares Zühlke, Janni Röbbecke, www.qizdroid.wordpress.com
     * @since 0.01 (10.05.2019)
     */
    @Override
    public void update() {
        move();
    }
    
    /**
     * Ändert die Bewegungsrichtung der Spielfigur
     * @author Ares Zühlke, Janni Röbbecke, www.qizdroid.wordpress.com
     * @param p Punkt (xP|yP), der die Bewegungsrichtung in x-Richtung (xP) und y-Richtung (yP)angibt
     * @since 0.01 (10.05.2019)
     */
    public void setMove(Point p){
        xMove = p.x;
        yMove = p.y;
    }
}