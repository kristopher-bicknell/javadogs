import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class SimonMinigame extends Minigame{

    KeyHandler keyHandler;
    public boolean gameIsRun = false;
    private BufferedImage simonBoard, simonUpOn, simonUpOff, simonLeftOn, simonLeftOff, simonRightOn, simonRightOff, simonDownOn, simonDownOff;

    public SimonMinigame(KeyHandler keyHandler) {
        super(keyHandler);
        this.keyHandler = keyHandler;
        loadImages();
    }

    public void loadImages() {
        try {
            simonBoard = ImageIO.read(new File("src/minigames/simon/simonboard.png"));
            simonUpOff = ImageIO.read(new File("src/minigames/simon/simonup_off.png"));
            simonDownOff = ImageIO.read(new File("src/minigames/simon/simondown_off.png"));
            simonLeftOff = ImageIO.read(new File("src/minigames/simon/simonleft_off.png"));
            simonRightOff = ImageIO.read(new File("src/minigames/simon/simonright_off.png"));

            simonUpOn = ImageIO.read(new File("src/minigames/simon/simonup_on.png"));
            simonDownOn = ImageIO.read(new File("src/minigames/simon/simondown_on.png"));
            simonLeftOn = ImageIO.read(new File("src/minigames/simon/simonleft_on.png"));
            simonRightOn = ImageIO.read(new File("src/minigames/simon/simonright_on.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hold = false;
    private boolean advancedKeyPress(boolean isKeyPressed) {
        if (hold) {
            return false;
        }
        if (isKeyPressed) {
            hold = true;
            return true;
        }
        hold = false;
        return false;
    }

    @Override
    public BufferedImage getBackground() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/minigames/simonbackground.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private boolean isPlayerTurn = false;
    private boolean isCPUTurn = true;
    private boolean isCPUPlaying = false;
    private ArrayList<Integer> inputs = new ArrayList<>();
    private int currInputIndex = 0;
    public int score = 0;
    boolean gameOver = false;
    @Override
    public void update() {
        if (isCPUTurn) {
            int nextInteger = rng.nextInt(4);
            inputs.add(nextInteger);
            isCPUTurn = false;
            isCPUPlaying = true;
        }
        if (isPlayerTurn) {
            //listener for input
                if (advancedKeyPress(keyHandler.upPressed)) {
                    if (inputs.get(currInputIndex) == 0) {
                        score++;
                        currInputIndex++;
                    } else {
                        gameOver = true;
                    }
                }
                if (advancedKeyPress(keyHandler.downPressed)) {
                    if (inputs.get(currInputIndex) == 1) {
                        score++;
                        currInputIndex++;
                    } else {
                        gameOver = true;
                    }
                }
                if (advancedKeyPress(keyHandler.leftPressed.get())) {
                    if (inputs.get(currInputIndex) == 2) {
                        score++;
                        currInputIndex++;
                    } else {
                        gameOver = true;
                    }
                }
                if (advancedKeyPress(keyHandler.rightPressed.get())) {
                    if (inputs.get(currInputIndex) == 3) {
                        score++;
                        currInputIndex++;
                    } else {
                        gameOver = true;
                    }
                }
                if (currInputIndex == inputs.size() || gameOver) {
                    isPlayerTurn = false;
                    currInputIndex = 0;
                    if (!gameOver) {
                        isCPUTurn = true;
                    }
                }

        }
    }

    private int count = 30;
    private boolean isLight = true;

    @Override
    public void draw(Graphics2D g2d) {
        BufferedImage image = null;
        image = simonBoard;
        g2d.drawImage(image, 200,100,null);
        //draw buttons
        image = simonUpOff;
        g2d.drawImage(image, 200,100,null);
        image = simonLeftOff;
        g2d.drawImage(image, 200,100,null);
        image = simonRightOff;
        g2d.drawImage(image, 200,100,null);
        image = simonDownOff;
        g2d.drawImage(image, 200,100,null);

        if (isCPUPlaying) {
            if (isLight && inputs.size() > currInputIndex) {
                switch (inputs.get(currInputIndex)) {
                    case 0: //up
                        image = simonUpOn;
                        break;
                    case 1: //down
                        image = simonDownOn;
                        break;
                    case 2: //left
                        image = simonLeftOn;
                        break;
                    case 3: //right
                        image = simonRightOn;
                        break;
                }
                g2d.drawImage(image, 200, 100, null);
                if (count == 0) {
                currInputIndex++;
                count = 30;
                }
            }
            if (count > 0) {
                count--;
            }
            if (inputs.size() <= currInputIndex) {
                currInputIndex = 0;
                isCPUPlaying = false;
                isPlayerTurn = true;
                count = 0;
            }
        }
        //player turn
        if (isPlayerTurn) {
            //listener for input
            if (keyHandler.upPressed) {
                image = simonUpOn;
                g2d.drawImage(image, 200, 100, null);
            }
            if (keyHandler.downPressed) {
                image = simonDownOn;
                g2d.drawImage(image, 200, 100, null);
            }
            if (keyHandler.rightPressed.get()) {
                image = simonRightOn;
                g2d.drawImage(image, 200, 100, null);
            }
            if (keyHandler.leftPressed.get()) {
                image = simonLeftOn;
                g2d.drawImage(image, 200, 100, null);
            }
        }

        //ui
        g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        g2d.drawString("Score: " + score, 55,540);

        //game over
        if (gameOver) {
            g2d.drawString("Game over!", 200,100);
        }
    }

    @Override
    public void reset() {

    }
}
