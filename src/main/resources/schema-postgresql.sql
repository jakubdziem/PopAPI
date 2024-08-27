-- -- Drop tables if they exist
-- DROP TABLE IF EXISTS YEAR_AND_POPULATION;
-- DROP TABLE IF EXISTS ARTIST;
-- DROP TABLE IF EXISTS COUNTRY;
--
-- -- Create the ARTIST table
-- CREATE TABLE ARTIST (
--                         id BIGSERIAL PRIMARY KEY,
--                         artist_name VARCHAR(255),
--                         lead_streams VARCHAR(255),
--                         image_url VARCHAR(255),
--                         last_update DATE
-- );
--
-- -- Create the COUNTRY table
-- CREATE TABLE COUNTRY (
--                          country_name VARCHAR(255) PRIMARY KEY,
--                          genc VARCHAR(255),
--                          flag_url VARCHAR(255)
-- );
--
-- Create the YEAR_AND_POPULATION table
-- CREATE TABLE YEAR_AND_POPULATION (
--                                      id BIGSERIAL PRIMARY KEY,
--                                      year_of_measurement VARCHAR(255),
--                                      population VARCHAR(255),
--                                      annual_growth VARCHAR(255),
--                                      country_name VARCHAR(255),
--                                      FOREIGN KEY (country_name) REFERENCES COUNTRY (country_name)
-- );
-- --
-- DROP TABLE stats;
-- DROP TABLE score;
-- DROP TABLE u_name;
-- DROP TABLE "user";
-- CREATE TABLE "user" (
--                         user_id VARCHAR(255) PRIMARY KEY,
--                         CONSTRAINT unique_user_id UNIQUE (user_id)
-- );
-- CREATE TABLE stats (
--                        user_id VARCHAR(255) PRIMARY KEY,
--                        total_game_played BIGINT,
--                        avg_score DECIMAL,
--                        time_played BIGINT,
--                        total_scored_points BIGINT,
--                        number_of_won_games INTEGER,
--                        CONSTRAINT fk_user
--                            FOREIGN KEY (user_id) REFERENCES "user" (user_id) ON DELETE CASCADE
-- );
-- CREATE TABLE score (
--                        id SERIAL PRIMARY KEY,
--                        best_score INTEGER,
--                        mode VARCHAR(255),
--                        user_id VARCHAR(255),
--                        CONSTRAINT fk_user
--                            FOREIGN KEY (user_id) REFERENCES "user" (user_id) ON DELETE CASCADE
-- );
-- CREATE INDEX idx_genc ON country (genc);
-- CREATE INDEX idx_year_of_measurement ON year_and_population (year_of_measurement);

-- STATS, USER, and SCORE tables are not dropped
-- CREATE TABLE u_name(
--                        user_id VARCHAR(255) PRIMARY KEY,
--                        name VARCHAR(255),
--                        last_update TIMESTAMP,
--                        CONSTRAINT fk_user
--                            FOREIGN KEY (user_id) REFERENCES "user" (user_id) ON DELETE CASCADE
-- );
--
-- CREATE TABLE leaderboard(
--                             id SERIAL PRIMARY KEY,
--                             user_id VARCHAR(255),
--                             mode VARCHAR(255),
--                             score INTEGER,
--                             name VARCHAR(255),
--                             CONSTRAINT fk_user
--                                 FOREIGN KEY (user_id) REFERENCES "user" (user_id) ON DELETE CASCADE
-- );
-- ALTER TABLE "user" add guest boolean;
-- UPDATE "user" set guest = true ;

CREATE TABLE song(
                     id SERIAL PRIMARY KEY,
                     song_name varchar(255),
                     artist_name varchar(255),
                     total_streams varchar(255),
                     image_url varchar(255),
                     last_update TIMESTAMP
);