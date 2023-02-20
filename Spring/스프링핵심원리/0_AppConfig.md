```java
package hello.core.member;

public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
```

### 문제점

- OCP 위반 : MemberRepository 코드 변경이 불가피함.
- DIP 위반 : 의존관계가 인터페이스뿐만 아니라 구현 객체까지 의존하고 있음
- MemberServiceImpl에서 저장소를 Jdbc 저장소로 변경하려면?

```java
private final MemberRepsitory memberRepsitory = new JdbcMemberRepository();
```

- MemberServiceImpl의 코드 변경이 필요하다.

**구현 객체와의 의존을 없애고 인터페이스와만 의존 관계를 맺도록 하여 DIP, OCP를 지키자.**

```java
package hello.core.member;

public class MemberServiceImpl implements MemberService{

    private MemberRepository memberRepository;

    //생성자 주입
    public MemberServiceImpl(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
```

- 생성자를 통해 MemberRepository의 구현 객체를 주입받는다.
- OCP 준수 : MemberRepository를 외부에서 주입받으므로 코드를 변경할 필요가 없음.
- DIP 준수 : MeberRepository 인터페이스에만 의존하고 있음.

**그럼 객체는 어디서 생성되어 주입되는 것일까?**

## 🍎 AppConfig

```java
public class AppConfig {
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

}
```

```java
//AppConfig 테스트
public class JoinApp {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        MemberService memberService = appConfig.memberService();
        //회원가입할 member 생성
        long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);
 }
}
```

- AppConfig 설정 정보 클래스에서 객체를 관리한다.
- MemberServiceImpl은 이제부터 의존관계에 대한 고민은 외부에 맡기고 실행에만 집중하면 된다.
- 이제 MemberRepository를 변경해도 애플리케이션의 구성 역할을 담당하는 AppConfig만 변경하면 된다.
- AppConfig는 애플리케이션의 전체 동작 방식을 구성(config)하기 위해, 구현 객체를 생성하고, 연결하는
  책임을 가진다. 프로그램에 대한 제어 흐름에 대한 권한은 모두 AppConfig가 가지고 있다
- AppConfig와 같이 프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 관리하는 것을 `제어의 역전(IoC)`이라
  한다.

## 🍎 @Configuration, @Bean

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
```

- AppConfig에 설정을 구성한다는 뜻의 @Configuration 을 붙여준다.
- 각 메서드에 @Bean 을 붙여준다. 이렇게 하면 스프링 컨테이너에 스프링 빈으로 등록한다.
- 스프링 컨테이너는 @Configuration 이 붙은 AppConfig 를 설정 정보로 사용한다. 여기서 @Bean
  이라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다. 이렇게 스프링 컨테이너에
  등록된 객체를 스프링 빈이라 한다.
- 기존에는 개발자가 직접 자바코드로 모든 것을 했다면 이제부터는 스프링 컨테이너에 객체를 스프링 빈으로
  등록하고, 스프링 컨테이너에서 스프링 빈을 찾아서 사용하도록 변경되었다
