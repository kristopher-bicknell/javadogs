import java.awt.*;

public final class DialogDraw {
    public DialogDraw() {
        //there is nothing to be constructed...
    }

    public static void drawDialogBox(Graphics2D g2d, String dialog) {
        g2d.setColor(new Color(0,0,0,180));
        g2d.fillRoundRect(5,400,790,195,35,35);
        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(5,400,790,195,35,35);

        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
        int coordDialogX = 15;

        //draw string
        int numLines = dialog.split("\n").length; //get number of lines to be drawn
        int dialogHeight = (int)g2d.getFontMetrics().getStringBounds(dialog,g2d).getHeight(); //get height of each line
        int coordDialogY = 525-(int)((0.5*dialogHeight)*numLines);
        g2d.setColor(Color.white);
        for (String line : dialog.split("\n")) {
            for (String coloredLine : line.split("#")) {
                if (coloredLine.length() > 0 && coloredLine.charAt(0) == '!') { //something about String.split gives this an empty array sometimes, so the first check prevents a crash
                    String[] color = coloredLine.split(" ");
                    switch (color[0]) {
                        case "!red":
                            g2d.setColor(Color.red);
                            coloredLine = coloredLine.replaceFirst("!red ", "");
                            break;
                        case "!blue":
                            g2d.setColor(Color.blue);
                            coloredLine = coloredLine.replaceFirst("!blue ", "");
                            break;
                        case "!green":
                            g2d.setColor(Color.green);
                            coloredLine = coloredLine.replaceFirst("!green ", "");
                            break;
                    }
                }
                g2d.drawString(coloredLine,coordDialogX,coordDialogY);
                g2d.setColor(Color.white);
                coordDialogX += (int)g2d.getFontMetrics().getStringBounds(coloredLine,g2d).getWidth();
            }
            coordDialogY += dialogHeight;
            coordDialogX = 15;
        }
        g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        coordDialogX = 400-(int)(0.5*g2d.getFontMetrics().getStringBounds("(Enter)",g2d).getWidth());
        g2d.setColor(new Color(200,200,200));
        g2d.drawString("(Enter)", coordDialogX, 575);
    }
}
