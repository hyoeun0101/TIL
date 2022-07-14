# 쿠키란?
- 이름과 값으로 구성된 정보.
- 클라이언트를 식별하기 위해 사용됨.
- 아스키 문자만 저장할 수 있음.(한글은 URL인코딩 해서 저장해야함.)

__<동작 과정>__

1. 서버에서 처음 요청
2. 서버에서 쿠키 생성 후 응답헤더에 쿠키를 추가하여 응답 
    - Set-Cookie : id=qqqq (응답헤더에 추가됨.)
3. 클라이언트에 쿠키 저장   
4. 다음 요청 시 요청 경로를 확인해서 그에 맞는 쿠키를 요청 헤더에 넣어서 요청함.

ex> 아이디 저장 기능 만들기
### 쿠키 생성
```java
Cookie cookie = new Cookie("id", "aaaa");// 쿠키 생성
cookie.setMaxAge(60*60*24); // 유효기간 24시간
response.addCookie(cookie); //응답헤더에 추가
```

응답헤더에   
Set-Cookie id=aaaa; Max-Age=86400;... (유효시간)  
추가되어 응답.   
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

## 쿠키 읽어 오기
요청 헤더에   
POST /ch1/login/login HTTP/1.1   
HOST: localhost   
Cookie: id=qqqq; SESSIONID=zzzzzzzzzz    

쿠키가 있음.

```java
Cookie[] cookies = request.getCookies();//쿠키 읽기, 여러 개일 수도 있으니 배열로,없으면 null

for(Cookie cookie: cookies){
    String name = cookie.getName();
    String value = cookie.getValue();

    System.out.printf("[cooke]name= %s, value=%s", name, value);
}

```
### 실습

1. 아이디 기억 체크되어 있으면 쿠키 생성
2. 없으면 쿠키 삭제

3. jsp에서 쿠키가 있으면 id보여주고, 아이디 기억 체크하기
```html
        <input type="text" name="id" value="${cookie.id.value }" placeholder="이메일 입력" autofocus>

        <label><input type="checkbox" name="rememberId" ${empty cookie.id.value ? "" : "checked" }> 아이디 기억</label> |
```

추가로 checkbox에서 value="on" 이 기본임.
서버에서 boolean rememberId로 받으면 체크하면 true, 체크안하면 false임.
String rememberId로 받는다면 on으로 찍히는 걸 볼 수 있음.