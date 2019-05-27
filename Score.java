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
    private Date datum;

    /**
     * Konstruktor für Objekte der Klasse Score
     */
    public Score(int score, String name)
    {
        this.score = score;
        this.name = name;
        datum = new Date();
    }
    
    public String toString()
    {
        return getName() + " " + getScore() + " " + getDate();
    }
    

    public int getScore() {return score;}
    public String getName() {return name;}
    public String getDate() { return Integer.toString(datum.getDate()) + "."+ Integer.toString(1 + datum.getMonth()) + "." + Integer.toString(1900 + datum.getYear());}

}
