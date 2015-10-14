package greenmoonsoftware.tidewater.web.pipeline.view

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class PipelineView {
    @Id String name
    String script
}
