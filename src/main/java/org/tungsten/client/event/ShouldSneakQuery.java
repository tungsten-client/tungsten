package org.tungsten.client.event;

public class ShouldSneakQuery extends Event {
    public ShouldSneakQuery(boolean shouldSneak) {
        this.shouldSneak = shouldSneak;
    }
    boolean shouldSneak;
    public boolean getShouldSneak() {
        return this.shouldSneak;
    }
    public void setShouldSneak(boolean shouldSneak) {
        this.shouldSneak = shouldSneak;
    }
}