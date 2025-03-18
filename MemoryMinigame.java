import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MemoryMinigame extends Minigame{
    KeyHandler keyHandler;
    public static boolean gameIsRun = false;
    private boolean isTutorial = true;
    int score = 0;
    int remainingGuesses;

    private int[][] easyCards = { //difficulty 0
            {0,0,0,0},
            {0,0,0,0}
    };

    private int[][] mediumCards = { //difficulty 1
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}
    };

    private int[][] hardCards = { //difficulty 2
            {0,0,0,0,0,0},
            {0,0,0,0,0,0},
            {0,0,0,0,0,0}
    };

    private boolean[][] isFlipped;
    private boolean[][] isCorrect;
    private boolean[][] previousFlip;

    private int[][] getDeck() {
        switch (difficulty) {
            case 0:
                return easyCards;
            case 1:
                return mediumCards;
            default:
                return hardCards;
        }
    }

    private BufferedImage cardBackground, cardBack;
    private BufferedImage correctCard;
    private BufferedImage incorrectCard;
    private BufferedImage[] cardImages = new BufferedImage[9];
    private BufferedImage cursor;

    int difficulty = 0;

    public MemoryMinigame(KeyHandler keyHandler) {
        super(keyHandler);
        this.keyHandler = keyHandler;
        randomizeCards();
        setDeckBooleans();
        loadImages();
        remainingGuesses = (int)Math.floor(3 + 2 * difficulty); //fuck java dude
        /*for (int i = 0; i < hardCards.length; i++) {
            for (int j = 0; j < hardCards[i].length; j++) {
                System.out.print(hardCards[i][j] + " ");
            }
            System.out.println();
        }*/
    }

    private void loadImages() {
        try {
            cardBackground = ImageIO.read(new File("src/minigames/memory/cardbase.png"));
            for (int i = 0; i < cardImages.length; i++) {
                cardImages[i] = ImageIO.read(new File("src/minigames/memory/card"+(i+1)+".png"));
            }
            correctCard = ImageIO.read(new File("src/minigames/memory/cardcorrect.png"));
            incorrectCard = ImageIO.read(new File("src/minigames/memory/incorrectcard.png"));
            cursor = ImageIO.read(new File("src/minigames/memory/cursor.png"));
            cardBack = ImageIO.read(new File("src/minigames/memory/cardback.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void randomizeCards() {
        int[][] cards;
        for (int numDifficulties = 0; numDifficulties < 3; numDifficulties++) {
            switch (numDifficulties) {
                case 0:
                    cards = easyCards;
                    break;
                case 1:
                    cards = mediumCards;
                    break;
                default:
                    cards = hardCards;
                    break;
            }
            int numUniqueCards = (cards.length * cards[0].length) / 2;
            for (int cardType = 1; cardType <= numUniqueCards; cardType++) { //loop for each card type
                for (int numCardType = 0; numCardType < 2; numCardType++) { //twice per card type
                    boolean cardIsPlaced = false;
                    int cardRng = 0;
                    //fucking pray
                    while (cardRng < 3) {
                        for (int i = 0; i < cards.length; i++) { //rows
                            for (int j = 0; j < cards[i].length; j++) { //cols
                                cardRng = rng.nextInt(4); //random call for card placement
                                if (cardRng == 2 && cards[i][j] == 0 && !cardIsPlaced) {
                                    cards[i][j] = cardType;
                                    cardIsPlaced = true;
                                } else if (!cardIsPlaced){ //fuuuuuck java dude
                                    cardRng = 0;
                                }
                            }
                        }
                    }
                }
            }

            switch (numDifficulties) {
                case 0:
                    easyCards = cards;
                    break;
                case 1:
                    mediumCards = cards;
                    break;
                default:
                    hardCards = cards;
                    break;
            }
        }
    }

    private void setDeckBooleans() {
        int[][] deck = getDeck();
        isFlipped = new boolean[deck.length][deck[0].length];
        isCorrect = new boolean[deck.length][deck[0].length];
        previousFlip = new boolean[deck.length][deck[0].length];
        for (int i = 0; i < deck.length; i++) {
            for (int j = 0; j < deck[i].length; j++) {
                isFlipped[i][j] = false;
                isCorrect[i][j] = false;
                previousFlip[i][j] = false;
            }
        }
    }

    @Override
    public BufferedImage getBackground() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("src/minigames/memorybackground.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    boolean gameOver = false;
    boolean gameWon = false;
    boolean firstFlipped = false;
    int coordX = 0;
    int coordY = 0;
    int count = 10;

    int coordCorrectX[] = new int[2];
    int coordCorrectY[] = new int[2];

    @Override
    public void update() {
        if (!isTutorial && !gameOver && !gameWon) {
            int[][] currDeck = getDeck();
            //handle moving over cards
            if (count == 0) {
                if (keyHandler.leftPressed.get()) {
                    if (coordX == 0) {
                        coordX = currDeck[0].length - 1;
                    } else {
                        coordX--;
                    }
                    count = 10;
                }
                if (keyHandler.rightPressed.get()) {
                    if (coordX == currDeck[0].length - 1) {
                        coordX = 0;
                    } else {
                        coordX++;
                    }
                    count = 10;
                }
                if (keyHandler.upPressed) {
                    if (coordY == 0) {
                        coordY = currDeck.length-1;
                    } else {
                        coordY--;
                    }
                    count = 10;
                }
                if (keyHandler.downPressed) {
                    if (coordY == currDeck.length-1) {
                        coordY = 0;
                    } else {
                        coordY++;
                    }
                    count = 10;
                }
                //handle selecting card
                if (keyHandler.enterPressed) {
                    //check that isn't already flipped and isn't already revealed correct
                    if (!isFlipped[coordY][coordX] && !isCorrect[coordY][coordX]) {
                        isFlipped[coordY][coordX] = true;
                        if (!firstFlipped) {
                            firstFlipped = true;
                            resetPreviousVars();
                            coordCorrectX[0] = coordX;
                            coordCorrectY[0] = coordY;
                        } else {
                            coordCorrectX[1] = coordX;
                            coordCorrectY[1] = coordY;
                            checkFlips();
                            firstFlipped = false;
                            if (isWon()) {
                                gameWon = true;
                            }
                        }
                    }
                    count = 10;
                }
            }
        } else if (keyHandler.enterPressed && count == 0){ //etc screens
            if (gameOver || gameWon) {
                score += remainingGuesses;
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

    private void checkFlips() {
        int[][] deck = getDeck();
        if (deck[coordCorrectY[0]][coordCorrectX[0]] == deck[coordCorrectY[1]][coordCorrectX[1]]) {
            isCorrect[coordCorrectY[0]][coordCorrectX[0]] = true;
            isCorrect[coordCorrectY[1]][coordCorrectX[1]] = true;
            score++;
        } else {
            remainingGuesses--;
            if (remainingGuesses == 0) {
                gameOver = true;
            }
        }
        isFlipped[coordCorrectY[0]][coordCorrectX[0]] = false;
        isFlipped[coordCorrectY[1]][coordCorrectX[1]] = false;
        previousFlip[coordCorrectY[0]][coordCorrectX[0]] = true;
        previousFlip[coordCorrectY[1]][coordCorrectX[1]] = true;
    }

    private boolean isWon() {
        int[][] deck = getDeck();
        for (int i = 0; i < deck.length; i++) {
            for (int j = 0; j < deck[i].length; j++) {
                if (!isCorrect[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetPreviousVars() {
        previousFlip[coordCorrectY[0]][coordCorrectX[0]] = false;
        previousFlip[coordCorrectY[1]][coordCorrectX[1]] = false;
        coordCorrectX[0] = 0;
        coordCorrectY[0] = 0;
        coordCorrectX[1] = 0;
        coordCorrectY[1] = 0;
    }

    private int getDistToCenter(int imageField, char coord) {
        switch (coord) {
            case 'X':
                return (800 - imageField) / 2;
            case 'Y':
                return (600 - imageField) / 2;
        }
        return 0;
    }

    @Override
    public void draw(Graphics2D g2d) {
        BufferedImage image = null;
        int maxX, maxY;
        int imageSize = 75;
        int padding = 10;
        int[][] printedCards = getDeck();
        maxX = printedCards[0].length;
        maxY = printedCards.length;

        int imageFieldX = (imageSize * maxX) + (padding * maxX);
        int imageFieldY = (imageSize * maxY) + (padding * maxY);

        int xPrint = getDistToCenter(imageFieldX, 'X');
        int yPrint = getDistToCenter(imageFieldY, 'Y');
        if (isTutorial) {
            Minigame.drawTutorialBox(g2d);
            g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
            g2d.setColor(Color.white);
            String toPrint = "";
            toPrint = "Flip cards over and try to make matches. You'll get";
            g2d.drawString(toPrint , 100, 150);
            toPrint = "extra points for every guess you don't use, so guess";
            g2d.drawString(toPrint , 100, 200);
            toPrint = "sparingly!";
            g2d.drawString(toPrint , 100, 250);

        } else {
            //print cards
            for (int i = 0; i < printedCards.length; i++) {
                for (int j = 0; j < printedCards[i].length; j++) {
                    image = cardBack;
                    g2d.drawImage(image, xPrint, yPrint, null);

                    if (isFlipped[i][j] || isCorrect[i][j] || previousFlip[i][j]) { //check if symbol is revealed
                        image = cardBackground;
                        g2d.drawImage(image, xPrint, yPrint, null);
                        image = cardImages[printedCards[i][j] - 1];
                        g2d.drawImage(image, xPrint, yPrint, null);

                        if (isCorrect[i][j]) { //check if card should be color changed (stays flipped)
                            image = correctCard;
                            g2d.drawImage(image, xPrint, yPrint, null);
                        } else if (previousFlip[i][j]) {
                            image = incorrectCard;
                            g2d.drawImage(image, xPrint, yPrint, null);
                        }
                    }
                    //print cursor
                    if (coordX == j && coordY == i) {
                        image = cursor;
                        g2d.drawImage(image, xPrint, yPrint, null);
                    }
                    xPrint = xPrint + imageSize + padding;
                }
                xPrint = getDistToCenter(imageFieldX, 'X');
                yPrint = yPrint + imageSize + padding;
            }

            g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            g2d.drawString("Score: " + score, 55,540);

            if (gameOver) {
                g2d.drawString("Game over!", 250,100);
            } else if (gameWon) {
                g2d.drawString("You won!", 250,100);
            } else {
                g2d.drawString("Guesses remaining: " + remainingGuesses, 250,100);
            }
        }
        //center of screen is (400, 300)
    }

    @Override
    public void reset() {
        if (gameWon) {
            difficulty++;
            if (difficulty > 2) {
                difficulty = 0;
            }
        }
        gameOver = false;
        gameWon = false;
        resetPreviousVars();
        setDeckBooleans();
        remainingGuesses = (int)Math.floor(3 + 2 * difficulty);
        count = 10;
        coordX = 0;
        coordY = 0;
        score = 0;
        isTutorial = true;
    }
}
