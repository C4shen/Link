import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
/**
 * Der Highscore-Manager verwaltet die Highscores im Spiel. 
 * Sie werden zu beginn aus einer Text-Datei ausgelesen und hier gespeichert.
 * Während dem Spielen können neue Einträge gemacht werden, die in die Score-Liste eingetragen werden.
 * Beim Beenden des Spiels wird die Text-Datei überschrieben und die Highscores werden aktualisiert.
 * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
 * @since 27.05.2019
 */
public class HighScoreManager 
{
    private static final int MAX_SCORE_NUMBER = 10; //Die maximale Anzahl an zu speichernden Scores
    private static final String path = "/res/scores.link"; //Der Pfad zur Highscore-Textdatei
    private ArrayList<Score> scores; //Eine Liste mit allen highscores
    /**
     * Erstellt einen neuen HighScoreManager, der die bisher erzielten Highscores aus der Highscore-Text-Datei einliest.
     * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
     * @since 27.05.2019
     */
    public HighScoreManager() {
        String[] scoreDaten = Utils.loadFileAsString(path).split("\\s+");
        //Ein Score besteht aus den drei Teilen [ScoreValue] [Name] [Datum]
        scores = new ArrayList<Score>(scoreDaten.length/3); //Um Laufzeit zu sparen, wird die Größe der Liste angegeben (sie ist ja bekannt)
        for(int i=0; i<scoreDaten.length-2; i++) { //.length-2, weil in einer Zeile i zwei Mal erhöht werden muss.
            Score scoreElement = new Score(Utils.parseInt(scoreDaten[i++]), scoreDaten[i++], scoreDaten[i]);
            scores.add(scoreElement);
        }
        sortScores(); //Sortiert sicherheitshalber die Scores nach größe
        sortOutScores(); //Sortiert sicherheitshalber die überschüssigen Scores aus
    }
    
    /**
     * Fügt der Score-Liste einen neuen Score hinzu. Die Scoreliste wird anschließend sortiert und der schlechteste Score wird ggf. entnommen, wenn
     * die maximale Anzahl an zu speichernden Scores überschritten wurde
     * @param newScore der Score, der in die Score-Liste aufgenommen werden soll.
     * @author Jakob Kleine, Cashen Adkins, Janni Röbbecke
     * @since 27.05.2019
     */
    public void addScore(Score newScore) {
        scores.add(newScore);
        sortScores();
        sortOutScores();
    }
    
    /**
     * Speichert die High-Scores, indem sie in die Highscore-Textdatei eingetragen werden.
     */
    public void saveScores() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(Utils.absoluteFileOf(path));
        } 
        catch(FileNotFoundException e) { e.printStackTrace(); }
        if(writer != null) {
            for(Score s : scores) {
                writer.print(s.capsuleData()+"\n"); //es wird .capsuleData und nicht toString aufgerufen, weil die [Score] [Name] [Datum] nur mit Leerzeichen getrennt werden sollen
            }
            writer.close();
        }
    }
    
    /**
     * Sortiert solange die schlechtesten Scores aus der Score-Liste, bis nur noch die 
     * maximale Anzahl an zu speichernden Socres [<code>MAX_SCORE_NUMBER</code>] in der Liste ist.
     * @author Jakob Kleine, Cashen Adkins
     * @since 27.05.2019
     */
    private void sortOutScores() {
        while(scores.size() > MAX_SCORE_NUMBER) 
            scores.remove(scores.size()-1); //Wenn zu viele Scores gespeichert werden, wird der letzte gelöscht
    }
    
    /**
     * Sortiert die Liste der Scores mithilfe von Insertion-Sort.
     * Dieser Sortier-Algorithmus bietet sich hier an, weil die Score-Liste i.d.R. fast völlig sortiert ist, und immer nur 
     * ein neuer Wert eingeordnet werden muss.
     * @author Jakob Kleine, Cashen Adkins [nach Shium Rahman und Clemens Zander]
     * @since 27.05.2019
     */
    private void sortScores() {
        int laenge = scores.size();
        for(int i=1;i<laenge;i++)
        {
            Score value = scores.get(i);
            int hole=i;
            while(hole>0 && scores.get(hole-1).isLessThan(value))
            {
                scores.set(hole, scores.get(hole-1));
                hole--;
            }
            scores.set(hole, value);
        }
    }
    
    /**
     * Gibt die Liste mit den HighScores aus.
     * @return eine Liste mit den HighScores, absteigend nach ihrer Größe sortiert
     * @author Janni Röbbecke, Jakob Kleine, Cashen Adkins
     * @since 27.05.2019
     */
    public ArrayList<Score> getScores() {
        return scores;
    }
}