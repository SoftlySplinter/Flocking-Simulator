package com.alexanderdbrown.aco;

import java.awt.Point;

public class Utils {

	public final static double getDistance(final Point to, final Point from) {
		final double xSquared = Math.pow(from.x - to.x, 2);
		final double ySquared = Math.pow(from.y - to.y, 2);
		
		final double rSquared = xSquared + ySquared;
		final double r = Math.pow(rSquared, 1d/2d);
		
		return r;
	}

}
