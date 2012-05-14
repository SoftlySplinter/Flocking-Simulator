package com.alexanderdbrown.flocking.bird;

import java.awt.Point;

import com.alexanderdbrown.flocking.Flockable;
import com.alexanderdbrown.flocking.Map;

public class TowardsMean extends Bird {

	public TowardsMean(int x, int y) {
		super(x, y);
	}

	private Point getMean(Iterable<Flockable> flock) {
		int avgX = 0;
		int avgY = 0;
		int total = 0;
		
		for(Flockable f : flock) {
			avgX += f.getLocation().x;
			avgY += f.getLocation().y;
			total++;
		}
		
		avgX /= total;
		avgY /= total;
		
		return new Point(avgX, avgY);
	}

	@Override
	protected Point getNewAim() {
		return getMean(Map.INSTANCE.flock);
	}
	
}
