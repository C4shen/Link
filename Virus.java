import java.awt.Point;
import java.awt.Rectangle;

/**
 * Write a description of class Virus here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Virus extends Enemy
{
    //Noch nicht bestimmt
    private static final Point[][] handPosition = new Point[][]{
        new Point[]{ new Point(25, 20), new Point(14, 26), new Point(18, 23) },
        new Point[]{ new Point(25, 19), new Point(17, 17), new Point(02, 06) },
        new Point[]{ new Point(27, 19), new Point(32, 22), new Point(43, 11) },
        new Point[]{ new Point(32, 18), new Point(37, 26), new Point(34, 22) }
    };
    
    /**
     * Standard-Lebenspunkte, die der Spielcharakter zu Beginn des Spiels hat
     */
    public static final int DEFAULT_HEALTH = 50;
    /**
     * Standard-Geschwindigkeit des Spielcharakters
     */
    public static final int DEFAULT_SPEED = 1;
    
    public static final int DEFAULT_SCORE_VALUE = 200;
    /**
     * Ertellt einen neuen Nebeneffekt
     * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
     * @param x die x-Koordinate, auf der der Nebeneffekt gespawnt wird
     * @param y die y-Koordinate, auf der der Nebeneffekt gespawnt wird
     * @param enemySprite das SpriteSheet des Nebeneffekts
     * @since 15.05.2019
     */
    public Virus(int x, int y, SpriteSheet enemySprite) 
    {
        super(x, y, "Virus", enemySprite, DEFAULT_HEALTH, DEFAULT_SPEED, 
                new Cursor(new SpriteSheet("/res/sprites/weapons/cursor.png", 3 /*moves*/, 8 /*directions*/, 16 /*width*/, 16 /*height*/), x+10, y+30, DEFAULT_SPEED, false));
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
        return new Rectangle((int) Math.round(entityX), (int) Math.round(entityY), 25, 48);
    }
    
    public int getScoreValue() {
        return DEFAULT_SCORE_VALUE;
    }
    
    public Weapon target(Player player){
        if(player.getHitbox().intersects(getHitbox())){
            return startAttack();
        }
        else {
            double xAbstand = player.entityX - entityX;
            double yAbstand = player.entityY - entityY;
            setMove(xAbstand/yAbstand, yAbstand/xAbstand);
            
            return null;
        }
    }
}
