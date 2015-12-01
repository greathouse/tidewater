# tidewater
Groovy DSL and web server for Creating Build Pipelines

## Motivation
I started this project because of the many frustrations while trying to use Jenkins as a deploy pipeline. Jenkins is a decent continuous **integration** server. But it fails as a continuous **deployment** server. Difficult things like:
* Pass artifacts downstream reliably
* Fail upstream builds if downstream fails
* Restart a pipeline if a step fails for external reasons
* Configuring/Passing/Refactoring parameters passed to downstream jobs
* Running concurrent pipelines without having filesystem collisions (`mvn clean`, `gradle clean`, etc)

## How Tidewater Attempts to Solve These Problems
* Use a dedicated, unique directory for each pipeline run (called a `context` in Tidewater).
* Each `step` in a `context` is executed in the same base directory.
* If a step fails, Tidewater allows for the `context` to be restarted. All of the existing state is saved and immutable.
* Since a pipeline is defined using a single groovy script, refactoring a pipeline should be easy and straightforward.

## Architecture Overview
There are only a few concepts in Tidewater.
### Pipelines
A pipeline is simply a definition of a workflow. The pipeline consists of a script that can be executed at any time. A pipeline could simply be thought of a template of work. It is **not** the work itself.
### Contexts
A Context is a specific run of a pipeline script. This is where the work of a pipeline is performed. When a context is started, the current pipeline script is saved and **will not change through the entire life of the context.**
### Steps
A step is a granular unit of work inside of a context. If a step fails or errors, it can be retried as many times as needed.
### Plugins
Plugins currently are only available to add new step types. All of the steps in Tidewater are via plugins even the built-in steps.


## Built-In Plugins
* Docker Support
* AWS S3
* Shell (ie: bash)
* Groovy
* Gradle
* Git

## Sample Scripts
```groovy
//Build Tidewater itself
step clone(type: 'greenmoonsoftware.tidewater.plugins.git.GitClone') {
    url 'git@github.com:greathouse/tidewater.git'
    dir 'tidewater'
}

step build(type: 'greenmoonsoftware.tidewater.plugins.gradle.Gradle') {
    workingDir clone.dir
    tasks 'installGulp npmInstall build'
}

step copyArtifact(type: 'greenmoonsoftware.tidewater.plugins.shell.Shell') {
    env ([
        toArtifactDir: "${context.workspace}/artifacts",
        artifact: "${context.workspace}/${clone.dir}/web/build/libs/web.jar"
    ])
    contents '''\
        mkdir -p $toArtifactDir
        cp $artifact $toArtifactDir
        ls -lah $toArtifactDir
    '''.stripIndent()
}
```

```groovy
//Docker Sample
properties {
    dockerHost 'tcp://192.168.99.100:2376'
    dockerCert '/Users/robert/.docker/machine/machines/default'
    dockerImage 'ubuntu:15.04'
}

step pull(type: 'greenmoonsoftware.tidewater.plugins.docker.DockerPull') {
    uri context.dockerHost //Properties are accessible via the context variable
    certPath context.dockerCert
    image context.dockerImage
}

step start(type: 'greenmoonsoftware.tidewater.plugins.docker.DockerStart') {
    uri context.dockerHost
    certPath context.dockerCert
    image context.dockerImage
    waitForCompletion false
    command (['/bin/sh', '-c', 'while true; do echo hello world; sleep 5; done'])
}

step sleep { //If no type is provided for the step, then plain 'ol groovy code is executed.
    def millisecondsToSleep = 10000
    println "Going to sleep for ${millisecondsToSleep / 1000} seconds..."
    sleep 10000
    println 'I am awake'
}

step stop(type: 'greenmoonsoftware.tidewater.plugins.docker.DockerStop') {
    uri context.dockerHost
    certPath context.dockerCert
    containerId start.containerId //Steps may have output variables. These are referenced by their step name. ie: "start"
}

step logs(type: 'greenmoonsoftware.tidewater.plugins.docker.DockerLogs') {
    uri context.dockerHost
    certPath context.dockerCert
    containerId start.containerId
    toFilePath "${context.workspace}/logs.txt" //Each individual execution of a pipeline (ie: context) has it's own workspace directory. No file system collisions with concurrent executions.
}

step catDockerLogToPipeline(type: 'greenmoonsoftware.tidewater.plugins.shell.Shell') {
    env (['fileToCat': logs.toFilePath]) //Previous step inputs are available using their step name. ie: "logs"
    contents '''\\
        #!/bin/bash
        cat $fileToCat
    '''.stripIndent()
}

step tag(type: 'greenmoonsoftware.tidewater.plugins.docker.DockerTag') {
    uri context.dockerHost
    certPath context.dockerCert
    imageToTag context.dockerImage
    repository 'local.docker:5000/tidewater/ubuntu'
    tag "${new Date().format('yyyyMMddHHmmss')}"
}

step push(type: 'greenmoonsoftware.tidewater.plugins.docker.DockerPush') {
    uri context.dockerHost
    certPath context.dockerCert
    image tag.repository
}
```

## Build Instructions
```
./gradlew installGulp npmInstall build
```

## Running
```
./gradlew web:{copyPlugins,bootRun}
```

## Development Architecture
* Event-based both server and client
* Eventsourcing & CQRS
* Package-by-feature
* AngularJS
* Groovy
* Spring-Boot
* Websocket
