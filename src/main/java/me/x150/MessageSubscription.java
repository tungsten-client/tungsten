package me.x150;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A subscription to a message type
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageSubscription {
    /**
     * The priority of this subscriber. The lower, the earlier in the subscriber stack it gets called
     *
     * @return The set priority, or 0 by default
     */
    int priority() default 0;
}
