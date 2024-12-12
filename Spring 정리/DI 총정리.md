## 🔴 의존성 주입(DI) 어노테이션 총정리


### 1. @Autowird
- by Type. 타입으로 찾아서 주입

- 생략 가능.언제? 

### 2. @Resource
- by name. 이름으로 찾아서 주입

### 3. @Qualifier
- 빈 충돌 시 사용.


## 🔴 의존성 주입(DI)하는 방법 4가지

### 1. 생성자 주입

- `불변, 필수`

- 생성자 시점에 딱 한 번만 호출된다.

- 필드가 final이므로 의존관계 주입을 변경할 수 없다.

- 의존관계는 생성자가 호출될 때 모두 주입된다.

- 생성자 주입을 사용하자!!
    - 객체를 생성할 때 필수적으로 의존관계를 넣어줘야 하기 때문에 오류를 줄여준다. 또한 한 번 생성하면 의존관계를 바꿀 수 없기 때문에 안전하다. 
    - 순수 자바 테스트 코드 작성 시에는 스프링 컨테이너 없이 작성 가능하다.

- 생성자가 하나만 있으면 @Autowired 생략 가능.

- 롬복의 `@RequiredArgsConstructor`를 사용하면 final 필드를 모아 생성자를 만들어준다.

```java
@Component
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    
    @Autowired // 생략가능
    public OrderServiceImpl (@Autowired MemberRepository memberRepository, DiscountPolicy discountPolicy) {
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