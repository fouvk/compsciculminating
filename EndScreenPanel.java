import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;


class EndScreenPanel extends JPanel
{ 
    private JButton back;
    private JButton restart;
    private JLabel results;
    
    public EndScreenPanel (BossFightGame frame)
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);      
        
        setBackground(Color.LIGHT_GRAY);
        
        results = new JLabel("");
        results.setFont(new Font("Arial", Font.BOLD, 24)); 
        results.setForeground(Color.WHITE);
        
        ImageIcon backIcon = new ImageIcon(Login.class.getResource("/Images/Back.png"));
        back = new JButton(backIcon);

        makeImageButton(back, 250, 80);
        
        ImageIcon restartIcon = new ImageIcon(Login.class.getResource("/Images/Restart.png"));
        restart = new JButton(restartIcon);

        makeImageButton(restart, 250, 80);
        
        
        back.addActionListener(e -> 
        {
            frame.showLogin();
        });
        
        restart.addActionListener(e -> 
        {
            frame.startGame(frame.getCurrentUser());
        });
        
        gbc.gridx = 2; gbc.gridy = 1; 
        add(results,gbc);
        gbc.gridx = 1; gbc.gridy = 2; 
        add(back,gbc);
        gbc.gridx = 3; gbc.gridy = 2; 
        add(restart,gbc);
    }
    
    public void makeImageButton(JButton btn, int width, int height) 
    {
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        
        if (btn.getIcon() instanceof ImageIcon) {
            ImageIcon icon = (ImageIcon) btn.getIcon();
            Image img = icon.getImage();
            
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); 
            btn.setIcon(new ImageIcon(scaledImg));
        }
        btn.setPreferredSize(new Dimension(width, height));
    }
    
    public void updateStats(int playerHP, int bossHP, int stage, int mins, int secs) {
        results.setText("<html>" +
            "Health : " + playerHP + "<br>" +
            "Boss Health : " + bossHP + "<br>" +
            "Stage: " + stage + "<br>" +
            "Time: " + mins + ":" + String.format("%02d", secs) +
            "</html>");
    }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            
                
        }
}
