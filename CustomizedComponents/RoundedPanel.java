package CustomizedComponents;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.AbstractBorder;

public class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Color backgroundColor;
    private Color foregroundColor;

    public RoundedPanel(int cornerRadius, Color backgroundColor, Color foregroundColor) {
        this.cornerRadius = cornerRadius;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        setOpaque(false);

        setBorder(new RoundedBorder(cornerRadius, foregroundColor));

        // addMouseListener(new MouseAdapter() {
        //     public void mouseEntered(MouseEvent e){
        //         setBorder(new RoundedBorder(cornerRadius,Color.black));
        //     }
        //     public void mouseExited(MouseEvent e){
        //         setBorder(new RoundedBorder(cornerRadius,Color.gray));  
        //     }
        // });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius * 2, cornerRadius * 2);
    }

    private class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius * 2, radius * 2);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            int offset = radius / 2;
            return new Insets(offset, offset, offset, offset);
        }
    }
}
