DROP TABLE IF EXISTS rate;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS resume;
DROP TABLE IF EXISTS freshen_goal;
DROP TABLE IF EXISTS freshen;
DROP TABLE IF EXISTS users CASCADE;


DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name       VARCHAR                           NOT NULL,
    email      VARCHAR                           NOT NULL,
    password   VARCHAR                           NOT NULL,
    registered TIMESTAMP           DEFAULT now() NOT NULL,
    enabled    BOOLEAN             DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE freshen
(
    id            INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    recorded_date TIMESTAMP           DEFAULT now() NOT NULL,
    language      TEXT                              NOT NULL,
    level         TEXT                              NOT NULL,
    workplace     TEXT                              NOT NULL,
    user_id       INTEGER,
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE freshen_goal
(
    freshen_id INTEGER NOT NULL,
    goal       VARCHAR,
    FOREIGN KEY (freshen_id) REFERENCES freshen (id) ON DELETE CASCADE
);

CREATE TABLE resume
(
    id           INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    title        VARCHAR   NOT NULL,
    name         VARCHAR   NOT NULL,
    age          VARCHAR   NOT NULL,
    address      VARCHAR   NOT NULL,
    salary       INTEGER   NOT NULL,
    work_before  VARCHAR   NOT NULL,
    url          VARCHAR   NOT NULL,
    skills       VARCHAR   NOT NULL,
    release_date TIMESTAMP NOT NULL,
    freshen_id   INTEGER   NOT NULL,
    CONSTRAINT resume_idx UNIQUE (title, work_before),
    FOREIGN KEY (freshen_id) REFERENCES freshen (id) ON DELETE CASCADE
);

CREATE TABLE vote
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    local_date TIMESTAMP NOT NULL,
    resume_id  INTEGER   NOT NULL,
    user_id    INTEGER   NOT NULL,
    CONSTRAINT votes_idx UNIQUE (resume_id, user_id),
    FOREIGN KEY (resume_id) REFERENCES RESUME (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE rate
(
    id        INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name               TEXT                 NOT NULL,
    value              DOUBLE PRECISION     NOT NULL,
    local_date         TIMESTAMP            NOT NULL,
    CONSTRAINT rate_idx UNIQUE (name)
);
