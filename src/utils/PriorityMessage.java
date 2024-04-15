package utils;

import java.time.Instant;

/**
 * Record used for storing Prioritized Messages.
 * @param priority String: "LOW", "NORMAL", "HIGH"
 * @param message String: Message
 */
public record PriorityMessage(Priority priority, String message, Instant timestamp) implements Comparable<PriorityMessage> {
	public PriorityMessage(Priority priority, String message) {
		this(priority, message, Instant.now());
	}

	@Override
	public int compareTo(PriorityMessage other) {
		int priorityComparison = this.priority.ordinal() - other.priority.ordinal();
		if (priorityComparison == 0) {
			return this.timestamp.compareTo(other.timestamp);
		}
		return priorityComparison;
	}
}
