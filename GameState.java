import java.awt.Canvas;
import java.awt.Graphics;
import java.util.LinkedList;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
/**
  * @author Cashen Adkins,Janni Röbbecke, www.quizdroid.wordpress.com
  * @version 0.02 (22.05.2019)
  * @since 0.01 (22.05.2019
  */
public class GameState extends State 
{
    private Player player; //Die Spielfigur des Spielers
    private LinkedList<Enemy> gegnerListe; //Eine Liste mit allen Gegnern im Spiel
    private Room room; //Der Raum, der gerade gespielt wird
    public GameState(Game game)
    {
        super(game);
        SpriteSheet playerSprite = new SpriteSheet("/res/sprites/player1.png", 3 /*moves*/, 4 /*directions*/, 64 /*width*/, 64 /*height*/);
        player = new Player(320, 320, playerSprite);
        gegnerListe = new LinkedList<Enemy>();
        TileSet tileSet = new TileSet("/res/tilesets/standard-raum-ts.png", 3, 3);
        room = new Room("/res/rooms/standard-raum.txt", tileSet);
        SpriteSheet krebsSprite = new SpriteSheet("/res/sprites/krebs.png", 3 /*moves*/, 4 /*directions*/, 64 /*width*/, 64 /*height*/);
        gegnerListe.add(new SideEffect(150, 150, krebsSprite));
    }
    
    @Override
    public void render(Graphics g) 
    {
        Canvas c = game.getCanvas();
        //c.setBackground(Color.blue);
        BufferStrategy bs = c.getBufferStrategy();
        if(bs == null)
            game.getCanvas().createBufferStrategy(3);
        else{
            g = bs.getDrawGraphics();
            //Clear Screen
            g.clearRect(0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
            room.renderMap(g); // Erst die Spielfläche ...
            player.render(g); // ... und darauf die Spielfigur
            for(Enemy e :  gegnerListe) {
                e.render(g);
            }
            bs.show();
            g.dispose();
        }
    }
    
    @Override
    public void update() 
    {
        player.setMove(game.getInput()); //Bewegt den Spieler entsprechend der Eingabe über die Tasten
        if(game.getKeyManager().attack()) //Wenn die Taste zum Angriff gedrückt wurde, greift der Spieler an
            player.startAttack();
        player.update();
        for(Enemy e : gegnerListe) {
            e.update();
        }
    }
    
}
    