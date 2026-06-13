import java.awt.*;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.IOException;
class Platform extends GameObject
{
    private Image platform;
    
    public Platform(int x, int y)
    {
        super(x, y, 150, 50);
        loadPlatform();
    }
    
    public void loadPlatform () {
        try {
            platform = ImageIO.read(getClass().getResource("/Images/Platform.png"));
            }
        catch (IOException e) {
            System.out.println("Could not find image");
            }
    }
    
    @Override
    public void draw(Graphics g)
    {
        g.drawImage(platform, (int)x, (int)y - 10, (int)width, (int)height, null);
            
    }
    
    @Override
    public void update(List<GameObject> objects){}
}
