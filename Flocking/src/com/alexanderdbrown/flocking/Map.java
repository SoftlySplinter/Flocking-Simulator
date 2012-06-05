package com.alexanderdbrown.flocking;

/**
 * Maps which holds the sizes and the flock.
 * @author softly
 */
public class Map {
	/** Singleton instance */
	public static Map INSTANCE = null;

	/** Create the instance */
	public static final void createInstance(Iterable<Flockable> flock, int x,
			int y) {
		if (INSTANCE == null) {
			INSTANCE = new Map(flock, x, y);
		} else {
			throw new RuntimeException("Instance already exists.");
		}
	}

	/**
	 * Private consturctor to stop anything other than {@value #INSTANCE} being
	 * used.
	 */
	private Map(final Iterable<Flockable> flock, int x, int y) {
		this.flock = flock;
		this.x = x;
		this.y = y;
	}

	/** The current flock */
	public final Iterable<Flockable> flock;
	
	/** X size of the map */
	public final int x;
	
	/** Y size of the map */
	public final int y;

}
