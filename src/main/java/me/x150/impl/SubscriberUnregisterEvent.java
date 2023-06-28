package me.x150.impl;

/**
 * Emitted by {@link me.x150.MessageManager}, when an old org.tungsten.client.event handler has been removed from the subscriber list
 *
 * @param handlerClass The class owning the handlers being removed
 */
public record SubscriberUnregisterEvent(Class<?> handlerClass) {
}
