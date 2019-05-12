import java.net.URL;
import java.io.*;
import java.util.Scanner;

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
     * Gibt einen File mit absoluten Pfad (tatsächlicher Pfad im System) eines 
     * relativen Pfads (Pfad von dem Ordner des BlueJ-Projekts aus) zurück
     * @author Jakob Kleine, Cashen Adkins
     * @param relativePath der relative Pfad, dessen absouter Pfad gesucht wird
     * @return ein File mit dem absuluten Pfad des relativen Pfads
     * @since 0.01 (12.05.2019)
     */
    public static File absoluteFileOf(String relativePath) {
        return new File(new File("").getAbsolutePath()+relativePath);
    }
    
    /**
     * Lädt eine Text-Datei (z.B. eine, die einen Room beschreibt) als String. 
     * Im Tutorial wurden die Klassen File- und BufferedReader verwendet. 
     * Die Klasse java.util.Scanner ist allerdings kompakter und übersichtlicher.
     * @author Jakob Kleine, Cepehr Bromand
     * @param path der relative Pfad der Textdatei
     * @return ein String der mit der Textdatei überinstimmt
     */
    public static String loadFileAsString(String path){
        StringBuilder builder = new StringBuilder(); //Effizienter, als immer zwei Strings zu verbindnen

        Scanner sc = null; //Die Variable muss aßerhalb des try-Blocks deklariert werden, damit sie außerhalb verwendet werden kann
        try {
            sc = new Scanner(absoluteFileOf(path));
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        if(sc != null) { //Wenn die Datei gefunden wurde
            while(sc.hasNextLine()) { //Solange der Scanner noch nicht am Ende angelangt ist
                //Im Tutorial wurde ein Zeilenumbruch angehöngt, was allerdings nicht sinnvoll ist, weil dann der String bei Leerzeichen und Zeilenumbrüchen
                //gesplittet werden muss. [WIR HABEN DAS ABER MAL TROTZDEM SO GELASSEN, SODASS WIRKLICH DIE DATEI VÖLLIG MIT DEM STRING ÜBEREINSTIMMT]
                builder.append(sc.nextLine() + "\n"); 
            }
            sc.close();
        }
        return builder.toString();
    }
}