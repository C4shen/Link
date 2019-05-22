import java.awt.Graphics;
/**
 * 
 * @author Cashen Adkins,Janni Röbbecke, www.quizdroid.wordpress.com
 * @version 0.02 (22.05.2019)
 * @since 0.01 (22.05.2019
 */
public abstract class State 
{
    private static State currentState = null;
    protected Game game;
    public State(Game game)
    {
      this.game = game;
    }
    
    /**
     * Ändert den State des Spiels
     * @author Cashen Adkins, Janni Röbbecke, www.quizdroid.wordpress.com
     * @param state Der neue State des Spiels.
     * @since 0.01 (22.05.2019)
     */
    public static void setState(State state)
    {
      currentState = state;
    }
    
    /**
     * Gibt den State des Spiels wieder.
     * @author Cashen Adkins, Janni Röbbecke, www.quizdroid.wordpress.com
     * @return Momentaner State des Spiels.
     * @since 0.01 (22.05.2019)
     */
    public static State getState()
    {
      return currentState;
    }
    
    /**
     * Aktualisiert den State.
     * @author Cashen Adkins, Janni Röbbecke, www.quizdroid.wordpress.com
     * @since 0.01 (22.05.2019)
     */
    public abstract void update();
    
    /**
     * Rendert den State.
     * @author Cashen Adkins, Janni Röbbecke, www.quizdroid.wordpress.com
     * @since 0.01 (22.05.2019)
     */
    public abstract void render(Graphics g);
}