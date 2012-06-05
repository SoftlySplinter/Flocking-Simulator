package com.alexanderdbrown.flocking.bird;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;


import com.alexanderdbrown.flocking.Flockable;
import com.alexanderdbrown.flocking.Map;

public class AvoidNear extends Bird {
	public final int DISTANCE = 0;

	public AvoidNear(int x, int y) {
		super(x, y);
	}

	@Override
	protected Point getNewAim() {
		Point aim = getMean(Map.INSTANCE.flock);
		aim = avoidNear(aim);

		return aim;
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

	private Point avoidNear(Point aim) {
		Collection<Flockable> tooClose = new ArrayList<Flockable>();

		for (Flockable f : Map.INSTANCE.flock) {
			if(f == this) 
				continue;
			final long distance = getDistance(this.getLocation(), f.getLocation());
			if(distance < DISTANCE * DISTANCE) {
				tooClose.add(f);
			}
		}
		
		if(tooClose.isEmpty()) return aim;
		
		final Point newAim = (this.getMean(tooClose));
		
		final int x = newAim.x - this.getLocation().x;
		final int y = newAim.y - this.getLocation().y;
		
		if(x == 0 && y == 0) return aim;
		
		return new Point(this.getLocation().x - x, this.getLocation().y - y);
	}

	private long getDistance(Point from, Point to) {
		final int x = from.x - to.x;
		final int y = from.y - to.y;
		
		return x*x + y*y;
	}

}
