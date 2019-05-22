import java.awt.Point;
import java.awt.image.BufferedImage;
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
    
    private static final Point[][] handPosition = new Point[][]{
        new Point[]{ new Point(25, 20), new Point(14, 26), new Point(18, 23) },
        new Point[]{ new Point(25, 19), new Point(17, 17), new Point(02, 06) },
        new Point[]{ new Point(27, 19), new Point(32, 22), new Point(43, 11) },
        new Point[]{ new Point(32, 18), new Point(37, 26), new Point(34, 22) }
    };
    
    /**
     * Erzeugt eine Spielfigur
     * @author Ares Zühlke, Janni Röbbecke, www.qizdroid.wordpress.com
     * @param x die x-Position der Spielfigur
     * @param y die y-Position der Spielfigur
     * @param playerSprite ein Spritesheet, das das Aussehen der Spielfigur bestimmt
     * @since 0.01 (10.05.2019)
     */
    public Player(int x, int y, SpriteSheet playerSprite) {
        super("Player", playerSprite, x, y, Entity.DEFAULT_WIDTH, Entity.DEFAULT_HEIGHT, Player.DEFAULT_HEALTH, Player.DEFAULT_SPEED, 
                new Cursor(new SpriteSheet("/res/sprites/weapons/cursor.png", 3 /*moves*/, 4 /*directions*/, 16 /*width*/, 16 /*height*/), x+10, y+30, DEFAULT_SPEED)); 
                //Eigentlich Waffe nicht im Konstruktor!
    }
    
    public Point getHandPosition(int xPos, int direction){
        return new Point(handPosition[xPos][direction].x + (int)Math.round(entityX), handPosition[xPos][direction].y + (int)Math.round(entityY));
    }
}