import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
public class OptionsState extends State 
{
    private static final int NAME_IN_INDEX = 1;
    private static final int MENU_ENTRIES = 2;
    private BufferedImage menuItemFrame; //Der Rahmen, der sich um den ausgewählten Menüpunkt befindet.
    private TextField nameInput;
    private int menuItem;
    private OptionsManager optionsManager;
    
    public OptionsState(OptionsManager oManager) {
        optionsManager = oManager;
        nameInput = new TextField(optionsManager.getPlayerName(), 250, 500, 300);
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
        g.drawString("Name:", 110, 550);
        nameInput.render(g);
        if(menuItem != NAME_IN_INDEX) 
            g.drawImage(menuItemFrame, Game.SCREEN_WIDTH/2-190, 140 + menuItem * 80, 384, 96, null); //Zeichnet den Rahmen um das ausgewählte Item
    }
    
    public void update(KeyManager keyManager) {
        if(nameInput.isFocussed()) {
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
                if(menuItem == NAME_IN_INDEX) 
                    nameInput.recieveFocus();
            }
            else if(keyManager.upEinmal()) {
                if(menuItem > 0)  //Wenn nicht beim ersten Item -> zum vorherigen
                    menuItem--;
                else
                    menuItem = MENU_ENTRIES-1; //Sonst: zum letzten Item
                if(menuItem == NAME_IN_INDEX) 
                    nameInput.recieveFocus();
            }
            
            if(keyManager.escapeEinmal()) //Durch Drücken von ESC / BACKSPACE wird zum Hauptmenü zurückgekehrt
                setState(StateNames.MENU);
        }
    }
    
    private void nameEintragen() { 
        if(!nameInput.getText().isEmpty()) { 
            setPlayerName(nameInput.getText());
        }
        nameInput.empty();
        nameInput.loseFocus();
    }
    
    private void setPlayerName(String newName) {
        optionsManager.setPlayerName(newName);
        nameInput.setPlaceholder(newName);
    }
    
    public OptionsManager getOptionsManager() {
        return optionsManager;
    }
}