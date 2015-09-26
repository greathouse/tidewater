package greenmoonsoftware.tidewater.web.pipeline.view

interface PipelineViewQueryService {
    String getScript(String name)
    List<PipelineView> getPipelines()
}
