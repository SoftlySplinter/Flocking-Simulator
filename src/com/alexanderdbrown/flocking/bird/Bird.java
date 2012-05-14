package com.alexanderdbrown.flocking.bird;

import java.awt.Point;

import com.alexanderdbrown.flocking.Flockable;
import com.alexanderdbrown.flocking.Map;
import com.alexanderdbrown.flocking.Mouse;

import static java.lang.Math.PI;

public abstract class Bird implements Flockable {
	private final static int MAX_ACC = 3;
	private final static int MIN_ACC = 1;
	private final static double TURN = PI / 50;

	private final Point location;
	private final Point aim;

	private int acceleration = MIN_ACC;
	private int speed = getSpeed(this.acceleration);

	/**
	 * Directional heading in radians.
	 * <p>
	 * Starts at 0.0 (North).
	 */
	private double direction = 0 * TURN;

	public Bird(int x, int y) {
		this.location = new Point(x, y);
		this.aim = new Point(x, y);
	}

	@Override
	public Point getLocation() {
		return this.location;
	}

	private int getSpeed(int acceleration) {
		int newSpeed = (int) Math.ceil(Math.pow(this.acceleration, 2) / 2);
		return newSpeed;
	}

	@Override
	public void tick() {
		this.aim.setLocation(this.getNewAim());
		this.move();

	}

	protected abstract Point getNewAim();

	private double getAngle(final Point a, final Point b) {
		final Point bPrimed = new Point(b.x, b.y);

		// Get the closest.
		for (int i = b.x-Map.INSTANCE.x; i <= Map.INSTANCE.x; i += Map.INSTANCE.x) {
			for (int j = b.y-Map.INSTANCE.y; j <= Map.INSTANCE.y; j += Map.INSTANCE.y) {
				bPrimed.setLocation(i, j);
				if (isCloser(bPrimed, b, a)) {
					b.setLocation(bPrimed);
				}
			}
		}
		System.out.println();

		final double adj = a.x - b.x;
		final double opp = a.y - b.y;
		final double hyp = Math.sqrt(opp * opp + adj * adj);

		final double theta = Math.acos(adj / hyp);

		if (a.y - b.y > 0) {
			return theta;
		} else {
			return -theta;
		}
	}

	private boolean isCloser(Point a, Point b, Point to) {
		final int aX = to.x - a.x;
		final int aY = to.y - a.y;
		final long aH = this.getPythag(aX, aY);

		final int bX = to.x - b.x;
		final int bY = to.y - b.x;
		final long bH = this.getPythag(bX, bY);

		return aH <= bH;
	}

	private void move() {
		if (this.location.equals(this.getAim())) {
			return;
		}

		this.calcAngle();
		this.calcMovement();
		this.calcMove();
	}

	private void calcAngle() {
		final double directionToAim = this.getAngle(this.location,
				this.getAim());

		if (this.direction != directionToAim) {
			final double directionAtZero = this.direction - this.direction;
			double directionToAimAtZero = directionToAim - this.direction;

			if (directionToAimAtZero <= -PI) {
				directionToAimAtZero += 2 * PI;
			} else if (directionToAimAtZero > PI) {
				directionToAimAtZero -= 2 * PI;
			}

			if (directionToAimAtZero > 0) {
				if (directionAtZero + TURN < directionToAimAtZero) {
					this.direction += TURN;
				} else {
					this.direction = directionToAim;
				}
			} else {
				if (directionAtZero - TURN > directionToAimAtZero) {
					this.direction -= TURN;
				} else {
					this.direction = directionToAim;
				}
			}
		}
	}

	private void calcMovement() {
		final int speed2 = this.speed * this.speed;
		final long hyp2 = getPythag(
				this.location.x - this.getAim().x,
				this.location.y - this.getAim().y);

		if (speed2 > hyp2) {
			final int speed2primed = this.speed - 1 * this.speed - 1;

			if (isCloser(speed2primed, speed2, hyp2) && this.acceleration >= MIN_ACC) {
				this.acceleration--;
			}
		} else if (speed2 < hyp2) {
			final int speed2primed = this.speed + 1 * this.speed + 1;

			if (isCloser(speed2primed, speed2, hyp2)
					&& this.acceleration < MAX_ACC) {
				this.acceleration++;
			}
		}

		this.speed = this.getSpeed(this.acceleration);
	}

	private void calcMove() {
		int x = (int) (this.speed * Math.cos(this.direction + PI))
				+ this.location.x;
		int y = (int) (this.speed * Math.sin(this.direction + PI))
				+ this.location.y;

		this.location.setLocation(x, y);
	}

	private boolean isCloser(long a, long b, long target) {
		final long aDiff = Math.abs(target - a);
		final long bDiff = Math.abs(target - b);

		return aDiff < bDiff;
	}

	private long getPythag(final int a, final int b) {
		return (long) (a * a) + (long) (b * b);
	}

	@Override
	public Point getAim() {
		return aim;
	}
}
