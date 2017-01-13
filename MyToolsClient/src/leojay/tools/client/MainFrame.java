package leojay.tools.client;

import leojay.tools.client.panel.MainPanel;
import leojay.tools.client.panel.WebServicePanel;

import javax.swing.*;
import java.awt.*;

/**
 * <p>
 * time: 17/1/7__08:14
 *
 * @author leojay
 */
public class MainFrame extends JFrame {
    private MainPanel mainPanel;

    public MainFrame() throws HeadlessException {
        super("MyTools");
        init();
//        add(new MainPanel());
        add(new WebServicePanel());

    }

    private void init() {
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
