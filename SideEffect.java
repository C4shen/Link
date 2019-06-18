import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;
/**
 * Ein SideEffect (Nebeneffekt) ist eine Art von Gegner.
 * Er wird durch einen Krebs verkörpert und kann sich dementsprechend nur entlang der x-Achse bewegen.
 * Er greift mit seinen <a href="Pincers.html">Pincers</a> an.
 * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
 * @since 15.05.2019 [mit eigener Waffe seit 28.05.2019]
 */
public class SideEffect extends Enemy
{
    private static final int DEFAULT_WIDTH = 80, DEFAULT_HEIGHT = 50; //Die Standard-Maße eines Side-Effects in px
    /*
     * Die Koordinaten der Hand sind beim SideEffect immer gleich. [Erklärung der Aufgabe dieses Attributs beim gleichnamigen Attribut in der Player - Klasse].
     * Er besitzt eine linke und eine rechte Hand, deren Koordinaten sich unterscheiden.
     */
    private static final Point handPosLeft = new Point(23, 25), handPosRight = new Point(56, 29);
    /*
     * Das Standart-Spritesheet-Objekt, auf das alle Objekte dieser Klasse als ihr SpriteSheet referenzieren. 
     * Dadurch, dass es ein Standard-Bild gibt, müssen die gleichen Bilder nicht mehrmals geladen werden.
     */
    private static final SpriteSheet DEFAULT_SPRITE_SHEET = new SpriteSheet("/res/sprites/creatures/sideEffectOhneSchere.png", 
                                                                            3 /*moves*/, 4 /*directions*/, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    
    /*
     * Standard-Lebenspunkte, die der Side-Effect zu Beginn des Spiels hat
     */
    private static final int DEFAULT_HEALTH = 20;
    
    /*
     * Standard-Geschwindigkeit des Side-Effects
     */
    private static final int DEFAULT_SPEED = 5;
    
    /*
     * Der Wert, um den ein Side-Effekt den Score erhöht.
     */
    private static final int DEFAULT_SCORE_VALUE = 50;

    private Pincers pincersLeft; //Die linke Schere des Side-Effects
    private Pincers pincersRight; //Die rechte Schere des Side-Effects
    /**
     * Ertellt einen neuen Side-Effekt
     * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
     * @param x die x-Koordinate, auf der der Nebeneffekt gespawnt wird
     * @param y die y-Koordinate, auf der der Nebeneffekt gespawnt wird
     * @since 15.05.2019
     */
    public SideEffect(int x, int y) 
    {
        super(x, y, DEFAULT_SPRITE_SHEET, DEFAULT_HEALTH, DEFAULT_SPEED);
        setMove(new Point(-1, 0)); //Zu Beginn bewegt sich der Side-Effect immer nach links
        health = DEFAULT_HEALTH;
        pincersLeft = new Pincers(x, y, true);
        pincersRight = new Pincers(x, y, false);
    }
   
    /**
     * Arbeitet das Knockback des Side-Effect wie bei allen anderen Kreaturen ab.
     * Dazu, dass die Figur selbst verschoben wird, müssen allerdings auch die beiden Pincers verschoben
     * werden. Deswegen wird die Methode knockbackAbarbeiten hier überschrieben 
     * @param zusatzX der Wert, um den alle Elemente der Kreatur entlang der x-Achse verschoben werden
     * @param zusatzY der Wert, um den alle Elementer der Kreatur entlang der y-Achse verschoben werden
     * @author Jakob Kleine, Janni Röbbecke
     * @since 28.05.2019
     * @see <a href="Creature.html">Klasse Creature</a>
     */
    @Override 
    protected void knockbackAbarbeiten(int zusatzX, int zusatzY) {
        super.knockbackAbarbeiten(zusatzX, zusatzY);
        //Der SideEffect muss nicht nur sich sondern auch seine beiden Scheren versetzen, 
        //sollte dies nicht schon in der Methode der Superklasse geschehen sein
        if(weapon == null || weapon == pincersLeft) 
            pincersRight.moveBy(zusatzX, zusatzY);
        if(weapon == null || weapon == pincersRight) 
            pincersLeft.moveBy(zusatzX, zusatzY);
    }
    
    /**
     * Der Side-Effect nimmt Schaden, wie jede andere Kreatur auch. 
     * Zusätzlich stellt er aber auch - wenn das Knockback entlang der x-Achse ist - seine Richtung so ein,
     * dass er, wenn das Knockback abgearbeitet wurde, in die entgegengesetzte Richtung des Knockbacks läuft, also
     * quasi flieht.
     * @param attackingWeapon die Waffe, mit der dem Side-Effect Schaden zugefügt wurde
     * @author Janni Röbbecke, Cashen Adkins
     * @since 22.05.2019
     */
    @Override
    public void startBeingAttacked(Weapon attackingWeapon) {
        super.startBeingAttacked(attackingWeapon);
        //Der SideEffect soll nach dem angriff in die Entgegengesetzte Richtung des Angreifers, vor diesem weglaufen
        if(knockback.getDirectionX() != 0) 
            xMove = knockback.getDirectionX();
    }
    
    /**
     * Überschreibt das Updaten, das erfolgen soll, wenn gerade kein Knockback erfolgt.
     * Bei anderen Kreaturen wird immer die Waffe in die Hand gelegt, wenn sie gerade nicht angreift.
     * Weil der Side-Effect aber zwei Waffen besitzt, müssen beide in seine Hand positioniert werden.
     * @author Janni Röbbecke, Jakob Kleine
     * @since 28.05.2019
     */
    @Override
    public void defaultUpdate() {
        move();
        if(weapon != null && weapon.isAttacking()) 
            weapon.update();
        else {
            pincersRight.setPositionInHand(getHandPosition(0, 2), xPos, prevDirection);
            pincersLeft.setPositionInHand(getHandPosition(0,1), xPos, prevDirection);
        }
    }
    
    /**
     * Erweitert das Standard-Rendern des Side-Effects.
     * Bei anderen Kreaturen wird immer die Waffe mit der Kreatur gerendert.
     * Weil der Side-Effect aber zwei Waffen besitzt, müssen beide gerendert werden.
     * @author Jakob Kleine, Janni Röbbecke
     * @since 28.05.2019
     * @param g die Graphics, mit denen der Side-Effect gemalt werden soll
     */
    @Override
    public void render(Graphics g) {
        super.render(g);
        if(weapon == null || weapon == pincersLeft) 
            pincersRight.render(g);
        if(weapon == null || weapon == pincersRight) 
            pincersLeft.render(g);
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 28.05.2019
     */
    @Override
    protected boolean weaponBehind(){ 
        return false; //Die Pincers sollen immer über dem Side-Effect gerendert werden.
    }
    
    /**
     * @author Janni Röbbecke, Jakob Kleine
     * @since 22.05.2019
     */
    public Point getHandPosition(int xPos, int direction){
        int handX;
        int handY;
        if(direction == 1) { //Der Side-Effect bewegt sich nach links, also wird die Position der linken Hand zurückgegeben 
            handX = handPosLeft.x;
            handY = handPosLeft.y;
        } 
        else { //Die rechte Hand
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
        return new Rectangle((int) Math.round(entityX), (int) Math.round(entityY), DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    /**
     * @author Jakob Kleine, Cashen Adkins, Janni Röbbecke
     * @since 27.05.2019
     */
    public int getScoreValue() {
        return DEFAULT_SCORE_VALUE;
    }
    
    /**
     * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
     * @since 15.05.2019 [Basis-Version], 26.05.2019
     */
    public Weapon target(Player player){
        /*
         * Wenn es eine Waffe gibt [beim Spawnen wird keine Waffe festgelegt, erst dann wenn der Spieler zum ersten Mal anvisiert wird], wird nur geupdated, 
         * wenn sie nicht angreift. Sonst wird auch geupdatet, wenn es gerade keine Waffe gibt (weapon == null)
         */
        if((weapon != null && !weapon.isAttacking()) || weapon == null) { 
            Rectangle hitboxPlayer = player.getHitbox();
            Rectangle hitboxEigen = getHitbox();
            if(player.getHitbox().intersects(hitboxEigen)){ //Wenn der Spieler mit dem Side-Effect kollidiert, soll der Side-Effect angreifen
                if(hitboxPlayer.getLocation().x < entityX) //Wenn der Spieler links von dem Side-Effect ist, schlagen ihn die linken Pincers
                    weapon = pincersLeft;
                else //Sonst schlagen ihn die rechten Pincers. Sie werden also die aktive Waffe und in der start-Attack Methode verwendet
                    weapon = pincersRight;
                startAttack();
                player.startBeingAttacked(weapon); //Der Side-Effect gibt seinen zugefügten Schaden direkt an den Spieler weiter.
            }
            else {
                if(xMove == 0){ //Wenn der Side-Effect sich gerade nicht bewergt (weil sein Angriff vorbei ist)
                    if(hitboxPlayer.getLocation().x < entityX) //Wenn der Spieler links vom Side-Effect ist, läuft der Side-Effekt auf ihn zu
                        setMove(new Point(-1,0));
                    else 
                        setMove(new Point(1,0));
                }
                if(entityX <= GameState.BORDER_WIDTH) //Der Side-Effect läuft immer vom linken Rand des Spielfelds zum rechten. Er fängt hier also an nach rechts zu laufen
                    setMove(new Point(1, 0));
                if(entityX >= Game.SCREEN_WIDTH-DEFAULT_WIDTH-GameState.BORDER_WIDTH) //Wenn er am rechten Rand angekommen ist, läuft er wieder nach links
                    setMove(new Point(-1, 0));
            }
        }
        /*
         * Wir sind eigentlich daon ausgegangen, dass der Spielr nut angegriffen wird, wenn er mit einer Waffe kollidiert, sodass die Waffe immer 
         * kollidieren und mit dem Spieler kollidieren müsste, wenn ein Gegner mit dem Spieler kollidiert. 
         * Wir haben aber nicht bedacht, dass es auch Gegner gibt, die nur bei einer Kollision mit dem Spieler angreifen, also immer bereits in der
         * target-Methode Schaden zufügen.
         * An dieser Stelle wäre es sinnvoll, unsere Modellierung leicht zu überarbeiten, allerdings ist uns dieser Aspekt erst spät aufgefallen, 
         * weswegen wir  dazu nicht gekommen sind. Deswegen gibt z.B. der Side-Effect immer keine angreifende Waffe zurück, weil er dem Schaden direkt an 
         * den Spieler gibt, und die Kollision also nicht in der Game-Klasse verwaltet werden muss, wie wir es ursprünglich geplant hatten.
         */        
        return null; 
    }
    
    /**
     * @author Jakob Kleine, Janni Röbbecke
     * @since 26.05.2019
     */
    public double getDefaultSpeed() {
        return DEFAULT_SPEED;
    }
}
