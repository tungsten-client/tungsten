package org.tungsten.client.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ShouldSneakQuery extends Event {
	boolean shouldSneak;
}