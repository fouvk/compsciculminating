import java.awt.*;
import java.util.List;
import java.util.Random;
import java.awt.event.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

class Boss extends GameObject {
    public int health = 1000;
    private int stage = 1;
    private Color color = Color.BLUE;
    private Player player;
    protected static int WIDTH = 225;
    protected static int HEIGHT = 275;
    
    private Image boss1;
    private Image boss2;
    private Image boss3;
    private Image boss4;
    private Image boss5;
    private Image boss6;
    private Image boss;
    
    private Image boss1Left;
    private Image boss2Left;
    private Image boss3Left;
    private Image boss4Left;
    
    int counter = 0;
    double ox,oy;
    
    private int raincd = 250;
    
    private boolean isFlying = false;
    private boolean flyingRight = false;
    private boolean facingLeft = true;
    
    private boolean isLaserRaining = false;
    private long lastRainSpawn = 0;
    private int rainCount = 0;

    private long attackTimer = System.currentTimeMillis();
    private int cooldown = 1000;

    public Boss(int x, int y, Player player) {
        super(x, y, WIDTH, HEIGHT);
        this.player = player;
        
        loadBossImages();
        boss = boss1;
    }

    @Override
    public void update(List<GameObject> objects) {
        updateBossSprite();
        if (System.currentTimeMillis() - attackTimer > cooldown) {

            switch(stage)
            {
                case 1:
                    chooseAttack(objects, new int[]{0,0,0,1,1});
                    break;
                case 2:
                    chooseAttack(objects, new int[]{0,1,2,3,3,7});
                    break;
                case 3:
                    chooseAttack(objects, new int[]{4,4,4,5,6,6,6,7,7});
                    break;
                case 4:
                    chooseAttack(objects, new int[]{3,3,4,4,5,6,6,7,7});
                    break;
            }

            attackTimer = System.currentTimeMillis();
        }
        
        if (isFlying) {
            if (flyingRight && x > 25) {
                x -= 5;
            } else if (!flyingRight && x+width < GamePanel.WIDTH - 25) {
                x += 5;
            } else {
                isFlying = false;
                facingLeft = !flyingRight;
            }
        }
        
        if (isLaserRaining) {
            if (System.currentTimeMillis() - lastRainSpawn > raincd) {
                if (rainCount >= (cooldown - raincd*2) / raincd) { 
                    isLaserRaining = false;
                    x = ox;
                    y = oy;
                    width /=3;
                    height = HEIGHT;
                } else {
                    objects.add(new BossLaserRain());
                    lastRainSpawn = System.currentTimeMillis();
                    rainCount++;
                }
            }
        }
    }
    
    public void loadBossImages() {
        try {
            boss1 = ImageIO.read(getClass().getResource("/BossImages/Boss1.png"));
            boss2 = ImageIO.read(getClass().getResource("/BossImages/Boss2.png"));
            boss3 = ImageIO.read(getClass().getResource("/BossImages/Boss3.png"));
            boss4 = ImageIO.read(getClass().getResource("/BossImages/Boss4.png"));
            boss5 = ImageIO.read(getClass().getResource("/BossImages/Boss5.png"));
            boss6 = ImageIO.read(getClass().getResource("/BossImages/Boss6.png"));
            
            boss1Left = flipImage(boss1);
            boss2Left = flipImage(boss2);
            boss3Left = flipImage(boss3);
            boss4Left = flipImage(boss4);
            
            }
        catch (IOException e) {
            System.out.println("Could not find image");
            }        
    }

    private void updateStage() {
        if ((stage == 3) && (health <= 250)) {
            stage = 4;
        }
        else if (stage == 2 && health <= 500) {
            stage = 3;
        }
        else if (stage == 1 && health <= 750) {
            stage = 2;
        }
    }
    
    private void updateBossSprite () {
        if (isLaserRaining) {
            if (stage == 4) {
            boss = boss6;
            } else boss = boss5;
        } else if (isFlying){
            if (flyingRight) {
                switch(stage) {
                    case 1: boss = boss1; break;
                    case 2: boss = boss2; break;
                    case 3: boss = boss3; break;
                    case 4: boss = boss4; break;
                    }
                } else {
                switch(stage) {
                    case 1: boss = boss1Left; break;
                    case 2: boss = boss2Left; break;
                    case 3: boss = boss3Left; break;
                    case 4: boss = boss4Left; break;
                    }  
                }
            } else {
                if (facingLeft) {
                    switch(stage) {
                        case 1: boss = boss1; break;
                        case 2: boss = boss2; break;
                        case 3: boss = boss3; break;
                        case 4: boss = boss4; break;
                    }
                } else {
                    switch(stage) {
                        case 1: boss = boss1Left; break;
                        case 2: boss = boss2Left; break;
                        case 3: boss = boss3Left; break;
                        case 4: boss = boss4Left; break;
                }
            }
        }
    }

     private Image flipImage (Image img) {
        BufferedImage buffered = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = buffered.createGraphics();
        g2d.drawImage(img, img.getWidth(null), 0, -img.getWidth(null), img.getHeight(null), null);
        
        g2d.dispose();
        return buffered;
     }
    
    @Override
    public void draw(Graphics g) {
        
        if(boss != null) {
            g.drawImage(boss, (int)x, (int)y, (int)width, (int)height, null );
        }
    }

    public void damage(int amount) {
        if(!isLaserRaining) {
            health -= amount;
        }
        
        updateStage();
        if (health <= 0) {
            alive = false;
        }
    }

    public int getHealth() {
        return health;
    }

    public int getStage() {
        return stage;
    }


    
    
    void chooseAttack(List<GameObject> objects, int[] pool) {
        int atk = pool[new Random().nextInt(pool.length)];
        
        switch(atk) {
            case 0: //bullet
                objects.add(new BossBullet(x+width/2,y+height/2, player));
                cooldown = 700;
                break;
            case 1: //bomb
                objects.add(new BossBomb(x+width/2, y+height/2, player));
                cooldown = 1200;
                break;
            case 2: //boomerang
                objects.add(new BossBoomerang(x+width/2, y+height/2, player));
                cooldown = 1500;
                break;
            case 3: //fly
                if(isFlying || isLaserRaining) {
                    cooldown = 100;
                    break;
                }
                isFlying = true;
                flyingRight = (x >= GamePanel.WIDTH/2);
                cooldown = 1000;
                if(stage == 4) {
                    cooldown *= 0.5;
                }
                break;
            case 4://big laser
                if(isFlying || isLaserRaining) {
                    cooldown = 100;
                    break;
                }
                cooldown = 2500;
                if(stage == 4) {
                    cooldown *= 0.75;
                }
                objects.add(new BossBigLaser(x+width/2,y+height/2, player, cooldown));
                break;
            case 5://laser rain
                if(isFlying || isLaserRaining) {
                    cooldown = 100; 
                    break;
                }
                isLaserRaining = true;
                rainCount = 0;
                ox = x;
                oy = y;
                width *=3;
                height = 300;
                cooldown = 8000;
                if(stage == 4) {
                    raincd = 200;
                }
                x = GamePanel.WIDTH/2 - width/2;
                y = GamePanel.HEIGHT/2 - HEIGHT/2 -50;
                break;
            case 6://triple attack
                objects.add(new BossBullet(x+width/2,y+height/2, player));
                objects.add(new BossBomb(x+width/2, y+height/2, player));
                objects.add(new BossBoomerang(x+width/2, y+height/2, player));
                cooldown = 1600;
                if(stage == 4) {
                    cooldown *= 0.75;
                }
                break;
            case 7: //teleport up/down
                if(isFlying || isLaserRaining) {
                    cooldown = 100;
                    break;
                }
                y = (new Random()).nextInt((GamePanel.GROUND-height)-0) + 0;
                cooldown = 500;
                if(stage == 4) {
                    cooldown *= 0.75;
                }
                break;
            default: break;
            
        }
    }
}
