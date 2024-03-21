package utils;

/**
 * Record used for storing Prioritized Messages.
 * @param priority String: "LOW", "NORMAL", "HIGH"
 * @param message String: Message
 */
public record PriorityMessage(Priority priority, String message) { }
