package greenmoonsoftware.es.samples.user.commands;

import greenmoonsoftware.es.command.Command;

public final class VerifyUserCommand implements Command {
    public final String aggregateId;

    public VerifyUserCommand(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }
}
