CREATE TABLE tickets (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(200)  NOT NULL,
    description VARCHAR(5000) NOT NULL,
    priority    VARCHAR(20)   NOT NULL,
    status      VARCHAR(20)   NOT NULL,
    assignee    VARCHAR(100)  NOT NULL,
    created_at  TIMESTAMP     NOT NULL,
    updated_at  TIMESTAMP     NOT NULL
);

CREATE INDEX idx_tickets_status ON tickets (status);
CREATE INDEX idx_tickets_created_at ON tickets (created_at);

CREATE TABLE comments (
    id         BIGSERIAL PRIMARY KEY,
    ticket_id  BIGINT        NOT NULL REFERENCES tickets (id),
    text       VARCHAR(2000) NOT NULL,
    created_at TIMESTAMP     NOT NULL
);

CREATE INDEX idx_comments_ticket_id ON comments (ticket_id);
