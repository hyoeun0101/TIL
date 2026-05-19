# 🔴 Log-BO

## 🟡 기존 테이블 not null
- 상황
  - 기존 제휴멤버십 포인트 카드 저장해두는 테이블에서 포인트 카드번호가 not null이다.
  - 여기서 제휴사 빗썸을 추가해야 하는데 빗썸은 포인트 카드번호가 존재하지 않는다.
  - 따라서 단순히 포인트카드번호 not null 때문에 기존 테이블을 사용하지 못하는 상황이다.
  - not null을 하지 않고, java 코드로 유효성 체크해서 넣었다면 null을 방지하고, 기존 테이블을 쓸 수 있었을 것이다.

- **not null 속성을 넣을 때 확장성을 생각하고 넣자.**
- 결국 해당 테이블 안쓰고 동의 테이블 관리 사용함.

## 🟡 @JsonAnyGetter, @JsonAnySetter
- @JsonAnyGetter : Map 필드를 Json으로 Serialization 해줌.

<br>

---

## 🟡 FilterChain

<br>

---

## 🟡 배치를 java로 돌린다
- 배치를 돌리는 방법이  2가지가 있다. shell로 돌리기, java로 돌리기
1. shell로 돌리기
   - shell script를 써서 돌림. 이런 경우 보통 로직이 프로시저로 되어 있음.


2. java로 돌리기
   - 내가 아는 배치 돌리기. java 코드로 돌림.

<br>

---

## 🟡 카테시안 곱 = cross join

```sql
-- A_TABLE x B_TABLE
SELECT *
FROM A_TABLE, B_TALBE
```
- A_TABLE x B_TABLE
  - 두 테이블의 모든 행을 곱한 결과. 이를 **CROSS JOIN**이라고 한다.
  - A_TABLE의 튜플 개수 : 10, B_TABLE의 튜플 개수 : 5 => 결과는 50개 행 반환.

```sql
-- INNER JOIN
SELECT *
FROM A_TABLE, B_TABLE
WHERE A_TABLE.CODE = B_TABLE.CD;
```
- 두 테이블의 컬럼 비교를 걸어주면 INNER JOIN이 된다.


<br>

---

## 🟡 ssgpnt_coreapi 주요 로직

**RequestValidateInterceptor**

```java
public class RequestValidateInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 요청 로그
        // clientId, apiUrl 검증.
        // 토큰 체크
        return true;
    }
}

```

**ApiServiceLoadingUtil**

```java
import java.util.HashMap;

public class ApiServiceLoadingUtil implements InitializingBean {
    private static Map<String, Object> _map = new HashMap<>();
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // _map 세팅하는 로직
        // clientId, apiUrl, columns를 조회해서 _map에 저장함.
    }
}

```
- 처음 컴파일 실행할 때 afterPropertiesSet이 실행됨.

<br>

---

## 🟡 Spring에서 String을 Json으로 변환
- ObjectMapper 이용하기.
```java

String jsonString = "{\"name\":\"홍길동\",\"age\":25}";

JsonNode jsonNode = objectMapper.readTree(jsonString);
jsonNode.get("name").asText(); // 홍길동

Person person = objectMapper.readValue(jsonString, Person.class);
person.getName(); // 홍길동

```

<br>

---

## 🟡 OAuth2.0 그리고 Authorization Basic, Bearer


<br>

---

## 🟡 Spring- Mockito 테스트코드 작성할 때 @Value 주입하기

<br>

---
## 🟡 Spring에서 UTF-8을 US7ASCII로 변환

- 문제 상황 : Spring에선 UTF-8이고 DB의 charaterset은 US7ASCII여서 한글값을 DB에 insert할 때 한글 깨짐 문제가 발생하였다.
- 해결: Mybatis에 TypeHandler를 적용함.

```sql
<insert id="insertUser" parameterType="map">
    INSERT INTO USER_TABLE (
        USER_ID,
        USER_NAME,
        REG_DT
    ) VALUES (
        #{userId},
        #{userName,
          javaType=String,
          typeHandler=com.ssgpnt.handler.OracleUsStringTypeHandler},
        SYSDATE
    )
</insert>

```
```java
// MyBatisConfig 등록할 때 TypeHandler 지정 필요
@Slf4j
@MappedTypes(String.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class OracleUsStringTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, encodeUS7ASCII(parameter));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (rs.getObject(columnName) instanceof String) {
            return (null == decodeUS7ASCII(rs.getBytes(columnName))
                    || decodeUS7ASCII(rs.getBytes(columnName)).length() == 0) ? null
                            : decodeUS7ASCII(rs.getBytes(columnName));
        } else {
            return rs.getString(columnName);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (rs.getObject(columnIndex) instanceof String) {
            return decodeUS7ASCII(rs.getBytes(columnIndex));
        } else {
            return rs.getString(columnIndex);
        }
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (cs.getObject(columnIndex) instanceof String) {
            return decodeUS7ASCII(cs.getBytes(columnIndex));
        } else {
            return cs.getString(columnIndex);
        }
    }

    public String decodeUS7ASCII(byte[] value) {
        if (value == null) {
            return "";
        }
        String str = "";
        try {
            str = new String(value, "KSC5601");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return str;
    }

    public String encodeUS7ASCII(String value) {
        if (value == null) {
            return "";
        }
        String str = "";
        try {
            str = new String(value.getBytes("KSC5601"), "8859_1");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return str;
    }

}
```


<br>

---
## 🟡 @Retryable VS WebClient의 retry()

### 1. Retryer 사용법

1. 의존성 추가 필요
```gradle
implementation 'org.springframework.retry:spring-retry'
implementation 'org.springframework.boot:spring-boot-starter-aop'
```
2. 빈 등록 필요
```java
@Configuration
@EnableRetry
public class RetryConfig{
    
}
```
3. 사용할 Service 정의
```java
@Service
public class ExternalApiService{
    @Retryable(
            value = { RuntimeException.class }, //RuntimeException 발생하면 재시도
            maxAttemps = 3, // 실패 시 최대 3회 시도
            backoff = @Backoff(delay=1000) // 1초 간격으로 재시도
    )
    public String callExternalApi() {
        // TODO: 외부 API 호출
        throw new RuntimeException("API 실패");
    }
}

```

4. 예외별 제어

```java
import java.net.SocketTimeoutException;

@Retryable(
        include = { SocketTimeoutException.class },
        exclude = { IllegalArgumentException.class },
        maxAttempts = 5,
        backoff= @Backoff(delay = 500, multiplier = 2)
)

```

### 2. Retry와 WebClient의 retry() 차이점

- Retry는 보통 RestTemplate과 같이 사용한다.



## 🟡 운영에서 new RestTemplate()을 하면 안되는 이유(=빈 등록 후 사용해야 하는 이유)


## 🟡 WebClient 통신 오류 로그 쌓기 (Http status 4XX, 5XX, 네트워크 오류)
### 4XX : 클라이언트 문제
- 클라이언트가 잘못된 요청을 한 경우: 데이터 검증 실패, JSON 요청 형식 오류 등
- 인증 에러 : 401 Unauthorized
- 권한 없음: 403 Forbidden
- 존재하지 않는 리소스 : 404 Not Found
- 너무 많은 요청: 429 Too Many Requests

### 5XX: 서버, 일시 장애 문제
- 서버 내부 예외
- DB 장애, 의존 서비스 장애 등
- 게이트웨이/로드밸런서 문제: 502 Bad Gateway, 503 Service Unavailable, 504 Gateway Timeout

### 네트워크 오류: 서버 통신 자체가 안됨
- DNS 실패(호스트 못찾음)
- Connection refused(포트 열려있지 않음)
- Connect timeout / Read timeout
- TLS handshake 실패
- 네트워크 단절

## 🟡 WebClient 'recvAddress(..) failed: Connection reset by peer' 해결하기
- 문제 원인 : WebClient가 다른 서버와 통신하기 위해 열어둔 Connection이 close됐는데 해당 Connection을 다시 사용해서 발생함. 

<내 코드>

```java
import java.awt.*;
import java.beans.BeanProperty;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    @Bean
    @Primary
    WebClient webClient() {
        HttpClient httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2_000)
                .responseTimeout(Duration.ofMillis(2_000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(2_000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(2_000, TimeUnit.MILLISECONDS))
                );

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}

```


# 🔴 Log - FO

---




## 🔴 Review

---

## 🔴 Refactoring



