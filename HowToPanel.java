import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;

class HowToPanel extends JPanel 
{
    private Image howTo; 
    
    public HowToPanel(BossFightGame frame)
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        
        loadStartPage();
        
        JLabel title = new JLabel("HOW TO");
        title.setFont(new Font("Arial", Font.BOLD, 70)); 
        title.setForeground(Color.WHITE);
        
        JLabel control = new JLabel("<html>" +
            "Left/Right : 'a' / 'd'<br>" +
            "Crouch : 's'<br>" +
            "Jump: Space Bar<br>" +
            "Shoot: 'j'" +
            "</html>");
        control.setFont(new Font("Arial", Font.BOLD, 24)); 
        control.setForeground(Color.WHITE);
        
        ImageIcon backIcon = new ImageIcon(Login.class.getResource("/Images/Back.png"));
        JButton back = new JButton(backIcon);

        makeImageButton(back, 250, 80);
        
        gbc.gridx = 1; gbc.gridy = 1; 
        add(title,gbc);
        gbc.gridx = 1; gbc.gridy = 2; 
        add(control,gbc);
        gbc.gridx = 1; gbc.gridy = 3; 
        add(back,gbc);
        
        back.addActionListener(e -> 
        {
                frame.showLogin();
        });
    }
        public void loadStartPage() {
            try {
            howTo = ImageIO.read(getClass().getResource("/Images/StartPage.png"));
            }
            catch (IOException e) {
            System.out.println("Could not find image");
            }
        }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
            if (howTo != null) {
                g.drawImage(howTo, 0, 0, getWidth(), getHeight(), this);
            }
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
}
