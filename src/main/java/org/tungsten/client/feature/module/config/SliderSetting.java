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
		if (value > max) { value = max; }
		else if (value < min) { value = min; }
		super.setValue(value);
	}

	@Override
	public String getDescriptor() {
		return "<p class=\"element-descriptor\">" + this.name + "</p>";
	}

	@Override
	public String toHTML() {
		return this.getDescriptor() + "\n" + "<label class=\"slider-container\"><input type=\"range\" min=\"" +
				this.min + "\" max=\"" + this.max + "\" step=\"1\" value=\"" + this.getValue().toString() + "\"></label>";
	}

}
