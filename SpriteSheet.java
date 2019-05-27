import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Ein SpriteSheet ist eine Sammlung von Bildern, die eine Creature in verschiedenen Positionen aus verschiedenen Ansichten zeigen
 * @author Janni Röbbecke, Ares Zühlke, www.quizdroid.wordpress.com
 * @version 0.01 (10.05.2019)
 * @since 0.01 (10.05.2019)
 */
public class SpriteSheet {    
    /*
     * Array mit den versiedenen Ansichten der Figur: 
     * Für jede Position (1. Dimension) ein Bild für jede Richtung (2.Dimension) 
     */
    private BufferedImage[][] sprite;
    
    /**
     * Erstellt ein neues Srite-Sheet-Objekt, wobei die einzelnen Ansichten der Figur aus Bild am angegebenen
     * Pfad ausgelesen werden.
     * 
     * @author Janni Röbbecke, Ares Zühlke, www.quizdroid.wordpress.com
     * @param path der Pfad, bei dem das Sprite-Sheet liegt
     * @param moves die Anzahl der Bewegungen des Charakters
     * @param moves die Anzahl der Richtungen der Bewegungen
     * @param width die Breite (px) des Charakters
     * @param height die Höhe (px) des Charakters
     * @since 0.01 (10.05.2019)
     */
    public SpriteSheet(String path, int moves, int directions, int width, int height){
        sprite = new BufferedImage[moves][directions];
        BufferedImage sheet = null; //Gesamtes Bild mit allen Ansichten und Positionen 
        try {
            sheet = ImageIO.read(Utils.absoluteFileOf(path));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if(sheet != null) { //Wenn das Sheet gefunden wurde
            for(int horiz = 0; horiz < moves; ++horiz) {
                for(int vert = 0; vert < directions; ++vert) {
                    sprite[horiz][vert] = sheet.getSubimage(horiz * width, vert * height, width, height); //Kopiert das einzelne Bild aus dem Tile-Set
                }
            }
        }
    }
    
    /**
     * Gibt die Anazhl aller möglichen Positionen an, die in dem SpriteSheet gespeichert wurden
     * @return die Anzahl aller möglichen Positionen 
     * @author Janni Röbbecke, Jakob Kleine
     * @since 24.05.2019
     */
    public int getPoseAmount() {
        return sprite.length;
    }
    
    /**
     * Gibt das Bild des Charakters in der entsprechenden Pose und Richtung an 
     * @author Janni Röbbecke, Ares Zühlke, www.quizdroid.wordpress.com
     * @param x die Bewegung des Charakters
     * @param y die Richtung der Bewegung
     * @return ein BufferedImage, das die gewünschte Pose abbildet
     * @since 0.01 (10.05.2019)
     */
    public BufferedImage getSpriteElement(int x, int y) {
        return sprite[x][y];
    }
}