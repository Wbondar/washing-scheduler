COPY users                 FROM '/home/wbond//Documents/Projects/washing-scheduler/washing-scheduler-datasource/src/test/resources/users.csv' DELIMITER ',';
COPY account_providers     FROM '/home/wbond//Documents/Projects/washing-scheduler/washing-scheduler-datasource/src/test/resources/account_providers.csv' DELIMITER ',';
COPY accounts              FROM '/home/wbond//Documents/Projects/washing-scheduler/washing-scheduler-datasource/src/test/resources/accounts.csv' DELIMITER ',';

INSERT INTO goody_types VALUES
(1, 'WASHMACHIN')
;

INSERT INTO goodies (id, type_id, meta) VALUES
  (1, 1, 'BLACK')
, (2, 1, 'WHITE')
;

INSERT INTO goody_statuses VALUES
  (1, 'AVAIL')
, (2, 'UNAVAIL')
;

INSERT INTO goody_status_modifications (id, effective_at, goody_id, status_id) VALUES
  (DEFAULT, NOW(), 1, 1)
, (DEFAULT, NOW(), 2, 1)
;