import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FlashcardMinigame extends Minigame{
    private KeyHandler keyHandler;

    private int equationOne;
    private int equationTwo;
    private int equationAnswer;
    private BufferedImage card;
    private BufferedImage[] numbers = new BufferedImage[10];
    private BufferedImage[] operators = new BufferedImage[4];

    //calculator
    private BufferedImage calcBackground, calcButton1, calcButton2;
    private BufferedImage clear, equals;
    int buttonY = 0;
    int buttonX = 0;

    private int difficulty = 0;
    private boolean questionIsAnswered = false;
    private int problemNum = 1;
    public static boolean gameIsRun = false;
    private boolean gameOver = false;
    private boolean isTutorial = true;
    public int score = 0;
    public FlashcardMinigame(KeyHandler keyHandler) {
        super(keyHandler); //does fucking nothing lol
        this.keyHandler = keyHandler;
        loadImages();
        generateMathEquation(difficulty, true);
    }

    @Override
    public BufferedImage getBackground() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/minigames/flashcardbackground.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private void loadImages() {
        try {
            //flashcard shit
            card = ImageIO.read(new File("src/minigames/cardgame/flashcard.png"));
            for (int i = 0; i < 10; i++) { //i am so clever right
                numbers[i] = ImageIO.read(new File("src/minigames/cardgame/" + i + ".png"));
            }
            operators[0] = ImageIO.read(new File("src/minigames/cardgame/+.png"));
            operators[1] = ImageIO.read(new File("src/minigames/cardgame/-.png"));
            operators[2] = ImageIO.read(new File("src/minigames/cardgame/x.png"));
            operators[3] = ImageIO.read(new File("src/minigames/cardgame/divide.png"));

            //calculator
            calcBackground = ImageIO.read(new File("src/minigames/cardgame/calculator_background.png"));
            calcButton1 = ImageIO.read(new File("src/uimanagement/button1.png"));
            calcButton2 = ImageIO.read(new File("src/uimanagement/button2.png"));
            clear = ImageIO.read(new File("src/minigames/cardgame/clear.png"));
            equals = ImageIO.read(new File("src/minigames/cardgame/equals.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateMathEquation(int type, boolean inclZero) {
        // TYPES:
        // 0: addition
        // 1: subtraction
        // 2: multiplication
        // 3: division
        int tempNum;
        switch (type) {
        case 0:
            equationOne = rng.nextInt(0,10);
            equationTwo = rng.nextInt(0,10);
            equationAnswer = equationOne + equationTwo;
            break;
        case 1:
            // Subtraction and divison are inverses of addition and multiplication,
            // so to avoid negatives or remainders, just generate an addition or
            // multiplication problem and swap equationOne and equationAnswer
            generateMathEquation(0, true);
            tempNum = equationOne;
            equationOne = equationAnswer;
            equationAnswer = tempNum;
            break;
        case 2:
            if (inclZero) {
                equationOne = rng.nextInt(0,10);
                equationTwo = rng.nextInt(0,10);
            } else {
                equationOne = rng.nextInt(1,10);
                equationTwo = rng.nextInt(1,10);
            }
            equationAnswer = equationOne * equationTwo;
            break;
        case 3:
            generateMathEquation(2, false);
            tempNum = equationOne;
            equationOne = equationAnswer;
            equationAnswer = tempNum;
            break;
        }
    }

    private int checkAnswer(String answer) {
        int numAnswer;
        try {
            numAnswer = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            return 1; //if the program somehow fucks up, just mark it right, not the user's fault
        }
        if (numAnswer == equationAnswer) {
            return 1;
        }
        return 0;
    }

    public int timeBonus, finalScore;

    private void calculateFinalScore() {
        //get time bonus
        switch (minutes) {
            case 0:
                timeBonus = 5;
                break;
            case 1:
                if (seconds <= 30) {
                    timeBonus = 4;
                } else {
                    timeBonus = 3;
                }
                break;
            case 2:
                if (seconds <= 30) {
                    timeBonus = 2;
                } else {
                    timeBonus = 1;
                }
                break;
            default:
                timeBonus = 0;
        }

        //difficulty bonus is + difficulty

        finalScore = score + timeBonus + difficulty;

    }

    private String userAnswer = "";
    private int count = 10;

    private int seconds = 0;
    private int minutes = 0;
    private int timeCount = 30;

    @Override
    public void update() {
        if (!isTutorial && !gameOver) {
            //check for generating new problem
            if (questionIsAnswered) {
                score += checkAnswer(userAnswer);
                userAnswer = "";
                if (problemNum < 10) { //check if last problem was answered
                    generateMathEquation(difficulty, true);
                    questionIsAnswered = false;
                    problemNum++;
                } else {
                    gameOver = true;
                    calculateFinalScore();
                }
            } else if (count == 0) {
                //let user type on calculator
                if (keyHandler.downPressed) {
                    if (buttonY == 0) {
                        buttonY = 3;
                    } else {
                        buttonY --;
                    }
                    count = 10;
                }
                if (keyHandler.upPressed) {
                    if (buttonY == 3) {
                        buttonY = 0;
                    } else {
                        buttonY++;
                    }
                    count = 10;
                }
                if (keyHandler.leftPressed.get()) {
                    if (buttonX == 0) {
                        buttonX = 2;
                    } else {
                        buttonX--;
                    }
                    count = 10;
                }
                if (keyHandler.rightPressed.get()) {
                    if (buttonX == 2) {
                        buttonX = 0;
                    } else {
                        buttonX++;
                    }
                    count = 10;
                }
                if (keyHandler.enterPressed) {
                    switch (buttonY) {
                        case 0: //row 1
                            switch (buttonX) {
                                case 0: //0
                                    userAnswer = userAnswer.concat("0");
                                    break;
                                case 1: //clear
                                    userAnswer = "";
                                    break;
                                case 2:
                                    if (!userAnswer.equals("")) {
                                        questionIsAnswered = true;
                                    }
                                    break;
                            }
                            break;
                        case 1: //row 2
                            switch (buttonX) {
                                case 0: //1
                                    userAnswer = userAnswer.concat("1");
                                    break;
                                case 1: //2
                                    userAnswer = userAnswer.concat("2");
                                    break;
                                case 2: //3
                                    userAnswer = userAnswer.concat("3");
                                    break;
                            }
                            break;
                        case 2: //row 3
                            switch (buttonX) {
                                case 0: //4
                                    userAnswer = userAnswer.concat("4");
                                    break;
                                case 1: //5
                                    userAnswer = userAnswer.concat("5");
                                    break;
                                case 2: //6
                                    userAnswer = userAnswer.concat("6");
                                    break;
                            }
                            break;
                        case 3: //row 4
                            switch (buttonX) {
                                case 0: //7
                                    userAnswer = userAnswer.concat("7");
                                    break;
                                case 1: //8
                                    userAnswer = userAnswer.concat("8");
                                    break;
                                case 2: //9
                                    userAnswer = userAnswer.concat("9");
                                    break;
                            }
                            break;
                    }
                    count = 10;
                }
            }
            if (timeCount == 0) {
                incrementTime();
                timeCount = 30;
            } else {
                timeCount--;
            }
        } else if (keyHandler.enterPressed && count == 0){ //etc screens
            if (gameOver) {
                gameIsRun = false;
            } else {
                isTutorial = false;
            }
            count = 10;
        }
        if (count > 0) {
            count--;
        }
    }

    private void incrementTime() {
        seconds++;
        if (seconds == 60) {
            seconds = 0;
            minutes++;
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
            toPrint = "Solve the math problems and enter your answer into";
            g2d.drawString(toPrint , 100, 150);
            toPrint = "the calculator. You will be given extra points for";
            g2d.drawString(toPrint , 100, 200);
            toPrint = "finishing fast!";
            g2d.drawString(toPrint , 100, 250);
        } else if (!gameOver){
            //print flashcard
            image = card;
            g2d.drawImage(image, 100, 100, null);

        /* print numbers test
        int x = 50;
        for (int i = 0; i < 10; i++) {
            image = numbers[i];
            g2d.drawImage(image, x, 300, null);
            x += 70;
        }*/

            //print numbers
            //equationOne can be above 9 because of subtraction or divison problems
            if (equationOne > 9) {
                //deal with two-digit numbers
                int tensDigit = (equationOne / 10) % 10;
                image = numbers[tensDigit];
                g2d.drawImage(image, 140, 150, null);
                int onesDigit = (equationOne / 1) % 10;
                image = numbers[onesDigit];
                g2d.drawImage(image, 185, 150, null);
            } else {
                image = numbers[equationOne];
                g2d.drawImage(image, 185, 150, null);
            }

            //get the operator
            image = operators[difficulty]; //operator corresponds to difficulty level, see how smart i am
            g2d.drawImage(image, 140, 215, null);

            //equationTwo will never be above 9
            image = numbers[equationTwo];
            g2d.drawImage(image, 185, 215, null);

            //TODO: remove
            //display answer
        /*if (equationAnswer > 9) {
            //deal with two-digit numbers
            int tensDigit = (equationAnswer/10)%10;
            image = numbers[tensDigit];
            g2d.drawImage(image,250,300,null);
            int onesDigit = (equationAnswer/1)%10;
            image = numbers[onesDigit];
            g2d.drawImage(image,275,300,null);
        } else {
            image = numbers[equationAnswer];
            g2d.drawImage(image,275,300,null);
        }*/

            //calculator
            image = calcBackground;
            g2d.drawImage(image, 375, 75, null);

            //calculator buttons
            int x = 410;
            int y = 435;
            int num = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttonY == i && buttonX == j) {
                        image = calcButton2;
                    } else {
                        image = calcButton1;
                    }
                    g2d.drawImage(image, x, y, null);
                    image = numbers[num];
                    if (i == 0 && j == 1) {
                        image = clear;
                    } else if (i == 0 & j == 2) {
                        image = equals;
                    } else {
                        num++;
                    }
                    g2d.drawImage(image, x + 12, y + 12, null);
                    x += 90;
                }
                x = 410;
                y -= 90;
            }

            // calculator screen
            g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            int dialogWidth = (int) g2d.getFontMetrics().getStringBounds(userAnswer, g2d).getWidth();
            g2d.drawString(userAnswer, (675 - dialogWidth), 142);

            //score
            g2d.drawString("Score: " + score, 60, 540);
            g2d.drawString(minutes + ":" + String.format("%02d", seconds), 60, 100);
        } else {
            g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            int xPrint = 400-((int)g2d.getFontMetrics().getStringBounds("Game over!",g2d).getWidth())/2;
            g2d.drawString("Game over!" , xPrint, 100);

            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
            g2d.drawString("Score: " + score, 100, 150);
            g2d.drawString("Time bonus: +" + timeBonus, 100, 200);
            g2d.drawString("Difficulty bonus: +" + difficulty, 100, 250);

            g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            xPrint = 400-((int)g2d.getFontMetrics().getStringBounds("Final score: " + finalScore,g2d).getWidth())/2;
            g2d.drawString("Final score: " + finalScore , xPrint, 500);
        }
    }

    @Override
    public void reset() {
        count = 10;
        difficulty++;
        if (difficulty > 3) {
            difficulty = 0;
        }
        problemNum = 1;
        questionIsAnswered = false;
        gameOver = false;
        isTutorial = true;
        score = 0;
        buttonY = 0;
        buttonX = 0;
        minutes = 0;
        seconds = 0;
    }


}
