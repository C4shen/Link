import java.awt.image.BufferedImage;
/**
 * Jann, Ares, quizdroid
 * (10.05.19)
 */
public class Player extends Creature {
    /**
     * Standard Lebenspunkte, die der Spielcharakter zu Beginn des Spiels hat
     */
    public static final int DEFAULT_HEALTH = 100;
    /**
     * Standard Geschwindigkeit des Spielcharakters
     */
    public static final int DEFAULT_SPEED = 1;
    
    /**
     * erzeugt eine Spielfigur, die auf der angegebenen Position steht und dessen Aussehen dem übergebenen Spritesheet entspricht
     */
    public Player(int x, int y, BufferedImage image) {
        super("Player", image, x, y, Entity.DEFAULT_WIDTH, Entity.DEFAULT_HEIGHT, Player.DEFAULT_HEALTH, Player.DEFAULT_SPEED);
    }
    
    /**
     * berechnet die neue Position des Spielecharakters
     */
    @Override
    public void update() {
        move();
    }
}