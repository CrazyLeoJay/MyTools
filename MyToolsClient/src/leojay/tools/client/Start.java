package leojay.tools.client;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>
 * time: 17/1/5__15:18
 *
 * @author leojay
 */
public class Start {
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });


    }
}
