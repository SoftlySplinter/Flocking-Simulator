package com.alexanderdbrown.aco;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

public final class Graph {
	private final Collection<Vertex> verticies = new ArrayList<Vertex>();
	
	public void addVertex(Point p) {
		Vertex vertex = new Vertex(p);
	}
}
