package greenmoonsoftware.es.samples.user;

import greenmoonsoftware.es.event.Aggregate;
import greenmoonsoftware.es.event.EventApplier;
import greenmoonsoftware.es.event.Event;
import greenmoonsoftware.es.event.EventList;
import greenmoonsoftware.es.samples.user.commands.CreateUserCommand;
import greenmoonsoftware.es.samples.user.commands.VerifyUserCommand;

import java.util.Arrays;
import java.util.Collection;

public class User implements Aggregate {
    public String getFullname() {
        return fullname;
    }

    public State getState() {
        return state;
    }

    @Override
    public String getId() {
        return id;
    }

    public Collection<Event> handle(CreateUserCommand command) {
        return Arrays.asList(new UserCreatedEvent(command.getAggregateId(), command.fullname));
    }

    public Collection<Event> handle(VerifyUserCommand command) {
        return Arrays.asList(new UserVerifiedEvent(command.getAggregateId()));
    }

    @Override
    public void apply(EventList events) {
        events.forEach((event) -> EventApplier.apply(this, event));
    }

    private void handle(UserVerifiedEvent event) {
        state = State.VERIFIED;
    }

    private void handle(UserCreatedEvent event) {
        id = event.getAggregateId();
        fullname = event.getFullname();
        state = State.REGISTERED;
    }

    private String id;
    private String fullname;
    private State state;

    public enum State {
        REGISTERED, VERIFIED, DISABLED, SUSPENDED;
    }
}
