## 🍎Spring Security
- 기본적으로 /login은 시큐리티가 낚아챈다.
### 1. 시큐리티 의존성 추가
### 2. SecurityConfig 생성
- @Configuration, @EnableWebSecurity 붙이기.

```java
//Spring  Security 6 버전부터 바뀜. 
//http 설정
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.GET,"/api/**").anonymous()
                    .anyRequest().authenticated()//권한 필요. 권한이 없으면 로그인 페이지로 이동
                    .requestMatchers("/manager/**").hasRole("MANGER")// 해당 ROLE이 있어야 접근ok. 해당 ROLE이 아니면 403.Forbdden
                    .requestMatchers("/admin/**").hasAnyRole("MANAGER", "ADMIN")
            )
            .formLogin(form -> form //로그인 페이지 등록
                    .loginPage("/loginForm")
                    .loginProcessingUrl("/login")// /login은 시큐리티가 관리함. /login 호출되면 시큐리티가 낚아채서 로그인을 진행한다.
                    .defaultSuccessUrl("/")// 로그인 성공하면 기본적으로 리디렉션할 url
            );

    return http.build();
}
```

- SecurityContextHolder
    - 시큐리티의 세션 저장소라고 보면 됨.
    - ThreadLocal의 구현체로 한 쓰레드 내에서 자원을 공유할 수 있다.
    - 이 안에는 Authentication 타입만 들어갈 수 있다. Authentication 안에는 UserDetails 타입이 들어갈 수 있다. UserDetails에는 사용자 정보가 담겨있다.

### 3. UserDetailsService 생성
```java
@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1. DB에서 해당 유저정보를 검색한다.
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        //UserDetails의 구현체 User를 사용하여 Account를 UserDetails로 변환한다.
        return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
    }

    // Account의 role을 GrantedAuthority로 변환하는 메서드
    private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }
}

```
- 실행 규칙
    - /login 요청이 오면 UserDetailsService 타입으로 IoC되어 있는 빈의 loadUserByUsername 함수가 실행된다.
    - 이 함수에서 반환된 UserDetails는 SecurityContextHolder 안에 저장된다. 
        - SecurityContextHoder[내부 Authentication[내부 UserDetails]]

### 4. UserDetails 구현체 생성
- 이건 선택 사항.

### 5. 특정 url에 권한처리
- SecurityConfig에 @EnableGlobalMethodSecurity 애노테이션 추가
```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(secured=true, prePostEnabled=true) // @Security, @PreAuthorize, PostAuthorize 활성화
class SecurityConfig {...}
```
- 권한을 부여할 특정 메서드에 @Secured, @PreAuthorize 붙이기. 해당 권한이 있어야 url에 접근 가능하다.
```java
@Secured("ROLE_ADMIN")
@GetMapping("/info")
public String getInfo(){...}


@PreAuthorize("hasRole('ROLE_AMDIN') or hasRole('ROLE_MANAGER')")
@GetMapping("/data")
public String getData(){...}
```
- 하나의 권한만 부여하려면 @Secured를 사용하고, 여러 권한을 부여하려면 @PreAuthorize를 사용하라.


## 🍎 OAuth2.0

- Spring Security + OAuth2.0 사용하기

### 1. OAuth2 Client 의존성 추가
```
<!-- https://mvnrepository.com/artifact/org.springframework.security/spring-security-oauth2-client -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-client</artifactId>
    <version>6.2.3</version>
</dependency>

```
### 2. 구글에 클라이언트 등록
- 서비스마다 등록 방법 다르니 찾아볼 것.
- 정보 3개 : client id, client secret, redirect url 

### 3. 


- http://localhost:8080/login/oauth2/code/google
    - `/login/oauth2/code/` 이 부분은 oauth2-client 라이브러리에서 고정한 값임.





구글 로그인
href="/oauth2/authorization/google"

