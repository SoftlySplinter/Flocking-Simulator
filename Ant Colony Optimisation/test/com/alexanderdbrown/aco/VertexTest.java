/**
 * 
 */
package com.alexanderdbrown.aco;

import static org.junit.Assert.fail;

import java.awt.Point;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * @author softly
 *
 */
public class VertexTest {
	private Vertex vertex;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		vertex = new Vertex(new Point(0,0));
	}

	/**
	 * Test method for {@link com.alexanderdbrown.aco.Vertex#getDistanceTo(com.alexanderdbrown.aco.Vertex)}.
	 */
	@Test
	public void testGetDistanceToSelf() {
		final double expected = 0;
		final double actual = vertex.getDistanceTo(vertex);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetDistanceShort() {
		final double expected = 1;
		final Vertex other = new Vertex(new Point(0,1));
		final double actual = vertex.getDistanceTo(other);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetDistanceBackwards() {
		final double expected = 1;
		final Vertex other = new Vertex(new Point(0,1));
		final double actual = other.getDistanceTo(this.vertex);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetDistanceDiagonal() {
		final double expected = Math.sqrt(2);
		final Vertex other = new Vertex(new Point(1,1));
		final double actual = this.vertex.getDistanceTo(other);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetDistanceDiagonalBackwards() {
		final double expected = Math.sqrt(2);
		final Vertex other = new Vertex(new Point(1,1));
		final double actual = other.getDistanceTo(this.vertex);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetDistanceDiagonal2() {
		final double expected = 5;
		final Vertex other = new Vertex(new Point(3,4));
		final double actual = this.vertex.getDistanceTo(other);
		
		Assert.assertEquals(expected, actual);
	}

}
