import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
/**
 * Der HighscoresState ist der State, in dem sich das Spiel befindet, wenn die Highscores angezeigt werden. 
 * Dann werden die Highscores vom HighscoreManager abgefragt und angezeigt.
 */
public class HighscoresState extends State {
    private HighScoreManager scoreManager;
    private Font basisFont; 
    public HighscoresState(HighScoreManager sManager) {
        scoreManager = sManager;
        basisFont = new Font("Futura", Font.BOLD, 40);
    }
    
    /*
     * @author Jakob Kleine, Janni Röbbecke, Cashen Adkins
     * @since 26.05.2019
     */
    public void update(KeyManager keyManager) {
        if(keyManager.escapeEinmal()) //Durch Drücken von ESC / BACKSPACE wird zum Hauptmenü zurückgekehrt
            setState(StateNames.MENU);
    }
    
    /*
     * @author Jakob Kleine, Janni Röbbecke
     * @since 26.05.2019
     */
    public void render(Graphics g) {
        g.drawImage(MainMenuState.menuBackground, 0, 0, null);
        Utils.fontFestlegen(g,basisFont.deriveFont(Font.BOLD));
        g.setColor(new Color(65, 41, 31));
        
        Utils.centerText(g, "BESTENLISTE", Game.SCREEN_WIDTH, 100);
        
        g.setFont(basisFont.deriveFont(Font.PLAIN, 30));
        ArrayList<Score> scores = scoreManager.getScores();
        for(int i=0; i<scores.size(); i++) {
            /*
             * (i<9?"0":"") -> Wenn die Score-Nr. < 9 ist wird eine 0 vorher angefügt.
             * (i+1)+": "+scores.get(i) -> [ScoreNr]: [ScoreWert - ScoreName (ScoreDatum)]
             * (175+55*i) -> Der erste Score wird bei y=175 platziert, alle weiteren in einem Abstand von 55px
             */
            g.drawString((i<9?"0":"")+(i+1)+": "+scores.get(i), 40 /*x-Position*/, 175+55*i);
        }
       
        g.setColor(Color.white);
        g.setFont(basisFont.deriveFont(Font.ITALIC, 15)); 
        String hinweis = "Um zum Hauptmenü zurückzukehren bitte ESCAPE drücken.";
        Utils.centerText(g, hinweis, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT-15);
    }
    
    public HighScoreManager getScoresManager() {
        return scoreManager;
    }
    
    public void addScore(int scoreValue) {
        String playerName = ((OptionsState) getStates().get(StateNames.OPTIONS)).getOptionsManager().getPlayerName();
        scoreManager.addScore(new Score(scoreValue,playerName));
    }
}