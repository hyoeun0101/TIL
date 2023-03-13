## 🍎 MERGE

MERGE문을 사용하면 하나의 쿼리문으로 INSERT, UPDATE, DELETE 작업을 할 수 있다.  
테이블에 데이터가 있으면 UPDATE (또는 DELETE)하고, 없으면 INSERT한다.

## 🍎 MERGE 사용법

### 1. 단일 테이블 사용법 - DUAL

```sql
MERGE INTO 테이블명 A
     USING DUAL
        ON 조건절
WHEN MATCHED THEN --일치하는 경우 [update|delete]
UPDATE SET A.컬럼명 = 변경값
WHEN NOT MATCHED THEN --불일치하는 경우[insert]
   INSERT (컬럼)
   VALUES (값)
```

: ON 조건절이 일치하면 조건과 일치하는 튜플을 UPDATE한다. 일치하지 않으면 테이블 A에 INSERT한다.

### 2. 동일한 테이블 구조를 가지고있는 테이블 B로부터 데이터 옮기는 법

```sql
MERGE INTO 테이블 A
     USING 테이블 B
        ON A.id = B.id
WHEN MATCHED THEN
UPDATE SET A.컬럼 = 변경값
WHEN NOT MATCHED THEN
    INSERT (컬럼) VALUES (값)
```

- 테이블 A와 테이블 B는 동일한 테이블 구조를 가지고 있다.
- ON 절의 조건은 PK를 사용해야한다. 그렇지 않으면 입력 시 중복이 발생하여 에러가 발생할 수 있다.

### 3. INSERT만 하기

```sql
MERGE INTO 테이블 A
     USING DUAL
        ON A.id = '1234'
WHEN NOT MATCHED THEN
    INSERT (컬럼)
    VALUES (값)
```

커서
