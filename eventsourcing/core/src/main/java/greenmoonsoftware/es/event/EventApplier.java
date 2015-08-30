package greenmoonsoftware.es.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class EventApplier {
    public static void apply(Object aggregate, Event event) {
        apply(aggregate, "handle", event);
    }

    public static void apply(Object aggregate, String method, Event event) {
        try {
            handle(aggregate, method, event);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    private static void handle(Object aggregate, String method, Event event) throws Throwable {
        Method handleMethod = findHandleMethod(aggregate, method, event);
        if (handleMethod == null) {
            return;
        }
        invokeMethod(handleMethod, aggregate, event);
    }

    private static void invokeMethod(Method handleMethod, Object aggregate, Event event) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.unreflect(handleMethod);
        mh.invoke(aggregate, event);
    }

    private static Method findHandleMethod(Object aggregate, String method, Event event) {
        Method handleMethod;
        try {
            handleMethod = aggregate.getClass().getDeclaredMethod(method, event.getClass());
            handleMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            handleMethod = null;
        }
        return handleMethod;
    }
}
