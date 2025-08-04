DROP TABLE IF EXISTS tg_user;

-- Create tg_user table
CREATE TABLE tg_user (chat_id INTEGER NOT NULL,
                      username VARCHAR NOT NULL,
                      firstname VARCHAR,
                      lastname VARCHAR,
                      access_token VARCHAR,
                      refresh_token VARCHAR,
                      last_date_view_response BIGINT,
                      resume_subscription_id INTEGER,
                      last_date_vacancy BIGINT,
                      is_subscript BOOLEAN NOT NULL);