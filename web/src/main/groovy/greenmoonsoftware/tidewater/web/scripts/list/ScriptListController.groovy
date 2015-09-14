package greenmoonsoftware.tidewater.web.scripts.list
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class ScriptListController {
    @Autowired ScriptListService listService

    @RequestMapping(value ='/scripts/list', method = RequestMethod.GET)
    String index(Map<String, Object> model) {
        model['scripts'] = listService.scripts
        return 'scripts/list/index'
    }
}
