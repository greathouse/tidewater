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
    lastSuccessfulRun TIMESTAMP,
    lastFailure TIMESTAMP,
    script CLOB
);