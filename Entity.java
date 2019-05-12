import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Jann, Ares, quizdroid
 * (10.05.19)
 */
public abstract class Entity {
    /**
     * Standard Breite einer Figur
     */
    public static final int DEFAULT_WIDTH = 64;
    /**
     * Standard Höhe einer Figur
     */
    public static final int DEFAULT_HEIGHT = 64;
    
    protected String name;
    /**
     * aktuelle x-Position der Figur
     */
    protected int entityX;
    /**
     * aktuelle y-Position der Figur
     */
    protected int entityY;
    /**
     * Breite der Figur
     */
    protected int width;
    /**
     * Höhe der Figur
     */
    protected int height;
    /**
     * Spritesheet mit allen Ansichten der Figur
     */
    protected BufferedImage image;
    
    /**
     * Erzeugt eine Figur (Entität) mit den angegebenen Atributwerten
     */
    public Entity(String name, BufferedImage image, int x, int y, int width, int height) {
        this.name = name;
        this.image = image;
        this.entityX = x;
        this.entityY = y;
        this.width = width;
        this.height = height;
    }
    /**
     * berechnet die neue Position und das neue Aussehen der Figur
     */
    protected abstract void update();
    /**
     * Visualisiert die Figur im Fenster
     */
    protected void render(Graphics g) {
        g.drawImage(image, entityX, entityY, null);
    }
}