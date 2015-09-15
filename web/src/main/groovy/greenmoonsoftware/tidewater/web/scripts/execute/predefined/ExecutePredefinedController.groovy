package greenmoonsoftware.tidewater.web.scripts.execute.predefined

import greenmoonsoftware.tidewater.web.TidewaterServerProperties
import greenmoonsoftware.tidewater.web.scripts.execute.ExecuteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ExecutePredefinedController {
    @Autowired ExecuteService executeService

    @RequestMapping(value = '/scripts/{script}/execute')
    String index(@PathVariable('script') String scriptName, ModelMap response) {
        def text = new File(TidewaterServerProperties.SCRIPT_REPO_DIRECTORY, scriptName + '.tw').text
        def context = executeService.execute(text)
        response.addAttribute('contextId', context.attributes.id)
        return 'scripts/execute/liveLog'
    }
}
