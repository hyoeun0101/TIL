## 🔴 Mybatis란?

- 스프링과 SQL을 매핑하는 프레임워크이다. 단순하고 쉽다는 특징을 가지고 있다.
- MyBatis를 사용하면 자바 코드로부터 SQL문을 분리해서 관리할 수 있다.
- 매개변수 설정과 쿼리 결과를 읽어오는 코드를 제거할 수 있다.

- 기존에는 다음과 같이 자바 파일에서 SQL문을 관리했다. MyBatis를 사용하면 SQL문을 xml파일로 자바 파일과 분리하여 관리할 수 있다.

## 🔴 Spring Boot에서 Mybatis 설정
### 1. 의존성 설치

```java
dependencies {
    implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:2.2.0'
}
```

### 2. Mybatis 설정
- resources > `mybatis-config.xml` 생성

- sqlSession 설정
    - sqlSessionFactory 빈 등록 : mapper location 설정, mybatis-config.xml 위치 설정
    - sqlSessionTemplate 빈 등록 

### 3. mybatis-config.xml
- mybatis 관련 설정 해줌. `application.yml`로 설정할 수도 있음.



## 🔴 SqlSessionFacotryBean 이란?

- SQL 명령을 수행하는데 필요한 메서드를 제공하는 클래스인 SqlSession을 생성해서 제공하는 일을 한다.

- 각 쓰레드(요청)은 하나의 sqlSession을 가짐.

### SqlSession 메서드

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

## 🔴 mybatis-config.xml
- resources 바로 아래 위치.


### 카멜케이스 변환
