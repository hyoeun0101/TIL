[TOPIC]  
|tid |title |description |author_id|
|----|-----|-----------|----------|
|001| 제목1| 세부사항1| author1|
|002| 제목2| 세부사항2| author2|
|003| 제목3| 세부사항3| author1|
|004| 제목4| 세부사항4| NULL|

[AUTHOR]  
|aid |name |city |profile_id|
|----|-----|-----|-------|
|author1|이름1|서울|프로필1|
|author2|이름2|제주|프로필2|
|author3|이름3|부산|프로필3|

[PROFILE]
|pid| title |description|
|---|--------|-----------|
|프로필1| 개발자| developer is …|
|프로필2| 디자이너| designer is … |
|프로필3| DBA| DBA is ..|

## 🍎LEFT OUTER JOIN

- 합집합
- 왼쪽 기준으로 테이블 합치기.
- 왼쪽 테이블 값은 모두 select.
- join하는 기준값이 오른쪽 테이블에 없으면 NULL로 표시.

```SQL
SELECT *
FROM TOPIC
LEFT JOIN AUTHOR
ON TOPIC.AUTHOR_ID = AUTHOR.AID;
```

| tid | title | description | author_id | aid     | name  | city | profile_id |
| --- | ----- | ----------- | --------- | ------- | ----- | ---- | ---------- |
| 001 | 제목1 | 세부사항1   | author1   | author1 | 이름1 | 서울 | 프로필1    |
| 002 | 제목2 | 세부사항2   | author2   | author2 | 이름2 | 제주 | 프로필2    |
| 003 | 제목3 | 세부사항3   | author1   | author1 | 이름1 | 서울 | 프로필1    |
| 004 | 제목4 | 세부사항4   | NULL      | NULL    | NULL  | NULL | NULL       |

## 🍎INNER JOIN

- 교집합
- 조인하는 테이블에 없는 값은 지워짐.

```SQL
SELECT *
FROM TOPIC
INNER JOIN AUTHOR
ON TOPIC.AUTHOR_ID = AUTHOR.AID;
```

| tid | title | description | author_id | aid     | name  | city | profile_id |
| --- | ----- | ----------- | --------- | ------- | ----- | ---- | ---------- |
| 001 | 제목1 | 세부사항1   | author1   | author1 | 이름1 | 서울 | 프로필1    |
| 002 | 제목2 | 세부사항2   | author2   | author2 | 이름2 | 제주 | 프로필2    |
| 003 | 제목3 | 세부사항3   | author1   | author1 | 이름1 | 서울 | 프로필1    |

## 🍎FULL OUTER JOIN

- 합집합인데...

```SQL
SELECT *
FROM TOPIC
FULL OUTER JOIN AUTHOR
ON TOPIC.AUTHOR_ID = AUTHOR.AID;
```

다음과 같음

```SQL
(
    SELECT *
    FROM TOPIC
    LEFT JOIN AUTHOR
    ON TOPIC.AUTHOR_ID = AUTHOR.AID
)
UNION
(
    SELECT *
    FROM TOPIC
    RIGHT JOIN AUTHOR
    ON TOPIC.AUTHOR_ID = AUTHOR.AID
);

```

| tid  | title | description | author_id | aid     | name  | city | profile_id |
| ---- | ----- | ----------- | --------- | ------- | ----- | ---- | ---------- |
| 001  | 제목1 | 세부사항1   | author1   | author1 | 이름1 | 서울 | 프로필1    |
| 002  | 제목2 | 세부사항2   | author2   | author2 | 이름2 | 제주 | 프로필2    |
| 003  | 제목3 | 세부사항3   | author1   | author1 | 이름1 | 서울 | 프로필1    |
| 004  | 제목4 | 세부사항4   | NULL      | NULL    | NULL  | NULL | NULL       |
| NULL | NULL  | NULL        | NULL      | author3 | 이름3 | 부산 | 프로필3    |

- null인지는 확실하지 않음! ㅎㅎ^^

## 🍎 EXCLUSIVE LEFT JOIN

- 여집합

```SQL
SELECT *
FROM TOPIC
LEFT JOIN AUTHOR
ON TOPIC.AUTHOR_ID = AUTHOR.AID
WHERE AUTHOR.AID IS NULL;
```

| tid | title | description | author_id | aid  | name | city | profile_id |
| --- | ----- | ----------- | --------- | ---- | ---- | ---- | ---------- |
| 004 | 제목4 | 세부사항4   | NULL      | NULL | NULL | NULL | NULL       |
