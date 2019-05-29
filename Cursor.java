import java.awt.Point;
import java.awt.Rectangle;
/**
 * Der Cursor ist eine Art von Waffe, die der Spieler einsammeln kann, um Gegner zu bekämpfen.
 * Beim Angriff wird der Cursor geworfen, und trifft eventuell dabei einen Gegner.
 * Der Cursor fliegt dann in die Hand des Spielers zurück.
 */
public class Cursor extends Weapon { 
    /*
     * Das Standart-Spritesheet-Objekt, auf das alle Objekte dieser Klasse als ihr SpriteSheet referenzieren. 
     * Dadurch, dass es ein Standard-Bild gibt, müssen die gleichen Bilder nicht mehrmals geladen werden.
     */
    private static final SpriteSheet DEFAULT_SPRITE_SHEET = new SpriteSheet("/res/sprites/weapons/cursor.png", 3 /*moves*/, 8 /*directions*/, 16 /*width*/, 16 /*height*/);
    private static final int DEFAULT_WIDTH = 16, DEFAULT_HEIGHT = 16; //Die Standard-Größe eines Cursors
    private static final double DEFAULT_ATTACK_SPEED = 4; //Die Standard-Geschwindigkeit eines Cursors, die er zu Beginn seines Angriffs besitzt
    private static final int KB_AMOUNT = 50; //Die Standard-Weite des Knockbacks
    private static final double KB_STRENGTH = 7; //Die Standard-Stärke des Knockbacks
    private static final int DEFAULT_DAMAGE = 5; //Der Standard-Schaden des Cursors
    
    /**
     * Legt fest ob die Waffe vom Spieler oder von einem Gegner geworfen wurde und ob sie Gegner oder den Spieler angreift
     */
    private boolean isFriendly;
    /**
     * Die Waffen werden immer relativ zu ihren Besitzern positioniert.
     * Der Cursor hat einen Griff dessen Position in diesem Array in den Verschiedenen positionen des Cursors angegeben wird.
     */
    protected static final Point[][] handelPositions = new Point[][]{
        new Point[] { new Point(06, 14), new Point(06, 14), new Point(06, 14) },
        new Point[] { new Point(10, 13), new Point(10, 13), new Point(10, 13) },
        new Point[] { new Point(06, 14), new Point(06, 14), new Point(06, 14) },
        new Point[] { new Point(10, 13), new Point(10, 13), new Point(10, 13) }
    };
    
    private double startX; //Die Startposition vor dem Angriff. Der Cursor kehrt hierhin zurück
    private double startY;
    /**
     * Erstellt einen neuen Cursor
     * @param x die x-Position des Cursors
     * @param y die y-Position des Cursors
     * @param isFriendly gibt an ob dieser Cursor dem Spieler oder einem Gegner gehört
     * @author Ares Zülke, Janni Röbbecke, Jakob Kleine
     */
    public Cursor(int x, int y, boolean isFriendly) {
        super(DEFAULT_SPRITE_SHEET, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.isFriendly = isFriendly;
    }
    
    /**
     * Berechnet die neue Position der Waffe, wenn diese gerade angreift. Sonst wird ihre Position von ihrem Besitzer festgelegt
     * @author Jakob Kleine, Janni Röbbecke
     * @since 17.05.2019
     */
    @Override
    public void update() {
        if(isAttacking) {
            move();
            if(beyondStart()){ //Wenn die Waffe zum Startpunkt zurückgekehrt ist / oder zu weit ist
                stopAttack();
            }
            else {
                speed -= 0.2; //Sonst wird immer die Geschwindigkeit ein wenig verringert, irgendwann ist sie negativ -> Waffe kommt zurück
            }
        }
        //Sonst muss die Waffe von ihrem Besitzer gelenkt werden
    }
    
    /**
     * Setzt den Cursor in die Hand seines Besitzers
     * @param handPosition die absolute Position der Hand des Besitzers dieser Waffe
     * @param poseOwner Pose, in der sich der Besitzer der Waffe gerade befindet
     * @param richtung in die der Besitzer dieser Waffe gerade schaut
     * @author Janni Röbbecke, Jakob Kleine
     * @since 22.05.2019
     */
    @Override
    public void setPositionInHand(Point handPosition, int poseOwner, int directionOwner) {
        entityX = handPosition.x - handelPositions[directionOwner][poseOwner].x;
        entityY = handPosition.y - handelPositions[directionOwner][poseOwner].y;
        
        setEntityImage(spriteSheet.getSpriteElement(xPos, prevDirection));
    }
    
    /**
     * Benachrichtigt die Waffe, das sie getroffen hat
     * @author Jakob Kleine, Janni Röbbecke
     */
    public void notifySuccess() {
        image = spriteSheet.getSpriteElement(0, prevDirection+4); //Das könnte eigentlich länger sein, dmit man das besser sieht
    }
    
    /**
     * Lässt die Waffe einen Angriff starten 
     * @param Richtung in die Angegriffen werden soll
     * @author Jakob Kleine, Janni Röbbecke
     * @since 17.05.2019
     */
    @Override
    public void startAttack(Point direction) {
        if(!isAttacking) {
            startX = entityX; //Speichert die Position vor dem Angriff
            startY = entityY; 
            speed = DEFAULT_ATTACK_SPEED; //Jetzt bewegt sich der Cursor mit einer eigenen Geschwindigkeit
            setMove(direction); //Der Cursor wird in die angegebene Richtung geworfen
            isAttacking = true;
        }
    }
    
    /**
     * Beendet den Angriff mit diesem Cursor
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
    
    @Override
    public void moveBy(int byX, int byY) {
        super.moveBy(byX, byY);
        startX += byX;
        startY += byY;
    }
    
    /**
     * @author Jakob Kleine, Cashen Adkins
     * @since 23.05.2019
     * @return Knockback, mit dem ein Gegner zurückgeworfen wird, wenn er von dieser Waffe getroffen wird
     */
    public Knockback getKnockback() {
        return new Knockback(KB_AMOUNT, KB_STRENGTH, xMove, yMove);
    }
    
    /**
     * @author Jakob Kleine, Cashen Adkins
     * @since 23.05.2019
     * @return Schaden den diese Waffe verursacht.
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
        return isFriendly;
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 26.05.2019
     */
    public double getDefaultSpeed() {
        return DEFAULT_ATTACK_SPEED;
    }
}    