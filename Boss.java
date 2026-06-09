import java.awt.*;
import java.util.List;
import java.util.Random;

class Boss extends GameObject {
    private int health = 300;
    private int stage = 1;
    private Color color = Color.RED;
    private Player player;

    private long attackTimer = System.currentTimeMillis();
    private int cooldown = 1000;

    public Boss(int x, int y, Player player) {
        super(x, y, 160, 250);
        this.player = player;
    }

    @Override
    public void update(List<GameObject> objects) {

        if (System.currentTimeMillis() - attackTimer > cooldown) {

            switch(stage)
            {
                case 1:
                    chooseAttack(objects, new int[]{0,1});
                    
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }

            attackTimer = System.currentTimeMillis();
        }
    }

    private void updateStage() {
        if ((stage == 2) && (health <= 100)) {
            stage = 3;
            color = Color.MAGENTA;
        }
        else if (stage == 1 && health <= 200) {
            stage = 2;
            color = Color.ORANGE;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect((int)x, (int)y, width, height);
    }

    public void damage(int amount) {
        health -= amount;

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
            case 0: 
                objects.add(new BossBullet(x+width/2,y+height/2, player));
                cooldown = BossBullet.cooldown;
                break;
            case 1: 
                objects.add(new BossBomb(x+width/2, y+height/2, player));
                cooldown = BossBomb.cooldown;
                break;
            case 4:
                
                break;
            //bullet
            //boss
            //big laser
            //laser rain
            //fly
            //boomerang
            default: break;
        }
    }
}