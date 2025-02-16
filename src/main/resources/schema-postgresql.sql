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
alter table cinema add column  image_source_short varchar(50);
alter table country add column  image_source_short varchar(50);

alter table artist add column  image_source_short varchar(50);
alter table artist add column  image_source varchar(500);
alter table song add column  image_source_short varchar(50);
alter table song add column  image_source varchar(500);

CREATE TABLE weekly_stats (
                            id SERIAL PRIMARY KEY,
                            week_start_date DATE,
                            user_id VARCHAR(50),
                            total_game_played BIGINT,
                            avg_score DECIMAL(10, 2),
                            time_played VARCHAR(255),
                            total_scored_points BIGINT,
                            number_of_won_games INT,
                            name VARCHAR(255),
                            MODE varchar(255)
);
CREATE TABLE weekly_users_summed (
                            week_start_date DATE,
                            guest_users INT,
                            google_or_email_users INT,
                            PRIMARY KEY (week_start_date)
);
CREATE TABLE daily_stats_summed (
                              id SERIAL PRIMARY KEY,
                              day DATE,
                              MODE varchar(255),
                              total_game_played BIGINT,
                              avg_score DECIMAL(10, 2),
                              time_played VARCHAR(255),
                              total_scored_points BIGINT,
                              number_of_won_games INT
);
CREATE TABLE daily_users_summed(
                              day DATE,
                              guest_users INT,
                              google_or_email_users INT,
                              PRIMARY KEY (day)
);
INSERT INTO DAILY_USERS_SUMMED (day, guest_users, google_or_email_users) VALUES ('2024-11-18', 0, 0);
INSERT INTO DAILY_USERS_SUMMED (day, guest_users, google_or_email_users) VALUES ('2024-11-19', 0, 0);
INSERT INTO DAILY_USERS_SUMMED (day, guest_users, google_or_email_users) VALUES ('2024-11-20', 5, 8);
INSERT INTO DAILY_USERS_SUMMED (day, guest_users, google_or_email_users) VALUES ('2024-11-21', 5, 5);

insert into daily_users_summed (day, guest_users, google_or_email_users) values ('2024-11-22', 4, 5);
insert into daily_users_summed (day, guest_users, google_or_email_users) values ('2024-11-23', 5, 5);
insert into daily_users_summed (day, guest_users, google_or_email_users) values ('2024-11-24', 6, 2);
insert into daily_users_summed (day, guest_users, google_or_email_users) values ('2024-11-25', 6, 3);



UPDATE daily_stats_summed SET total_game_played = total_game_played + 4 where day IN ('2024-11-23', '2024-11-22')
                                                                          and mode = 'COUNTRIES_1939';
select * from daily_stats_summed where (day = '2024-11-22' or day= '2024-11-23') and mode = 'COUNTRIES_1939';

UPDATE daily_stats_summed SET total_game_played = total_game_played - 6 where day IN ('2024-11-23', '2024-11-22') and mode = 'COUNTRIES_1900';
select * from daily_stats_summed where (day = '2024-11-22' or day= '2024-11-23') and mode = 'COUNTRIES_1900';


UPDATE daily_stats_summed SET total_game_played = total_game_played - 288 where day IN ('2024-11-23', '2024-11-22') and mode = 'COMBINED_STATS';
select * from daily_stats_summed where (day = '2024-11-22' or day= '2024-11-23') and mode = 'COMBINED_STATS';


UPDATE daily_stats_summed SET total_game_played = total_game_played + 20 where day IN ('2024-11-23', '2024-11-22') and mode = 'COUNTRIES_1989';
select * from daily_stats_summed where (day = '2024-11-22' or day= '2024-11-23') and mode = 'COUNTRIES_1989';


UPDATE daily_stats_summed SET total_game_played = total_game_played - 62 where day IN ('2024-11-23', '2024-11-22') and mode = 'COUNTRIES_NORMAL';
select * from daily_stats_summed where (day = '2024-11-22' or day= '2024-11-23') and mode = 'COUNTRIES_NORMAL';

-- countries future no need


UPDATE daily_stats_summed SET total_game_played = total_game_played +3 where day IN ('2024-11-23') and mode = 'COUNTRIES_CHAOS';
select * from daily_stats_summed where (day = '2024-11-22' or day= '2024-11-23') and mode = 'COUNTRIES_CHAOS';

UPDATE daily_stats_summed SET total_game_played = total_game_played +15 where day IN ('2024-11-24') and mode = 'SPOTIFY_TOP_ARTISTS';

CREATE TABLE weekly_new_users_summed (
                                         week_start_date DATE,
                                         new_users INT,
                                         PRIMARY KEY (week_start_date)
);
CREATE TABLE weekly_active_users (
                                     week_start_date DATE,
                                     active_users INT,
                                     active_new_users INT,
                                     active_old_users INT,
                                     PRIMARY KEY (week_start_date)
);
CREATE TABLE daily_active_users (
                                     day DATE,
                                     active_users INT,
                                     PRIMARY KEY (day)
);

INSERT INTO daily_active_users(day, active_users) VALUES('2024-12-09', 0);
INSERT INTO daily_active_users(day, active_users) VALUES('2024-12-10', 0);
INSERT INTO daily_active_users(day, active_users) VALUES('2024-12-11', 37);

INSERT INTO weekly_new_users_summed (week_start_date, new_users) values ('2024-11-17', 3);
INSERT INTO weekly_new_users_summed (week_start_date, new_users) values ('2024-11-24', 47);
INSERT INTO weekly_new_users_summed (week_start_date, new_users) values ('2024-12-01', 36);
INSERT INTO weekly_new_users_summed (week_start_date, new_users) values ('2024-12-08', 17);

CREATE TABLE weekly_active_users_stats (
                                           id SERIAL PRIMARY KEY,
                                           week_start_date DATE,
                                           user_id VARCHAR(50),
                                           total_game_played BIGINT,
                                           avg_score DECIMAL(10, 2),
                                           time_played VARCHAR(255),
                                           total_scored_points BIGINT,
                                           number_of_won_games INT,
                                           name VARCHAR(255),
                                           new_user bool

);

CREATE TABLE drivers_podiums(
                     id SERIAL PRIMARY KEY,
                     name varchar(255),
                     podiums INT,
                     image_url VARCHAR(255),
                     image_source varchar(500),
                     image_source_short varchar(50),
                     tier INT
);
CREATE TABLE drivers_fastest_laps(
                                    id SERIAL PRIMARY KEY,
                                    name varchar(255),
                                    laps INT,
                                    image_url VARCHAR(255),
                                    image_source varchar(500),
                                    image_source_short varchar(50),
                                    tier INT
);

CREATE TABLE drivers_gp(
                                     id SERIAL PRIMARY KEY,
                                     name varchar(255),
                                     gp INT,
                                     image_url VARCHAR(255),
                                     image_source varchar(500),
                                     image_source_short varchar(50),
                                     tier INT
);
CREATE TABLE countries_gp(
                           id SERIAL PRIMARY KEY,
                           name varchar(255),
                           gp INT,
                           image_url VARCHAR(255),
                           image_source varchar(500),
                           image_source_short varchar(50),
                           tier INT
);