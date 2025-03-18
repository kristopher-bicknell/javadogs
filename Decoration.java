import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Decoration {
    public BufferedImage[] wall, floor, window, wallDecor, floorDecor;
    public int[] currDecor = {0,0,0,0,0};

    GamePanel gp;

    public Decoration(GamePanel gp) {
        this.gp = gp;
        getDecorImage();

    }

    public void getDecorImage() {
        wall = new BufferedImage[4];
        floor = new BufferedImage[3]; //TODO: this better
        window = new BufferedImage[3];
        wallDecor = new BufferedImage[4];
        floorDecor = new BufferedImage[3];
        try {
            wall[0] = ImageIO.read(new File("src/decoration/wall1.png"));
            wall[1] = ImageIO.read(new File("src/decoration/wall2.png"));
            wall[2] = ImageIO.read(new File("src/decoration/wall3.png"));
            wall[3] = ImageIO.read(new File("src/decoration/wall4.png"));

            floor[0] = ImageIO.read(new File("src/decoration/floor1.png"));
            floor[1] = ImageIO.read(new File("src/decoration/floor2.png"));
            floor[2] = ImageIO.read(new File("src/decoration/floor3.png"));

            window[0] = ImageIO.read(new File("src/decoration/window1.png"));
            window[1] = ImageIO.read(new File("src/decoration/window2.png"));
            window[2] = ImageIO.read(new File("src/decoration/window3.png"));

            wallDecor[0] = ImageIO.read(new File("src/decoration/walldecor1.png"));
            wallDecor[1] = ImageIO.read(new File("src/decoration/walldecor2.png"));
            wallDecor[2] = ImageIO.read(new File("src/decoration/walldecor3.png"));
            wallDecor[3] = ImageIO.read(new File("src/decoration/walldecor4.png"));

            floorDecor[0] = ImageIO.read(new File("src/decoration/floordecor1.png"));
            floorDecor[1] = ImageIO.read(new File("src/decoration/floordecor2.png"));
            floorDecor[2] = ImageIO.read(new File("src/decoration/floordecor3.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDecoration(BufferedImage[] decoration, int decorType) {
        if (decoration.length-1 == currDecor[decorType]) {
            currDecor[decorType] = 0;
        } else {
            currDecor[decorType]++;
        }
    }

    public void updateDecoration(int decorType) {
        switch (decorType) {
            case 0: //wall
                updateDecoration(wall,decorType);
                break;
            case 1: //floor
                updateDecoration(floor,decorType);
                break;
            case 2: //window
                updateDecoration(window,decorType);
                break;
            case 3: //wall decor
                updateDecoration(wallDecor,decorType);
                break;
            case 4: //floor decor
                updateDecoration(floorDecor,decorType);
                break;
        }
    }

    public void update() {

    }

    public void draw(Graphics2D g2d) {
        BufferedImage image = null;
        image = floor[currDecor[1]];
        g2d.drawImage(image, 0, 0,null);

        image = wall[currDecor[0]];
        g2d.drawImage(image, 0, 0,null);

        image = window[currDecor[2]];
        g2d.drawImage(image, 50,100,null);

        image = wallDecor[currDecor[3]];
        g2d.drawImage(image,340,60,null);

        image = floorDecor[currDecor[4]];
        g2d.drawImage(image, 500, 50, null);
    }
}
