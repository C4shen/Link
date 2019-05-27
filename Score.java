import java.util.Date;
/**
 * Beschreiben Sie hier die Klasse Score.
 * 
 * @author Cashen Adkins, Janni Roebbecke
 * @version
 * @since 27.5.2019
 */
public class Score
{
    private int score;
    private String name;
    private String date;
    public Score(int score, String name, String datum) {
        this.score = score;
        this.name = name;
        date = datum;
    }
    
    public Score(int score, String name) {
        this(score, name, Utils.parseDate(new Date()));
    }
    
    @Override
    public String toString() {
        return score + " - " + name + " ("+date+")";
    }
    
    public String capsleData() {
        return score + " " + name + " "+date;
    }
    
    public boolean isLessThan(Score s) {  
        if(score == s.score) 
            return name.compareTo(s.name) < 0;
        else
            return score < s.score; 
    }
}
