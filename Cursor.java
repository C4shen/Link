import java.awt.Point;

public class Cursor extends Weapon {
    private static final int DEFAULT_WIDTH = 16;
    private static final int DEFAULT_HEIGHT = 16;
    private static final double DEFAULT_ATTACK_SPEED = 3;
    
    private boolean isAttacking;
    private double startX;
    private double startY;
    /**
     * Erstellt einen neuen Cursor
     * @param spriteSheet das SpriteSheet des Cursors
     * @param x die x-Position des Cursors
     * @param y die y-Position des Cursors
     */
    public Cursor(SpriteSheet spriteSheet, int x, int y, double speed) {
        super("Cursor", spriteSheet, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, speed);
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 17.05.2019
     */
    @Override
    public void update() {
        move();
        if(isAttacking) {
            if(entityX == startX && entityY == startY) {
                stopAttack();
            }
            else {
                speed -= 0.000001;
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
        speed = Cursor.DEFAULT_SPEED;
    }
}    