import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class KeyHandler implements KeyListener {

    public volatile static KeyEvent mostRecentKey = null;
    public volatile static int debounce = 0;
    boolean upPressed, downPressed;
    boolean onePressed, twoPressed, threePressed, fourPressed;
    AtomicBoolean leftPressed = new AtomicBoolean(false);
    AtomicBoolean rightPressed = new AtomicBoolean(false);
    boolean buttonPressed;
    boolean enterPressed;
    boolean spacePressed;

    GamePanel gp; //used to access the game panel thread

    public KeyHandler(GamePanel gp) {
        super();
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    private synchronized boolean isPressable(KeyEvent e) {
        if (mostRecentKey == null) {
            mostRecentKey = e;
            debounce = 10;
            return true;
        }
        if (mostRecentKey.getKeyCode() == e.getKeyCode()) {
            if (debounce == 0) {
                debounce = 10;
                return true;
            }
            //do nothing if debounce > 0
            return false;
        } else {
            debounce = 10;
            mostRecentKey = e;
            return true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: //0
                leftPressed.set(isPressable(e));
                break;
            case KeyEvent.VK_RIGHT: //1
                rightPressed.set(isPressable(e));
                break;
            case KeyEvent.VK_UP: //2
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN: //3
                downPressed = true;
                break;
            case KeyEvent.VK_1:
                onePressed = true;
                break;
            case KeyEvent.VK_2:
                twoPressed = true;
                break;
            case KeyEvent.VK_3:
                threePressed = true;
                break;
            case KeyEvent.VK_4:
                fourPressed = true;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = true;
                buttonPressed = true;
                break;
            case KeyEvent.VK_SPACE:
                spacePressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_LEFT:
                leftPressed.set(false);
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed.set(false);
                break;
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
            case KeyEvent.VK_1:
                onePressed = false;
                break;
            case KeyEvent.VK_2:
                twoPressed = false;
                break;
            case KeyEvent.VK_3:
                threePressed = false;
                break;
            case KeyEvent.VK_4:
                fourPressed = false;
                break;
            case KeyEvent.VK_ENTER:
                buttonPressed = false;
                enterPressed = false;
                break;
            case KeyEvent.VK_SPACE:
                spacePressed = false;
                break;
        }
    }
}
