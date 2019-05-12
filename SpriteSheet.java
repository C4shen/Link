import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Jann, Ares, quizdroid
 * (10.05.19)
 */
public class SpriteSheet {
    /**
     * Datei mit dem gesamten Sprite-Sheet
     */
    private BufferedImage sheet;
    /**
     * Array mit den versiedenen Ansichten der Figur
     */
    private BufferedImage[][] sprite;
    
    /**
     * Ermittelt aus dem übwergebenen Sprite-Sheet, das die übergebene Höhe und Breite hat und den Charakter in moves 
     * verschiedenen Bewegungen und directions verschiedenen Richtungenn zeigt, die einzelnen Ansichten der Figur 
     * und speichert diese in einem Array
     */
    public SpriteSheet(String path, int moves, int directions, int width, int height){
        sprite = new BufferedImage[moves][directions];
        try {
            sheet = ImageIO.read(Game.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        for(int horiz = 0; horiz < moves; ++horiz) {
            for(int vert = 0; vert < directions; ++vert) {
                sprite[horiz][vert] = sheet.getSubimage(horiz * width, vert * height, width, height);
            }
        }
    }
    /**
     * Giebt das Bild des Charakters in der entsprechenden Pose an 
     */
    public BufferedImage getSpriteElement(int x, int y) {
        return sprite[x][y];
    }
}