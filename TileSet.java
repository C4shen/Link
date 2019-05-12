import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Ein Tile-Set enthält alle benötigten Kacheln (Tiles), aus denen der Hintergrund des Spielfelds 
 * zusammengebaut werden kann.
 * @version 0.01 (10.05.2019)
 * @author Jakob Kleine, Cashen Adkins, www.quizdroid.wordpress.com
 */
public class TileSet 
{
    /**
     * Die Breite eines Tiles  
     */
    public static final int TILEWIDTH = 64;
    
    /**
     * Die Höhe eines Tiles  
     */
    public static final int TILEHEIGHT = 64;
    private BufferedImage[] tiles; //Alle Kacheln aus TileSet, als einzelne Bilder ausgeschnitten

    /**
     * Erstellt ein neues TileSet, wobie die einzelnen Kacheln aus dem angegebenen Tile-Set ausgeschnitten werden
     * @author Jakob Kleine, Cashen Adkins
     * @param path der relative Pfad zur Datei des TileSets
     * @param sizeX die Anzahl der Tiles in einer Reihe
     * @param sizeX die Anzahl Reihen der Tiles in dem Set
     * @since 0.01 (10.05.2019)
     */
    public TileSet(String path, int sizeX, int sizeY) 
    {
        tiles = new BufferedImage[sizeX * sizeY];
        BufferedImage tileSet;
        try {
            tileSet = ImageIO.read(Utils.absolutePathOf(path));
        } 
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        int i = 0;
        for(int y = 0; y < sizeY; y++) {
            for(int x = 0; x < sizeX; x++) {
                tiles[i++] = tileSet.getSubimage(x * (TILEWIDTH + 3), y * (TILEHEIGHT + 3),
                TILEWIDTH, TILEHEIGHT);
            }
        }
    }
    
    /**
     * Zeichnet eine Kachel
     * @author Jakob Kleine, Cashen Adkins
     * @param g die Graphics, mit denen die Kachel gemalt wird
     * @param tileNum die Nummer (Index) der Kachel
     * @param x die x-Koordinate der Kachel (linke obere Ecke??)
     * @param y die y-Koordinate der Kachel 
     * @since 0.01 (10.05.2019)
     */
    public void renderTile(Graphics g, int tileNum, int x, int y) {
        g.drawImage(tiles[tileNum], x, y, TILEWIDTH, TILEHEIGHT, null);
    }
}