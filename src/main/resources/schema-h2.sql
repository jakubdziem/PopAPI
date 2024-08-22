-- -- Drop tables if they exist
-- DROP TABLE IF EXISTS YEAR_AND_POPULATION;
-- DROP TABLE IF EXISTS ARTIST;
-- DROP TABLE IF EXISTS COUNTRY;
--
-- -- Create the ARTIST table
-- CREATE TABLE ARTIST (
--                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
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
-- -- Create the YEAR_AND_POPULATION table
-- CREATE TABLE YEAR_AND_POPULATION (
--                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
--                                      year_of_measurement VARCHAR(255),
--                                      population VARCHAR(255),
--                                      annual_growth VARCHAR(255),
--                                      country_name VARCHAR(255),
--                                      FOREIGN KEY (country_name) REFERENCES COUNTRY (country_name)
-- );
--
-- STATS, USER, and SCORE tables are not dropped
-- DROP TABLE "user";
-- DROP TABLE stats;
-- DROP TABLE score;
CREATE TABLE "user" (
                        user_id UUID PRIMARY KEY,              -- Primary key for the user
    -- Relationships will be handled via the Stats and Score tables
    -- No need to add stats and scores directly in this table, as they're managed by foreign keys
    -- We will handle those in the corresponding Stats and Score tables
    -- Additional columns related to the user could be added here if needed
);

-- Create table for Stats that includes the foreign key to User
CREATE TABLE stats (
                       user_id UUID PRIMARY KEY,              -- Primary key and foreign key to User table
                       total_game_played BIGINT,              -- Total games played
                       avg_score DECIMAL,                     -- Average score
                       time_played BIGINT,                    -- Total time played
                       total_scored_points BIGINT,            -- Total scored points
                       number_of_won_games INTEGER,           -- Number of games won
                       CONSTRAINT fk_stats_user FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);

-- Create table for Score that references the User table
CREATE TABLE score (
                       id BIGSERIAL PRIMARY KEY,              -- Primary key for the score entry
                       best_score INTEGER,                    -- Best score value
                       mode VARCHAR(255),                     -- Game mode
                       user_id UUID,                          -- Foreign key to the user table
                       CONSTRAINT fk_score_user FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);

