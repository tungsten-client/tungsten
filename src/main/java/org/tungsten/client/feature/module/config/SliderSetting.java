package org.tungsten.client.feature.module.config;

public class SliderSetting extends GenericSetting<Double> {
	final double min;
	final double max;

	public SliderSetting(double defaultValue, double min, double max, String name, String description) {
		super(defaultValue, name, description);
		this.min = min;
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	@Override
	public void setValue(Double value) {
		if (value > max || value < min) {
			return;
		}
		super.setValue(value);
	}

	@Override
	public String toHTML() {
		return this.getDescriptor() + "\n" + "<input type=\"range\" min=\"" + this.min + "\" max=\"" + this.max + "\" step=\"1\" value=\"" + this.getValue()
				.toString() + "\">";
	}

}
