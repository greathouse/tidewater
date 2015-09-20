package greenmoonsoftware.tidewater.web.pipeline.view

import greenmoonsoftware.es.event.Event
import greenmoonsoftware.es.event.EventApplier
import greenmoonsoftware.es.event.EventSubscriber
import greenmoonsoftware.tidewater.web.pipeline.events.PipelineCreatedEvent
import groovy.sql.Sql

import javax.sql.DataSource

class PipelineViewEventSubscriber implements EventSubscriber<Event> {
    private final DataSource ds
    private final Sql sql

    PipelineViewEventSubscriber(DataSource d) {
        ds = d
        sql = new Sql(d)
    }

    @Override
    void onEvent(Event event) {
        EventApplier.apply(this, event)
    }

    private void handle(PipelineCreatedEvent event) {
        sql.executeInsert("insert into pipeline (name, script) values (${event.aggregateId}, ${event.scriptText})")
    }
}
