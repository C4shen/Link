import java.awt.Point;
/**
 * @author 
 * @version 
 */
public class Krebs extends Enemy
{
    /**
     * Standard-Lebenspunkte, die der Spielcharakter zu Beginn des Spiels hat
     */
    public static final int DEFAULT_HEALTH = 100;
    /**
     * Standard-Geschwindigkeit des Spielcharakters
     */
    public static final int DEFAULT_SPEED = 5;
    
    public Krebs(int x, int y, String name, SpriteSheet enemySprite) 
    {
        super(x, y, "Krebs", enemySprite, DEFAULT_HEALTH, DEFAULT_SPEED);
        setMove(new Point(-1, 0));
    }
    
    @Override
    public void update()
    {
        if(entityX<20)
            setMove(new Point(1, 0));
        if(entityX>400)
            setMove(new Point(-1, 0));
        move();
    }
    
    /**
     * Ändert die Bewegungsrichtung der Spielfigur
     * @author Ares Zühlke, Janni Röbbecke, www.qizdroid.wordpress.com
     * @param p Punkt (xP|yP), der die Bewegungsrichtung in x-Richtung (xP) und y-Richtung (yP)angibt
     * @since 0.01 (10.05.2019)
     */
    public void setMove(Point p){
        xMove = p.x;
        yMove = p.y;
    }
}
