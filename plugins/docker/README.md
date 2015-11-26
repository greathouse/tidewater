properties {
    dockerHost 'tcp://192.168.99.100:2376'
    dockerCert '/Users/robert/.docker/machine/machines/default'
    dockerImage 'ubuntu:15.04'
}

step pull(type: 'greenmoonsoftware.tidewater.plugins.docker.DockerPull') {
    uri context.dockerHost
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

step sleep {
    sleep 10000
}

step stop(type: 'greenmoonsoftware.tidewater.plugins.docker.DockerStop') {
    uri context.dockerHost
    certPath context.dockerCert
    containerId start.containerId
}

step logs(type: 'greenmoonsoftware.tidewater.plugins.docker.DockerLogs') {
    uri context.dockerHost
    certPath context.dockerCert
    containerId start.containerId
    toFilePath "${context.workspace}/logs.txt"
}

step push(type: 'greenmoonsoftware.tidewater.plugins.docker.DockerPush') {
    uri context.dockerHost
    certPath context.dockerCert
    image 'robert/ubuntu'
}