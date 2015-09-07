package greenmoonsoftware.tidewater.web

import greenmoonsoftware.tidewater.config.NewContext
import greenmoonsoftware.tidewater.runtime.StdoutLoggingSubscriber
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Application {
    static void main(String[] args) {
        def cli = new CliBuilder(usage: 'java -jar tidewater.jar [options]')
        cli.with {
            f longOpt: 'file', args: 1, required: false, 'Path to script file. Runs the script and exits.'
        }
        def opts = cli.parse(args)
        if (!opts) {
            cli.usage()
            System.exit(1)
        }

        def file = opts.f

        if (file) {
            executeFile(file)
        }
        else {
            SpringApplication.run(Application.class, args)
        }
    }

    static void executeFile(String filepath) {
        def script = new File(filepath).text
        def context = new NewContext()
        context.addEventSubscribers(new StdoutLoggingSubscriber())
        context.execute(script)
    }
}
