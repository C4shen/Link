/**
 * Knockback definiert, wie weit und wie schnell eine Kreatur zurückgestoßen wird, wenn sie Schaden erleidet.
 * @author Jakob Kleine, Cashen Adkins, Janni Röbbecke
 * @since 23.05.2019
 * @see <a href="Weapon.html">Weapon</a>
 */
public class Knockback
{
    private int knockbackLeft; //Das Knockback, das noch erfolgen muss
    private double knockbackStrength; //Die Stärke des Knockbacks (entspricht der speed)
    private double directionX; //Die Richtung des Knockbacks
    private double directionY;
    
    /**
     * Erstellt ein neues Knockback
     * @param amount die Anzahl der Pixel, die die getroffene Kreatur nach hinten geworfen wird
     * @param strength die Stärke (= Geschwindigkeit) des Knockbacks
     * @param dirX die Richtung des Knockbacks in x-Richtung
     * @param dirY die Richtung des Knockbacks in y-Richtung
     * @author Jakob Kleine, Cashen Adkins, Janni Röbbecke
     * @since 23.05.2019
     */
    public Knockback(int amount, double strength, double dirX, double dirY) {
        /* Bei keiner Richtungsangabe würde das Opfer des Knockbacks auf der Stelle bleiben, sich aber nicht bewegen könen, weil noch Knockback durchgeführt werden muss.
         * Das KNockback könnte allerdings nicht abgearbeitet werden, weil "knockbackLeft" nicht reduziert werden kann. 
         * Deswegen muss verhindert werden, dass keine Richtung angegeben wird [Es wäre möglich eine IllegalArgumentException zu throwen, aber das Spiel soll sich nicht aufhängen]
         */
        if(dirX == 0 && dirY == 0) { 
            System.err.println("Es wurde ein Knockback ohne Richtung erstellt. Das ist nicht vorgesehen, weshalb eine zufällige Richtung (nach rechts) bestimmt wird.");
            dirX = 1;
        }
        knockbackLeft = amount;
        knockbackStrength = strength;
        directionX = dirX;
        directionY = dirY;
    }
    
    /**
     * Gibt zurück, wie viel Knockback noch erfolgen muss
     * @return das Knockback, das noch nicht abgearbeitet wurde
     * @author Jakob Kleine, Cashen Adkins, Janni Röbbecke
     * @since 23.05.2019
     */
    public int getAmountLeft() {
        return knockbackLeft;
    }
    
    /**
     * Verringert das noch abzuarbeitende Knockback. Aufgerufen, wenn das Opfer des Knockbacks weiter nach hinten gestoßen wurde.
     * @param by die gerade abgearbeiteten Knockback-Pixel
     * @author Jakob Kleine, Cashen Adkins, Janni Röbbecke
     * @since 23.05.2019
     * @see konkrete Subklassen von Creature (Update-Methoden)
     */
    public void reduceAmountLeft(int by) {
        knockbackLeft -= by;
    }
    
    /**
     * Gibt zurück, wie stark das Knockback erfolgt
     * @return das Knockback, das noch nicht abgearbeitet wurde
     * @author Jakob Kleine, Cashen Adkins
     * @since 23.05.2019
     */
    public double getStrength() {
        return knockbackStrength;
    }
    
    /**
     * Gibt zurück, in welcher x-Richtung das Knockback erfolgt
     * @return die Richtung des Knockbacks entlang der xAchse: -1: links; 0: keins; +1: rechts
     * @author Jakob Kleine, Cashen Adkins
     * @since 23.05.2019
     */
    public double getDirectionX() {
        return directionX;
    }
    
    /**
     * Gibt zurück, in welcher y-Richtung das Knockback erfolgt
     * @return die Richtung des Knockbacks entlang der yAchse: -1: oben; 0: keins; +1: unten
     * @author Jakob Kleine, Cashen Adkins
     * @since 23.05.2019
     */
    public double getDirectionY() {
        return directionY;
    }
}