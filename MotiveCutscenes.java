import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class MotiveCutscenes{
    KeyHandler keyHandler;
    GamePanel gp;
    private String[] dialogStrings = {
            "Marvin enjoys a delicious meal.\n#!green HP# reset to full.",
            "Marvin takes a refreshing hot shower.\n#!green Hygeine# reset to full.",
            "Marvin sleeps peacefully through the night.\n#!red Energy# reset to full.",
            "Marvin runs around outside.\n#!blue Happiness# reset to full."
    };

    private String[] cannotCompleteStrings = {
            "Marvin is too bored. Try completing a minigame first!",
            "Marvin isn't hungry!",
            "Marvin isn't dirty!",
            "Marvin isn't tired!",
            //another motive is too low
            "Marvin is too #!red hungry# to shower! \nTry feeding him first.",
            "Marvin is too #!red hungry# to sleep! \nTry feeding him first.",
            "Marvin is too #!red dirty# to sleep! \nTry bathing him first.",
            //too low to play
            "Marvin is too #!red hungry# to play! \nTry feeding him first.",
            "Marvin is too #!red dirty# to play! \nTry bathing him first.",
            "Marvin is too #!red tired# to play! \nTry letting him sleep first."
    };

    BufferedImage displayImage;
    BufferedImage cutsceneHunger, cutsceneTired, cutsceneClean, cutsceneFun, cutsceneDeath;
    private int count = 10;

    public MotiveCutscenes(GamePanel gp, KeyHandler keyH) {
        super();
        this.gp = gp;
        keyHandler = keyH;

        getCutscenes();
    }

    private String getCutsceneText() {
        if (Javadog.canComplete()) {
            return dialogStrings[UIManagement.currButton];
        } else {
            if (Javadog.happiness < 0.1) {
                return cannotCompleteStrings[0];
            }
            switch (UIManagement.currButton) {
                case 0: //why can't you eat?
                    return cannotCompleteStrings[1];
                case 1: //why can't you bathe?
                    if (Javadog.hunger < 0.25) {
                        return cannotCompleteStrings[4];
                    }
                    return cannotCompleteStrings[2];
                case 2: //why can't you sleep?
                    if (Javadog.hunger < 0.25) {
                        return cannotCompleteStrings[5];
                    } else if (Javadog.clean < 0.25) {
                        return cannotCompleteStrings[6];
                    }
                    return cannotCompleteStrings[3];
                case 3: //why can't you play?
                    if (Javadog.hunger < 0.25) {
                        return cannotCompleteStrings[7];
                    } else if (Javadog.clean < 0.25) {
                        return cannotCompleteStrings[8];
                    } else if (Javadog.energy < 0.25) {
                        return cannotCompleteStrings[9];
                    }
            }
        }
        return "Error";
    }

    public void getCutscenes() {
        try {
            cutsceneHunger = ImageIO.read(new File("src/cutscenes/dogeat.png"));
            cutsceneTired = ImageIO.read(new File("src/cutscenes/dogsleeproom.png"));
            cutsceneClean = ImageIO.read(new File("src/cutscenes/dogbathe.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getCurrCutscene() {
        switch (UIManagement.currButton) {
            case 0:
                return cutsceneHunger;
            case 1:
                return cutsceneClean;
            case 2:
                return cutsceneTired;
            case 3:
                return cutsceneFun;
            case 4:
                return cutsceneDeath;
        }
        return cutsceneDeath;
    }

    public void update() {
        if (GamePanel.gameState == 1) {
            if (keyHandler.enterPressed && count == 0) {
                GamePanel.gameState = 0;
                Javadog.resetMotive();
                count = 10;
            } else if (count > 0) {
                count--;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        if (Javadog.canComplete()) {
            displayImage = getCurrCutscene();
            g2d.drawImage(displayImage, 50, 50, 700, 500, null);
            g2d.setColor(Color.black);
            g2d.setStroke(new BasicStroke(5));
            g2d.drawRect(50, 50, 700, 500);
        }
        DialogDraw.drawDialogBox(g2d, getCutsceneText());
    }
}
