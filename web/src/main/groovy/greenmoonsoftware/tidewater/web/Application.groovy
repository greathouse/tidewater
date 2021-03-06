package greenmoonsoftware.tidewater.web
import greenmoonsoftware.tidewater.Tidewater
import greenmoonsoftware.tidewater.run.RunContext
import greenmoonsoftware.tidewater.runtime.StdoutLoggingSubscriber
import org.h2.jdbcx.JdbcDataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile

import javax.sql.DataSource
import java.nio.file.Files

@SpringBootApplication
class Application {
    static final Logger logger = LoggerFactory.getLogger(Application)

    static void main(String[] args) {
        def opts = parseArguments(args)
        def file = opts.f

        if (file) {
            executeFile(file)
        }
        else {
            logger.info('Tidewater Home set to {}', Tidewater.WORKSPACE_ROOT)
            TidewaterServerProperties.SCRIPT_REPO_DIRECTORY.mkdirs()
            def app = new SpringApplication(Application)
            app.setAdditionalProfiles('production')
            app.run(args)
        }
    }

    private static OptionAccessor parseArguments(String[] args) {
        def cli = new CliBuilder(usage: 'java -jar tidewater.jar [options]')
        cli.with {
            f longOpt: 'file', args: 1, required: false, 'Path to script file. Runs the script and exits.'
        }
        def opts = cli.parse(args)
        if (!opts) {
            cli.usage()
            System.exit(1)
        }
        opts
    }

    static void executeFile(String filepath) {
        def script = new File(filepath).text
        def context = new RunContext()
        context.addEventSubscribers(new StdoutLoggingSubscriber())
        context.execute(script)
    }

    @Bean
    @Profile('production')
    DataSource datasource() {
        def d = new JdbcDataSource()
        d.with {
            user = 'testuser'
            url = "jdbc:h2:${Tidewater.WORKSPACE_ROOT}/web/db".toString()
        }
        return d
    }

    @Bean
    @Profile('test')
    DataSource initalize() {
        def datasource = new JdbcDataSource()
        def jdbcUrl = "jdbc:h2:${Files.createTempDirectory('tidewater-test').toString()}/web".toString()
        logger.info("Database Url: ${jdbcUrl}")
        datasource.with {
            user = 'testuser'
            url = jdbcUrl
        }
        return datasource
    }

    @Bean
    PipelineContextContainer pipelineContextContainer() {
        new PipelineContextContainer()
    }
}
