## 🍎 Mybatis란?

- 스프링과 SQL을 매핑하는 프레임워크이다. 단순하고 쉽다는 특징을 가지고 있다.
- MyBatis를 사용하면 자바 코드로부터 SQL문을 분리해서 관리할 수 있다.
- 매개변수 설정과 쿼리 결과를 읽어오는 코드를 제거할 수 있다.

- 기존에는 다음과 같이 자바 파일에서 SQL문을 관리했다. MyBatis를 사용하면 SQL문을 xml파일로 자바 파일과 분리하여 관리할 수 있다.

## 🍎 1. 필요한 라이브러리

- mybatis
- mybatis-spring

## 🍎 2. SqlSessionFactoryBean을 빈으로 등록하기

- root-context.xml에 추가하기

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

- 또는 애노테이션으로 등록하기

```java
@Configuration
public class DataBaseConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AES256Util aes256;

    @Bean(name = "myDataSource")
    public DataSource getDataSource(@Qualifier("shinsegaePointReadWriteHikariConfig")HikariConfig hikariConfig) {
        hikariConfig.setJdbcUrl(aes256.aesDecode(hikariConfig.getJdbcUrl()));
        hikariConfig.setUsername(aes256.aesDecode(hikariConfig.getUsername()));
        hikariConfig.setPassword(aes256.aesDecode(hikariConfig.getPassword()));

        return new HikariDataSource(hikariConfig);

    }

    @Bean(name = "sessionFactory")
    public SqlSessionFactory getSessionFactory(final DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();

        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(applicationContext.getResource("classpath:mapper/*Mapper.xml"));

        sqlSessionFactory.setTypeAliasesPackage("com.mycloudmembership.extension.shinsegaepoint.model.**");
        sqlSessionFactory.setTypeHandlersPackage("com.mycloudmembership.extension.shinsegaepoint.model.**");

        Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml");
        sqlSessionFactory.setConfigLocation(myBatisConfig);

        return sqlSessionFactory.getObject();

    }
    @Bean(name = "shinsegaePointReadWriteSessionTemplate")
    public SqlSessionTemplate getSessionTemplate(@Qualifier("shinsegaePointReadWriteSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
```

### SqlSessionFacotryBean 이란?

- FactoryBean 인터페이스의 구현체로 SqlSessionFactory를 스프링에서 사용하기 위해 등록해야 하는 빈이다.
- SQL 명령을 수행하는데 필요한 메서드를 제공하는 클래스인 SqlSession을 생성해서 제공하는 일을 한다.

### SqlSessionFacotryBean 속성

- datasource : 반드시 주입해야한다.
- configLocation : mybatis-config.xml 설정파일 위치를 지정한다.
- mapperLocation : 매퍼 xml 파일의 위치를 지정한다.
### SqlSessionFactory
- 한 번 만든 뒤 SqlSessionFactory는 애플리케이션을 실행하는 동안 존재해야 한다. 싱글톤인 빈으로 등록해서 사용하면 된다.
### SqlSession
- 각각의 쓰레드는 각각의 SqlSession 인스턴스를 가져야 한다. 
#### SqlSession 메서드

| 메서드                                                       | 설명                                                           |
| ------------------------------------------------------------ | -------------------------------------------------------------- |
| int insert(String, Object parameter)                         | insert문 실행, insert된 행의 갯수를 반환                       |
| int delete(String s,Object parameter)                        | delete 문 실행 후, 행의 갯수 반환                              |
| int update(String s,Object parameter)                        | update문 실행 후, 행의 갯수 반환                               |
| T selectOne(String s,Object parameter)                       | 하나의 행 반환하는 select                                      |
| List<E> selectList(String s, Object parameter)               | 여러 행 반환하는 select                                        |
| Map<K,V> selectMap(String s, String keyCol,Object parameter) | 여러 행 반환하는 select, keyCol에 Map의 key로 사용할 컬럼 지정 |

### SqlSessionTemplate

- SQL명령을 수행하는데 필요한 메서드 제공한다.
- 여러 Dao를 동시에 실행해도 안전하도록 SqlSessionTemplate이 관리해준다. thread-safe하다.
