package greenmoonsoftware.tidewater.web.pipeline.view

interface ViewQueryService {
    String getScript(String name)
    List<PipelineView> getPipelines()
}
