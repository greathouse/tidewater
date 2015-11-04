package greenmoonsoftware.tidewater.web.pipeline.view

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface PipelineViewRepository extends CrudRepository<PipelineView, String> {

    @Transactional
    @Modifying
    @Query('update PipelineView v set v.script = :script where name = :name')
    void updateScript(@Param('name') String name, @Param('script') String script)
}
