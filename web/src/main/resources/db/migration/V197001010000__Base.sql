create table PipelineEvents (
    id VARCHAR,
    aggregateId VARCHAR,
    eventType VARCHAR,
    eventDateTime TIMESTAMP,
    savedTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    data BLOB
);

create table Pipeline_View (
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

create table Pipeline_Context_View (
    context_Id VARCHAR not null,
    pipeline_Name VARCHAR not null,
    status int not null,
    start_Time TIMESTAMP not null,
    end_Time TIMESTAMP not null
);