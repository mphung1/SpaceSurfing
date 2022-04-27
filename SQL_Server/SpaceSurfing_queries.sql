-- Basic Query
-- Query to find userID, Name, Major, Email, Phone number and address of users who are from class of 2023
SELECT userid, username, usermajor, email, phonenumber, address
FROM userAccount
WHERE userclass = '2023'
ORDER BY userid;

-- Query to find ID, title, body text, dates created & updated, likes, comments and shares of post created after 01-APR-22 that has more than 10 likes 
SELECT postid, title, bodytext, created_at, updated_at, likes, comments, shares
FROM post
WHERE likes > 10 AND created_at > '01-APR-22'
ORDER BY likes DESC;

-- AVG, MEDIAN & ROUND keyword
-- Query to find Average price (Rounded values) of furnished living spaces. Group by Housing Type.
SELECT housingtype, ROUND(AVG(price)) as average_price
FROM livingspace
WHERE furnished = 1 
GROUP BY housingtype
ORDER BY average_price;

-- Query to find Median price of living spaces that have a pool. Group by Housing Type.
SELECT housingtype, MEDIAN(price) as median_price
FROM livingspace
WHERE pool = 1 
GROUP BY housingtype
ORDER BY median_price DESC;

-- COUNT keyword
-- Query to count number of users in each major
SELECT usermajor, COUNT(*) as count_User
FROM userAccount
GROUP BY usermajor
ORDER BY count_USER DESC;

-- LIKE & UNION keyword
-- Query to list all the users or living spaces related to Philly
SELECT address FROM userAccount x
WHERE x.address LIKE '%Philadelphia'
    UNION
    SELECT address FROM livingspace y
    WHERE y.address LIKE '%Philadelphia';

--Subquery
-- Query to find the information of users who have their post uploaded to the housing feed before 29 April 2022
-- nested method
SELECT *
FROM userAccount 
WHERE userID in (SELECT userID
                    FROM housingfeed
                    where updated_at < '29-APR-2022');

--other method
SELECT * 
FROM userAccount a, housingfeed b
WHERE b.updated_at < '29-APR-2022' AND a.userID=b.userID;


