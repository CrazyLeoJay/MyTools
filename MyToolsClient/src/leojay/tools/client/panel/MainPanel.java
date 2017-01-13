package leojay.tools.client.panel;

import javax.swing.*;
import java.awt.*;

/**
 * <p>
 * time: 17/1/5__15:19
 *
 * @author leojay
 */
public class MainPanel extends JPanel {
    public MainPanel() {
        super(new FlowLayout(FlowLayout.CENTER));
        init();
        FlowLayout layout = (FlowLayout) getLayout();
        initExample();
    }

    private void init() {

    }

    private void initExample() {
        add(new JButton("我是JButton"));
        add(new JToggleButton("我是JToggleButton"));
        add(new JLabel("我是JLabel"));
        add(new JCheckBox("我是JCheckBox"));
        add(new JRadioButton("我是JRadioButton"));
        add(new JTextField("我是JTextField"));
        add(new JPasswordField("我是JPasswordField"));
        add(new JTextArea("我是JTextArea"));

    }
}
