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
-- Table: public.score
CREATE TABLE IF NOT EXISTS mode_stats
(
    id SERIAL PRIMARY KEY,
    total_game_played BIGINT,
    avg_score DECIMAL,
    time_played BIGINT,
    total_scored_points BIGINT,
    number_of_won_games INTEGER,
    mode varchar(255),
    user_id VARCHAR(255),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES "user" (user_id) ON DELETE CASCADE
);
CREATE TABLE driver (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255),
        score INTEGER,
        image_url VARCHAR(255)
);
CREATE TABLE APARTMENT (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255),
        score double precision,
        category VARCHAR(255),
        image_url VARCHAR(255)
);

CREATE TABLE history(
                        id SERIAL PRIMARY KEY,
                        name varchar(255),
                        year varchar(255),
                        image_url varchar(255),
                        tier integer,
                        era varchar(2)
);

alter table song drop column last_update;
alter table song add column last_update date;
-- update song set last_update = '2024-08-16';

update driver set name = 'Fangio Juan Manuel', image_url = '/images/f1/Fangio_Juan_Manuel.png' where name = 'FANGIO Juan Manuel';
select * from driver where name = 'Fangio Juan Manuel';

alter table driver add column image_source varchar(500);
alter table apartment add column image_source varchar(700);
alter table history add column image_source varchar(700);

alter table country add column image_source varchar(700);

CREATE TABLE social_media(
                        id SERIAL PRIMARY KEY,
                        name varchar(255),
                        type varchar(30),
                        followers varchar(255),
                        image_url varchar(255),
                        image_source integer
);
alter table social_media add column tier integer;
alter table social_media drop column image_source;
alter table social_media add column image_source varchar(700);


CREATE TABLE cinema(
                       id SERIAL PRIMARY KEY,
                       name varchar(255),
                       type varchar(30),
                       ranking integer,
                       image_url varchar(255),
                       image_source varchar(700),
                       tier integer
);



alter table apartment add column  image_source_short varchar(50);
alter table driver add column  image_source_short varchar(50);
alter table history add column  image_source_short varchar(50);
alter table social_media add column  image_source_short varchar(50);