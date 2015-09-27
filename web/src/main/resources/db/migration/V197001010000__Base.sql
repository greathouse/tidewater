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

create table PipelineContext (
    pipelineName VARCHAR not null,
    contextId VARCHAR not null,
    status int not null,
    startTime TIMESTAMP not null,
    endTime TIMESTAMP not null
);