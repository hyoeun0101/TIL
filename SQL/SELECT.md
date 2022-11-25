## WEHRE 조건
- `WHERE num BETWEEN 1 AND 10` : 1 부터 10사이
- `WHERE addr IN ('서울','경기')` :주소가 서울, 경기에 속하다
- `WHERE mem_name LIKE '우%'` : 문자 '우'로 시작, 우주소녀 
- `WHERE mem_name LIKE '__핑크'` : 블랙핑크, 에이핑크

## ORDER BY
정렬    
- 

SELECT DISTINCT : 중복 제거

# GROUP BY
집계 함수
|함수명|설명|
|-----|----|
|SUM()|합계|
|AVG()|평균|
|COUNT(DISTINCT)|행의 개수(중복제거)|

그룹 바이에서 묶은 그룹에 대한 조건을 줄 때 HAVING