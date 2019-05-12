import java.net.URL;
/**
 * Eine Klasse mit nützlichen öfters benutzen Methoden, die keiner bestimmten Klasse zugeordnet werden können 
 * 
 * @author Jakob Kleine, Cashen Adkins, quizdroid.wordpress.com
 */
public class Utils 
{
    /**
     * Wandelt eine Zeichenkette in eine Zahl um
     * @author Jakob Kleine, Cashen Adkins
     * @param s der String, der in eine Zahl umgewandelt werden soll
     * @return ein int, der in dem String stand; -1, wenn der String keine Zahl enthielt
     * @since 0.01 (10.05.2019)
     */
    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        }
        catch(NumberFormatException nfe) {
            return -1;
        }
    }
    
    /**
     * Gibt den absoluten Pfad (tatsächlicher Pfad im System) eines 
     * relativen Pfads (Pfad von dem Ordner des BlueJ-Projekts aus) zurück
     * @author Jakob Kleine, Cashen Adkins
     * @param relativePath der relative Pfad, dessen absouter Pfad gesucht wird
     * @return eine URL, die den absuluten Pfad des relativen Pfads enthält
     * @since 0.01 (12.05.2019)
     */
    public static URL absolutePathOf(String relativePath) {
        return Utils.class.getResource(relativePath);
    }
}