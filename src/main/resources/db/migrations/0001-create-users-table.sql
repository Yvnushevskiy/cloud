
CREATE TABLE IF NOT EXISTS users (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     username VARCHAR(255) NOT NULL,
                                     password VARCHAR(255) NOT NULL
);
