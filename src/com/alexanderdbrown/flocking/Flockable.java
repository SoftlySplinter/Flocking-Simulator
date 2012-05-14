package com.alexanderdbrown.flocking;

import java.awt.Point;

public interface Flockable {
	public Point getLocation();
	public Point getAim();
	public void tick();
}
