import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;
/**
 * Ein SideEffect (Nebeneffekt) ist eine Art von Gegner. 
 * Er wird durch einen Krebs verkörpert und kann sich dementsprechend nur entlang der x-Achse bewegen.
 * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
 * @version 0.01 (15.05.2019)
 * @since 15.05.2019
 */
public class SideEffect extends Enemy
{
    /*
     * Die Koordinaten der Hand sind beim SideEffect immer gleich. [Erklärung der Aufgabe dieses Attributs beim gleichnamigen Attribut in der Player - Klasse]
     */
    private static final Point handPosLeft = new Point(23, 25), handPosRight = new Point(56, 29);
    /*
     * Das Standart-Spritesheet-Objekt, auf das alle Objekte dieser Klasse als ihr SpriteSheet referenzieren. 
     * Dadurch, dass es ein Standard-Bild gibt, müssen die gleichen Bilder nicht mehrmals geladen werden.
     */
    private static final SpriteSheet DEFAULT_SPRITE_SHEET = new SpriteSheet("/res/sprites/creatures/sideEffectOhneSchere.png", 3 /*moves*/, 4 /*directions*/, 80 /*width*/, 50 /*height*/);
    
    /**
     * Standard-Lebenspunkte, die der Spielcharakter zu Beginn des Spiels hat
     */
    public static final int DEFAULT_HEALTH = 20;
    
    /**
     * Standard-Geschwindigkeit des Spielcharakters
     */
    public static final int DEFAULT_SPEED = 5;
    
    public static final int DEFAULT_SCORE_VALUE = 50;
    
    private Pincers pincersLeft;
    private Pincers pincersRight;
    /**
     * Ertellt einen neuen Nebeneffekt
     * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
     * @param x die x-Koordinate, auf der der Nebeneffekt gespawnt wird
     * @param y die y-Koordinate, auf der der Nebeneffekt gespawnt wird
     * @param enemySprite das SpriteSheet des Nebeneffekts
     * @since 15.05.2019
     */
    public SideEffect(int x, int y) 
    {
        super(x, y, "Krebs", DEFAULT_SPRITE_SHEET, DEFAULT_HEALTH, DEFAULT_SPEED);
        setMove(new Point(-1, 0)); //Zu Beginn bewegt sich der Side-Effect immer nach links
        health = SideEffect.DEFAULT_HEALTH;
        pincersLeft = new Pincers(x, y, true);
        pincersRight = new Pincers(x, y, false);
    }
   
    @Override 
    protected void knockbackAbarbeiten(int zusatzX, int zusatzY) {
        super.knockbackAbarbeiten(zusatzX, zusatzY);
        
        if(weapon == null || weapon == pincersLeft) 
            pincersRight.moveBy(zusatzX, zusatzY);
        if(weapon == null || weapon == pincersRight) 
            pincersLeft.moveBy(zusatzX, zusatzY);
    }
    
    @Override
    public void startBeingAttacked(Weapon attackingWeapon) {
        super.startBeingAttacked(attackingWeapon);
        if(knockback.getDirectionX() != 0) 
            xMove = knockback.getDirectionX();
    }
    
    public void update() {
        super.update();
        if(knockback == null) {
            if(weapon == null || weapon == pincersLeft) 
                pincersRight.setPositionInHand(getHandPosition(0, 2), xPos, prevDirection);
            if(weapon == null || weapon == pincersRight) 
                pincersLeft.setPositionInHand(getHandPosition(0,1), xPos, prevDirection);
        }
    }
    
    public void render(Graphics g) {
        super.render(g);
        if(weapon == null || weapon == pincersLeft) 
            pincersRight.render(g);
        if(weapon == null || weapon == pincersRight) 
            pincersLeft.render(g);
    }
    
    public boolean weaponBehind(){ 
        return false; 
    }
    
    /**
     * @author Janni Röbbecke, Jakob Kleine
     * @since 22.05.2019
     */
    public Point getHandPosition(int xPos, int direction){
        int handX;
        int handY;
        if(direction == 1) {
            handX = handPosLeft.x;
            handY = handPosLeft.y;
        } 
        else {
            handX = handPosRight.x;
            handY = handPosRight.y;
        }
        return new Point((int) Math.round(entityX) + handX, (int) Math.round(entityY) + handY);
    }
    
    /**
     * @author Jakob Kleine, Cepehr Bromand, Ares Zühlke
     * @since 22.05.2019
     */
    public Rectangle getHitbox() {
        return new Rectangle((int) Math.round(entityX), (int) Math.round(entityY), 64, 64);
    }
    
    public int getScoreValue() {
        return DEFAULT_SCORE_VALUE;
    }
    
    public Weapon target(Player player){
        Rectangle hitboxPlayer = player.getHitbox();
        Rectangle hitboxEigen = getHitbox();
        if(player.getHitbox().intersects(getHitbox())){
            setMove(new Point(0, 0));
            if(hitboxPlayer.getLocation().x < hitboxEigen.getLocation().x + hitboxEigen.getWidth())
                weapon = pincersLeft;
            else
                weapon = pincersRight;
            return startAttack();
        }
        else {
            if(xMove == 0){
                if(player.entityX < entityX)
                    setMove(new Point(-1,0));
                else 
                    setMove(new Point(1,0));
            }
            if(entityX<20)
                setMove(new Point(1, 0));
            if(entityX>550)
                setMove(new Point(-1, 0));
            return null;
        }
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 26.05.2019
     */
    public double getDefaultSpeed() {
        return DEFAULT_SPEED;
    }
}
