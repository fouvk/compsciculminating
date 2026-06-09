import java.awt.*;
import java.util.List;

abstract class GameObject {

    protected double x, y;
    protected int width;
    protected int height;
    protected boolean alive = true;
    
    public GameObject() {
        this.x = 0;
        this.y = 0;
        this.width = 20;
        this.height = 20;
    }
    
    public GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update(List<GameObject> objects);
    public abstract void draw(Graphics g);

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}