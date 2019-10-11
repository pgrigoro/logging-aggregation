CREATE TABLE IF NOT EXISTS logging.log_event
(
    date_id           INT,
    log_level         TEXT,
    host_ip           TEXT,
    app_name          TEXT,
    uuid              TEXT,
    user_name         TEXT,
    creation_date_utc TIMESTAMP,
    message           TEXT,
    version           INT,
    PRIMARY KEY ((date_id, app_name, log_level), creation_date_utc, uuid)
) WITH CLUSTERING ORDER BY (creation_date_utc DESC, uuid DESC);