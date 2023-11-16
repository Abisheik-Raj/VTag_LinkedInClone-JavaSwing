package CustomizedComponents;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.AbstractBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {
    private int cornerRadius;
    private Color backgroundColor;
    private Color foregroundColor;

    public RoundedButton(String text, int cornerRadius, Color backgroundColor, Color foregroundColor) {
        super(text);
        this.cornerRadius = cornerRadius;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;

        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(foregroundColor);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setBorder(new RoundedBorder(cornerRadius, new Color(10, 102, 194)));
            }
        });

        setBorder(new RoundedBorder(cornerRadius, foregroundColor));

        // Add a MouseListener to handle mouse events
        // addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseEntered(MouseEvent e) {
        //         setBackground(new Color(255, 0, 0)); // Change the background color when mouse enters
        //     }

        //     @Override
        //     public void mouseExited(MouseEvent e) {
        //         setBackground(backgroundColor); // Restore the original background color when mouse exits
        //     }

        //     @Override
        //     public void mouseClicked(MouseEvent e) {
        //         setBackground(new Color(0, 255, 0)); // Change the background color when clicked
        //     }
        // });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius * 2, cornerRadius * 2);
        super.paintComponent(g);
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


