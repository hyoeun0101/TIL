public class ResponseEntity<T> extends HttpEntity<T>{...}

HttpEntity을 상속하고 있다. `HttpStatus`, `HttpHeaders`, `HttpBody`를 포함하고 있다.
응답으로 변환될 모든 정보들을 객체로 만들어 반환한다.
