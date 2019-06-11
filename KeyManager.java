import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 * Der KeyManager verwaltet die Eingaben über die Tastatur
 * @author Ares Zühlke, Janni Röbbecke, Jakob Kleine, Cashen Adkins, www.quizdroid.wordpress.com
 * @version 0.02 (12.05.2019)
 * @since 0.01 (11.05.2019)
 */
public class KeyManager implements KeyListener {
    private boolean capsLocked;
    private boolean[] keys; //Speichert für alle Tasten, ob sie gedrückt werden
    private boolean[] keysLast;
    private boolean[] relevantKeys; //Speichert, ob die relevanten Tasten für das Spiel gedrückt werden: w, s, a, d, space, escape (in dieser Reihenfolge; w a s d meint auch pfeile in entsprechender Richtung)
    private boolean[] lastStateRelevantKeys; //Speichert den letzten Status der relevanten Tasten, sodass die Taste nicht mehrmals pro Loop "gedrückt" wird, obwohl man sie nur einmal drücken will.
    /**
     * Erstellt einen neuen KeyManager.
     * @author Janni Röbbecke, Ares Zühlke, Jakob Kleine, www.quizdroid.wordpress.com
     * @since 0.01 (11.05.2019)
     */
    public KeyManager(){
        keys = new boolean[256];
        keysLast = new boolean[256];
        relevantKeys = new boolean[6];
        lastStateRelevantKeys = new boolean[6];
    }
    
    /**
     * Aktualisiert die Werte der Attribute <code>up</code>, <code>down</code>, <code>left</code>, <code>right</code>, <code>attack</code> und <code>escape</code>
     * @author Janni Röbbecke, Jakob Kleine, Ares Zühlke, Cashen Adkins, www.quizdroid.wordpress.com
     * @since 0.01 (11.05.2019)
     */
    public void update(){
        relevantKeys[0] = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]; //Bei der Steuerung der Richtung sollen auch die Pfeiltasten gültig sein
        relevantKeys[1] = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
        relevantKeys[2] = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
        relevantKeys[3] = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
        relevantKeys[4] = keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_ENTER]; //Bei der Enter soll auch einen Angirff starten (besonders wichtig im Hauptmenü)
        relevantKeys[5] = keys[KeyEvent.VK_ESCAPE] || keys[KeyEvent.VK_BACK_SPACE]; //Zum Beenden/Zurückkehren soll auch Backspace erlaubt sein
    }
    
    /**
     * Reageiert darauf, wenn eine Taste gedrückt (pressed) wurde.
     * @author Janni Röbbecke, Jakob Kleine, Ares Zühlke, www.quizdroid.wordpress.com
     * @param e das <code>KeyEvent</code>, das dabei eintritt
     * @since 0.01 (11.05.2019)
     */
    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true; //Der gedrückte Knopf wird jetzt als gedrückt gespeichert
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
        if(e.getKeyCode() == KeyEvent.VK_CAPS_LOCK)
            capsLocked = !capsLocked;
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
     * Ermittelt, ob eine Taste nur einmal gedrückt wurde. 
     * Dadurch kann verhindert werden, dass Tasten (wie zum Beispiel escape) mehrmals als gedrückt gewertet werden, wenn sie gedrückt gehalten werden.
     * @author Cashen Adkins, Jakob Kleine 
     * @since 24.05.2019 (Überarbeitet: 26.05.2019)
     * @param der Index der Taste im <code>relevantKeys</code>-Array
     * @return true, wenn die gewählte Taste gerade gedrückt wird, und bei der vorherigen Abfrage nicht gedrückt worden ist
     */
    private boolean keyPressedOnce(int keyIndex) {
        if(relevantKeys[keyIndex] && !lastStateRelevantKeys[keyIndex]) { //Nur wenn grade gedrückt && vorher nicht gedrückt
            lastStateRelevantKeys[keyIndex] = relevantKeys[keyIndex]; //Speichert den nun letzten Status des gewählten Keys
            return true;
        }
        else {
            lastStateRelevantKeys[keyIndex] = relevantKeys[keyIndex]; //Speichert den nun letzten Status des gewählten Keys
            return false;
        }
    }
    
    public boolean generalKeyPressedOnce(int keyIndex) {
        if(keys[keyIndex] && !keysLast[keyIndex]) { //Nur wenn grade gedrückt && vorher nicht gedrückt
            keysLast[keyIndex] = keys[keyIndex]; //Speichert den nun letzten Status des gewählten Keys
            return true;
        }
        else {
            keysLast[keyIndex] = keys[keyIndex]; //Speichert den nun letzten Status des gewählten Keys
            return false;
        }
    }
    
    public boolean crtl() {
        return keys[KeyEvent.VK_CONTROL];
    }
    
    public boolean upperCase() {
        return keys[KeyEvent.VK_SHIFT] ^ capsLocked;
    }
    
    /**
     * Ermittelt, ob die Taste nach oben momentan gedrückt wird
     * @author Jakob Kleine, Janni Röbbecke
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach oben gedrückt wird
     * @since 0.02 (12.05.2019)
     */
    public boolean up() {
        return relevantKeys[0];
    }
    
    /**
     * Ermittelt, ob die Up-Taste ihren Status geändert hat, sodass egal wie lange man die Taste drückt, nur bei der ersten Abfrage true zurückgegeben wird.
     * @author Cashen Adkins, Janni Röbbecke
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach oben mit einer Statusveränderung gedrückt wird.
     * @since 0.02 (24.05.2019)
     */
    public boolean upEinmal() {
        return keyPressedOnce(0);
    }
    
    /**
     * Ermittelt, ob die Taste nach unten momentan gedrückt wird
     * @author Jakob Kleine, Janni Röbbecke
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach unten gedrückt wird
     * @since 0.02 (12.05.2019)
     */
    public boolean down() {
        return relevantKeys[1];
    }
    
    /**
     * Ermittelt, ob die Down-Taste ihren Status geändert hat, sodass egal wie lange man die Taste drückt, nur bei der ersten Abfrage true zurückgegeben wird.
     * @author Cashen Adkins, Janni Röbbecke
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach unten mit einer Statusveränderung gedrückt wird.
     * @since 0.02 (24.05.2019)
     */
    public boolean downEinmal() {
        return keyPressedOnce(1);
    }
    
    /**
     * Ermittelt, ob die Taste nach links momentan gedrückt wird
     * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach links gedrückt wird
     * @since 0.02 (12.05.2019)
     */
    public boolean left() {
        return relevantKeys[2];
    }
    
    /**
     * Ermittelt, ob die Left-Taste ihren Status geändert hat, sodass egal wie lange man die Taste drückt, nur bei der ersten Abfrage true zurückgegeben wird.
     * @author Cashen Adkins, Jakob Kleine
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach linkks mit einer Statusveränderung gedrückt wird.
     * @since 0.02 (24.05.2019)
     */
    public boolean leftEinmal() {
        return keyPressedOnce(2);
    }
    
    /**
     * Ermittelt, ob die Taste nach rechts momentan gedrückt wird
     * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach rechts gedrückt wird
     * @since 0.02 (12.05.2019)
     */
    public boolean right() {
        return relevantKeys[3];
    }
    
    /**
     * Ermittelt, ob die Right-Taste ihren Status geändert hat, sodass egal wie lange man die Taste drückt, nur bei der ersten Abfrage true zurückgegeben wird.
     * @author Cashen Adkins, Janni Röbbecke
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach rechts mit einer Statusveränderung gedrückt wird.
     * @since 0.02 (24.05.2019)
     */
    public boolean rightEinmal() {
        return keyPressedOnce(3);
    }
    
    /**
     * Ermittelt, ob die Angriffs-Taste momentan gedrückt wird
     * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
     * @return ein boolean, der angibt, ob die Taste für eine Bewegung nach rechts gedrückt wird
     * @since 0.02 (12.05.2019)
     */
    public boolean attack() {
        return relevantKeys[4];
    }
    
    /**
     * Ermittelt, ob die Attack-Taste ihren Status geändert hat, sodass egal wie lange man die Taste drückt, nur bei der ersten Abfrage true zurückgegeben wird.
     * @author Cashen Adkins, Janni Röbbecke
     * @return ein boolean, der angibt, ob die Taste escape mit einer Statusveränderung gedrückt wird
     * @since 0.02 (26.05.2019)
     */
    public boolean attackEinmal() {
        return keyPressedOnce(4);
    }
    
    /**
     * Ermittelt, ob die Escape-Taste ihren Status geändert hat, sodass egal wie lange man die Taste drückt, nur einmal das Pausenmenü geöffnet wird.
     * @author Cashen Adkins, Janni Röbbecke
     * @return ein boolean, der angibt, ob die Taste escape mit einer Statusveränderung gedrückt wird
     * @since 0.02 (22.05.2019)
     */
    public boolean escapeEinmal() {
        return keyPressedOnce(5);
    }
}