### 🍎DECODE 함수

```
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

### 🍎TO_CHAR 함수

: 날짜, 숫자를 문자열로 변환

```sql
SELECT TO_CHAR(SYSDATE, 'YYYYMMDD')              --20200723
     , TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') --2020-07-23 11:10:52
  FROM dual
```

- `SYSDATE` : 오늘 날짜
- `SYSDATE-1` : 어제 날짜

### NVL(데이터, 지정값)

: 데이터가 NULL이면 지정값으로 변환.

### TO_DATE()

: 문자열을 날짜 데이터로 변환

```sql
SELECT TO_DATE('2021-02-18', 'YYYY-MM-DD'),
	TO_DATE('20210219', 'YYYY-MM-DD')
    FROM dual;
```

## 🍎NVL 함수

```sql
NVL('데이터','지정값')
```

: 데이터가 null이면 지정값

## 🍎NVL2 함수

```sql
NVN2("데이터", 'N', 'Y')
```

: 데이터가 null이면 N, null이 아니면 Y

## 🍎ROW_NUMBER
