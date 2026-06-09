import java.awt.*;
import java.util.List;

class Bullet extends Attack {
    private double vx;
    private double vy;
    protected static final int RADIUS = 6;
    protected static final int SPEED = 10;

    public Bullet(double x, double y, Direction direction) {
        super(10, true);
        
        cooldown = 200;
        
        this.x = x;
        this.y = y;
        
        switch(direction) {
            case Direction.LEFT -> vx = -SPEED;
            case Direction.RIGHT -> vx = SPEED;
            case Direction.UP -> vy = -SPEED;
            case Direction.DOWN -> vy = SPEED;
        }
    }

    @Override
    public void update(List<GameObject> objects) {
        x += vx;
        y += vy;

        if (x < 0-width || x > GamePanel.WIDTH || y < 0-height || y > GamePanel.HEIGHT) {
            alive = false;
        }
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval((int)x, (int)y, RADIUS*2, RADIUS*2);
    }
}