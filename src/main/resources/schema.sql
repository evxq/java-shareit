DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

--CREATE TABLE IF NOT EXISTS users (
--    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--    name VARCHAR(255) NOT NULL,
--    email VARCHAR(512) NOT NULL,
--    CONSTRAINT pk_user PRIMARY KEY (id),
--    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
--);

CREATE TABLE IF NOT EXISTS users ( id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, name VARCHAR(255) NOT NULL, email VARCHAR(512) NOT NULL, CONSTRAINT pk_user PRIMARY KEY (id), CONSTRAINT UQ_USER_EMAIL UNIQUE (email));

--CREATE TABLE IF NOT EXISTS items (
--    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--    name VARCHAR(255) NOT NULL,
--    description VARCHAR(512) NOT NULL,
--    owner_id BIGINT,
--    is_available BOOLEAN,
--    request_id BIGINT,
--    CONSTRAINT pk_item PRIMARY KEY (id),
--    CONSTRAINT fk_item_user FOREIGN KEY (owner_id) REFERENCES users (id)
--);

CREATE TABLE IF NOT EXISTS items ( id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, name VARCHAR(255) NOT NULL, description VARCHAR(512) NOT NULL, owner_id BIGINT, is_available BOOLEAN, request_id BIGINT, CONSTRAINT pk_item PRIMARY KEY (id), CONSTRAINT fk_item_user FOREIGN KEY (owner_id) REFERENCES users (id));

--CREATE TABLE IF NOT EXISTS bookings (
--id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
--start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
--end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
--item_id BIGINT NOT NULL,
--booker_id BIGINT NOT NULL,
--status varchar(50),
--CONSTRAINT pk_booking PRIMARY KEY (id),
--CONSTRAINT fk_booking_item FOREIGN KEY (item_id) REFERENCES items (id),
--CONSTRAINT fk_booking_user FOREIGN KEY (booker_id) REFERENCES users (id));

CREATE TABLE IF NOT EXISTS bookings ( id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL, item_id BIGINT NOT NULL, booker_id BIGINT NOT NULL, status varchar(50), CONSTRAINT pk_booking PRIMARY KEY (id), CONSTRAINT fk_booking_item FOREIGN KEY (item_id) REFERENCES items (id), CONSTRAINT fk_booking_user FOREIGN KEY (booker_id) REFERENCES users (id));

CREATE TABLE IF NOT EXISTS requests ( id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, description VARCHAR(512) NOT NULL, requestor_id BIGINT NOT NULL, CONSTRAINT pk_request PRIMARY KEY (id), CONSTRAINT fk_request_user FOREIGN KEY (requestor_id) REFERENCES users (id));

CREATE TABLE IF NOT EXISTS comments ( id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, text VARCHAR(1000), item_id BIGINT NOT NULL, author_id BIGINT NOT NULL, created TIMESTAMP WITHOUT TIME ZONE NOT NULL, CONSTRAINT pk_comment PRIMARY KEY (id), CONSTRAINT fk_comment_item FOREIGN KEY (item_id) REFERENCES items (id), CONSTRAINT fk_comment_user FOREIGN KEY (author_id) REFERENCES users (id));