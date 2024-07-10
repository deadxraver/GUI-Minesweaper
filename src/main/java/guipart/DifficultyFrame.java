package guipart;

import enums.Size;

import javax.swing.*;
import java.awt.*;

public class DifficultyFrame extends JFrame {

    private final JLabel label = new JLabel("Choose field size: ");
    private final JComboBox<Size> sizeBox = new JComboBox<>(new Size[]{Size.SMALL, Size.MEDIUM, Size.LARGE});
    private final JButton button = new JButton("Play");
    private Size option = Size.SMALL;

    public DifficultyFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(label, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(sizeBox, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(button, gbc);

        processBoxAndButton();

        pack();
        setBounds(0, 0, getWidth() + 50, getHeight() + 50);
    }

    private void processBoxAndButton() {
        sizeBox.setSelectedItem(Size.SMALL);
        sizeBox.setToolTipText("select field size");
        sizeBox.addActionListener(l -> option = (Size) sizeBox.getSelectedItem());

        button.addActionListener(l -> {
            setVisible(false);
            new MainFrame(option).setVisible(true);
        });
    }
}
