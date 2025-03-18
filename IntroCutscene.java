import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class IntroCutscene {
    GamePanel gp;
    KeyHandler keyHandler;
    public IntroCutscene(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;
        getImages();
    }

    String[] dialogStrings = {
            "You're watching TV one morning. The news is tuned to a national \n" +
                    "press conference for Dog Corp, the nation's leader in dog \n" +
                    "technology.",
            "They are unveiling their newest prototype: \n" +
                    "#!red Javadogs#, a more advanced dog.",
            "Alongside this reveal is a second piece of news: Dog Corp is \n" +
                    "running a sweepstakes for their new Javadogs.\n" +
                    "You lean forward, knocking the spoon out of your bowl \n" +
                    "of Flavor-O's.",
            "#!blue \"Viewers at home, keep an eye out in your breakfast! A select \n" +
                    "#!blue few cereal-eaters may find a lucky gold pass in their \n#" +
                    "#!blue morning bowl!\"#",
            "As you scramble for your spoon, you catch a glimpse of something \n" +
                    "shiny floating in your breakfast.\n",
            "#!blue \"Winners should send this ticket to our headquarters with a \n" +
                    "#!blue return address to receive their very own Javadog! \n#" +
                    "#!blue Terms apply, postage not included.\"#",
            "You excitedly rush to the post office and mail your gold pass, \n" +
                    "postage included. Then you wait.",
            "A week later, you get a big package in the mail. \n" +
                    "Inside is your brand-new Javadog, #!green Marvin#."
    };

    BufferedImage[] cutsceneImages = new BufferedImage[dialogStrings.length];
    static BufferedImage titleScreen;

    private int currScene = 0;

    private void getImages() {
        try {
            for (int i = 0; i < dialogStrings.length; i++) { //should end at i = 6
                cutsceneImages[i] = ImageIO.read(new File("src/cutscenes/intro/scene" + (i+1) + ".png"));
            }
            titleScreen = ImageIO.read(new File("src/cutscenes/title.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int enterCounter = 10;

    public void update() {
        if (keyHandler.enterPressed && enterCounter == 0) {
            if (currScene == dialogStrings.length - 1) {
                gp.gameState = 0;
            } else {
                enterCounter = 10;
                currScene++;
            }
        } else if (enterCounter > 0) {
            enterCounter--;
        }
    }

    public void draw(Graphics2D g2d) {
        BufferedImage image = null;
        image = cutsceneImages[currScene];
        g2d.drawImage(image, 0,0,null);
        DialogDraw.drawDialogBox(g2d, dialogStrings[currScene]);
    }
}
