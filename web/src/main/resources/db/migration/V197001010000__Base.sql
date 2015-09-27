create table PipelineEvents (
    id VARCHAR,
    aggregateId VARCHAR,
    eventType VARCHAR,
    eventDateTime TIMESTAMP,
    savedTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    data BLOB
);

create table Pipeline (
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
    contextId VARCHAR not null,
    pipelineName VARCHAR not null,
    status int not null,
    startTime TIMESTAMP not null,
    endTime TIMESTAMP not null
);