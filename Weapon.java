import java.awt.image.BufferedImage;
import java.awt.Point;
/**
 * <code>Weapons</code> (Waffen) werden von <code>Creatures</code> (Kreaturen) verwendet, um anderen Kreaturen Schaden zuzufügen.
 * Waffen haben einen Angriffs-Modus, der mit der Methode {@link #startAttack(Point) startAttack} aktiviert wird, und automatisch abklingt.
 * Sie bewegen sich in der Regel mit ihren Besitzern mit und werden realtiv zu ihnen positioniert. 
 * Beim Angriff mit einer Waffe bewegt sich diese aber selbstständig und kehrt, wenn der Angriff vorbei ist, wieder zu ihrem Benutzer zurück.
 * 
 * @author Jakob Kleine, Janni Röbbecke
 * @version 0.01 (17.05.2019)
 * @since 17.05.2019
 */
public abstract class Weapon extends Movable {
    /**
     * Speichert, ob die Waffe gerade im Angriffs-Modus ist
     * @since 21.05.2019
     */
    protected boolean isAttacking;
    /**
     * Ersellt eine neue Waffe
     * @author Jakob Kleine, Janni Röbbecke
     * @param name der Name der Waffe
     * @param image das SpriteSheet, das die Waffe abbildet
     * @param x die x-Position der Waffe
     * @param y die y-Position der Waffe
     * @param width die Breite der Waffe
     * @param height die Höhe der Waffe
     * @since 0.01 (17.05.2019)
     */
    public Weapon(SpriteSheet spriteSheet, int x, int y, int width, int height) {
        super(spriteSheet, x, y, width, height, 0);
    }
    
    /**
     * Startet den Angriff mit dieser Waffe
     * @param direction die Richtung, in die der Spieler zum Startpunkt des Angriffs guckt
     * @author Jakob Kleine, Janni Röbbecke
     * @since 0.01 (17.05.2019)
     */
    public abstract void startAttack(Point direction);
    
    /**
     * Benachrichtigt die Waffe, dass sie erfolgreich angegriffen, also Schaden zugefügt hat.
     * Die Waffe kann darauf zum Beispiel mit einer Animation reagieren, oder ihren Angriff sofort beenden o.Ä.
     * @author Jakob Kleine, Cashen Adkins
     * @since 23.05.2019
     */
    public abstract void notifySuccess();
    
    /**
     * Ändert die Position der Waffe so, dass sie in der Hand des Benutzers liegt
     * @param handPosition die Koordinaten der Hand des Besitzers
     * @param direction die Richtung, in die sich die Waffe bewegen soll. Momentan sind nur 4 Richtungen implementiert: 
     *                  <ul>
     *                      <li>0 -> Süden</li>
     *                      <li>1 -> Westen</li>
     *                      <li>2 -> Osten</li>
     *                      <li>3 -> Norden</li>
     *                  </ul>    
     * @author Janni Röbbecke, Jakob Kleine
     * @since 21.05.2019                   
     */
    public abstract void setPositionInHand(Point handPosition, int poseOwner, int directionOwner);
    
    /**
     * Gibt das Knockback der Waffe zurück
     * @return ein <code>Knockback</code>-Objekt, das angibt, wie weit, stark und in welche Richtung das Opfer der Waffe zurückgestoßen wird
     * @author Jakob Kleine, Cashen Adkins
     * @since 23.05.2019
     */
    public abstract Knockback getKnockback();
    
    /**
     * Gibt den Schaden, den die Waffe anrichtet, zurück.
     * @return die HP, die dem Opfer der Waffe abgezogen werden sollen
     * @author Jakob Kleine, Cashen Adkins
     * @since 23.05.2019
     */
    public abstract int getDamage();
    
    /**
     * Gibt an, ob die Waffe momentan angreift
     * @return true, wenn die Waffe gerade im Angriffs-Modus ist, sonst false
     * @author Janni Röbbecke, Jakob Kleine
     * @since 21.05.2019                   
     */
    public boolean isAttacking() {
        return isAttacking;
    }
    
    /**
     * Für die Kollision ist es wichtig zu wissen, ob eine Waffe friendly (also dem Spieler positiv gesinnt) oder nicht friendly 
     * (dem Spieler negativ gesinnt) ist.
     * <br>Waffen die friendly sind, greifen nicht den Spieler sondern nur die Gegner an. Gegenteiliges gilt für Waffen die nicht friendly sind.
     * Diese Methode ermittelt, ob diese Waffe friendly ist.
     * @return true, wenn die Waffe nicht dem Spieler sondern den Gegnern schaden soll
     * @author Janni Röbbecke, Jakob Kleine
     * @since 22.05.2019                   
     */
    public abstract boolean isFriendly();
}