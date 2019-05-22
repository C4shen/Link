import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 * Der KeyManager verwaltet die Eingaben über die Tastatur
 * @author Ares Zühlke, Janni Röbbecke, Jakob Kleine, Cashen Adkins, www.quizdroid.wordpress.com
 * @version 0.02 (12.05.2019)
 * @since 0.01 (11.05.2019)
 */
public class KeyManager implements KeyListener {
    private boolean[] keys; //Speichert für alle Tasten, ob sie gedrückt werden
    private boolean up, down, left, right, attack, escape; //Speichert, ob die Tasten für eine Bewegung nach oben, unten usw. gedrückt werden
    private boolean letzterStatusEscape; //Speichert den letzten Status der Escape-Taste sodass die Taste nicht mehrmals pro Loop "gedrückt" wird, obwohl man sie nur einmal drücken will.
    
    /**
     * Erstellt einen neuen KeyManager.
     * @author Janni Röbbecke, Ares Zühlke, Jakob Kleine, www.quizdroid.wordpress.com
     * @since 0.01 (11.05.2019)
     */
    public KeyManager(){
        keys = new boolean[256];
        letzterStatusEscape = false;
    }
    
    /**
     * Aktualisiert die Werte der Attribute <code>up</code>, <code>down</code>, <code>left</code>, <code>right</code>, <code>attack</code> und <code>escape</code>
     * @author Janni Röbbecke, Jakob Kleine, Ares Zühlke, Cashen Adkins, www.quizdroid.wordpress.com
     * @since 0.01 (11.05.2019)
     */
    public void update(){
        up = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
        attack = keys[KeyEvent.VK_SPACE];
        escape = keys[KeyEvent.VK_ESCAPE];
    }
    
    /**
     * Reageiert darauf, wenn eine Taste gedrückt (pressed) wurde.
     * @author Janni Röbbecke, Jakob Kleine, Ares Zühlke, www.quizdroid.wordpress.com
     * @param e das <code>KeyEvent</code>, das dabei eintritt
     * @since 0.01 (11.05.2019)
     */
    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true; //Der gedrückte Knopf wird jetzt gedrückt
    }
    
    /**
     * Reageiert darauf, wenn eine gedrückte Taste losgelassen (released) wurde.
     * @author Janni Röbbecke, Jakob Kleine, Ares Zühlke, www.quizdroid.wordpress.com
     * @param e das <code>KeyEvent</code>, das dabei eintritt
     * @since 0.01 (11.05.2019)
     */
    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false; //Der losgelassene Knopf wird nicht mehr gedrückt
    }
    
    /**
     * Reagiert darauf, wenn eine Taste gerückt und wieder losgelassen (getyped) wurde.
     * Hier muss darauf nicht reagiert werden.
     * @author Janni Röbbecke, Jakob Kleine, Ares Zühlke, www.quizdroid.wordpress.com
     * @param e das <code>KeyEvent</code>, das dabei eintritt
     * @since 0.01 (11.05.2019)
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    /**
     * Ermittelt, ob die Taste nach oben momentan gedrückt wird
     * @author Jakob Kleine, Janni Röbbecke
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach oben gedrückt wird
     * @since 0.02 (12.05.2019)
     */
    public boolean up() {
        return up;
    }
    
    /**
     * Ermittelt, ob die Taste nach unten momentan gedrückt wird
     * @author Jakob Kleine, Janni Röbbecke
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach unten gedrückt wird
     * @since 0.02 (12.05.2019)
     */
    public boolean down() {
        return down;
    }
    
    /**
     * Ermittelt, ob die Taste nach links momentan gedrückt wird
     * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach links gedrückt wird
     * @since 0.02 (12.05.2019)
     */
    public boolean left() {
        return left;
    }
    
    /**
     * Ermittelt, ob die Taste nach rechts momentan gedrückt wird
     * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach rechts gedrückt wird
     * @since 0.02 (12.05.2019)
     */
    public boolean right() {
        return right;
    }
    
    /**
     * Ermittelt, ob die Angriffs-Taste momentan gedrückt wird
     * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach rechts gedrückt wird
     * @since 0.02 (12.05.2019)
     */
    public boolean attack() {
        return attack;
    }
    
    /**
     * Ermittelt, ob die Escape-Taste ihren Status geändert hat, sodass egal wie lange man die Taste drückt, nur einmal das Puasenmenü geöffnet wird.
     * @author Cashen Adkins, Janni Röbbecke
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach rechts gedrückt wird
     * @since 0.02 (12.05.2019)
     */
    public boolean escape() {
        if(escape && !letzterStatusEscape)
        {
            letzterStatusEscape = escape;
            return true;
        }
        else
        {
            letzterStatusEscape = escape;
            return false;
        }
    }
}