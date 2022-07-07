# RequestMapping

@RequestMapping(value="/login/hello", method={RequestMethod.POST})   
   
== @PostMapping("/login/hello")   
   
단순히 GET요청 받아 jsp 띄워 주는거면   
servelt-context.xml에 다음 코드 추가
```xml
<view-controller path="/register/add" view-name="registerForm"/>
<!-- <mvc:view-controller path="/register/add" view-name="registerForm"/> -->
```

# URL Pattern

### 우선순위
1. exact mapping -완전 일치 /test/hello.do
2. path mapping - 경로 일치 /test/*
3. extension mapping - 확장자 일치  *.do


```java
@Controller
public class RequestMappingTest {
    @RequestMapping("/login/hello.do") //login/hello.do
    public void test1(){
        System.out.println("urlpattern=/login/hello.do");
    }

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
*은 하나 이상   
**은 하위 경로까지   
?은 한 글자.   

### url 인코딩
url에다가 non-ASCII 작성하면 자동으로 문자열로 변환됨.   
url 인코딩: 문자코드(숫자)를 문자열로 변환.   
   
request.setCharacterEncoding("UTF-8") 이걸로 인코딩 해줘야함.   
일일히 하기 싫으니 필터에 넣어줌.   
   
web.xml에 한글 필터 작성.
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
메이븐 까보면 필터 나와있음. 코드 까보면 doFilter에서 전처리로 request.setCharacterEncoding("") 해주는 걸 볼 수 있다.   
   
   
응답할 때, msg를 인코딩 후 보내고, 뷰에서 다시 디코딩하기.   
String msg = URLEncoder.encode("msg입니당","utf-8");   
   
<%@ page import="java.net.URLDecoder" %>   
${URLDecoder.decode(param.msg,"utf-8")}   