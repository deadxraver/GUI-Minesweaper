package guipart;

import javax.swing.*;

public class CellButton extends JButton {
	public final int gridX;
	public final int gridY;
	public CellButton(String text, int gridX, int gridY) {
		super(text);
		this.gridX = gridX;
		this.gridY = gridY;
	}
}
