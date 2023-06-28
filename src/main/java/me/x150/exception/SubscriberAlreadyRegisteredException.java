package me.x150.exception;

/**
 * Thrown when a {@link me.x150.MessageManager} received a subscription from a handler, which is already subscribed.
 */
public class SubscriberAlreadyRegisteredException extends RuntimeException {
    public SubscriberAlreadyRegisteredException(String cause) {
        super(cause);
    }
}
