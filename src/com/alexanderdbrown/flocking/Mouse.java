package com.alexanderdbrown.flocking;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


public class Mouse implements MouseMotionListener {
	public static final Mouse INSTANCE = new Mouse();	
	
	public Point mouse = new Point(0,0);

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mouse = e.getPoint();
	}

}
