import java.awt.image.BufferedImage;

public abstract class Item extends Entity
{
    public Item(String name, SpriteSheet spriteSheet, int xKoordinate, int yKoordinate, int width, int height) {
        super(name, spriteSheet.getSpriteElement(0, 0), xKoordinate, yKoordinate, width, height);
        this.spriteSheet = spriteSheet;
    }
    
    public void update() {
        if(animationDelay >= 7) {
            animationDelay = 0;
            if(xPos < spriteSheet.getPoseAmount()-1) 
                xPos++;
            else 
                xPos = 0;
            image = spriteSheet.getSpriteElement(xPos, 0); //Das Bild der entsprechenden Pose. Items bewegen sich nicht, also gibt es nur eine Richtung (0)
        }
        else
            animationDelay++;
    }
    
    public abstract void affect(Player p);
}
