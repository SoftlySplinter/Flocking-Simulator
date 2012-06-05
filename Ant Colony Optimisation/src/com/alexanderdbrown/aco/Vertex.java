package com.alexanderdbrown.aco;

import java.awt.Point;

public final class Vertex {
	private final Point location;
	
	public Vertex(Point location) {
		this.location = location;
	}

	public double getDistanceTo(Vertex other) {
		return Utils.getDistance(this.location, other.getLocation());
	}

	public Point getLocation() {
		return this.location;
	}
}
