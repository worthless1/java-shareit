-- DROP TABLE IF EXISTS COMMENTS CASCADE;
-- DROP TABLE IF EXISTS BOOKINGS CASCADE;
-- DROP TABLE IF EXISTS ITEMS CASCADE;
-- DROP TABLE IF EXISTS REQUESTS CASCADE;
-- DROP TABLE IF EXISTS USERS CASCADE;

CREATE TABLE IF NOT EXISTS USERS
(
    user_id BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) PRIMARY KEY,
    name    VARCHAR(32) NOT NULL,
    email   VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS REQUESTS
(
    request_id   BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) PRIMARY KEY,
    description  VARCHAR(512)                NOT NULL,
    requestor_id BIGINT                      NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_request_to_users FOREIGN KEY (requestor_id) REFERENCES users (user_id) ON UPDATE RESTRICT ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ITEMS
(
    item_id     BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) PRIMARY KEY,
    owner_id    BIGINT        NOT NULL,
    name        VARCHAR(64)   NOT NULL,
    description VARCHAR(1024) NOT NULL,
    available   BOOLEAN       NOT NULL,
    request_id  BIGINT,
    CONSTRAINT items_fk FOREIGN KEY (owner_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT ITEMS_REQUESTS_fk FOREIGN KEY (request_id) REFERENCES REQUESTS (request_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS BOOKINGS
(
    booking_id BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) PRIMARY KEY,
    item_id    BIGINT                      NOT NULL,
    booker_id  BIGINT                      NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status     VARCHAR(10)                 NOT NULL,
    CONSTRAINT bookings_items_fk FOREIGN KEY (item_id) REFERENCES items (item_id) ON DELETE CASCADE,
    CONSTRAINT bookings_users_fk FOREIGN KEY (booker_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS COMMENTS
(
    comment_id BIGINT GENERATED ALWAYS AS IDENTITY (START WITH 1) PRIMARY KEY,
    text       VARCHAR(512)                NOT NULL,
    item_id    BIGINT                      NOT NULL,
    author_id  BIGINT                      NOT NULL,
    created    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT comments_items_fk FOREIGN KEY (item_id) REFERENCES items (item_id) ON DELETE CASCADE,
    CONSTRAINT comments_users_fk FOREIGN KEY (author_id) REFERENCES users (user_id) ON DELETE CASCADE
);