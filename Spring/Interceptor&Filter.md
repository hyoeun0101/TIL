![1](https://user-images.githubusercontent.com/96059261/211508902-fe794b85-41b1-4b42-9d45-135b1f70b76f.PNG) 
## 🍎 Filter
- J2EE 표준 스펙 기능으로 DispatcherServlet에 요청이 전달되기 전/후에 실행되어 모든 요청에 대한 부가작업을 처리할 수 있게 해준다.
- 스프링 컨테이너가 아닌 웹 컨테이너(서블릿 컨테이너)에 의해 관리된다.(스프링 빈으로 등록은 된다)
- 공통적인 요청 전처리, 응답 후처리에 사용한다. 로깅, 인코딩 등

- 대표적으로 Filter를 인증/인가에 사용하는 도구로 SpringSecurity가 있다. SpringSecurity는 Spring MVC에 종속적이지 않는데 이는 필터를 기반으로 인증/인가를 처리하기 때문이다.

### Filter의 메소드
- javax.servlet의 Filter 인터페이스를 구현하며 이는 다음의 세 가지 메소드를 가진다.
```java
package javax.servlet;

import java.io.IOException;

public interface Filter {
    default void init(FilterConfig filterConfig) throws ServletException {
    }

    void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException;

    default void destroy() {
    }
}

```
- init : 웹 컨테이너에 의해 처음 1회만 실행되며 필터 객체를 초기화한다.
- doFilter : 요청이 DispatcherServlet으로 전달되기 전에 실행된다. FilterChain의 doFilter를 통해 다음 대상으로 요청을 전달한다. chain.doFilter() 호출 전/후에 필요한 전처리 과정을 넣는다.
- destory : 웹 컨테이너의해 1번만 실행되며 필터 객체를 제거하고 자원을 반납한다.

### Filter의 동작 방식

- 요청이 들어오면 filter에서 요청에 대한 전처리를 진행한다.
- Servlet 실행하여 요청, 응답 처리를 하는 기본적인 동작을 한다.
- filter에서 응답에 대한 후처리를 진행한다.

### Filter가 여러 개일 경우 어떻게 동작할까?

1. Filter1의 전처리 실행
2. Filter2의 전처리 실행
3. 서블릿 호출 (기본적인 동작)
4. Filter2의 후처리 실행
5. Filter1의 후처리 실행

### 필터 등록하기

- web.xml에 필터를 등록한다.
- url 패턴과 일치하는 요청이 들어오면 filter를 통과한 다음 serlvet을 실행한다.
- 다음은 encoding Filter 등록 코드이다.

```xml
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
```

### 필터 만들기

- Filter 인터페이스의 메서드 init, doFilter, destroy를 작성한다.

```java
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

// 필터를 적용할 요청의 패턴 지정해야한다.
@WebFilter(urlPatterns="/*") // 모든 요청에 필터를 적용.
public class PerformanceFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 1. 전처리 작업
		long startTime = System.currentTimeMillis();

		// 2. 서블릿 또는 다음 필터를 호출
		chain.doFilter(request, response);

		// 3. 후처리 작업
		System.out.print("["+((HttpServletRequest)request).getRequestURI()+"]");
		System.out.println(" 소요시간="+(System.currentTimeMillis()-startTime)+"ms");
	}

	@Override
	public void destroy() {}
}
```


## 🍎 인터셉터
- Spring이 제공하는 기술로써, DispatcherServlet이 Controller를 호출하기 전/후에 실행되며 HttpServletRequest와 HttpServletResponse를 참조 및
가공할 수 있게 해준다.
- 웹 컨테이너에서 동작하는 Filter와 달리 인터셉터는 스프링 컨텍스트에서 동작한다.
- DispatcherServlet은 핸들러 매핑을 통해 적절한 컨트롤러를 찾아주는데 그 결과로 실행체인(HandlerExecutionChain)을 반환한다.
- 실행 체인에 등록된 인터셉터를 차례대로 실행한 후 Controller를 실행한다. 

### 인터셉터의 메소드

```java
package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;

public interface HandlerInterceptor {
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
```
- preHandle : 컨트롤러 호출 전에 실행된다. 컨트롤러 이전의 전처리 작업을 추가할 수 있다. true를 반환하면 다음 작업을 진행하고, false이면 작업을 중단하여 Controller 실행을 안한다.
- postHandle : 컨트롤러 호출 후에 실행된다. 컨트롤러 이후의 후처리 작업을 추가할 수 있다. 네 번째 파라미터인 ModelAndView는 컨트롤러의 반환값인데 요즘은 RestAPI 기반의 컨트롤러를 사용해서 자주 사용되지는 않는다.
- afterCompletion : 모든 작업이 완료되면 실행된다. 요청 처리 중에 사용한 리소스를 반환할 때 사용하기 적합하다. postHandler와 달리 컨트롤러를 실행하다가 예외가 발생하더라도 afterCompetion은 반드시 호출된다.


 ## 🍎 Filter와 Interceptor 차이 정리
|차이|Filter|Interceptor|
|---|------|-----------|
|호출 시점|DispatcherServlet이 실행 전|DispatchServlet 실행 후|
|설정 위치|web.xml|spring-servlet.xml|
|관리되는 컨테이너|서블릿 컨테이너|스프링 컨테이너|
|스프링의 예외처리 여부|X|O|
|Request/Response 객체 조작 여부|O|X|
|구현 방식|web.xml에서 설정하기|설정 및 메서드 구현 필요|
|용도|공통된 보안 및 인증/인가 작업, 모든 요청에 대한 로깅, 이미지/데이터 압축 및 문자열 인코딩, Spring과 분리되어야 하는 기능|세부적인 보안 및 인증/인가 공통작업, API 호출에 대한 로깅, Controller로 넘겨주는 데이터 가공|


- 스프링의 예외 처리 여부
    - Filter는 스프링 앞의 서블릿에서 관리하기 때문에 예외가 처리되지 않고 서블릿까지 전달된다. 그럼 자동으로 서블릿은 500 Status 응답을 반환한다. 이를 해결하기 위해선 Filter에서 응답 객체에 대한 예외처리가 필요하다.
```java
public class MyFilter implements Filter {
    @Ovrride
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse servletRes = (HttpServletResponse) response;
        servletRes.setStatus(HttpServletResponse.SC_NOT_FOUND);
        servletRes.getWriter().print("유저가 존재하지 않음");
    }
}
```

- Request/Response 객체 조작 가능 여부
    - Filter는 FilterChain으로 요청,응답 객체를 전달할 수 있어 조작이 가능하다.
    - DispatcherServlet이 여러 Interceptor 목록을 가지고 있고 이를 for문으로 실행시킨다. Interceptor에서는 boolean값을 리턴하며 요청, 응답 객체를 변경할 수 없다. true를 리턴하면 다음 인터셉터를 실행하거나 컨트롤러로 요청이 전달된다.

## WebMvcConfigurer

- Spring MVC 자동 구성 제어하기
  Spring MVC 구성에 Forammter, MessageConverter 등을 추가 등록하기 위해서 WebMvcConfigurer를 구현한다.

WebMvcRegistrations 는 RequestMappingHandlerMapping, Request
