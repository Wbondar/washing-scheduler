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
    , owner_id    SMALLINT     NOT NULL REFERENCES users (id)             ON DELETE RESTRICT ON UPDATE RESTRICT
    , provider_id SMALLINT     NOT NULL REFERENCES account_providers (id) ON DELETE RESTRICT ON UPDATE RESTRICT
    , external_id VARCHAR(100) NOT NULL 
    , token       TEXT         NOT NULL
    , updated_at  TIMESTAMP    NOT NULL
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
    , effective_at         TIMESTAMP NOT NULL CHECK (effective_at >= occured_at)
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

/*
 * `account_providers.meta` has to be in upper snake_case with alphanumeric characters only.
 * Function `format_as_account_provider_meta` cleans up input string to match the format.
 */
CREATE FUNCTION format_as_account_provider_meta(arg_input TEXT) RETURNS TEXT
LANGUAGE sql STRICT IMMUTABLE SECURITY INVOKER   
AS $$
SELECT UPPER(TRIM(regexp_replace(arg_input, '\W+', '', 'g')));
;
$$
;

/* 
 * Creates an account in the system for user by credentials, provided by OAuth 2.0 protocol.
 * Or updates token and external id of existing account.
 * Returns id of newly created or existing user.
 */
CREATE FUNCTION user_update(arg_account_provider_meta character varying, arg_external_account_id character varying, arg_token TEXT, OUT arg_user_id SMALLINT)
    LANGUAGE plpgsql STRICT SECURITY DEFINER
    AS $$
DECLARE 
    var_provider_id SMALLINT;
    var_account_id  SMALLINT;
BEGIN
	/* Fetch `provider_id` by meta name. */
    SELECT id INTO var_provider_id 
    FROM account_providers 
    WHERE meta = format_as_account_provider_meta(arg_account_provider_meta)
    ;
    /* Create new provider in case one with provided meta name does not exist. */
    IF NOT FOUND THEN
        INSERT INTO account_providers (id, meta) 
        VALUES (DEFAULT, format_as_account_provider_meta(arg_account_provider_meta))
        RETURNING (SELECT id INTO var_provider_id)
        ;
    END IF 
    ;
    /* Fetch account id by provider and external id. */
    SELECT id INTO var_account_id 
    FROM accounts 
    WHERE CONCAT(provider_id, external_id) = CONCAT(var_provider_id, arg_external_account_id)
    ;
    /* 
     * If account of given credentials exists, update it. 
     * Create one otherwise.
     */
    IF FOUND THEN 
        UPDATE accounts SET 
	          external_id = arg_external_account_id
	        , token = arg_token
	        , updated_at = NOW()
        WHERE id = var_account_id
        ;
        SELECT owner_id INTO arg_user_id 
        FROM accounts 
        WHERE id = var_account_id
        ;
    ELSE
        INSERT INTO users (id)
        VALUES (DEFAULT)
        RETURNING (SELECT id INTO arg_user_id)
        ;
	    INSERT INTO accounts (id, owner_id, provider_id, external_id, token, updated_at) 
	    VALUES
	    ( 
	          DEFAULT
	        , arg_user_id
	        , var_provider_id
	        , arg_external_account_id
	        , arg_token
	        , NOW()
	    )
	    ;
    END IF
    ;
END
;
$$;
/*
 * Makes request for machine with the lowest load for specified time for specified user.
 */
CREATE FUNCTION request_create(arg_user_id SMALLINT, arg_effective_at TIMESTAMP WITH TIME ZONE, arg_reservation_duration INTERVAL) 
RETURNS INTEGER
STRICT
LANGUAGE PLpgSQL
SECURITY DEFINER
AS
$$
DECLARE 
    var_request_id   INTEGER   = NULL;
    var_machine_id   SMALLINT  = (SELECT id FROM available_machines WHERE load = (SELECT MIN(load) FROM available_machines) LIMIT 1);
    var_effective_at TIMESTAMP = COALESCE(arg_effective_at, NOW());
BEGIN
	IF (SELECT TRUE FROM requests WHERE machine_id = var_machine_id AND (effective_at, reservation_duration) OVERLAPS (var_effective_at, arg_reservation_duration) LIMIT 1) THEN
        SELECT COALESCE(MAX(finishes_at), NOW()) INTO var_effective_at
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
