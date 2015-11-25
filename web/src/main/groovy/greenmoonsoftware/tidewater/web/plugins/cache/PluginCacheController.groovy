package greenmoonsoftware.tidewater.web.plugins.cache

import greenmoonsoftware.tidewater.plugins.PluginClassLoaderCache
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class PluginCacheController {
    @RequestMapping(value="/plugins/reload", method = RequestMethod.GET)
    ResponseEntity index() {
        PluginClassLoaderCache.clear()
        return new ResponseEntity(HttpStatus.OK)
    }
}
