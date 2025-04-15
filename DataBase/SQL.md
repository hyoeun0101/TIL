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

```

### 테이블 데이터 타입

|문자|숫자|날짜/시간|
|----|----|--------|
|CHAR(n)|INT|DATE|
|VARCHAR(n)|FLOAT|TIME|
|TEXT|DOUBLE|DATETIME|
| - |DECIMAL(m,n)|TIMESTAMP|

- 정수 데이터 타입
    - `TINYINT` : 1바이트 정수, -128~127. 나이, 학년 등의 크기가 작은 정수
    - `SMALLINT` : 2바이트 정수, -32768~32767. 물품번호, 인원 등 중간 크기의 정수
    - `INT` : 4바이트 정수. 물품의 금액, 전화번호 등
    - `BIGINT` : 8바이트 정수. 계좌의 잔고, 천문학적인 크기의 정수
- 실수 데이터 타입
    - `DECIMAL(m,n)` : 전체 M자리, 소수점 이하는 N자리. (ex> DECIMAL(5,2) => -999.99~999.99) 
    - `NUMERIC` : DECIMAL과 동일
    - `FLOAT` : 4바이트 크기 부동 소수
    - `FLOAT(P)` : 소수점 이하는 P개
    - `DOUBLE` : 8바이트 크기의 부동 소수
- 날짜와 시간 데이터 타입
    - `DATE` : YYYY-MM-DD
    - `YEAR` : YYYY
    - `TIME` : HH:MI:SS
    - `DATETIME` : YYYY-MM-DD HH:MI:SS
    - `TIMESTAMP` : DATETIME과 동일
- 문자 데이터 타입
    - `CHAR(N)` : 최대 길이 N인 고정길이 문자열
    - `VARCHAR(N)` : 최대 길이 N인 가변길이 문자열
    - `TEXT`, `CLOB` : 길이가 최대 2~4GB인 가변길이 문자열
    - `ENUM` : ex> ENUM('남','여')

- __CHAR(n)과 VARCHAR(n)__ : VARCHAR는 공간 낭비가 없어서 메모리 상으론 효율적이지만 레코드마다 컬럼의 길이가 달라서 관리가 힘들다. 따라서 검색 속도가 느릴 수 있다.
- INT와 FLOAT
- DECIMAL과 NUMBERIC
- DATETIME과 TIMESTAMP
- ENUM과 SET

### 테이블 제약조건
- PRIMARY KEY : 기본키 지정, UNIQUE + NOTNULL
- FOREIGN KEY : 외래키 지정. 참조 컬럼 정의
- NOT NULL : NUL이 될 수 없음.
- UNIQUE : 동일한 컬럼값을 가질 수 없음.
- AUTO_INCREMENT : 레코드가 추가될 때 자동으로 속성값이 1부터 1씩 중가됨.
- DEFAULT
- CHECK : 컬럼값에 특정 조건 지정.
```SQL
CREATE TABLE 개인구매회원(
    이름 CHAR(10) NOT NULL,
    ID CHAR(20) PRIMARY KEY,
    비밀번호 VARCHAR(30) NOT NULL,
    이메일 VARCHAR(100) NOT NULL,
    통신사 CHAR NOT NULL CHECK(통신사 IN('SKT', 'KT', 'LGT')),
    SMS수신 CHAR(1) DEFAULT 'N'
);

CREATE TABLE 교수(
    교수번호 CHAR(13) NOT NULL,
    교수 이름 CHAR(30) NOT NULL,
    소속학과 CHAR(50) NOT NULL,
    PRIMARY KEY(교수번호),
    FOREIGN KEY (소속학과) REFERENCES 학과(학과이름)
);
```

### 테이블 수정, 삭제
```SQL
-- 테이블 수정 (컬럼 추가)
ALTER TABLE 교수
    ADD COLUMN 나이 INT;

-- 테이블 수정 (컬럼 삭제)
ALTER TABLE 교수
    DROP COLUMN 직위;


-- 테이블 삭제
DROP TABLE 교수;
```




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
