import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Jann, Ares, quizdroid
 * (10.05.19)
 */
public abstract class Creature extends Entity {
    /**
     * Standard Lebenspunkte die eine Figur hat wenn sie im Spiel erscheind
     */
    public static final int DEFAULT_HEALTH = 10;
    /**
     * Standard Geschwindigkeit einer Figur
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
     * Erzeugt eine Figur (Entität) mit den angegebenen Atributwerten
     */
    public Creature(String name, BufferedImage image, int x, int y, int width, int height, int health, int speed) {
        super(name, image, x, y, width, height);
        this.health = health;
        this.speed = speed;
        xMove = 0;
        yMove = 0;
    }
    /**
     * die Figur bewegt sich mit ihrer bestimmten Geschwindigkeit auf eine neue Position 
     */
    public void move(){
        entityX += xMove * speed;
        entityY += yMove * speed;
    }
}