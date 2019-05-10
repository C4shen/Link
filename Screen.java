import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * @author Cashen Adkins, Janni Röbbecke, quizdroid.wordpress.com
 * @version 0.01 10.05.2019
 */
public class Screen 
{
    private JFrame frame;
    private Canvas canvas;

    private String title;
    private int width, height;

    public Screen(String title, int width, int height)
    {
        //Die Parameter werden als Attribute gespeichert.
        this.title = title;
        this.width = width;
        this.height = height;

        //Es wird ein neues Fenster erstellt mit dem Titel-Attribut als Titel und den Dimensions-Attributen als Dimensionen.
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Das Fenster hat eine Feste Breite und Höhe, die vom Benutzer erstmals unveränderbar ist.
        frame.setResizable(false);
        //Das Fenster befindet sich im Zentrum des Bildschirmes.
        frame.setLocationRelativeTo(null);
        //Das Fenster wird angezeigt.
        frame.setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setFocusable(false);

        frame.add(canvas);
        frame.pack();
    }

    public Canvas getCanvas(){
        return canvas;
    }

    public JFrame getFrame(){
        return frame;
    }
}
