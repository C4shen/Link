import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.awt.Color;
/**
 * Eine Klasse mit nützlichen öfters benutzen Methoden, die nicht unbedingt einer bestimmten Klasse zugeordnet werden können 
 * 
 * @author Jakob Kleine, Cashen Adkins, Cepehr Bromand, quizdroid.wordpress.com
 * @since 0.01 (10.05.2019)
 * @version 0.02 (12.05.2019)
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
     * Färbt ein Bild ein der angegebenen Farbe mit der angegeben Transparenz ein
     * @author Jakob Kleine, Janni Röbbecke
     * since 24.05.2019
     * @param i das Bild das eingefärbt werden soll
     * @param dye die Farbe, die über das Bild gelegt werden soll
     * @param alpha die Transparent (0-255; inklusiv) mit der die Farbe über das Bild gelegt werden soll
     * @return ein Bild das entsprechend eingefärbt wurde
     */
    public static BufferedImage dyeImage(BufferedImage i, Color dye, int alpha) {
        BufferedImage gefaerbtesBild = new BufferedImage(i.getWidth(), i.getHeight(), i.getType());

        for (int x = 0; x < i.getWidth(); x++) { //Alle Pixel werden durchgegangen
            for (int y = 0; y < i.getHeight(); y++) {
                int pixel = i.getRGB(x,y); //Der Pixel an der aktuellen Stelle im Originalbild
                if(new Color(pixel, true).getAlpha() == 0) //Wenn das Bild an dieser Stelle transparent ist 
                    gefaerbtesBild.setRGB(x, y, pixel); //Der Pixel soll übernommen werden und nicht rot gefärbt sein
                else //Sonst werden die beiden Farben gemischt
                    gefaerbtesBild.setRGB(x, y, mixColorsWithAlpha(new Color(pixel, true), dye, alpha).getRGB());
            }
        }
        return gefaerbtesBild;
    }
    
    /**
     * Vermischt zwei Farben mit der angegebenen Tranzparenz so, dass die zweite Farbe quais über die ertse gelegt wird
     * @author Jakob Kleine, Janni Röbbecke, https://stackoverflow.com/questions/8409827/combining-2-rgb-colors-with-alpha
     * since 24.05.2019
     * @param color1 die "untere" Farbe
     * @param color2 die "obere" Farbe
     * @param alpha die Transparenz, mit der color2 über color1 gelegt werden soll; von 0-255, inklusive
     * @return eine Farbe, die bei der Vermischung entsteht
     */
    private static Color mixColorsWithAlpha(Color color1, Color color2, int alpha)
    {
        float factor = alpha / 255f;
        int red = (int) (color1.getRed() * (1 - factor) + color2.getRed() * factor);
        int green = (int) (color1.getGreen() * (1 - factor) + color2.getGreen() * factor);
        int blue = (int) (color1.getBlue() * (1 - factor) + color2.getBlue() * factor);
        return new Color(red, green, blue);
    }
    
    /**
     * Gibt einen File mit absoluten Pfad (tatsächlicher Pfad im System; z.B. C:/[...]) eines 
     * relativen Pfads (Pfad von dem Ordner des BlueJ-Projekts aus; ../[...]) zurück
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
     * @return ein String der mit der Textdatei übereinstimmt
     * @since 0.01 (10.05.2019)
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
                builder.append(sc.nextLine() + "\n"); 
            }
            sc.close();
        }
        return builder.toString();
    }
}