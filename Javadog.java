import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Javadog {
    final static double MAX_HUNGER = 1.0; // Lower levels means lower motives; numbers are percentages
    final static double MAX_CLEAN = 1.0;
    final static double MAX_ENERGY = 1.0;
    final static double MAX_HAPPINESS = 1.0;

    final int LOCATION_X = 400;
    final int LOCATION_Y = 200;

    GamePanel gp;
    KeyHandler keyHandler;

    double hunger_decay;
    double clean_decay;
    double energy_decay;
    double happiness_decay;

    //TODO: Make these make sense
    static double hunger;
    static double clean;
    static double energy;
    static double happiness;

    public BufferedImage moodContent, moodDirty, moodHungry, moodSad, moodTired;
    public BufferedImage dog1, dog2;

    //Dog animation variables
    boolean dogFrame = true; //true = dog1, false = dog2
    int dogAnimCounter = 0;

    public Javadog(){

    }

    public Javadog(GamePanel gp, KeyHandler keyHandler){
        hunger = MAX_HUNGER;
        clean = MAX_CLEAN;
        energy = MAX_ENERGY;
        happiness = MAX_HAPPINESS;

        hunger_decay = 0.0010;
        clean_decay = 0.0005;
        energy_decay = 0.0007;
        happiness_decay = (1-hunger) + (1-clean) + (1-energy);

        this.gp = gp;
        this.keyHandler = keyHandler;

        getMoodImage();
        getDogImage();
    }

    /**
     * Decreases motives on an interval
     */
    public void decayMotives() {
        if (gp.gameState != 2) {
            hunger_decay = 0.0010;
            clean_decay = 0.0005;
            energy_decay = 0.0007;
            happiness_decay = (1 - ((hunger + clean + energy) / 3)) * 0.001;
        } else {
            hunger_decay = 0.00025;
            clean_decay = 0.00005;
            energy_decay = 0.00007;
        }
        hunger -= hunger_decay;
        if (hunger <= 0) {
            GamePanel.starvationCountdown--;
        } else {
            GamePanel.starvationCountdown = 60;
        }
        clean -= clean_decay;
        energy -= energy_decay;
        if (GamePanel.gameState != 2) {
            happiness -= happiness_decay;
        }

        hunger = Math.clamp(hunger, 0,1);
        clean = Math.clamp(clean, 0,1);
        energy = Math.clamp(energy, 0,1);
        happiness = Math.clamp(happiness, 0,1);
    }

    public void getMoodImage() {
        try {
             moodContent = ImageIO.read(new File("src/javadog/content.png"));
             moodDirty = ImageIO.read(new File("src/javadog/dirty.png"));
             moodHungry = ImageIO.read(new File("src/javadog/hungry.png"));
             moodSad = ImageIO.read(new File("src/javadog/sad.png"));
             moodTired = ImageIO.read(new File("src/javadog/tired.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDogImage() {
        try {
            dog1 = ImageIO.read(new File("src/javadog/dogbig.png"));
            dog2 = ImageIO.read(new File("src/javadog/dogbig2.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getEmotionalState() {
        if (hunger <= 0.25) {
            return 1;
        } else if (energy <= 0.25) {
            return 2;
        } else if (clean <= 0.20) {
            return 3;
        } else if (happiness <= 0.1) {
            return 4;
        }
        return 0;
    }

    public static boolean canComplete() {
        switch (UIManagement.currButton) {
            case 0: //hunger
                if (happiness < 0.1 || hunger > 0.5) {
                    return false;
                }
                break;
            case 1: //hygeine
                if (happiness < 0.1 || hunger < 0.25 || clean > 0.5) {
                    return false;
                }
                break;
            case 2: //energy
                if (happiness < 0.1 || hunger < 0.25 || clean < 0.25 || energy > 0.5)
                {
                    return false;
                }
                break;
            case 3: //gamin'
                if (hunger < 0.25 || clean < 0.25 || energy < 0.25) {
                    return false;
                }
        }
        return true;
    }

    /*emotional states:
    0 = content
    1 = hungry
    2 = tired
    3 = dirty
    4 = sad
     */

    public void update(UIManagement ui) {
        decayMotives();

    }

    public static void resetMotive() {
        if (GamePanel.gameState == 2) {
            happiness = MAX_HAPPINESS;
        } else
            if (canComplete()) {
                switch (UIManagement.currButton) {
                    case 0:
                        hunger = MAX_HUNGER;
                        break;
                    case 1:
                        clean = MAX_CLEAN;
                        break;
                    case 2:
                        energy = MAX_ENERGY;
                        break;
                }
            }
        }

    public void draw(Graphics2D g2d) {
        BufferedImage image = null;

        //draw dog
        if (dogFrame) {
            image = dog1;

        } else {
            image = dog2;
        }
        dogAnimCounter++;
        if (dogAnimCounter == 10) {
            dogFrame = !dogFrame; //flip to other frame
            dogAnimCounter = 0;
        }
        g2d.drawImage(image, gp.getWidth()/2-(400/2), gp.getHeight()/2-(400/2)+65, 400, 400, null);

        switch (getEmotionalState()) {
            case 0:
                //content
                image = moodContent;
                break;
            case 1:
                //hungry
                image = moodHungry;
                break;
            case 2:
                //tired
                image = moodTired;
                break;
            case 3:
                //dirty
                image = moodDirty;
                break;
            case 4:
                //sad
                image = moodSad;
                break;
        }
        g2d.drawImage(image, LOCATION_X, LOCATION_Y, 200, 200, null);
    }
}
