import java.awt.*;
import java.util.List;
import java.util.Random;

class BossBomb extends Attack {
    private double vx;
    private double vy;
    private int RADIUS = 12;
    public double h;
    public double r;
    public double a;
    public double g = 0.6;      // same gravity as update()
    public double t = 60.0;  // reach player in 60 frames;double 
    private double deltaX, deltaY;
    private double timeStart = 0;
    private boolean notExploded = false;
    
    public BossBomb(double x, double y, Player player) {
        super(6, false);
        
        
        this.x = x;
        this.y = y;
        
        deltaX = player.x + ((new Random()).nextInt(100) - 50) - x;
        deltaY = player.y + ((new Random()).nextInt(100) - 50) - y;
        
        width = RADIUS*2;
        height = RADIUS*2;

        vx = deltaX / t;
        vy = (deltaY - 0.5 * g * t * t) / t;
        
    }

    @Override
    public void update(List<GameObject> objects) {
        x += vx;
        y += vy;

        if(y+ (RADIUS*2) >= GamePanel.GROUND)
        {
            if(!notExploded)
            {
                timeStart = System.currentTimeMillis();
            }
            notExploded = true;
            vy = 0;
            vx = 0;
            y= GamePanel.GROUND - (RADIUS*2);
            RADIUS = 40;
            width = 80;
            height = 80;
            if(System.currentTimeMillis() - timeStart >= 250)
            {
                alive = false;
                timeStart = System.currentTimeMillis();
            }
        }
        else
        {
            //vx = deltaX / t;
            vy += g;
            RADIUS = 15;
            width = 30;
            height = 30;
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
        g.setColor(Color.ORANGE);
        g.fillOval((int)x, (int)y, RADIUS*2, RADIUS*2);
    }
}
