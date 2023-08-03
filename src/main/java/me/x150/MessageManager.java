package me.x150;

import me.x150.exception.InvalidSubscriberException;
import me.x150.exception.SubscriberAlreadyRegisteredException;
import me.x150.impl.SubscriberRegisterEvent;
import me.x150.impl.SubscriberUnregisterEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A message manager
 */
public class MessageManager {
    protected final Map<Class<?>, List<Handler>> eventMap = new ConcurrentHashMap<>();

    /**
     * Registers a new handler. Will sort all handlers based on priority, and emit an {@link SubscriberRegisterEvent}
     *
     * @param handler The handler to register
     */
    protected void register(Handler handler) {
        List<Handler> handlers = eventMap.computeIfAbsent(handler.subscriptionType, c -> new CopyOnWriteArrayList<>());

        if (handlers.contains(handler)) {
            throw new SubscriberAlreadyRegisteredException(String.format("Handler %s.%s%s is already registered",
                    handler.getClass().getName(),
                    handler.callee.getName(),
                    Util.signatureOf(handler.callee)));
        }

        handlers.add(searchIndex(handlers, handler), handler);
        send(new SubscriberRegisterEvent(handler));
    }

    private int searchIndex(List<Handler> handlers, Handler handler) {
        // Binary search algorithm
        int low = 0;
        int high = handlers.size() - 1;

        while (low < high) {
            int mid = (low + high) >>> 1;
            int priority = handlers.get(mid).priority;

            if (priority > handler.priority) {
                low = mid + 1;
            } else if (priority < handler.priority) {
                high = mid - 1;
            } else {
                return mid;
            }
        }

        return low;
    }

    /**
     * Sends a message to all handlers listening for it.
     *
     * @param o The message to send. Can be any object.
     */
    public void send(Object o) {
        try {
            Class<?> aClass = o.getClass();
            List<Handler> handlers = eventMap.get(aClass);
            if (handlers == null) return;
            for (Handler handler : handlers) {
                handler.invoke(o);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes all handlers from the handler class of object {@code instance}
     *
     * @param instance The instance of the handler class, whose handlers to remove
     *
     * @implNote This calls {@link #unregister(Class)} with the class type of {@code instance}
     */
    public void unregister(Object instance) {
        unregister(instance.getClass());
    }

    /**
     * Removes all handlers from the class {@code i}
     *
     * @param i The class owning the handlers which to remove
     */
    public void unregister(Class<?> i) {
        this.eventMap.values().forEach(list -> list.removeIf(handler -> handler.ownerClass == i));
        send(new SubscriberUnregisterEvent(i));
    }

    /**
     * Registers all methods annotated with {@link MessageSubscription} as handlers for specific messages.<p>
     * Note that all handlers must have exactly one argument of the type of message they wish to listen for, and no return type (V)<p>
     * A method handler not following these rules will cause an {@link InvalidSubscriberException} to be thrown, explaining which method violated the rule, and how.
     *
     * @param instance The instance of the class containing the handler methods.
     */
    public void registerSubscribers(Object instance) {
        Class<?> aClass = instance.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            MessageSubscription annotation = declaredMethod.getAnnotation(MessageSubscription.class);
            if (annotation == null) {
                continue;
            }
            Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new InvalidSubscriberException(String.format("Handler %s.%s%s has an invalid signature: Expected 1 argument, found %s",
                    aClass.getName(),
                    declaredMethod.getName(),
                    Util.signatureOf(declaredMethod),
                    parameterTypes.length));
            }
            Class<?> listenerType = parameterTypes[0];
            Handler handler = new Handler(instance, aClass, declaredMethod, listenerType, annotation.priority());
            register(handler);
        }
    }

    /**
     * A message handler
     *
     * @param ownerInstance    The instance of the owning class, or null if handler is static
     * @param ownerClass       The type of the owning class
     * @param callee           The handler method
     * @param subscriptionType The object type being subscribed to
     * @param priority         The priority of this handler
     */
    public record Handler(Object ownerInstance, Class<?> ownerClass, Method callee, Class<?> subscriptionType, int priority) {
        /**
         * Invokes this handler
         *
         * @param message The message being sent to this handler
         *
         * @throws InvocationTargetException When {@link Method#invoke(Object, Object...)} throws
         * @throws IllegalAccessException    When {@link Method#invoke(Object, Object...)} throws
         * @implNote The object {@code message} is not instance checked with {@link #subscriptionType}, and may be an arbitrary object.
         */
        public void invoke(Object message) throws InvocationTargetException, IllegalAccessException {
            callee.setAccessible(true);
            callee.invoke(ownerInstance, message);
        }

        @Override
        public String toString() {
            return "Handler{" + "ownerInstance=" + ownerInstance + ", ownerClass=" + ownerClass + ", callee=" + callee + ", subscriptionType=" + subscriptionType + ", priority=" + priority + '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Handler h)) {
                return false;
            }
            return callee.equals(h.callee);
        }
    }
}
