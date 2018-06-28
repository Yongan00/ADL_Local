CREATE TABLE profile(id int primary key, username char(25));

CREATE TABLE record(
id INT primary key auto_increment,
light float,
xAcce float,
yAcce float,
zAccr float,
angle float,
azimuth float,
pitch float,
roll float,
latitude float,
longitude float,
altitude float,
hour int,
moving float,
turning float,
lightChanging float,
dark float,
accel float,
chargingStatus int,
screenOn int,
earPlug int, 
actionLabel char(50),
timeStamp datetime,
userID int,
CONSTRAINT FOREIGN KEY(userID) REFERENCES profile(id) ON DELETE CASCADE);

DROP TABLE record;
INSERT INTO profile values(0, "PhilSamsang")
