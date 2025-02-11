## 🍎 RestTemplat이란?
- Rest API를 호출할 수 있는 Spring 내장 클래스이다.

### RestTemplate의 특징
- Restful 원칙을 지킨다.
- HTTP 서버와 통신을 단순화해준다.?
- Spring 3.0부터 지원되었고, json, xml 응답을 모두 받을 수 있다.
- HTTP 메서드 (GET, POST, DELETE, PATCH, PUT)을 지원한다.
- API 호출 후 응답받을 때까지 기다리는 동기 방식이며, Spring 5.0 이후 부터는 deprecated되었다. (Spring 5부터는 동기, 비동기 방식을 둘다 지원)

## 🍎 RestTemplate 동작 원리
![Alt text](/image/resttemplate.png)     

1. 애플리케이션 내부에서 **RestTemplate**를 생성하고, URI, HTTP 메서드 등의 헤더를 담아 요청한다.
    - (EX) restTemplate.exchange(uri, HTTPMethod.GET, Object.class));
2. **RestTemplate**은 **MessageConverter**를 이용해서 전송할 데이터(requestEntity)를 request Body에 담을 Json으로 변환한다.
3. **RestTemplate**은 **ClientHttpResquestFactory**로부터 **ClientHttpRequest**를 받아온다.
    - ClientHttpRequestFactory의 구현체로는 HttpURLConnection, Apache HttpComponents, HttpClient 등이 있다. 
4. **CilentHttpRequest**가 HTTP 요청 메세지를 만들어 서버에게 요청을 보낸다.
5. 응답에서 에러가 발생하면 **ResponseErrorHandler**가 에러 처리를 한다.
6. **ResponseErrorHandler**는 **ClientHttpResponse**에서 응답 데이터를 가져와 에러를 처리한다.
7. **RestTemplate**은 **HttpMessageConverter**를 통해 응답 메세지를 java object로 변환한다.
8. 애플리케이션에 반환한다.


## 🍎 RestTemplate 사용하기
### 1. RestTemplate 빈 등록
- spring-webmvc 의존성을 추가하지만 이는 기본 스프링 부트 의존성에 포함되어 있다. 
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

### 2. RestTemplate의 메서드
|메서드|HTTP|설명|
|----|------|---|
|getForObject|GET|GET 요청을 하고, 응답 데이터를 object로 반환한다|
|getForEntity|GET|GET 요청을 하고, 응답 데이터를 ResponseEntity로 반환한다|
|postForObject|POST|POST 요청을 하고, 응답 데이터를 object로 반환한다|
|postForEntity|POST|POST 요청을 하고, 응답 데이터를 ResponseEntity로 반환한다|
|postForLocation|POST|POST 요청을 하고, 응답 헤더에 저장된 uri를 반환한다|
|delete|DELETE|HTTP DELETE 메서드 요청을 한다|
|put|PUT|HTTP PUT 메서드 요청을 한다|
|patchForObject|PATCH|HTTP PATCH 메서드 요청을 한다.|
|headForHeaders|HEADER|HEADER 메서드 요청을 한다. (header 메서드가 뭔지 찾아볼 것!)|
|optionsForAllow|OPTIONS|주어진 URL 주소에서 지원하는 HTTP 메서드를 조회한다|
|exchange|any|HTTP 헤더를 새로 만들며, 모든 HTTP 메서드를 사용할 수 있다|
|execute|any|Request/Response 콜백을 수정할 수 있다|



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