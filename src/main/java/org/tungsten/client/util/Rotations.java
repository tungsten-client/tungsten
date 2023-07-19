package org.tungsten.client.util;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class Rotations {

	private static final long MAX_LIFETIME_MS = 1_000;
	static List<Entry> entries = new CopyOnWriteArrayList<>();

	public static void addRotation(String id, float priority, Goal goal) {
		Optional<Entry> first = entries.stream().filter(entry -> entry.id.equals(id)).findFirst();
		if (first.isPresent()) {
			first.get().lastRequested = System.currentTimeMillis();
			first.get().goal = goal;
			first.get().prio = priority;
		} else {
			Entry e = new Entry(id, priority, System.currentTimeMillis(), goal);
			entries.add(e);
		}
		removeDeadEntries();
		reorder();
	}

	static void reorder() {
		entries.sort(Comparator.comparingDouble(value -> -value.prio)); // high prio first
	}

	static void removeDeadEntries() {
		entries.removeIf(entry -> System.currentTimeMillis() - entry.lastRequested > MAX_LIFETIME_MS);
	}

	public static Goal getCurrentGoal() {
		removeDeadEntries();
		if (entries.isEmpty()) {
			return null;
		}
		return entries.get(0).goal; // should always be the highest priority, non dead entry
	}

	public record Rotation(float pitch, float yaw) {
	}

	static class Entry {
		String id;
		float prio;
		long lastRequested;
		Goal goal;

		public Entry(String id, float prio, long lastRequested, Goal goal) {
			this.id = id;
			this.prio = prio;
			this.lastRequested = lastRequested;
			this.goal = goal;
		}
	}

    public interface Goal {
        Rotation compute();
    }
}

