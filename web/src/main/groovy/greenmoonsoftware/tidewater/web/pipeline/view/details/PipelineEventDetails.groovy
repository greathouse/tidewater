package greenmoonsoftware.tidewater.web.pipeline.view.details
import greenmoonsoftware.es.event.Aggregate
import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventList

class PipelineEventDetails implements Aggregate {
    String pipelineName
    List<Event> events = []

    @Override
    String getId() {
        pipelineName
    }

    @Override
    void apply(EventList events) {
        events.forEach { this.events << it}
    }
}
