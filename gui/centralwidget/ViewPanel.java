package centralwidget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import core.Parameters;

/**
 * This panel is used for only viewing a page.
 * 
 * @author Stelios
 *
 */
public class ViewPanel extends JPanel {
	
	/****************************************
	 * 
	 * Constructors.
	 * 
	 ****************************************/
	
	/**
	 * Constructor.
	 */
	public ViewPanel() {
		super();
		this.setBackground(Color.LIGHT_GRAY);
	}
	
	/**
	 * The paintComponent method.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D brush = (Graphics2D) g;
		
		g.drawImage(Parameters.getCurrPageImg(), 0, 0, null);
	}
}
