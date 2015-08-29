package greenmoonsoftware.es.samples.user.commands;

import greenmoonsoftware.es.command.Command;

public final class CreateUserCommand implements Command {
    public final String id;
    public final String fullname;

    public CreateUserCommand(String id, String fullname) {
        this.id = id;
        this.fullname = fullname;
    }

    @Override
    public String getAggregateId() {
        return id;
    }
}
