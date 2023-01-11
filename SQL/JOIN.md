topic
|tid |title |description |author_id|
|----|-----|-----------|----------|
|1| HTML| HTML is …| 1|
|2| CSS| CSS is …| 2|
|3| Database| Database is ..| 1|
|4| Oracle| Oracle is …| NULL|

author
|aid |name |city |profile_id|
|----|-----|-----|-------|
|1 |egoing |seoul |1|
|2| leezche| jeju| 2|
|3| blackdew |namhae| 3|

profile
|pid| title |description|
|---|--------|-----------|
|1| developer| developer is …|
|2| designer| designer is … |
|3| DBA| DBA is ..|

# ⭐LEFT OUTER JOIN

:A...left 기준으로 테이블 합치기. 없는 값은 NULL

### Q. topic 테이블을 기준으로 author테이블 left join하기

SELECT \* FROM topic <span style='color:red'>LEFT JOIN author ON topic.author_id=author.aid</span>;  
![left](https://user-images.githubusercontent.com/96059261/211751490-5a238ab1-0dc6-44b3-bc0c-2d2213043b26.png)

# ⭐INNER JOIN

:A와 B 교집합..없는 값 지워짐.

SELECT \* FROM topic <span style='color:red'>INNER JOIN author ON topic.author_id=author.aid</span>;

# FULL OUTER JOIN

: A와 B의 합집합... 중복된 값은 제거.

SELECT \* FROM topic FULL OUTER JOIN author ON topic.author_id=author.aid

(SELECT _ FROM topic LEFT JOIN author ON topic.author_id=author.aid) UNION (SELECT _ FROM topic RIGHT JOIN author ON topic.author_id=author.aid)

# EXCLUSIVE LEFT JOIN

: A-B...A에서 B 삭제

SELECT \* FROM topic LEFT JOIN author ON topic.author_id=author_aid WHERE author.aid is NULL
