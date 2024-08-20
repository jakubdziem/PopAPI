-- Drop tables if they exist
DROP TABLE IF EXISTS YEAR_AND_POPULATION;
DROP TABLE IF EXISTS ARTIST;
DROP TABLE IF EXISTS COUNTRY;

-- Create the ARTIST table
CREATE TABLE ARTIST (
                        id BIGSERIAL PRIMARY KEY,
                        artist_name VARCHAR(255),
                        lead_streams VARCHAR(255),
                        image_url VARCHAR(255),
                        last_update DATE
);

-- Create the COUNTRY table
CREATE TABLE COUNTRY (
                         country_name VARCHAR(255) PRIMARY KEY,
                         genc VARCHAR(255),
                         flag_url VARCHAR(255)
);

-- Create the YEAR_AND_POPULATION table
CREATE TABLE YEAR_AND_POPULATION (
                                     id BIGSERIAL PRIMARY KEY,
                                     year_of_measurement VARCHAR(255),
                                     population VARCHAR(255),
                                     annual_growth VARCHAR(255),
                                     country_name VARCHAR(255),
                                     FOREIGN KEY (country_name) REFERENCES COUNTRY (country_name)
);

-- STATS, USER, and SCORE tables are not dropped
