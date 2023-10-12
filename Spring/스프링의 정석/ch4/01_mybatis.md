## 🍎 MyBatis

- 스프링과 SQL을 매핑하는 프레임워크이다. 단순하고 쉽다는 특징을 가지고 있다.
- MyBatis를 사용하면 자바 코드로부터 SQL문을 분리해서 관리할 수 있다.
- 매개변수 설정과 쿼리 결과를 읽어오는 코드를 제거할 수 있다.

- 기존에는 다음과 같이 사용했다.

```java
String sql = "sql문";

Connection conn = ds.getConnection();
PreareStatement p = conn.prepareStatement(sql);

```

- MyBatis를 사용하면 SQL문만 xml파일로 따로 관리할 수 있다.

[main/resources/mapper/UserMapper.xml]

```xml
<mapper namespace="com.fastcampus.ch4.dao.UserMapper">
    <insert id="insert" parameterType="com.fastcampus.ch4.domain.UserDto">
        INSERT INTO user_info VALUES (#{id}, #{pwd},#{name},#{email},#{birth},#{sns},now());
    </insert>
```

[main/java/com/example/dao/UserDao.java]

```java
@Repository
public class UserDaoImpl implements UserDao{
    @Autowired
    private SqlSession session;
    private static String namespace="com.fastcampus.ch4.dao.UserMapper";

    @Override
    public int insert(User user){
        return session.insert(namespace+"insert",user);
    }
}
```

#### SqlSession 메서드

| 메서드                                                       | 설명                                                           |
| ------------------------------------------------------------ | -------------------------------------------------------------- |
| int insert(String, Object parameter)                         | insert문 실행, insert된 행의 갯수를 반환                       |
| int delete(String s,Object parameter)                        | delete 문 실행 후, 행의 갯수 반환                              |
| int update(String s,Object parameter)                        | update문 실행 후, 행의 갯수 반환                               |
| T selectOne(String s,Object parameter)                       | 하나의 행 반환하는 select                                      |
| List<E> selectList(String s, Object parameter)               | 여러 행 반환하는 select                                        |
| Map<K,V> selectMap(String s, String keyCol,Object parameter) | 여러 행 반환하는 select, keyCol에 Map의 key로 사용할 컬럼 지정 |

### Mapper XML 작성하기

[resources/mapper/boardMapper.xml]

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">


<!-- 여기 namespace는 BoardDaoImpl에 있는 namespace와 같아야함. -->
<mapper namespace="com.fastcampus.ch4.dao.BoardMapper">
    <select id="count" resultType="int">
        SELECT count(*) FROM board
    </select>

    <delete id="delete" parameterType="map">
        DELETE FROM board WHERE bno = #{bno} and writer = #{writer}
    </delete>
    <!-- 여기 BoardDto에서 getTitle 불러서 #{titlle}에 넣어주는 것. 그래서 getter 꼭 필요!!! -->
    <insert id="insert" parameterType="com.fastcampus.ch4.domain.BoardDto">
        INSERT INTO board
            (title, content, writer)
        VALUES
            (#{title}, #{content}, #{writer})
    </insert>
    <!-- 여기 parameterType의 int는 사실 Integer임! mybatis가 java.lang.Integer의 별명을 int로 준 것!! -->
    <select id="select" parameterType="int" resultType="BoardDto">
        <include refid="selectFromBoard"/>
        WHERE bno = #{bno}
    </select>
</mapper>
```

### typeAliases으로 이름 짧게 하기

[resources/mybatis-config.xml]에 추가

```xml
<typeAliases>
    <typeAlias alias="BoardDto" type="com.fastcampus.domain.BoardDto"/>
</typeAliases>
```

별명(alias)은 대소문자 구별 안함!!!!
