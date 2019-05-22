import java.awt.Graphics;
/**
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
    
        public static void setState(State state)
    {
      currentState = state;
    }
    
    public static State getState()
    {
      return currentState;
    }
    
    public abstract void update();
    
    public abstract void render(Graphics g);
}