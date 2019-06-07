import java.awt.Graphics;
import java.awt.Rectangle;

public class TextField
{
    private static final int DEFAULT_TILE_WIDH = 64;
    private static final int DEFAULT_TILE_HEIGHT = 64;
    private static final TileSet tileSet = new TileSet("/res/tilesets/standard-raum-ts.png", 3 /*Anzahl Tiles x*/, 1/*Anzahl Tiles y*/, 3/*Abstand zwischen Tiles*/);
    private int anzahlTiles;
    private String text;
    private String placeholder;
    
    public TextField(String ph, int x, int y, int width) {
        placeholder = ph;
        anzahlTiles = (int) width / DEFAULT_TILE_WIDH;
        if(width % DEFAULT_TILE_WIDH != 0) 
            anzahlTiles ++;
    }
    
    public void update() {
        
    }
    
    public void render(Graphics g) {
        tileSet.renderTile(g, 0, TileSet.TILE_WIDTH, TileSet.TILE_HEIGHT);
        for(int tileX = 0; tileX < anzahlTiles-2; tileX++){
            //tileX*TileSet.TILE_WIDTH entspricht der x-Koordinate der zu malenden Kachel
            //Die y-Koordinate wird um so viel erhöht (nach unten verschoben), wie die Lebensanzeige usw. Platz benötigt
            tileSet.renderTile(g, 1, tileX * TileSet.TILE_WIDTH, TileSet.TILE_HEIGHT);
        }
        tileSet.renderTile(g, 2, (anzahlTiles-2) * TileSet.TILE_WIDTH, TileSet.TILE_HEIGHT);
    }
}