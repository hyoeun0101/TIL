# 검색 기능 구현하기

옵션 : 제목+내용

```html
<form action="ch4/board/list" class="search-form" method="get">
  <select class="select-option" name="option">
    <option value="A" selected>제목+내용</option>
    <option value="T">제목만</option>
    <option value="W">작성자</option>
  </select>

  <input type="text" name="keyword" class="search-input" value="" />
  <input type="submit" class="search-button" value="검색" />
</form>
```

### sql 문

`SELECT * FROM board WHERE true AND title LIKE concat('title','%')`;  
board 테이블의 title 속성값이 title% 인 것 select해라.  
반대는 not like

`SELECT * FROM board WHERE true AND bno IN (11,12,13) ORDER BY bno;`

반대는 not in

## MyBatis의 동적 쿼리

### 공통 부분을 <sql>로 정의하고 <include>로 포함시켜 사용하기

```xml
<sql id="selectFromBoard">
    SELECT bno, title, content, writer, view_cnt, comment_cnt, reg_date
    FROM board
</sql>

<select id="select" parameterType="int" resultType="BoardDto">
    <include refid="selectFromBoard"/>
    WHERE bno = #{bno}
</select>
```

### <if>

자바의 if문과 동일. - 여기선 if문보다 choose, when이 더 적합.

```xml
<select id="searchResultCnt" parameterType="SearchCondition" resultType="int">
    SELECT count(*)
    FROM board
    WHERE true
    <if test='option=="A"'>
    AND (title LIKE concat('%', #{keyword}, '%') OR content LIKE ('%', #{keyword}, '%'))
    </if>

    <if test='option=="T"'>
    AND (title LIKE concat('%', #{keyword}, '%')
    </if>

    <if test='option=="W"'>
    AND (writer LIKE concat('%', #{keyword}, '%')
    </if>
</select>
```

%keyword% : 키워드를 포함하고 있는 것.

### <choose> <when>

if-elseif와 비슷

```xml
<select id="searchResultCnt" parameterType="SearchCondition" resultType="int">
    SELECT count(*)
    FROM board
    WHERE true
    <choose>
        <when test='option=="T"'>
            AND (title LIKE concat('%', #{keyword}, '%')
        </when>

        <when test='option=="W"'>
            AND (writer LIKE concat('%', #{keyword}, '%')
        </when>

        <otherwise>
            AND (title LIKE concat('%', #{keyword}, '%') OR content LIKE ('%', #{keyword}, '%'))
        </otherwise>
    </choose>
</select>
```

% : 0~n글자
\_: 1글자

### foreach

WHERE bno IN (1,2,3)  
(1,2,3)을 동적으로 작성하기.

```xml
<select id="getSeleted" resultType="BoardDto">
    SELECT bno, title, content, writer, view_cnt, comment_cnt, reg_date
    FROM board
    WEHRE bno IN
    <foreach collection="array" item="bno" open="(" close=")" separator=",">
</select>
```

array는 매개변수로 받음. bnoArr가 array로 들어가는 것.

```java
sesson.selectList(namespace+ "getSeleted", bnoArr);
```
