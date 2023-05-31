DROP DATABASE MOVIE;
CREATE DATABASE MOVIE;
USE MOVIE;

CREATE TABLE USER_CATEGORY (
	ID VARCHAR(10),
    CATEGORY VARCHAR(40),
    POINT_LOWERBOUND INT,
    CONSTRAINT PK_UC PRIMARY KEY (ID)
) ENGINE = InnoDB;
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

CREATE TABLE PROMOTIONS(
	ID VARCHAR(10) NOT NULL,
    PROMOTION_NAME VARCHAR(255),
    START_DATE DATETIME NOT NULL,
    END_DATE DATETIME NOT NULL,
    PROMOTION_DESCRIPTION TEXT,
    DISCOUNT FLOAT,
    USER_CATEGORY_ID VARCHAR(10),
    CONSTRAINT PK_PROMOTION PRIMARY KEY(ID),
    CONSTRAINT FK_PROMOTION_UC FOREIGN KEY (USER_CATEGORY_ID) REFERENCES USER_CATEGORY(ID)
) ENGINE = InnoDB;
CREATE TABLE REVIEW(
	USER_ID VARCHAR(10) NOT NULL,
    MOVIE_ID VARCHAR(10) NOT NULL,
    RATING FLOAT,
    COMMENT VARCHAR(200),
    DATE DATETIME,
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
CREATE TABLE SHOW_TIMES (
    ID VARCHAR(10),
    START_TIME TIME,
    SHOW_DATE DATE,
    -- SCREEN_ROOM_ID VARCHAR(10),
    -- SCHEDULE_ID VARCHAR(10),
    CONSTRAINT PK_SHOW_TIME PRIMARY KEY (ID)
    -- CONSTRAINT FK_ST_SCHEDULE FOREIGN KEY (SCHEDULE_ID) REFERENCES SCHEDULES(ID),
    -- CONSTRAINT FK_ST_SCREEN FOREIGN KEY (SCREEN_ROOM_ID) REFERENCES SCREEN_ROOMS(ID)
) ENGINE = InnoDB;
CREATE TABLE SCHEDULES (
	ID VARCHAR(10),
    SHOW_TIME_ID VARCHAR(10),
    SCREEN_ROOM_ID VARCHAR(10),
    SHOW_DATE DATE,
    MOVIE_ID VARCHAR(10),
    CONSTRAINT PK_SCHEDULES PRIMARY KEY (ID),
    CONSTRAINT FK_SCHEDULE_ROOM FOREIGN KEY (SCREEN_ROOM_ID) REFERENCES SCREEN_ROOMS(ID),
    CONSTRAINT FK_SCHEDULE_MOVIE FOREIGN KEY (MOVIE_ID) REFERENCES MOVIES(ID),
    CONSTRAINT FK_SCHEDULE_ST FOREIGN KEY (SHOW_TIME_ID) REFERENCES SHOW_TIMES(ID)
) ENGINE = InnoDB;
CREATE TABLE TICKETS(
	ID VARCHAR(10),
    SEAT_ID VARCHAR(10),
    SCHEDULE_ID VARCHAR(10),
    AMOUNT DECIMAL(20),
    CONSTRAINT PK_TICKETS PRIMARY KEY (ID),
    CONSTRAINT FK_TICKET_SEAT FOREIGN KEY (SEAT_ID) REFERENCES SEATS(ID),
    CONSTRAINT FK_TICKET_SCHEDULE FOREIGN KEY (SCHEDULE_ID) REFERENCES SCHEDULES(ID)
)ENGINE = InnoDB;
CREATE TABLE BOOKING_TICKETS(
	PAYMENT_ID VARCHAR(10),
    TICKET_ID VARCHAR(10),
    CONSTRAINT PK_BOOKING_SEATS PRIMARY KEY (PAYMENT_ID, TICKET_ID),
    CONSTRAINT FK_BS_TICKET FOREIGN KEY (TICKET_ID) REFERENCES TICKETS(ID),
    CONSTRAINT FK_BS_PAYMENT FOREIGN KEY (PAYMENT_ID) REFERENCES PAYMENTS(ID)
) ENGINE = InnoDB;
CREATE TABLE ITEMS(
	ID VARCHAR(10) NOT NULL UNIQUE,
    NAME VARCHAR(255) NOT NULL,
    CATEGORY VARCHAR(50) NOT NULL,		-- LOAI ITEM: NUOC, BAP, ...
    PRICE_ID VARCHAR(10),
    REVENUE DOUBLE DEFAULT 0,
    CONSTRAINT PK_ITEM PRIMARY KEY(ID)
) ENGINE = InnoDB;
CREATE TABLE BOOKING_ITEMS(
	PAYMENT_ID VARCHAR(10),
	ITEM_ID VARCHAR(10),
    QUANTITY INT,
    CONSTRAINT PK_BOOKING_ITEM PRIMARY KEY (ITEM_ID, PAYMENT_ID),
    CONSTRAINT FK_BI_ITEM FOREIGN KEY (ITEM_ID) REFERENCES ITEMS(ID),
    CONSTRAINT FK_BI_TI FOREIGN KEY (PAYMENT_ID) REFERENCES PAYMENTS(ID)
)ENGINE = InnoDB;

-- CREATE TABLE PROMOTION_RELATIONSHIP(
-- 	PROMOTION_ID VARCHAR(10) NOT NULL,
--     USER_CATEGORY_ID VARCHAR(10),
--     MOVIE_ID VARCHAR(10),
--     CINEMA_ID VARCHAR(10) NOT NULL,
--     ITEM_ID VARCHAR(10),
--     TICKET_ID VARCHAR(10),
--     CONSTRAINT PK_PROMOTION_RELATIONSHIP PRIMARY KEY(PROMOTION_ID, USER_CATEGORY_ID, MOVIE_ID, CINEMA_ID, ITEM_ID),
--     CONSTRAINT FK_PR_PROMOTION FOREIGN KEY(PROMOTION_ID) REFERENCES PROMOTIONS(ID),
--     CONSTRAINT FK_PR_USER FOREIGN KEY(USER_CATEGORY_ID) REFERENCES USER_CATEGORY(ID),
--     CONSTRAINT FK_PR_MOVIE FOREIGN KEY(MOVIE_ID) REFERENCES MOVIES(ID),
--     CONSTRAINT FK_PR_CINEMA FOREIGN KEY(CINEMA_ID) REFERENCES CINEMAS(ID),
--     CONSTRAINT FK_PR_ITEM FOREIGN KEY(ITEM_ID) REFERENCES ITEMS(ID),
--     CONSTRAINT FK_PR_TICKET FOREIGN KEY(TICKET_ID) REFERENCES TICKETS(ID)
-- ) ENGINE = InnoDB;

CREATE TABLE PAYMENT_METHODS(
	ID VARCHAR(10),
    NAME VARCHAR(45),
    CONSTRAINT PK_PAYMENT_METHODS PRIMARY KEY (ID)
) ENGINE = InnoDB;
CREATE TABLE PAYMENTS(
	ID VARCHAR(10) NOT NULL UNIQUE,
    USER_ID VARCHAR(10) NOT NULL,
    PAYMENT_DATE DATETIME NOT NULL,
    PAYMENT_METHOD_ID VARCHAR(10) NOT NULL,	-- PHUONG THUC THANH TOAN
    TOTAL_AMOUNT DECIMAL(20) NOT NULL,
    SCHEDULE_ID VARCHAR(10) NOT NULL,
    PROMOTION_ID VARCHAR(10),
    CONSTRAINT PK_PAYMENT PRIMARY KEY(ID),
    CONSTRAINT FK_PAYMENT_SCHEDULE FOREIGN KEY (SCHEDULE_ID) REFERENCES SCHEDULES(ID),
    CONSTRAINT FK_PAYMENT_USER FOREIGN KEY (USER_ID) REFERENCES USERS(ID),
    CONSTRAINT FK_PAYMENT_PM FOREIGN KEY (PAYMENT_METHOD_ID) REFERENCES PAYMENT_METHODS(ID)
) ENGINE = InnoDB;

INSERT INTO AUTHENTICATION VALUES('1', 'sa123456', 'USE_00001');

-- PROCEDURE:

DELIMITER //
CREATE PROCEDURE increaseViewCount()
BEGIN
	UPDATE MOVIES SET VIEW_COUNT = VIEW_COUNT + 1;
END; //
DELIMITER ;


-- TRIGGER INSERT:

DELIMITER //
CREATE TRIGGER TG_INSERT_PAYMENTS
AFTER INSERT ON PAYMENTS
FOR EACH ROW
BEGIN
	UPDATE PAYMENTS SET TOTAL_AMOUNT = (SELECT AMOUNT FROM PAYMENT_TICKETS WHERE NEW.PAYMENT_TICKET_ID = ID) +
									  (SELECT AMOUNT FROM PAYMENT_ITEMS WHERE NEW.PAYMENT_ITEM_ID = ID);

	UPDATE USERS SET SCORE = SCORE + NEW.TOTAL_AMOUNT / 1000 WHERE ID = NEW.USER_ID;

	UPDATE MOVIES SET REVENUE = REVENUE + (SELECT AMOUNT
										   FROM PAYMENT_TICKETS
                                           WHERE NEW.PAYMENT_TICKET_ID = ID)
    WHERE ID = (SELECT S.MOVIE_ID
				FROM SCHEDULES S, TICKET T, PAYMENT_TICKETS PT, BOOKING_TICKETS BT
				WHERE S.ID = T.SCHEDULE_ID AND 
					  PT.ID = NEW.PAYMENT_TICKET_ID AND
                      BT.PAYMENT_TICKET_ID = PT.ID AND 
                      BT.TICKET_ID = T.ID
				LIMIT 1);

	UPDATE ITEMS SET REVENUE = REVENUE + (SELECT AMOUNT
										  FROM PAYMENT_ITEMS PT
                                          WHERE NEW.PAYMENT_ITEM_ID = PT.ID)
    WHERE ID = (SELECT ITEM_ID
				FROM BOOKING_ITEMS BI
				WHERE BI.PAYMENT_ITEM_ID = NEW.PAYMENT_ITEM_ID);

END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_INSERT_REVIEW
AFTER INSERT ON REVIEW
FOR EACH ROW
BEGIN
    UPDATE MOVIES SET VOTE_AVERAGE = ((VOTE_AVERAGE * VOTE_COUNT) + NEW.RATING) / (VOTE_COUNT + 1) WHERE ID = NEW.MOVIE_ID;
	UPDATE MOVIES SET VOTE_COUNT = VOTE_COUNT + 1 WHERE ID = NEW.MOVIE_ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_INSERT_BOOKING_TICKETS
AFTER INSERT ON BOOKING_TICKETS
FOR EACH ROW
BEGIN
	UPDATE PAYMENT_TICKETS
    SET AMOUNT = AMOUNT + (SELECT AMOUNT FROM TICKETS WHERE ID = NEW.TICKET_ID)
    WHERE ID = NEW.PAYMENT_TICKET_ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_INSERT_BOOKING_ITEMS
AFTER INSERT ON BOOKING_ITEMS
FOR EACH ROW
BEGIN
	UPDATE PAYMENT_ITEMS
    SET AMOUNT = AMOUNT + (SELECT PRICE FROM ITEMS WHERE ID = NEW.ITEM_ID) * NEW.QUANTITY
    WHERE ID = NEW.PAYMENT_ITEM_ID;
END; //
DELIMITER ;


-- TRIGGER DELETE:

DELIMITER //
CREATE TRIGGER TG_DELETE_USERS
BEFORE DELETE ON USERS
FOR EACH ROW
BEGIN
	DELETE FROM AUTHENTICATION WHERE OLD.ID = USER_ID;
    DELETE FROM REVIEW WHERE OLD.ID = USER_ID;
    DELETE FROM PAYMENTS WHERE OLD.ID = USER_ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_SEATS
BEFORE DELETE ON SEATS
FOR EACH ROW
BEGIN
	DELETE FROM TICKETS WHERE SEAT_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_SCREENROOMS
BEFORE DELETE ON SCREEN_ROOMS
FOR EACH ROW
BEGIN
	DELETE FROM SEATS WHERE SCREEN_ROOM_ID = OLD.ID;
    DELETE FROM SHOW_TIMES WHERE ROOM_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_TICKETS
BEFORE DELETE ON TICKETS
FOR EACH ROW
BEGIN
	DELETE FROM BOOKING_TICKETS WHERE TICKET_ID = OLD.ID;
    DELETE FROM PROMOTION_RELATIONSHIP WHERE TICKET_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_ITEMS
BEFORE DELETE ON ITEMS
FOR EACH ROW
BEGIN
	DELETE FROM BOOKING_ITEMS WHERE ITEM_ID = OLD.ID;
    DELETE FROM PROMOTION_RELATIONSHIP WHERE ITEM_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_PAYMENT_TICKETS
BEFORE DELETE ON PAYMENT_TICKETS
FOR EACH ROW
BEGIN
	DELETE FROM BOOKING_TICKETS WHERE PAYMENT_TICKET_ID = OLD.ID;
	DELETE FROM PAYMENTS WHERE PAYMENT_TICKET_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_PAYMENT_ITEMS
BEFORE DELETE ON PAYMENT_ITEMS
FOR EACH ROW
BEGIN
	DELETE FROM BOOKING_ITEMS WHERE PAYMENT_ITEM_ID = OLD.ID;
    DELETE FROM PAYMENTS WHERE PAYMENT_ITEM_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_MOVIES
BEFORE DELETE ON MOVIES
FOR EACH ROW
BEGIN
	DELETE FROM MOVIE_GENRES WHERE MOVIE_ID = OLD.ID;
    DELETE FROM REVIEW WHERE MOVIE_ID = OLD.ID;
    DELETE FROM MOVIE_PRODUCTION_COMPANIES WHERE MOVIE_ID = OLD.ID;
    DELETE FROM SCHEDULES WHERE MOVIE_ID = OLD.ID;
    DELETE FROM PROMOTION_RELATIONSHIP WHERE MOVIE_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_GENRES
BEFORE DELETE ON GENRES
FOR EACH ROW
BEGIN
	DELETE FROM MOVIE_GENRES WHERE GENRE_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_CINEMAS
BEFORE DELETE ON CINEMAS
FOR EACH ROW
BEGIN
	DELETE FROM SCREEN_ROOMS WHERE CINEMA_ID = OLD.ID;
    DELETE FROM PROMOTION_RELATION_SHIP WHERE CINEMA_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_PROMOTIONS
BEFORE DELETE ON PROMOTIONS
FOR EACH ROW
BEGIN
	DELETE FROM PROMOTION_RELATIONSHIP WHERE PROMOTION_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_REVIEW
BEFORE DELETE ON REVIEW
FOR EACH ROW
BEGIN
    UPDATE MOVIES SET VOTE_AVERAGE = ((VOTE_AVERAGE * VOTE_COUNT) - OLD.RATING) / (VOTE_COUNT - 1) WHERE ID = OLD.MOVIE_ID;
	UPDATE MOVIES SET VOTE_COUNT = VOTE_COUNT - 1 WHERE ID = OLD.MOVIE_ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_BOOKING_TICKETS
BEFORE DELETE ON BOOKING_TICKETS
FOR EACH ROW
BEGIN
	UPDATE PAYMENT_TICKETS
    SET AMOUNT = AMOUNT - (SELECT AMOUNT FROM TICKETS WHERE ID = OLD.TICKET_ID)
    WHERE ID = OLD.PAYMENT_TICKET_ID;
    DELETE FROM TICKET WHERE OLD.TICKET_ID = ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_BOOKING_ITEMS
BEFORE DELETE ON BOOKING_ITEMS
FOR EACH ROW
BEGIN
	UPDATE PAYMENT_ITEMS
    SET AMOUNT = AMOUNT - (SELECT PRICE FROM ITEMS WHERE ID = OLD.ITEM_ID) * OLD.QUANTITY
    WHERE ID = OLD.PAYMENT_ITEM_ID;
END; //
DELIMITER ;


-- TRIGGER UPDATE

DELIMITER //
CREATE TRIGGER TG_UPDATE_REVIEW
AFTER UPDATE ON REVIEW
FOR EACH ROW
BEGIN
    UPDATE MOVIES SET VOTE_AVERAGE = ((VOTE_AVERAGE * VOTE_COUNT) - OLD.RATING + NEW.RATING) / (VOTE_COUNT) WHERE ID = NEW.MOVIE_ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_UPDATE_PAYMENT_TICKETS
AFTER UPDATE ON PAYMENT_TICKETS
FOR EACH ROW
BEGIN
	UPDATE PAYMENTS SET TOTAL_AMOUNT = TOTAL_AMOUNT - OLD.AMOUNT + NEW.AMOUNT
    WHERE PAYMENT_TICKET_ID = NEW.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_UPDATE_PAYMENT_ITEMS
AFTER UPDATE ON PAYMENT_ITEMS
FOR EACH ROW
BEGIN
	UPDATE PAYMENTS SET TOTAL_AMOUNT = TOTAL_AMOUNT - OLD.AMOUNT + NEW.AMOUNT
    WHERE PAYMENT_ITEM_ID = NEW.ID;
END; //
DELIMITER ;

-- FAKE DATA:

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

