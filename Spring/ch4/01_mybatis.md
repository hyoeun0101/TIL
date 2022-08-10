# MyBatis
: SQL Mapping Framework - Easy & Simple    
자바 코드로부터 SQL문을 분리해서 관리   
매개변수 설정과 쿼리 결과를 읽어오는 코드를 제거   


```java
String sql = "sql문";

Connection conn = ds.getConnection();
PreareStatement p = conn.prepareStatement(sql);

```
이렇게 자바코드 안에 sql문이 있는데 MyBatis를 쓰면 나눌 수 있음. 

이렇게1   
sql문 따로 xml에 저장.
```xml
<mapper namespace="com.fastcampus.ch4.dao.UserMapper">
    <insert id="insert" parameterType="com.fastcampus.ch4.domain.UserDto">
        INSERT INTO user_info VALUES (#{id}, #{pwd},#{name},#{email},#{birth},#{sns},now());
    </insert>
```
이렇게2
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

maven repo - mybatis

#### 2. SqlSessionFactoryBean
: SqlSession을 생성해서 제공    
- SqlSession : SQL 명령을 수행하는데 필요한 메서드 제공

- SqlSesionFactoryBean : SqlSessionFactory를 Spring에서 사용하기 위한 빈   
- SqlSessionTemplate : SQL명령을 수행하는데 필요한 메서드 제공. thread-safe   
여러 Dao를 동시에 실행해도 안전함. 그걸 SqlSessionTemplate가 관리해줌.   

[root-context.xml]에 추가
```xml

	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource"/>
        <!-- mybatis 설정 파일 지정 -->
		<property name="configLocation"  value="classpath:mybatis-config.xml"/>
        <!-- sql문 들어있는 xml파일인 Mapper 위치 지정하기 -->
		<property name="mapperLocations" value="classpath:mapper/*Mapper.xml"/>
	</bean>

	<bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
		<constructor-arg ref="sqlSessionFactory"/>
	</bean>

```

#### 3. SqlSession 메서드
|메서드|설명|
|---|----|
|int insert(String, Object parameter)|insert문 실행, insert된 행의 갯수를 반환|
|int delete(String s,Object parameter)|delete 문 실행 후, 행의 갯수 반환|
|int update(String s,Object parameter)|update문 실행 후, 행의 갯수 반환|
|T selectOne(String s,Object parameter)|하나의 행 반환하는 select|
|List<E> selectList(String s, Object parameter)|여러 행 반환하는 select|
|Map<K,V> selectMap(String s, String key,Object parameter)||

### <typeAliases> 으로 이름 짧게 하기
```xml
<typeAliases>
    <typeAlias alias="BoardDto" type="com.fastcampus.domain.BoardDto"/>
</typeAliases>
```
별명은 대소문자 구별 안함!!!!    
