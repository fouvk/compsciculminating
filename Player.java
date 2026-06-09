import java.awt.*;
import java.util.List;

enum Direction {
    UP,DOWN,LEFT,RIGHT;
}

class Player extends GameObject {

    public double vx;
    public double vy;
    
    protected double prevY;

    protected boolean left, right, up, down, jump;
    protected boolean grounded;
    
    protected Direction direction = Direction.RIGHT;
    protected Direction prevDirection = Direction.RIGHT;
    
    private int health = 100;
    private long lastShot;

    public Player(int x, int y) {
        super(x, y, 50, 70);
    }

    @Override
    public void update(List<GameObject> objects) {
        if (left) vx = -6;
        if (right) vx = 6;
        
        if (jump && !down && grounded) {
            vy = -14;
            grounded = false;
        }
        
        vy += 0.67;
        vx *= 0.8;
        
        prevY = y;
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
        if(down)
        {
            setSize(50, 30);
            y += 20;
        }
        else
        {
            setSize(50, 70);
        }

    }

    public void shoot(List<GameObject> objects) {
        if (System.currentTimeMillis() - lastShot > Bullet.cooldown) {
            objects.add(new Bullet(x + width/2 - Bullet.RADIUS, y + height/2 - Bullet.RADIUS, direction));

            lastShot = System.currentTimeMillis();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect((int)x, (int)y, width, height);
    }

    public void damage(int amount) {
        health -= amount;
    }

    public int getHealth() {
        return health;
    }
    
    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }
}