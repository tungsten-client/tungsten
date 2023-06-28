package me.x150.exception;

import me.x150.MessageSubscription;

/**
 * Thrown when a method with {@link MessageSubscription} has an invalid handler signature
 * <p>
 * A method annotated with {@link MessageSubscription} should have one argument, the class being listened for.<p>
 * Note that objects extending the listener type of the handler will also be sent to your handler, allowing you to receive all events by listening to {@link Object}
 * <p>
 * Working example:
 * <blockquote><pre>
 *    {@literal @}MessageSubscription
 *     void handle(Message message) {
 *         System.out.println("Received message " + message);
 *     }
 * </pre></blockquote>
 */
public class InvalidSubscriberException extends RuntimeException {
    public InvalidSubscriberException(String cause) {
        super(cause);
    }
}
