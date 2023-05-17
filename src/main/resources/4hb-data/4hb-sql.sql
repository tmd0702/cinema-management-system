DROP DATABASE MOVIE;
CREATE DATABASE MOVIE;
USE MOVIE;
SET FOREIGN_KEY_CHECKS=0;
SET SQL_SAFE_UPDATES=1;
CREATE TABLE USERS(
	ID VARCHAR(10) NOT NULL,
    USERNAME VARCHAR(255) UNIQUE, -- TEN TAI KHOAN
    FIRST_NAME VARCHAR(255),
    LAST_NAME VARCHAR(255) NOT NULL,
    -- FULLNAME VARCHAR(255),
    DOB DATE,	-- date of birth
    GENDER VARCHAR(3),
    ADDRESS VARCHAR(255),
    PHONE VARCHAR(10) NOT NULL,
    EMAIL VARCHAR(255) NOT NULL,
    USER_ROLE VARCHAR(255) NOT NULL,
    SCORE INT,
    USER_CATEGORY_ID VARCHAR(10),
    CONSTRAINT PK_CUSTOMER PRIMARY KEY(ID),
    CONSTRAINT FK_USER_UC FOREIGN KEY (USER_CATEGORY_ID) REFERENCES USER_CATEGORY(ID)
) ENGINE = InnoDB;
CREATE TABLE MOVIES(
	ID VARCHAR(10) NOT NULL UNIQUE,
	TITLE VARCHAR(255) NOT NULL,
    OVERVIEW TEXT,
    RELEASE_DATE DATE NOT NULL,
    LANGUAGE VARCHAR(50) NOT NULL,
    DURATION INT NOT NULL, -- THOI LUONG DON VI LA PHUT
    MOVIE_STATUS VARCHAR(20) NOT NULL, -- SAP CHIEU HOAC DANG CHIEU
    VIEW_COUNT INT,
    POSTER_PATH VARCHAR(50),
    BACKDROP_PATH VARCHAR(50),
    REVENUE DOUBLE,
    TAGLINE TEXT,
    VOTE_COUNT INT NOT NULL,
    VOTE_AVERAGE FLOAT NOT NULL,
    CONSTRAINT PK_MOVIE PRIMARY KEY(ID)
) ENGINE = InnoDB;
CREATE TABLE GENRES(
	ID VARCHAR(10),
    NAME VARCHAR(50),
    CONSTRAINT PK_GENRE PRIMARY KEY(ID)
) ENGINE = InnoDB;
CREATE TABLE MOVIE_GENRES(
	MOVIE_ID VARCHAR(10),
    GENRE_ID VARCHAR(10),
    CONSTRAINT PK_MOVIE_GENRES_ID PRIMARY KEY (MOVIE_ID, GENRE_ID),
    CONSTRAINT FK_MOVIE_GENRES_MOVIE_ID FOREIGN KEY (MOVIE_ID) REFERENCES MOVIES(ID),
    CONSTRAINT FK_MOVIE_GENRES_GENRE_ID FOREIGN KEY (GENRE_ID) REFERENCES GENRES(ID)
) ENGINE = InnoDB;
CREATE TABLE CINEMAS(
	ID VARCHAR(10) NOT NULL UNIQUE,
    NAME VARCHAR(255) NOT NULL,	-- TEN RAP CHIEU PHIM
    -- TICKET_COUNT INT,		-- SỐ VÉ ĐÃ BÁN
    ADDRESS VARCHAR(255),
    CINE_AREA VARCHAR(255) NOT NULL,
    CONSTRAINT PK_CINEMA PRIMARY KEY(ID)
) ENGINE = InnoDB;
CREATE TABLE ITEMS(
	ID VARCHAR(10) NOT NULL UNIQUE,
    ITEM_NAME VARCHAR(255) NOT NULL,
    CATEGORY VARCHAR(50) NOT NULL,		-- LOAI ITEM: NUOC, BAP, ...
    PRICE DECIMAL(20),
    CONSTRAINT PK_ITEM PRIMARY KEY(ID)
) ENGINE = InnoDB;
CREATE TABLE PROMOTIONS(
	ID VARCHAR(10) NOT NULL,
    PROMOTION_NAME VARCHAR(255),
    START_DATE DATETIME NOT NULL,
    END_DATE DATETIME NOT NULL,
    PROMOTION_DESCRIPTION TEXT,
    DISCOUNT FLOAT,
    CONSTRAINT PK_PROMOTION PRIMARY KEY(ID)
) ENGINE = InnoDB;
CREATE TABLE REVIEW(
	USER_ID VARCHAR(10) NOT NULL,
    MOVIE_ID VARCHAR(10) NOT NULL,
    RATING INT,
    REVIEW_COMMENT TEXT,
    REVIEW_DATE DATETIME,
    CONSTRAINT PK_REVIEW PRIMARY KEY(USER_ID, MOVIE_ID),
    CONSTRAINT FK_REVIEW_USER FOREIGN KEY(USER_ID) REFERENCES USERS(ID),
    CONSTRAINT FK_REVIEW_MOVIE FOREIGN KEY(MOVIE_ID) REFERENCES MOVIES(ID)
) ENGINE = InnoDB;

CREATE TABLE AUTHENTICATION(
    ID VARCHAR(10) NOT NULL, -- TAI KHOAN
    PASS VARCHAR(255) NOT NULL,
    USER_ID VARCHAR(10),
    CONSTRAINT PK_ACCOUNT PRIMARY KEY(ID, PASS),
	CONSTRAINT FK_ACCOUNT_USER FOREIGN KEY(USER_ID) REFERENCES USERS(ID)
) ENGINE = InnoDB;
CREATE TABLE SCREEN_ROOMS(
	ID VARCHAR(10) NOT NULL UNIQUE,
    NAME VARCHAR(255) NOT NULL,
    CAPACITY INT,
    CINEMA_ID VARCHAR(10),
    CONSTRAINT PK_SCREEN PRIMARY KEY(ID),
    CONSTRAINT FK_SCREEN_CINEMA FOREIGN KEY (CINEMA_ID) REFERENCES CINEMAS(ID)
) ENGINE = InnoDB;
CREATE TABLE SEATS(
	ID VARCHAR(10) NOT NULL UNIQUE,
    NAME VARCHAR(10),
    SCREEN_ROOM_ID VARCHAR(10) NOT NULL,
    CATEGORY VARCHAR(50), -- VIP, NORMAL, COUPLE
    SEAT_STATUS VARCHAR(30) NOT NULL, -- ONBOOKING/ AVAILABLE
    CONSTRAINT PK_SEAT PRIMARY KEY(ID),
    CONSTRAINT FK_SEAT_SCREEN FOREIGN KEY(SCREEN_ROOM_ID) REFERENCES SCREEN_ROOMS(ID)

) ENGINE = InnoDB;

CREATE TABLE PRODUCTION_COMPANIES (
	ID VARCHAR(10),
    NAME VARCHAR(50),
    CONSTRAINT PK_PRODUCTION_COMPANIES PRIMARY KEY (ID)
) ENGINE = InnoDB;
CREATE TABLE MOVIE_PRODUCTION_COMPANIES (
	MOVIE_ID VARCHAR(10),
    PRODUCTION_COMPANY_ID VARCHAR(10),
    CONSTRAINT PK_MOVIE_PRODUCTION_COMPANIES_ID PRIMARY KEY (MOVIE_ID, PRODUCTION_COMPANY_ID),
    CONSTRAINT FK_MOVIE_PRODUCTION_COMPANIES_MOVIE_ID FOREIGN KEY (MOVIE_ID) REFERENCES MOVIES(ID),
    CONSTRAINT FK_MOVIE_PRODUCTION_COMPANIES_PRODUCTION_COMPANY_ID FOREIGN KEY (PRODUCTION_COMPANY_ID) REFERENCES PRODUCTION_COMPANIES(ID)
) ENGINE = InnoDB;
CREATE TABLE SCHEDULES (
	ID VARCHAR(10),
    SHOW_TIME_ID VARCHAR(10),
    MOVIE_ID VARCHAR(10),
    CONSTRAINT PK_SCHEDULES PRIMARY KEY (ID),
    CONSTRAINT FK_SCHEDULE_MOVIE FOREIGN KEY (MOVIE_ID) REFERENCES MOVIES(ID),
    CONSTRAINT FK_SCHEDULE_ST FOREIGN KEY (SHOW_TIME_ID) REFERENCES SHOWTIMES(ID)
) ENGINE = InnoDB;
CREATE TABLE TICKETS(
	ID VARCHAR(10),
    USER_ID VARCHAR(10),
    SCHEDULE_ID VARCHAR(10),
    PAYMENT_ID VARCHAR(10),
    AMOUNT DECIMAL(20),
    CONSTRAINT PK_TICKETS PRIMARY KEY (ID),
    CONSTRAINT FK_TICKET_USER FOREIGN KEY (USER_ID) REFERENCES USERS(ID),
    CONSTRAINT FK_TICKET_SCHEDULE FOREIGN KEY (SCHEDULE_ID) REFERENCES SCHEDULES(ID)
)ENGINE = InnoDB;

CREATE TABLE TICKET_ITEMS(
	ID VARCHAR(10),
    AMOUNT DECIMAL(10,0),
    CONSTRAINT PK_TICKET_ITEM PRIMARY KEY (ID)
) ENGINE = InnoDB;
CREATE TABLE BOOKING_ITEMS(
	ITEM_ID VARCHAR(10),
    TICKET_ITEM_ID VARCHAR(10),
    QUANTITY INT,
    CONSTRAINT PK_BOOKING_ITEM PRIMARY KEY (ITEM_ID, TICKET_ITEM_ID),
    CONSTRAINT FK_BI_ITEM FOREIGN KEY (ITEM_ID) REFERENCES ITEMS(ID),
    CONSTRAINT FK_BI_TI FOREIGN KEY (TICKET_ITEM_ID) REFERENCES TICKET_ITEMS(ID)
)ENGINE = InnoDB;
CREATE TABLE BOOKING_SEATS(
	SEAT_ID VARCHAR(10),
    TICKET_ID VARCHAR(10),
    CONSTRAINT PK_BOOKING_SEATS PRIMARY KEY (SEAT_ID, TICKET_ID),
    CONSTRAINT FK_BS_TICKET FOREIGN KEY (TICKET_ID) REFERENCES TICKETS(ID),
    CONSTRAINT FK_BS_SEAT FOREIGN KEY (SEAT_ID) REFERENCES SEATS(ID)
) ENGINE = InnoDB;
CREATE TABLE USER_CATEGORY (
	ID VARCHAR(10),
    CATEGORY VARCHAR(40),
    CONSTRAINT PK_UC PRIMARY KEY (ID)
) ENGINE = InnoDB;
CREATE TABLE PROMOTION_RELATIONSHIP(
	PROMOTION_ID VARCHAR(10) NOT NULL,
    USER_CATEGORY_ID VARCHAR(10),
    MOVIE_ID VARCHAR(10),
    CINEMA_ID VARCHAR(10) NOT NULL,
    ITEM_ID VARCHAR(10),
    TICKET_ID VARCHAR(10),
    CONSTRAINT PK_PROMOTION_RELATIONSHIP PRIMARY KEY(PROMOTION_ID, USER_ID, MOVIE_ID, CINEMA_ID, ITEM_ID),
    CONSTRAINT FK_PR_PROMOTION FOREIGN KEY(PROMOTION_ID) REFERENCES PROMOTIONS(ID),
    CONSTRAINT FK_PR_USER FOREIGN KEY(USER_CATEGORY_ID) REFERENCES USER_CATEGORY(ID),
    CONSTRAINT FK_PR_MOVIE FOREIGN KEY(MOVIE_ID) REFERENCES MOVIES(ID),
    CONSTRAINT FK_PR_CINEMA FOREIGN KEY(CINEMA_ID) REFERENCES CINEMAS(ID),
    CONSTRAINT FK_PR_ITEM FOREIGN KEY(ITEM_ID) REFERENCES ITEMS(ID),
    CONSTRAINT FK_PR_TICKET FOREIGN KEY(TICKET_ID) REFERENCES TICKETS(ID)
) ENGINE = InnoDB;

CREATE TABLE PAYMENTS(
	ID VARCHAR(10) NOT NULL UNIQUE,
    TICKET_ID VARCHAR(10) NOT NULL,
    TICKET_ITEM_ID VARCHAR(10),
    USER_ID VARCHAR(10) NOT NULL,
    PAYMENT_DATE DATETIME NOT NULL,
    PAYMENT_METHOD VARCHAR(255) NOT NULL,	-- PHUONG THUC THANH TOAN
    TOTAL_AMOUNT DECIMAL(20) NOT NULL,
    CONSTRAINT PK_PAYMENT PRIMARY KEY(ID),
    CONSTRAINT FK_PAYMENT_USER FOREIGN KEY (USER_ID) REFERENCES USERS(ID),
    CONSTRAINT FK_PAYMENT_TICKET FOREIGN KEY (TICKET_ID) REFERENCES TICKETS(ID),
    CONSTRAINT FK_PAYMENT_TI FOREIGN KEY (TICKET_ITEM_ID) REFERENCES TICKET_ITEMS(ID)
) ENGINE = InnoDB;

CREATE TABLE SHOW_TIMES (
    ID VARCHAR(10),
    START_TIME TIME,
    SHOW_DATE DATE,
    ROOM_ID VARCHAR(10),
    SCHEDULE_ID VARCHAR(10),
    CONSTRAINT PK_SHOW_TIME PRIMARY KEY (ID),
    CONSTRAINT FK_ST_SCHEDULE FOREIGN KEY (SCHEDULE_ID) REFERENCES SCHEDULES(ID),
    CONSTRAINT FK_ST_SCREEN FOREIGN KEY (ROOM_ID) REFERENCES SCREEN_ROOMS(ID)
) ENGINE = InnoDB;

