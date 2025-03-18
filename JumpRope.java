import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class JumpRope extends Minigame{
    KeyHandler keyHandler;
    public static boolean gameIsRun = false;
    private boolean isTutorial = true;

    //images
    BufferedImage[] jumpRopeFrames = new BufferedImage[8];
    BufferedImage dog;

    public JumpRope(KeyHandler keyHandler) {
        super(keyHandler);
        this.keyHandler = keyHandler;
        loadImages();
    }


    @Override
    public BufferedImage getBackground() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/minigames/jumpropebackground.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public void loadImages() {
        try {
            //load jumprope frames
            for (int i = 0; i < jumpRopeFrames.length; i++) {
                jumpRopeFrames[i] = ImageIO.read(new File("src/minigames/jumprope/rope" + (i+1) + ".png"));
            }
            dog = ImageIO.read(new File("src/javadog/dogbig.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int score = 0;
    int jumpRopeY = 150;
    boolean jumpRopeIsBehind = false;

    int dogY = 370;

    private int getSpeed() {
        return 4 + score;
    }

    private void checkScore() {
        if (dogIsMoving && jumpRopeY >= 450) {
            score++;
        } else if (!dogIsMoving && jumpRopeY >= 450) {
            gameOver = true;
        }
    }

    private int getJumpRopeFrame() {
        if (!jumpRopeIsBehind) { //frames 1-4
            if (jumpRopeY < 225) {
                return 0;
            } else if (jumpRopeY >=225 && jumpRopeY < 300) {
                return 1;
            } else if (jumpRopeY >= 300 && jumpRopeY < 375) {
                return 2;
            } else {
                return 3;
            }
        } else { //frames 5-8
            if (jumpRopeY < 225) {
                return 7;
            } else if (jumpRopeY >=225 && jumpRopeY < 300) {
                return 6;
            } else if (jumpRopeY >= 300 && jumpRopeY < 375) {
                return 5;
            } else {
                return 4;
            }
        }
    }

    boolean dogIsMoving = false;
    boolean dogIsGoingDown = false;
    boolean gameOver = false;

    int count = 10;

    @Override
    public void update() {
        if (!isTutorial && !gameOver) {
            if (!jumpRopeIsBehind) { //moving down
                jumpRopeY += getSpeed();
                if (jumpRopeY >= 450) {
                    checkScore();
                    jumpRopeIsBehind = true;
                }
            } else { //moving up
                jumpRopeY -= getSpeed();
                if (jumpRopeY <= 150) {
                    jumpRopeIsBehind = false;
                }
            }
            if (keyHandler.spacePressed && !dogIsMoving) {
                dogIsMoving = true;
            }

            //move dog
            if (dogIsMoving) {
                if (!dogIsGoingDown) {//move up
                    dogY -= getSpeed() * 1.5;
                    if (dogY <= 300) {
                        dogIsGoingDown = true;
                    }
                } else { //move down
                    dogY += getSpeed()*1.5;
                    if (dogY >= 370) {
                        dogY = 370; //fix extra movement
                        dogIsGoingDown = false;
                        dogIsMoving = false;
                    }
                }
            }
        } else if (keyHandler.enterPressed && count == 0){ //etc screens
            if (gameOver) {
                gameIsRun = false;
            } else {
                isTutorial = false;
            }
        } else if (count > 0) {
            count--;
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        BufferedImage image = null;
        if (isTutorial) {
            Minigame.drawTutorialBox(g2d);
            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
            g2d.setColor(Color.white);
            String toPrint = "";
            toPrint = "Use the spacebar to jump over the jump rope.";
            g2d.drawString(toPrint , 100, 150);
            toPrint = "The rope gets faster the longer you play, so try not";
            g2d.drawString(toPrint , 100, 200);
            toPrint = "to trip!";
            g2d.drawString(toPrint , 100, 250);
        } else {
            if (jumpRopeIsBehind) { //draw it first
                image = jumpRopeFrames[getJumpRopeFrame()];
                g2d.drawImage(image, 52, jumpRopeY, 696, 50, null);
            }
            image = dog;
            g2d.drawImage(image,320,dogY,140,140,null);
            if (!jumpRopeIsBehind) { //draw it second
                image = jumpRopeFrames[getJumpRopeFrame()];
                g2d.drawImage(image, 52, jumpRopeY, 696, 50, null);
            }

            g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            g2d.drawString("Score: " + score, 55,540);

            //game over
            if (gameOver) {
                g2d.drawString("Game over!", 200,100);
            }
        }
    }

    @Override
    public void reset() {
        score = 0;
        jumpRopeY = 150;
        dogY = 370;
        gameOver = false;
        isTutorial = true;
        count = 10;
    }
}
