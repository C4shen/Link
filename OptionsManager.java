import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class OptionsManager
{
    private static final String optionsPath = "/res/options.link";
    private String playerName;
    //Hier sollten noch mehr Optionen gespeichert werden (Sounds, etc.)
    
    public OptionsManager() {
        loadOptions();
    }
    
    private void loadOptions() {
        String[] optionsData = Utils.loadFileAsString(optionsPath).split("\n");
        playerName = optionsData[0]; //Der Name steht in der ersten Zeile
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String newName) {
        playerName = newName;
    }
    
    public void saveOptions() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(Utils.absoluteFileOf(optionsPath));
        } 
        catch(FileNotFoundException e) { e.printStackTrace(); }
        if(writer != null) {
            writer.print(playerName+"\n");
            writer.close();
        }
    }
}