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
    public static final int GROUND = 470;
    
    private Timer timer;
    private Player player;
    private Boss boss;
    private Image image;
    private long timeStart = 0;
    private long time = 0;
    private int seconds = 0;
    private int minutes = 0;
    public static boolean gameOver = true;
    private BossFightGame frame;
    public long deathTime;

    private List<GameObject> objects = new ArrayList<>();
    private List<GameObject> objectsToAdd = new ArrayList<>();

    public GamePanel(BossFightGame frame) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        loadBackground();
        setFocusable(true);
        addKeyListener(this);
        
        this.frame = frame;
        
        player = new Player(100, GROUND-70);
        boss = new Boss(WIDTH-Boss.WIDTH- 25, HEIGHT - Boss.HEIGHT - 200,player);
        
        objectsToAdd.add(boss);
        objectsToAdd.add(new Platform(300, 350));
        objectsToAdd.add(new Platform(550, 350));
        objectsToAdd.add(new Platform(425, 200));

        objectsToAdd.add(player);
        
        timer = new Timer(16, this);
        timer.start();
        
        startTime();
        
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                player.left = false;
                player.right = false;
                player.up = false;
                // Don't reset 'down' directly without fixing the Y coordinate!
                if (player.down) {
                    player.down = false;
                    player.y -= Player.HEIGHT - Player.CROUCH_HEIGHT;
                }
                player.jump = false;
            }
        });
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

        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update(objectsToAdd);
        }
        objects.addAll(objectsToAdd);
        objectsToAdd.clear();
        objects.removeIf(o -> !o.isAlive());

        if(player.health < 0)
        {
            player.health = 0;
        }
        
        if(!gameOver)
        {
            timer();
            handleCollisions();

            if(player.getHealth() <= 0 || boss.getHealth() <= 0)
            {
                gameOver = true;
                deathTime = System.currentTimeMillis();
                
                //Change the player image here:
                if(boss.getHealth() <= 0 && player.getHealth() > 0) {
                    frame.saveScore(frame.getCurrentUser(), (minutes * 60) + seconds) ;
                }
            }
        }
        
        repaint();
        
        if(player.getHealth() <= 0 || boss.getHealth() <= 0)
            {
                
                if(System.currentTimeMillis() - deathTime >= 1000)
                {
                    timer.stop();
                    frame.showEnd(player.getHealth(), boss.getHealth(), boss.getStage(), minutes, seconds);
                }
                
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
                    if(attack instanceof BossBigLaser laser) {
                        attack.setAlive(true);
                    }
                    else {
                        attack.setAlive(false);
                    }
                }
            }
            
            if(obj instanceof Platform platform)
            { // if the player is holding down and jump at the same time, they sink through the first 10 pixels of the playforms top
                if (!(player.down && player.jump) && 
                    player.vy > 0 && 
                    player.x + player.width >= platform.x && 
                    player.x <= platform.x + platform.width) {
                    
                    double prevBottom = player.prevY + player.prevHeight;
                    double currentBottom = player.y + player.height;
                    
                    boolean wasAbove = prevBottom  <= platform.y + 1;
                    boolean isNowBelow = currentBottom >= platform.y;
                    
                    if (wasAbove && isNowBelow) {
                        player.grounded = true;
                        player.y = platform.y - player.height;
                        player.vy = 0; 
                    }
                }
            }  
        }
        
        if(player.y >= GROUND-player.height) { //ground
        
            player.y = GROUND - player.height;
            player.grounded = true;
        
            if(player.uncrouchLock <= 0) {
                player.vy = 0;
            }
        }
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
            case KeyEvent.VK_S:
                if (!player.down) {
                    player.down = true;
                }
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
                if (player.down) {
                    player.down = false;
                }
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
