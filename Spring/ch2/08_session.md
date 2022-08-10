# 세션이란?

: a collection of related HTTP transactions made by one browser to one server
서로에 관련된 요청으

- 서로 관련된 요청과 응답을 하나로 묶은 것.- 쿠키를 이용
  브라우저가 요청하면 서버가 쿠키에 세선id를 생성해서 줌.
  그 다음 요청에 쿠키가 따라감. 요청 구별 . 쿠키땜ㄴ에
  요청에는 같은 세션id라는 공통점이 생기고 그룹화시킬 수 있음
  같은 세션을 가지고 있으면 같은 세션 저장소 사용
  수동 종료 " invalidate()
  자동 종료 : time out
  종료되면 새로운 세션id생성

브라우저 요청ㅇ
세션 객체 생성- 세션 id를 쿠키에 담음.Set-Cookie:JSESSIONID=asdfsdf
브라우저 쿠키 저장. 요청헤더에 쿠키붙음.
같은 pc라도 서로 다른 브라우저면 다른 세션id저장.
세션 저장소 사용

```
HttpSession session = request.getSession();//세션 저장소 사용
session.setAttribute("id","asdf");
```

Cooke:JSESSIONS=qwer
응답이 왔어.getSessopm()하면 서버에서 일치하는 세션객체를 찾아서 거기에 setAttribute("id","asdf")하면 키-값으로 저장.

- 요청은 stateless하고, 독립적인데, 세션을 통해 브라우저를 구별할 수 있음.
- 브라우저 1 : 세션 1 (서버에서 브라우저마다 개별 저장소를 제공. 즉, 세션 객체를 제공)
- 브라우저마다 하나씩 무조건 존재하기 때문에 서버 부담이 큼. 그래서 최소한의 데이터만 저장하는 게 좋음.

**<동작 과정>**

1. 브라우저 요청
2. 서버에서 세션 저장소를 생성하고, 세션 ID를 부여. 쿠키에 세션 ID를 저장. 요청 헤더에 쿠키를 담아 응답.  
   Cookie JSESSIONID=~~
3. 다음 요청 시 브라우저는 쿠키에 세션 ID를 담아 요청함.

### 세션 객체 얻기

```java
//request에서 세션을 가져오기
HttpSession session = request.getSession();
session.setAttribute("id","qqqq");// 세션 저장소에 id=qqqq 저장
```

**\_<저장소 관련 메서드>**

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

2. 자동 종료 - web.xml

```xml
<session-config>
    <!--> 30분 뒤 종료<-->
    <session-timeout>30</session-timeout>
</session-config>
```

세션이 종료되면 응답에서 새로운 세션ID 발급(새로운 세션 객체 생성)
이전 세션을 가진 브라우저에서 30분동안 요청을 안하면 timeout. 새로운 세션id 생성.

정리
|쿠키|세션|
|---|---|
|브라우저에 저장| 서버에 저장|
|서버 부담x|서버 부담 o|
|보안에 불리|보안에 유리|
|서버 다중화에 유리|서버 다중화에 불리|
서버1- 서버 m가 있다면, 서버 m개에 모두 각각 세션저장소가 존재. m개의 세션 저장소를 동기화해야함. 그래서 세션은 서버 다중화에 불리.
그래서 쿠키에 저장하는 대신 암호화.

### 쿠키 차단했을 경우

우선 브라우저에서 첫 요청을 보내면 서버에서 세션ID를 생성하여 브라우저에게 응답을 하는데, 이때 두 가지 방법으로 응답을 함.  
응답 헤더에 쿠키를 추가, jsp의 url에 세션ID를 붙이기

```html
<li><a href="/ch1/;jsessionid=7DC2F856214A14B31E3A017E32AC4AE0">Home</a></li>
```

<c:url> 이게 세션ID를 붙이는 역할을 함. 그래서 <c:url> 로 작성해줘야함.  
브라우저에서 쿠키를 차단하면 url에 세션ID를 붙여서 동작 하고, 차단하지 않았으면 다음부턴 쿠키의 세션ID를 통해 동작함.  
<c:url>를 사용하지 않고, 쿠키도 차단했다면 브라우저의 요청마다 계속 세션ID가 부여됨.  
이러면 서버에 부담이 감.  
결론: 브라우저에서 쿠키를 차단했을 경우를 대비해 url은 <c:url>로 작성해주어야 한다.

### 실습

1. 로그인 성공하면, 세션에 id저장
2. 로그아웃 하면, 세션 삭제
3. 세션에 id가 있으면 logout보이게, 없으면 login 보이게

```java
@GetMapping("/logout")
public String logout(HttpSession session){
  //1. 세션 종료
  session.invalidate();

}
```

```jsp
<c:set var="loginOutLink" value="${sessionScope.id==null ? '/login/login' : 'login/logout'}" />
<c:set var="loginOut" value="${sessionScope.id==null ? 'Login' : 'Logout' }"/>

<li><a href="<c:url value='${loginOutLink }'/>">${ loginOut }</a></li>
```

### session=false

```jsp
<%@page session="false"  %>
```

-> 세션이 필요없는 jsp화면(로그인 전 home, loginForm 등)에 작성한다.  
 기존 세션에 영향을 끼치진 않음. 세션이 없을 때 세션을 새로 생성하지 않음.  
session=false 일때는 sessionScope, pageContext.session는 사용 불가.

from_url, to_url 뽑기

```
String refer = req.getHeader("referer");

```
