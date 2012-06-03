package com.alexanderdbrown.flocking;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Class for mouse interaction.
 * @author softly
 *
 */
public class Mouse implements MouseMotionListener {
	/** Singleton instance */
	public static final Mouse INSTANCE = new Mouse();	
	
	/** The current location of the mouse */
	public Point mouse = new Point(0,0);

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/** Update the location */
	@Override
	public void mouseMoved(MouseEvent e) {
		this.mouse = e.getPoint();
	}

}
