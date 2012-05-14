package com.alexanderdbrown.flocking.gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import com.alexanderdbrown.flocking.Flockable;
import com.alexanderdbrown.flocking.Map;
import com.alexanderdbrown.flocking.Mouse;
import com.alexanderdbrown.flocking.bird.TowardsMean;

public final class FlockGUI extends Frame {
	private static final Random R = new Random();
	public static final int FPS_LIMIT = 60;

	public static void main(String[] args) {
		Collection<Flockable> flock = new ArrayList<Flockable>();

		for (int i = 0; i < 1000; i++) {
			flock.add(new TowardsMean(R.nextInt(1440), R.nextInt(900)));
		}

		Map.createInstance(flock, 1440, 900);

		FlockGUI gui = new FlockGUI();
		gui.start();
	}

	private final Iterable<Flockable> flock;
	private boolean open;

	private int fps;
	private int fpsReadings = 1;
	private int fpsTotal = FPS_LIMIT;
	private long lastDraw;

	public FlockGUI() {
		this(Map.INSTANCE);
	}

	public FlockGUI(Map map) {
		this(map.flock);
	}

	public FlockGUI(Iterable<Flockable> flock) {
		// this.setSize(Map.INSTANCE.x, Map.INSTANCE.x);
		GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().setFullScreenWindow(this);

		this.flock = flock;

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				dispose();
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				System.exit(0);
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});

		this.addMouseMotionListener(Mouse.INSTANCE);
	}

	public void start() {
		this.open = true;

		this.setVisible(true);

		while (open) {
			this.tick();
		}
	}

	public synchronized void tick() {
		for (Flockable f : this.flock) {
			f.tick();
		}

		this.draw();
	}

	public synchronized void draw() {
		Image i = this.createImage(this.getWidth(), this.getHeight());
		Graphics2D g = (Graphics2D) i.getGraphics();

		g.setColor(Color.white);
		g.fillRect(0, 0, i.getWidth(this), i.getHeight(this));

		for (Flockable f : this.flock) {
			final int x = wrap(f.getLocation().x, i.getWidth(this));
			final int y = wrap(f.getLocation().y, i.getHeight(this));
			g.setColor(Color.black);
			g.fillOval(x - 2, y - 2, 4, 4);

			final int aX = wrap(f.getAim().x, i.getWidth(this));
			final int aY = wrap(f.getAim().y, i.getHeight(this));

			g.setColor(Color.red);
			g.drawOval(aX - 5, aY - 5, 10, 10);
		}

		g.drawString(String.format("%d fps", this.fps), 10, 10);
		this.getGraphics().drawImage(i, 0, 0, this);

		long drawTime = System.currentTimeMillis();
		long millisSinceLastDraw = drawTime - this.lastDraw;
		this.lastDraw = drawTime;

		double seconds = (double) millisSinceLastDraw / 1000.0;
		int curfps = (int) (1.0 / seconds);

		this.fpsTotal += curfps;
		this.fpsReadings++;

		if (this.fps > FPS_LIMIT - 2) {
			int idealMillisPerFrame = (int) ((1.0 / (double) this.fps) * 1000);
			try {
				Thread.sleep(idealMillisPerFrame);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.fps = fpsTotal / fpsReadings;

		if (fpsReadings > FPS_LIMIT * 50) {
			fpsReadings = 1;
			fpsTotal = this.fps;
		}
	}

	private int wrap(int i, int max) {
		if (i < 0) {
			return max + i;
		} else if (i > max) {
			return i - max;
		}
		return i;
	}
}
