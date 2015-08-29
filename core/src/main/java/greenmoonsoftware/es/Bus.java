package greenmoonsoftware.es;

public interface Bus<T, S> {
    void post(T payload);
    Bus<T, S> register(S subscriber);
    void unregister(S subscriber);
}
