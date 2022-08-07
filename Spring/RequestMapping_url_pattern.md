# RequestMapping
- @RequestMapping("/url") 는 get,post 요청 둘다 허용
- @RequestMapping(value="/url", method=RequestMethod.POST) 는 POST 요청 허용
- @RequestMapping(value="/url", method=RequestMethod.GET) 는 GET 요청 허용

### @GetMapping, @PostMapping
- 4.3부터 사용 가능. 스프링 버전 5.0.7로 변경하기
   
단순히 GET요청 받아 jsp 띄워 주는거면      
`servelt-context.xml`에 다음 코드 추가
```xml
<view-controller path="/register/add" view-name="registerForm"/>
<!-- <mvc:view-controller path="/register/add" view-name="registerForm"/> -->
```
```java
@GetMapping("/register/add")
public String register(){
    return "registerForm"
}
```
이것과 동일. 간단하게 뷰 이름만 반환하는 메서드 대신에 뷰를 뷰 컨트롤러에 등록. 뷰 컨트롤러는 `GET`요청만 허용함.   

### 에러 메세지 띄우기
```java
@PostMapping("/register/save")
public Strign save(User user, Model m) throws Exception{
    if(!isValid(user)){
        String msg = URLEncoder.encode("id를 잘못를 입력했습니다.","utf-8");

        m.addAttribute("msg",msg);
        return "redirect:register/add";
        //return "redircet:/register/add?msg="+msg;
    }
    return "registerInfo";
}
```
여기서 redirect를 하는데 사실 redirect를 통해 url를 재요청을 하게되면,    
save에 있는 Model과 add에 있는 Model은 다른것.         
save의 모델에 msg를 저장하고 넘겨주는데,      
이는 스프링이 자동으로 "redircet:/register/add?msg="+msg 이렇게 바꿔주기 때문임.   
즉 위 두줄과 주석처리 부분은 같음.     
[registerInfo.jsp]      
```jsp
<h1>id=${param.id}</h1>
<h1>pwd=${param.pwd}</h1>
<h1>name=${param.name}</h1>
<h1>email=${param.email}</h1>
<h1>birth=${param.birth}</h1>
<h1>sns=${paramValues.sns}</h1>
<h1>sns=${paramValues.sns[0]}</h1>
<h1>sns=${paramValues.sns[1]}</h1>
<h1>sns=${paramValues.sns[2]}</h1>
```
[registerInfo.jsp]변경
```jsp
<h1>id=${user.id}</h1>
<h1>pwd=${user.pwd}</h1>
<h1>name=${user.name}</h1>
<h1>email=${user.email}</h1>
<h1>birth=${user.birth}</h1>
<h1>sns=${user.sns}</h1>
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

# URL 인코딩
- 퍼센트 인코딩
- url에다가 non-ASCII 작성하면 자동으로 문자 코드(16진수)문자열로 변환됨.   
- url은 ASCII 여야함.
- url 인코딩: 문자코드(숫자)를 문자열로 변환.  ex) 김효은 -> %EA%B9%80%ED%9A%A8%EC%9D%80
- 반대는 디코딩.
   
요청이 들어오면 request.setCharacterEncoding("UTF-8") 이걸로 인코딩 해줘야함.     
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
