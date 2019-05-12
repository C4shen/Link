import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * @author Janni Röbbecke, Ares Zühlke, Jakob Kleine, www.quizdroid.wordpress.com
 * @version 0.02 (13.05.2019)
 * @since 0.01 (10.05.2019
 */
public abstract class Creature extends Entity {
    /**
     * Standard-Lebenspunkte die eine Figur hat wenn sie im Spiel erscheind
     */
    public static final int DEFAULT_HEALTH = 10;
    /**
     * Standard-Geschwindigkeit einer Figur
     */
    public static final int DEFAULT_SPEED = 3;
    
    /**
     * Spritesheet mit Bildern für die Bewegung der Figur
     */
    protected SpriteSheet spriteSheet;
    
    /**
     * Lebenspunkte der Figur
     */
    protected int health;
    /**
     * Geschwindigkeit der Figur
     */
    protected int speed;
    /**
     * speichert ob die Figur sich gerade in x- bzw. y-Richtung bewegt
     */
    protected int xMove, yMove;
    
    private int animationDelay = 0; //Zählt die Durchläufe der Game-Loop. Die Geh-Animation soll nur alle sieben Druchläufe erfolgen
    private int op = 1;
    private int xPos = 0;
    private int prevDirection; //Die Richtung, in die sich die Figur vor dem Stillstand bewegt hat
    /**
     * Erzeugt eine Kreatur (bzw. im Deutschen schöner Figur)
     * @author 
     * @param name der Name der Figur
     * @param spriteSheet ein Spritesheet, das das Aussehen der Figur beschreibt (stimmt das?)
     * @param x die x-Position aud der die Entität gespawnt werden soll
     * @param y die y-Position aud der die Entität gespawnt werden soll
     * @param width die Breite der Figur
     * @param height die Höhe der Figur
     * @param health die HP der Figur
     * @param speed die Geschwindigkeit der Figur
     */
    public Creature(String name, SpriteSheet spriteSheet, int x, int y, int width, int height, int health, int speed) {
        super(name, spriteSheet.getSpriteElement(0, 1), x, y, width, height);
        this.spriteSheet = spriteSheet;
        this.health = health;
        this.speed = speed;
        xMove = 0;
        yMove = 0;
    }
    
    /**
     * Bewegt die Figur mit einer Animation in die Richtung, in die sie guckt
     * Die Animation erfolgt dabei nur alle sieben Durchläufe der Game-Loop
     * @author
     * @since 0.01 (10.05.2019) [Animiert seit 13.05.2019]
     */
    public void move(){
        entityX += xMove * speed; //Die Figur bewegt sich mit ihrer bestimmten Geschwindigkeit in ihre Richtung
        entityY += yMove * speed;
        
        if(animationDelay++ >= 7) { //Wenn sieben Mal verzögert wurden, soll jetzt die Animation erfolgen
            if(xMove == 0 && yMove == 0) { //Wenn die Figur gerade nicht bewegt wird
                animationDelay = 7; //Die Animation soll nicht verzögert erfolgen, wenn die Figur wieder bewegt wird
                setCurrentImage(0, 0, 0); //Das Bild der Figur im Stillstand
            } else {
                animationDelay = 0; //Setzt die Verzögerung zurück. Sie erfolgt alle sieben Durchläufe
                if(op == -1 && xPos <= 0) {
                    op = 1;
                } else if(op == 1 && xPos >= 2) {
                    op = -1;
                }
                xPos = (xPos + op);
                setCurrentImage(xMove, yMove, xPos); //Aktualisiert das Bild entsprechend
            }
        }
    }
    
    /**
     * Ändert im Zuge der Animation das Bild der Figur.
     * @author
     * @param x
     * @param y
     * @param xPos
     * @since 0.02 (13.05.2019)
     */
    void setCurrentImage(int x, int y, int xPos) {
        BufferedImage image; //Im Tutorial ist das ein Attribut. Der Sinn erschließt sich uns nicht.
        if(y == -1) {
            image = spriteSheet.getSpriteElement(xPos, 3);
            prevDirection = 3;
        } else if(y == 1) {
            image = spriteSheet.getSpriteElement(xPos, 0);
            prevDirection = 0;
        } else if(x == -1) {
            image = spriteSheet.getSpriteElement(xPos, 1);
            prevDirection = 1;
        } else if(x == 1) {
            image = spriteSheet.getSpriteElement(xPos, 2);
            prevDirection = 2;
        } else { //Die Figur hat sich nicht bewegt. Es soll das Bild im Stillstand der vorherigen Richtung angezeigt werden
            image = spriteSheet.getSpriteElement(1, prevDirection);
        }
        setEntityImage(image); //Ändert das tatsächlich angezeigte Bild
    }
}