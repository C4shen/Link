/**
 * Gegner sind solche Kreaturen, die dem Spieler Schaden zufügen.
 * 
 * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
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
    public Enemy(int x, int y, SpriteSheet enemySprite, int health, double speed) 
    {
        super(enemySprite, x, y, Entity.DEFAULT_WIDTH, Entity.DEFAULT_HEIGHT, health, speed);
    }
    
    /**
     * Jeder Gegner ist unterschiedlich schwer zu besiegen. Deswegen besitzt jede Gegner-Art einen eigenen Wert für den Score, der mit
     * dieser Methode ermittelt wird.
     * @return der Wert, um den der Score erhöht werden soll, wenn dieser Gegner getötet wurde
     * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
     * @since 24.05.2019
     */
    public abstract int getScoreValue();
    
    /**
     * Teilt dem Gegner vor dem <code>update</code> mit, wo der Spieler ist, sodass er seine Richtung ändern kann, um 
     * den Spieler anzuvisieren, oder sodass er den Spieler direkt angreifen kann, wenn möglich.
     * Weil jede Gegner-Art ein eigenes Verhalten aufweist, wird diese Methode von allen Gegner-Klassen unterschiedlich
     * implementiert.
     * @param player der Spieler, den der Gegner anvisieren soll
     * @return die Waffe, die für den Angriff verwendet wurde, sodass die Kollision in der Game-Klasse überprüft werden kann.
     *         null, wenn der Gegner nicht angegriffen hat.
     * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
     * @since 24.05.2019
     */
    public abstract Weapon target(Player player);
}
