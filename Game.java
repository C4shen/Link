/**
 * @author Cashen Adkins, quizdroid.wordpress.com
 * @version 0.01 09.05.2019
 */
public class Game implements Runnable {
    //Das Bild des Spieles soll 60 Mal pro Sekunde erneuert werden (Frames per Second)
    public static final int FPS = 60;
    //Bei einer FPS darf das Spiel maximal 1000/60 ms benötigen um berechnungen der Loop zu schaffen
    public static final long maxLoopTime = 1000 / FPS;
    //Das Spiel hat eine Breite von 10 Tiles (10*64 = 640)
    public static final int SCREEN_WIDTH = 640;
    //Das Spiel hat erstmal eine Höhe von 10 Tiles und einem Platz für u.a. Leben und Punktzahl (10*64 + 100 = 740)
    public static final int SCREEN_HEIGHT = 740;
    
    public static void main(String[] arg) 
    {
        Game game = new Game();
        new Screen("LINK", SCREEN_WIDTH, SCREEN_HEIGHT);
        new Thread(game).start();
    }
    public boolean running = true;
  
    public void run() 
    {
        long timestamp;
        long oldTimestamp;
        while(running) 
        {
            oldTimestamp = System.currentTimeMillis();
            update();
            timestamp = System.currentTimeMillis();
            if(timestamp-oldTimestamp > maxLoopTime) 
            {
                System.out.println("Wir sind zu spät!");
                continue;
            }
            render();
            timestamp = System.currentTimeMillis();
            System.out.println(maxLoopTime + " : " + (timestamp-oldTimestamp));
            if(timestamp-oldTimestamp <= maxLoopTime) 
            {
                try 
                {
                    Thread.sleep(maxLoopTime - (timestamp-oldTimestamp) );
                } 
                catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
            }
        }
    }
    static void update() 
    {
    }
    void render() 
    { }
}
