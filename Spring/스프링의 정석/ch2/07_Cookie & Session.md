## 🍎 쿠키

- 이름과 값으로 구성된 정보.
- 클라이언트를 식별하기 위해 사용한다.
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

### 쿠키 읽기

1. 배열에 담아 읽기

```java
Cookie[] cookies = request.getCookies();//쿠키 읽기, 여러 개일 수도 있으니 배열로.없으면 null

for(Cookie cookie: cookies){
    String name = cookie.getName();
    String value = cookie.getValue();

    System.out.printf("[cooke]name= %s, value=%s", name, value);
}
```

2. 매개변수에 애노테이션 `@CookieValue("키이름")` 붙히기

```java
@CookieValue("JSESSIONID") String sessionId
```

- 추가로 jsp에서 `${ cookie.id.value }`로 값 읽을 수 있다.

## 🍎 세션

- 서로 관련된 요청과 응답을 하나로 묶은 것.
- 쿠키를 이용하여 세션 ID를 브라우저에 저장한다.
- 요청은 stateless해서 브라우저를 구별할 수 없는데 세션을 통해 브라우저를 구별할 수 있다.
- 브라우저 1 : 세션 1 (서버에서 브라우저마다 세션 객체를 제공한다.)
- 브라우저마다 하나씩 무조건 존재하기 때문에 서버 부담이 크다. 때문에 최소한의 데이터만 저장하는 게 좋다.

**<동작 과정>**

1. 브라우저 처음 요청
2. 서버에서 세션 저장소(세션 객체)를 생성하고, 세션 ID를 부여한다.쿠키에 세션 ID를 저장하고 응답 헤더에 쿠키를 담아 응답한다.
   - Set-Cookie: JSESSIONID=ASDQWE12323ASD123
3. 쿠키를 브라우저에 저장.
4. 다음 요청 시 쿠키를 요청 헤더에 넣어 요청한다.
   - 서버에선 쿠키에 있는 세션ID로 브라우저를 구별할 수 있다.
   - Cookie:JSESSIONID=ASDQWE12323ASD123

## 🍎 세션 사용법

### 세션 객체 얻기

```java
//request에서 세션을 가져오기. 요청헤더에 있는 세션ID와 일치하는 세션 객체를 반환
HttpSession session = request.getSession();
session.setAttribute("id","qqqq");// 세션 저장소에 id=qqqq 저장
```

- getSession(true)는 세션이 없는 경우 새로운 세션 생성
- getSession(false)는 세션이 없어도 새로 생성X.

### 세션 관련 메서드

- 추가: void setAttribute(String name, Object value)
- 읽기 : Object getAttribute(String name)
- 삭제 : void removeAttribute(String name)
- 모든 key 반환 : Enumeration getAttribNames()

| 세션 메서드                               | 설명                                                        |
| ----------------------------------------- | ----------------------------------------------------------- |
| String getId()                            | 세션의 ID를 반환                                            |
| long getLastAccessedTime()                | 세션 내에서 최근 요청을 받은 시간을 반환                    |
| boolean isNew()                           | 새로 생성된 세션인지 반환. request.getSession()호출 후 사용 |
| void invalidate()                         | 세션 객체를 제거, 세션 즉시 종료                            |
| void setMaxInactiveInterval(int interval) | 지정된 시간 후에 세션을 종료                                |
| int getMaxInactiveInterval()              | 예약된 세션 종료 시간을 반환                                |

### 세션의 종료

1. 수동 종료

```java
HttpSession session = request.getSession();
session.invalidate();//세션 즉시 종료
session.setMaxInactiveInterval(30*60);//30후 종료
```

2. 자동 종료 - web.xml에 추가

```xml
<session-config>
    <!--> 30분 뒤 종료<-->
    <session-timeout>30</session-timeout>
</session-config>
```

- 이전 세션을 가진 브라우저에서 30분동안 요청을 안하면 Timeout. 새로운 세션ID 생성.

---

## 🍎 쿠키와 세션의 차이점

|              | 쿠키                                          | 세션                                 |
| ------------ | --------------------------------------------- | ------------------------------------ |
| 저장 위치    | 브라우저에 저장                               | 서버에 저장                          |
| 요청 속도    | 빠름                                          | 서버 처리가 필요하기 때문에 느림     |
| 서버 부담    | 서버 부담x                                    | 서버 부담 o                          |
| 보안         | 불리                                          | 서버에서 관리하기 때문에 보안에 유리 |
| 라이프사이클 | 브라우저 종료해도 만료시간이 있으면 남아있음. | 브라우저 종료 시 삭제됨.             |

---

## 🍎쿠키 차단한 경우

- 서버에서 세션ID를 생성하여 브라우저에게 응답할 때 두 가지 방법으로 한다.
  1.  응답 헤더에 쿠키를 추가
  2.  jsp의 url에 세션ID를 붙이기
      - 세션ID를 가진 쿠키를 브라우저에 저장하면 다음 url엔 세션ID를 붙이지 않는다.
- **브라우저에서 쿠키를 차단한 경우** 요청마다 세션ID를 계속 생성하고 url에 붙여 응답한다. 새로운 세션ID를 계속 생성하기 때문에 서버에 부담이 간다.
- `<c:url>`을 사용하면 세션ID를 계속 생성하기 않고, 세션ID를 url에 붙인다. 브라우저에서 쿠키를 차단했을 경우를 대비해 `<c:url>`을 사용하자.

## 🍎 JSP에서 session 사용하기

```jsp
<%@page session="false"  %>
```

- 세션이 필요없는 jsp화면에 작성한다.
- 세션이 없을 때 세션을 새로 생성하지 않는다.
- session=false 일때는 sessionScope, pageContext.session는 사용 불가.
