
## 🍎 DI 시 빈 충돌
- 똑같은 빈이 여러 개 있다면?
    - 자동 등록한 빈 vs 수동 등록한 빈 => 원래는 수동이 자동을 덮어버리지만 최근엔 오류 발생
    - 자동 등록한 빈 vs 자동 등록한 빈 => `ConflictingBeanDefinitionException` 발생

## 🍎 DI 시 빈 중복 - 여러 개 중 하나만 주입하기
- 빈 조회 시 여러 개 나오면?

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

- DiscountPolicy를 주입할 때 RateDiscountPolicy, FixDiscountPolicy 두 개가 조회가 돼서 `NoUniqueBeanDefinitionException`이 발생한다.
- 하위 타입으로 DI할 수도 있지만 이는 DIP를 위반하고 유연성이 떨어지므로 하위 타입으로 정의해선 안된다.

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
