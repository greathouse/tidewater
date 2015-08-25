package greenmoonsoftware.tidewater.archive.details

import greenmoonsoftware.tidewater.config.Tidewater

class ArchiveDetailsService {

    ArchiveDetails retrieve(String name) {
        def workspaceDir = new File(Tidewater.WORKSPACE_ROOT, name)
        return new ArchiveDetails(
                name: name,
                script: new File(workspaceDir, 'script.tw').text,
                log: new File(workspaceDir, 'log.txt').text
        )

    }

    static void main(String[] args) {
        def details = new ArchiveDetailsService().retrieve('201508192245380826')
        println details.name
        println details.script
        println details.log
    }
}
