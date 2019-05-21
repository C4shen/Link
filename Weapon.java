import java.awt.image.BufferedImage;
import java.awt.Point;
/**
 * 
 * @author Jakob Kleine, Janni Röbbecke
 * @version 0.01 (17.05.2019)
 * @since 17.05.2019
 */
public abstract class Weapon extends Movable {
    
    /**
     * Ersellt eine neue Waffe
     * @author Jakob Kleine, Janni Röbbecke
     * @param name der Name der Waffe
     * @param image das SpriteSheet, das die Waffe abbildet
     * @param x die x-Position der Waffe
     * @param y die y-Position der Waffe
     * @param width die Breite der Waffe
     * @param height die Höhe der Waffe
     * @since 0.01 (17.05.2019)
     */
    public Weapon(String name, SpriteSheet spriteSheet, int x, int y, int width, int height, double speed) {
        super(name, spriteSheet, x, y, width, height, speed);
    }
    
    /**
     * Startet den Angriff mit dieser Waffe
     * @param direction die Richtung, in die der Spieler zum Startpunkt des Angriffs guckt
     * @author Jakob Kleine, Janni Röbbecke
     * @since 0.01 (17.05.2019)
     */
    public abstract void startAttack(Point direction);
}