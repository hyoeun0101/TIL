## WEHRE 조건

- `WHERE num BETWEEN 1 AND 10` : num이 1 부터 10사이
- `WHERE addr IN ('서울','경기')` :addr가 서울, 경기에 속하다
- `WHERE mem_name LIKE '우%'` : mem_name이 '우'로 시작
- `WHERE mem_name LIKE '__핑크'` : mem_name이 OO핑크. ex)블랙핑크, 에이핑크

## ORDER BY

정렬

- ASC : 오름차순
- DESC: 내림차순

- ++ SELECT DISTINCT : 중복 제거

## GROUP BY - HAVING

집계 함수
|함수명|설명|
|-----|----|
|SUM()|합계|
|AVG()|평균|
|COUNT(DISTINCT)|행의 개수(중복제거)|

그룹 바이에서 묶은 그룹에 대한 조건을 줄 때 HAVING

## 분기문 CASE WHEN
### 예시>
```sql
SELECT ENAME
     , DNUMBER
     , (CASE DNUMBER WHEN ‘D1001’ THEN ‘문구생산부’
                     WHEN ‘D2001’ THEN ‘가구생산부’
                     WHEN ‘D3001’ THEN ‘악세사리생산부’
                     WHEN ‘D4001’ THEN ‘전자기기생산부’
                     WHEN ‘D5001’ THEN ‘음료생산부’
                     ELSE ‘부서없음’
                     END)as “부서명”
   FROM tEmployee as tem
ORDER BY ENAME
```
또는
```sql
SELECT ENAME
     , DNUMBER
     , (CASE WHEN DNUMBER = ‘D1001’ THEN ‘문구생산부’
             WHEN DNUMBER = ‘D2001’ THEN ‘가구생산부 ’
             WHEN DNUMBER = ‘D3001’ THEN ‘악세사리생산부’
             WHEN DNUMBER = ‘D4001’ THEN ‘전자기기생산부’
             WHEN DNUMBER = ‘D5001’ THEN ‘음료생산부’
             ELSE ‘부서없음’
             END)as “부서명”
  FROM tEmployee as tem
ORDER BY ENAME

```
[결과]    
|ENAME|DNUMBER|부서명|
|-----|-------|-----|
|김문구|D1001|문구생산부|
## 집합연산- UNION

```sql
SELECT [컬럼1]
    , [컬럼2]
    , [컬럼3]
FROM [테이블명]
UNION
SELECT [컬럼1]
    , [컬럼2]
    , [컬럼3]
FROM [테이블명2]
```

- 동일한 컬럼이 있을 시 중복 제거함.
- 동일한 컬럼 중복하려면 `UNION ALL` 작성

## 🍎UNION , UNION ALL

### UNION, UNION ALL in Oracle

1. select한 모든 컬럼 집합
2. UNION은 정렬 후 모든 컬럼을 거쳐 중복 제거하는 일을 한다.

- 따라서 UNION ALL이 성능은 더 좋다.

### UNION 조건

- SELECT한 컬럼 개수 같아야한다.
- SELECT한 컬럼 동일한 데이터 타입이여야한다.
