package greenmoonsoftware.tidewater.context

class InvalidScriptException extends RuntimeException {
    InvalidScriptException() {
    }

    InvalidScriptException(String var1) {
        super(var1)
    }

    InvalidScriptException(String var1, Throwable var2) {
        super(var1, var2)
    }

    InvalidScriptException(Throwable var1) {
        super(var1)
    }

    InvalidScriptException(String var1, Throwable var2, boolean var3, boolean var4) {
        super(var1, var2, var3, var4)
    }
}
