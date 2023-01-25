## 🍎 RequestMapping

- `@RequestMapping("/url")` 는 get,post 요청 둘다 허용 -` @RequestMapping(value="/url", method=RequestMethod.POST)` 는 POST 요청 허용
- `@RequestMapping(value="/url", method=RequestMethod.GET)` 는 GET 요청 허용

## 🍎 @GetMapping, @PostMapping

- 4.3부터 사용 가능. 스프링 버전 5.0.7로 변경하기 -` 단순 GET 요청`은 설정으로 대체하기
  - `servelt-context.xml`에 뷰 컨트롤러 등록하기

```xml
<view-controller path="/register/add" view-name="registerForm"/>
<!-- <mvc:view-controller path="/register/add" view-name="registerForm"/> -->
```

- 아래와 동일. 뷰 컨트롤러는 `GET`요청만 허용함.

```java
@GetMapping("/register/add")
public String register(){
    return "registerForm"
}
```

## 🍎 URL Pattern

### 우선순위

1. `exact mapping` -완전 일치 /test/hello.do
2. `path mapping` - 경로 일치 /test/\*
3. `extension mapping` - 확장자 일치 \*.do

```java
@Controller
public class RequestMappingTest {


    @RequestMapping("/login/*")   // /login/hello, /login/hi
    public void test2(){
        System.out.println("urlpattern=/login/*");
    }

    @RequestMapping("/login/**/tmp/*.do")   // /login/tmp/hello.do, /login/aaa/tmp/hello.do
    public void test3(){
        System.out.println("urlpattern=/login/**/tmp/*.do");
    }

    @RequestMapping("/login/??")
    public void test4(){   // /login/hi, /login/my.car
        System.out.println("urlpattern=/login/??");
    }

    @RequestMapping("*.do") // /hello.do, /hi.do, /login/hi.do
    public void test5(){
        System.out.println("urlpattern=*.do");
    }

    @RequestMapping("/*.???") //  /hello.aaa, /abc.txt
    public void test6(){
        System.out.println("urlpattern=*.???");
    }
}

```

- \*: 하나 이상

```java
@RequestMapping("/login/*") // /login/hello, /login/hi
```

- \*\* : 하위 경로 있어도 되고 없어도 되고

```java
@RequestMapping("/login/**/tmp/*.do")   // /login/tmp/hello.do, /login/aaa/tmp/hello.do
```

- ?은 한 글자.

```java
@RequestMapping("/login/??")  // /login/hi, /login/my.car
@RequestMapping("/*.???") //  /hello.aaa, /abc.txt
```

## 🍎 URL 인코딩

= 퍼센트 인코딩

- url은 ASCII 여야한다!! 따라서 non-ASCII 이면 자동으로 문자 코드(16진수) 문자열로 변환된다.
- 문자코드(숫자)를 문자열로 변환. ex) 김효은 -> %EA%B9%80%ED%9A%A8%EC%9D%80
- 반대는 디코딩.

- GET 요청일 때 url로 데이터가 오면 인코딩 작업이 필요하다.
  - 요청이 들어오면 `request.setCharacterEncoding("UTF-8")`을 통해 인코딩 해줘야한다.
  - 일일히 하기 싫으니 필터에 인코딩 설정을 한다.
  - `web.xml`에 한글 필터 작성.

```xml

	<!-- 한글 변환 필터 시작 -->
	<filter>
		<filter-name>encodingFilter</filter-name>
		<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>UTF-8</param-value>
		</init-param>
		<init-param>
			<param-name>forceEncoding</param-name>
			<param-value>true</param-value>
		</init-param>
	</filter>

	<filter-mapping>
		<filter-name>encodingFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
	<!-- 한글 변환 필터 끝 -->
```

`Maven dependencies > spring-web .jar > filter > CharacterEncodingFilter` 를 보면 doFilter에서 전처리로 `request.setCharacterEncoding(encoding)` 하는 것을 볼 수 있다.

- 응답할 때는 msg를 인코딩한 후 보내고, 뷰에서 다시 디코딩한다.
  - `String msg = URLEncoder.encode("msg입니당","utf-8");`
- jsp에서 디코딩하는 법

```jsp
<%@ page import="java.net.URLDecoder" %>
${URLDecoder.decode(param.msg,"utf-8")}
```
