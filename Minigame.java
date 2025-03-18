import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public abstract class Minigame {

    public static Random rng = new Random();

    public Minigame(KeyHandler keyHandler) {
    }

    public void drawMinigameBox(Graphics2D g2d) {
        g2d.drawImage(getBackground(), 50,50,null);
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRect(50,50,700,500);
    }

    /**
     * Start text at y = 125, end at y = 500
     * @param g2d
     */
    public static void drawTutorialBox(Graphics2D g2d) {
        g2d.setColor(new Color(0,0,0,180)); //same as dialog box
        g2d.fillRoundRect(50,50,700,500, 10,10);
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(50,50,700,500,10,10);
        //text
        g2d.setColor(Color.white);
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        int xPrint = 400-((int)g2d.getFontMetrics().getStringBounds("How to Play:",g2d).getWidth())/2;
        g2d.drawString("How to Play:" , xPrint, 100);

        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        xPrint = 400-(int)(0.5*g2d.getFontMetrics().getStringBounds("(Enter)",g2d).getWidth());
        g2d.setColor(new Color(200,200,200));
        g2d.drawString("(Enter)", xPrint, 525);
    }

    public abstract BufferedImage getBackground();

    public abstract void update();

    public abstract void draw(Graphics2D g2d);

    public abstract void reset();
}
