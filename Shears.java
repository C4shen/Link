import java.awt.Point;
import java.awt.Rectangle;
/**
 * Die Scheren sind die Waffen eines Side-Effects.
 */
public class Shears extends Weapon 
{
    /*
     * Das Standart-Spritesheet-Objekt, auf das alle Objekte dieser Klasse als ihr SpriteSheet referenzieren. 
     * Dadurch, dass es ein Standard-Bild gibt, müssen die gleichen Bilder nicht mehrmals geladen werden.
     */
    private static final SpriteSheet DEFAULT_SPRITE_SHEET = new SpriteSheet("/res/sprites/weapons/shears.png", 2 /*moves*/, 2 /*directions*/, 17 /*width*/, 21 /*height*/);
    private static final int DEFAULT_WIDTH = 17, DEFAULT_HEIGHT = 21; //Die Standard-Größe
    private static final int KB_AMOUNT = 50; //Die Standard-Weite des Knockbacks
    private static final double KB_STRENGTH = 7; //Die Standard-Stärke des Knockbacks
    private static final int DEFAULT_DAMAGE = 5; //Der Standard-Schaden

    /**
     * Die Waffen werden immer relativ zu ihren Besitzern positioniert. 
     * Der Cursor ist eine Waffe des Players. Das sind also die zusätzlichen Werte für die 
     * x-/y-Koordinaten des Cursors in der bestimmten Pose [2.Dimension] einer Bewegungsrichtung [1.Dimension]
     */
    private static final Point[][] handelPositions = new Point[][]{
        new Point[] { new Point(06, 14), new Point(06, 14), new Point(06, 14) },
        new Point[] { new Point(10, 13), new Point(10, 13), new Point(10, 13) },
        new Point[] { new Point(0, 0), new Point(0,0), new Point(0,0) },
        new Point[] { new Point(0, 0), new Point(0,0), new Point(0,0) }
    };
    
    private double startX; //Die Startposition vor dem Angriff. Der Cursor kehrt hierhin zurück
    private double startY;
    public Shears(int x, int y, double ownerSpeed, boolean isFriendly) {
        super("Scheren eines Side-Effects", DEFAULT_SPRITE_SHEET, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, ownerSpeed);
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 17.05.2019
     */
    @Override
    public void update() {
        if(isAttacking) {
            
        }
        //Sonst muss die Waffe von ihrem Besitzer gelenkt werden
    }
    
    /**
     * @author Janni Röbbecke, Jakob Kleine
     * @since 22.05.2019
     */
    @Override
    public void setPositionInHand(Point handPosition, int xPosOwner, int yPosOwner) {
        entityX = handPosition.x ;//- handelPositions[xPosPlayer][directionPlayer].x;
        entityY = handPosition.y ;//- handelPositions[xPosPlayer][directionPlayer].y;
        
        setEntityImage(spriteSheet.getSpriteElement(xPos, prevDirection));
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     */
    public void notifySuccess() {
        image = spriteSheet.getSpriteElement(0, prevDirection+4); //Das könnte eigentlich länger sein, dmit man das besser sieht
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 17.05.2019
     */
    @Override
    public void startAttack(Point direction) {
        if(!isAttacking) {
            startX = entityX; //Speichert die Position vor dem Angriff
            startY = entityY; 
            speed = 5; //Jetzt bewegt sich der Cursor mit einer eigenen Geschwindigkeit
            setMove(direction); //Der Cursor wird in die angegebene Richtung geworfen
            isAttacking = true;
        }
    }
    
    /**
     * Beendet den Angriff mit dem Cursor
     * @author Jakob Kleine, Janni Röbbecke
     * @since 17.05.2019
     */
    private void stopAttack() {
        isAttacking = false;
        entityX = startX; //Setzt den Cursor sicherheitshalber auf die Startposition zurück
        entityY = startY;
    }
    
    /**
     * Überprüft, ob die Waffe wieder an ihrem Startpunkt angekommen ist, bzw. ob sie schon darüber hinaus (beyond) bewegt wurde
     * @return true, wenn die Position der Waffe ihrer Startposition gleicht, oder sie (in der Bewegungsrichtung der Waffe) größer ist. Sonst false
     */
    private boolean beyondStart(){
        //Wenn keine Bewegung entlang x || Bewegung nach links -> entityX soll >= startX sein || Bewegung nach rechts -> ...
        return (xMove==0 || (xMove<0 && entityX>=startX) || (xMove>0 && entityX<=startX))
                && //y.Richtung muss genauso überprüft werden
               (yMove==0 || (yMove<0 && entityY>=startY) || (yMove>0 && entityY<=startY));
    }

    /**
     * @author Jakob Kleine, Cashen Adkins
     * @since 23.05.2019
     */
    public Knockback getKnockback() {
        return new Knockback(KB_AMOUNT, KB_STRENGTH, xMove, yMove);
    }
    
    /**
     * @author Jakob Kleine, Cashen Adkins
     * @since 23.05.2019
     */
    public int getDamage() {
        return DEFAULT_DAMAGE;
    }
    
    /**
     * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
     * @since 22.05.2019
     */
    public Rectangle getHitbox() {
        return new Rectangle((int) Math.round(entityX), (int) Math.round(entityY), 16, 16);
    }
    
    /**
     * @author Janni Röbbecke, Jakob Kleine
     * @since 26.05.2019
     */
    public boolean isFriendly(){
        return false;
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 26.05.2019
     */
    public double getDefaultSpeed() {
        return 0;
    }
}