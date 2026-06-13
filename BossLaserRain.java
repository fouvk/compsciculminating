import java.awt.*;
import java.util.Random;
import java.util.List;

class BossLaserRain extends Attack {
    private int speed = 5;

    public BossLaserRain() {
        super(4, false);
        
        
        width = 10;
        height = 50;
        x = (new Random()).nextInt(GamePanel.WIDTH-(0-width)) + (0-width);
        y = 0 - height;
        speed = (new Random()).nextInt(6) + 3;
    }

    @Override
    public void update(List<GameObject> objects) {
        y += speed;

        if (x < 0-width || x > GamePanel.WIDTH || y > GamePanel.HEIGHT) {
            alive = false;
        }
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect((int)x, (int)y, width, height);
    }
}
