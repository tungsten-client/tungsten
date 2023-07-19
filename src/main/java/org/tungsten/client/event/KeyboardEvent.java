package org.tungsten.client.event;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KeyboardEvent extends Event {
	int keycode;
	int modifiers;
	int action;
}
