import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Ein Tile-Set enthält alle benötigten Kacheln (Tiles), aus denen der Hintergrund des Spielfelds 
 * zusammengebaut werden kann.
 * @version 0.01 (10.05.2019)
 * @author Jakob Kleine, Cashen Adkins, www.quizdroid.wordpress.com
 * @since 0.01 (10.05.2019)
 */
public class TileSet 
{
    /**
     * Die Breite einer Kachel in Pixeln
     */
    public static final int TILE_WIDTH = 64;
    
    /**
     * Die Höhe einer Kachel in Pixeln
     */
    public static final int TILE_HEIGHT = 64;
    private BufferedImage[] tiles; //Alle Kacheln aus dem angegebenen Tile-Set, als einzelne Bilder ausgeschnitten

    /**
     * Erstellt ein neues TileSet, wobie die einzelnen Kacheln aus dem angegebenen Tile-Set ausgeschnitten werden
     * @author Jakob Kleine, Cashen Adkins, www.quizdroid.wordpress.com
     * @param path der relative Pfad zur Datei des Tile-Sets, aus dem die Kacheln ausgelesen werden sollen
     * @param sizeX die Anzahl der Tiles in einer Reihe
     * @param sizeX die Anzahl an Reihen der Tiles in dem Set
     * @since 0.01 (10.05.2019)
     */
    public TileSet(String path, int sizeX, int sizeY, int border) 
    {
        tiles = new BufferedImage[sizeX * sizeY]; //Insgesamt gibt es sizeX*sizeY Bilder, die einzeln gespeichert werden sollen
        BufferedImage tileSet = null;
        try {
            tileSet = ImageIO.read(Utils.absoluteFileOf(path)); //Versucht, die Datei mit dem Tile-Set zu lesen
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        if(tileSet != null) { //Anders als im Tutorial; Damit die Programmierung strukturiert bleibt
            //Alle einzelnen Kacheln werden aus dem Tile-Set ausgeschnitten
            int i = 0; //Der Index des Sub-Bilds im tiles-Array
            for(int y = 0; y < sizeY; y++) { //Für alle Reihen: 
                for(int x = 0; x < sizeX; x++) { //Für alle Spalten: Schneide ein Bild aus
                    //x * (TILE_WIDTH + 3) entspricht der "Koordinate" des auszuschneidenden Bilds: (die Länge eines Tiles + Abstand von 3px) * Anzahl der Tiles vor diesem Tile
                    //y * (TILE_HEIGHT+ 3) entspricht x*(...)
                    tiles[i++] = tileSet.getSubimage(x * (TILE_WIDTH + border), y * (TILE_HEIGHT + border), TILE_WIDTH, TILE_HEIGHT);
                }
            }
        }
    }
    
    /**
     * Zeichnet eine Kachel
     * @author Jakob Kleine, Cashen Adkins, www.quizdroid.wordpress.com
     * @param g die Graphics, mit denen die Kachel gemalt wird
     * @param tileNum die Nummer (Index) der Kachel
     * @param x die x-Koordinate der Kachel (linke obere Ecke??)
     * @param y die y-Koordinate der Kachel 
     * @since 0.01 (10.05.2019)
     */
    public void renderTile(Graphics g, int tileNum, int x, int y) {
        g.drawImage(tiles[tileNum], x, y, TILE_WIDTH, TILE_HEIGHT, null);
    }
    //Gideon war hier (:
}