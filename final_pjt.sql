DROP DATABASE IF EXISTS final_pjt;

CREATE DATABASE final_pjt;

USE final_pjt;

# 유저
CREATE TABLE users(
	id VARCHAR(100) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    age INT NOT NULL
);


# 피검사, 약처방 일정 관리
CREATE TABLE medicheck(
	id VARCHAR(100),
    bloodCheck boolean, # 피 검사
    medicine boolean, # 약 받기
    date DATE, # 날짜
    FOREIGN KEY(id)
    REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);

# 콜레스테롤
CREATE TABLE chole(
	id VARCHAR(100), # 유저 id
    ldl INT, # LDL
    hdl INT, # HDL
    date DATE, # 날짜
    FOREIGN KEY(id)
    REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);
# 식단
CREATE TABLE diet(
	id VARCHAR(100), # id
    weekday VARCHAR(10), # 요일
    meal VARCHAR(255), # 아침, 점심, 저녁
    food VARCHAR(100), # 음식
    FOREIGN KEY(id)
    REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);

# 은동기록
CREATE TABLE exercise(
	id VARCHAR(100), # id
    date DATE, # 날짜
    exer VARCHAR(1000), # 운동
    FOREIGN KEY(id)
    REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);

# 칼로리
CREATE TABLE calorie(
	id VARCHAR(100), # id
    date DATE, # 날짜
    calorie INT, #칼로리
    FOREIGN KEY(id)
    REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO users()
VALUES ('ssafy','1234','김싸피','ssafy.kim@ssafy.com',20),
('kmkm','1234','강민','krkkmin1@gmail.com',29);
