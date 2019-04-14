import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;


public class AmplitudeGraph extends JPanel
{

    protected void paintComponent(Graphics g)  {
         super.paintComponent(g);

         Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        int pointsToDraw=4000;
        double max= AudioFrame.sines[0];
        for(int i=1;i<pointsToDraw;i++)  if (max< AudioFrame.sines[i]) max= AudioFrame.sines[i];
        int border=10;
        int w = getWidth();
        int h = (2*border+(int)max);

        double xInc = 0.5;

        //Draw x and y axes
        g2.draw(new Line2D.Double(border, border, border, 2*(max+border)));
        g2.draw(new Line2D.Double(border, (h- AudioFrame.sines[0]), w-border, (h- AudioFrame.sines[0])));

        g2.setPaint(Color.red);

        for(int i = 0; i < pointsToDraw; i++) {
            double x = border + i*xInc;
            double y = (h- AudioFrame.sines[i]);
            g2.fill(new Ellipse2D.Double(x-2, y-2, 2, 2));
        }
   }
}

