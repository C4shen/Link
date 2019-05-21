import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

/**
 * Eine Kreatur ist eine bewegbare Entität, die sich auf dem Spielfeld bewegen kann, und Lebenspunkte besitzt.
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
    
    private Weapon weapon;
    
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
    
    public void startAttack() {
        Point weaponDirection;
        switch(prevDirection){
            case 0: weaponDirection = new Point(0,1); break;
            case 1: weaponDirection = new Point(-1,0); break;
            case 2: weaponDirection = new Point(1,0); break;
            default: weaponDirection = new Point(0,-1); break;
        }
        setMove(new Point(0,0));
        weapon.startAttack(weaponDirection);
    }
    
    /**
     * Berechnet die neue Position des Spielcharakters
     * @author Ares Zühlke, Janni Röbbecke, www.quizdroid.wordpress.com
     * @since 0.01 (10.05.2019)
     */
    @Override
    public void update() {
        move();
        if(weapon.isAttacking())
            weapon.update();
        else
            weapon.setPositionAccordingly(entityX, entityY, xPos, prevDirection);
    }
    
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
}