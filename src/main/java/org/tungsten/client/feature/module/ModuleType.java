package org.tungsten.client.feature.module;

public class ModuleType {
	/* can we just use lombok for some of these? */
	private final String name;
	private double x;
	private double y;

	private boolean expanded;

	public ModuleType(String name) {
		this.name = name.toUpperCase();
		this.x = 0;
		this.y = 0;
		this.expanded = false;
	}

	public ModuleType(String name, int x, int y) {
		this.name = name.toUpperCase();
		this.x = x;
		this.y = y;
		this.expanded = false;
	}

	public String getName() {
		return this.name.toUpperCase();
	}

	public double[] getPos() {
		return new double[]{
				x,
				y
		};
	}
	/* couldn't this be replaced with a @Getter ? */
	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public boolean getExpanded() { return this.expanded; }

	public void setExpanded(boolean expanded) { this.expanded = expanded; }
}