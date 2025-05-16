## 🔴 PostgreSQL

### 🟡 COALESCE

```sql
-- A가 NULL이면 N
SELECT COALESCE(A, 'N') FROM 테이블; 

-- A가 NULL이면 B, B가 NULL이면 C, C가 NULL이면 N
SELECT COALESCE(A, B, C, ...,'N') FROM 테이블;
```

### 🟡 CONCAT
- 문자열 합치기
- CONCAT 대신 `||` 사용 가능
```sql
-- 1234김철수00
SELECT CONCAT(mber_id, mber_nm, '00') FROM 테이블;


-- 1234김철수00
SELECT mber_id || mber_nm || '00' FROM 테이블;

```


### 🟡 TO_DATE()
- 문자열 -> 날짜

```sql
SELECT TO_DATE('2021-02-18', 'YYYY-MM-DD')
     , TO_DATE('20210219', 'YYYY-MM-DD')
  FROM dual;
```


### 🟡 ROW_NUMBER() 사용법
- 각 행에 대해 고유한 번호를 부여

```sql
-- salary 내림차순으로 번호 부여.
SELECT 
    ROW_NUMBER() OVER (ORDER BY salary DESC) AS row_num
     , id
     , name
     , salary
FROM employees;
```


<br><br><br><br><br><br>



 
## 🔴 Oracle
### 🟡 DECODE

```sql
DECODE(expr, search, result [,search, result]... [,default])
```

- expr과 search가 같으면 result 반환. 같지 않으면 default 반환.default가 없으면 null 반환

- 인자는 숫자, 문자가 될 수 있다.
- Oracle은 단축 평가(shor-circuit evaluation)을 사용한다. 따라서 expr과 비교하기 전에 search를 평가한다.

| 오라클 SQL에서의 표현                 | 의미                                                                      |
| ------------------------------------- | ------------------------------------------------------------------------- |
| DECODE(A, B, X, Y)                    | A = B 이면 X를 출력, A ≠ B 이면 Y를 출력                                  |
| DECODE(A, B, X, C, Y, Z)              | A = B이면 X 출력, A = C이면 Y 출력, A ≠ B 이고 A ≠ C이면 Z 출력           |
| DECODE(A1, B, DECODE(A2, C, X, Y), Z) | A1=B이면서 A2=C이면 X 출력, A1=B이면서 A2≠C이면 Y를 출력, A1≠B이면 Z 출력 |


### 🟡 TO_CHAR

: 날짜, 숫자-> 문자열

```sql
SELECT TO_CHAR(SYSDATE, 'YYYYMMDD')              --20200723
     , TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') --2020-07-23 11:10:52
  FROM dual
```
- Oracle
  - `SYSDATE` : 오늘 날짜
  - `SYSDATE-1` : 어제 날짜



### 🟡 NVL(데이터, 지정값)

- 데이터가 NULL이면 지정값으로 변환.
```sql
NVL('데이터','지정값')
```


### 🟡 NVL2 함수
- 데이터가 null이면 N, null이 아니면 Y

```sql
NVN2("데이터", 'N', 'Y')
```



