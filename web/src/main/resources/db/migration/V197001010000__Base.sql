create table PipelineEvents (
    id VARCHAR,
    aggregateId VARCHAR,
    eventType VARCHAR,
    eventDateTime TIMESTAMP,
    savedTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    data BLOB
);

create table Pipeline (
    id VARCHAR,
    name VARCHAR,
    script CLOB
);

create table PipelineRunEvents (
    id VARCHAR,
    aggregateId VARCHAR,
    eventType VARCHAR,
    eventDateTime TIMESTAMP,
    savedTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    data BLOB
);

create table PipelineRun (
    pipelineName VARCHAR,
    contextId VARCHAR,
    script VARCHAR,
    startTime TIMESTAMP,
    endTime TIMESTAMP
);