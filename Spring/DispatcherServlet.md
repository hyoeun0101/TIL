
입력, 처리, 출력 중 입력을 DispatcherServlet이 처리를 함.   


![image](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d5665bce-e1c5-4588-8359-2a3239c24c9e/Untitled.png)

`HandlerMapping`: URL-메서드 식으로 맵핑되어 있는데, URL과 맵핑되는 메서드를 찾아 DS에게 반환   
`HandlerAdapter` : DS와 Controller의 느슨한 연결을 위해 존재. (변경에 유리) 메서드를 받아 Controller와 연결해줌. 컨트롤러 뿐만 아니라 서블릿도 호출 가능.

`ViewResolver` : 실제 뷰이름을 반환. InternalResourceViewResolver 사용.(servlet-context.xml 에서 빈 등록)

`JstlView` : 모델을 받아 jsp에 전달.

# DispatcherServlet 소스 분석하기

spring-webmvc-5.0.7RELEASE.jar >   
소스파일 위치> org/springframework/web/servlet/DispatcherServlet.java   
기본 전략> org/springframework/web/servlet/DispatcherServlet.properties   


- 기본 전략 소스
```
//지역에 대한 정보 처리
org.springframework.web.servlet.LocaleResolver=org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
//테마 기능
org.springframework.web.servlet.ThemeResolver=org.springframework.web.servlet.theme.FixedThemeResolver
//두 개의 클래스 지정, 순서대로 처리
org.springframework.web.servlet.HandlerMapping=org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping,\
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

org.springframework.web.servlet.HandlerAdapter=org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter,\
	org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter,\
	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
//예외 처리
org.springframework.web.servlet.HandlerExceptionResolver=org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver,\
	org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver,\
	org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver

org.springframework.web.servlet.RequestToViewNameTranslator=org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator

org.springframework.web.servlet.ViewResolver=org.springframework.web.servlet.view.InternalResourceViewResolver
//데이터를 맵 형태로 일시적으로 저장. session을 이용함.
org.springframework.web.servlet.FlashMapManager=org.springframework.web.servlet.support.SessionFlashMapManager
```

### 주요 메서드
|메서드|역할|
|---------|----|
| void initStrategies(ApplicationContext context) | 기본 전략 초기화|
|void doService(HttpServletRequest request, HttpServletResponse response)|doDispatch() 호출|
|void doDispatch(HttpServletRequest request, HttpServletResponse response) |실제 요청 처리|
|void processDispatchResult(HttpServletRequest request, HttpServletResponse response, HandlerExecutionChain)|예외가 발생했는지 확인, 발생하지 않았으면 render()를 호출|
|void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response)| 응답 결과를 생성해서 전송|

`DS` -------- url -------> `HandlerMapping`      
     <-- HandlerMethod --     
`DS`---이 메서드를 누가 처리? ---> `HandlerAdapter` ----> `controller`