import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        super("Javadogs");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        GamePanel gamePanel = new GamePanel(mainWindow);
        mainWindow.add(gamePanel);
        mainWindow.pack(); //Causes this Window to be sized to fit the preferred size and layouts of its subcomponents.
        mainWindow.setLocationRelativeTo(null);
        gamePanel.startGameThread();

        mainWindow.revalidate();
    }
}