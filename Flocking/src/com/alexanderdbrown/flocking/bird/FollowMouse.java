package com.alexanderdbrown.flocking.bird;

import java.awt.Point;

import com.alexanderdbrown.flocking.Mouse;

public class FollowMouse extends Bird {


	public FollowMouse(int x, int y) {
		super(x, y);
	}

	@Override
	protected Point getNewAim() {
		return Mouse.INSTANCE.mouse;
	}

}
