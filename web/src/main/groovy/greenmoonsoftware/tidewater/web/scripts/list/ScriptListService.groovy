package greenmoonsoftware.tidewater.web.scripts.list

import greenmoonsoftware.tidewater.web.TidewaterServerProperties
import org.springframework.stereotype.Service

@Service
class ScriptListService {
    List<ScriptMetaData> getScripts() {
        TidewaterServerProperties.SCRIPT_REPO_DIRECTORY.listFiles(new FileFilter() {
            @Override
            boolean accept(File pathname) {
                return pathname.name.endsWith('.tw')
            }
        }).collect { script ->
            new ScriptMetaData(script)
        }
    }
}
