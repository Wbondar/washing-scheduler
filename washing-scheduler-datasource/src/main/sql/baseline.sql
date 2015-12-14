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
      id          SMALLSERIAL  NOT NULL PRIMARY KEY
    , owner_id    SMALLINT     NOT NULL REFERENCES users (id)
    , provider_id SMALLINT     NOT NULL REFERENCES account_providers (id)
    , external_id VARCHAR(100) NOT NULL 
    , UNIQUE (owner_id, provider_id)
    , UNIQUE (provider_id, external_id)
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
      id                   SERIAL    NOT NULL PRIMARY KEY
    , machine_id           SMALLINT  NOT NULL
    , client_id            SMALLINT  NOT NULL
    , occured_at           TIMESTAMP NOT NULL
    , effective_at         TIMESTAMP NOT NULL CHECK (occured_at <= effective_at) 
    , reservation_duration INTERVAL  NOT NULL
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
SELECT
      r.id
    , r.machine_id
    , r.client_id
    , r.occured_at
    , r.effective_at
    , r.effective_at + r.reservation_duration AS finishes_at
    , r.reservation_duration
    , FALSE AS is_canceled
FROM requests AS r LEFT JOIN requests_canceled ON r.id = requests_canceled.request_id
ORDER BY effective_at, machine_id DESC  
; 

CREATE VIEW effective_reservations AS
SELECT * 
FROM reservations 
WHERE is_canceled IS FALSE
;

CREATE VIEW current_reservations AS 
SELECT * 
FROM effective_reservations 
WHERE (effective_at + reservation_duration) > NOW()
;

/* 
 * TODO Implement proper mechanizm for detecting 
 * available machines and machines out of order. 
 */
CREATE VIEW available_machines AS 
SELECT machines.*, COUNT(effective_reservations.id) AS load
FROM machines 
    LEFT JOIN effective_reservations ON machines.id = effective_reservations.machine_id
GROUP BY machines.id
ORDER BY load
;

CREATE VIEW service_accounts AS
 SELECT accounts.owner_id,
    concat(account_providers.meta, accounts.external_id) AS account
   FROM (accounts
     JOIN account_providers ON ((accounts.provider_id = account_providers.id)))
;

CREATE RULE machine_manufacturers_on_insert_title_to_upper_case AS ON INSERT
    TO machine_manufacturers
    DO INSTEAD INSERT INTO machine_manufacturers (id, title) SELECT NEW.id, UPPER(TRIM(NEW.title)) 
;

CREATE RULE machine_manufacturers_on_update_title_to_upper_case AS ON UPDATE
    TO machine_manufacturers
    DO INSTEAD UPDATE machine_manufacturers SET id = NEW.id, title = UPPER(TRIM(NEW.title)) WHERE id = OLD.id
;

CREATE FUNCTION user_create(arg_account_provider_meta character varying, arg_external_account_id character varying) RETURNS smallint
    LANGUAGE plpgsql STRICT SECURITY DEFINER
    AS $$
DECLARE 
    var_new_user_id SMALLINT;
BEGIN
    INSERT INTO users (id) VALUES (DEFAULT)
    RETURNING (SELECT id INTO var_new_user_id)
    ;
    INSERT INTO accounts (id, owner_id, provider_id, external_id) 
    VALUES
    ( 
          DEFAULT
        , var_new_user_id
        , (SELECT id FROM account_providers WHERE account_providers.meta = arg_account_provider_meta LIMIT 1)
        , arg_external_account_id
    )
    ;
    RETURN var_new_user_id
    ;
END
;
$$;
/*
 * Makes request for machine with the lowest load for specified time for specified user.
 */
CREATE FUNCTION request_create(arg_user_id SMALLINT, arg_reservation_duration INTERVAL) 
RETURNS INTEGER
LANGUAGE PLpgSQL
STRICT
SECURITY DEFINER
AS
$$
DECLARE 
    var_request_id   INTEGER   = NULL;
    var_machine_id   SMALLINT  = (SELECT id FROM available_machines WHERE load = (SELECT MIN(load) FROM available_machines) LIMIT 1);
    var_effective_at TIMESTAMP = NOW();
BEGIN
	IF (SELECT TRUE FROM requests WHERE machine_id = var_machine_id AND (effective_at, reservation_duration) OVERLAPS (var_effective_at, arg_reservation_duration) LIMIT 1) THEN
        SELECT finishes_at INTO var_effective_at
        FROM effective_reservations
        WHERE machine_id = var_machine_id
        ;
	END IF 
	;
    INSERT INTO requests (id, client_id, machine_id, occured_at, effective_at, reservation_duration)
    VALUES 
    (
          DEFAULT
        , arg_user_id
        , var_machine_id
        , NOW()
        , var_effective_at
        , arg_reservation_duration
    )
    RETURNING (SELECT id INTO var_request_id)
    ; 
    RETURN var_request_id
    ;
END
;
$$
;

COMMIT
;
