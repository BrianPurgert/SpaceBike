import java.io.FileInputStream;
import at.emini.physics2D.World;
import at.emini.physics2D.util.PhysicsFileReader;

public class PhyTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Testing level0.phy");
        PhysicsFileReader reader0 = new PhysicsFileReader(new FileInputStream("src/main/res/raw/level0.phy"));
        World.loadWorld(reader0);
        System.out.println("Success level0");

        System.out.println("Testing level.phy");
        PhysicsFileReader reader = new PhysicsFileReader(new FileInputStream("src/main/res/raw/level.phy"));
        World.loadWorld(reader);
        System.out.println("Success level");
    }
}