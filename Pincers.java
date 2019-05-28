import java.awt.Point;
import java.awt.Rectangle;
/**
 * Die Scheren sind die Waffen eines Side-Effects.
 */
public class Pincers extends Weapon 
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
    private static final int DEFAULT_ATTACK_DELAY = 30;

    /**
     * Die Waffen werden immer relativ zu ihren Besitzern positioniert. 
     * Der Cursor ist eine Waffe des Players. Das sind also die zusätzlichen Werte für die 
     * x-/y-Koordinaten des Cursors in der bestimmten Pose [2.Dimension] einer Bewegungsrichtung [1.Dimension]
     */
    private static final Point[] handelPositions = new Point[]{
        new Point(17, 21),
        new Point(00, 21) 
    };
    
    private int attackDelay;
    public Pincers(int x, int y, boolean isLeft) {
        super("Schere eines Side-Effects", DEFAULT_SPRITE_SHEET, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        prevDirection = isLeft ? 0 : 1;
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 17.05.2019
     */
    @Override
    public void update() {
        if(isAttacking) {
            if(animationDelay-- <= 0) {
                stopAttack();
            }
        }
    }
    
    /**
     * @author Janni Röbbecke, Jakob Kleine
     * @since 22.05.2019
     */
    @Override
    public void setPositionInHand(Point handPosition, int xPosOwner, int yPosOwner) {
        entityX = handPosition.x - handelPositions[prevDirection].x;
        entityY = handPosition.y - handelPositions[prevDirection].y;
        
        setEntityImage(spriteSheet.getSpriteElement(0, prevDirection));
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     */
    public void notifySuccess() {
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 17.05.2019
     */
    @Override
    public void startAttack(Point direction) {
        if(!isAttacking) {
            image = spriteSheet.getSpriteElement(1, prevDirection);
            attackDelay = DEFAULT_ATTACK_DELAY;
        }
    }
    
    /**
     * Beendet den Angriff mit dem Cursor
     * @author Jakob Kleine, Janni Röbbecke
     * @since 17.05.2019
     */
    private void stopAttack() {
        isAttacking = false;
        image = spriteSheet.getSpriteElement(0, prevDirection); //Das könnte eigentlich länger sein, dmit man das besser sieht
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