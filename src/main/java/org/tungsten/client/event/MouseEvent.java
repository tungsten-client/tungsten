package org.tungsten.client.event;

import com.google.common.base.Preconditions;

public record MouseEvent(int button, Type type) {
	public enum Type {
		LIFT, CLICK;

		public static Type of(int id) {
			Preconditions.checkElementIndex(id, 2);
			return Type.values()[id];
		}
	}
}
