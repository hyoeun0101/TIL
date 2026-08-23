## 🔴 요구사항
```
1. api server
2. db는 ORACLE 4개 - US7ASC2 or EUC-KR
3. 에러, api result 결과가 동일한 포맷
4. open doc을 이용한 swagger 설정
```
- spring-boot 2.6
- JDK1.8
- mybatis- typehandler 사용
- mapper 인터페이스 사용
- mapper repository 사용
- sql injection 직접 사용

<br>

## 🔴 프로젝트 생성
- 문제 : Spring Initializer는 Spring Boot 3버전, JDK 17 이상만 가능..

- 해결
    - Spring boot 2.6 lastest version 검색. 

    - JDK 1.8 지원하는지 확인

    1. 일단 Spring Initializer로 프로젝트 생성. (버전 신경 X)

    2. build.gradle 파일에서 Spring Boot 버전을 변경한다. -> External Libraries에서 버전 바뀌었는지 확인

    3. Project Structure >  SDKs > jdk 추가

    4. Project Structure > Project - SDK 버전 변경

    5. Settings > Gradle - Gradle JVM 버전 확인


### 🟡 의존성 추가
- Spring  Web
- PostgreSQL Driver
- Oracle Driver(일단)
- Lombok
- mybatis-spring-boot-starter 2.2버전 사용

### 🟡 Swagger
- 설정하기 위한 라이브러리 2개 존재
    - SpringFox, SpringDoc
    - SpringFox는 더이상 업데이트 X. 따라서 SpringDoc 사용
    
```gradle
dependencies {
    // Spring Boot 2.x는 1.8.0 가능
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:1.8.0'
}
```
- 문제 : swagger 의존성 추가하는데 안된다...
```
#### 에러코드 ####

Execution failed for task ':compileJava'.
> Could not resolve all files for configuration ':compileClasspath'.
   > Could not find org.springdoc:springdoc-openapi-starter-webmvc-ui:1.8.0.
     Required by:
         root project :

```
- 해결
    - https://springdoc.org/v1/ -> 1.8.0 버전 문서를 보았다.
    - 문서를 제대로 읽어보자!
    - 읽어보니 Spring-webmvc에선 다음 의존성이 추가로 필요했다.
```xml
  <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-webmvc-core</artifactId>
      <version>1.8.0</version>
   </dependency>
```
```java
dependencies {
    // Spring Boot 2.x는 1.8.0 가능
    implementation 'org.springdoc:springdoc-openapi-webmvc-core:1.8.0'
}
```

## 🔴 다중 데이터베이스 설정
- 고려할 사항 
    - 다중 데이터베이스에 대한 트랜잭션 처리.
    - 즉 TransactionManager가 여러 개일 때 트랜잭션 처리는 어떻게?




### 🟡 Connection Pool?
- DB연결 요청(=하나의 Http 요청)이 들어올 때마다 Connection을 생성해야 하는데 Connection은 유지하는 것보다 생성하는데 자원이 많이 소모된다.

- 따라서 Connection Pool에 미리 일정 수 만큼의 Connection을 만들어놓고 꺼내쓰는 방식을 사용한다.

### 🟡 HikariCP?
- 데이터베이스 Connection을 관리해주는 라이브러리.

- 기존 SpringBoot에선 tomcat-jdbc를 기본 Datasource로 제공

- Spring Boot 2.0 부터 HikariCP가 기본 JDBC Connection Pool로 변경됨.

- `spring-boot-starter-jdbc`에 자동으로 포함. 없으면 다음 hikariCP 의존성 추가 필요

- `mybatis-spring-boot-starter` 안에 jdbc-starter 포함.
    
```java
dependencies {
    // hikariCP 들어있음.
	implementation 'org.springframework.boot:spring-boot-starter-data-jdbc'
}

```
```xml
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <!-- <version>5.1.0</version> -->
</dependency>
```

- HikariCP의 Database url 설정은 `url`이 아닌 `jdbc-url`을 사용한다. 그러니 `application.yml`에서 설정할 때 `url`이 아닌 `jdbc-url`로 설정해야함!

### 🟡 Mybatis 설정
- 의존성 추가
```java
    implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:2.3.1'

```
- DB 당 각각의 SqlSessionFactory 필요한가? 같은 sqlSession을 써도 되지 않나?

dao 당 sqlSession 임.



sqlSessionFactory는 SqlSession을 관리하는 얘. 


sqlSession - DB
           - DB


<br><br><br><br><br>


## 🔴 로깅
- https://docs.spring.io/spring-boot/reference/features/logging.html

- 의존성 : `spring-jcl` 필요. 이는 `spring-boot-starter-web`에 들어있음.

### 설정법
1. application.yml에 설정
2. 커스텀을 원하면 resources에 logback-spring.xml 추가하여 설정

## 🔴 DTO와 VO?
### 🟡 DTO (data transfer object)
- 계층 간 **데이터를 전송하기 위해** 사용. controller -> service로 이동할 때 사용
- 데이터 캡슐화.
- 데이터의 변환 및 전달이 주된 역할.
- 특징
    - **가변. 필드 값 변할 수 있음.** 즉 setter 존재.
    - **데이터 전달용**으로만 사용. 비즈니스 로직 포함하지 않음.
    - 데이터베이스 모델(Entity)와 일치하지 않아도 됨. 필요한 데이터만 담아서 사용 가능.
    - 직렬화 가능해야함.

### VO(value object)
- 특정 값을 표현하기 위해 사용
- 그저 값을 담기 위해 존재. **불변성!!**
- 특징
    - **불변. setter없고, 생성자로 값 설정**
    - 객체의 동일성은 값 비교로 판단. equals()와 hashCode()를 재정의 해야함.
    - 값만 표현하므로 비즈니스 로직을 포함하지 않음.

### 정리
|특성|DTO|VO|
|---|---|----|
|목적|데이터 전송|데이터 표현|
|가변성|mutable|immutable|
|주요 사용 위치|Controller, Service, API 간 전송|도메인 계층, 모델 내부|
|비즈니스 로직|포함 X|포함 X|
|동일성 판단 기준|객체의 참조 비교|객체의 값 비교|


- VO를 꼭 사용해야 할까?
    - vo는 데이터 안전성을 가지지만 객체를 새로 생성해야 한다. 따라서 간단한 애플리케이션에선 DTO가 더 적합할 수 있다.
    - 하지만 복잡한 도메인 모델이라면 vo 사용을 고려할 필요가 있다.
    - vo는 불변의 특징을 가지고 있어서 데이터 의미를 명확히 한다. 따라서 데이터 무결성이 중요한 도메인에선 유리하다. 데이터의 의미를 강조하는 도메인 주도 설계(DDD)에서 주로 사용된다.

header 값은 Vo로 표현하면 되지 않을까???????
하지만 암호화, 복호화는???
??? 뭐야. mapper 어디서 빈으로 등록하냐???




## 🔴 Memo

```sql
-- 테이블만 생성
create table b1 select * from a1 where false;

-- 테이블 생성 & 데이터도 복사
create table b1 select * from a1 where false;

```

- 다중 데이터베이스 설정 시 각 Datasource와 매퍼를 연결해주어야 함.

