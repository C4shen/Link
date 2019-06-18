import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.EnumMap;
/**
 * Ein State ist ein Zustand in dem sich das Programm befinden kann.
 * Hier wird der aktuelle State und alle anderen States gespeichert und verwaltet. 
 * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
 * @since 25.05.2019 [neue Modellierung seit 15.06.2019]
 */
public abstract class State
{
    /**
     * Die Namen aller States, in dem sich das Spiel befinden kann. 
     * Sie sind der Schlüssel zu den jeweiligen States in einer EnumMap.
     * @since 17.06.2019
     * @author Jakob Kleine, Janni Röbbecke
     */
    protected enum StateNames { 
        MENU, GAME, TUTORIAL, OPTIONS, SCORES;
    }
    private static State currentState;
    
    private static EnumMap<StateNames, State> states;
    
    public static void initialise() {
        states = new EnumMap<StateNames, State>(StateNames.class);
        states.put(StateNames.MENU, new MainMenuState());
        //Der Game-State wird noch nicht initialisiert, damit das Spiel noch nicht startet
        states.put(StateNames.TUTORIAL, new TutorialState());
        states.put(StateNames.OPTIONS, new OptionsState(new OptionsManager()));
        states.put(StateNames.SCORES, new HighscoresState(new HighScoreManager()));
        
        setState(StateNames.MENU);
    }
    
    protected static void setState(StateNames newStateName) {
        currentState = states.get(newStateName);
    }
    
    /**
     * Aktualisiert den State.
     * @author Cashen Adkins, Janni Röbbecke, www.quizdroid.wordpress.com
     * @since 0.01 (22.05.2019)
     */
    public abstract void update(KeyManager keymanager);
    
    /**
     * Rendert den State.
     * @author Cashen Adkins, Janni Röbbecke, www.quizdroid.wordpress.com
     * @since 0.01 (22.05.2019)
     */
    public abstract void render(Graphics g);
    
    /**
     * Erzeugt einen neuen GameState und setzt alle Werte für die nächste Runde des Spiels zurück.
     * @author Jakob Kleine, Cashen Adkins, Janni Röbbecke
     * @since 25.05.2019
     */
    protected void newGame() {  
        states.put(StateNames.GAME, new GameState());
        Coffee.resetSelectedAmount(); //Die Effekte des Kaffes verändern sich, je mehr Kaffee eingesammelt wurde. Das muss jetzt zurückgesetzt werden
        setState(StateNames.GAME);
    }
    
    protected EnumMap<StateNames, State> getStates() {
        return states; 
    }
    
    /**
     * Setzt das Spiel nach einer Pause fort
     * @author Cashen Adkins, Janni Röbbecke
     * @since 25.05.2019
     */
    protected void resumeGame() {
        if(states.get(StateNames.GAME) != null) //Wenn das Spiel verloren wird, wird der GemeState auf null gesetzt. Dann könnte nicht weitergespielt werden 
            setState(StateNames.GAME);
    }
    
    public static State getState() {
        return currentState;
    }
    
    /** 
     * Beendet das Spiel
     * @author Cashen Adkins, Jakob Kleine
     * @since 0.01 (26.05.2019)
     */
    public void endGame() {
        ((HighscoresState) states.get(StateNames.SCORES)).getScoresManager().saveScores();
        ((OptionsState) states.get(StateNames.OPTIONS)).getOptionsManager().saveOptions();
        System.exit(0);
    }
}