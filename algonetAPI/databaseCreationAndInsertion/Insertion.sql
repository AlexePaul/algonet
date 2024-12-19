INSERT INTO "users" ("id", "username", "password", "email", "role", "created_at")
VALUES
    (1, 'john_doe', 'password123', 'john.doe@example.com', 'admin', '2024-12-19 08:00:00'),
    (2, 'alice_smith', 'alicepass', 'alice.smith@example.com', 'user', '2024-12-19 09:00:00'),
    (3, 'bob_jones', 'bobpassword', 'bob.jones@example.com', 'user', '2024-12-19 10:00:00');

INSERT INTO "tags" ("id", "name")
VALUES
    (1, 'Arrays'),
    (2, 'Strings'),
    (3, 'Mathematics'),
    (4, 'Dynamic Programming'),
    (5, 'Sorting');

INSERT INTO "problems" ("id", "title", "body", "restrictions", "created_at")
VALUES
    (1, 'Find Maximum Subarray', 'Given an array of integers, find the subarray with the largest sum.', 'No size restriction.', '2024-12-19 08:00:00'),
    (2, 'Palindrome Check', 'Check if a string is a palindrome.', 'String length <= 1000', '2024-12-19 08:30:00'),
    (3, 'QuickSort', 'Implement the QuickSort algorithm to sort an array.', 'Array size <= 10000', '2024-12-19 09:00:00');

INSERT INTO "solutions" ("id", "user_id", "problem_id", "code")
VALUES
    (1, 1, 1, 'def max_subarray(arr): return max(arr)'),
    (2, 2, 2, 'def is_palindrome(s): return s == s[::-1]'),
    (3, 3, 3, 'def quicksort(arr): return sorted(arr)');

INSERT INTO "user_ratings" ("user_id", "tag_id", "grade")
VALUES
    (1, 1, 5),
    (2, 3, 4),
    (3, 2, 3);

INSERT INTO "problem_ratings" ("problem_id", "tag_id", "grade")
VALUES
    (1, 1, 5),
    (2, 2, 4),
    (3, 3, 3);
