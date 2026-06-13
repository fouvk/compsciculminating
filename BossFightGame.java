import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;


public class BossFightGame extends JFrame
{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String currentUser = "";
    
    private Login loginPanel;
    private EndScreenPanel endScreenPanel;
    
    public BossFightGame() {
        setTitle("BossFightGame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(false);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        loginPanel = new Login(this);
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(new HowToPanel(this), "HOWTO");
        
        endScreenPanel = new EndScreenPanel(this);
        mainPanel.add(endScreenPanel, "END");
        
        add(mainPanel);
        cardLayout.show(mainPanel,"LOGIN");
    }
    
    public void startGame(String username) {
        this.currentUser = username;
        GamePanel gamePanel = new GamePanel(this);
        mainPanel.add(gamePanel, "GAME");
        cardLayout.show(mainPanel,"GAME");
        gamePanel.requestFocusInWindow();
        GamePanel.gameOver = false;
    }
    public void showLogin() {
        loginPanel.refreshLeaderboard();
        cardLayout.show(mainPanel,"LOGIN");
        
    }
    
    public void showHowTo() {
        cardLayout.show(mainPanel,"HOWTO");
    }
    
    public void showEnd(int playerHP, int bossHP, int stage, int mins, int secs) {
        endScreenPanel.updateStats(playerHP, bossHP, stage, mins, secs);
        cardLayout.show(mainPanel, "END");
    }
    
    public List<String> getLeaderboard() {
        try {
            Path path = Paths.get("Leaderboard.txt");
            if (!Files.exists(path)) {
                return new ArrayList<>();
            }
            List<String> lines = Files.readAllLines(path);
            Collections.sort(lines);
            return lines;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public void saveScore(String name, int totalSeconds) {
        List<String> scores = getLeaderboard();
        scores.add(String.format("%05d,%s", totalSeconds, name));
        Collections.sort(scores);
        if (scores.size() > 5) {
            scores = scores.subList(0,5);
        }
        try {
            Files.write(Paths.get("Leaderboard.txt"), scores);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
