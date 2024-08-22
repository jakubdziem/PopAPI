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
-- CREATE TABLE "user" (
--                         user_id UUID PRIMARY KEY,
--                         CONSTRAINT unique_user_id UNIQUE (user_id)
-- );
-- CREATE TABLE stats (
--                        user_id UUID PRIMARY KEY,
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
--                        user_id UUID,
--                        CONSTRAINT fk_user
--                            FOREIGN KEY (user_id) REFERENCES "user" (user_id) ON DELETE CASCADE
-- );
-- CREATE INDEX idx_genc ON country (genc);
-- CREATE INDEX idx_year_of_measurement ON year_and_population (year_of_measurement);

-- STATS, USER, and SCORE tables are not dropped
CREATE TABLE u_name(
                       user_id UUID PRIMARY KEY,
                       name VARCHAR(255),
                       last_update TIMESTAMP,
                       CONSTRAINT fk_user
                           FOREIGN KEY (user_id) REFERENCES "user" (user_id) ON DELETE CASCADE
);