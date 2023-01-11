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

# GROUP BY - HAVING

집계 함수
|함수명|설명|
|-----|----|
|SUM()|합계|
|AVG()|평균|
|COUNT(DISTINCT)|행의 개수(중복제거)|

그룹 바이에서 묶은 그룹에 대한 조건을 줄 때 HAVING

# 분기문 CASE WHEN

```sql
SELECT
    (CASE [컬럼명] WHEN [비교값1] THEN [반환값1]
        WHEN [비교값2] THEN [반환값2]
                :
    ELSE [WHEN절 이외의 조건]
    END) AS [별칭 컬럼명]
FROM [테이블명]
```

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
