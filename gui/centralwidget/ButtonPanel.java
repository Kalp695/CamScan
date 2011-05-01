package centralwidget;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

/**
 * The button panel that contains basic tools for
 * manipulating the current page.
 * 
 * @author Stelios
 *
 */
public class ButtonPanel extends JPanel {
	
	/****************************************
	 * 
	 * Constructor(s)
	 * 
	 ****************************************/
	
	/**
	 * Constructor.
	 */
	public ButtonPanel() {
		super();

		this.setLayout(new GridLayout(2,4));
		
		// Setup all the buttons
		JButton hFlipButton = new JButton("Horizontal Split");
		this.add(hFlipButton);
		
		JButton vFlipButton = new JButton("Vertical Split");
		this.add(vFlipButton);
		
		JButton rotateLeftButton = new JButton("Rotate Left");
		this.add(rotateLeftButton);
		
		JButton rotateRightButton = new JButton("Rotate Right");
		this.add(rotateRightButton);
		
		JLabel contrastLabel = new JLabel("Contrast: ", SwingConstants.CENTER);
		this.add(contrastLabel);
		
		JSlider contrastSlider = new JSlider();
		this.add(contrastSlider);
		
		JLabel temperatureLabel = new JLabel("Temperature: ", SwingConstants.CENTER);
		this.add(temperatureLabel);
		
		JSlider temperatureSlider = new JSlider();
		this.add(temperatureSlider);
	}
}
