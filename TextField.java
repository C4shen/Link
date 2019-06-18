import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
public class TextField
{
    private static final int PADDING_SIDES = 17;
    private static final int PADDING_BOTTOM = 20;
    private static final int DEFAULT_TILE_WIDH = 64;
    private static final int DEFAULT_TILE_HEIGHT = 64;
    private static final TileSet tileSet = new TileSet("/res/tilesets/textField-ts.png", 3 /*Anzahl Tiles x*/, 1/*Anzahl Tiles y*/, 3/*Abstand zwischen Tiles*/);
    private static final Color phColor = Color.LIGHT_GRAY;
    private static final Font phFont = new Font("Futura", Font.ITALIC, 30);
    private static final Color tColor = Color.BLACK;
    private static final Font tFont = new Font("Futura", Font.PLAIN, 30);
    private static final int DEFAULT_CURSOR_DELAY = 30;
    private static final int CURSOR_WIDTH = 2;
    private int xPosition;
    private int yPosition;
    private int anzahlTiles;
    private String placeholder;
    private StringBuilder text;
    private boolean lengthChanged;
    private int cursorDelay;
    private boolean focussed;
    public TextField(String ph, int x, int y, int width) {
        xPosition = x;
        yPosition = y;
        placeholder = ph;
        text = new StringBuilder();
        anzahlTiles = getTilesNumber(width);
    }
    
    public void recieveFocus() {
        focussed = true;
                cursorDelay = DEFAULT_CURSOR_DELAY;
    }

    public void loseFocus() {
        focussed = false;
    }
    
    public boolean isFocussed() {
        return focussed;
    }
    
    public static int getTilesNumber(int width) {
        int anzahlTiles = (int) width / DEFAULT_TILE_WIDH;
        if(width % DEFAULT_TILE_WIDH != 0) 
            anzahlTiles ++;
        return anzahlTiles;
    }
    
    public void setPlaceholder(String ph) {
        placeholder = ph;
    }
    
    public static int getActualWidth(int width) {
        int anzahlTiles = getTilesNumber(width);
        return anzahlTiles * DEFAULT_TILE_WIDH;
    }
    
    public void recieveInput(KeyManager m) {
        if(focussed) {
            lengthChanged = false;
            for(int i=0; i<256; i++) {
                if(m.generalKeyPressedOnce(i)) {
                    char c = (char) i;
                    switch(i) {
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_LEFT:
                        case KeyEvent.VK_RIGHT:
                        break;
                        case KeyEvent.VK_DELETE:  
                        case KeyEvent.VK_BACK_SPACE:
                            if(text.length() > 0) {
                                if(m.crtl()) 
                                    text.setLength(0);
                                else
                                    text.deleteCharAt(text.length()-1);
                                lengthChanged = true;
                            }
                        break;
                        case 86:
                            if(m.crtl()) {
                                String pastedText = Utils.pasteClipboard();
                                if(pastedText != null && !pastedText.isEmpty()) {
                                    text.append(pastedText);
                                    lengthChanged = true;
                                }
                                break;
                            }
                        default: 
                            if(Utils.isPrintableChar(c)) {
                                if(m.upperCase())
                                    c = Character.toUpperCase(c);
                                else 
                                    c = Character.toLowerCase(c);
                                text.append(c);
                                lengthChanged = true;
                            }
                    }
                }
            }
        }
    }
    
    public void render(Graphics g) {
        tileSet.renderTile(g, 0, xPosition, yPosition);
        for(int tileX = 1; tileX < anzahlTiles-1; tileX++){
            tileSet.renderTile(g, 1, xPosition + tileX * DEFAULT_TILE_WIDH, yPosition);
        }
        tileSet.renderTile(g, 2, xPosition + (anzahlTiles-1) * DEFAULT_TILE_WIDH, yPosition);
        
        if(text.length() == 0) {
            g.setColor(phColor);
            Utils.fontFestlegen(g, phFont);
            g.drawString(placeholder, xPosition+PADDING_SIDES+CURSOR_WIDTH, yPosition+DEFAULT_TILE_HEIGHT-PADDING_BOTTOM);
            renderCursor(g, 0);
        }
        else {
            Utils.fontFestlegen(g, tFont);
            int textWidth = Utils.textBreite(g, text.toString());
            int maxWidth = anzahlTiles*DEFAULT_TILE_WIDH - PADDING_SIDES*2;
            if(textWidth > maxWidth) {
                //Der Text wird in ein seperates Bild gemalt, damit ein Teil davon ausgeschnitten werden kann
                BufferedImage textImg = new BufferedImage(textWidth, DEFAULT_TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
                Graphics gImg = textImg.createGraphics();
                gImg.setColor(tColor);
                Utils.fontFestlegen(gImg, tFont); //Der Text wird in dem Bild um die überschüssige Länge nach links verschoben:
                gImg.drawString(text.toString(), maxWidth - textWidth, DEFAULT_TILE_HEIGHT-PADDING_BOTTOM);
                
                textImg = textImg.getSubimage(0, 0, textWidth, DEFAULT_TILE_HEIGHT); //Dann wird der Teil, der im Bild drinnen ist ausgeschnitten
                g.drawImage(textImg, xPosition+PADDING_SIDES, yPosition, null); //Und an die passende Stelle (mit Padding) gemalt.
                renderCursor(g, maxWidth);
            }
            else {
                g.setColor(tColor);
                g.drawString(text.toString(), xPosition+PADDING_SIDES, yPosition+DEFAULT_TILE_HEIGHT-PADDING_BOTTOM);
                renderCursor(g, textWidth);
            }
        }
    }
    
    private void renderCursor(Graphics g, int xPosCursor) {
        if(focussed && (lengthChanged || cursorDelay-- <= 0)) {
            g.setColor(Color.RED);
            g.fillRect(xPosition+PADDING_SIDES+xPosCursor, yPosition+PADDING_BOTTOM/2+5, CURSOR_WIDTH, DEFAULT_TILE_HEIGHT-PADDING_BOTTOM-10);
            if(cursorDelay <= -DEFAULT_CURSOR_DELAY)
                cursorDelay = DEFAULT_CURSOR_DELAY;
        }
    }
    
    public String getText() {
        return text.toString(); 
    }
    
    public void setText(String t) {
        empty();
        text.append(t);
    }
    
    public void empty() {
        text.setLength(0);
    }
}