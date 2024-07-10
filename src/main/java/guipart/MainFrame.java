package guipart;

import classes.GameField;
import classes.WorkWithInput;
import enums.Size;
import exceptions.InputException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

	private GameField gameField;
	private CellButton[][] buttonField;

	public MainFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.gameField = new GameField(Size.SMALL);
		setLayout(new GridBagLayout());
		drawField();

		pack();
		setBounds(0, 0, getWidth() + 50, getHeight() + 50);
	}

	private void drawField() {
		GridBagConstraints gbc = new GridBagConstraints();
		buttonField = new CellButton[gameField.getField().length - 2][gameField.getField()[0].length - 2];
		for (int gridY = 0; gridY < buttonField.length; gridY++) {
			for (int gridX = 0; gridX < buttonField[0].length; gridX++) {
				gbc.gridx = gridX;
				gbc.gridy = gridY;
				CellButton button = processButton(gridX, gridY);
				buttonField[gridY][gridX] = button;
				add(button, gbc);
			}
		}
	}

	private CellButton processButton(int gridX, int gridY) {
		CellButton button = new CellButton("?", gridX, gridY);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String inp = "" + (char)(button.gridX + 'A') + (button.gridY + 1) + (SwingUtilities.isRightMouseButton(e) ? "f" : "");
				try {
					gameField.move(WorkWithInput.convert(inp));
				} catch (InputException ex) {
					System.err.println(ex.getMessage());
				}
				gameField.removeZeros();
				refreshField();
				super.mouseClicked(e);
			}
		});
		return button;
	}

	private void refreshField() {
		for (int i = 0; i < buttonField.length; i++) {
			for (int j = 0; j < buttonField[0].length; j++) {
				buttonField[i][j].setText(gameField.getField()[i + 1][j + 1].toString());
			}
		}
	}

}
