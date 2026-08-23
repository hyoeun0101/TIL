- Spring 프로젝트 내에서 API 통신을 해야할 때 RestTemplate를 사용한다.

- `2025.12.19` Spring 6.1부터는 동기식 HTTP 통신을 위한 RestClient를 제공함. 비동기 및 스트리밍 시나리오의 경우 반응형 WebClient도 고려해보자.

- Spring에서 신규 개발은 WebClient를 권장함.

## 🔴 RestTemplat VS WebClient
- Spring에서 신규 개발은 WebClient를 권장한다.

- RestTemplate은 Blocking. 요청 하나 당 스레드 하나가 할당됨. 요청하고 응답이 올 때까지 스레드를 점유함.

- WebClient는 Non-Blocking. 소수의 스레드로 다수 요청을 처리함. 요청을 하면 스레드를 반환하고 응답이 오면 이벤트로 처리함. 즉 대량 트래픽에 적절함.

- 결론 : 레거시 유지보수는 RestTemplate으로 하되, 신규 개발, 트래픽 큼, 외부 API 많은 경우는 WebClient를 권장한다.

## 🔴 RestTemplat?
- org.springframework.web.client 패키지 내의 클래스.

- Http 메서드(GET, POST, PUT 등)을 지원
- API 호출 후 응답받을 때까지 기대라는 **동기식**.
- **Spring 5.0 이후부터 Deprecated** 됨.
  - Spring 5.0부턴 동기/비동기 방식을 둘 다 지원함.


## 🔴 RestTemplate 사용하기
- spring-webmvc 의존성에 포함되어있음.

### 🟡 빈 등록하기

**빈으로 등록해서 사용해야 하는 이유는?**
1. 기본 생성자를 통해 사용할 경우 타임아웃 설정이 없다. connect timeout, read timeout 설정 안됨.

  - 즉 외부 API가 응답을 안하면 **스레드는 무한 대기**를 한다.
2. 공통 설정이 깨진다.
  - Authorization 헤더, User-Agent, 로깅, Retry, 장애 대응과 같은 공통 설정을 한 RestTemplate 객체를 빈으로 등록하여 사용하는 것이 유리함.

```java
@Bean
public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
    .additionalInterceptors(new LoggingInterceptor())
    .build();
}
```


3. 테스트/Mocking 용이
4. RestTemplate은 Thread-safe하다. 내부상태없고, 재사용해도 안전하므로 싱글톤 Bean으로 두는 게 맞는 설계이다.


```java
package com.osc.config;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.osc.core.filter.OscClientHttpRequestInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RestTemplateConfig {

    @Autowired
    private OscClientHttpRequestInterceptor oscClientHttpRequestInterceptor;

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        log.debug(">>> OSC restTemplate init");
        RestTemplate restTemplate = new RestTemplate(bufferingClientHttpRequestFactory());
        restTemplate.getInterceptors().add(oscClientHttpRequestInterceptor);
        return restTemplate;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        log.debug(">>> OSC clientHttpRequestFactory init");

        TrustManager[ ] certs = new TrustManager[ ] {
            new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
            }
        };

        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, certs, new SecureRandom());
        } catch (java.security.GeneralSecurityException ignored) {
        }

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(600) // connection pool 적용
                .setMaxConnPerRoute(120) // connection pool 적용
                .setSSLHostnameVerifier((hostname, session) -> true)
                .setSSLContext(ctx)
                .build();

        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }

    @Bean
    public BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory() {
        log.debug(">>> OSC bufferingClientHttpRequestFactory init");

        BufferingClientHttpRequestFactory clientHttpRequestFactory = new BufferingClientHttpRequestFactory(clientHttpRequestFactory());
        
        return clientHttpRequestFactory;
    }

}
```


### 3. POST 예시
```java
public class RestTemplateExam{
    //빈으로 등록하여 자동 주입을 하자!
    private final RestTemplate restTemplate;
    public ResponseEntity<UserResponse> callApi() {
        //1. ClientHttpRequestFactory 생성
        HttpCompoentsClientHttpRequestFactory factory = new HttpCompoentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 타임아웃 설정 5초
        factory.setReadTimeout(5000); // 타임아웃 설정 5초

        
        HttpClient httpClient = HttpClientBuilder.create()
                        .setMaxConnToTal(50) // 최대 커넥션 수
                        .setMaxConnPerRoute(20).build();

        factory.setHttpClient(httpClient); // httpClient 설정

        // 2. RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate(factory);

        
        HttpHeader header = new HttpHeader();
        User user = new User();
        // 3. 요청할 때 보낼 HttpEntity 생성
        HttpEntity<User> entity = new HttpEntity<User>(user, header);

        // 4. 요청 uri 정의
        URI uri = UriComponentsBuilder
                    .fromUriString("http://localhost:9090")
                    .path("/api/server/user/{userid}/name/{userName}")
                    .encode()
                    .build()
                    .expand(001, "Tom")
                    .toUri();
        
        // 5. api 요청
        restTemplate.exchang(uri.toString(), HttpMethod.GET, entity, UserResponse.class);
        ResponseEntity<UserResponse> res = restTemplate.postForEntity(uri, user, UserResponse.class);

        return res.getBody();
    }
}
```