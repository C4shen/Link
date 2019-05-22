import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

/**
 * Eine Kreatur ist eine bewegbare Entität (<a href="Entity.html">Movable</a>), die sich auf dem Spielfeld bewegen kann, Lebenspunkte und eine Waffe (<a href="Weapon.html">Weapon</a>) besitzt.
 * @author Janni Röbbecke, Ares Zühlke, Jakob Kleine, www.quizdroid.wordpress.com
 * @version 0.02 (13.05.2019)
 * @since 0.01 (10.05.2019
 */
public abstract class Creature extends Movable {
    /**
     * Standard-Lebenspunkte die eine Figur hat wenn sie im Spiel erscheind
     */
    protected static final int DEFAULT_HEALTH = 10;
    
    /**
     * Lebenspunkte der Figur
     */
    protected int health;
    
    private Weapon weapon; //Die Waffe der Figur
    
    /**
     * Erzeugt eine Kreatur (bzw. im Deutschen schöner Figur)
     * @author Ares Zühlke, Janni Röbbecke, www.quizdroid.wordpress.com
     * @param name der Name der Figur
     * @param spriteSheet ein Spritesheet, das das Aussehen der Figur beschreibt (stimmt das?)
     * @param x die x-Position aus der die Entität gespawnt werden soll
     * @param y die y-Position aus der die Entität gespawnt werden soll
     * @param width die Breite der Figur
     * @param height die Höhe der Figur
     * @param health die HP der Figur
     * @param speed die Geschwindigkeit der Figur
     */
    public Creature(String name, SpriteSheet spriteSheet, int x, int y, int width, int height, int health, double speed, Weapon weapon) {
        super(name, spriteSheet, x, y, width, height, speed);
        this.weapon = weapon;
        this.health = health;
    }
    
    /**
     * Beginnt einen Angriff von dieser Kreatur. Die Kreatur startet also einen Angriff mit ihrer Waffe
     * @author Janni Röbbecke, Jakob Kleine
     * @since 21.05.2019
     */
    public void startAttack() {
        Point weaponDirection; //Die Richtung, in die der Angriff erfolgt, entspricht der Blickrichtung der Kreatur
        switch(prevDirection){ //prevDir wird verwendet, weil auch im Stand die Richtung der vorherigen Bewegung verwendet werden soll
            case 0: weaponDirection = new Point(0,1); break; //0: Süden 
            case 1: weaponDirection = new Point(-1,0); break;//1: Westen
            case 2: weaponDirection = new Point(1,0); break; //2: Osten
            default: weaponDirection = new Point(0,-1); break;//3: Norden
        }
        setMove(new Point(0,0)); //Während dem Angriff bewegt sich die Figur nicht
        weapon.startAttack(weaponDirection); //Dann beginnt der Angriff
    }
    
    /**
     * Berechnet die neue Position der Kreatur. Außerdem muss die Waffe neu Positioniert werden.
     * @author Janni Röbbecke, Jakob Kleine, Ares Zühlke, www.quizdroid.wordpress.com
     * @since 0.01 (10.05.2019)
     */
    @Override
    public void update() {
        move();
        if(weapon.isAttacking()) //Wenn die Waffe sich bewegt, soll sie sich eigenständig updaten, sonst wird ihre Bewegung vorgegeben
            weapon.update();
        else
            weapon.setPositionInHand(getHandPosition(prevDirection,xPos));
    }
    
    /**
     * Zeigt die Kreatur an. Außerdem wird die Waffe angezeigt.
     * @param g die Graphics, mit denen die Kreatur und Waffe gemalt werden
     * @author Janni Röbbecke, Jakob Kleine, Ares Zühlke, www.quizdroid.wordpress.com
     * @since 0.01 (10.05.2019)
     */
    @Override 
    public void render(Graphics g) {
        super.render(g);
        weapon.render(g);
    }
    
    public void setMove(java.awt.Point p ){
        if(!weapon.isAttacking()){
            super.setMove(p);
        }
    }
    
    public abstract Point getHandPosition(int xPos, int direction);
}