## 🍎 톰캣 내부 구조

![톰캣 내부 구조](https://user-images.githubusercontent.com/96059261/200199871-5272a408-9a8b-48bd-934c-af91df354c49.png)

- Server > Service > Connector > Engine > Host > Context > Servlet
- 톰캣을 실행해보면 위의 순서대로 생성, 호출되는 것을 볼 수 있다.
1. 사용자 요청이 옴.
2. 요청은 쓰레드풀에 있는 쓰레드가 처리하게 됨.
3. Service안의 Connector는 프로토콜에 따라 이 요청을 처리할 Connector가 결정된다.
4. 그 다음 Connector는 요청을 Engine에게 전달한다. Engine안에는 여러 host가 있을 수 있다.
5. Host안에 여러 개의 Context가 있을 수 있으며, Context가 바로 우리가 만드는 어플리케이션이다.
6. Context안에는 Servlet이 여러 개 있다. Servlet은 작은 서버 프로그램이란 뜻이며, Controller와 비슷한 개념! (요청하면 응답)

## 🍎 톰캣 설정 파일

### 톰캣설치경로/conf/server.xml

- Tomcat 서버 설정 파일
- Server > Service > Engine > Host > Context에 대한 설정이 있다.

### 톰캣설치경로/conf/web.xml

- Tomcat의 모든 web app의 공통 설정
- 서블릿을 등록한다. (DefaultServlet, JspServlet 등록)

```xml
    <servlet>
        <servlet-name>default</servlet-name>
        <servlet-class>org.apache.catalina.servlets.DefaultServlet</servlet-class>
        <init-param>
            <param-name>debug</param-name>
            <param-value>0</param-value>
        </init-param>
        <init-param>
            <param-name>listings</param-name>
            <param-value>false</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

   <servlet-mapping>
        <servlet-name>default</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

```

### 웹앱이름/WEB-INF/web.xml

- web app의 개별 설정
- 스프링에서 서블릿으로 DispatcherServlet을 등록한다.

```xml
<servlet>
		<servlet-name>appServlet</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
        <!-- servelt-context.xml로 설정한다는 의미. -->
			<param-name>contextConfigLocation</param-name>
			<param-value>/WEB-INF/spring/appServlet/servlet-context.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>appServlet</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
```
