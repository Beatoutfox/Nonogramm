/*
Brustur-Buksa Beatrice
521/2
* */
import javax.swing.*;
import java.awt.*;

public class BackgroundImg{
    private Image backgroundImage;
    private Color gradientStartColor;
    private Color gradientEndColor;

    public BackgroundImg(String imagePath, Color gradientStart, Color gradientEnd) {
        this.backgroundImage = new ImageIcon(imagePath).getImage();
        this.gradientStartColor = gradientStart;
        this.gradientEndColor = gradientEnd;
    }

    public void paint(Graphics g, JPanel panel) {
        g.drawImage(backgroundImage, 0, 0, panel.getWidth(), panel.getHeight(), panel);

        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(0, 0, gradientStartColor, 0, panel.getHeight(), gradientEndColor);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, panel.getWidth(), panel.getHeight());
    }
}
