package greenmoonsoftware.es.command;

public class CommandNotAllowedException extends RuntimeException {
    public CommandNotAllowedException() {
    }

    public CommandNotAllowedException(String message) {
        super(message);
    }

    public CommandNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandNotAllowedException(Throwable cause) {
        super(cause);
    }

    public CommandNotAllowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
