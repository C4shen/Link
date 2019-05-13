import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * Mit dem Screen wird ein Spiel visualisiert. 
 * Er besitzt eine Leinwand (Canvas), auf der der Hintergrund und Entities etc. gemalt werden.
 * und eienen 
 * @author Cashen Adkins, Jakob Kleine, quizdroid.wordpress.com
 * @version 0.01 09.05.2019
 */
public class Screen 
{
    private JFrame frame; //Enthält die Leinwand
    private Canvas canvas; //Leinwand: Hierauf wird gemalt

    private String title; //Name des Frames
    private int width, height; //Maße des Frames

    /**
     * Erstellt einen neuen Screen, auf dem das Spiel visualisiert werden kann.
     * @author Cashen Adkins, Jakob Kleine
     * @param title der Titel des Spiels
     * @param width die Breite des Screens
     * @param height die Höhe des Screens
     * @since 0.01 (10.05.2019)
     */
    public Screen(String title, int width, int height)
    {
        this.title = title;
        this.width = width;
        this.height = height;

        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Damit die Virtuelle Maschine beim Schließen des Frames beendet wird
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); //Positioniert den Frame in der Mitte des Bildschirms
        frame.setVisible(true);

        canvas = new Canvas();
        //Das Canvas soll genauso groß sein, wie der JFrame
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setFocusable(false);

        frame.add(canvas);
        frame.pack();
    }

    /**
     * Gibt die Leinwand des Screens zurück
     * @author Cashen Adkins, Jakob Kleine
     * @return die Leinwand (Canvas) des Screens, auf der die Objekte "gemalt" werden
     * @since 0.01 (10.05.2019)
     */
    public Canvas getCanvas(){
        return canvas;
    }
    
    /**
     * Gibt den Frame des Screens zurück
     * @author Cashen Adkins, Jakob Kleine
     * @return der JFrame, der die Leinwand (Canvas) enthält
     * @since 0.01 (10.05.2019)
     */
    public JFrame getFrame(){
        return frame;
    }
}
