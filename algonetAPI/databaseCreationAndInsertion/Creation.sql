CREATE TABLE "problems" (
                            "id" integer PRIMARY KEY,
                            "title" varchar(20),
                            "body" text,
                            "restrictions" text,
                            "created_at" timestamp
);

CREATE TABLE "solutions" (
                             "id" integer PRIMARY KEY,
                             "user_id" integer,
                             "problem_id" integer,
                            "created_at" timestamp,
                             "code" text,
                             "grade" integer
);

CREATE TABLE "users" (
                         "id" integer PRIMARY KEY,
                         "username" varchar(20),
                         "password" varchar,
                         "email" varchar,
                         "role" varchar,
                         "created_at" timestamp
);

CREATE TABLE "user_ratings" (
                                "user_id" integer,
                                "tag_id" integer,
                                "grade" integer,
                                PRIMARY KEY ("user_id", "tag_id")
);

CREATE TABLE "problem_ratings" (
                                   "problem_id" integer,
                                   "tag_id" integer,
                                   "grade" integer,
                                   PRIMARY KEY ("problem_id", "tag_id")
);

CREATE TABLE "tags" (
                        "id" integer PRIMARY KEY,
                        "name" varchar
);

ALTER TABLE "solutions" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "solutions" ADD FOREIGN KEY ("problem_id") REFERENCES "problems" ("id");

ALTER TABLE "user_ratings" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "user_ratings" ADD FOREIGN KEY ("tag_id") REFERENCES "tags" ("id");

ALTER TABLE "problem_ratings" ADD FOREIGN KEY ("problem_id") REFERENCES "problems" ("id");

ALTER TABLE "problem_ratings" ADD FOREIGN KEY ("tag_id") REFERENCES "tags" ("id");

drop table solutions