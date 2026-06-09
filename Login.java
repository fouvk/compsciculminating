import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;

 class Login extends JPanel {
     private JTextField textField;
     public JTextField EnterUN = new JTextField(20);
     public static String name;
     private Image start; 
     private Image player;
     
     public Login(BossFightGame frame)
     {
        JLabel explanation = new JLabel("Entering username allows high score to be saved");
        JLabel prompt = new JLabel("Enter Username: ");
        JButton playBtn = new JButton("Proceed to game");
        
        explanation.setBounds(50, 100, 500, 50);
        prompt.setBounds(50, 50, 150, 50);
        EnterUN.setBounds(180, 60, 200, 30);
        playBtn.setBounds(400, 60, 200, 30);
        
        loadStartPage();
        add(explanation);
        add(EnterUN);
        add(prompt);
        add(playBtn);
        
        playBtn.addActionListener(e -> 
        {
            name = EnterUN.getText().trim();
            if(!name.isEmpty())
            {
                frame.startGame(name);
            }
            
        });
     
     }
        public void loadStartPage() {
            try {
            start = ImageIO.read(getClass().getResource("/Images/StartPage.png"));
            player = ImageIO.read(getClass().getResource("/Images/CharacterHome.png"));
            }
            catch (IOException e) {
            System.out.println("Could not find image");
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        
                if (start != null || player != null) {
                    g.drawImage(start, 0, 0, getWidth(), getHeight(), this);
                    g.drawImage(player, 150, 130, 200, 400, this);
                }
        }
            
    
 }