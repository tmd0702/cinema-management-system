DROP DATABASE MOVIE;
CREATE DATABASE MOVIE;
USE MOVIE;
SET GLOBAL log_bin_trust_function_creators = 1;
-- SET FOREIGN_KEY_CHECKS=1;
SET SQL_SAFE_UPDATES = 1;
CREATE TABLE USER_CATEGORY (
	ID VARCHAR(10),
    CATEGORY VARCHAR(40),
    POINT_LOWERBOUND INT,
    CONSTRAINT PK_UC PRIMARY KEY (ID)
) ENGINE = InnoDB;
CREATE TABLE USERS(
	ID VARCHAR(10) NOT NULL,
    USERNAME VARCHAR(255) UNIQUE, -- TEN TAI KHOAN
    FIRST_NAME VARCHAR(255) NOT NULL,
    LAST_NAME VARCHAR(255) NOT NULL,
    REGIS_DATE DATE NOT NULL,
    -- FULLNAME VARCHAR(255),
    DOB DATE,	-- date of birth
    GENDER VARCHAR(3) NOT NULL,
    ADDRESS VARCHAR(255),
    PHONE VARCHAR(10) NOT NULL,
    EMAIL VARCHAR(255) NOT NULL,
    USER_ROLE VARCHAR(255) NOT NULL,
    STATUS VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
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
    VIEW_COUNT INT DEFAULT 0 NOT NULL,
    POSTER_PATH VARCHAR(50),
    BACKDROP_PATH VARCHAR(50),
    REVENUE DOUBLE DEFAULT 0,
    TAGLINE TEXT,
    VOTE_COUNT INT DEFAULT 0 NOT NULL,
    VOTE_AVERAGE FLOAT DEFAULT 0.0 NOT NULL,
    CONSTRAINT PK_MOVIE PRIMARY KEY(ID)
) ENGINE = InnoDB;
CREATE TABLE GENRES(
	ID VARCHAR(10),
    NAME VARCHAR(50) NOT NULL UNIQUE,
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
    ADDRESS VARCHAR(255) NOT NULL,
    STATUS VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT PK_CINEMA PRIMARY KEY(ID)
) ENGINE = InnoDB;

CREATE TABLE PROMOTIONS(
	ID VARCHAR(10) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    START_DATE DATETIME NOT NULL,
    END_DATE DATETIME NOT NULL,
    DESCRIPTION TEXT,
    STATUS VARCHAR(20) NOT NULL,
    DISCOUNT FLOAT DEFAULT 0.0 NOT NULL,
    USER_CATEGORY_ID VARCHAR(10) NOT NULL,
    CONSTRAINT PK_PROMOTION PRIMARY KEY(ID),
    CONSTRAINT FK_PROMOTION_UC FOREIGN KEY (USER_CATEGORY_ID) REFERENCES USER_CATEGORY(ID)
) ENGINE = InnoDB;
CREATE TABLE REVIEW(
	USER_ID VARCHAR(10) NOT NULL,
    MOVIE_ID VARCHAR(10) NOT NULL,
    RATING FLOAT DEFAULT 0.0 NOT NULL,
    COMMENT VARCHAR(200),
    DATE DATETIME NOT NULL,
    CONSTRAINT PK_REVIEW PRIMARY KEY(USER_ID, MOVIE_ID),
    CONSTRAINT FK_REVIEW_USER FOREIGN KEY(USER_ID) REFERENCES USERS(ID),
    CONSTRAINT FK_REVIEW_MOVIE FOREIGN KEY(MOVIE_ID) REFERENCES MOVIES(ID)
) ENGINE = InnoDB;

CREATE TABLE AUTHENTICATION(
    ID VARCHAR(10) NOT NULL, -- TAI KHOAN
    PASS VARCHAR(255) NOT NULL,
    USER_ID VARCHAR(10) NOT NULL,
    CONSTRAINT PK_ACCOUNT PRIMARY KEY(ID, PASS),
	CONSTRAINT FK_ACCOUNT_USER FOREIGN KEY(USER_ID) REFERENCES USERS(ID)
) ENGINE = InnoDB;
CREATE TABLE SCREEN_ROOMS(
	ID VARCHAR(10) NOT NULL UNIQUE,
    NAME VARCHAR(255) NOT NULL,
    CAPACITY INT DEFAULT 0 NOT NULL,
    STATUS VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CINEMA_ID VARCHAR(10) NOT NULL,
    CONSTRAINT PK_SCREEN PRIMARY KEY(ID),
    CONSTRAINT FK_SCREEN_CINEMA FOREIGN KEY (CINEMA_ID) REFERENCES CINEMAS(ID)
) ENGINE = InnoDB;
CREATE TABLE SEAT_CATEGORY (
	ID VARCHAR(10),
    CATEGORY VARCHAR(50) NOT NULL,
    CONSTRAINT PK_SEAT_CATEGORY PRIMARY KEY(ID)
) ENGINE = InnoDB;
CREATE TABLE SEATS(
	ID VARCHAR(10) NOT NULL UNIQUE,
    NAME VARCHAR(10) NOT NULL,
    SCREEN_ROOM_ID VARCHAR(10) NOT NULL,
    SEAT_CATEGORY_ID VARCHAR(50) NOT NULL, -- VIP, NORMAL, COUPLE
    STATUS VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT PK_SEAT PRIMARY KEY(ID),
    CONSTRAINT FK_SEAT_SCREEN FOREIGN KEY(SCREEN_ROOM_ID) REFERENCES SCREEN_ROOMS(ID),
    CONSTRAINT FK_SEAT_SEAT_CATEGORY FOREIGN KEY (SEAT_CATEGORY_ID) REFERENCES SEAT_CATEGORY(ID)
) ENGINE = InnoDB;
CREATE TABLE SHOW_TIMES (
    ID VARCHAR(10),
    START_TIME TIME NOT NULL,
    STATUS VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT PK_SHOW_TIME PRIMARY KEY (ID)
) ENGINE = InnoDB;
CREATE TABLE SCHEDULES (
	ID VARCHAR(10),
    SHOW_TIME_ID VARCHAR(10) NOT NULL,
    SCREEN_ROOM_ID VARCHAR(10) NOT NULL,
    SHOW_DATE DATE NOT NULL,
    STATUS VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    MOVIE_ID VARCHAR(10) NOT NULL,
    CONSTRAINT PK_SCHEDULES PRIMARY KEY (ID),
    CONSTRAINT FK_SCHEDULE_ROOM FOREIGN KEY (SCREEN_ROOM_ID) REFERENCES SCREEN_ROOMS(ID),
    CONSTRAINT FK_SCHEDULE_MOVIE FOREIGN KEY (MOVIE_ID) REFERENCES MOVIES(ID),
    CONSTRAINT FK_SCHEDULE_ST FOREIGN KEY (SHOW_TIME_ID) REFERENCES SHOW_TIMES(ID)
) ENGINE = InnoDB;
CREATE TABLE TICKETS(
	ID VARCHAR(10),
    SEAT_ID VARCHAR(10) NOT NULL,
    SCHEDULE_ID VARCHAR(10) NOT NULL,
    CONSTRAINT PK_TICKETS PRIMARY KEY (ID),
    CONSTRAINT FK_TICKET_SEAT FOREIGN KEY (SEAT_ID) REFERENCES SEATS(ID),
    CONSTRAINT FK_TICKET_SCHEDULE FOREIGN KEY (SCHEDULE_ID) REFERENCES SCHEDULES(ID)
)ENGINE = InnoDB;
CREATE TABLE PAYMENT_METHODS(
	ID VARCHAR(10),
    NAME VARCHAR(45) NOT NULL,
    STATUS VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
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
    CONSTRAINT FK_PAYMENT_PM FOREIGN KEY (PAYMENT_METHOD_ID) REFERENCES PAYMENT_METHODS(ID),
    CONSTRAINT FK_PAYMENT_PROMOTION FOREIGN KEY (PROMOTION_ID) REFERENCES PROMOTIONS(ID)
) ENGINE = InnoDB;

CREATE TABLE SEAT_PRICES (
	ID VARCHAR(10),
	SEAT_CATEGORY_ID VARCHAR(10) NOT NULL,
    PRICE DECIMAL(20) DEFAULT 0 NOT NULL,
    DATE DATETIME NOT NULL,
    CONSTRAINT PK_PRICE PRIMARY KEY(ID),
    CONSTRAINT FK_SC_ID_PRICE FOREIGN KEY (SEAT_CATEGORY_ID) REFERENCES SEAT_CATEGORY(ID)
) ENGINE = InnoDB;
CREATE TABLE BOOKING_TICKETS(
	PAYMENT_ID VARCHAR(10) NOT NULL,
    TICKET_ID VARCHAR(10) NOT NULL,
    SEAT_PRICE_ID VARCHAR(10),
    CONSTRAINT PK_BOOKING_SEATS PRIMARY KEY (PAYMENT_ID, TICKET_ID),
    CONSTRAINT FK_BS_SP FOREIGN KEY (SEAT_PRICE_ID) REFERENCES SEAT_PRICES(ID),
    CONSTRAINT FK_BS_TICKET FOREIGN KEY (TICKET_ID) REFERENCES TICKETS(ID),
    CONSTRAINT FK_BS_PAYMENT FOREIGN KEY (PAYMENT_ID) REFERENCES PAYMENTS(ID)
) ENGINE = InnoDB;
CREATE TABLE ITEM_CATEGORY(
	ID VARCHAR(10),
    CATEGORY VARCHAR(50) NOT NULL,
    CONSTRAINT PK_ITEM_CATEGORY PRIMARY KEY(ID)
) ENGINE = InnoDB;
CREATE TABLE ITEMS(
	ID VARCHAR(10) NOT NULL UNIQUE,
    NAME VARCHAR(255) NOT NULL,
    ITEM_CATEGORY_ID VARCHAR(10) NOT NULL,		-- LOAI ITEM: NUOC, BAP, ...
    REVENUE DOUBLE DEFAULT 0,
    UNIT VARCHAR(20) NOT NULL,
    QUANTITY INT DEFAULT 0 NOT NULL,
    STATUS VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT PK_ITEM PRIMARY KEY(ID),
    CONSTRAINT FK_ITEM_ITEM_CATEGORY FOREIGN KEY (ITEM_CATEGORY_ID) REFERENCES ITEM_CATEGORY(ID)
) ENGINE = InnoDB;
CREATE TABLE ITEM_PRICES (
	ID VARCHAR(10),
	ITEM_CATEGORY_ID VARCHAR(10) NOT NULL,
    PRICE DECIMAL(20) DEFAULT 0 NOT NULL,
    DATE DATETIME NOT NULL,
    CONSTRAINT PK_PRICE PRIMARY KEY(ID),
    CONSTRAINT FK_IC_ID_PRICE FOREIGN KEY (ITEM_CATEGORY_ID) REFERENCES ITEM_CATEGORY(ID)
) ENGINE = InnoDB;
CREATE TABLE BOOKING_ITEMS(
	PAYMENT_ID VARCHAR(10),
	ITEM_ID VARCHAR(10),
    QUANTITY INT NOT NULL,
    ITEM_PRICE_ID VARCHAR(10),
    CONSTRAINT PK_BOOKING_ITEM PRIMARY KEY (ITEM_ID, PAYMENT_ID),
    CONSTRAINT FK_BI_IP FOREIGN KEY (ITEM_PRICE_ID) REFERENCES ITEM_PRICES(ID),
    CONSTRAINT FK_BI_ITEM FOREIGN KEY (ITEM_ID) REFERENCES ITEMS(ID),
    CONSTRAINT FK_BI_TI FOREIGN KEY (PAYMENT_ID) REFERENCES PAYMENTS(ID)
)ENGINE = InnoDB;

-- PROCEDURE:

-- tăng count view
DELIMITER //
CREATE PROCEDURE increaseViewCount()
BEGIN
	UPDATE MOVIES SET VIEW_COUNT = VIEW_COUNT + 1;
    COMMIT;
END; //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE updateUserCategory(uid VARCHAR(10))
BEGIN
	UPDATE USERS U SET U.USER_CATEGORY_ID = (SELECT UC.ID FROM USER_CATEGORY UC
											WHERE UC.POINT_LOWERBOUND <= U.SCORE
                                            ORDER BY UC.POINT_LOWERBOUND DESC LIMIT 1)
										WHERE U.ID = uid;
END; //
DELIMITER ;

DELIMITER //
CREATE FUNCTION signInFunction(username VARCHAR(255), pass varchar(255)) RETURNS BOOLEAN
BEGIN
DECLARE res BOOLEAN;
DECLARE passw VARCHAR(255);
SET passw = (SELECT A.PASS FROM AUTHENTICATION A JOIN USERS U ON A.USER_ID = U.ID
			WHERE U.USERNAME = username);
IF (passw = pass) THEN
	SET res = 1;
ELSE
 	SET res = 0;
END IF;
RETURN res;
END; //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE signUpProcedure(uid varchar(10), auth_id varchar(10),
								username varchar(255),firstname varchar(255),
								pass varchar(255), lastname varchar(255), dob date,
                                gender varchar(3), address varchar(255),
                                phone varchar(10), email varchar(255))
BEGIN
DECLARE USERID VARCHAR(10);
DECLARE USERCATEGORY VARCHAR(10);
SET USERCATEGORY = (SELECT ID FROM USER_CATEGORY WHERE CATEGORY = 'MEMBER');
SET USERID = (SELECT U.ID FROM USERS U WHERE U.EMAIL = email);
IF (USERID IS NULL) THEN
	INSERT INTO USERS VALUES (uid, username, firstname, lastname, dob, gender, address, phone, email, 'CUSTOMER', 0, USERCATEGORY);
    INSERT INTO AUTHENTICATION VALUES (auth_id, pass, uid);
    COMMIT;
ELSE
	SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'HAVE BEEN EXISTED ACCOUNT USE THIS EMAIL';
END IF;
END; //
DELIMITER ;
-- TRIGGER INSERT:
-- khi thêm người dùng thì set mặc định Score là 0 và user_category là Member
DELIMITER //
CREATE TRIGGER TG_INSERT_USERS
BEFORE INSERT ON USERS
FOR EACH ROW
BEGIN
	SET NEW.USER_CATEGORY_ID = (SELECT ID FROM USER_CATEGORY WHERE CATEGORY = 'Member');
    SET NEW.SCORE = 0;
END;//
DELIMITER ;
-- Sau khi thêm payment mới thì các giá trị khác thay đổi

DELIMITER //
DROP TRIGGER IF EXISTS TG_INSERT_PAYMENTS;
CREATE TRIGGER TG_INSERT_PAYMENTS
AFTER INSERT ON PAYMENTS FOR EACH ROW
BEGIN

    DECLARE PAYMENT_DATETIME DATETIME;
	SET PAYMENT_DATETIME = (SELECT CAST(CONCAT(SHOW_DATE,' ', ST.START_TIME) AS DATETIME )
							FROM SCHEDULES SCH, SHOW_TIMES ST
							WHERE SCH.SHOW_TIME_ID = ST.ID AND SCH.ID = NEW.SCHEDULE_ID);
	IF(PAYMENT_DATETIME < NEW.PAYMENT_DATE) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'The payment datetime have to be before the movie on showtime';
	ELSE
		UPDATE USERS SET SCORE = SCORE + NEW.TOTAL_AMOUNT / 10000 WHERE ID = NEW.USER_ID;
	CALL updateUserCategory(NEW.USER_ID);
END IF;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_INSERT_BOOKING_ITEMS
AFTER INSERT ON BOOKING_ITEMS FOR EACH ROW
BEGIN
	UPDATE ITEMS SET REVENUE = REVENUE + (SELECT IP.PRICE * NEW.QUANTITY
									  FROM ITEM_PRICES IP
									  WHERE IP.ID = NEW.ITEM_PRICE_ID), QUANTITY = QUANTITY - NEW.QUANTITY
	WHERE ID = NEW.ITEM_ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_INSERT_BOOKING_TICKETS
AFTER INSERT ON BOOKING_TICKETS FOR EACH ROW
BEGIN
	UPDATE MOVIES SET REVENUE = REVENUE + (SELECT SP.PRICE
								FROM SEAT_PRICES SP
								WHERE SP.ID = NEW.SEAT_PRICE_ID)
		WHERE ID = (SELECT S.MOVIE_ID FROM SCHEDULES S, TICKETS T WHERE T.SCHEDULE_ID = S.ID AND T.ID = NEW.TICKET_ID);
END; //
DELIMITER ;


-- khi thêm một bình luận thì kiểm tra phim đã được chiếu hay chưa
DELIMITER //
CREATE TRIGGER TG_INSERT_REVIEW
BEFORE INSERT ON REVIEW
FOR EACH ROW
BEGIN
DECLARE MOVIE_RELEASE_DATE DATE;
SET MOVIE_RELEASE_DATE = (SELECT RELEASE_DATE
						  FROM MOVIES
                          WHERE NEW.MOVIE_ID = MOVIES.ID);
	IF(NEW.DATE > MOVIE_RELEASE_DATE) THEN
		UPDATE MOVIES
        SET VOTE_AVERAGE = ((VOTE_AVERAGE * VOTE_COUNT) + NEW.RATING) / (VOTE_COUNT + 1)
        WHERE ID = NEW.MOVIE_ID;
		UPDATE MOVIES SET VOTE_COUNT = VOTE_COUNT + 1
        WHERE ID = NEW.MOVIE_ID;
	ELSE
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'The movie have not been released';
        END IF;
END; //
DELIMITER ;




-- TRIGGER UPDATE

-- khi nhập một giá vé mới phải đảm bảo giá vé lớn hơn không
DELIMITER //
CREATE TRIGGER TG_INSERT_ITEM_PRICES
AFTER UPDATE ON ITEM_PRICES
FOR EACH ROW
BEGIN
	IF(NEW.PRICE < 0) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'The price is not valid';
	END IF;
END;//
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_INSERT_SEAT_PRICES
AFTER UPDATE ON SEAT_PRICES
FOR EACH ROW
BEGIN
	IF(NEW.PRICE < 0) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'The price is not valid';
	END IF;
END;//
DELIMITER ;
-- khi update review thì tính lại rating của phim

DELIMITER //
CREATE TRIGGER TG_UPDATE_REVIEW
AFTER UPDATE ON REVIEW
FOR EACH ROW
BEGIN
    UPDATE MOVIES SET VOTE_AVERAGE = ((VOTE_AVERAGE * VOTE_COUNT) - OLD.RATING + NEW.RATING) / (VOTE_COUNT) WHERE ID = NEW.MOVIE_ID;
END; //
DELIMITER ;
DELIMITER //
CREATE TRIGGER TG_UPDATE_USER_CATEGORY
AFTER UPDATE ON USER_CATEGORY
FOR EACH ROW
BEGIN
    UPDATE USERS U SET U.USER_CATEGORY_ID = (SELECT ID FROM USER_CATEGORY WHERE POINT_LOWERBOUND <= U.SCORE ORDER BY POINT_LOWERBOUND DESC LIMIT 1);
END; //
DELIMITER ;

-- TRIGGER DELETE:

DELIMITER //
CREATE TRIGGER TG_DELETE_USER_CATEGORY
BEFORE DELETE ON USER_CATEGORY
FOR EACH ROW
BEGIN
	DELETE FROM USERS WHERE USER_CATEGORY_ID = OLD.ID;
    DELETE FROM PROMOTIONS WHERE USER_CATEGORY_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_ITEM_CATEGORY
BEFORE DELETE ON ITEM_CATEGORY
FOR EACH ROW
BEGIN
	DELETE FROM ITEMS WHERE ITEM_CATEGORY_ID = OLD.ID;
    DELETE FROM ITEM_PRICES WHERE ITEM_CATEGORY_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_SEAT_CATEGORY
BEFORE DELETE ON SEAT_CATEGORY
FOR EACH ROW
BEGIN
	DELETE FROM SEATS WHERE SEAT_CATEGORY_ID = OLD.ID;
    DELETE FROM SEAT_PRICES WHERE SEAT_CATEGORY_ID = OLD.ID;
END; //
DELIMITER ;


DELIMITER //
CREATE TRIGGER TG_DELETE_USERS
BEFORE DELETE ON USERS
FOR EACH ROW
BEGIN
	DELETE FROM AUTHENTICATION WHERE OLD.ID = USER_ID;
    DELETE FROM REVIEW WHERE OLD.ID = USER_ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_CINEMAS
BEFORE DELETE ON CINEMAS
FOR EACH ROW
BEGIN
	DELETE FROM SCREEN_ROOMS WHERE CINEMA_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_SCREENROOMS
BEFORE DELETE ON SCREEN_ROOMS
FOR EACH ROW
BEGIN
	DELETE FROM SEATS WHERE SCREEN_ROOM_ID = OLD.ID;
    DELETE FROM SCHEDULES WHERE SCREEN_ROOM_ID = OLD.ID;
END; //
DELIMITER ;


DELIMITER //
CREATE TRIGGER TG_DELETE_SCHEDULES
BEFORE DELETE ON SCHEDULES
FOR EACH ROW
BEGIN
	DELETE FROM TICKETS WHERE SCHEDULE_ID = OLD.ID;
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
CREATE TRIGGER TG_DELETE_TICKETS
BEFORE DELETE ON TICKETS
FOR EACH ROW
BEGIN
	DELETE FROM BOOKING_TICKETS WHERE TICKET_ID = OLD.ID;
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_BOOKING_TICKETS
BEFORE DELETE ON BOOKING_TICKETS
FOR EACH ROW
BEGIN
	UPDATE PAYMENTS SET TOTAL_AMOUNT = TOTAL_AMOUNT - (SELECT PRICE FROM SEAT_PRICES WHERE SEAT_CATEGORY_ID = (SELECT S.SEAT_CATEGORY_ID FROM SEATS S, TICKETS T WHERE S.ID = T.SEAT_ID AND T.ID = OLD.TICKET_ID));
    UPDATE USERS SET SCORE = SCORE - (SELECT PRICE FROM SEAT_PRICES WHERE SEAT_CATEGORY_ID = (SELECT S.SEAT_CATEGORY_ID FROM SEATS S, TICKETS T WHERE S.ID = T.SEAT_ID AND T.ID = OLD.TICKET_ID))/1000 WHERE ID = (SELECT P.USER_ID FROM PAYMENTS P WHERE OLD.PAYMENT_ID = P.ID);
END; //
DELIMITER ;

DELIMITER //
CREATE TRIGGER TG_DELETE_MOVIES
BEFORE DELETE ON MOVIES
FOR EACH ROW
BEGIN
	DELETE FROM MOVIE_GENRES WHERE MOVIE_ID = OLD.ID;
    DELETE FROM REVIEW WHERE MOVIE_ID = OLD.ID;
    DELETE FROM SCHEDULES WHERE MOVIE_ID = OLD.ID;
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
CREATE TRIGGER TG_DELETE_REVIEW
BEFORE DELETE ON REVIEW
FOR EACH ROW
BEGIN
    UPDATE MOVIES
    SET VOTE_AVERAGE = ((VOTE_AVERAGE * VOTE_COUNT) - OLD.RATING) / (VOTE_COUNT - 1)
    WHERE ID = OLD.MOVIE_ID;
	UPDATE MOVIES SET VOTE_COUNT = VOTE_COUNT - 1 WHERE ID = OLD.MOVIE_ID;
END; //
DELIMITER ;
INSERT INTO AUTHENTICATION VALUES('1', 'sa123456', 'USE_00001');