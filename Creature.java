import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * @author Janni, Ares, www.quizdroid.wordpress.com
 * @version 0.01 (10.05.19)
 */
public abstract class Creature extends Entity {
    /**
     * Standard-Lebenspunkte die eine Figur hat wenn sie im Spiel erscheind
     */
    public static final int DEFAULT_HEALTH = 10;
    /**
     * Standard-Geschwindigkeit einer Figur
     */
    public static final int DEFAULT_SPEED = 3;
    
    /**
     * Lebenspunkte der Figur
     */
    protected int health;
    /**
     * Geschwindigkeit der Figur
     */
    protected int speed;
    /**
     * speichert ob die Figur sich gerade in x- oder y-Richtung bewegt
     */
    protected int xMove, yMove;
    
    /**
     * Erzeugt eine Kreatur (Entität)
     * @param name der Name der Figur
     * @param image ein Spritesheet, das das Aussehen der Figur beschreibt (stimmt das?)
     * @param x die x-Position aud der die Entität gespawnt werden soll
     * @param y die y-Position aud der die Entität gespawnt werden soll
     * @param width die Breite der Figur
     * @param height die Höhe der Figur
     * @param speed die Geschwindigkeit der Entität
     */
    public Creature(String name, BufferedImage image, int x, int y, int width, int height, int health, int speed) {
        super(name, image, x, y, width, height);
        this.health = health;
        this.speed = speed;
        xMove = 0;
        yMove = 0;
    }
    /**
     * Die Figur bewegt sich mit ihrer bestimmten Geschwindigkeit auf eine neue Position 
     */
    public void move(){
        entityX += xMove * speed;
        entityY += yMove * speed;
    }
}