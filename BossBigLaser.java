import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;


class BossBigLaser extends Attack {
    private boolean state = false;
    double time;
    int cooldown;
    
    public BossBigLaser(double x, double y, Player player, int cooldown) {
        super(0, false);
        this.x = x;
        this.y = y;
        this.cooldown = cooldown;
        
        width = GamePanel.WIDTH;
        this.x = 0;
        
        height = 200;
        this.y -= height/2;
        
        
        
        time = System.currentTimeMillis();
    }
    
    @Override
    public void update(List<GameObject> objects) {
        
        if(System.currentTimeMillis() > time + cooldown) {
            alive = false;
        }
        else if (System.currentTimeMillis() > time + (cooldown/3)*2){
            state = true;
            damage = 2;
        }
        else {
            state = false;
            damage = 0;
        }
    }
    
    @Override
    public void draw(Graphics g) {
        if(state) {
            g.setColor(Color.CYAN);
        } else {
            g.setColor(new Color(255, 0, 0, 100));
        }
        g.fillRect((int)x, (int)y, width, height);
    }
}
