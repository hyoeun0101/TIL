## 🍎 컴포넌트 스캔

: 자동으로 스프링빈 등록하기

1. 설정 정보 클래스에 `@ComponentScan` 붙이면 컴포넌트 스캔을 시작한다.
   - `@ComponentScan(basePackages="경로")` 작성한 경로의 하위부터 컴포넌트 스캔을 함.
   - 설정 정보 클래스를 프로젝트의 최상단에 두고 basePackages 작성하지 않음.
2. `@Component`가 붙은 클래스를 스프링 빈으로 생성한다.
3. @Autowired가 붙은 생성자의 필드에 의존관계를 주입한다. (by Type. 타입을 기준으로 찾아 주입)

## 🍎 @Autowired 4가지 방법

### 1. 생성자 주입

- `불변, 필수`
- 생성자 시점에 딱 한 번만 호출된다.
- 필드가 final이므로 의존관계 주입을 변경할 수 없다.
- 의존관계는 생성자가 호출될 때 모두 주입된다.
- **생성자 주입을 사용하자!!** 의존관계가 누락되면 컴파일 오류로 잡아줄 수 있을 뿐더러 한 번 생성하면 바꿀 수 없기 때문에 안전하다. 순수 자바 테스트 코드 작성 시에는 스프링 컨테이너 없이 작성 가능하다.
- 생성자가 하나만 있으면 @Autowired 생략 가능.
- 롬복의 `@RequiredArgsConstructor`를 사용하면 final 필드를 모아 생성자를 만들어준다.

```java
@Component
public class OrderServiceImpl implements OrderService {
 private final MemberRepository memberRepository;
 private final DiscountPolicy discountPolicy;
 @Autowired
 public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy
discountPolicy) {
 this.memberRepository = memberRepository;
 this.discountPolicy = discountPolicy;
 }
}
```

### 2. setter 주입

- `변경, 선택`
- setter는 public이므로 setter를 통해 의존관계를 변경할 수 있다. 누군가 실수로 변경할 수도 있기 때문에 좋은 방법은 아니다.
- 필수 값이 아닌 경우만 setter 주입을 사용하고, 필수 값은 생성자 주입을 사용하자.

```java
@Component
public class OrderServiceImpl implements OrderService {
 private MemberRepository memberRepository;
 private DiscountPolicy discountPolicy;
 @Autowired
 public void setMemberRepository(MemberRepository memberRepository) {
 this.memberRepository = memberRepository;
 }
 @Autowired
 public void setDiscountPolicy(DiscountPolicy discountPolicy) {
 this.discountPolicy = discountPolicy;
 }
}
```

### 3. 필드 주입

- 필드는 private이여서 외부에서 변경이 불가능하여 테스트하기 어렵다.
- 거의 사용하지 않는다.
- 컨테이너를 이용하는 통합 테스트를 주로 하는 DAO에서 필드 주입을 해도 문제가 되지 않는다.
- DAO에 DataSource를 DI하는 경우 사용
  - 테스트용 DB의 설정정보로 DataSource 타입의 빈을 등록하고 DAO에서 이걸 주입한다. 테스트 시 스프링 컨테이너에 등록된 DAO의 빈을 사용하므로 DataSource는 필드 주입을 받아 불변으로 만들어 주는게 낫다. 아니면 수정자 주입 사용

```java
@Component
public class OrderServiceImpl implements OrderService {
 @Autowired
 private MemberRepository memberRepository;
 @Autowired
 private DiscountPolicy discountPolicy;
}
```

## 🍎 Autowired 옵션 처리

**DI 할 때 검색된 스프링 빈이 없으면 오류가 발생한다. 스프링 빈이 없어도 동작해야 할 땐 어떻게 하나?**

```java
//1. Autowired(required=false)
// 자동 주입할 스프링 빈이 없으면
//호출 안됨
@Autowired(required = false)
public void setNoBean1(Member member) {
 System.out.println("setNoBean1 = " + member);
}

//2. org.springframework.lang.@Nullable
// 자동 주입할 스프링 빈이 없으면
//null 호출
@Autowired
public void setNoBean2(@Nullable Member member) {
 System.out.println("setNoBean2 = " + member);
}

//3. Optional<>
//자동 주입할 스프링 빈이 없으면
//Optional.empty 호출
@Autowired(required = false)
public void setNoBean3(Optional<Member> member) {
 System.out.println("setNoBean3 = " + member);
}
```

## 🍎 DI 시 빈 충돌

**똑같은 빈이 있다면?**

- 자동 등록한 빈 vs 수동 등록한 빈 => 원래는 수동이 자동을 덮어버리지만 최근엔 오류 발생
- 자동 등록한 빈 vs 자동 등록한 빈 => `ConflictingBeanDefinitionException` 발생

## 🍎 DI 시 빈 중복 - 여러 개 중 하나만 주입하기

**빈 조회 시 여러 개 나오면?**

```java
@Component
public class FixDiscountPolicy implements DiscountPolicy {}
```

```java
@Component
public class RateDiscountPolicy implements DiscountPolicy {}
```

```java
@Autowired
private DiscountPolicy discountPolicy;
```

: RateDiscountPolicy, FixDiscountPolicy 두 개가 조회가 돼서 `NoUniqueBeanDefinitionException` 발생한다. 하위 타입으로 지정할 수도 있지만 이는 DIP를 위반하고 유연성이 떨어진다.

### 1. @Autowired 필드명

- @Autowired는 우선 타입 매칭을 한다.
- 검색된 빈이 여러 개인 경우에는 필드 이름(또는 파라미터 이름)으로 매칭을 한다.

```java
@Autowired
private DiscountPolicy rateDiscountPolicy;
```

- RateDiscountPolicy가 주입된다.

### 2. @Qualifier

- @Qualifier라는 추가적인 정보를 붙인다. 빈 등록할 때와 의존관계 주입할 때 둘다 @Qualifier를 작성하고, 이름이 같은 @Qualifier끼리 매칭한다.
- @Qualifier("mainDiscountPolicy")로 매칭이 안된다면 mainDiscountPolicy라는 이름의 빈을 찾아 주입한다.
- 그래도 찾을 수 없다면 `NoSuchBeanDefinitionException` 발생한다.

```java
@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy{}
```

```java
@Autowired
public OrderServiceImple(@Qualifier("mainDiscountPolicy") DiscountPolicy discoutPolicy){
   this.discountPolicy = discountPolicy;//RateDiscountPolicy가 주입된다.
}
```

### 3. @Primary

- 빈에 우선순위를 부여한다. 우선순위를 줄 스프링 빈에 @Primary를 붙인다.

```java
@Component
@Primary
public class RateDiscountPolicy implements DiscountPolicy{}

@Component
public class FixDiscountPolicy implements DiscountPolicy{}
```

```java
@Autowired
public OrderServiceImple (DiscountPolicy discountPolicy){
   this.discountPolicy = discountPolicy;//RateDiscountPolicy가 주입된다.
}
```

### 빈 중복 시 @Qualifier, @Primary 중 어느 방법을 활용해야할까?

- @Qualifier가 우선권이 높다.
- @Primary는 기본값처럼 동작하므로 기본적인 스프링 빈에 @Primary를 붙이고 상세하게 동작하는 스프링 빈에 @Qualifier를 붙인다.
- 예를 들어 메인 데이터베이스의 커넥션을 획득하는 빈에는 @Primary를 붙여 기본적으로 이 스프링 빈이 조회되도록 한다. 서브 데이터베이스의 커넥션을 획득하는 빈에는 @Qualifier를 지정하여 명시적으로 조회한다.

## 🍎 DI 시 빈 중복 - 여러 개 모두 사용하기

### List, Map 사용하자

```java
class DiscountService{
   private final Map<String, DiscountPolicy> policyMap;

   public DiscountService(Map<String, DiscountPolicy> policyMap){
      this.policyMap = policyMap;
   }
}
```

- policyMap에 RateDiscountPolicy, FixDiscountPolicy 가 담긴다.
