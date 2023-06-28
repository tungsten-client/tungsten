package me.x150.impl;

import me.x150.MessageManager;

/**
 * Emitted by {@link MessageManager}, when a new subscriber has been registered
 *
 * @param handler The new handler added to the subscriber list
 */
public record SubscriberRegisterEvent(MessageManager.Handler handler) {
}
