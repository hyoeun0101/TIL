## 🔴 MERGE
- insert, update, delete를 하나의 쿼리로 처리할 수 있음.
- 데이터가 존재하면 update (또는 delete)하고, 없으면 insert 한다.

## 🔴 MERGE 사용법

### 🟡 단일 테이블 사용법(DUAL)
- ON 조건절이 일치하면 update. 일치하지 않으면 insert.

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

```sql
MERGE INTO S_CUSTOMER S 
    USING DUAL
       ON (S.CUSTID = '1234')
WHEN MATCHED THEN
UPDATE 
    SET

```
2. __동일한 테이블 구조를 가지고있는 테이블 B로부터 데이터 옮기는 법__
   - 테이블 A와 테이블 B는 동일한 테이블 구조를 가지고 있다.
   - ON 절의 조건은 PK를 사용해야한다. 그렇지 않으면 입력 시 중복이 발생하여 에러가 발생할 수 있다.

```sql
MERGE INTO 테이블 A
     USING 테이블 B
        ON A.id = B.id
WHEN MATCHED THEN
UPDATE SET A.컬럼 = 변경값
WHEN NOT MATCHED THEN
    INSERT (컬럼) VALUES (값)
```


 3. __INSERT만 하기__

```sql
MERGE INTO 테이블 A
     USING DUAL
        ON A.id = '1234'
WHEN NOT MATCHED THEN
    INSERT (컬럼)
    VALUES (값)
```





```sql
INSERT INTO A_TABLE (COL1, COL2, COL3)
VALUES (VAL1, VAL2, VAL3)
ON CONFLICT (COL1) DO
UPDATE SET COL2 = 'TEST'

```
