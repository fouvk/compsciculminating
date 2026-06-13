import java.awt.*;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

enum Direction {
    UP,DOWN,LEFT,RIGHT;
}

class Player extends GameObject {

    public double vx;
    public double vy;
    
    protected double prevY;
    protected double prevHeight;

    protected boolean left, right, up, down, jump;
    protected boolean grounded;
    
    protected Direction direction = Direction.RIGHT;
    protected Direction prevDirection = Direction.RIGHT;
    
    public int health = 100;
    private long lastShot;
    
    private PlayerState state = PlayerState.IDLE;
    
    private int runFrame = 0;
    private long lastRunFrame = 0;
    
    protected int uncrouchLock = 0;
    
    private long shootEnd = 0;
    private long hurtEnd = 0;
    
    private Image playerIdle;
    private Image playerRun1;
    private Image playerRun2;
    private Image playerJump;
    private Image playerFall;
    private Image playerHurt;
    private Image playerShoot;
    private Image playerCrouch;
    private Image playerIdleLeft;
    private Image playerRun1Left;
    private Image playerRun2Left;
    private Image playerJumpLeft;
    private Image playerFallLeft;
    private Image playerHurtLeft;
    private Image playerShootLeft;
    private Image playerCrouchLeft;
    
    private Image current;
    
    protected static final int WIDTH = 50;
    protected static final int HEIGHT = 150;
    protected static final int CROUCH_HEIGHT = 75;

    public Player(int x, int y) {
        super(x, y, 0, 0);
        width = WIDTH;
        height = HEIGHT;
        
        loadPlayerImages();
    }
    
    enum PlayerState {
        IDLE,
        RUNNING,
        JUMPING,
        FALLING,
        CROUCHING,
        SHOOTING,
        HURT,
        SHOOTUP
    }

    @Override
    public void update(List<GameObject> objects) {
        
        if (uncrouchLock > 0) {
            uncrouchLock --;
        }
        
        //updating player state
        if (state == PlayerState.SHOOTING && System.currentTimeMillis() > shootEnd) {
                state = PlayerState.IDLE;
        }
        if (state == PlayerState.HURT && System.currentTimeMillis() > hurtEnd) {
            state = PlayerState.IDLE;
        }
        
        if (left) vx = -6;
        if (right) vx = 6;
        
        if (jump && !down && grounded) {
            vy = -16;
            grounded = false;
        }
        if (state != PlayerState.SHOOTING && state != PlayerState.HURT) {
            if (!grounded && vy > 0) {
                state = PlayerState.FALLING;
            }
            else if (!grounded  && vy < 0) {
                state = PlayerState.JUMPING;
            } 
            else if (Math.abs(vx) > 1) {
                state = PlayerState.RUNNING;
            }
            else state = PlayerState.IDLE;
        }
        vy += 0.67;
        vx *= 0.8;
        
        prevY = y;
        prevHeight = height;
        
        y += vy;
        x += vx;
        
        if (x <= 0) {
            x = 0;
        }
        else if (x >= 1000-width)
        {
            x = 1000-width;
        }
        
        //crouch mechanic
        if(down && grounded)
        {
            state = PlayerState.CROUCHING;
            
            if (height != CROUCH_HEIGHT) {
                resizeHeight(CROUCH_HEIGHT);
            }
        }
        else
        {
            if (!down && grounded) {
                if (height != HEIGHT ) {
                    resizeHeight(HEIGHT);
                }
            }
        }

            
            if (Math.abs(vx) > 1 && grounded) {
                if (System.currentTimeMillis() - lastRunFrame > 100) {
                    runFrame = (runFrame + 1) % 2;
                    lastRunFrame = System.currentTimeMillis();
                }
            }
    }

    public void shoot(List<GameObject> objects) {
        if (System.currentTimeMillis() - lastShot > 200) {
            objects.add(new Bullet(x + width/2 - Bullet.RADIUS, y + height/2 - Bullet.RADIUS, direction));

            lastShot = System.currentTimeMillis();
            
            state = PlayerState.SHOOTING;
            shootEnd = System.currentTimeMillis() + 150;
        }
    }
    
    public void loadPlayerImages()
    {
        try {
            playerIdle = ImageIO.read(getClass().getResource("/ImagesPlayer/PlayerIdle.png"));
            playerShoot = ImageIO.read(getClass().getResource("/ImagesPlayer/PlayerShoot.png"));
            playerCrouch = ImageIO.read(getClass().getResource("/ImagesPlayer/PlayerCrouch.png"));
            playerFall = ImageIO.read(getClass().getResource("/ImagesPlayer/PlayerFall.png"));
            playerHurt = ImageIO.read(getClass().getResource("/ImagesPlayer/PlayerHurt.png"));
            playerRun1 = ImageIO.read(getClass().getResource("/ImagesPlayer/PlayerSprint.png"));
            playerRun2 = ImageIO.read(getClass().getResource("/ImagesPlayer/PlayerSprint1.png"));
            playerJump = ImageIO.read(getClass().getResource("/ImagesPlayer/PlayerJump.png"));
            
            //left facing images
            playerIdleLeft = flipImage(playerIdle);
            playerShootLeft = flipImage(playerShoot);
            playerCrouchLeft = flipImage(playerCrouch);
            playerFallLeft = flipImage(playerFall);
            playerHurtLeft = flipImage(playerHurt);
            playerRun1Left = flipImage(playerRun1);
            playerRun2Left = flipImage(playerRun2);
            playerJumpLeft = flipImage(playerJump);
        } catch (IOException e) {
            System.out.println("Image not found");
        }
    }

    @Override
    public void draw(Graphics g) {
            
            if (direction == Direction.LEFT) {
                switch(state) {
                    case RUNNING:
                        current = (runFrame == 0) ? playerRun1Left: 
                        playerRun2Left;
                        break;
                    case JUMPING:
                        current = playerJumpLeft;
                        break;
                    case FALLING:
                        current = playerFallLeft;
                        break;
                    case CROUCHING:
                        current = playerCrouchLeft;
                        break;
                    case SHOOTING:
                        current = playerShootLeft;
                        break;
                    case HURT:
                        current = playerHurtLeft;
                        break;
                    default:
                        current = playerIdleLeft;
                } 
            } else {
                switch(state) {
                    case RUNNING:
                        current = (runFrame == 0) ? playerRun1: 
                        playerRun2;
                        break;
                    case JUMPING:
                        current = playerJump;
                        break;
                    case FALLING:
                        current = playerFall;
                        break;
                    case CROUCHING:
                        current = playerCrouch;
                        break;
                    case SHOOTING:
                        current = playerShoot;
                        break;
                    case HURT:
                        current = playerHurt;
                        break;
                    default:
                        current = playerIdle;
                }   
            }
            
            int drawX = (int)x - 25;
            int drawWidth = (int)width + 50;
            
            if (state == PlayerState.CROUCHING) {
                g.drawImage(current, drawX, (int)y - 40, drawWidth, HEIGHT, null);
            } else 
                g.drawImage(current, drawX, (int)y, drawWidth, (int)height, null);
            
    }

    private void resizeHeight(int newHeight) {
        
        double oldHeight = this.height;
        double deltaHeight = oldHeight - newHeight;
        
        this.y += deltaHeight;
        this.height = newHeight;
        
        if (newHeight == CROUCH_HEIGHT) {
            this.vy = 0;
            this.uncrouchLock = 2;
        } else {
        this.vy = 0.1;
        this.uncrouchLock = 5;
        }
    }
    
    private Image flipImage (Image img) {
        BufferedImage buffered = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = buffered.createGraphics();
        g2d.drawImage(img, img.getWidth(null), 0, -img.getWidth(null), img.getHeight(null), null);
        
        g2d.dispose();
        return buffered;
    }
    
    public void damage(int amount) {
        
        if  (System.currentTimeMillis() < hurtEnd) {
            return;
        }
        
        health -= amount;
        state = PlayerState.HURT;
        
        hurtEnd = System.currentTimeMillis() + 400;
    }

    public int getHealth() {
        return health;
    }
    
}
