package guipart;

import classes.Cell;
import classes.GameField;
import classes.WorkWithInput;
import enums.Condition;
import enums.Contains;
import enums.Size;
import exceptions.InputException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

public class MainFrame extends JFrame {

	private final GameField gameField;
	private CellButton[][] buttonField;
	private MouseAdapter[][] mouseAdapters;
	private final Instant startTime = Instant.now();
	private JLabel timeLabel;
	private Thread r;

	public MainFrame(int x, int y, Size option) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Minesweeper");
		this.gameField = new GameField(option);
		setLayout(new GridBagLayout());
		timeLabel = new JLabel("00:00:00");
		drawField();
		pack();
		setBounds(x, y, getWidth() + 60, getHeight() + 60);
		setResizable(false);
	}

	private void updateTime() {
		while (true) {
			timeLabel.setText(getStringTime());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
				return;
            }
        }
	}

	private String getStringTime() {
		Duration currentTime = Duration.between(startTime, Instant.now());
		String hours = (currentTime.toHours() % 24 < 10) ? ("0" + currentTime.toHours() % 24) : ("" + currentTime.toHours() % 24);
		String minutes = (currentTime.toMinutes() % 60 < 10) ? ("0" + currentTime.toMinutes() % 60) : ("" + currentTime.toMinutes() % 60);
		String seconds = (currentTime.toSeconds() % 60 < 10) ? ("0" + currentTime.toSeconds() % 60) : ("" + currentTime.toSeconds() % 60);
		return hours + ":" + minutes + ":" + seconds;
	}

	private void drawField() {
		GridBagConstraints gbc = new GridBagConstraints();
		buttonField = new CellButton[gameField.getField().length - 2][gameField.getField()[0].length - 2];
		mouseAdapters = new MouseAdapter[gameField.getField().length - 2][gameField.getField()[0].length - 2];
		for (int gridY = 0; gridY < buttonField.length; gridY++) {
			for (int gridX = 0; gridX < buttonField[0].length; gridX++) {
				gbc.gridx = gridX;
				gbc.gridy = gridY;
				CellButton button = processButton(gridX, gridY);
				buttonField[gridY][gridX] = button;
				add(button, gbc);
			}
		}
		r = new Thread(this::updateTime);
		r.start();
		gbc.gridy++;
		add(timeLabel, gbc);
	}

	private CellButton processButton(int gridX, int gridY) {
		CellButton button = new CellButton("?", gridX, gridY);
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String inp = "" + (char)(button.gridX + 'A') + (button.gridY + 1) + (SwingUtilities.isRightMouseButton(e) ? "f" : "");
				try {
					gameField.move(WorkWithInput.convert(inp));
				} catch (InputException ex) {
					System.err.println(ex.getMessage());
				}
				gameField.removeZeros();
				if (!gameField.checkCondition()) {
					gameField.openAll();
					refreshField();
					if (gameField.getField()[button.gridY + 1][button.gridX + 1].getContainment() == Contains.MINE) button.setText("X ");
					String text = "You ";
					if (button.getText().trim().equals("X") || gameField.getField()[button.gridY + 1][button.gridX + 1].getContainment() == Contains.MINE) {
						text += "lose";
					} else text += "win";
					endGame(text);
				}
				refreshField();
				super.mouseClicked(e);
			}
		};
		mouseAdapters[gridY][gridX] = mouseAdapter;
		button.addMouseListener(mouseAdapter);
		return button;
	}

	private void refreshField() {
		for (int i = 0; i < buttonField.length; i++) {
			for (int j = 0; j < buttonField[0].length; j++) {
				Cell cell = gameField.getField()[i + 1][j + 1];
				buttonField[i][j].setText(cell.toString());
				if (cell.getContainment() == Contains.MINE && cell.getCondition() == Condition.OPEN) buttonField[i][j].setText("X ");
				if (buttonField[i][j].getText().trim().equals("X") || buttonField[i][j].getText().trim().equals("F")) buttonField[i][j].setBackground(Color.RED);
				else if (buttonField[i][j].getText().trim().equals("?")) buttonField[i][j].setBackground(null);
				else buttonField[i][j].setBackground(Color.GREEN);
			}
		}
	}

	private void endGame(String text) {
		r.interrupt();
		for (int i = 0; i < buttonField.length; i++) {
			for (int j = 0; j < buttonField[0].length; j++) {
				buttonField[i][j].setEnabled(false);
				buttonField[i][j].removeMouseListener(mouseAdapters[i][j]);
			}
		}
		JFrame endFrame = new JFrame(text);
		JLabel label = new JLabel(text);
		JButton button = new JButton("OK");
		GridBagConstraints gbc = new GridBagConstraints();

		endFrame.setLayout(new GridBagLayout());

		gbc.gridx = 0;
		gbc.gridy = 0;
		endFrame.add(label, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		endFrame.add(button, gbc);
		endFrame.pack();
		endFrame.setBounds(getX() + 30, getY() + 30, endFrame.getWidth() + 70, endFrame.getHeight() + 50);

		button.addActionListener(l -> System.exit(0));
		endFrame.setVisible(true);
	}

}
