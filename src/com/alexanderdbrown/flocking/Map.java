package com.alexanderdbrown.flocking;


public class Map {
	public static Map INSTANCE = null;
	
	public static final void createInstance(Iterable<Flockable> flock, int x, int y) {
		if(INSTANCE == null) {
			INSTANCE = new Map(flock,x,y);
		} else {
			throw new RuntimeException("Instance already exists.");
		}
	}
	
	private Map(final Iterable<Flockable> flock, int x, int y) {
		this.flock = flock;
		this.x = x;
		this.y = y;
	}
	
	public final Iterable<Flockable> flock;
	public final int x;
	public final int y;
	
}
