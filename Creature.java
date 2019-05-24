import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;

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
    
    /**
     * Die Waffe der Figur
     */
    protected Weapon weapon;
    
    /**
     * Das Knockback, das die Figur erleidet. null, wenn kein Knockback erfolgen soll
     */
    protected Knockback knockback;
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
     * @return die Waffe, mit der der Angriff begonnen wird. Ihre Kollisiion mit einem Gegner wird in der Spiel-Klasse verwaltet.
     *         <br>null, wenn die Waffe des Spielers sich gerade im Angriff befindet.
     * @see ---
     */
    public Weapon startAttack() {
        if(weapon.isAttacking()) 
            return null; //Kein Angriff wurde ausgeführt, es gibt keine neue Waffe
        else {
            Point weaponDirection; //Die Richtung, in die der Angriff erfolgt, entspricht der Blickrichtung der Kreatur
            switch(prevDirection){ //prevDir wird verwendet, weil auch im Stand die Richtung der vorherigen Bewegung verwendet werden soll
                case 0: weaponDirection = new Point(0,1); break; //0: Süden 
                case 1: weaponDirection = new Point(-1,0); break;//1: Westen
                case 2: weaponDirection = new Point(1,0); break; //2: Osten
                default: weaponDirection = new Point(0,-1); break;//3: Norden
            }
            setMove(new Point(0,0)); //Während dem Angriff bewegt sich die Figur nicht
            weapon.startAttack(weaponDirection); //Dann beginnt der Angriff
            return weapon;
        }
    }
    
    /**
     * Teilt der Kreatur mit, dass sie angegriffen wurde, sodass sie entsprechend reagieren kann
     * @param attackingWeapon die Waffe, mit der die Kreatur angegriffen wurde
     * @author Jakob Kleine, Janni Röbbecke
     * @since 23.05.2019
     */
    public abstract void startBeingAttacked(Weapon attackingWeapon);
    
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
        if(knockback != null) //Wenn die Kreatur gerade Knockback erleidet, wird sie leicht rot gefärbt
            image = Utils.dyeImage(image, Color.RED, 20); //Es wäre Performance-technisch sicher besser, alle Bilder einmal zu färben und dann zu speichern
            
        if(weaponBehind()){
            weapon.render(g); //Wenn die Waffe hinter der Kreatur sein soll, wird sie zuerst gemalt, die 
            super.render(g); //Die Kreatur malt sich dann "drüber"
        }
        else{
            super.render(g); //Sonst andersrum
            weapon.render(g);
        }
    }
    
    /**
     * Ändert die Bewegungsrichtung der Kreatur, wenn ihre Waffe gerade nicht angreift (dann darf sich die Kreatur nicht bewegen).
     * @author Janni Röbbecke, Jakob Kleine
     * @since 22.05.2019
     * @param p ein Punkt, der die neue Bewegungsrichtung angibt: 
     *          <ul>
     *              <li>x := Bewegung in x-Richtung
     *                  <ul>
     *                      <li>-1 -> Bewegung nach rechts</li>
     *                      <li> 0 -> keine Bewegung</li>
     *                      <li>+1 -> Bewegung nach links</li>
     *                  </ul>
     *              </li>
     *              <li>y := Bewegung in y-Richgung
     *                  <ul>
     *                      <li>-1 -> Bewegung nach oben</li>
     *                      <li> 0 -> keine Bewegung</li>
     *                      <li>+1 -> Bewegung nach unten</li>
     *                  </ul>
     *              </li>
     *          </ul>
     */
    public void setMove(Point p){
        if(!weapon.isAttacking()){
            super.setMove(p);
        }
    }
    
    /**
     * Ermittelt, ob die Waffe der Kreatur vor ihr, oder hinter hier gerendert werden soll.
     * Das wird von jeder Kreatur selbst bestimmt und hängt von dem Bild der aktuellen Position 
     * oder der Bewegungsrichtung ab.
     * @author Janni Röbbecke, Jakob Kleine
     * @since 22.05.2019
     * @return true, wenn die Waffe hinter der Kreatur gerendert werden soll
     */
    protected abstract boolean weaponBehind();
    
    /**
     * Ermittelt die Position der Hand der Kreatur, bzw. der Stelle, an der die Kreatur ihre Waffe hält.
     * Das wird von jeder Kreatur selbst bestimmt und hängt von dem Bild der aktuellen Position 
     * oder der Bewegungsrichtung ab.
     * @author Janni Röbbecke, Jakob Kleine
     * @since 22.05.2019
     * @param xPos die Pose der aktuellen Bewegung
     * @param direction die Richtung der aktuellen Bewegung
     * @return einen Punkt (x|y), dessen Koordinaten denen der Hand der Kreatur entsprechen
     */
    public abstract Point getHandPosition(int xPos, int direction);
}