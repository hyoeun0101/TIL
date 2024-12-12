- 현재 API의 문제점 : 아무나 /api/evnets를 조회, 생성할 수 있다.
- [POST] /api/events 생성과 수정은 권한이 있는 사용자만이 할 수 있다.

- 인증 시스템 도입: OAuth2를 활용한 Spring Security , Password 그랜트 타입?을 사용


### 1. Account 클래스 생성
```java
package com.example.demorestapi.accounts;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    //fetch의 기본값은 lazy
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}

``` 
### 2. Spring Security란?
## 🍎 Spring Security
### 시큐리티의 종류
- 웹 시큐리티 : 웹 요청에 인증 적용 . filter 기반 시큐리티. SecurityFilterChain을 사용한다.
- 메소드 시큐리티 : 메서드가 호출됐을 때 권한을 확인하는 방식. AOP 기반

- 둘 다 SecurityInterceptor를 사용하여 시큐리티 기능을 제공한다.
- SecurityInterceptor의 구현체 : FilterSecurityInterceptor, MethodSecurityInterceptor
+ spring 5부터 webflux와 Servlet 기반의 웹 두 가지로 나눔.
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
### 의존성 추가
```xml
<dependency>
    <groupId>org.springframework.security.oauth.boot</groupId>
    <artifactId>spring-security-oauth2-autoconfigure</artifactId>
    <version>2.1.0.RELEASE</version>
</dependency>

```


# Spring Security OAuth2 설정 : 인증 서버 설정
- Password Grant Type : 홉이 한번임. 즉 통신이 한번.