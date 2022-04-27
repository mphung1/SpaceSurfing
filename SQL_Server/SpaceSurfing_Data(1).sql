--livingSpace data
INSERT INTO livingSpace
VALUES(0, NULL, 'xyz.com', 800, 'Andy', 123465789, '638 Voss Street, Sacramento', '31-JUL-2022', 'apartment', 4, 2, 3, 1, 0, 0, 1, 1, 1);

INSERT INTO livingSpace
VALUES(1, NULL, 'xyzw.com', 1000, 'Margaret', 123465798, '291 Berry Street, Atlanta', '15-JUL-2022', 'studio', 2, 0, 0, 1, 0, 1, 0, 1, 0);

INSERT INTO livingSpace
VALUES(2, NULL, 'abcd.com', 1200, 'Jason', 123465978, '123 Peachtree Street, Houston', '15-AUG-2022', 'townhouse', 3, 0, 0, 0, 1, 1, 0, 0, 1);

INSERT INTO livingSpace
VALUES(3, NULL, 'ahsafa.com', 1400, 'Scammer', 111111111, '789 Baring Street, Philadelphia', '01-APR-2022', 'mansion', 5, 3, 4, 1, 0, 0, 0, 1, 1);

INSERT INTO livingSpace
VALUES(4, NULL, 'uuoo.com', 700, 'Mark', 222333444, '250 Spring Mill Road, Philadelphia', '04-MAR-2022', 'studio', 3, 2, 4, 0, 0, 1, 0, 1, 0);

INSERT INTO livingSpace
VALUES(5, NULL, 'poiu.com', 900, 'Aidan', 2345678910, '450 Stone Street, Sacramento', '02-SEP-2022', 'apartment', 2, 2, 2, 0, 1, 0, 1, 1, 1);

INSERT INTO livingSpace
VALUES(6, NULL, 'null.com', 1000, 'Athur', 4564564560, '7676 Bloomington, Sacramento', '03-SEP-2022', 'apartment', 3, 3, 3, 0, 1, 1, 0, 0, 0);

INSERT INTO livingSpace
VALUES(7, NULL, 'tuqo.com', 1100, 'Doyle', 5675675670, '3321 Castle, Spring', '04-SEP-2022', 'apartment', 4, 2, 3, 1, 1, 1, 1, 1, 1);

INSERT INTO livingSpace
VALUES(8, NULL, 'qffdc.com', 600, 'Jessie', 4567887654, '980 Dallas, Houston', '07-DEC-2022', 'apartment', 2, 1, 1, 0, 0, 0, 0, 0, 0);

INSERT INTO livingSpace
VALUES(9, NULL, 'fqqff.com', 700, 'Anna', 7890609876, '4333 Pillsbury, Milwaukee', '21-JUN-2022', 'townhouse', 5, 2, 2, 0, 1, 1, 0, 0, 1);

INSERT INTO livingSpace
VALUES(10, NULL, 'mnbvc.com', 1300, 'Max', 1234987654, '890 Baring Street, Philadelphia', '19-MAY-2022', 'mansion', 6, 4, 5, 0, 0, 1, 1, 1, 1);


-- sublessor data
INSERT INTO sublessor 
VALUES(0, 5);

INSERT INTO sublessor 
VALUES(1, 4);

INSERT INTO sublessor 
VALUES(2, 3);

INSERT INTO sublessor 
VALUES(3, 2);

INSERT INTO sublessor 
VALUES(4, 1);

INSERT INTO sublessor 
VALUES(5, 0);

INSERT INTO sublessor 
VALUES(6, 10);

INSERT INTO sublessor 
VALUES(7, 9);

INSERT INTO sublessor 
VALUES(8, 8);

INSERT INTO sublessor 
VALUES(9, 7);

INSERT INTO sublessor 
VALUES(10, 6);


--renter data
INSERT INTO renter 
VALUES(0, 4);

INSERT INTO renter 
VALUES(1, 3);

INSERT INTO renter 
VALUES(2, 10);

INSERT INTO renter 
VALUES(3, 2);

INSERT INTO renter 
VALUES(4, 1);

INSERT INTO renter 
VALUES(5, 7);

INSERT INTO renter 
VALUES(6, 0);

INSERT INTO renter 
VALUES(7, 8);

INSERT INTO renter 
VALUES(8, 9);

INSERT INTO renter 
VALUES(9, 5);

INSERT INTO renter 
VALUES(10, 10);


-- userAccount data
INSERT INTO userAccount 
VALUES (0, NULL, 'James', 2024, 'Computer Engineering', 7, 7, 'm', 'james@gmail.com', 1234567893, '450 Stone, Houston', NULL, 0, 1, 1, 0, 1, 'music');

INSERT INTO userAccount 
VALUES (1, NULL, 'Dayton', 2025, 'Civil Engineering', 6, 6, 'm', 'dayton@gmail.com', 1234567980, '638 Voss, Houston', NULL, 0, 1, 0, 0, 0, 'sports');

INSERT INTO userAccount 
VALUES (2, NULL, 'Ashley', 2023, 'Chemical Engineering', 5, 5, 'f', 'ashley@gmail.com', 1234569878, '291 Berry, Bellaire', NULL, 0, 0, 1, 0, 1, 'sing');

INSERT INTO userAccount  
VALUES (3, NULL, 'Diana', 2023, 'Business', 4, 4, 'f', 'diana@gmail.com', 9876543215, '123 Peachtree, Atlanta', NULL, 0, 0, 0, 1, 1, 'dance');

INSERT INTO userAccount 
VALUES (4, NULL, 'Franklin', 2022, 'Business', 3, 3, 'm', 'franklin@gmail.com', 3334455551, '4333 Pillsbury, Milwaukee', NULL, 1, 0, 0, 0, 0, 'gaming');

INSERT INTO userAccount 
VALUES (5, NULL, 'Jennifer', 2023, 'Psychology', 2, 2, 'f', 'jennifer@gmail.com', 9876543325, '7676 Bloomington, Sacramento', NULL, 1, 0, 1, 0, 1, 'study');

INSERT INTO userAccount 
VALUES (6, NULL, 'Jared', 2025, 'Computer Science', 1, 1, 'm', 'jared@gmail.com', 9876545641, '890 Baring Street, Philadelphia', NULL, 1, 1, 0, 0, 1, 'research');

INSERT INTO userAccount 
VALUES (7, NULL, 'Nick', 2023, 'Business', 0, 0, 'm', 'nick@gmail.com', 5827902251, '330 Baring Street, Philadelphia', NULL, 1, 0, 0, 1, 1, 'eating');

INSERT INTO userAccount  
VALUES (8, NULL, 'Adam', 2023, 'Mechanical Engineering', 8, 8, 'm', 'adam@gmail.com', 5827902261, '3321 Castle, Spring', NULL, 0, 0, 0, 0, 1, 'skiing');

INSERT INTO userAccount  
VALUES (9, NULL, 'Tom', 2024, 'Biology', 9, 9, 'm', 'tom@gmail.com', 5827902271, '250 Spring Mill Road, Philadelphia', NULL, 0, 0, 0, 0, 1, 'badminton');

INSERT INTO userAccount
VALUES (10, NULL, 'Ahmad', 2025, 'Chemistry', 10, 10, 'm', 'ahmad@gmail.com', 7538476221, '980 Dallas, Houston', NULL, 1, 1, 1, 1, 0, 'fencing');


--housingfeed data
INSERT INTO housingFeed 
VALUES(0, 2,'01-JAN-2022','05-MAY-2022');

INSERT INTO housingFeed 
VALUES(1, 3,'01-JAN-2022',NULL);

INSERT INTO housingFeed 
VALUES(2, 6,'02-JAN-2022','03-JAN-2022');

INSERT INTO housingFeed
VALUES(3, 8,'02-FEB-2022','03-FEB-2022');

INSERT INTO housingFeed
VALUES(4, 7,'02-MAR-2022','03-MAR-2022');

INSERT INTO housingFeed
VALUES(5, 10,'17-MAY-2022','18-MAY-2022');

--Post data
INSERT INTO post
VALUES(0, 5, 0,'Sublet 450 Stone Street, Sacramento', 'this is text', NULL, NULL, '02-APR-2022', '04-APR-2022', 15, 3, 6);

INSERT INTO post
VALUES(1, 0, 1,'Sublet 638 Voss Street, Sacramento', 'this is text1', NULL, NULL, '03-APR-2022', '05-APR-2022', 16, 4, 5);

INSERT INTO post
VALUES(2, 1, 2,'Sublet 291 Berry Street, Atlanta', 'this is text2', NULL, NULL, '07-APR-2022', '09-APR-2022', 10, 2, 3);

INSERT INTO post
VALUES(3, 2, 3,'Sublet 123 Peachtree Street, Houston', 'this is text3', NULL, NULL, '09-APR-2022', '13-APR-2022', 11, 15, 12);

INSERT INTO post
VALUES(4, 3, 5,'Sublet 789 Baring Street, Philadelphia', 'this is text4', NULL, NULL, '09-MAY-2022', '12-MAY-2022', 9, 2, 1);

INSERT INTO post
VALUES(5, 4, 9,'Sublet 250 Spring Mill Road, Philadelphia', 'this is text5', NULL, NULL, '21-MAR-2022', '27-MAR-2022', 9, 6, 0);



