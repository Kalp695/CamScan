package centralwidget;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import core.Corners;

public class SplitMode extends EditPanel {
	
	private EditPanel editPanel;
	
	private Corners box1;
	private Corners box2;
	
	/**
	 * The actual drawn ellipses. These
	 * differ from cornerUR in that they
	 * have the transform applied to them,
	 * making them appear correctly on screen.
	 */
	private Ellipse2D drawableUR1;
	private Ellipse2D drawableUL1;
	private Ellipse2D drawableDR1;
	private Ellipse2D drawableDL1;
	private Ellipse2D drawableUR2;
	private Ellipse2D drawableUL2;
	private Ellipse2D drawableDR2;
	private Ellipse2D drawableDL2;;
	
	
	/**
	 * These point transform objects represent the affine
	 * transform that must be applied to a point based on
	 * the user dragging and dropping the point.
	 */
	private PointTransform transUR1 = new PointTransform();
	private PointTransform transUL1 = new PointTransform();
	private PointTransform transDR1 = new PointTransform();
	private PointTransform transDL1 = new PointTransform();
	private PointTransform transUR2 = new PointTransform();
	private PointTransform transUL2 = new PointTransform();
	private PointTransform transDR2 = new PointTransform();
	private PointTransform transDL2 = new PointTransform();
	
	
	/**
	 * Connecting lines for each of the two boxes
	 */
	private Line2D lineULUR1;
	private Line2D lineURDR1;
	private Line2D lineDRDL1;
	private Line2D lineDLUL1;
	private Line2D lineULUR2;
	private Line2D lineURDR2;
	private Line2D lineDRDL2;
	private Line2D lineDLUL2;
	
	
	public SplitMode(Corners box1, Corners box2, EditPanel panel) {
		this.box1 = box1;
		this.box2 = box2;
		this.editPanel = panel;
	}

	public void paint(Graphics2D brush) {

		// apply the transforms to the corners
		this.scaleAndTranslate(this.box1.upright(), this.transUR1, this.transCanvas, this.drawableUR1);
		this.scaleAndTranslate(this.box1.upleft(), this.transUL1, this.transCanvas, this.drawableUL1);
		this.scaleAndTranslate(this.box1.downleft(), this.transDL1, this.transCanvas, this.drawableDL1);
		this.scaleAndTranslate(this.box1.downright(), this.transDR1, this.transCanvas, this.drawableDR1);
		
		this.scaleAndTranslate(this.box2.upright(), this.transUR2, this.transCanvas, this.drawableUR2);
		this.scaleAndTranslate(this.box2.upleft(), this.transUL2, this.transCanvas, this.drawableUL2);
		this.scaleAndTranslate(this.box2.downleft(), this.transDL2, this.transCanvas, this.drawableDL2);
		this.scaleAndTranslate(this.box2.downright(), this.transDR2, this.transCanvas, this.drawableDR2);

		// make the lines connect to the new point location
		this.updateConnectingLinesSplitMode();
		
		// draw lines
		brush.setColor(Color.BLACK);
		brush.draw(this.lineULUR1);
		brush.draw(this.lineURDR1);
		brush.draw(this.lineDRDL1);
		brush.draw(this.lineDLUL1);
		brush.draw(this.lineULUR2);
		brush.draw(this.lineURDR2);
		brush.draw(this.lineDRDL2);
		brush.draw(this.lineDLUL2);

		// draw points
		brush.draw(this.drawableUR1);
		brush.fill(this.drawableUR1);
		brush.draw(this.drawableUL1);
		brush.fill(this.drawableUL1);
		brush.draw(this.drawableDR1);
		brush.fill(this.drawableDR1);
		brush.draw(this.drawableDL1);
		brush.fill(this.drawableDL1);
		
		brush.draw(this.drawableUR2);
		brush.fill(this.drawableUR2);
		brush.draw(this.drawableUL2);
		brush.fill(this.drawableUL2);
		brush.draw(this.drawableDR2);
		brush.fill(this.drawableDR2);
		brush.draw(this.drawableDL2);
		brush.fill(this.drawableDL2);
	}
	
	public void mouseDragged(MouseEvent e) {
		if (this.transImage.dragging) this.dragPoint(transImage, e);
		else if (this.transUL1.dragging) this.dragPoint(this.transUL1, e);
		else if (this.transUR1.dragging) this.dragPoint(this.transUR1, e);
		else if (this.transDL1.dragging) this.dragPoint(this.transDL1, e);
		else if (this.transDR1.dragging) this.dragPoint(this.transDR1, e);
		else if (this.transUL2.dragging) this.dragPoint(this.transUL2, e);
		else if (this.transUR2.dragging) this.dragPoint(this.transUR2, e);
		else if (this.transDL2.dragging) this.dragPoint(this.transDL2, e);
		else if (this.transDR2.dragging) this.dragPoint(this.transDR2, e);
		else if (editPanel.transCanvas.dragging) this.dragPoint(editPanel.transCanvas, e);
	}
	
	public void mousePressed(MouseEvent e) {
		// set the hand cursor
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		// register drags for the corners of box1
		if (this.isWithinCornerEllipse(this.drawableUL1, e.getX(), e.getY())) {
			this.registerMousePress(this.transUL1, e);
		} else if (this.isWithinCornerEllipse(this.drawableUR1, e.getX(), e.getY())) {
			this.registerMousePress(this.transUR1, e);
		} else if (this.isWithinCornerEllipse(this.drawableDL1, e.getX(), e.getY())) {
			this.registerMousePress(this.transDL1, e);
		} else if (this.isWithinCornerEllipse(this.drawableDR1, e.getX(), e.getY())) {
			this.registerMousePress(this.transDR1, e);
		}
		
		// register drags for the corners of box2
		if (this.isWithinCornerEllipse(this.drawableUL2, e.getX(), e.getY())) {
			this.registerMousePress(this.transUL2, e);
		} else if (this.isWithinCornerEllipse(this.drawableUR2, e.getX(), e.getY())) {
			this.registerMousePress(this.transUR2, e);
		} else if (this.isWithinCornerEllipse(this.drawableDL2, e.getX(), e.getY())) {
			this.registerMousePress(this.transDL2, e);
		} else if (this.isWithinCornerEllipse(this.drawableDR2, e.getX(), e.getY())) {
			this.registerMousePress(this.transDR2, e);
		}

		// the image
		else if (editPanel.isWithinImage(e.getX(), e.getY())) {
			editPanel.registerMousePress(editPanel.transImage, e);
		}

		// otherwise drag the canvas
		else {
			editPanel.registerMousePress(editPanel.transCanvas, e);
		}
	}
	
	/**
	 * Updates all lines connecting the corners.
	 */
	private void updateConnectingLinesSplitMode() {
		this.moveLine(this.lineULUR1, this.drawableUL1, this.drawableUR1);
		this.moveLine(this.lineURDR1, this.drawableUR1, this.drawableDR1);
		this.moveLine(this.lineDRDL1, this.drawableDR1, this.drawableDL1);
		this.moveLine(this.lineDLUL1, this.drawableDL1, this.drawableUL1);
		this.moveLine(this.lineULUR2, this.drawableUL2, this.drawableUR2);
		this.moveLine(this.lineURDR2, this.drawableUR2, this.drawableDR2);
		this.moveLine(this.lineDRDL2, this.drawableDR2, this.drawableDL2);
		this.moveLine(this.lineDLUL2, this.drawableDL2, this.drawableUL2);
	}
	
	public void mouseReleased() {
		this.transUR1.dragging = false;
		this.transUL1.dragging = false;
		this.transDR1.dragging = false;
		this.transDL1.dragging = false;
		this.transUR2.dragging = false;
		this.transUL2.dragging = false;
		this.transDR2.dragging = false;
		this.transDL2.dragging = false;
	}
	
}
