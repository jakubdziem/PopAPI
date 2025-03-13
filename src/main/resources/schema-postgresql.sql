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
CREATE TABLE f1_teams_points(
                             id SERIAL PRIMARY KEY,
                             name varchar(255),
                             points NUMERIC(10,2),
                             image_url VARCHAR(255),
                             image_source varchar(500),
                             image_source_short varchar(50),
                             tier INT
);
CREATE TABLE f1_teams_gp(
                             id SERIAL PRIMARY KEY,
                             name varchar(255),
                             gp INT,
                             image_url VARCHAR(255),
                             image_source varchar(500),
                             image_source_short varchar(50),
                             tier INT
);

-- UPDATE public.drivers_podiums dp
-- SET image_source = d.image_source,
--     image_source_short = d.image_source_short
-- FROM public.driver d
-- WHERE dp.image_url = d.image_url;
-- UPDATE public.drivers_gp dp
-- SET image_source = d.image_source,
--     image_source_short = d.image_source_short
-- FROM public.driver d
-- WHERE dp.image_url = d.image_url;
-- UPDATE public.drivers_fastest_laps dp
-- SET image_source = d.image_source,
--     image_source_short = d.image_source_short
-- FROM public.driver d
-- WHERE dp.image_url = d.image_url;
-- update drivers_gp set image_source = 'https://static.independent.co.uk/s3fs-public/thumbnails/image/2014/10/10/17/Andrea-de-Cesaris.jpg', image_source_short = 'independenct.co.uk' where image_url = '/images/f1/De_Cesaris_Andrea.png';
-- update drivers_gp set image_source = 'https://msmproduction.s3.eu-west-1.amazonaws.com/s3fs-public/uploads/2014/08/Derek-Warwick.jpg', image_source_short = 'msmproduction' where image_url = '/images/f1/Warwick_Derek.png';
-- update drivers_gp set image_source = 'https://upload.wikimedia.org/wikipedia/commons/7/7e/Jean-Pierre_Jarier_en_1976.jpg', image_source_short = 'Wikipedia' where image_url = '/images/f1/Jarier_Jean-Pierre.png';
-- update drivers_gp set image_source = 'https://upload.wikimedia.org/wikipedia/commons/8/87/Eddie_Cheever_Jr_2009_Indy_500_Second_Qual_Day.JPG', image_source_short = 'Wikipedia' where image_url = '/images/f1/Cheever_Eddie.png';
-- update drivers_gp set image_source = 'https://www.f1forgottendrivers.com/wp-content/uploads/300-port-martini.jpg', image_source_short = 'f1forgottendrivers.com' where image_url = '/images/f1/Martini_Pierluigi.png';
-- update drivers_gp set image_source = 'https://www.f1-fansite.com/wp-content/uploads/2012/11/Mika-Salo.jpg', image_source_short = 'f1-fansite.com' where image_url = '/images/f1/Salo_Mika.png';
-- update drivers_gp set image_source = 'https://www.autohebdo.fr/app/uploads/2023/07/ALLIOT-CRED-ALLIOT-copie-e1690452503460.jpg', image_source_short = 'autohebdo.fr' where image_url = '/images/f1/Alliot_Philippe.png';
-- update drivers_gp set image_source = 'https://www.f1-fansite.com/wp-content/uploads/2012/09/Jos-Verstappen-2003.jpg', image_source_short = 'f1-fansite.com' where image_url = '/images/f1/Verstappen_Jos.png';
-- update drivers_gp set image_source = 'https://formula-fan.ru/assets/images/drivers/Mass_Jochen.jpg', image_source_short = 'formula-fan.ru' where image_url = '/images/f1/Mass_Jochen.png';
-- update drivers_gp set image_source = 'https://www.racefans.net/wp-content/uploads/2021/02/racefansdotnet-20210204-144926-1.jpg', image_source_short = 'racefans.net' where image_url = '/images/f1/de_la_Rosa_Pedro.png';
-- update drivers_gp set image_source = 'https://f1i.com/wp-content/uploads/2016/09/Optimized-WRI2_00003155-001-e1473579542615.jpg', image_source_short = 'f1i.com' where image_url = '/images/f1/Bonnier_Jo.png';
-- update drivers_gp set image_source = 'https://upload.wikimedia.org/wikipedia/commons/5/51/Fazenda_da_Toca_foto_kiko_ferrite_02295_-_Pedro_Paulo_Diniz.jpg', image_source_short = 'Wikipedia' where image_url = '/images/f1/Diniz_Pedro.png';
-- update drivers_gp set image_source = 'https://a.espncdn.com/combiner/i?img=/i/headshots/rpm/players/full/4622.png', image_source_short = 'www.espn.com' where image_url = '/images/f1/Ericsson_Marcus.png';
-- update drivers_gp set image_source = 'https://formula-fan.ru/assets/images/drivers/Siffert_Jo.jpg', image_source_short = 'formula-fan.ru' where image_url = '/images/f1/Siffert_Jo.png';
-- update drivers_gp set image_source = 'https://formula-fan.ru/assets/images/drivers/Katayama_Ukyo.jpg', image_source_short = 'formula-fan.ru' where image_url = '/images/f1/Katayama_Ukyo.png';
-- update drivers_gp set image_source = 'https://motorsportmagazine.b-cdn.net/wp-content/uploads/2020/11/Ivan-Capelli-header.jpg', image_source_short = 'motorsportmagazine.com' where image_url = '/images/f1/Capelli_Ivan.png';
-- update drivers_gp set image_source = 'https://cdn-4.motorsport.com/images/mgl/2erVbR9Y/s8/dtm-start-field-presentation-2016-timo-glock-bmw-team-rmg-bmw-m4-dtm.jpg', image_source_short = 'motorsport.com' where image_url = '/images/f1/Glock_Timo.png';
-- update drivers_gp set image_source = 'https://www.reddit.com/media?url=https%3A%2F%2Fpreview.redd.it%2Fwhen-is-takuma-sato-coming-back-v0-q27mfinc3hec1.jpeg%3Fauto%3Dwebp%26s%3D20bf1849c4501d79f52e3e45769e98e963b47322', image_source_short = 'Reddit' where image_url = '/images/f1/Sato_Takuma.png';
-- update drivers_gp set image_source = 'https://www.f1-fansite.com/wp-content/uploads/2018/12/1985-Portugal-Palmer-01.jpg', image_source_short = 'f1-fansite.com' where image_url = '/images/f1/Palmer_Jonathan.png';
-- UPDATE public.countries_gp dp
-- SET image_source = d.image_source,
--     image_source_short = d.image_source_short
-- FROM public.country d
-- WHERE dp.image_url = d.flag_url;
-- UPDATE public.drivers_podiums dp
-- SET image_source = d.image_source,
--     image_source_short = d.image_source_short
-- FROM public.drivers_gp d
-- WHERE dp.image_url = d.image_url;
-- update drivers_podiums set image_source = 'https://i.ebayimg.com/images/g/K3kAAOSwr9RgRjIR/s-l1200.jpg', image_source_short = 'eBay' where image_url = '/images/f1/Behra_Jean.png';
-- update drivers_podiums set image_source = 'https://cdn.ferrari.com/cms/network/media/img/resize/6142200cc8d35f6304c6776c-petercollions1250x700?', image_source_short = 'ferrari.com' where image_url = '/images/f1/Collins_Peter.png';
-- update drivers_podiums set image_source = 'https://www.museonicolis.com/html/uploads/2018/03/NANNINI-MINARDI-1986-e1520932762985.jpg', image_source_short = 'museonicolis.com' where image_url = '/images/f1/Nannini_Alessandro.png';
-- update drivers_podiums set image_source = 'https://static.wikia.nocookie.net/f1wikia/images/c/c2/Villoresi_luigi.jpg/revision/latest?cb=20130402193600', image_source_short = '' where image_url = '/images/f1/Villoresi_Luigi.png';
-- update drivers_podiums set image_source = 'https://static.wikia.nocookie.net/f1wikia/images/c/c2/Villoresi_luigi.jpg/revision/latest?cb=20130402193600', image_source_short = 'ajayproductions.fandom.com' where image_url = '/images/f1/Bandini_Lorenzo.png';
-- update drivers_podiums set image_source = 'https://www.snaplap.net/wp-content/uploads/2016/02/revson-peter.jpg', image_source_short = 'snaplap.net' where image_url = '/images/f1/Revson_Peter.png';
-- update drivers_podiums set image_source = 'https://cdn-1.latimages.com/images/mgl/Ooyv8o/s4_1/1017471471-LAT-19550612-302_29A.jpg', image_source_short = 'latimages.com' where image_url = '/images/f1/Musso_Luigi.png';
-- update drivers_podiums set image_source = 'https://upload.wikimedia.org/wikipedia/commons/b/b3/Luigi_Fagioli_in_his_Maserati_at_the_1932_Targa_Florio_%28cropped%29.jpg', image_source_short = 'Wikipedia' where image_url = '/images/f1/Fagioli_Luigi.png';
-- update drivers_podiums set image_source = 'https://i.ebayimg.com/images/g/QQIAAOSwgvpmGrD~/s-l1200.jpg', image_source_short = 'eBay' where image_url = '/images/f1/von_Trips_Wolfgang.png';
-- update drivers_podiums set image_source = 'https://i.ebayimg.com/images/g/wWIAAOSw75lmGuXs/s-l1200.jpg', image_source_short = 'eBay' where image_url = '/images/f1/Pace_Carlos.png';
-- update drivers_podiums set image_source = 'https://upload.wikimedia.org/wikipedia/commons/8/8e/Piero_Taruffi.jpg', image_source_short = 'Wikipedia' where image_url = '/images/f1/Taruffi_Piero.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'logos-world.net', IMAGE_SOURCE = 'https://logos-world.net/wp-content/uploads/2020/07/Ferrari-Scuderia-Logo.png' where image_url = '/images/f1_teams/Ferrari.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'formula1.com', IMAGE_SOURCE = 'https://media.formula1.com/image/upload/f_auto,c_limit,q_75,w_1320/content/dam/fom-website/2018-redesign-assets/team%20logos/red%20bull' where image_url = '/images/f1_teams/Red_Bull.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'formula1.com', IMAGE_SOURCE = 'https://media.formula1.com/image/upload/f_auto,c_limit,q_75,w_1320/content/dam/fom-website/2018-redesign-assets/team%20logos/mercedes' where image_url = '/images/f1_teams/Mercedes.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'brandlogos.net', IMAGE_SOURCE = 'https://brandlogos.net/wp-content/uploads/2022/04/mclaren_formula_1_team-logo-brandlogos.net_.png' where image_url = '/images/f1_teams/McLaren.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/commons/f/f9/Logo_Williams_F1.png' where image_url = '/images/f1_teams/Williams.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'f1.fandom.com', IMAGE_SOURCE = 'https://f1.fandom.com/wiki/Lotus_F1' where image_url = '/images/f1_teams/Lotus.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/commons/4/49/Renault_F1_Team_logo_2019.png' where image_url = '/images/f1_teams/Renault.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'lezebre.lu', IMAGE_SOURCE = 'https://lezebre.lu/images/detailed/17/30580-Force-India-F1.png' where image_url = '/images/f1_teams/Force_India.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'worldvectorlogo.com', IMAGE_SOURCE = 'https://cdn.worldvectorlogo.com/logos/brabham-racing-organisation-logo.svg' where image_url = '/images/f1_teams/Brabham.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'freebiesupply.com', IMAGE_SOURCE = 'https://cdn.freebiesupply.com/logos/large/2x/benetton-f1-logo-png-transparent.png' where image_url = '/images/f1_teams/Benetton.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'pngwing.com', IMAGE_SOURCE = 'https://w7.pngwing.com/pngs/486/628/png-transparent-tyrrell-f1-hd-logo.png' where image_url = '/images/f1_teams/Tyrrell.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/commons/thumb/9/94/Sauber_F1_Team_logo.svg/1280px-Sauber_F1_Team_logo.svg.png' where image_url = '/images/f1_teams/Sauber.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Alpine_F1_Team_Logo.svg/523px-Alpine_F1_Team_Logo.svg.png' where image_url = '/images/f1_teams/Alpine.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'astonmartin.com', IMAGE_SOURCE = 'https://media.astonmartin.com/wp-content/uploads/2021/01/588489-scaled-1500x1370.jpg' where image_url = '/images/f1_teams/Aston_Martin.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'logos.fandom.com', IMAGE_SOURCE = 'https://logos.fandom.com/wiki/Toro_Rosso' where image_url = '/images/f1_teams/Toro_Rosso.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'british-racing-motors.myshopify.com', IMAGE_SOURCE = 'https://british-racing-motors.myshopify.com/cdn/shop/files/BRM_logo.png?height=628&pad_color=ffffff&v=1707912723&width=1200' where image_url = '/images/f1_teams/BRM.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'f1-fansite.com', IMAGE_SOURCE = 'https://www.f1-fansite.com/wp-content/uploads/2018/12/Ligier-F1-team-info-stats.jpg' where image_url = '/images/f1_teams/Ligier.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/BMW.svg/640px-BMW.svg.png' where image_url = '/images/f1_teams/BMW_Sauber.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'f1.fandom.com', IMAGE_SOURCE = 'https://f1.fandom.com/wiki/Cooper_Car_Company' where image_url = '/images/f1_teams/Cooper.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/en/thumb/0/09/Scuderia_Alpha-Tauri.svg/1200px-Scuderia_Alpha-Tauri.svg.png' where image_url = '/images/f1_teams/AlphaTauri.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Logo_Haas_F1.png/800px-Logo_Haas_F1.png' where image_url = '/images/f1_teams/Haas.png';;
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'redbubble.net', IMAGE_SOURCE = 'https://ih1.redbubble.net/image.2067866172.0351/st,small,507x507-pad,600x600,f8f8f8.jpg' where image_url = '/images/f1_teams/Jordan.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'cdnlogo.com', IMAGE_SOURCE = 'https://cdnlogo.com/logos/t/53/toyota-f1.svg' where image_url = '/images/f1_teams/Toyota.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'pitwall.app', IMAGE_SOURCE = 'https://pitwall.app/rails/active_storage/disk/eyJfcmFpbHMiOnsiZGF0YSI6eyJrZXkiOiIwNWJ3cGRxMGR1aXY1dXZ2ZmMzcWxqNTA2NHFwIiwiZGlzcG9zaXRpb24iOiJpbmxpbmU7IGZpbGVuYW1lPVwicmFjaW5nX3BvaW50LnBuZ1wiOyBmaWxlbmFtZSo9VVRGLTgnJ3JhY2luZ19wb2ludC5wbmciLCJjb250ZW50X3R5cGUiOiJpbWFnZS9wbmciLCJzZXJ2aWNlX25hbWUiOiJsb2NhbCJ9LCJleHAiOiIyMDI1LTAyLTE3VDEwOjQ0OjAzLjQzNloiLCJwdXIiOiJibG9iX2tleSJ9fQ==--30e6e979a2decd21f39cdbbee8d86452f87f7d42/racing_point.png' where image_url = '/images/f1_teams/Racing_Point.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/en/2/25/British_american_racing_logo.png' where image_url = '/images/f1_teams/BAR.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'formula1.com', IMAGE_SOURCE = 'https://www.formula1.com/content/dam/fom-website/manual/teams/Sauber/Alfa_Romeo_Racing_logo.jpg' where image_url = '/images/f1_teams/Alfa_Romeo.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'redbubble.net', IMAGE_SOURCE = 'https://ih1.redbubble.net/image.1272940646.8172/st,small,845x845-pad,1000x1000,f8f8f8.u9.jpg' where image_url = '/images/f1_teams/March.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://en.m.wikipedia.org/wiki/File:Brawn_GP_logo.svg' where image_url = '/images/f1_teams/Brawn_GP.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = '1000logos.net', IMAGE_SOURCE = 'https://1000logos.net/wp-content/uploads/2023/08/Matra-Sports-Logo.png' where image_url = '/images/f1_teams/Matra.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'cdnlogo.com', IMAGE_SOURCE = 'https://cdnlogo.com/logos/h/40/honda-f1-racing.svg' where image_url = '/images/f1_teams/Honda.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'cdnlogo.com', IMAGE_SOURCE = 'https://cdnlogo.com/logos/a/1/arrows-f1.svg' where image_url = '/images/f1_teams/Arrows.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'fragrantica.pl', IMAGE_SOURCE = 'https://www.fragrantica.pl/marki/Walter-Wolf.html' where image_url = '/images/f1_teams/Wolf.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'liquipedia.net', IMAGE_SOURCE = 'https://liquipedia.net/commons/images/a/a2/Shadow_lightmode.png' where image_url = '/images/f1_teams/Shadow.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/en/5/58/Vanwall_logo.jpg' where image_url = '/images/f1_teams/Vanwall.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'formula1points.com', IMAGE_SOURCE = 'https://www.formula1points.com/images/constructors/surtees.webp' where image_url = '/images/f1_teams/Surtees.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'logos.fandom.com', IMAGE_SOURCE = 'https://logos.fandom.com/wiki/Jaguar_Racing_(F1)' where image_url = '/images/f1_teams/Jaguar.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'f1.fandom.com', IMAGE_SOURCE = 'https://f1.fandom.com/wiki/Porsche' where image_url = '/images/f1_teams/Porsche.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'heskethracing.co.uk', IMAGE_SOURCE = 'https://www.heskethracing.co.uk/wp-content/uploads/2020/07/Logo_HeskethRacing_Landscape.jpg' where image_url = '/images/f1_teams/Hesketh.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'logos.fandom.com', IMAGE_SOURCE = 'https://logos.fandom.com/wiki/Stewart_Gran_Prix' where image_url = '/images/f1_teams/Stewart.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'formula1.com', IMAGE_SOURCE = 'https://media.formula1.com/image/upload/f_auto,c_limit,q_75,w_1320/content/dam/fom-website/2018-redesign-assets/team%20logos/rb' where image_url = '/images/f1_teams/RB.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'formula1points.com', IMAGE_SOURCE = 'https://www.formula1points.com/images/constructors/lola.webp' where image_url = '/images/f1_teams/Lola.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'logos.fandom.com', IMAGE_SOURCE = 'https://logos.fandom.com/wiki/Minardi' where image_url = '/images/f1_teams/Minardi.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Prost_Grand_Prix_Formula_One_Logo.png/800px-Prost_Grand_Prix_Formula_One_Logo.png' where image_url = '/images/f1_teams/Prost.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'logos.fandom.com', IMAGE_SOURCE = 'https://logos.fandom.com/wiki/Fittipaldi' where image_url = '/images/f1_teams/Copersucar.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/en/archive/3/37/20230824002954%21Toleman_logo.png' where image_url = '/images/f1_teams/Toleman.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'logos.fandom.com', IMAGE_SOURCE = 'https://logos.fandom.com/wiki/Footwork' where image_url = '/images/f1_teams/Footwork.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/20/Team_Penske_logo.svg/2560px-Team_Penske_logo.svg.png' where image_url = '/images/f1_teams/Penske.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'formula1points.com', IMAGE_SOURCE = 'https://www.formula1points.com/images/constructors/ensign.webp' where image_url = '/images/f1_teams/Ensign.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'statsf1.com', IMAGE_SOURCE = 'https://www.statsf1.com/constructeurs/logo/eagle.png' where image_url = '/images/f1_teams/Eagle.png';
-- UPDATE F1_TEAMS_POINTS SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Dallara_logo.svg/2560px-Dallara_logo.svg.png' where image_url = '/images/f1_teams/Dallara.png';
-- UPDATE public.f1_teams_gp dp
-- SET image_source = d.image_source,
--     image_source_short = d.image_source_short
-- FROM public.f1_teams_points d
-- WHERE dp.image_url = d.image_url;
-- UPDATE F1_TEAMS_GP SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/commons/9/9a/Osella.svg' where image_url = '/images/f1_teams/Osella.png';
-- UPDATE F1_TEAMS_GP SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/ATS.svg/2560px-ATS.svg.png' where image_url = '/images/f1_teams/ATS.png';
-- UPDATE F1_TEAMS_GP SET IMAGE_SOURCE_SHORT = '1000logos.net', IMAGE_SOURCE = 'https://1000logos.net/wp-content/uploads/2023/08/Marussia-Motors-Logo.png' where image_url = '/images/f1_teams/Marussia.png';
-- UPDATE F1_TEAMS_GP SET IMAGE_SOURCE_SHORT = 'pinterest.com', IMAGE_SOURCE = 'https://pl.pinterest.com/pin/maserati-f1-logo-and-emblem--521010250624101099/' where image_url = '/images/f1_teams/Maserati.png';
-- UPDATE F1_TEAMS_GP SET IMAGE_SOURCE_SHORT = 'Wikipedia', IMAGE_SOURCE = 'https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/HRT_F1_TEAM_2012.svg/1200px-HRT_F1_TEAM_2012.svg.png' where image_url = '/images/f1_teams/HRT.png';
-- UPDATE F1_TEAMS_GP SET IMAGE_SOURCE_SHORT = 'am-online.com', IMAGE_SOURCE = 'https://www.am-online.com/news/2014/1/21/caterham-reveals-new-branding/34256/' where image_url = '/images/f1_teams/Caterham.png';
-- UPDATE F1_TEAMS_GP SET IMAGE_SOURCE_SHORT = 'f1.fandom.com', IMAGE_SOURCE = 'https://f1.fandom.com/wiki/Zakspeed' where image_url = '/images/f1_teams/Zakspeed.png';


-- fixes the incrementation guy problem
-- UPDATE public.daily_stats_summed AS target
-- SET
--     total_game_played = source.total_game_played,
--     avg_score = source.avg_score,
--     time_played = source.time_played,
--     total_scored_points = source.total_scored_points,
--     number_of_won_games = source.number_of_won_games
-- FROM public.daily_stats_summed AS source
-- WHERE source.day = '2025-02-08'
--   AND target.day = '2025-02-16'
--   AND source.mode = target.mode;

-- alter table weekly_stats  alter column user_id type varchar(51);

update daily_active_users set active_users = 18 where day = '2025-02-23';
update daily_active_users set active_users = 24 where day = '2025-03-02';

update daily_users_summed set guest_users = 4, google_or_email_users = 4 where day = '2025-02-23';
update daily_users_summed set guest_users = 13, google_or_email_users = 10 where day = '2025-03-02';


-- update daily_stats_summed set total_game_played = total_game_played - 5200,
-- time_played = '04:00:42', total_scored_points = total_scored_points - 8886
-- where day = '2025-03-07' and mode = 'COMBINED_STATS';;
-- SELECT *
-- FROM public.daily_stats_summed where day = '2025-03-07' and mode = 'COMBINED_STATS';

-- update weekly_stats set total_game_played = 32, time_played = '00:48:24',
-- total_scored_points = 0
-- where user_id = 'GTK1BYggHwZvRVGB1o67svLIcjG3' and mode = 'COMBINED_STATS';
-- select * from weekly_stats where user_id = 'GTK1BYggHwZvRVGB1o67svLIcjG3' and mode = 'COMBINED_STATS'

-- update daily_stats_summed set total_game_played = 428,
-- time_played = '04:04:39', total_scored_points = 1915
-- where day = '2025-02-23' and mode = 'COMBINED_STATS';
-- SELECT *
-- FROM public.daily_stats_summed where day = '2025-02-23' and mode = 'COMBINED_STATS';

-- update daily_stats_summed set total_game_played = 549,
-- time_played = '03:20:45' 12045, total_scored_points = 1787
-- where day = '2025-03-02' and mode = 'COMBINED_STATS';
-- SELECT *
-- FROM public.daily_stats_summed where day = '2025-03-02' and mode = 'COMBINED_STATS';

-- update daily_stats_summed set total_game_played = 549,
--                               time_played = '03:20:45', total_scored_points = 1787
-- where day = '2025-03-02' and mode = 'COMBINED_STATS';
-- SELECT *
-- FROM public.daily_stats_summed where day = '2025-03-02' and mode = 'COMBINED_STATS';

-- update weekly_new_users_summed set new_users = 75 where week_start_date = '2025-03-09'
-- update weekly_active_users set active_users = 97, active_new_users = 75, active_old_users = 22 where week_start_date = '2025-03-09';
-- update daily_active_users set active_users = 11 where day = '2025-03-08';
-- update daily_users_summed set guest_users = 5, google_or_email_users = 6 where day = '2025-03-08';


-- update stats set total_game_played = 32, time_played = 2904, total_scored_points = 0 where user_id = 'GTK1BYggHwZvRVGB1o67svLIcjG3';
-- SELECT * from stats where user_id = 'GTK1BYggHwZvRVGB1o67svLIcjG3';

-- update weekly_stats set total_game_played = total_game_played - 5200, time_played = '189:59:37'
-- , total_scored_points = total_scored_points - 8886
-- where user_id = 'STATS_OFF_ALL_USERS'
-- and mode = 'COMBINED_STATS' and week_start_date = '2025-03-09';
--
-- select * from weekly_stats where user_id = 'STATS_OFF_ALL_USERS'
-- and mode = 'COMBINED_STATS' and week_start_date = '2025-03-09';