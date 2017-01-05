package leojay.tools.applet;

import java.applet.Applet;
import java.awt.*;

/**
 * <p>
 * time: 16/12/19__19:37
 *
 * @author leojay
 */
public class MyApplet extends Applet {
    int x, y ,sum;
    @Override
    public void init() {
        super.init();
        String s1 = getParameter("girl");
        String s2 = getParameter("boy");
        x = Integer.parseInt(s1);
        y = Integer.parseInt(s2);
        sum = x + y;

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawString("sum = " + sum, 90, 120);
    }
}
