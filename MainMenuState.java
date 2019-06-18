import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
/**
 * Der Break-Menu-State ist der State, in dem sich das Spiel befindet, wenn eine Pause gemacht wird, oder wenn das Spiel gestartet wird.
 * @author Cashen Adkins, Janni Röbbecke, Jakob Kleine, www.quizdroid.wordpress.com
 * @version 0.03 (26.05.2019)
 * @since 0.01 (22.05.2019
 */
public class MainMenuState extends State 
{
    public static BufferedImage menuBackground;
    private static final int ANZAHL_MENU_ITEMS = 6; //Die Anzahl an Items in dem Menü
    private int menuItem; //Variable, die speichert, bei welchem Menüpunkt sich der Spieler gerade befindet.
    private BufferedImage menuItemFrame; //Der Rahmen, der sich um den ausgewählten Menüpunkt befindet.
    private Font font; //Das Font, welches für den Text in den Buttons benutzt wird.
    private Color color; //Die Farbe, in der der Text geschrieben werden soll
    /*
     * Erstellt einen neuen Menu-State.
     * @author Cahsen Adkins, Janni Röbbecke, www.quizdroid.wordpress.com
     * @since 22.05.2019
     */
    public MainMenuState() {
        menuItem = 0; //Zu Beginn des Menüs ist der ausgewählte Knopf der erste.
        try {
             menuItemFrame = ImageIO.read(Utils.absoluteFileOf("/res/tilesets/menuitemframe.png")); //Der Rahmen wird gelesen und als BufferedImage gespeichert.
             menuBackground = ImageIO.read(Utils.absoluteFileOf("/res/tilesets/menubackground.png"));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        font = new Font("Futura", Font.BOLD, 40);
        color = new Color(65, 41, 31);
    }
    
    /*
     * @author Cashen Adkins, Jakob Kleine, Janni Röbbecke, www.quizdroid.wordpress.com
     * @since 22.05.2019
     */
    @Override
    public void render(Graphics g) {
        g.drawImage(menuBackground, 0, 0, null);
        Utils.fontFestlegen(g,font);
        g.setColor(color);
        g.drawString("Neues Spiel", Game.SCREEN_WIDTH/2-120, 200);
        if(true) {//gameState == null) { //Wenn der Game-State null ist, kann gerade nicht weitergespielt werden, also wird das Menüitem dafür heller gemalt
            g.setColor(color.brighter());
            g.drawString("Weiterspielen", Game.SCREEN_WIDTH/2-120, 280);
            g.setColor(color);
        }
        else //Sonst wird es ganz normal angezeigt
            g.drawString("Weiterspielen", Game.SCREEN_WIDTH/2-120, 280);
        g.drawString("Anleitung", Game.SCREEN_WIDTH/2-120, 360);
        g.drawString("Optionen", Game.SCREEN_WIDTH/2-120, 440);
        g.drawString("Bestenliste", Game.SCREEN_WIDTH/2-120, 520);
        g.drawString("Beenden", Game.SCREEN_WIDTH/2-120, 600);
        g.drawImage(menuItemFrame, Game.SCREEN_WIDTH/2-190, 140 + menuItem * 80, 384, 96, null); //Zeichnet den Rahmen um das ausgewählte Item
    }
       
    /*
     * @author Cashen Adkins, Jakob Kleine, Janni Röbbecke
     * @since 27.05.2019
     */
    @Override
    public void update(KeyManager keyManager) 
    {
        if(keyManager.downEinmal()) {
            if(menuItem < ANZAHL_MENU_ITEMS-1) //Wenn nicht beim letzten Item -> zum nächsten
                menuItem++;
            else //Sonst: zum ersten
                menuItem = 0; 
        }
        else if(keyManager.upEinmal()) {
            if(menuItem > 0) //Wenn nicht beim ersten Item -> zum vorherigen
                menuItem--;
            else
                menuItem = ANZAHL_MENU_ITEMS-1; //Sonst: zum letzten Item
        }
        else if(keyManager.attackEinmal()) { //Enter oder Backspace -> Das ausgewählte MenüItem soll "angeclickt" werden
            switch(menuItem) {
                case 0:
                    newGame();
                break;
                case 1:
                    resumeGame();
                break; 
                case 2:
                    setState(StateNames.TUTORIAL);
                break;
                case 3: 
                    setState(StateNames.OPTIONS);
                break;
                case 4:
                    setState(StateNames.SCORES);
                break;
                default: //Das letzte Item ist das Beenden-Item
                    endGame();
            }
        }
        
        if(keyManager.escapeEinmal()) //Escpae oder Backspace -> weiterspielen wenn möglich
            resumeGame();
    }
    
    /* 
     * Ändert das ausgewählte Menü-Item. Aufgerufen, wenn das Spiel pausiert wird, sodass
     * automatisch das Menü-Item "Weiterspielen" ausgewählt wird.
     * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
     * @since 28.05.2019
     */
    public void setMenuItem(int item) {
        if(item >= 0 && item < ANZAHL_MENU_ITEMS)
            menuItem = item;
    }
}