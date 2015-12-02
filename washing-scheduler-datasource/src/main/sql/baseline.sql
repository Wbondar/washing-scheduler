START TRANSACTION
;

CREATE TABLE machine_manufacturers
(
      id    SMALLSERIAL  NOT NULL PRIMARY KEY
    , title VARCHAR(100) NOT NULL UNIQUE
)
;

CREATE TABLE machine_models
(
      id              SMALLSERIAL  NOT NULL PRIMARY KEY
    , manufacturer_id SMALLINT     NOT NULL REFERENCES machine_manufacturers (id)
    , code            VARCHAR(100) NOT NULL UNIQUE
)
;

CREATE TABLE machines
(
      id              SMALLSERIAL NOT NULL PRIMARY KEY
    , model_id        SMALLINT    NOT NULL REFERENCES machine_models (id)
)
;

CREATE TABLE machine_failures
(
      id          SMALLSERIAL NOT NULL PRIMARY KEY
    , machine_id  SMALLINT    NOT NULL REFERENCES machines (id)
    , occured_at  TIMESTAMP   NOT NULL
    , UNIQUE (machine_id, occured_at)
)
;

CREATE TABLE machine_comebacks
(
      failure_id SMALLINT  NOT NULL PRIMARY KEY
    , occured_at TIMESTAMP NOT NULL
    , FOREIGN KEY (failure_id) REFERENCES machine_failures (id)
)
;

CREATE TABLE users
(
    id SMALLSERIAL NOT NULL PRIMARY KEY
)
;

CREATE TABLE account_providers
(
      id   SMALLSERIAL NOT NULL PRIMARY KEY 
    , meta VARCHAR(10) NOT NULL UNIQUE
)
;

CREATE TABLE accounts
(
      id          SMALLSERIAL NOT NULL PRIMARY KEY 
    , provider_id SMALLINT    NOT NULL REFERENCES account_providers (id)
    , external_id BIGINT      NOT NULL
    , UNIQUE (provider_id, external_id)
)
;

CREATE TABLE user_accounts 
(
      user_id    SMALLINT NOT NULL REFERENCES users (id)
    , account_id SMALLINT NOT NULL REFERENCES accounts (id)
    , PRIMARY KEY (user_id, account_id)
)
;

CREATE TABLE contact_types
(
      id   SMALLSERIAL NOT NULL PRIMARY KEY
    , meta VARCHAR(10) NOT NULL UNIQUE
)
;

CREATE TABLE contacts
(
      id       SMALLSERIAL  NOT NULL PRIMARY KEY
    , type_id  SMALLINT     NOT NULL REFERENCES contact_types (id)
    , owner_id SMALLINT     NOT NULL REFERENCES users (id)
    , content  VARCHAR(100) NOT NULL UNIQUE
)
;

CREATE TABLE requests
(
      id           SERIAL    NOT NULL PRIMARY KEY
    , machine_id   SMALLINT  NOT NULL
    , client_id    SMALLINT  NOT NULL
    , requested_at TIMESTAMP NOT NULL CHECK (requested_at < locked_at)
    , locked_at    TIMESTAMP NOT NULL CHECK (locked_at < unlocked_at)
    , unlocked_at  TIMESTAMP NOT NULL
)
;

CREATE TABLE requests_canceled
(
      request_id    INTEGER   NOT NULL PRIMARY KEY
    , cancelator_id SMALLINT  NOT NULL REFERENCES users (id)
    , canceled_at   TIMESTAMP NOT NULL
    , FOREIGN KEY (request_id)    REFERENCES requests (id)
)
;

CREATE VIEW reservations AS 
SELECT requests.*, FALSE AS is_canceled
FROM requests LEFT JOIN requests_canceled ON requests.id = requests_canceled.request_id
ORDER BY machine_id, locked_at DESC  
; 

CREATE VIEW effective_reservations AS
SELECT * 
FROM reservations 
WHERE is_canceled IS FALSE
;

CREATE VIEW current_reservations AS 
SELECT * 
FROM effective_reservations 
WHERE unlocked_at > NOW()
;

CREATE VIEW available_machines AS 
SELECT machines.*, COUNT(effective_reservations.id) AS load
FROM machines 
    LEFT JOIN machine_failures ON machines.id = machine_failures.machine_id
    RIGHT JOIN machine_comebacks ON machine_failures.id = machine_comebacks.failure_id
    JOIN effective_reservations ON effective_reservations.machine_id = machines.id
GROUP BY machines.id
ORDER BY load ASC
;

CREATE RULE machine_manufacturers_on_insert_title_to_upper_case AS ON INSERT
    TO machine_manufacturers
    DO INSTEAD INSERT INTO machine_manufacturers (id, title) SELECT NEW.id, UPPER(TRIM(NEW.title)) 
;

CREATE RULE machine_manufacturers_on_update_title_to_upper_case AS ON UPDATE
    TO machine_manufacturers
    DO INSTEAD UPDATE machine_manufacturers SET id = NEW.id, title = UPPER(TRIM(NEW.title)) WHERE id = OLD.id
;

COMMIT
;
