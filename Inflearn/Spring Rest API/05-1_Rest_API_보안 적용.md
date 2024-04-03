@ElmentCollection()
기본값 fetch lazy

## 🍎 Spring Security
### 시큐리티의 종류
- 웹 시큐리티 : filter 기반 시큐리티. SecurityFilterChain을 사용한다.
- 메소드 시큐리티 : 메서드가 호출됐을 때 권한을 확인하는 방식. AOP 기반

- 둘 다 SecurityInterceptor를 사용하여 시큐리티 기능을 제공한다.
- SecurityInterceptor의 구현체 : FilterSecurityInterceptor, MethodSecurityInterceptor

### 동작 방식
ex) BasicAuthentication  
1. 요청이 오면 SecurityInterceptor 호출. 해당 요청이 인증이 필요한지 체크 (SecurityFilter를 적용해야하는지)
2. 인증 정보 확인 : SecurityContextHolder(TreadLocal를 구현)에서 인증정보를 꺼내려고 시도. 인증정보 없으면
-> AuthenticationManager를 사용해서 로그인. AuthenticationManager를 통해 얻은 인증 정보를 SecurityContextHolder에 저장.
2. 인증이 필요한 url인데 요청에 인증 정보가 없으면 로그인 화면을 띄워줌. AuthenticationManager를 통해 로그인을 한 후 인증 정보(Authentication)를 얻고, 그 인증 정보를 SecurityContextHolder에 저장
    - AuthenticationManager가 로그인할 때 사용하는 주요 인터페이스 두 개 : UserDetailsService, PasswordEncoder
    - 사용자가 아이디, 비번을 입력하면 UserDetailService가 username과 매핑된 password를 데이터베이스(또는 어딘가)로부터 가져옴. 사용자가 입력한 password와 가져온 password가 일치하는지 확인, 일치하면 로그인 성공. 이 때 password는 PasswordEncoder로 인코딩. 
    - SecurityHolder는 ThreadLocal의 구현체이다. ThreadLocal은 한 쓰레드 내에서 자원을 공유할 수 있다. 
3. 인증이 되었다면 (인증 정보가 있다면) : 해당 인증 정보가 접근할 리소스에 권한을 가지고 있는지 확인.(AccessDecisionManager) 보통 User의 Role로 체크를 함.


- 정리
    - webFilterSecurityInterceptor에서 인증이 필요한지 체크
    - 이 요청에 security filter를 적용해야한다면
    - 인증 정보 확인. SecurityContextHoder(인메모리)에서 인증정보를 꺼내려고 함.
    - 인증 정보가 없다.
    - authenticationManager를 통해 로그인.
    - 로그인 성공적
    - Authentication (인증 정보) 생성 후 SecurityContextHoder에 저장


## 🍎 예외 발생 테스트 코드 짜는 방법
1. Assertions.assertThrows 사용
    - 예외 타입만 확인 가능 (Junit4에선 @Test(expected = Exception.class))
2. try-catch
    - 예외 타입과 메시지 확인 가능
    - 하지만 코드가 다소 복잡.
3. @Rule ExpectedException
    - 코드는 간결하면서 예외 타입과 메시지 모두 확인 가능
    - 단 given-when-then 순서로 테스트 못하고, expected-when 순으로 테스트 작성해야함.



Spring 시큐리티 자동 설정을 적용. 모든 요청은 인증이 필요하게 되고, 인메모리로 임의의 사용자를 만들어줌.  


docs/index.


웹에서 필터를 적용할지 말지 설정
@EnableWebSecurity를 붙여야 스프링 부트가 기본으로 설정되어 있는 시큐리티 설정을 무시하고, 내가 설정한 설정을 사용함.  
## 🍎Spring Security 적용하기
### 1. 의존성 추가
```
<dependency>
    <groupId>org.springframework.security.oauth.boot</groupId>
    <artifactId>spring-security-oauth2-autoconfigure</artifactId>
    <version>2.1.0.RELEASE</version>
</dependency>
```
- Spring security 의존성을 추가하면 모든 요청에 인증이 필요해짐.
### Spring Security config 설정하기

1. Appconfig에 passwordEncoder 빈 등록
2. SecurityConfig 생성 : 이제 Spring Security는 기본 설정 대신 이 설정을 사용한다.
    - tokenStore 빈 등록
    - AuthenticationManager 빈 등록 : Autherization Server와 리소스 서버에서 해당 AuthenticationManager를 참조할 수 있도록 빈으로 노출.
    - AuthenticationManagerBuilder를 통해 내가 정의한 UserDetailService와 PasswordEncoder를 사용하도록 등록. but 최신 시큐리티 버전에선 필요없음!! 따로 등록하지 않아도 자동으로 찾아 사용한다.
    - WebSecurity 설정 : docs와 정적 파일은 웹에서 설정. 시큐리티 필터를 적용하기 전. 따라서 정적 파일을 모두 허용할 거라면 web에서 거르는 게 비용이 더 적음.
    - HttpSecurity 설정 : 시큐리티 필터 안으로 들어옴.

3. 시큐리디 로깅 Debug로 설정하여 메시지 확인
```yml
logging:
  level:
    org.springframework.security=DEBUG
```


```

2024-02-10T00:54:28.148+09:00 DEBUG 4149 --- [nio-8081-exec-5] o.s.security.web.FilterChainProxy        : Securing GET /error
2024-02-10T00:54:28.148+09:00 DEBUG 4149 --- [nio-8081-exec-5] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2024-02-10T00:54:28.150+09:00 DEBUG 4149 --- [nio-8081-exec-5] o.s.s.w.s.HttpSessionRequestCache        : Saved request http://localhost:8081/error?continue to session
2024-02-10T00:54:28.150+09:00 DEBUG 4149 --- [nio-8081-exec-5] s.w.a.DelegatingAuthenticationEntryPoint : Trying to match using And [Not [RequestHeaderRequestMatcher [expectedHeaderName=X-Requested-With, expectedHeaderValue=XMLHttpRequest]], MediaTypeRequestMatcher [contentNegotiationStrategy=org.springframework.web.accept.ContentNegotiationManager@491f3fb0, matchingMediaTypes=[application/xhtml+xml, image/*, text/html, text/plain], useEquals=false, ignoredMediaTypes=[*/*]]]
2024-02-10T00:54:28.150+09:00 DEBUG 4149 --- [nio-8081-exec-5] s.w.a.DelegatingAuthenticationEntryPoint : Match found! Executing org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint@40874f54
2024-02-10T00:54:28.150+09:00 DEBUG 4149 --- [nio-8081-exec-5] o.s.s.web.DefaultRedirectStrategy        : Redirecting to http://localhost:8081/login
2024-02-10T00:54:28.153+09:00 DEBUG 4149 --- [nio-8081-exec-6] o.s.security.web.FilterChainProxy        : Securing GET /login


```


```
2024-02-10T00:58:58.174+09:00  INFO 4208 --- [nio-8081-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2024-02-10T00:58:58.174+09:00  INFO 4208 --- [nio-8081-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2024-02-10T00:58:58.175+09:00  INFO 4208 --- [nio-8081-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2024-02-10T00:58:58.183+09:00 DEBUG 4208 --- [nio-8081-exec-1] o.s.security.web.FilterChainProxy        : Securing GET /docs/index.html
2024-02-10T00:58:58.187+09:00 DEBUG 4208 --- [nio-8081-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2024-02-10T00:58:58.187+09:00 DEBUG 4208 --- [nio-8081-exec-1] o.s.security.web.FilterChainProxy        : Secured GET /docs/index.html
2024-02-10T00:58:58.616+09:00 DEBUG 4208 --- [nio-8081-exec-3] o.s.security.web.FilterChainProxy        : Securing GET /favicon.ico
2024-02-10T00:58:58.618+09:00 DEBUG 4208 --- [nio-8081-exec-3] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2024-02-10T00:58:58.619+09:00 DEBUG 4208 --- [nio-8081-exec-3] o.s.security.web.FilterChainProxy        : Secured GET /favicon.ico
2024-02-10T00:58:58.626+09:00 DEBUG 4208 --- [nio-8081-exec-3] o.s.security.web.FilterChainProxy        : Securing GET /error
2024-02-10T00:58:58.627+09:00 DEBUG 4208 --- [nio-8081-exec-3] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2024-02-10T00:58:58.639+09:00 DEBUG 4208 --- [nio-8081-exec-3] o.s.s.w.s.HttpSessionRequestCache        : Saved request http://localhost:8081/error?continue to session
2024-02-10T00:58:58.639+09:00 DEBUG 4208 --- [nio-8081-exec-3] o.s.s.w.a.Http403ForbiddenEntryPoint     : Pre-authenticated entry point called. Rejecting access


```

### OAuth
- OAuth가 토큰 발급 제공하는 6가지 방법 중에 2가지 방법 사용
    - password, Refresh Token
    - 최초 토큰 발급 시에는 password Granty Type로 토큰을 발급함.
   