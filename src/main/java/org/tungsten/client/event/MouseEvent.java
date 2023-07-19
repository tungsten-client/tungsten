package org.tungsten.client.event;

import com.google.common.base.Preconditions;
import lombok.Getter;

@Getter
public class MouseEvent extends Event {
	int button;
	Type type;

	public MouseEvent(int button, Type type) {
		this.button = button;
		this.type = type;
	}

	public enum Type {
		LIFT, CLICK;

		public static Type of(int id) {
			Preconditions.checkElementIndex(id, 2);
			return Type.values()[id];
		}
	}
}
