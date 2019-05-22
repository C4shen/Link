import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Graphics;

/**
 * Eine Border markiert den Rand des Raumes. Sie besitzt kein Bild und wird nicht gerendert oder upgedatet.
 * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
 * @since 22.05.2019
 */
public class Border extends Entity
{
    //Die Border wird nur über ihre Hitbox definiert
    private Rectangle hitBox; 
    /**
     * Erstellt eine neue Border
     * @param x die x-Koordinate der Border
     * @param y die y-Koordinate der Border
     * @param width die Weite der Border
     * @param height die Höhe der Border
     * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
     * @since 22.05.2019
     */
    public Border(int x, int y, int width, int height) {
        super("Room-Border", null, x, y, width, height);
        hitBox = new Rectangle(x, y, width, height);
    }
    
    /**
     * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
     * @since 22.05.2019
     */
    public Rectangle getHitbox() {
        return hitBox;
    }
      
    /**
     * Die render-Methode sollte nicht aufgerufen werden. Die Border wird nicht angezeigt.
     * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
     * @since 22.05.2019
     */
    @Override
    public void render(Graphics g) { 
        
    }
     
    /**
     * Die render-Methode sollte nicht aufgerufen werden. Die Border besitzt keine Funktionalität
     * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
     * @since 22.05.2019
     */
    @Override
    public void update() {
        
    }
}