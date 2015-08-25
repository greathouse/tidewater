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
}
