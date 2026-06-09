import javax.swing.*;
import java.awt.*;

public class BossFightGame extends JFrame
{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String currentUser = "";
    
    public BossFightGame() {
        setTitle("BossFightGame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(false);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        mainPanel.add(new Login(this), "LOGIN");
        
        add(mainPanel);
        cardLayout.show(mainPanel,"LOGIN");
    }
    
    public void startGame(String username) {
        this.currentUser = username;
        GamePanel gamePanel = new GamePanel();
        mainPanel.add(gamePanel, "GAME");
        cardLayout.show(mainPanel,"GAME");
        gamePanel.requestFocusInWindow();
        GamePanel.gameOver = false;
    }
    public void showLogin() {
        cardLayout.show(mainPanel,"LOGIN");
    }
    
    public String getCurrentUser()
    {
        return currentUser;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BossFightGame().setVisible(true);
        });
    }
}