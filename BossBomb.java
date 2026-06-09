import java.awt.*;
import java.util.List;

class BossBomb extends Attack {
    private double vx;
    private double vy;
    private int RADIUS = 12;
    public double h;
    public double r;
    public double a;
    public double g = 0.7;      // same gravity as update()
    public double t = 60.0;  // reach player in 60 frames;double 
    private double deltaX, deltaY;
    private double timeStart = 0;
    private boolean notExploded = false;
    
    public BossBomb(double x, double y, Player player) {
        super(10, false);
        
        cooldown = 1000;
        this.x = x;
        this.y = y;
        
        deltaX = player.x - x;
        deltaY = player.y - y;
        
        width = RADIUS*2;
        height = RADIUS*2;

        vx = deltaX / t;
        vy = (deltaY - 0.5 * g * t * t) / t;
        
    }

    @Override
    public void update(List<GameObject> objects) {
        x += vx;
        y += vy;

        if(y+ (RADIUS*2) >= GamePanel.HEIGHT)
        {
            if(!notExploded)
            {
                timeStart = System.currentTimeMillis();
            }
            notExploded = true;
            vy = 0;
            vx = 0;
            y= GamePanel.HEIGHT - (RADIUS*2);
            RADIUS = 30;
            width = 60;
            height = 60;
            if(System.currentTimeMillis() - timeStart >= 500)
            {
                alive = false;
                timeStart = System.currentTimeMillis();
            }
        }
        else
        {
            //vx = deltaX / t;
            vy += g;
            RADIUS = 12;
            width = 24;
            height = 24;
        }
        /*
        if(y+height >= GamePanel.HEIGHT) {
            vy *=-0.9;
        }*/
        if (x < 0-width || x > GamePanel.WIDTH) {
            alive = false;
        }
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval((int)x, (int)y, RADIUS*2, RADIUS*2);
    }
}