import java.awt.Point;
/**
 * Ein SideEffect (Nebeneffekt) ist eine Art von Gegner. 
 * Er wird durch einen Krebs verkörpert und kann sich dementsprechend nur entlang der x-Achse bewegen.
 * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
 * @version 0.01 (15.05.2019)
 * @since 15.05.2019
 */
public class SideEffect extends Enemy
{
    private static final Point[][] handPosition = new Point[][]{
        new Point[]{ new Point(25, 20), new Point(14, 26), new Point(18, 23) },
        new Point[]{ new Point(25, 19), new Point(17, 17), new Point(02, 06) },
        new Point[]{ new Point(27, 19), new Point(32, 22), new Point(43, 11) },
        new Point[]{ new Point(32, 18), new Point(37, 26), new Point(34, 22) }
    };
    
    /**
     * Standard-Lebenspunkte, die der Spielcharakter zu Beginn des Spiels hat
     */
    public static final int DEFAULT_HEALTH = 100;
    /**
     * Standard-Geschwindigkeit des Spielcharakters
     */
    public static final int DEFAULT_SPEED = 5;
    
    /**
     * Ertellt einen neuen Nebeneffekt
     * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
     * @param x die x-Koordinate, auf der der Nebeneffekt gespawnt wird
     * @param y die y-Koordinate, auf der der Nebeneffekt gespawnt wird
     * @param enemySprite das SpriteSheet des Nebeneffekts
     * @since 15.05.2019
     */
    public SideEffect(int x, int y, SpriteSheet enemySprite) 
    {
        super(x, y, "Krebs", enemySprite, DEFAULT_HEALTH, DEFAULT_SPEED, 
                new Cursor(new SpriteSheet("/res/sprites/weapons/cursor.png", 3 /*moves*/, 4 /*directions*/, 16 /*width*/, 16 /*height*/), x+10, y+30, DEFAULT_SPEED));
        setMove(new Point(-1, 0)); //Zu Beginn bewegt sich der Side-Effect immer nach links
    }
    
    /**
     * Aktualisiert die Bewegung des Nebeneffekts. Er bewegt sich nur entlang der 
     * x-Achse.
     * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
     * @since 15.05.2019
     */
    @Override
    public void update()
    {
        if(entityX<20)
            setMove(new Point(1, 0));
        if(entityX>550)
            setMove(new Point(-1, 0));
        super.update();
    }
    
    public Point getHandPosition(int xPos, int direction){
        return handPosition[xPos][direction];
    }
}
