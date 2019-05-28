import java.util.Date;
/**
 * Ein Score besteht aus einem Score-Wert, dem Namen des Spielers, der den Score erreicht hat und dem Datum des Spiels, in dem der Score erzielt wurde.
 * @author Cashen Adkins, Janni Roebbecke
 * @since 27.5.2019
 */
public class Score
{
    private int score; 
    private String name; 
    private String date;
    /**
     * Erstellt einen neuen Score. Dieser Konstuktor wird verwendet, um alte Scores aus der Text-Datei wiederherzustellen;  
     * deswegen wird das Datum als Parameter übergeben und nicht das aktuelle Datum verwendet.
     * @param score der Wert des Scores
     * @param name der Name des Spielers, der den Score erzielt hat
     * @param datum das Datum des Spiels, bei dem der Score erzielt wurde
     * @author Cashen Adkins, Janni Roebbecke
     * @since 27.5.2019
     */
    public Score(int score, String name, String datum) {
        this.score = score;
        this.name = name;
        date = datum;
    }

    /**
     * Erstellt einen neuen Score. Dieser Konstuktor wird verwendet, um neue Scores zu erstellen; 
     * deswegen wird das Datum nicht als Parameter übergeben sondern das aktuelle Datum verwendet.
     * @param score der Wert des Scores
     * @param name der Name des Spielers, der den Score erzielt hat
     * @author Cashen Adkins, Janni Roebbecke
     * @since 27.5.2019
     */
    public Score(int score, String name) {
        this(score, name, Utils.parseDate(new Date()));
    }
    
    /**
     * Gibt einen String aus, der diesen Score nach dem Schema {[Score-wert] - [Score-"Besitzer"] ([Score-Datum])} beschreibt.
     * @return ein String nach dem Schema {[Score-wert] - [Score-"Besitzer"] ([Score-Datum])}
     * @author Cashen Adkins, Janni Roebbecke
     * @since 27.5.2019
     */
    @Override
    public String toString() {
        return score + " - " + name + " ("+date+")";
    }
    
    /**
     * Gibt einen String aus, der die Daten dieses Scores nach dem Schema {[Score-wert] [Score-"Besitzer"] [Score-Datum]} kapselt.
     * Diese Methode wird verwendet, um Scores in der Highscore-Textdatei zu speichern, in der die Daten nur mit Leerzeichen getrennt werden sollen.
     * @return ein String nach dem Schema [Score-wert] [Score-"Besitzer"] [Score-Datum]} 
     * @author Cashen Adkins, Janni Roebbecke
     * @since 27.5.2019
     */
    public String capsuleData() {
        return score + " " + name + " "+date;
    }
    
    /**
     * Vergleicht diesen Score mit einem anderen, für das Sortieren der Scores.
     * @param s der Score, mit dem dieser Score verglichen werden soll
     * @return true, wenn der Score-Wert dieses Scores kleiner ist als der des anderen. 
     *         <br>Sind die Werte gleich groß, werden die Namen der "Besitzer" der Scores lexikographisch verglichen
     */
    public boolean isLessThan(Score s) {  
        if(score == s.score) 
            return name.compareTo(s.name) < 0;
        else
            return score < s.score; 
    }
}
