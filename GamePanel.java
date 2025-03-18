import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    final int ORIGINAL_TILE_SIZE = 600;
    final double SCALE = 0.3;

    final int TILE_SIZE = (int)(ORIGINAL_TILE_SIZE * SCALE);
    final int MAX_SCREEN_COL = 6;
    final int MAX_SCREEN_ROW = 4;
    final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

    int FPS = 30;
    //TODO: set to 10
    public static int gameState = 11; //for the title scene, because i plan ahead
    //public static int gameState = 0;
    public int totalScore = 0;

    Thread gameThread;
    KeyHandler keyHandler = new KeyHandler(this);
    MotiveCutscenes motiveCutscenes = new MotiveCutscenes(this, keyHandler);
    Javadog javadog = new Javadog(this, keyHandler);
    Decoration decoration = new Decoration(this);
    UIManagement ui = new UIManagement(this, javadog, motiveCutscenes, keyHandler, decoration);
    FlashcardMinigame flashcardMinigame = new FlashcardMinigame(keyHandler);
    JumpRope jumpRope = new JumpRope(keyHandler);
    MemoryMinigame memoryMinigame = new MemoryMinigame(keyHandler);
    //SimonMinigame simonMinigame = new SimonMinigame(keyHandler);
    MainWindow mainWindow;
    IntroCutscene intro = new IntroCutscene(this, keyHandler);
    EndingCutscene ending = new EndingCutscene(this, keyHandler);

    public GamePanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.setPreferredSize(new Dimension(800,600));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    int speedrunClock = 0;
    int speedrunClockCount = 30;

    public int gameEnd() {
        if (speedrunClock <= 300) { //15 minutes
            return 999;
        } else {
            return 100;
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        this.requestFocus();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        //game loop :)
        while (gameThread != null) {
                //Update game info
                update();
                //Draw screen
                repaint();

                //this doesn't work properly but i fear if i delete it it will explode
                if (KeyHandler.debounce > 0) {
                    KeyHandler.debounce--;
                }

                //speedrun clock increment every second
            if (!isTutorial) {
                if (speedrunClockCount == 0) {
                    speedrunClock++;
                    speedrunClockCount = 30;
                } else if (speedrunClockCount > 0) {
                    speedrunClockCount--;
                }
            }

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
            } catch (Exception e) {
                e.printStackTrace();
            }

            nextDrawTime += drawInterval;
        }
    }

    int countdown = 0;
    int tutorialPage = 0;
    static int starvationCountdown = 60;

    public void update() {
        switch (gameState) {
            case 11: //title screen
                if (keyHandler.enterPressed) {
                    gameState = 10;
                    countdown = 10;
                }
                break;
            case 10: //intro cutscene, make it a big number to avoid the bullshit
                countdown = 10;
                intro.update();
                break;
            case 0: //normal menu
                if (isTutorial) {
                    if (countdown == 0 && keyHandler.enterPressed) {
                        tutorialPage++;
                        if (tutorialPage == 2) {
                            isTutorial = false;
                        }
                        countdown = 10;
                    } else if (countdown > 0) {
                        countdown--;
                    }
                } else {
                    decoration.update();
                    javadog.update(ui);
                    ui.update();
                    //check for game win
                    if (totalScore >= 100) {
                        gameState = gameEnd();
                    }
                    //check for game loss
                    if (starvationCountdown == 0) {
                        gameState = -1;
                    }
                    //debug
                    if (keyHandler.fourPressed) {
                        totalScore = 100;
                    }
                    if (keyHandler.threePressed) {
                        speedrunClock = 99999;
                    }
                    //debug
                    /*if (keyHandler.twoPressed && countdown == 0) {
                        FlashcardMinigame.gameIsRun = true;
                        countdown = 10;
                        gameState = 2;
                    }
                    if (keyHandler.threePressed && countdown == 0) {
                        JumpRope.gameIsRun = true;
                        countdown = 10;
                        gameState = 2;
                    }
                    if (keyHandler.fourPressed && countdown == 0) {
                        MemoryMinigame.gameIsRun = true;
                        countdown = 10;
                        gameState = 2;
                    }*/
                }
                break;
            case 1: //cutscene
                motiveCutscenes.update();
                break;
            case 2: //flashcards
                javadog.update(ui);
                if (FlashcardMinigame.gameIsRun) {
                    flashcardMinigame.update();
                    if (!FlashcardMinigame.gameIsRun) {
                        Javadog.resetMotive();
                        gameState = 0;
                        totalScore += flashcardMinigame.finalScore;
                        flashcardMinigame.reset();
                        countdown = 10;
                    }
                } else if (JumpRope.gameIsRun) {
                    jumpRope.update();
                    if (!JumpRope.gameIsRun) {
                        Javadog.resetMotive();
                        gameState = 0;
                        totalScore += jumpRope.score;
                        jumpRope.reset();
                        countdown = 10;
                    }
                } else if (MemoryMinigame.gameIsRun) {
                    memoryMinigame.update();
                    if (!MemoryMinigame.gameIsRun) {
                        Javadog.resetMotive();
                        gameState = 0;
                        totalScore += memoryMinigame.score;
                        memoryMinigame.reset();
                        countdown = 10;
                    }
                }
                break;
            case 3: //decoration menu
                decoration.update();
                ui.update();
                break;
            case 100: //normal end game
            case 999: //special ending
            case -1: //dog fucking died
                ending.update();
                break;
        }
        if (countdown > 0) {
            countdown--;
        }
    }

    public static boolean isTutorial = true;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        decoration.draw(g2);
        switch (gameState) {
            case 11:
                g2.drawImage(IntroCutscene.titleScreen, 0,0,null);
                break;
            case 10: //intro cutscene
                intro.draw(g2);
                break;
            case 0:
                if (isTutorial) {
                    javadog.draw(g2);
                    Minigame.drawTutorialBox(g2);
                    g2.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
                    g2.setColor(Color.white);
                    String toPrint = "";
                    switch (tutorialPage) {
                        case 0:
                            toPrint = "Welcome to Javadogs! Marvin is just like any";
                            g2.drawString(toPrint, 100, 150);
                            toPrint = "other dog, but with more advanced needs.";
                            g2.drawString(toPrint, 100, 200);

                            toPrint = "Use the arrow keys to navigate the buttons that ";
                            g2.drawString(toPrint, 100, 300);
                            toPrint = "will let you meet Marvin's needs. The icons ";
                            g2.drawString(toPrint, 100, 350);
                            toPrint = "tell you what each button does. ";
                            g2.drawString(toPrint, 100, 400);

                            toPrint = "Above all else, don't let Marvin get too hungry!";
                            g2.drawString(toPrint, 100, 450);
                            break;
                        case 1:
                            toPrint = "Javadogs love playing games, just like you!";
                            g2.drawString(toPrint, 100, 150);
                            toPrint = "Dog Corp is running a competition along with";
                            g2.drawString(toPrint, 100, 225);

                            toPrint = "the sweepstakes. If you can win 100 points";
                            g2.drawString(toPrint, 100, 275);
                            toPrint = "across the three available minigames, you";
                            g2.drawString(toPrint, 100, 325);
                            toPrint = "will win a prize.";
                            g2.drawString(toPrint, 100, 375);

                            toPrint = "What's the prize? You should find out!";
                            g2.drawString(toPrint, 100, 475);
                            break;
                    }
                } else {
                    javadog.draw(g2);
                    ui.draw(g2);
                }
                break;
            case 1:
                if (!javadog.canComplete()) {
                    javadog.draw(g2);
                }
                motiveCutscenes.draw(g2);
                break;
            case 2:
                if (FlashcardMinigame.gameIsRun) {
                    flashcardMinigame.drawMinigameBox(g2);
                    flashcardMinigame.draw(g2);
                }
                if (JumpRope.gameIsRun) {
                    jumpRope.drawMinigameBox(g2);
                    jumpRope.draw(g2);
                }
                if (MemoryMinigame.gameIsRun) {
                    memoryMinigame.drawMinigameBox(g2);
                    memoryMinigame.draw(g2);
                }
                break;
            case 3: //decoration menu
                ui.draw(g2);
                break;
            case 100: //normal end game
            case 999: //special ending
            case -1:
                ending.draw(g2);
                break;
        }
        //debug
        if (keyHandler.onePressed) {
            //DialogDraw.drawDialogBox(g2,"#!green Test# dialogue is very fun.\nTest #!red dialogue# is very fun.\nTest dialogue #!blue is very fun.");
        }
    }
}
