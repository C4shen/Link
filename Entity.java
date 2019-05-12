import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * @author Janni Röbbecke, Ares Zühlke, Jakob Kleine, www.quizdroid.wordpress.com
 * @version 0.02 (13.05.2019)
 * @since 0.01 (10.05.2019)
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
     * Bild das die Entität abbildet
     */
    protected BufferedImage image;
    
    /**
     * Erzeugt eine Entität (Figur)
     * @author 
     * @param name der Name der Entität
     * @param image das Bild, das die Entität abbildet
     * @param x die x-Position der Figur
     * @param y die y-Position der Figur
     * @param width die Breite der Figur
     * @param height die Höhe der Figur
     * @since 0.01 (10.05.2019)
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
     * Berechnet die neue Position und das neue Aussehen der Figur
     * @author
     * @since 0.01 (10.05.2019)
     */
    protected abstract void update();
    
    /**
     * Visualisiert die Figur im Fenster
     * @author
     * @param g die Graphics, mit denen das Bild der Figur gezeichnet werden soll
     * @since 0.01 (10.05.2019)
     */
    protected void render(Graphics g) {
        g.drawImage(image, entityX, entityY, null);
    }
    
    /**
     * Ändert das zu rendernde Bild der Entität
     * @author 
     * @param image das neue Bild, das in der Methode <code>render()</code> gemalt werden soll
     * @since 0.02 (13.05.2019)
     */
    protected void setEntityImage(BufferedImage image) {
        this.image = image;
    }
}