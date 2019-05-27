import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
/**
 * Mit dem Screen wird ein Spiel visualisiert. Der Screen entspricht damit dem View des MVC-Konzepts.
 * Er besitzt eine Leinwand (Canvas), auf der der Hintergrund und Entities etc. gemalt werden.
 * und eienen Frame, in den die Leinwand verpackt wird.
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
     * @param listener ein WindowListener, der das Schließen des Fensters verwalten soll und damit ein geplantes Beenden des Spiels ermöglicht
     * @since 10.05.2019 (WindowListener seit 27.05)
     */
    public Screen(String title, int width, int height, WindowListener listener)
    {
        this.title = title;
        this.width = width;
        this.height = height;

        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //Beim Drücken des x oben Rechts soll nichts passieren. Das schließen verwaltet der WindowListener
        frame.addWindowListener(listener);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); //Positioniert den Frame in der Mitte des Bildschirms
        frame.setIconImage(new ImageIcon(Utils.absoluteFileOf("/res/icon.png").toString()).getImage());
        
        canvas = new Canvas();
        //Das Canvas soll genauso groß sein, wie der JFrame
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setFocusable(false);

        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
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
