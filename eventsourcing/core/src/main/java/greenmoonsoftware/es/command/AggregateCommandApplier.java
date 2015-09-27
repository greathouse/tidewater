package greenmoonsoftware.es.command;

import greenmoonsoftware.es.event.Aggregate;
import greenmoonsoftware.es.event.Event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Collection;

public class AggregateCommandApplier {
    public static Collection<Event> apply(Aggregate aggregate, Command command) {
        return apply(aggregate, "handle", command);
    }

    public static Collection<Event> apply(Aggregate aggregate, String method, Command command) {
        try {
            return invoke(aggregate, method, command);
        } catch (Throwable throwable) {
            throw throwable instanceof RuntimeException ? (RuntimeException)throwable : new RuntimeException(throwable);
        }
    }

    private static Collection<Event> invoke(Aggregate aggregate, String method, Command command) throws Throwable {
        Method handleMethod = aggregate.getClass().getDeclaredMethod(method, command.getClass());
        handleMethod.setAccessible(true);
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.unreflect(handleMethod);
        return (Collection<Event>) mh.invoke(aggregate, command);
    }
}
