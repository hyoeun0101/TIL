## 🍎 쿠키

- 이름과 값으로 구성된 정보.
- 클라이언트를 식별하기 위해 사용한ㄴ다.
- 아스키 문자만 저장할 수 있다.(한글은 URL인코딩 해서 저장해야함.)
- 서버에서 생성 후 브라우저에 저장.
- 유효기간 이후 자동 삭제
- 서버에 요청시 domain, path가 일치하는 경우에만 자동 전송(요청헤더에 포함하여)

**<동작 과정>**

1. 서버로 처음 요청
2. 서버에서 쿠키 생성 후 응답헤더에 쿠키를 추가하여 응답
   - `Set-Cookie : id=qqqq` (응답헤더에 추가됨.)
3. 브라우저에 쿠키 저장
4. 다음 요청 시 쿠키를 요청 헤더에 넣어서 요청함.
   - `Cookie: id=qqqq`

## 🍎 쿠키 사용법

### 쿠키 생성

```java
Cookie cookie = new Cookie("id", "aaaa");// 쿠키 생성
cookie.setMaxAge(60*60*24); // 유효기간 24시간
response.addCookie(cookie); //응답헤더에 추가
```

- 응답헤더에 `Set-Cookie id=aaaa; Max-Age=86400;` 추가된다.

### 쿠키 삭제

```java
Cookie cookie = new Cookie("id","");// 삭제할 쿠키와 같은 이름 쿠키 생성
cookie.setMaxAge(0);//유효기간 0으로
response.addCookie(cookie);
```

### 쿠키 변경

```java
Cookie cookie = new Cookie("id", "")//변경할 쿠키와 같은 이름 쿠키 생성
cookie.setValue(URLEncoder.encode("김효은"));//값 변경
cookie.setDomain("www.eunoo.com"); // 도메인 변경
cookie.setPath("/ch1");//경로 변경
cookie.setMaxAge(60*60*24*7);//유효기간 변경
response.addCookie(cookie);
```

### 쿠키 읽어 오기

1. 배열에 담아 읽기

```java
Cookie[] cookies = request.getCookies();//쿠키 읽기, 여러 개일 수도 있으니 배열로.없으면 null

for(Cookie cookie: cookies){
    String name = cookie.getName();
    String value = cookie.getValue();

    System.out.printf("[cooke]name= %s, value=%s", name, value);
}
```

2. 매개변수에 애노테이션 `@CookieValue` 붙히기

```java
@CookieValue("JSESSIONID") String sessionId
```

- 추가로 jsp에서 `${ cookie.id.value }`로 값 읽을 수 있다.

## 🍎 세션
