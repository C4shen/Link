import java.io.PrintWriter;
import java.io.FileNotFoundException;

/**
 * Verwaltet die Optionen, die der Spieler im Spiel machen kann. Zum Beispiel: <ul><li>Name</li><li>Tastenbelegung</li><li>Lautstärke</li></ul>
 * Momentan wird nur der Name gespeichert und verwaltet
 * @since 17.06.2019
 * @author Jakob Kleine, Janni Röbbecke
 */
public class OptionsManager
{
    private static final String optionsPath = "/res/OPTIONS"; //Der Pfad zur Datei, in der die Optionen gespeichert werden
    private String playerName; //Der Dan
    //Hier sollten noch mehr Optionen gespeichert werden (Sounds, etc.)
    
    /**
     * Lädt die Optionen ein, die in der dafür vorhergesehenen Datei gespeichert werden.
     * @since 17.06.2019
     * @author Jakob Kleine, Janni Röbbecke
     */
    public OptionsManager() {
        loadOptions();
    }
    
    private void loadOptions() { //Lädt die Optionen
        String[] optionsData = Utils.loadFileAsString(optionsPath).split("\n"); //Lädt die Datei und spaltet den String an Zeilenumbrüchen
        if(optionsData.length == 0 || optionsData[0].isEmpty()) //Wenn keine Einstellungen geladen wurden oder der Name leer ist
            playerName = "Player123";
        else
            playerName = optionsData[0]; //Der Name steht in der ersten Zeile
    }
    
    /**
     * Gibt den Namen des Spielers zurück
     * @return der Name, den der Spieler sich selbst gegeben hat
     * @since 17.06.2019
     * @author Jakob Kleine, Janni Röbbecke
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Ändert den Namen des Spielers
     * @param newName der neue Name des Spielers
     * @since 17.06.2019
     * @author Jakob Kleine, Janni Röbbecke
     */
    public void setPlayerName(String newName) {
        playerName = newName;
    }
    
    /**
     * Speichert die Optionen, indem die Datei, in der die Optionen gespeichert werden sollen, überschieben wird.
     */
    public void saveOptions() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(Utils.absoluteFileOf(optionsPath));
        } 
        catch(FileNotFoundException e) { e.printStackTrace(); }
        if(writer != null) { //Nur wenn der File gefunden wurde
            writer.println(playerName);
            //Hier könnten noch mehr Optionen gespeichert werden
            writer.close();
        }
    }
}