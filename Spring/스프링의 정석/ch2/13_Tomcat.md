## 🍎 톰캣 내부 구조

![톰캣 내부 구조](https://user-images.githubusercontent.com/96059261/200199871-5272a408-9a8b-48bd-934c-af91df354c49.png)

- Server > Service > Connector > Engine > Host > Context > Servlet
- 톰캣을 실행해보면 위의 순서대로 생성, 호출되는 것을 볼 수 있다.

## 🍎 톰캣 설정 파일

### 톰캣설치경로/conf/server.xml

- Tomcat 서버 설정 파일
- Server > Service > Engine > Host > Context 순서로 서버에 대한 설정이 있다.

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

- servlet-context.xml로 DispatcherServlet을 초기화한다.
