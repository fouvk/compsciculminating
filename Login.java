import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;

 class Login extends JPanel {
     private JTextField textField;
     public JTextField EnterUN = new JTextField(20);
     public static String name;
     private Image start; 
     private Image player;
     
     private BossFightGame frame;
     private JLabel leaderboardLabel = new JLabel();
     
     public Login(BossFightGame frame)
     {
         this.frame = frame;
         
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        
        //Buttons
        ImageIcon startIcon = new ImageIcon(Login.class.getResource("/Images/Start.png"));
        JButton playBtn = new JButton(startIcon);
        playBtn.setIcon(startIcon);
        makeImageButton(playBtn);
        
        ImageIcon howIcon = new ImageIcon(Login.class.getResource("/Images/HowTo.png"));
        JButton howto = new JButton(howIcon);
        howto.setIcon(howIcon);
        makeImageButton(howto);
        
        ImageIcon quitIcon = new ImageIcon(Login.class.getResource("/Images/Quit.png"));
        JButton quit = new JButton(quitIcon);
        quit.setIcon(quitIcon);
        makeImageButton(quit);
        
        loadStartPage();
        gbc.gridx = 1; gbc.gridy = 1; 
        add(playBtn,gbc);
        gbc.gridy = 2; 
        add(EnterUN,gbc);
        gbc.gridy = 3;
        add(howto,gbc);
        gbc.gridy = 4; 
        add(quit,gbc);
        gbc.gridy = 5; 
        add(leaderboardLabel,gbc);
        
        refreshLeaderboard();
        
        playBtn.addActionListener(e -> 
        {
            name = EnterUN.getText().trim();
            if(!name.isEmpty())
            {
                frame.startGame(name);
            }
            
        });
        
        howto.addActionListener(e -> 
            {
            frame.showHowTo();
            });
        
        quit.addActionListener(e -> 
            {
            System.exit(0);
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

        public void refreshLeaderboard() {
            List<String> scores = frame.getLeaderboard();
            StringBuilder html = new StringBuilder("<html><div style='text-align: center; padding-left: 50px;'>" + "<b style='font-size:20px; color:orange;'>FASTEST KILLS</b><br><br>");
            
            for(int i = 0; i < 5; i++) {
                if (i < scores.size()) {
                    String[] parts = scores.get(i).split(",");
                    int totalSecs = Integer.parseInt(parts[0]);
                    String playerName = parts[1].trim();
                    
                    int mins = totalSecs / 60;
                    int secs = totalSecs % 60;
                    
                    html.append("<span style='color: #FFD700;'>");
                    
                    html.append((i + 1)).append(". ").append(playerName).append((" - ")).append(mins).append(":").append(String.format("%02d", secs)).append("<br>");
                } else {
                    html.append((i+1)).append(". <span style='color: #FFFFFF;'>---</span><br>");
                }
            }
            html.append("</div></html>");
            leaderboardLabel.setText(html.toString());
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        
                g.drawImage(start, 0, 0, getWidth(), getHeight(), this);
                g.drawImage(player, 100, 130, 200, 400, this);
                    
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                g.drawString("VIRUS", 720, 200);
                g.drawString("MAYHEM", 720, 250);
                    
                
        }
        
        public void makeImageButton(JButton btn) 
        {
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setOpaque(false);
        }
    
 }
