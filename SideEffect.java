import java.awt.Point;
import java.awt.Rectangle;
/**
 * Ein SideEffect (Nebeneffekt) ist eine Art von Gegner. 
 * Er wird durch einen Krebs verkörpert und kann sich dementsprechend nur entlang der x-Achse bewegen.
 * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
 * @version 0.01 (15.05.2019)
 * @since 15.05.2019
 */
public class SideEffect extends Enemy
{
    //Noch nicht bestimmt
    private static final Point[][] handPosition = new Point[][]{
        new Point[]{ new Point(25, 20), new Point(14, 26), new Point(18, 23) },
        new Point[]{ new Point(25, 19), new Point(17, 17), new Point(02, 06) },
        new Point[]{ new Point(27, 19), new Point(32, 22), new Point(43, 11) },
        new Point[]{ new Point(32, 18), new Point(37, 26), new Point(34, 22) }
    };
    
    /*
     * Das Standart-Spritesheet-Objekt, auf das alle Objekte dieser Klasse als ihr SpriteSheet referenzieren. 
     * Dadurch, dass es ein Standard-Bild gibt, müssen die gleichen Bilder nicht mehrmals geladen werden.
     */
    private static final SpriteSheet DEFAULT_SPRITE_SHEET = new SpriteSheet("/res/sprites/creatures/sideEffect.png", 3 /*moves*/, 4 /*directions*/, 64 /*width*/, 64 /*height*/);
    
    /**
     * Standard-Lebenspunkte, die der Spielcharakter zu Beginn des Spiels hat
     */
    public static final int DEFAULT_HEALTH = 20;
    
    /**
     * Standard-Geschwindigkeit des Spielcharakters
     */
    public static final int DEFAULT_SPEED = 5;
    
    public static final int DEFAULT_SCORE_VALUE = 50;
    /**
     * Ertellt einen neuen Nebeneffekt
     * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
     * @param x die x-Koordinate, auf der der Nebeneffekt gespawnt wird
     * @param y die y-Koordinate, auf der der Nebeneffekt gespawnt wird
     * @param enemySprite das SpriteSheet des Nebeneffekts
     * @since 15.05.2019
     */
    public SideEffect(int x, int y) 
    {
        super(x, y, "Krebs", DEFAULT_SPRITE_SHEET, DEFAULT_HEALTH, DEFAULT_SPEED, new Cursor(x+10, y+30, DEFAULT_SPEED, false));
        setMove(new Point(-1, 0)); //Zu Beginn bewegt sich der Side-Effect immer nach links
        health = SideEffect.DEFAULT_HEALTH;
    }
   
    @Override
    public void startBeingAttacked(Weapon attackingWeapon) {
        super.startBeingAttacked(attackingWeapon);
        if(knockback.getDirectionX() != 0) 
            xMove = knockback.getDirectionX();
    }
    
    //Noch nicht bestimmt
    public boolean weaponBehind(){ return false; }
    
    /**
     * @author Janni Röbbecke, Jakob Kleine
     * @since 22.05.2019
     */
    public Point getHandPosition(int xPos, int direction){
        return new Point(handPosition[xPos][direction].x + (int) Math.round(entityX), handPosition[xPos][direction].y + (int) Math.round(entityY));
    }
    
    /**
     * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
     * @since 22.05.2019
     */
    public Rectangle getHitbox() {
        return new Rectangle((int) Math.round(entityX), (int) Math.round(entityY), 64, 64);
    }
    
    public int getScoreValue() {
        return DEFAULT_SCORE_VALUE;
    }
    
    public Weapon target(Player player){
        if(player.getHitbox().intersects(getHitbox())){
            return startAttack();
        }
        else {
            if(xMove == 0){
                if(player.entityX < entityX)setMove(new Point(-1,0));
                else setMove(new Point(1,0));
            }
            if(entityX<20)
                setMove(new Point(1, 0));
            if(entityX>550)
                setMove(new Point(-1, 0));
            return null;
        }
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 26.05.2019
     */
    public double getDefaultSpeed() {
        return DEFAULT_SPEED;
    }
}
