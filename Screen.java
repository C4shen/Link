import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * Mit dem Screen wird ein Spiel visualisiert. 
 * Er besitzt eine Leinwand (Canvas), auf der der Hintergrund und Entities etc. angezeigt werden.
 * @author Cashen Adkins, Jakob Kleine, quizdroid.wordpress.com
 * @version 0.01 09.05.2019
 */
public class Screen 
{
    private JFrame frame;
    private Canvas canvas;

    private String title;
    private int width, height;

    /**
     * Erstellt einen neuen Screen, auf dem das Spiel visualisiert werden kann.
     * @author Cashen Adkins, Jakob Kleine
     * @param title der Titel des Spiels
     * @param width die Breite des Screens
     * @param height die Höhe des Screens
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
     */
    public Canvas getCanvas(){
        return canvas;
    }


    /**
     * Gibt den Frame des Screens zurück
     * @author Cashen Adkins, Jakob Kleine
     * @return der JFrame, der die Leinwand (Canvas) enthält
     */
    public JFrame getFrame(){
        return frame;
    }
}
