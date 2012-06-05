package com.alexanderdbrown.flocking;

/**
 * Allows for some paging of the GUI.
 * <p>
 * Paging checks whether a group of Flockable members are on the same "page".
 * <p>
 * A page is an area equal to the size of the map. Flockable members have a
 * tendency to go off the sides of a map, but simply wrapping them around causes
 * the mean of their locations to be somewhere in the centre.
 * <p>
 * Paging will only wrap the members once they are all on the same page. This
 * reduces the chances of integer overflow.
 * <p>
 * <b>Example:</b> (for simplicity we will use a 1D map here).
 * <p>
 * The map size is 100.<br />
 * The ideal location for the flock to be on is 0-100 (this is page 0).
 * <p>
 * If a member of the flock heads right far enough it eventually reaches the
 * location 101 (this is now on page 1). However the rest of the flock is still
 * on page 0 so nothing happens (the GUI will need to be able to cope with the
 * wrapping in it's drawing).
 * <p>
 * Eventually all members follow the member onto page 1. Once this happens their
 * locations are all reduced by 100, putting them back on page 0.
 * <p>
 * Repeat this as much as necessary.
 * 
 * @author softly
 * 
 */
public final class Page {
	/**
	 * Pages X and Y.
	 * 
	 * @param flock
	 */
	public final static void page(final Iterable<Flockable> flock) {
		checkXPage(flock, Map.INSTANCE.x);
		checkYPage(flock, Map.INSTANCE.y);
	}

	private final static void checkXPage(final Iterable<Flockable> flock,
			final int max) {
		final int page = getPage(flock.iterator().next().getLocation().x, max);
		int lowestPage = page;

		if (page == 0)
			return;

		for (final Flockable f : flock) {
			final int curPage = getPage(f.getLocation().x, max);

			if (lowestPage < 0) {
				if (curPage > 0) {
					return;
				}
				if (curPage > lowestPage) {
					lowestPage = curPage;
				}
			}

			if (lowestPage > 0) {
				if (curPage < 0) {
					return;
				}
				
				if(curPage < lowestPage) {
					lowestPage = curPage;
				}
			}

			// If they're on different pages jump out.
			if (curPage != page) {
				return;
			}
		}

		// Every bird is on the same page.
		final int pageDistance = max * lowestPage;

		for (final Flockable f : flock) {
			f.getLocation().setLocation(f.getLocation().x - pageDistance,
					f.getLocation().y);
		}
	}

	private final static int getPage(final int cur, final int max) {
		int page = cur / max;

		if (cur < 0) {
			page--;
		}

		return page;
	}

	private final static void checkYPage(final Iterable<Flockable> flock,
			final int max) {
		final int page = getPage(flock.iterator().next().getLocation().y, max);

		for (final Flockable f : flock) {
			final int curPage = getPage(f.getLocation().y, max);

			// If they're on different pages jump out.
			if (curPage != page) {
				return;
			}
		}

		if (page != 0) {

			// Every bird is on the same page.
			final int pageDistance = max * page;

			for (final Flockable f : flock) {
				f.getLocation().setLocation(f.getLocation().x,
						f.getLocation().y - pageDistance);
			}
		}
	}
}
