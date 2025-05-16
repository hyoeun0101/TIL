


### WEHRE 조건
```sql
-- 1~10 사이
SELECT * 
FROM 테이블명
WHERE 컬럼 BETWEEN 1 AND 10;

-- 포함되어있는지
SELECT * 
FROM 테이블명
WHERE 컬럼 IN ('서울', '경기')

-- MEMBER_NAME이 '우'로 시작
SELECT *
FROM 테이블명
WHERE MEMBER_NAME LIKE '우%';

-- TEAM_NAME이 OO핑크
SELECT *
FROM 테이블명
WHERE TEAM_NAME LIKE '__핑크';
```

### 정렬 - ORDER BY
- ASC : 오름차순
- DESC: 내림차순

```SQL
SELECT *
FROM 테이블명
ORDER BY

```

## 🍎DISTINCT

- 중복제거

## 🍎GROUP BY - HAVING

- 집계함수를 사용하서 그룹으로 묶기

집계 함수
|함수명|설명|
|-----|----|
|SUM()|합계|
|AVG()|평균|
|COUNT(DISTINCT)|행의 개수(중복제거)|

그룹 바이에서 묶은 그룹에 대한 조건을 줄 때 HAVING

## 🍎CASE WHEN

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

## 🍎UNION

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

- SELECT하는 컬럼 개수가 같아야 한다.
- SELECT한 컬럼이 동일한 데이터 타입이어야 한다.

- 동일한 컬럼이 있을 시 중복 제거한다.
- 중복 제거없이 모든 컬럼을 SELECT 하려면 `UNION ALL`을 사용한다.

### UNION ALL

- UNION은 정렬 후 모든 컬럼을 거치며 중복 제거하는 일을 한다.
- UNION ALL은 정렬 후 중복 제거하는 일을 하지 않아서 성능이 더 좋다.

