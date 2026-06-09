import java.awt.*;
import java.util.List;

class BossBullet extends Attack {
    private double vx;
    private double vy;
    protected static final int RADIUS = 6;
    protected static final int SPEED = 5;

    public BossBullet(double x, double y, Player player) {
        super(5, false);
        
        cooldown = 500;
        this.x = x;
        this.y = y;
        
        double t = Math.sqrt(Math.pow(x-(player.x+player.width/2),2) + Math.pow(y-(player.y+player.height/2),2));
        vx = SPEED * -((x-(player.x+player.width/2)) / t);
        vy = SPEED * -((y-(player.y+player.height/2)) / t);
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
        g.setColor(Color.RED);
        g.fillOval((int)x, (int)y, RADIUS*2, RADIUS*2);
    }
}