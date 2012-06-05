package com.alexanderdbrown.flocking.gui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
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
import com.alexanderdbrown.flocking.Page;
import com.alexanderdbrown.flocking.bird.*;

/**
 * GUI for the flock.
 * <p>
 * TODO:
 * <ul>
 * <li>Arguments for sizes.
 * <li>Map size based on screen size.
 * </ul>
 * 
 * @author softly
 */
@SuppressWarnings("serial")
public final class FlockGUI extends Frame {
	/** Random generator */
	private static final Random R = new Random();

	/** FPS limit */
	public static final int FPS_LIMIT = 60;

	/**
	 * Entry point.
	 * 
	 * @param args
	 *            Not used.
	 */
	public static void main(String[] args) {
		// Ready the flock.
		Collection<Flockable> flock = new ArrayList<Flockable>();

		// Create the flock.
		for (int i = 0; i < 300; i++) {
			flock.add(new AvoidNear(R.nextInt(1440), R.nextInt(900)));
		}

		// Create the map.
		Map.createInstance(flock, 1440, 900);

		// Create the gui and start.
		FlockGUI gui = new FlockGUI();
		gui.start();
	}

	/** Current Flock to draw */
	private final Iterable<Flockable> flock;

	/** If the window is open */
	private boolean open;

	/** Current FPS */
	private int fps;

	/** Current number of FPS readings */
	private int fpsReadings = 1;

	/** Rolling total of FPS */
	private int fpsTotal = FPS_LIMIT;

	/** The time of the last draw to calculate FPS */
	private long lastDraw;

	/**
	 * Get {@link Map#INSTANCE} and pass it to {@link #FlockGUI(Map)}.
	 */
	public FlockGUI() {
		this(Map.INSTANCE);
	}

	/**
	 * Get {@link Map#flock} and call into {@link #FlockGUI(Iterable)}.
	 * @param map The map.
	 */
	public FlockGUI(Map map) {
		this(map.flock);
	}

	/**
	 * Construct the gui based on a given flock.
	 * @param flock The flock.
	 */
	public FlockGUI(Iterable<Flockable> flock) {
		// Full Screen setup.
		GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().setFullScreenWindow(this);

		// Get the flock.
		this.flock = flock;

		// Close handelling.
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

		// Mouse listener.
		this.addMouseMotionListener(Mouse.INSTANCE);
	}

	/**
	 * Starts the GUI.
	 */
	public void start() {
		// Set the open flag
		this.open = true;

		// Make visable.
		this.setVisible(true);

		// Tick.
		while (open) {
			this.tick();
		}
	}

	/**
	 * Make a single tick.
	 */
	public synchronized void tick() {
		// Tick all members of the flock.
		for (Flockable f : this.flock) {
			f.tick();
		}

		// Draw onto the canvas.
		this.draw();
		
		// Page if possible.
		Page.page(flock);
	}

	/**
	 * Draw the flock onto the canvas.
	 */
	public synchronized void draw() {
		// Create off-screen image for buffering.
		Image i = this.createImage(this.getWidth(), this.getHeight());
		Graphics2D g = (Graphics2D) i.getGraphics();

		// Paint the BG.
		g.setColor(Color.white);
		g.fillRect(0, 0, i.getWidth(this), i.getHeight(this));

		// Paint each member of the flock.
		for (Flockable f : this.flock) {
			// Make sure x and y are wrapped.
			final int x = wrap(f.getLocation().x, i.getWidth(this));
			final int y = wrap(f.getLocation().y, i.getHeight(this));
			
			// Draw the bird.
			g.setColor(Color.black);
			g.fillOval(x - 2, y - 2, 4, 4);

			// Get the aims.
			final int aX = wrap(f.getAim().x, i.getWidth(this));
			final int aY = wrap(f.getAim().y, i.getHeight(this));

			g.setColor(Color.red);
			g.drawOval(aX - 2, aY - 2, 4, 4);
		}

		// FPS Counter.
		g.drawString(String.format("%d fps", this.fps), 50, 50);
		
		// Draw the buffered image onto the main canvas.
		this.getGraphics().drawImage(i, 0, 0, this);

		// The time this was drawn at.
		long drawTime = System.currentTimeMillis();
		
		// Get the time since the last draw.
		long millisSinceLastDraw = drawTime - this.lastDraw;
		this.lastDraw = drawTime;

		// Convert to seconds.
		double seconds = (double) millisSinceLastDraw / 1000.0;
		
		// Get the current FPS
		int curfps = (int) (1.0 / seconds);

		// Work out for averages.
		this.fpsTotal += curfps;
		this.fpsReadings++;

		// If the current FPS is greater than the FPS limit.
		// FPS limit - 2 seems to work better with the waits.
		if (this.fps > FPS_LIMIT - 2) {
			// Work out the ideal milliseconds per frame currently.
			int idealMillisPerFrame = (int) ((1.0 / (double) this.fps) * 1000);
			try {
				// Sleep that amount.
				Thread.sleep(idealMillisPerFrame);
			} catch (InterruptedException e) {
				// Do nothing.
			}
		}
		
		// Take the average.
		this.fps = fpsTotal / fpsReadings;

		// Reset the FPS readings to stop overflow.
		if (fpsReadings > FPS_LIMIT * 50) {
			fpsReadings = 1;
			fpsTotal = this.fps;
		}
	}

	/**
	 * Wrap a location so it is on canvas.
	 * @param i The location.
	 * @param max The edge of the canvas (the other is deemed to be 0).
	 * @return The wrapped value.
	 */
	private int wrap(int i, int max) {
		if (i < 0) {
			return max + i;
		} else if (i > max) {
			return i - max;
		}
		return i;
	}
}
