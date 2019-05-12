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
     * @since 1.0
     * @param s der String, der in eine Zahl umgewandelt werden soll
     * @return ein int, der in dem String stand; -1, wenn der String keine Zahl enthielt
     */
    public static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        }
        catch(NumberFormatException nfe) {
            return -1;
        }
    }
    
    public static URL absolutePathOf(String relativePath) {
        return Utils.class.getResource("res/"+relativePath);
    }
}