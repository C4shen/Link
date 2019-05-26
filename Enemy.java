/**
 * Gegner sind solche Kreaturen, die dem Spieler Schaden zufügen.
 * 
 * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
 * @version 0.01 (15.05.2019)
 * @since 15.05.2019
 */
public abstract class Enemy extends Creature
{
    /**
     * Erstellt einen neuen Gegner 
     * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
     * @param x die x-Koordinate, auf der der Gegner gespawnt wird
     * @param y die y-Koordinate, auf der der Gegner gespawnt wird
     * @param name der Name des Gegners
     * @param enemySprite das SpriteSheet, mit dem der Gegner animiert wird
     * @param health die Lebenspunkte des Gegners
     * @param speed die Geschwindigkeit des Gegners
     * @since 0.01 (15.05.2019)
     */
    public Enemy(int x, int y, String name, SpriteSheet enemySprite, int health, double speed, Weapon weapon) 
    {
        super(name, enemySprite, x, y, Entity.DEFAULT_WIDTH, Entity.DEFAULT_HEIGHT, health, speed);
        this.weapon = weapon; 
    }
    
    public abstract int getScoreValue();
    
    public abstract Weapon target(Player player);
}
