package greenmoonsoftware.tidewater.archive.list

import greenmoonsoftware.tidewater.config.Tidewater

class ArchiveListService {
    List<String> getNames() {
        new File(Tidewater.WORKSPACE_ROOT).listFiles(new FileFilter() {
            @Override
            boolean accept(File pathname) {
                return pathname.isDirectory()
            }
        }).collect {
            it.name
        }
    }

    static void main(String[] args) {
        new ArchiveListService().getNames().each {
            println it
        }
    }
}
