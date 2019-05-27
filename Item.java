import java.awt.image.BufferedImage;

/**
 * Items sind Entitäten, die vom Spieler eingesammelt werden können und ihn in einer Item-spezifischen Weise beeinflussen.
 */
public abstract class Item extends Entity
{
    /**
     * Erstellt ein neues Item
     * @param name der Name des Items
     * @param spriteSheet das SpriteSheet des Items, das verwendet wird, um das Item zu animieren
     * @param xKoordinate die x-Koordinate, bei der das Item gespawnt werden soll
     * @param yKoordinare die y-Koordinate, bei der das Item gespawnt werden soll
     * @param width die Breite des Items
     * @param height die Höhe des Items
     * @author Janni Röbbecke, Jakob Kleine
     * @since 26.05.2019
     */
    public Item(String name, SpriteSheet spriteSheet, int xKoordinate, int yKoordinate, int width, int height) {
        super(name, spriteSheet.getSpriteElement(0, 0), xKoordinate, yKoordinate, width, height);
        this.spriteSheet = spriteSheet;
    }
    
    /**
     * Aktualisiert das Item. Weil es keine aktiven Fähigkeiten beistzt, (außer wenn es eingesammelt wird) wird hier nur die Animation 
     * aktualisiert. 
     * Während die Animation bei bewegbaren Entitäten stufenweise abläuft (Bild 1->2->3->2->1 usw.), zirkuliert sie bei Items (1->2->3->1 usw.) 
     * @author Janni Röbbecke, Jakob Kleine
     * @since 26.05.2019
     */
    @Override
    public void update() {
        if(animationDelay++ >= 7) { //Die Animation soll nur alle 7 Durchläufe der Loop erfolgen
            animationDelay = 0; //Setzt das Delay zurück
            if(xPos < spriteSheet.getPoseAmount()-1) //wenn man gerade noch nicht beim letzten Bild ist, soll das nächste gewählt werden
                xPos++;
            else //Sonst beginnt man wieder beim ersten Bild
                xPos = 0;
            image = spriteSheet.getSpriteElement(xPos, 0); //Das Bild der entsprechenden Pose. Items bewegen sich nicht, also gibt es nur eine Richtung (0)
        }
    }
    
    /**
     * Items beeinflussen den Spieler in einer bestimmten Weise, wenn sie eingesammelt werden. 
     * Diese Beeinflussung ist Item-spezifisch und wird deshalb von jedem Item selbst implementiert.
     * @param p um den Spieler beinflussen zu können, muss das Item ihn kurzeitig für die Manipulation sehen und verändern können
     * @author Janni Röbbecke, Jakob Kleine
     * @since 26.05.2019
     */
    public abstract void affect(Player p);
}
