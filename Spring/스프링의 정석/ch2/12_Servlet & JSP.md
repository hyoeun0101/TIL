## 🍎 서블릿이란?

- Spring에서 Servlet을 다루므로 Servlet의 기본 개념은 익히는 것이 좋다.
- Servlet은 동적인 페이지를 만들기 위해 웹 서버에 붙이는 프로그램이다.

### 동작방식

- 요청이 들어오면 servletMapping에서 url pattern과 매칭되는 서블릿 이름을 찾는다.
- 서블릿 이름과 매핑된 서블릿을 찾는다.
- 해당 서블릿의 쓰레드 생성 후 실행한다.
- 서블릿은 싱글톤이다.
- 서블릿은 요청이 들어오면 객체가 존재하는지 확인한 다음 존재하지 않으면 객체를 생성한다.

```java
public class HellServlet extends HttpServlet{
    // 서블릿이 초기화될 때 자동 호출
    @Override
    public void init() throws ServletException{}

    // 핵심 처리
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{}

    // 서블릿이 메모리에서 제거될 때 서블릿 컨테이너에 의해서 호출
    @Override
    public void destroy(){}
}
```

## 🍎 @WebServlet

스프링의 @Controller + @RequestMapping 과 같다.

```java
@WebServlet(urlPatterns= {"/hello","/hello/*"}, loadOnStartup=1)
```

- loadOnStartup : 미리 초기화를 말한다. 1은 우선순위를 의미한다.
- 서블릿은 lazy Init이다. 요청이 들어오면 객체를 생성한다.

## 🍎 JSP(Java Serve Page)

- JspServlet이 동작한다.
- JSP로 작성된 프로그램은 WAS에서 내부적으로 Servlet 파일로 변환 후 응답한다.
- JSP 태그를 분해하여 순수한 HTML로 만든다.

### JSP 호출 과정

- 서블릿 인스턴스가 존재하는가 ?
  - NO: index.jsp -> index_jsp.java -> index_jsp.class 객체 생성 -> \_jspInit() 실행 -> \_jspService() 실행 -> 응답
  - YES : 바로 \_jspService() 실행 후 응답

### JSP의 기본 객체

- 생성없이 바로 사용할 수 있는 객체
- `request`, `session`, `pageContext`, `application`, `response`, `out` 등

### 기본 객체의 scope

- `pageContext`
  - 범위가 jsp 페이지이다. jsp 파일 당 하나의 pageContext.
  - 지역 변수 저장하기 위해 사용된다. 기본객체 request, response가 들어있다. EL을 사용하기 위해 변수를 pageContext에 저장한 후 사용한다.
- `application`
  - 애플리케이션 전체 당 1개.
- `session`
  - 클라이언트 당 1개. 클라이언트의 정보를 담는다.
  - 사용자 당 1개이기 때문에 최소한의 데이터를 담아야한다.
  - 로그인 시 생성하여 클라이언트를 구별할 수 있고, 로그아웃 시 삭제한다.
- `request`
  - 요청 당 1개. 요청 객체를 말한다.
  - request를 다른 jsp에게 넘길 때 forward를 통해 넘길 수 있다.
  - jsp에서 jsp로 데이터를 넘길 때 request로 전달할지 고려해보고, 안된다면 session을 사용해야한다. 이때, session에 부담이 갈 수 있으니 필요한 데이터를 사용 후 삭제하는 것이 좋다.
- getAttribute, setAttribute를 사용하여 읽기, 쓰기가 가능하다.

## 🍎 Filter

- 공통적인 요청 전처리, 응답 후처리에 사용한다. 로깅, 인코딩 등

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
