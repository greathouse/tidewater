package greenmoonsoftware.tidewater.config

class LogWriter  {
    private File logFile
    private PrintWriter writer

    LogWriter(File l) {
        logFile = l
        writer = logFile.newPrintWriter()
    }

    LogWriter println(String line) {
        System.out.println line
        writer.println(line)
        writer.flush()
    }
}
