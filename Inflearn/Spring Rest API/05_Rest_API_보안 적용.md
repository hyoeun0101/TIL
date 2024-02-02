@ElmentCollection()
기본값 fetch lazy

## 🍎 Spring Security
### 시큐리티의 종류
- 웹 시큐리티 : filter 기반 시큐리티. SecurityFilterChain을 사용한다.
- 메소드 시큐리티 : 메서드가 호출됐을 때 권한을 확인하는 방식. AOP 기반

- 둘 다 SecurityInterceptor를 사용하여 시큐리티 기능을 제공한다.
- SecurityInterceptor의 구현체 : FilterSecurityInterceptor, MethodSecurityInterceptor

### 동작 방식

1. 요청이 오면 servlet filter가 SecurityFilter를 적용해야하는지 즉, 인증이 필요한지 체크한다.
2. 
2. 인증 정보가 없는 경우: AuthenticationManager를 통해 로그인하면 인증 정보를 얻고, 그 인증 정보를 SecurityContextHolder에 저장한다.
    - SecurityHolder는 ThreadLocal의 구현체이다. ThreadLocal은 한 쓰레드 내에서 자원을 공유할 수 있다. 
3. 인증 정보가 있는 경우: 



securityInteceptor 호출. 여기서 인증해야하는지(SecuriyFilter를 적용해야하는지) 체크.
2. 인증정보 확인 : SecurityContextHolder(TreadLocal를 구현)에서 인증정보를 꺼내려고 시도. 인증정보 없으면
-> AuthenticationManager를 사용해서 로그인. AuthenticationManager를 통해 얻은 인증 정보를 SecurityContextHolder에 저장.

인증이 됐다면 권한이 적절한지 확인 (AccessDecisionManager) User의 role로. 
- AuthenticationManager가 로그인할 때 사용하는 인터페이즈 : passwordEncoder, UserDeatailService


Servlet fiterChain을 자동으로 구성하고 거침.  


webFilterSecurityInterceptor에서 인증이 필요한지
이 요청에 security filter를 적용해야한다면 : 인증 정보 확인. holder(인메모리)에서 인증정보를 꺼내려고 함. 
인증 정보가 없다. -> authenticationManager를 통해 로그인. -> 로그인 성공적 -> Authentication : 인증 정보. 얘를 holder에 저장.

AuthenticationManager가 사용하는 인터페이스 : UserDetailService, passwordEncoder
