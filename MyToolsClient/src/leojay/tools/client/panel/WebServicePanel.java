package leojay.tools.client.panel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Set;

/**
 * <p>
 * time: 17/1/7__14:53
 *
 * @author leojay
 */
public class WebServicePanel extends JPanel {
    private int width;
    private int height;

    public WebServicePanel() {
        super();
        setLayout(new FlowLayout(FlowLayout.LEFT));
        width = getWidth();
        height = getHeight();
        add(getPutMsg());
        add(getWSOP());
    }

    private PutMassage getPutMsg() {
        PutMassage putMassage = new PutMassage();
        putMassage.setBounds(0, 0, width, 50);
//        putMassage.setBackground(Color.BLUE);
        return putMassage;
    }

    private WebServiceOperationPanel getWSOP() {
        WebServiceOperationPanel wsopp = new WebServiceOperationPanel();
        wsopp.setBounds(0, 0, width, 150);

        return wsopp;
    }

    private class PutMassage extends JPanel {
        JTextField textField;
        JButton button;

        public PutMassage() {
            add(new JLabel("输入WSDL："));
            textField = getTextField();
            add(textField);
            button = getButton();
            add(button);
        }

        private JTextField getTextField() {
            JTextField textField = new JTextField(50);
            return textField;
        }

        private JButton getButton() {
            JButton jButton = new JButton("点击");
            return jButton;
        }
    }

    private class WebServiceOperationPanel extends JPanel {
        public WebServiceOperationPanel() {
            super(new FlowLayout(FlowLayout.LEFT));
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("h", "haha");
            data.put("a", "ahaha");
            JPanel messagePanel = createMessagePanel(data);
            JPanel testPanel = createTestPanel();

            add(messagePanel);
            add(testPanel);
        }

        private JPanel createMessagePanel(HashMap<String, String> data) {
            JPanel panel = new JPanel(new GridLayout());
            Set<String> strings = data.keySet();
            int i = 0;
            for (String item : strings) {
                JLabel label = new JLabel(item);
                add(label, i);
                label = new JLabel(data.get(item));
                add(label, i);
                i++;
            }

            return panel;
        }

        private JPanel createTestPanel() {
            JPanel panel = new JPanel();

            return panel;
        }

    }
}
