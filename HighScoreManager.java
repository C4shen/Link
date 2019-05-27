import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
public class HighScoreManager 
{
    private static final int MAX_SCORE_NUMBER = 10;
    String path;
    ArrayList<Score> scores;
    public HighScoreManager(String scoresPath) {
        path = scoresPath;
        String[] scoreDaten = Utils.loadFileAsString(scoresPath).split("\\s+");
        scores = new ArrayList<Score>(scoreDaten.length/3);
        for(int i=0; i<scoreDaten.length-2; i++) {
            Score scoreElement = new Score(Utils.parseInt(scoreDaten[i++]), scoreDaten[i++], scoreDaten[i]);
            scores.add(scoreElement);
        }
    }
    
    public void addScore(Score newScore) {
        scores.add(newScore);
        sortScores();
        if(scores.size() > MAX_SCORE_NUMBER) 
            scores.remove(scores.size()-1); //Wenn zu viele Scores gespeichert werden, wird der letzte gelöscht
    }
    
    public void saveScores() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(Utils.absoluteFileOf(path));
        } 
        catch(FileNotFoundException e) { e.printStackTrace(); }
        if(writer != null) {
            for(Score s : scores) {
                writer.print(s.capsleData()+"\n");
            }
            writer.close();
        }
    }
    
    /**
     * Insertion Sort
     */
    private void sortScores()
    {
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
    
    public ArrayList<Score> getScores() {
        return scores;
    }
}