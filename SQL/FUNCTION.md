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

## 분기함수 - DECODE, CASE

### 🍎DECODE 함수

| 오라클 SQL에서의 표현                 | 의미                                                                      |
| ------------------------------------- | ------------------------------------------------------------------------- |
| DECODE(A, B, X, Y)                    | A = B 이면 X를 출력, A ≠ B 이면 Y를 출력                                  |
| DECODE(A, B, X, C, Y, Z)              | A = B이면 X 출력, A = C이면 Y 출력, A ≠ B 이고 A ≠ C이면 Z 출력           |
| DECODE(A1, B, DECODE(A2, C, X, Y), Z) | A1=B이면서 A2=C이면 X 출력, A1=B이면서 A2≠C이면 Y를 출력, A1≠B이면 Z 출력 |

## TO_CHAR 함수

: 날짜, 숫자를 문자열로 반환

```sql
SELECT TO_CHAR(SYSDATE, 'YYYYMMDD')              --20200723
     , TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') --2020-07-23 11:10:52
  FROM dual
```
