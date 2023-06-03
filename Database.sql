-- passwords are the same text as the username
-- any primary key with 'ID' in it may be changed to have this increment automatically

SELECT Staff.username FROM Staff LEFT JOIN UserIssue ON Staff.username = UserIssue.username
WHERE Staff.username != 'manager1' AND (UserIssue.issueID != 1 OR UserIssue.issueID IS NULL)

-- SELECT managerFlag FROM Staff WHERE username = 'manager1'


-- CREATE LOGIN user1 WITH PASSWORD = 'comp1140isBAE';
-- USE seng2050_test;
-- CREATE USER user1 FOR LOGIN user1;
-- ALTER ROLE db_datareader ADD MEMBER user1;
-- ALTER ROLE db_datawriter ADD MEMBER user1;

DROP TABLE IF EXISTS UserIssue
DROP TABLE IF EXISTS IssueKeyword
DROP TABLE IF EXISTS Keyword
DROP TABLE IF EXISTS KnowledgeBaseArticle
DROP TABLE IF EXISTS Notification
DROP TABLE IF EXISTS Comment
DROP TABLE IF EXISTS Issue
DROP TABLE IF EXISTS Staff
DROP TABLE IF EXISTS Student
DROP TABLE IF EXISTS [User]


CREATE TABLE [User] (
    username VARCHAR(50),
    passwordHash VARCHAR(255) NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    contactNumber VARCHAR(20) NOT NULL,
    role VARCHAR(32) DEFAULT 'student' NOT NULL,

    CHECK (role = 'student' OR role = 'staff'),

    PRIMARY KEY (username)
)

CREATE TABLE Student (
    username VARCHAR(50),
    studentNumber VARCHAR(10) NOT NULL,

    PRIMARY KEY (username),

    FOREIGN KEY (username) REFERENCES [User](username)
        ON UPDATE CASCADE ON DELETE CASCADE
)

CREATE TABLE Staff (
    username VARCHAR(50),
    staffNumber VARCHAR(10) NOT NULL,
    managerFlag BIT DEFAULT 0 NOT NULL,

    PRIMARY KEY (username),

    FOREIGN KEY (username) REFERENCES [User](username)
        ON UPDATE CASCADE ON DELETE CASCADE
)

CREATE TABLE Issue (
    issueID INT IDENTITY(1,1),
    title VARCHAR(100) NOT NULL,
    category VARCHAR(8) NOT NULL,
    status VARCHAR(32) DEFAULT 'New' NOT NULL,
    description VARCHAR(1000) NOT NULL,
    resolutionDetails VARCHAR(1000),
    dateTimeReported DATETIME NOT NULL,
    dateTimeResolved DATETIME,

    CHECK (category = 'Network' OR category = 'Software' OR category = 'Hardware'
            OR category = 'Email' OR category = 'Account'),
    CHECK (status = 'New' OR status = 'In Progress' OR status = 'Waiting on Third Party' OR 'Waiting on Reporter'
            OR status = 'Completed' OR status = 'Not Accepted' OR status = 'Resolved'),

    PRIMARY KEY (issueID)
)

CREATE TABLE Comment (
    commentID INT IDENTITY(1,1),
    comment VARCHAR(250) NOT NULL,
    dateTimePosted DATETIME NOT NULL,
    username VARCHAR(50) NOT NULL,
    issueID INT NOT NULL,

    PRIMARY KEY (commentID),

    FOREIGN KEY (username) REFERENCES [User](username)
        ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (issueID) REFERENCES Issue(issueID)
        ON UPDATE CASCADE ON DELETE NO ACTION
)

CREATE TABLE Notification (
    notificationID INT IDENTITY(1,1),
    message VARCHAR(100) NOT NULL,
    dateTimeSent DATETIME NOT NULL,
    username VARCHAR(50) NOT NULL,
    issueID INT NOT NULL,
	hasSeen BIT NOT NULL DEFAULT 0,

    PRIMARY KEY (notificationID),

    FOREIGN KEY (username) REFERENCES [User](username)
        ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (issueID) REFERENCES Issue(issueID)
        ON UPDATE CASCADE ON DELETE NO ACTION
)

CREATE TABLE Keyword (
    keywordID INT IDENTITY(1,1),
    keyword VARCHAR(50) NOT NULL,

    PRIMARY KEY (keywordID)
)

CREATE TABLE KnowledgeBaseArticle (
    articleID INT IDENTITY(1,1),
    issueID INT NOT NULL,

    PRIMARY KEY (articleID),

    FOREIGN KEY (issueID) REFERENCES Issue(issueID)
        ON UPDATE CASCADE ON DELETE NO ACTION
)

CREATE TABLE UserIssue (
    username VARCHAR(50),
    issueID INT,

    PRIMARY KEY (username, issueID),

    FOREIGN KEY (username) REFERENCES [User](username)
        ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (issueID) REFERENCES Issue(issueID)
        ON UPDATE CASCADE ON DELETE NO ACTION
)

CREATE TABLE IssueKeyword (
    issueID INT,
    keywordID INT,

    PRIMARY KEY (issueID, keywordID),

    FOREIGN KEY (issueID) REFERENCES Issue(issueID)
        ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (keywordID) REFERENCES Keyword(keywordID)
        ON UPDATE CASCADE ON DELETE NO ACTION
)

CREATE TABLE StaffIssue (
    username VARCHAR(50),
    issueID INT,

    PRIMARY KEY (username, issueID),

    FOREIGN KEY (username) REFERENCES [User](username)
        ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (issueID) REFERENCES Issue(issueID)
        ON UPDATE CASCADE ON DELETE NO ACTION
)


INSERT INTO [User] (username, passwordHash, firstName, lastName, email, contactNumber, role)
VALUES
    ('user1', '0a041b9462caa4a31bac3567e0b6e6fd9100787db2ab433d96f6d178cabfce90', 'User', '1', 'user1@hotmail.com', '(02) 4821 2649', 'student'),
    ('user2', '6025d18fe48abd45168528f18a82e265dd98d421a7084aa09f61b341703901a3', 'User', '2', 'user2@bigpond.net', '(07) 6841 2649', 'student'),
    ('staff1', '010f4928749bd109657b1b4ef213359ac420678c72932b0d5bc110076afc52f7', 'Staff', '1', 'staff1@gmail.com', '+61 (05) 9801 3629', 'staff'),
    ('staff2', '00bbfdc27068d300bab70e46dc683a9d81355634eff13b1a491b498925b89a57', 'Staff', '2', 'staff2@newcastle.edu.au', '12345678', 'staff'),
    ('manager1', '380f9771d2df8566ce2bd5b8ed772b0bb74fd6457fb803ab2d267c394d89c750', 'Manager', '1', 'manager@newcastle.edu.au', '87654321', 'staff');

INSERT INTO Student (username, studentNumber)
VALUES
    ('user1', 'c1234567'),
    ('user2', 'c7654321');

INSERT INTO Staff (username, staffNumber, managerFlag)
VALUES
    ('staff1', 'STAFF1', 0),
    ('staff2', 'STAFF2', 0),
    ('manager1', 'MANGR1', 1);

INSERT INTO Issue (title, category, status, description, resolutionDetails, dateTimeReported, dateTimeResolved)
VALUES
    ('No Internet', 'Network', 'New', 'My internet no work help me please sir', NULL, '2023-05-15 10:30:00', NULL),
    ('Why Struts2?', 'Software', 'Resolved', 'Why do we have to use Struts2? How are we supposed to learn this shit and make an entire website in this amount of time?', NULL, '2023-04-25 10:30:00', NULL),
    ('How to post an issue?', 'Email', 'New', 'did i do it right?', NULL, '2023-05-12 08:23:20', NULL);

INSERT INTO Comment (comment, dateTimePosted, username, issueID)
VALUES
    ('This is a comment', '2023-05-15 10:31:00', 'user1', 1),
    ('The reply to that comment', '2023-05-15 10:32:00', 'staff2', 1);

INSERT INTO Notification (message, dateTimeSent, username, issueID, hasSeen)
VALUES
    ('No progress has been made on your issue.', '2023-05-15 12:00:00', 'user1', 1, 0);

INSERT INTO Keyword (keyword)
VALUES
    ('linux'),
    ('googleholism');

INSERT INTO KnowledgeBaseArticle (issueID)
VALUES
    (2);

INSERT INTO UserIssue (username, issueID)
VALUES
    ('user1', 1),
    ('staff2', 1);

INSERT INTO IssueKeyword (issueID, keywordID)
VALUES
    ((SELECT issueID FROM Issue WHERE title = 'No Internet'), (SELECT keywordID FROM Keyword WHERE keyword = 'linux')),
    ((SELECT issueID FROM Issue WHERE title = 'Why Struts2?'), (SELECT keywordID FROM Keyword WHERE keyword = 'googleholism'));




