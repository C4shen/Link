import java.awt.Graphics;

/**
 * Entspricht der Klasse Level des Tutorials. Für unser Spiel eignet sich allerdings der Name Raum mehr,
 * weil verschiedene Level auch im gleichen Raum stattfinden können (und i.d.R auch nur ein Standard-Raum verwendet wird). 
 * Ein Room wird durch die verwendeten Kacheln des TileSets und seiner Größe definiert.
 * Rooms werden mithilfe von Text-Dateien im Ordner ../res/rooms beschrieben.
 * @author Jakob Kleine, Cepehr Bromand, www.quizdroid.wordpress.com
 * @version 0.01 (12.05.2019)
 * @since 0.01 (12.05.2019)
 */
public class Room {
    private TileSet ts; //Das TileSet, aus dem die Kacheln gewählt werden
    private int sizeX, sizeY; //Die Anzahl der Kacheln in x- bzw. y-Richtung
    private int[][] tileMap; //Für jede Spalte (x; 1.Dimension) für jede Reihe (y; 2.Dimension) der Index der Kachel im TileSet

    /**
     * Erstellt ein neues Level
     * @author Jakob Kleine, Cepehr Bromand, www.quizdroid.wordpress.com
     * @param path der relative Pfad der Text-Datei, die das Level beschreibt
     * @param ts das TileSet, das die Kacheln für das Level enthält
     * @since 0.01 (12.05.2019)
     */
    public Room(String path, TileSet ts) {
        this.ts = ts;
        String file = Utils.loadFileAsString(path); //Liest das Level ein
        String[] tokens = file.split("\\s+"); //Trennt den eingelesenen String an den Leerzeichen und Zeilenumbrüchen
        sizeX = Utils.parseInt(tokens[0]); //Die erste Zahl gibt die Anzahl der Kacheln in x-Richtung an
        sizeY = Utils.parseInt(tokens[1]); //Die zweite Zahl gibt die Anzahl der Kacheln in y-Richtung an
        tileMap = new int[sizeX][sizeY];
        int i = 2; //Die ersten beiden Zahlen geben die größe des Levels an -> Beginn erst ab Zahl 3
        for(int y = 0; y < sizeY; y++){
            for(int x = 0; x < sizeX; x++){
                tileMap[x][y] = Utils.parseInt(tokens[i++]);
            }
        }
    }

    /**
     * Visualisiert das Level, indem die Kacheln an die richtigen Stellen gemalt werden 
     * @author Jakob Kleine, Cepehr Bromand, www.quizdroid.wordpress.com
     * @param g die Graphics, mit denen die Kacheln gemalt werden sollen
     * @since 0.01 (12.05.2019)
     */
    public void renderMap(Graphics g){
        for(int tileY = 0; tileY < sizeY; tileY++){
            for(int tileX = 0; tileX < sizeX; tileX++){
                //tileX*TileSet.TILE_WIDTH entspricht der x-Koordinate der zu malenden Kachel
                //Die y-Koordinate wird um so viel erhöht (nach unten verschoben), wie die Lebensanzeige usw. Platz benötigt
                ts.renderTile(g, tileMap[tileX][tileY], tileX * TileSet.TILE_WIDTH, tileY * TileSet.TILE_HEIGHT + Game.HP_BAR_HEIGHT);
            }
        }
    }
}