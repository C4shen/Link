import java.util.ArrayList;
public class HighScoreManager 
{
    private static final int MAX_SCORE_NUMBER = 5;
    String path;
    ArrayList<Score> scores;
    public HighScoreManager(String scoresPath) {
        path = scoresPath;
        //String scoreDatei = loadFileAsString("");
    }
    
    public void addScore(Score newScore) {
        scores.add(newScore);
        sortScores();
        if(scores.size() > MAX_SCORE_NUMBER) 
            scores.remove(scores.size()-1); //Wenn zu viele Scores gespeichert werden, wird der letzte gelöscht
    }
    
    public void saveScores() {
        
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
            while(hole>0 && scores.get(hole-1).isGreaterThan(value))
            {
                scores.set(hole, scores.get(hole-1));
                hole--;
            }
            scores.set(hole, value);
        }

    }
}