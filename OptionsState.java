import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 * In diesem State kann der Spieler Optionen des Spiels verwalten
 * @since 17.06.2019
 * @author Jakob Kleine, Janni Röbbecke
 * @see <a href="OptionsManager.html">OptionsManager</a>
 */
public class OptionsState extends State 
{
    private static final int NAME_IN_INDEX = 0; //Der Index des Name-Textfelds
    private static final int MENU_ENTRIES = 1; //Die Anzahl der Menü-Items
    private BufferedImage menuItemFrame; //Der Rahmen, der sich um den ausgewählten Menüpunkt befindet.
    private TextField nameInput; //Ein Textfeld, in dem der Name eingegeben werden kann
    private int menuItem; //Das Menüitem, bei dem man sich gerade befindet
    private OptionsManager optionsManager; //Verwaltet die Optionen
    
    public OptionsState(OptionsManager oManager) {
        optionsManager = oManager;
        nameInput = new TextField(optionsManager.getPlayerName(), 175, 150, 300); //Das Textfeld hat als Platzhalter den Namen des Spielers
        try {
             menuItemFrame = ImageIO.read(Utils.absoluteFileOf("/res/tilesets/menuitemframe.png")); //Der Rahmen wird gelesen und als BufferedImage gespeichert.
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void render(Graphics g) {
        g.drawImage(MainMenuState.menuBackground, 0, 0, null);
        Utils.fontFestlegen(g, new Font("Futura", Font.BOLD, 40));
        Utils.centerText(g, "OPTIONEN", Game.SCREEN_WIDTH, 100);
        g.drawString("Name:", 50, 200);
        nameInput.render(g);
        if(menuItem != NAME_IN_INDEX) //Es soll kein Rahmen um das Textfeld gemalt werden
            g.drawImage(menuItemFrame, Game.SCREEN_WIDTH/2-190, 140 + menuItem * 80, 384, 96, null); //Zeichnet den Rahmen um das ausgewählte Item
            
        g.setColor(Color.white);
        g.setFont(new Font("Futura", Font.ITALIC, 15)); 
        String hinweis = "Um zum Hauptmenü zurückzukehren bitte ESCAPE drücken.";
        Utils.centerText(g, hinweis, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT-15);
    }
    
    public void update(KeyManager keyManager) {
        if(nameInput.isFocussed()) { //Wenn das Textfeld gefocussed wird, soll es beschrieben werden / bei Enter oder Escpae der Name aktualisiert werden
            if(keyManager.generalKeyPressedOnce(java.awt.event.KeyEvent.VK_ENTER) || keyManager.generalKeyPressedOnce(java.awt.event.KeyEvent.VK_ESCAPE))  
                nameEintragen();
            else 
                nameInput.recieveInput(keyManager);
        }
        else {
            if(keyManager.downEinmal()) {
                if(menuItem < MENU_ENTRIES-1) //Wenn nicht beim letzten Item -> zum nächsten
                    menuItem++;
                else //Sonst: zum ersten
                    menuItem = 0;
            }
            else if(keyManager.upEinmal()) {
                if(menuItem > 0)  //Wenn nicht beim ersten Item -> zum vorherigen
                    menuItem--;
                else
                    menuItem = MENU_ENTRIES-1; //Sonst: zum letzten Item
            }
            if(menuItem == NAME_IN_INDEX && !nameInput.isFocussed()) //Wenn das Textfeld ausgewählt wurde und noch nicht ausgewählt war:
                nameInput.recieveFocus();
            
            if(keyManager.escapeEinmal()) //Durch Drücken von ESC / BACKSPACE wird zum Hauptmenü zurückgekehrt
                setState(StateNames.MENU);
        }
    }
    
    private void nameEintragen() { //Der Spielername wird zu dem im Textfeld eingetragenen Namen geändert
        if(!nameInput.getText().isEmpty()) {
            optionsManager.setPlayerName(nameInput.getText());
            nameInput.setPlaceholder(nameInput.getText());
            nameInput.empty(); //Leert das Textfeld
        }
        nameInput.loseFocus();
        menuItem++; //es soll zum folgenden Item gegangen werden (oder keins ausgewählt, wenn das Textfeld das letzte Item ist)
    }
    
    public OptionsManager getOptionsManager() {
        return optionsManager;
    }
}