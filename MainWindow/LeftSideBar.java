package MainWindow;

import javax.swing.*;
import java.awt.*;

public class LeftSideBar extends JPanel {

    public LeftSideBar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.LIGHT_GRAY);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton button1 = new JButton("1");
        panel.add(button1);



        add(panel);
    }
}
