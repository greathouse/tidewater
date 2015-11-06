package greenmoonsoftware.tidewater.runtime
import greenmoonsoftware.tidewater.run.RunContext

class Start {
    static void main(String[] args) {
        new Start().start('''\
//            parameters ([
//                'gitBranch'
//            ])
//
//            properties {
//                gitUrl 'https://github.com/greathouse/green-tea-test.git'
//            }
//
//            step checkout(type: GitClone) {
//                url context.gitUrl
//                dir 'green-tea-test'
//                ref parameters.gitBranch
//            }
//
//            step buildProject(type: Gradle) {
//                workingDir checkout.dir
//                tasks 'clean build'
//            }
//
//            step custom {
//                println 'Workspace: ' + context.workspace
//                println 'Checkout dir: ' + checkout.dir
//                println 'Sha from checkout: ' + checkout.sha
//            }
//
//            step shell(type: Shell) {
//                env (['name': 'Robert'])
//                contents \'\'\'\\
//                    #!/bin/bash
//                    echo hello $name, from the shell
//                    whoami
//                \'\'\'.stripIndent()
//            }
//
//            step copy(type: Shell) {
//                contents \'\'\'
//                    cp -R /Users/robert/projects/sterling/web .
//                    ls -l
//                \'\'\'.stripIndent()
//            }
//
//            step s3uploadFile(type: greenmoonsoftware.tidewater.plugins.aws.S3Upload) {
//                bucketName 'tidewater-test-bucket\'
//                keyName 'dir/something-totally-different\'
//                upload 'web/build.gradle\'
//            }
//
//            step s3uploadDirectory(type: greenmoonsoftware.tidewater.plugins.aws.S3Upload) {
//                bucketName 'tidewater-test-bucket\'
//                keyName 'myDir\'
//                upload 'web'
//            }
//
//            step s3DownloadFile(type: greenmoonsoftware.tidewater.plugins.aws.S3Download) {
//                bucketName 'tidewater-test-bucket'
//                keyName 'dir/something-totally-different'
//                destination 'something/dir/something-totally-different'
//            }
//
//            step s3DownloadDir(type: greenmoonsoftware.tidewater.plugins.aws.S3Download) {
//                bucketName 'tidewater-test-bucket\'
//                keyName 'myDir'
//                destination 'downloadDir'
//                isDirectory true
//            }
//
//            step ls(type: Shell) {
//                contents \'\'\'
//                    tree
//                \'\'\'.stripIndent()
//            }
            step s3Copy(type: greenmoonsoftware.tidewater.plugins.aws.S3Copy) {
                sourceBucketName 'tidewater-test-bucket\'
                sourceKeyName 'dir/something-totally-different'
                destinationBucketName 'tidewater-test-bucket-copy\'
                destinationKeyName 'dir/something-totally-different'
            }
        '''.stripIndent())
    }

    public void start(String script) {
        def context = new RunContext()
        context.addEventSubscribers(new StdoutLoggingSubscriber())
        context.execute(script)
    }


}
