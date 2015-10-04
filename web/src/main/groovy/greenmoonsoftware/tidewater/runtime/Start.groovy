package greenmoonsoftware.tidewater.runtime
import greenmoonsoftware.tidewater.run.RunContext

class Start {
    static void main(String[] args) {
        new Start().start('''\
            parameters ([
                'gitBranch'
            ])

            properties {
                gitUrl 'https://github.com/greathouse/green-tea-test.git'
            }

            step checkout(type: GitClone) {
                url context.gitUrl
                dir 'green-tea-test'
                ref parameters.gitBranch
            }

            step buildProject(type: Gradle) {
                workingDir checkout.dir
                tasks 'clean build'
            }

            step custom {
                println 'Workspace: ' + context.workspace
                println 'Checkout dir: ' + checkout.dir
                println 'Sha from checkout: ' + checkout.sha
            }

            step shell(type: Shell) {
                env (['name': 'Robert'])
                contents \'\'\'\\
                    #!/bin/bash
                    echo hello $name, from the shell
                    whoami
                \'\'\'.stripIndent()
            }
        '''.stripIndent())
    }

    public void start(String script) {
        def context = new RunContext()
        context.addEventSubscribers(new StdoutLoggingSubscriber())
        context.execute(script)
    }


}