package org.tungsten.client.event;

import meteordevelopment.orbit.ICancellable;

public class PushOutOfBlockEvent implements ICancellable {
    private static final PushOutOfBlockEvent EVENT = new PushOutOfBlockEvent();
    private boolean isCancelled;

    private PushOutOfBlockEvent() {}

    public static PushOutOfBlockEvent get() {
        EVENT.isCancelled = false;
        return EVENT;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }
}