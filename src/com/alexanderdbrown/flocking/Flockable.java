package com.alexanderdbrown.flocking;

import java.awt.Point;

/**
 * Interface to allow different types of flockable members.
 * 
 * @author softly
 * 
 */
public interface Flockable {
	/** Get the member's location */
	public Point getLocation();

	/** Get the member's aim */
	public Point getAim();

	/**
	 * Make the member take a tick.
	 * <p>
	 * This should work out a new aim and a new location for the member.
	 */
	public void tick();
}
