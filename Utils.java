import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.Random;
import java.util.Date;
import java.awt.Graphics;

/**
 * Eine Klasse mit nützlichen öfters benutzen Methoden, die nicht unbedingt einer bestimmten Klasse zugeordnet werden können.
 * 
 * @author Jakob Kleine, Cashen Adkins, Cepehr Bromand, Janni Röbbecke, quizdroid.wordpress.com
 * @since 0.01 (10.05.2019)
 * @version 0.02 (12.05.2019)
 */
public class Utils 
{
    private static Random rGenerator = new Random(); //Ein Generator für zufällige Zahlen
    
    /**
     * Zeichnet einen Text zentriert in der angegebenen Breite
     * @author Jakob Kleine, Cepehr Bromand
     * @since 27.05.2019
     * @param g die Graphics, mit denen der Text gemalt werden soll
     * @param text der Text der gemalt werden soll
     * @param width die Breite, innerhalb der der Text zentiert werden soll
     * @param yPosition die y-Koordinate, bei der der Text stehen soll
     */
    public static void centerText(Graphics g, String text, int width, int yPosition) {
        //width/2 -> Text würde in Mitte starten; also width/2 - textBreite/2 -> passend verschoben; dann ausgeklammert -> (width-textBreite)/2
        g.drawString(text, (width - textBreite(g, text))/2, yPosition);
    }
    
    /**
     * Berechnet die theoretische Breite eines Textes, wenn er mit den angegebenen Graphics gemalt werden würde
     * @author Jakob Kleine, Cepehr Bromand
     * @since 27.05.2019
     * [@param und @return wegen Redundanz weggelassen]
     */
    private static int textBreite(Graphics g, String text) {
        return g.getFontMetrics().stringWidth(text);
    }
    
    /**
     * Wandelt ein <code>java.util.Date</code> in einen String nach dem Schema { [D]D.[M]M.YY } um
     * @param datum das Datum das formatiert werden soll
     * @return ein String, der das angegebene Datum wiedergibt
     * @author Janni Röbbecke, Cashen Adkins
     */
    public static String parseDate(Date datum) { 
        return Integer.toString(datum.getDate()) + "."+ Integer.toString(1 + datum.getMonth()) + "." + Integer.toString(1900 + datum.getYear());
    }
    
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
     * Färbt ein Bild in der angegebenen Farbe mit der angegeben Transparenz ein
     * @author Jakob Kleine, Janni Röbbecke
     * @since 24.05.2019
     * @param i das Bild das eingefärbt werden soll
     * @param dye die Farbe, die über das Bild gelegt werden soll
     * @param alpha die Transparenz (0-255; inklusiv) mit der die Farbe über das Bild gelegt werden soll
     * @return ein Bild das entsprechend eingefärbt wurde
     */
    public static BufferedImage dyeImage(BufferedImage i, Color dye, int alpha) {
        BufferedImage gefaerbtesBild = new BufferedImage(i.getWidth(), i.getHeight(), i.getType());
        boolean vorherTransparent = true; //Speichert, ob der vorherige Pixel transparent war, sodass ein opaker Rahmen um das Bild gezogen werden kann
        for (int x = 0; x < i.getWidth(); x++) { //Alle Pixel werden durchgegangen
            for (int y = 0; y < i.getHeight(); y++) {
                int pixel = i.getRGB(x,y); //Der Pixel an der aktuellen Stelle im Originalbild
                if(new Color(pixel, true).getAlpha() == 0) { //Wenn das Bild an dieser Stelle transparent ist 
                    gefaerbtesBild.setRGB(x, y, pixel); //Der Pixel soll übernommen werden und nicht rot gefärbt sein
                    vorherTransparent = true;
                }
                else {//Sonst werden die beiden Farben gemischt
                    //Wenn der vorherige Pixel transparent ist || (Wenn y noch nicht am Ende ist && der nächste Pixel transparent ist)
                    //  dann wird der Pixel eingefärbt aber nicht mit der angegebenen Transparenz, sondern völlig opak -> opaker Rahmen
                    if(vorherTransparent || (y+1 < i.getHeight() && new Color(i.getRGB(x, y+1), true).getAlpha() == 0)) 
                        gefaerbtesBild.setRGB(x, y, dye.getRGB());
                    else
                        gefaerbtesBild.setRGB(x, y, mixColorsWithAlpha(new Color(pixel, true), dye, alpha).getRGB());
                    vorherTransparent = false;
                }
            }
        }
        return gefaerbtesBild;
    }
    
    /**
     * Vermischt zwei Farben mit der angegebenen Tranzparenz so, dass die zweite Farbe quais über die ertse gelegt wird
     * @author Jakob Kleine, Janni Röbbecke, https://stackoverflow.com/questions/8409827/combining-2-rgb-colors-with-alpha
     * @since 24.05.2019
     * @param color1 die "untere" Farbe
     * @param color2 die "obere" Farbe
     * @param alpha die Transparenz, mit der color2 über color1 gelegt werden soll; von 0-255, inklusive
     * @return eine Farbe, die bei der Vermischung entsteht
     */
    private static Color mixColorsWithAlpha(Color color1, Color color2, int alpha)
    {
        float factor = alpha / 255f;
        //Es wird immer von der unteren Farbe der Farbwert (jeweils R,G,B) in der Transparenz abgezogen und von dem Farbwert der oberen Farbe ersetzt
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
     * Lädt eine Text-Datei (z.B. eine, die einen Room beschreibt oder die Bestenliste) als String. 
     * Im Tutorial wurden die Klassen File- und BufferedReader verwendet. 
     * Die Klasse java.util.Scanner ist allerdings kompakter und übersichtlicher.
     * @author Jakob Kleine, Cepehr Bromand, nach www.quizdroid.wordpress.com
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
    
    /**
     * Erzeugt eine zufällige Zahl zwischen <code>grenze1</code> und <code>grenze2</code>
     * @author Janni Röbbecke, Jakob Kleine
     * @param grenze1 die untere Grenze (inklusiv)
     * @param grenze2 die obere Grenze (inklusiv)
     * @return eine zufällige Zahl zwischen grenze1 und grenze2 (inklusiv)
     */
    public static int random(int grenze1, int grenze2) {
        /* Random.nextInt(x) gibt eine zufällige Zahl zw. 0 (inklusiv) und x (exklusiv) zurück. 
         * Die untere Grenze muss also mit +grenze1 nach oben verschoben werden.
         * Random.nextInt(x)+y gäbe eine zufällige Zahl zw. y (inklusiv) und x+y (exklusiv) 
         * Also muss die obere Grenze noch nach unten verschoben werden.
         * -> Random.nextInt(x-y)+y gibt eine zufällige Zahl zw. y (inklusiv) und x (exklusiv) 
         * Also muss die obere Grenze um 1 erhöht werden, damit sie x (inklusiv) ist.
         * -> return Random.nextInt(x-y+1)+y
         */
        return rGenerator.nextInt(grenze2 - grenze1 +1) + grenze1;
    }
}