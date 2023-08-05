package org.tungsten.client.feature.module;

public class ModuleType {

	private final String name;
	private double x;
	private double y;

	public ModuleType(String name) {
		this.name = name.toUpperCase();
		this.x = 0;
		this.y = 0;
	}

	public ModuleType(String name, int x, int y) {
		this.name = name.toUpperCase();
		this.x = x;
		this.y = y;
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
}
