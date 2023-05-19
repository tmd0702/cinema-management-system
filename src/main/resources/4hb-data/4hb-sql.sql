DROP DATABASE MOVIE;
CREATE DATABASE MOVIE;
USE MOVIE;
-- SET FOREIGN_KEY_CHECKS=0;
-- SET SQL_SAFE_UPDATES=0;
-- SET SQL_SAFE_UPDATES=1;
INSERT INTO AUTHENTICATION VALUES('1', 'sa123456', 'USE_00001');
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
    STATUS VARCHAR(20) NOT NULL, -- SAP CHIEU HOAC DANG CHIEU
    VIEW_COUNT INT,
    POSTER_PATH VARCHAR(50),
    BACKDROP_PATH VARCHAR(50),
    REVENUE DOUBLE DEFAULT 0,
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
    REVENUE DOUBLE DEFAULT 0,
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
    CONSTRAINT FK_SCHEDULE_ST FOREIGN KEY (SHOW_TIME_ID) REFERENCES SHOW_TIMES(ID)
) ENGINE = InnoDB;
CREATE TABLE TICKETS(
	ID VARCHAR(10),
    SCHEDULE_ID VARCHAR(10),
    AMOUNT DECIMAL(20),
    CONSTRAINT PK_TICKETS PRIMARY KEY (ID),
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
    CONSTRAINT PK_PROMOTION_RELATIONSHIP PRIMARY KEY(PROMOTION_ID, USER_CATEGORY_ID, MOVIE_ID, CINEMA_ID, ITEM_ID),
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
    SCREEN_ROOM_ID VARCHAR(10),
    -- SCHEDULE_ID VARCHAR(10),
    CONSTRAINT PK_SHOW_TIME PRIMARY KEY (ID),
    -- CONSTRAINT FK_ST_SCHEDULE FOREIGN KEY (SCHEDULE_ID) REFERENCES SCHEDULES(ID),
    CONSTRAINT FK_ST_SCREEN FOREIGN KEY (SCREEN_ROOM_ID) REFERENCES SCREEN_ROOMS(ID)
) ENGINE = InnoDB;

DELIMITER //
-- DROP PROCEDURE USERS_UPDATE_SCORE;
CREATE PROCEDURE USERS_UPDATE_SCORE(user_id VARCHAR(10), total_amount DECIMAL(20))
BEGIN
	DECLARE score INT;

	SELECT SCORE INTO score
    FROM USERS
    WHERE user_id = ID;

    UPDATE USERS SET SCORE = score + total_amount / 100000 WHERE ID = user_id;
END; //
DELIMITER ;

DELIMITER //
-- DROP PROCEDURE MOVIES_UPDATE_REVENUE;
CREATE PROCEDURE MOVIES_UPDATE_REVENUE(movie_id VARCHAR(10), movie_amount DECIMAL(20))
BEGIN
	DECLARE revenue DOUBLE;

	SELECT REVENUE INTO revenue
    FROM MOVIES
    WHERE movie_id = ID;

    UPDATE MOVIES SET REVENUE = revenue + movie_amount WHERE ID = movie_id;
END; //

DELIMITER ;

DELIMITER //
CREATE PROCEDURE ITEMS_UPDATE_REVENUE(item_id VARCHAR(10), item_amount DECIMAL(20))
BEGIN
	DECLARE revenue DOUBLE;

	SELECT REVENUE INTO revenue
    FROM ITEMS
    WHERE item_id = ID;

    UPDATE ITEMS SET REVENUE = revenue + item_amount WHERE ID = item_id;
END; //
DELIMITER ;

DELIMITER //
-- DROP TRIGGER TG_PAYMENTS;
CREATE TRIGGER TG_PAYMENTS AFTER INSERT ON PAYMENTS
FOR EACH ROW
BEGIN
	DECLARE user_id VARCHAR(10);
	DECLARE movie_id VARCHAR(10);
    DECLARE item_id VARCHAR(10);
    DECLARE total_amount DECIMAL(20);
	DECLARE movie_amount DOUBLE;
    DECLARE item_amount DOUBLE;

    SELECT USER_ID INTO user_id
    FROM PAYMENTS;

	SELECT s.MOVIE_ID INTO movie_id
	FROM SCHEDULES s, TICKETS t, PAYMENTS p
    WHERE t.ID = p.TICKET_ID and s.ID = t.SCHEDULE_ID;

	SELECT BI.ITEM_ID INTO item_id
    FROM PAYMENTS P, BOOKING_ITEMS BI
    WHERE P.TICKET_ITEM_ID = BI.TICKET_ITEM_ID;

    SELECT TOTAL_AMOUNT INTO total_amount
    FROM PAYMENTS;

	SELECT AMOUNT INTO movie_amount
	FROM PAYMENTS P, TICKETS t
    WHERE P.TICKET_ID = t.ID;

	SELECT AMOUNT INTO item_amount
	FROM TICKET_ITEMS TI, PAYMENTS P
    WHERE TI.ID = P.TICKET_ITEM_ID;

    CALL USERS_UPDATE_SCORE(user_id, total_amount);
    CALL MOVIES_UPDATE_REVENUE(movie_id, movie_amount);
    CALL ITEMS_UPDATE_REVENUE(item_id, item_amount);
END; //
DELIMITER ;




DELIMITER //
CREATE PROCEDURE DELETE_SEAT(seat_id VARCHAR(10))
BEGIN
	DELETE FROM BOOKING_SEATS WHERE SEAT_ID = seat_id;
END; //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE DELETE_SCREENROOM(room_id VARCHAR(10))
BEGIN
	DELETE FROM SEATS WHERE SCREEN_ROOM_ID = room_id;
    DELETE FROM SHOW_TIMES WHERE ROOM_ID = room_id;
END; //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE DELETE_TICKET(ticket_id VARCHAR(10))
BEGIN
	DELETE FROM BOOKING_SEATS WHERE TICKET_ID = ticket_id;
    DELETE FROM PROMOTION_RELATIONSHIP WHERE TICKET_ID = ticket_id;
    DELETE FROM PAYMENTS WHERE TICKET_ID = ticket_id;
END; //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE DELETE_ITEM(item_id VARCHAR(10))
BEGIN
	DELETE FROM BOOKING_ITEMS WHERE ITEM_ID = item_id;
    DELETE FROM PROMOTION_RELATIONSHIP WHERE ITEM_ID = item_id;
END; //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE DELETE_MOVIE(movie_id VARCHAR(10))
BEGIN
	DELETE FROM MOVIE_GENRES WHERE MOVIE_ID = movie_id;
    DELETE FROM REVIEW WHERE MOVIE_ID = movie_id;
    DELETE FROM MOVIE_PRODUCTION_COMPANIES WHERE MOVIE_ID = movie_id;
    DELETE FROM SCHEDULES WHERE MOVIE_ID = movie_id;
    DELETE FROM PROMOTION_RELATIONSHIP WHERE MOVIE_ID = movie_id;
END; //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE DELETE_GENRES(genre_id VARCHAR(10))
BEGIN
	DELETE FROM MOVIE_GENRES WHERE GENRE_ID = genre_id;
END; //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE DELETE_CINEMA(cinema_id VARCHAR(10))
BEGIN
	DELETE FROM SCREEN_ROOMS WHERE CINEMA_ID = cinema_id;
    DELETE FROM PROMOTION_RELATION_SHIP WHERE CINEMA_ID = cinema_id;
END; //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE DELETE_PROMOTION(promotion_id VARCHAR(10))
BEGIN
	DELETE FROM PROMOTION_RELATIONSHIP WHERE PROMOTION_ID = promotion_id;
END; //
DELIMITER ;


DELIMITER //
CREATE TRIGGER TG_DELETE_SEATS
BEFORE DELETE ON SEATS
FOR EACH ROW
BEGIN
	DECLARE seat_id VARCHAR(10);

    SELECT ID INTO seat_id
    FROM SEATS;

    CALL DELETE_SEAT(seat_id);
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_SCREENROOMS
BEFORE DELETE ON SCREEN_ROOMS
FOR EACH ROW
BEGIN
	DECLARE room_id VARCHAR(10);

    SELECT ID INTO room_id
    FROM SCREEN_ROOMS;

    CALL DELETE_SCREENROOM(room_id);
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_GENRES
BEFORE DELETE ON GENRES
FOR EACH ROW
BEGIN
	DECLARE genre_id VARCHAR(10);

    SELECT ID INTO genre_id
    FROM GENRES;

	CALL DELETE_GENRES(genre_id);
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_TICKET
BEFORE DELETE ON TICKETS
FOR EACH ROW
BEGIN
	DECLARE ticket_id VARCHAR(10);

    SELECT ID INTO ticket_id
    FROM TICKETS;

    CALL DELETE_TICKET(ticket_id);
END; //
DELIMTER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_ITEMS
BEFORE DELETE ON ITEMS
FOR EACH ROW
BEGIN
	DECLARE item_id VARCHAR(10);

    SELECT ID INTO item_id
    FROM ITEMS;

	CALL DELETE_ITEM(item_id);
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_MOVIES
BEFORE DELETE ON MOVIES
FOR EACH ROW
BEGIN
	DECLARE movie_id VARCHAR(10);

    SELECT ID INTO movie_id
    FROM MOVIES;

	CALL DELETE_MOVIE(movie_id);
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_CINEMAS
BEFORE DELETE ON CINEMAS
FOR EACH ROW
BEGIN
	DECLARE cinema_id VARCHAR(10);

    SELECT ID INTO cinema_id
    FROM CINEMAS;

	CALL DELETE_CINEMA(cinema_id);
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_PROMOTION
BEFORE DELETE ON PROMOTIONS
FOR EACH ROW
BEGIN
	DECLARE promotion_id VARCHAR(10);

    SELECT ID INTO promotion_id
    FROM PROMOTIONS;

	CALL DELETE_PROMOTION(promotion_id);
END; //
DELIMITER ;



-- INSERT INTO USER_CATEGORY(ID, CATEGORY) VALUES('UC0001', 'ADMIN');
-- SELECT * FROM USER_CATEGORY;

-- INSERT INTO USERS(ID, USERNAME, FIRST_NAME, LAST_NAME, DOB, GENDER, ADDRESS, PHONE, EMAIL, USER_ROLE, SCORE, USER_CATEGORY_ID)
-- VALUES ('U0001', 'mduc017', 'minh', 'duc', '2003/05/17', 'm', 'MELOSO KHANG DIEN', '0123456789', 'mduc017@gmail.com', 'admin', '0', 'UC0001');

-- SELECT * FROM USERS;

-- SELECT * FROM TICKET_ITEMS;
-- INSERT INTO TICKET_ITEMS(ID, AMOUNT) VALUES ('TI0001', '100');

-- SELECT * FROM MOVIES;
-- INSERT INTO MOVIES(ID, TITLE, OVERVIEW, RELEASE_DATE, LANGUAGE, DURATION, MOVIE_STATUS, VIEW_COUNT, POSTER_PATH, BACKDROP_PATH, REVENUE, TAGLINE, VOTE_COUNT, VOTE_AVERAGE)
-- VALUES ('M0001', 'Movie 1', 'Phim 1', '2020/10/10', 'VN, ENG', '90', 'COMING SOON', '100', 'www.poster.com', 'www.backdrop.com', '0.5', 'tagline', '100', '1');

-- SELECT * FROM SCHEDULES;
-- INSERT INTO SCHEDULES(ID, SHOW_TIME_ID, MOVIE_ID)
-- VALUES ('S0001', 'ST0001', 'M0001');

-- SELECT * FROM CINEMAS;
-- INSERT INTO CINEMAS(ID, NAME, ADDRESS, CINE_AREA)
-- VALUES ('C0001', 'Rap phim 1', 'THU DUC', 'TPHCM');

-- SELECT * FROM SCREEN_ROOMS;
-- INSERT INTO SCREEN_ROOMS(ID, NAME, CAPACITY, CINEMA_ID)
-- VALUES ('SR0001', 'ROOM 1', '20', 'C0001');

-- SELECT * FROM SHOW_TIMES;
-- INSERT INTO SHOW_TIMES(ID, START_TIME, SHOW_DATE, ROOM_ID, SCHEDULE_ID)
-- VALUES ('ST0001', '10:00:00', '2020-10-10', 'SR0001', 'S0001');

-- SELECT * FROM TICKETS;
-- INSERT INTO TICKETS(ID, USER_ID, SCHEDULE_ID, AMOUNT)
-- VALUES ('T0001', 'U0001', 'S0001', '100');

-- SELECT * FROM PAYMENTS;
-- SELECT * FROM USERS;
-- INSERT INTO PAYMENTS(ID, TICKET_ID, TICKET_ITEM_ID, USER_ID, PAYMENT_DATE, PAYMENT_METHOD, TOTAL_AMOUNT)
-- VALUES ('P0001', 'T0001', 'TI0001', 'U0001', '2020/10/10', 'MOMO', '1000');

-- SELECT * FROM GENRES;
-- INSERT INTO GENRES(ID, NAME) VALUES ('G0001', 'GENRES 1');

-- SELECT * FROM MOVIES;

-- SELECT * FROM MOVIE_GENRES;
-- INSERT INTO MOVIE_GENRES(MOVIE_ID, GENRE_ID) VALUES ('M0001', 'G0001');

-- DELETE FROM GENRES WHERE ID = 'G0001';
-- DELETE FROM CINEMAS WHERE ID = 'CIN_00005';
-- select *  FROM CINEMAS WHERE ID = 'CIN_00005';

