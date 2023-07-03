package org.tungsten.client.event;


import org.tungsten.client.event.Event;

public class ChunkRenderQuery extends Event {
    boolean shouldRender = false;

    boolean modified = false;

    public void setShouldRender(boolean t) {
        this.shouldRender = t;
        this.modified = true;
    }
    public boolean getModified(){ return this.modified; }
    public boolean getShouldRender(){ return this.shouldRender; }
}
