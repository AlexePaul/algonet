-- Optional test data for integration tests
-- This file can be loaded using @Sql("/test-data.sql")

INSERT INTO tags (id, name) VALUES (1, 'algorithms');
INSERT INTO tags (id, name) VALUES (2, 'data-structures');
INSERT INTO tags (id, name) VALUES (3, 'dynamic-programming');

-- Reset sequence for H2
ALTER SEQUENCE tags_seq RESTART WITH 4;
