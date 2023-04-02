DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	email VARCHAR(50) NOT NULL,
	name VARCHAR(50) NOT NULL,
	CONSTRAINT pk_user PRIMARY KEY (id),
	CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	description VARCHAR NOT NULL,
	requestor_id BIGINT NOT NULL,
	created TIMESTAMP WITHOUT TIME ZONE,
	CONSTRAINT pk_requests PRIMARY KEY (id),
	CONSTRAINT fk_requests_user FOREIGN KEY (requestor_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS items (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	name VARCHAR(50) NOT NULL,
	description VARCHAR NOT NULL,
	available BOOLEAN NOT NULL,
	user_id BIGINT NOT NULL,
	request_id BIGINT,
	CONSTRAINT pk_item PRIMARY KEY (id),
	CONSTRAINT fk_item_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_item_request FOREIGN KEY (request_id) REFERENCES requests (id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    item_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT pk_bookings PRIMARY KEY (id),
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_booking_item FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	text VARCHAR NOT NULL,
	item_id BIGINT NOT NULL,
	author_id BIGINT NOT NULL,
	created TIMESTAMP WITHOUT TIME ZONE,
	CONSTRAINT pk_comments PRIMARY KEY (id),
	CONSTRAINT fk_comments_user FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
	CONSTRAINT fk_comments_item FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE SET NULL ON UPDATE CASCADE
);