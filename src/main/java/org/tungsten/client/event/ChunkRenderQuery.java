package org.tungsten.client.event;


import lombok.Getter;

@Getter
public class ChunkRenderQuery extends Event {
	boolean shouldRender = false;
	boolean modified = false;

	public void setShouldRender(boolean t) {
		this.shouldRender = t;
		this.modified = true;
	}
}
