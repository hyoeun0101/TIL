## 🍎 RestTemplate?

- Rest API를 호출할 수 있는 Spring 내장 클래스이다.
- Spring 3.0부터 지원되었고, json, xml 응답을 모두 받을 수 있다.
- HTTP 메서드 (GET, POST, DELETE, PATCH, PUT)을 지원한다.
- API 호출 후 응답받을 때까지 기다리는 동기 방식이며, Spring 5.0 이후 부터는 deprecated되었다. (Spring 5부터는 동기, 비동기 방식을 지원)

## 🍎 RestTemplate 동작 원리

1. 애플리케이션 내부에서 RestTemplate의 메서드를 호출한다.
2. RestTemplate은 MessageConverter를 이용해서 Object를 request Body에 담을 Json으로 변환한다.
3. `ClientHttpResquestFactory`에서 ClientHttpRequest를 받아와 요청을 전달한다.
   - 실질적으로 CilentHttpRequest가 HTTP 요청을 실행하는 것이다.
4. MessageConvertoer를 이용해 response Body의 Json을 Object로 변환한다.

- RestTemplate은 ClientHttpRequestFactory (ClientHttpRequest, ClientHttpResponse)를 통해 통신하는 것이다. ClientHttpRequestFactory의 구현체는 HttpURLConnection, Apache HttpComponents, HttpClient가 있다.

## 🍎 RestTemplate 예시

```java
public class RestTemplateExam{
    private static RestTemplate restTemplate;
    public static ResponseEntity<VO> sendExam(){
        //HTTP 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String user = new User();
        //HTTP 생성
        HttpEntity<String> entity = new HttpEntity<String>("Body에 들어갈 Json 또는 객체", headers);

        URI uri = new URI("전송할 url");

        // rest api 요청
        restTemplate.postForObject(uri, entity, String.class);
        // postForEntity("요청 url", 요청 HTTP, 응답 매핑객체)
        // resTemplate.exchan
    }
}
```

## 🍎 메서드 종류

| 메서드          | HTTP | 설명                               |
| --------------- | ---- | ---------------------------------- |
| getForObject    | GET  | GET요청으로 객체 반환              |
| getForEntity    | GET  | GET 요청으로 ResponseEntity로 반환 |
| postForLocation | POST | POST 요청으로 헤더의 location 반환 |
| postForObject   | POST | POST 요청으로 객체를 반환          |
