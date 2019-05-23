import java.awt.Canvas;
import java.awt.Graphics;
import java.util.LinkedList;
import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
  * @author Cashen Adkins,Janni Röbbecke, www.quizdroid.wordpress.com
  * @version 0.02 (22.05.2019)
  * @since 0.01 (22.05.2019
  */
public class GameState extends State 
{
    private Player player; //Die Spielfigur des Spielers
    private LinkedList<Enemy> gegnerListe; //Eine Liste mit allen Gegnern im Spiel
    private LinkedList<Weapon> attackingWeapons; //Die Waffen, die sich gerade im Angriff befinden
    private Border[] roomBorders; //Die Wände des Raums
    private Room room; //Der Raum, der gerade gespielt wird
    public GameState(Game game)
    {
        super(game);
        SpriteSheet playerSprite = new SpriteSheet("/res/sprites/player1.png", 3 /*moves*/, 4 /*directions*/, 64 /*width*/, 64 /*height*/);
        player = new Player(320, 320, playerSprite);
        gegnerListe = new LinkedList<Enemy>();
        attackingWeapons = new LinkedList<Weapon>();
        TileSet tileSet = new TileSet("/res/tilesets/standard-raum-ts.png", 3, 3);
        room = new Room("/res/rooms/standard-raum.txt", tileSet);
        SpriteSheet krebsSprite = new SpriteSheet("/res/sprites/krebs.png", 3 /*moves*/, 4 /*directions*/, 64 /*width*/, 64 /*height*/);
        gegnerListe.add(new SideEffect(150, 150, krebsSprite));
        
        roomBorders = new Border[] {
            new Border(                    0,    Game.HP_BAR_HEIGHT,   Game.SCREEN_WIDTH, Border.BORDER_WIDTH), //links oben -> rechts oben
            new Border(                    0, Game.SCREEN_HEIGHT-10,   Game.SCREEN_WIDTH, Border.BORDER_WIDTH), //links unten -> rechts unten
            new Border(                    0,    Game.HP_BAR_HEIGHT, Border.BORDER_WIDTH,   Game.SCREEN_WIDTH), //links oben -> links unten
            new Border( Game.SCREEN_WIDTH-10,    Game.HP_BAR_HEIGHT, Border.BORDER_WIDTH,   Game.SCREEN_WIDTH) //rechts oben -> rechts unten
        };
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
            for(Enemy e :  gegnerListe)
                e.render(g);
            /*
            hitBoxAnzeigen(player, g);
            for(Enemy e :  gegnerListe)
                hitBoxAnzeigen(e, g);
            for(Weapon w : attackingWeapons) 
                hitBoxAnzeigen(w, g);
            for(Border b : roomBorders) 
                hitBoxAnzeigen(b, g);
            */
            bs.show();
            g.dispose();
        }
    }
      
    private void hitBoxAnzeigen(Entity e, Graphics g) {
        g.drawRect(e.getHitbox().x, e.getHitbox().y, e.getHitbox().width, e.getHitbox().height); //Nur temporär, um die Hitbox anzuzeigen
    }
    
    @Override
    public void update() 
    {
        player.setMove(game.getInput()); //Bewegt den Spieler entsprechend der Eingabe über die Tasten
        if(game.getKeyManager().attack()) { //Wenn die Taste zum Angriff gedrückt wurde, greift der Spieler an
            Weapon attackingWeapon = player.startAttack();
            if(attackingWeapon != null) //Wenn ein neuer Angriff ausgeführt wurde
                attackingWeapons.add(attackingWeapon); //Speichert die Waffe, um Kollisionen mit Gegnern zu prüfen
        }
        player.update();
        for(Enemy e : gegnerListe) {
            e.update();
        }
        
        for(Weapon w : attackingWeapons) {
            if(!w.isAttacking())
                attackingWeapons.remove(w);
            else {//Auf Kollision prüfen
                LinkedList<Entity> getroffeneGegner = collidesWith(w, gegnerListe);
                for(Entity e : getroffeneGegner) {
                    ((Enemy) e).startBeingAttacked(w);
                }
            }
        }
        
        keepInside(player);
        for(Weapon w : attackingWeapons)
            keepInside(w);
        for(Enemy e : gegnerListe)
            keepInside(e);
    }
    
    private void keepInside(Movable e) {
        if(collision(e, roomBorders[0])) //Border Oben
            e.setEntityY(Game.HP_BAR_HEIGHT + Border.BORDER_WIDTH);
        else if(collision(e, roomBorders[1])) //Border Unten
            e.setEntityY(Game.SCREEN_HEIGHT - (int) e.getHitbox().getHeight() - Border.BORDER_WIDTH); //Idee für Glitch -> -Borderwidth weglassen
        //Kein else hier, weil auch 2 Borders getroffen werden können
        if(collision(e, roomBorders[2])) //Border Links
            e.setEntityX(Border.BORDER_WIDTH);
        else if(collision(e, roomBorders[3])) //Border Rechts
            e.setEntityX(Game.SCREEN_WIDTH - (int) e.getHitbox().getWidth() - Border.BORDER_WIDTH);
    }
    
    private boolean collision(Entity e1, Entity e2) {
        return e1.getHitbox().intersects(e2.getHitbox());
    }
    
    private LinkedList<Entity> collidesWith(Entity e, LinkedList<Enemy> es) {
        LinkedList<Entity> collidedEntities = new LinkedList<Entity>();
        for(Entity ex : es) {
            if(collision(e, ex))
                collidedEntities.add(ex);
        }
        return collidedEntities;
    }
}
    