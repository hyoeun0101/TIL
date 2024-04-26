## 🍎 DDL 
- 스키마, 및 컬럼의 데이터 타입, 제약 조간, 색인 구조 등의 데이터베이스 객체를 정의
- CREATE : 스키마, 테이블, 인덱스, 뷰 생성
- ALTER : 스키마, 테이블 수정
- DROP : 스키마, 테이블, 인덱스, 뷰 삭제
### 스키마
= 데이터베이스
- 테이블, 인덱스, 뷰 등의 데이터베이스 객체의 집합
```sql
-- 스키마 생성
CREATE SCHEMA 스키마이름
-- 스키마 삭제
DROP SCHEMA 스키마이름
```

### 테이블 생성
```SQL
-- 테이블 생성
CREATE TABLE 교수(
    교수번호 CHAR(13) NOT NULL,
    교수이름 VARCHAR(30) NOT NULL,
    직위 CHAR(10) NOT NULL,
    소속학과 CHAR(50) NOT NULL,
    연봉 INT NOT NULL,
    PRIMARY KEY(교수번호)
)

-- 테이블 수정 (컬럼 추가)
ALTER TABLE 교수
    ADD COLUMN 나이 INT;

-- 테이블 수정 (컬럼 삭제)
ALTER TABLE 교수
    DROP COLUMN 직위;


-- 테이블 삭제
DROP TABLE 교수;

```
### 테이블 데이터 타입

|문자|숫자|날짜/시간|
|----|----|--------|
|CHAR(n)|INT|DATE|
|VARCHAR(n)|FLOAT|TIME|
|TEXT|DOUBLE|DATETIME|
| -   |DECIMAL(m,n)|TIMESTAMP|


### 테이블 데이터 타입
- CHAR(n)과 VARCHAR(n)
- INT와 FLOAT
- DECIMAL과 NUMBERIC
- DATETIME과 TIMESTAMP
- ENUM과 SET
### 테이블 제약조건
- PRIMARY KEY
- NOT NULL
- UNIQUE
- AUTO_INCREMENT
- DEFAULT
- FOREIGN KEY
- CHECK

## 🍎 DML
- 데이터를 CRUD
- INSERT, SELECT, UPDATE, DELETE

### INSERT, UPDATE, DELETE
```SQL
-- 데이터 삽입
INSERT INTO 테이블이름 VALUES (값1, 값2, 값3...);

INSERT INTO 테이블이름(컬럼1, 컬럼2) VALUES (값1, 값2);

-- 데이터 수정
UPDATE 교수
SET 교수이름 = '김효은'
WHERE 교수번호 = '1';

-- 데이터 삭제
DELETE FROM 테이블명
WHERE 조건
```
### SELECT
