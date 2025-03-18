import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class EndingCutscene {
    GamePanel gp;
    KeyHandler keyHandler;
    public EndingCutscene(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;
        getImages();
    }

    String[] badEndingDialog = {
            "Oh dear.",
            "In your pursuit of grand winnings, it seems that you forgot the\n" +
                    "most vital part of life: #!red food#.",
            "Maybe you were selfish. Maybe you thought you'd get a huge cash\n" +
                    "prize and be able to quit your job. Or maybe you thought,\n" +
                    "#!red \"A game this cute couldn't possibly let you kill a dog,#\n" +
                    "#!red right?\"#",
            "Who knows, really? Isn't our world just a never-ending cycle of \n" +
                    "life and death? And, in that context, could you consider \n" +
                    "your actions to be merciful?",
            "Either way, what's done is done.",
            "At least he was free."
    };

    String[] goodEndingDialog = {
            "Dog Inc. is impressed with your determination.",
            "Honestly, they thought you'd fail. But you didn't.",
            "You beat the odds and perservered.",
            "Because of your amazing performance, Dog Corp has given you and\n" +
                    "Marvin free tickets to see their new hit hip-hop idol, \n" +
                    "#!blue The Notorious D.O.G.#",
            "Doggie Doggie Doggie, can't you see? \n" +
                    "Sometimes your words just hypnotize me."
    };

    String[] secretEndingDialog = {
            "You really outdid yourself this time!",
            "Not only did you reach the goal, but you did it with such speed, \n" +
                    "such voracity. You didn't let anything stand in your way.",
            "The #!blue CEO# of Dog Corp has recognized your efforts, and he \n" +
                    "is so stunned that he tragically dies of a heart attack.",
            "With their CEO gone, Dog Corp finally decides what your reward \n" +
                    "should be. \n" +
                    "#!blue You are crowned the new CEO of Dog Corp.#",
            "Enjoy your massive salary and bi-monthly beachside vacations."
    };

    BufferedImage badCutsceneImage, goodCutsceneImage, secretCutsceneImage, credits, theEnd;

    private int currScene = 0;

    private void getImages() {
        try {
            badCutsceneImage = ImageIO.read(new File("src/cutscenes/ending/bad.png"));
            goodCutsceneImage = ImageIO.read(new File("src/cutscenes/ending/good.png"));
            secretCutsceneImage = ImageIO.read(new File("src/cutscenes/ending/secret.png"));
            credits = ImageIO.read(new File("src/cutscenes/ending/credits.png"));
            theEnd = ImageIO.read(new File("src/cutscenes/ending/theend.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int enterCounter = 0;
    private boolean finale = false;
    int coordY = 600;
    int endCoordY = 600+1300;

    private String[] getDialog() {
        switch (GamePanel.gameState) {
            case -1: //bad
                return badEndingDialog;
            case 100: //good
                return goodEndingDialog;
            default: //secret
                return secretEndingDialog;
        }
    }

    public void update() {
        if (!finale) {
            int compareTo = getDialog().length - 1;
            if (keyHandler.enterPressed && enterCounter == 0) {
                if (currScene == compareTo) {
                    finale = true;
                } else {
                    enterCounter = 10;
                    currScene++;
                }
            } else if (enterCounter > 0) {
                enterCounter--;
            }
        } else {
            if (endCoordY > 0) {
                coordY--;
                endCoordY--;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        BufferedImage image = null;
        switch (GamePanel.gameState) {
            case -1: //bad
                image = badCutsceneImage;
                break;
            case 100: //good
                image = goodCutsceneImage;
                break;
            case 999: //secret
                image = secretCutsceneImage;
                break;
        }
        g2d.drawImage(image, 0,0,null);
        if (!finale) {
            DialogDraw.drawDialogBox(g2d, getDialog()[currScene]);
        } else {
            image = credits;
            g2d.drawImage(image, 0, coordY, null);
            image = theEnd;
            g2d.drawImage(image, 0, endCoordY, null);
        }
    }
}
