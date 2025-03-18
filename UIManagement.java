import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;

public class UIManagement {
    GamePanel gp;
    Javadog dog;
    KeyHandler keyHandler;
    MotiveCutscenes cw;
    Decoration decoration;

    BufferedImage statusFrame, statusBar;
    BufferedImage button1, button2, buttonHungry, buttonClean, buttonSleep, buttonFun, buttonActivity, buttonDecor,
            buttonBack, buttonFlashcard, buttonJumprope, buttonMemory;

    private int buttonMoveCounter = 10;
    public static int currButton = 0;

    public UIManagement(GamePanel gp, Javadog dog, MotiveCutscenes cw, KeyHandler keyHandler, Decoration decoration) {
        this.gp = gp;
        this.dog = dog;
        this.cw = cw;
        this.keyHandler = keyHandler;
        this.decoration = decoration;

        getUI();
    }

    public void getUI() {
        try {
            statusFrame = ImageIO.read(new File("src/uimanagement/statusframe.png"));
            statusBar = ImageIO.read(new File("src/uimanagement/statusbar.png"));

            button1 = ImageIO.read(new File("src/uimanagement/button1.png"));
            button2 = ImageIO.read(new File("src/uimanagement/button2.png"));
            buttonHungry = ImageIO.read(new File("src/uimanagement/buttonhunger.png"));
            buttonClean = ImageIO.read(new File("src/uimanagement/buttonclean.png"));
            buttonSleep = ImageIO.read(new File("src/uimanagement/buttonsleep.png"));
            buttonFun = ImageIO.read(new File("src/uimanagement/buttonfun.png"));
            buttonActivity = ImageIO.read(new File("src/uimanagement/buttonactivity.png"));
            buttonDecor = ImageIO.read(new File("src/uimanagement/buttondecor.png"));
            buttonBack = ImageIO.read(new File("src/uimanagement/buttonback.png"));
            buttonFlashcard = ImageIO.read(new File("src/uimanagement/buttonflashcard.png"));
            buttonJumprope = ImageIO.read(new File("src/uimanagement/buttonjumprope.png"));
            buttonMemory = ImageIO.read(new File("src/uimanagement/buttonmemory.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isActivityWindow = false;

    public void update() {
        switch (gp.gameState) {
            case 0: //normal gameplay
                if (!GamePanel.isTutorial) {
                    if (!isActivityWindow) {
                        if (keyHandler.leftPressed.get() && buttonMoveCounter == 0) {
                            if (currButton == 0) {
                                currButton = 4;
                            } else {
                                currButton--;
                            }
                            buttonMoveCounter = 4;
                        } else if (keyHandler.rightPressed.get() && buttonMoveCounter == 0) {
                            if (currButton == 4) {
                                currButton = 0;
                            } else {
                                currButton++;
                            }
                            buttonMoveCounter = 4;
                        } else {
                            if (buttonMoveCounter > 0) {
                                buttonMoveCounter--;
                            }
                        }

                        //select button?
                        if (keyHandler.buttonPressed && buttonMoveCounter == 0) {
                            buttonMoveCounter = 5;
                            if (currButton < 3) {
                                //cutscenes
                                gp.gameState = 1;
                            }
                            if (currButton == 3) {
                                //activity window
                                if (Javadog.canComplete()) {
                                    currButton = 0;
                                    isActivityWindow = true;
                                } else {
                                    gp.gameState = 1;
                                }
                            }
                            if (currButton == 4) {
                                currButton = 0;
                                gp.gameState = 3;
                            }
                        }
                    } else {
                        if (keyHandler.leftPressed.get() && buttonMoveCounter == 0) {
                            if (currButton == 0) {
                                currButton = 3;
                            } else {
                                currButton--;
                            }
                            buttonMoveCounter = 5;
                        } else if (keyHandler.rightPressed.get() && buttonMoveCounter == 0) {
                            if (currButton == 3) {
                                currButton = 0;
                            } else {
                                currButton++;
                            }
                            buttonMoveCounter = 5;
                        } else {
                            if (buttonMoveCounter > 0) {
                                buttonMoveCounter--;
                            }
                        }
                        if (keyHandler.buttonPressed && buttonMoveCounter == 0) {
                            buttonMoveCounter = 5;
                            if (currButton < 3) {
                                gp.gameState = 2;
                            }
                            switch (currButton) {
                                case 0: //flashcard
                                    FlashcardMinigame.gameIsRun = true;
                                    break;
                                case 1: //jumprope
                                    JumpRope.gameIsRun = true;
                                    break;
                                default: //memory
                                    MemoryMinigame.gameIsRun = true;
                                    break;
                            }
                            isActivityWindow = false;
                            currButton = 3;
                        }
                    }
                }
                break;
            case 3:
                if (keyHandler.leftPressed.get() && buttonMoveCounter == 0) {
                    if (currButton == 0) {
                        currButton = 5;
                    } else {
                        currButton--;
                    }
                    buttonMoveCounter = 5;
                } else if (keyHandler.rightPressed.get() && buttonMoveCounter == 0) {
                    if (currButton == 5) {
                        currButton = 0;
                    } else {
                        currButton++;
                    }
                    buttonMoveCounter = 5;
                } else {
                    if (buttonMoveCounter > 0) {
                        buttonMoveCounter--;
                    }
                }
                if (keyHandler.buttonPressed && buttonMoveCounter == 0) {
                    buttonMoveCounter = 5;
                    if (currButton == 5) { //exit
                        currButton = 4;
                        gp.gameState = 0;
                    } else {
                        decoration.updateDecoration(currButton);
                    }
                }
                break;
        }
    }

    private Color getBarColor(double stat) {
        double r = 10;
        double g = 200;
        if (stat >= 0.5) {
            r = 390 - (380 * stat);
            g = 200;
        } else {
            r = 200;
            g = (380 * stat) + 10;
        }
         return new Color((int)r, (int)g, 10);
    }

    public void draw(Graphics2D g2d) {
        BufferedImage image = null;
        int statusBarWidth;

        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));

        switch (gp.gameState) {
            case 0:
                if (!isActivityWindow) {
                    //bar labels
                    g2d.drawString("Hunger", 0, 30);
                    g2d.drawString("Energy", 0, 60);
                    g2d.drawString("Hygeine", 175, 30);
                    g2d.drawString("Happiness", 175, 60);
                    //TODO: change color of bar
                    //TODO: make the bars less shit
                    g2d.setColor(getBarColor(Javadog.hunger));
                    statusBarWidth = (int) (100 * dog.hunger) - 13;
                    g2d.fillRect(79, 15, statusBarWidth, 22);
                    g2d.setColor(getBarColor(Javadog.energy));
                    statusBarWidth = (int) (100 * dog.energy) - 13;
                    g2d.fillRect(79, 46, statusBarWidth, 22);
                    g2d.setColor(getBarColor(Javadog.clean));
                    statusBarWidth = (int) (100 * dog.clean) - 13;
                    g2d.fillRect(279, 15, statusBarWidth, 22);
                    g2d.setColor(getBarColor(Javadog.happiness));
                    statusBarWidth = (int) (100 * dog.happiness) - 13;
                    g2d.fillRect(279, 46, statusBarWidth, 22);

                    image = statusFrame;
                    g2d.drawImage(image, 70, 0, null);
                    g2d.drawImage(image, 70, 30, null);
                    g2d.drawImage(image, 270, 0, null);
                    g2d.drawImage(image, 270, 30, null);

                    g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
                    g2d.setColor(Color.black);
                    g2d.drawString(gp.totalScore + " / 100 points", 400, 45);

                    //Buttons
                    for (int i = 0; i < 5; i++) {
                        if (currButton == i) {
                            image = button2;
                        } else {
                            image = button1;
                        }
                        g2d.drawImage(image, (i * 120), 500, null);
                        switch (i) {
                            case 0:
                                image = buttonHungry;
                                break;
                            case 1:
                                image = buttonClean;
                                break;
                            case 2:
                                image = buttonSleep;
                                break;
                            case 3:
                                image = buttonActivity;
                                break;
                            case 4:
                                image = buttonDecor;
                                break;
                        }
                        g2d.drawImage(image, (i * 120), 500, null);
                    }
                } else {
                    g2d.setColor(new Color(0,0,0,180));
                    g2d.fillRoundRect(50,50,700,500,35,35);
                    g2d.setColor(Color.white);
                    g2d.setStroke(new BasicStroke(5));
                    g2d.drawRoundRect(50,50,700,500,35,35);

                    //title
                    g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
                    int drawX = 400-(int)(0.5*g2d.getFontMetrics().getStringBounds("Minigames",g2d).getWidth());
                    g2d.drawString("Minigames", drawX, 150);
                    drawX = 185;
                    for (int i = 0; i < 4; i++) {
                        if (currButton == i) {
                            image = button2;
                        } else {
                            image = button1;
                        }
                        g2d.drawImage(image, drawX, 225, null);
                        switch (i) {
                            case 0: //flashcard
                                image = buttonFlashcard;
                                break;
                            case 1: //jump rope
                                image = buttonJumprope;
                                break;
                            case 2: //memory
                                image = buttonMemory;
                                break;
                            default: //go back
                                image = buttonBack;
                                break;
                        }
                        g2d.drawImage(image, drawX, 225, null);
                        drawX += 100;
                    }
                    String toPrint = "";
                    switch (currButton) {
                        case 0: //flashcard
                            toPrint = "Math Flashcards";
                            break;
                        case 1: //jump rope
                            toPrint = "Jump Rope";
                            break;
                        case 2: //memory
                            toPrint = "Memory";
                            break;
                        default: //go back
                            toPrint = "Go back";
                            break;
                    }
                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
                    drawX = 400-(int)(0.5*g2d.getFontMetrics().getStringBounds(toPrint,g2d).getWidth());
                    g2d.drawString(toPrint, drawX, 375);
                }
                break;
            case 3: //decoration window
                for (int i = 0; i < 6; i++) {
                    if (currButton == i) {
                        image = button2;
                    } else {
                        image = button1;
                    }
                    g2d.drawImage(image, (i*120), 500, null);
                    int height = 50;
                    int y = 500+25;
                    switch (i) {
                        case 0: //wall
                            image = decoration.wall[decoration.currDecor[0]];
                            break;
                        case 1: //floor
                            image = decoration.floor[decoration.currDecor[1]];
                            break;
                        case 2: //window
                            image = decoration.window[decoration.currDecor[2]];
                            break;
                        case 3: //wall decor
                            image = decoration.wallDecor[decoration.currDecor[3]];
                            break;
                        case 4: //floor decor
                            image = decoration.floorDecor[decoration.currDecor[4]];
                            y -= 10;
                            height = 75;
                            break;
                        default:
                            image = buttonBack;
                            break;
                    }
                    g2d.drawImage(image,(i*120)+25, y, 50, height, null);
                }
                String toPrint = "Redecorating:";
                g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
                g2d.setColor(Color.black);
                int drawX = 400-(int)(0.5*g2d.getFontMetrics().getStringBounds(toPrint,g2d).getWidth());
                g2d.drawString(toPrint, drawX, 50);
                switch (currButton) {
                    case 0: //wall
                        toPrint = "Wall";
                        break;
                    case 1: //jump rope
                        toPrint = "Floor";
                        break;
                    case 2: //memory
                        toPrint = "Window";
                        break;
                    case 3: //go back
                        toPrint = "Wall Decor";
                        break;
                    case 4:
                        toPrint = "Floor Decor";
                        break;
                    default:
                        toPrint = "Go back";
                        break;
                }
                drawX = 400-(int)(0.5*g2d.getFontMetrics().getStringBounds(toPrint,g2d).getWidth());
                g2d.drawString(toPrint, drawX, 75);
                break;
        }

    }
}
