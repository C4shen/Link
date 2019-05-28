import java.awt.Point;
import java.awt.Rectangle;
/**
 * Pincers (Scheren) sind die Waffen eines Side-Effects.
 * Sie greifen nur an, wenn der Sideeffekt mit dem Spieler kollidiert. Dann schließen sie sich und der Spieler erleidet Schaden und Knockback.
 * Die Pincers haben dann eine kurze Abklingzeit, in der sich auch der Sideeffect nicht mehr bewegen kann.
 * @author Jakob Kleine, Janni Röbbecke
 * @since 28.05.2019
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
    private static final double KB_STRENGTH = 10; //Die Standard-Stärke des Knockbacks
    private static final int DEFAULT_DAMAGE = 5; //Der Standard-Schaden
    /*
     * Nachdem die Pincers angegriffen haben, haben sie eine Abklingzeit, in denen der Krebs sich nicht mehr bewegen kann, weil die Pincers immernoch 
     * zurückgeben, dass sie angreifen. Diese Abklingzeit wird mit diesem Attribut definiert. Es gibt die Anzahl der Durchläufe der Game-Loop an, seitdem
     * die Pincers angegriffen haben, nach denen sie wieder zurückgeben, dass sie nicht angreifen.
     */
    private static final int DEFAULT_ATTACK_DELAY = 22; 

    /*
     * Die Waffen werden immer relativ zu ihren Besitzern positioniert. Dieses Attribut gibt die Position an, an der die Waffe in der Hand positioniert werden
     * (also die Position des Griffs)
     * Bei den Pincers unterscheidet sich die Position des Griffs in den einzelnen Bildern nur in der Position des linken Pincers und der des rechten.
     */
    private static final Point[] handelPositions = new Point[]{
        new Point(17, 21), //Die Position des Griffs des linken Pincers
        new Point(00, 21) //--- rechten Pincers
    };
    
    private int attackDelay; //Das tatsächliche Attack-Delay dieses Pincers. Es wird bei jedem Update reduziert, wenn der Pincer angreift
    /**
     * Erstellt einen neuen Pincer
     * @param x die x-Position des Pincers
     * @param y die y-Position des Pincers
     * @param isLeft gibt an, ob es sich bei diesem Pincer um den linken Pincer handelt (true), oder den rechten (false)
     * @author Janni Röbbecke, Jakob Kleine
     * @since 28.05.2019
     */
    public Pincers(int x, int y, boolean isLeft) {
        super(DEFAULT_SPRITE_SHEET, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        prevDirection = isLeft ? 0 : 1;
    }
    
    /**
     * @author Janni Röbbecke, Jakob Kleine
     * @since 28.05.2019
     */
    @Override
    public void update() {
        if(isAttacking) {
            if(attackDelay-- <= 0) { //Das Delay wird reduziert. Wenn es jetzt abgelaufen ist, greift der Pincer nicht mehr an.
                stopAttack();
            }
        }
    }
    
    /**
     * @author Janni Röbbecke, Jakob Kleine
     * @since 28.05.2019
     */
    @Override
    public void setPositionInHand(Point handPosition, int xPosOwner, int yPosOwner) {
        entityX = handPosition.x - handelPositions[prevDirection].x;
        entityY = handPosition.y - handelPositions[prevDirection].y;
        
        setEntityImage(spriteSheet.getSpriteElement(0, prevDirection)); //Ändert das Bild. Es soll das Standard-Bild benutzt werden, also soll wird 0 als erster Parameter gegeben
    }
    
    /**
     * Teilt dem Pincer mit, dass er getroffen hat. Er reagiert allerdings nicht darauf.
     * @author Jakob Kleine, Janni Röbbecke
     */
    public void notifySuccess() { }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 17.05.2019
     */
    @Override
    public void startAttack(Point direction) {
        if(!isAttacking) {
            image = spriteSheet.getSpriteElement(1, prevDirection); //Das Angriffs-Bild für links/rechts (entspricht prevDirection): eine geschlossene Schere
            attackDelay = DEFAULT_ATTACK_DELAY; //Startet das Delay
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
        image = spriteSheet.getSpriteElement(0, prevDirection); //Es wird wieder das Standard-Bild und nicht das Attacke-Bild gezeigt: eine offene Schere
    }

    /**
     * @author Jakob Kleine, Cashen Adkins
     * @since 23.05.2019
     */
    public Knockback getKnockback() {
        int kbDir = -1 + prevDirection*2; //Richtung des Knockbacks. Wenn Pincer-links: prevDir=0 -> kbDir = -1 [links]; wenn Pincer-rechts: prevDir=1 -> kbDir = 1 [rechts]
        return new Knockback(KB_AMOUNT, KB_STRENGTH, kbDir, 0);
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
        return false; //Die Pincer gehören dem Side-Effect [einem Gegner] -> er ist immer unfriendly
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 26.05.2019
     */
    public double getDefaultSpeed() {
        return 0; //Die Pincer bewegen sich nie aktiv, also besitzen sie keine Standard-Speed. 
    }
}