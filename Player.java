import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
/**
 * Der Player ist die Spielfigur, die vom Spieler gesteuert wird.
 * @author Janni Rübbecke, Ares Zühlke, Jakob Kleine, (zu geringen Teilen) www.quizdroid.wordpress.com
 * @version 0.01 (10.05.2019)
 * @since 0.01 (10.05.2019)
 */
public class Player extends Creature {
    /*
     * Das Standart-Spritesheet-Objekt, auf das alle Objekte dieser Klasse als ihr SpriteSheet referenzieren. 
     * Dadurch, dass es ein Standard-Bild gibt, müssen die gleichen Bilder nicht mehrmals geladen werden.
     */
    private static final SpriteSheet DEFAULT_SPRITE_SHEET = new SpriteSheet("/res/sprites/creatures/player.png", 3 /*moves*/, 4 /*directions*/, 64 /*width*/, 64 /*height*/);
    
    
    /**
     * Standard-Lebenspunkte, die der Spielcharakter zu Beginn des Spiels hat
     */
    public static final int DEFAULT_HEALTH = 100;
    
    /**
     * Standard-Geschwindigkeit des Spielcharakters
     */
    private static final double DEFAULT_SPEED = 1.5;
    
    /*
     * Die Koordinaten der Hand in den verschiedenen Bildern/Positionen von der oberen linken Ecke des jeweiligen Bilds aus.
     * Sie werden gespeichert, damit die Waffen immer genau in der Hand positioniert werden kann, und nicht speziell auf eine
     * Figur maßgeschneidert sein müssen.
     * Für jede Waffe wird außerdem die Position des Griffs gespeichert, also die Position der Stelle, an der die Waffe in die
     * Hand des Besitzers gelegt werden soll.
     */
    private static final Point[][] handPosition = new Point[][]{
        new Point[]{ new Point(31, 34), new Point(20, 40), new Point(24, 37) },
        new Point[]{ new Point(35, 32), new Point(27, 30), new Point(12, 19) },
        new Point[]{ new Point(33, 33), new Point(38, 36), new Point(49, 25) },
        new Point[]{ new Point(42, 31), new Point(47, 39), new Point(44, 35) }
    };
    
    /**
     * Erzeugt die Spielfigur
     * @author Ares Zühlke, Janni Röbbecke, www.qizdroid.wordpress.com
     * @param x die x-Position der Spielfigur
     * @param y die y-Position der Spielfigur
     * @param playerSprite ein Spritesheet, das das Aussehen der Spielfigur bestimmt
     * @since 0.01 (10.05.2019)
     */
    public Player(int x, int y) {
        super(DEFAULT_SPRITE_SHEET, x, y, Entity.DEFAULT_WIDTH, Entity.DEFAULT_HEIGHT, Player.DEFAULT_HEALTH, Player.DEFAULT_SPEED); 
    }
    
    /**
     * Der Spieler empfängt die Information in welche Richtungen er gerade Gesteuert wird
     * @param p Punkt, der die Richtungen angiebt in die die Spielfigur gerade gesteuert wird
     * @author Janni Röbbecke, Jakob Kleine
     * @since 28.05.2019
     */
    public void reciveKeyInput(Point p){
        if(p.x!=0 && p.y!=0){
            //sollte der Spieler sich in zwei Richtungen geleichzeitig bewegen wird seine Bewegung reduziert, 
            //damit er insgesmt genausoschnell läuft wie er in eine Richtung laufen würde
            xMove = 0.7071067811865475 *p.getX();
            yMove = 0.7071067811865475 *p.getY();
        }
        else {
            xMove = p.x;
            yMove = p.y;
        }
    }
    
    /**
     * @author Janni Röbbecke, Jakob Kleine
     * @since 22.05.2019
     */
    protected boolean weaponBehind(){ 
        return prevDirection == 1 || prevDirection == 3;
    }
    
    /**
     * @author Janni Röbbecke, Jakob Kleine
     * @since 22.05.2019
     */
    public Point getHandPosition(int xPos, int direction){
        return new Point(handPosition[xPos][direction].x + (int) Math.round(entityX), handPosition[xPos][direction].y + (int)Math.round(entityY));
    }

    /**
     * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
     * @since 22.05.2019
     */
    public Rectangle getHitbox() {
        return new Rectangle((int) Math.round(entityX), (int) Math.round(entityY), 64, 64); 
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 26.05.2019
     */
    public double getDefaultSpeed() {
        return DEFAULT_SPEED;
    }
}