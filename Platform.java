import java.awt.*;
import java.util.List;
class Platform extends GameObject
{
    public Platform(int x, int y)
    {
        super(x, y, 150, 50);
    }
    
    @Override
    public void draw(Graphics g)
    {
        g.setColor(Color.GRAY);
        g.fillRect((int)x, (int)y, width, height);
    }
    
    @Override
    public void update(List<GameObject> objects){}
}
