import java.awt.Point;

public class Cursor extends Weapon {
    private static final int DEFAULT_WIDTH = 16;
    private static final int DEFAULT_HEIGHT = 16;
    private static final double DEFAULT_ATTACK_SPEED = 4;
    
    private double startX;
    private double startY;
    /**
     * Erstellt einen neuen Cursor
     * @param spriteSheet das SpriteSheet des Cursors
     * @param x die x-Position des Cursors
     * @param y die y-Position des Cursors
     */
    public Cursor(SpriteSheet spriteSheet, int x, int y, double ownerSpeed) {
        super("Cursor", spriteSheet, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, ownerSpeed);
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 17.05.2019
     */
    @Override
    public void update() {
        move();
        if(isAttacking) {
            //if(entityX == startX && entityY == startY) {
            if(beyondStart()){
                stopAttack();
            }
            else {
                speed -= 0.2;
            }
        }
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 17.05.2019
     */
    @Override
    public void startAttack(Point direction) {
        if(!isAttacking) {
            startX = entityX;
            startY = entityY;
            speed = DEFAULT_ATTACK_SPEED;
            setMove(direction);
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
        entityX = startX;
        entityY = startY;
        speed = ownerSpeed;
    }
    
    private boolean beyondStart(){
        return (xMove==0 || (xMove<0 && entityX>=startX) || (xMove>0 && entityX<=startX)) && (yMove==0 || (yMove<0 && entityY>=startY) || (yMove>0 && entityY<=startY));
    }
}    