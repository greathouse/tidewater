package greenmoonsoftware.tidewater.web
import org.springframework.stereotype.Controller

@Controller
class IndexController {
//    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("time", new Date());
        model.put("message", 'Hi');
        return "index";
    }
}
