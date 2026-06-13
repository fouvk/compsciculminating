import java.awt.*;
import java.util.List;

class BossBoomerang extends Attack {
    private double vx;
    private double vy;
    protected static final int SPEED = 5;
    private Player player;
    private boolean right;
    private boolean turn = false;

    public BossBoomerang(double x, double y, Player player) {
        super(6, false);
        
        
        this.x = x;
        this.y = y;
        this.player = player;
        
        width = 80;
        height = 40;
        
        if(x > player.x)
        {
            right = false;
            vx = -18;
        }
        else
        {
            right = true;
            vx = 18;
        }
        vy = -10;
    }

    @Override
    public void update(List<GameObject> objects) {
        if(right)
        {
            vx -= 0.3;
        }
        else
        {
            vx += 0.3;
        }
        if((player.x+player.width/2 > x && player.y+player.height/2 < y) || (player.x+player.width/2 <= x && player.y+player.height/2 < y && vy > -8))
        {
            vy -= 0.25;
        }
        else
        {
            vy += 0.25;
        }
        
        x += vx;
        y += vy;

        if (x < 0-width || x > GamePanel.WIDTH || y > GamePanel.HEIGHT) {
            alive = false;
        }
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillOval((int)x, (int)y, width, height);
    }
}
