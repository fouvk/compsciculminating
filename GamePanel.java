import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.IOException;

class GamePanel extends JPanel implements ActionListener, KeyListener{
      
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;
    
    private Timer timer;
    private Player player;
    private Boss boss;
    private Image image;
    private long timeStart = 0;
    private long time = 0;
    private int seconds = 0;
    private int minutes = 0;
    public static boolean gameOver = true;

    private List<GameObject> objects = new ArrayList<>();

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        loadBackground();
        setFocusable(true);
        addKeyListener(this);

        player = new Player(100, 600-70);
        boss = new Boss(600, 200, player);

        objects.add(player);
        objects.add(boss);
        objects.add(new Platform(100, 500));
        objects.add(new Platform(450, 500));
        objects.add(new Platform(275, 400));

        timer = new Timer(16, this);
        timer.start();
        
        startTime();
    }

    public void loadBackground() {
        try {
        image = ImageIO.read(getClass().getResource("/Images/Background.png"));
        }
        catch (IOException e) {
            System.out.println("Could not find image");
        }
    }
    
    public void startTime() {
        timeStart = System.currentTimeMillis();
    }



    public void timer() {
        time = System.currentTimeMillis() - timeStart;
        seconds = (int)(time/1000) % 60;
        minutes = (int)(time/1000/60) % 60;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        for (GameObject obj : new ArrayList<>(objects)) {
            obj.update(objects);
        }
        
        
        if(!gameOver)
        {
            timer();
            
            handleCollisions();
            cleanup();
    
            repaint();
        }
        
    }
    

    private void handleCollisions() {
        player.grounded = false;
        for (GameObject obj : objects) {
            

            if (obj instanceof Attack attack) {

                if (attack.isPlayer && attack.getBounds().intersects(boss.getBounds())) {
                    boss.damage(attack.damage);
                    attack.setAlive(false);
                }

                if (!attack.isPlayer && attack.getBounds().intersects(player.getBounds())) {
                    player.damage(attack.damage);
                    attack.setAlive(false);
                }
            }
            
            if(obj instanceof Platform platform)
            { // if the player is holding down and jump at the same time, they sink through the first 10 pixels of the playforms top
                if(!(player.down && player.jump) && player.prevY + player.height <= platform.y+10 && player.y+player.height > platform.y && player.vy > 0 && player.x+player.width >= platform.x && player.x <= platform.x+platform.width)
                {
                    player.grounded = true;
                     //lets player jump
                    player.y = platform.y-player.height; //fix the player position
                    player.vy = 0; 
                }
            }  
        }
        
        if(player.y >= 600-player.height) { //ground
            player.y = 600-player.height;
            player.vy = 0;
            player.grounded = true;
        }
    }

    private void cleanup() {
        objects.removeIf(o -> !o.isAlive());
    }
    
    public Player getPlayer() {
        return player;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (image != null) {
            g.drawImage(image, 0, 0, WIDTH, HEIGHT, this);
        }

        for (GameObject obj : objects) {
            obj.draw(g);
        }

        g.setColor(Color.WHITE);
        g.drawString("Player HP: " + player.getHealth(), 20, 20);
        g.drawString("Boss HP: " + boss.getHealth(), 20, 40);
        g.drawString("Boss Stage: " + boss.getStage(), 20, 60);
        
        //Timer
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Time:" + minutes + ":" + seconds, 900,50);
        
        // if () {
            
        // }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_J:
                player.shoot(objects);
                break;
            case KeyEvent.VK_A:
                player.left = true; 
                if(!player.up){player.direction = Direction.LEFT;}
                break;
            case KeyEvent.VK_D:
                player.right = true; 
                if(!player.up){player.direction = Direction.RIGHT;}
                break;
            case KeyEvent.VK_W:
                player.direction = Direction.UP; 
                player.up = true;
                break;
            case KeyEvent.VK_S:
                player.down = true;
                break;
            case KeyEvent.VK_SPACE:
                player.jump = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.left = false;
                break;
            case KeyEvent.VK_D:
                player.right = false;
                break;
            case KeyEvent.VK_W:
                player.up = false;
                break;
            case KeyEvent.VK_S:
                player.down = false;
                player.y -= 20;
                break;
            case KeyEvent.VK_SPACE:
                player.jump = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {

        JFrame frame = new JFrame("Boss Fight");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BossFightGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
