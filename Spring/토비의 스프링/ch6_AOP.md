## 1. 트랜잭션 코드 분리하기

### 코드 분리하는 법

1. 메서드로 분리
2. 클래스로 분리.
3. 인터페이스 도입
4. DI 받기

## 2. 프록시

- 핵심 코드를 가진 클래스를 타겟이라고 하고, 보조 코드를 가진 클래스는 프록시라고 한다.
- 프록시 사용은 두 가지가 있다. 타겟에게 부가 기능을 추가하기 위해, 타겟의 접근을 제어하기 위해.

### 데코레이션 패턴

- 타겟에게 부가 기능을 추가하는 프록시를 사용하는 것은 데코레이션 패턴이다.

```java
InputStream is = new BufferedInputStream(new FileInputStream("a.txt"));
// BufferedInputStream은 데코레이션 프록시, FileInputStream은 타켓이다.
```

### 프록시 패턴

- 타겟의 접근 제어하는 프록시를 사용하는 것은 프록시 패턴이다.
- ex) Collections의 unmodifiedCollection()

### 프록시의 단점

- 프록시의 메서드마다 위임하는 코드를 작성해야 한다.
- 부가 기능을 추가할 때마다 프록시 클래스를 생성하며, 부가 기능 코드가 중복한다.

## 3. 다이내믹 프록시

- 다이내믹 프록시는 `Reflection`을 통해 프록시를 생성한다.
- 다이내믹 프록시는 부가 기능과 위임 코드를 담은 InvocationHandler 구현체를 호출한다. InvocationHandler의 invoke()를 통해 타깃을 호출하며, 그 사이에 부가 기능을 추가할 수 있다.

### 다이내믹 프록시 생성하는 법

```java
Hello proxiedHello = (Hello) Proxy.newProxyInstance(
        getClass().getClassLoader(),// 클래스 로더 제공
        new Class[]{Hello.class},// 구현할 인터페이스
        new UppercaseHandler(new HelloTarget()) // 부가기능, 위임 코드를 담은 InvocationHandler, 핸들러에게 타깃을 제공.
    )

```

- 하나의 클래스의 메소드의 공통 부가 기능을 한 번에 제공한다.

## 4. 팩토리 빈

- 스프링을 대신해서 오브젝트의 생성을 담당하는 빈이다.
- `팩토리 빈`을 통해 `다이내믹 프록시`를 `빈으로 등록`하여 사용할 수 있다.
- FactoryBean 인터페이스를 통해 구현한다.
- 팩토리 빈을 빈으로 등록하면, 팩토리 빈의 getObject() 메소드의 반환 타입이 빈으로 등록된다.

```java
@Bean
public MessageFactoryBean message() {
    MessageFactoryBean messageFactoryBean = new MessageFactoryBean();
    messageFactoryBean.setText("Factory Bean");
    return messageFactoryBean;
}
```

```java
@SpringBootTest
public class FactoryBeanTest {

    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("빈으로 등록된 MessageFactoryBean이 Message 타입인지 확인하기")
    public void getMessageFromFactoryBean() {
        Object message = context.getBean("message");
        assertThat(message.getClass()).isEqualTo(Message.class);
        assertThat(((Message)message).getText()).isEqualTo("Factory Bean");
    }

    @Test
    @DisplayName("&기호를 붙여주면 FactoryBean 자체를 반환한다.")
    public void returnFactoryBean() {
        Object factory = context.getBean("&message");
        assertThat(factory.getClass()).isEqualTo(MessageFactoryBean.class);
    }
}

```

### 팩토리 빈을 통해 다이내믹 프록시를 빈으로 등록

```java
    // 빈의 이름은 userService, 타입은 UserService.class이다.
    @Bean
    public TxProxyFactoryBean userService() {
        TxProxyFactoryBean factoryBean = new TxProxyFactoryBean();
        // 다이내믹 프록시에게 넘겨주기 위한 타겟의 정보 필요
        factoryBean.setTarget(userServiceImpl);
        factoryBean.setTransactionManager(transactionManager());
        factoryBean.setPattern("allUsers");
        factoryBean.setServiceInterface(UserService.class);
        return factoryBean;

    }
```

- `TxProxyFactoryBean`의 getObject는 `다이내믹 프록시`를 반환한다.
  - 다이내믹 프록시는 핸들러의 invoke()를 호출한다.
  - invoke()에서는 부가 기능 추가, 타깃에게 위임을 한다.
- 빈으로 등록한 `팩토리 빈의 타입`은 `타깃 인터페이스`이다.

### 한계

1. 부가 기능이 타겟 오브젝트마다 새로 만들어진다.
2. 부가 기능을 적용할 타겟마다 그 타겟을 생성하는 ProxyFactoryBean을 빈으로 등록해야한다.
   - 즉, PoxyFactoryBean : 타겟 = 1 : 1

## 5. ProxyFactoryBean

- 스프링에서 제공하는 프록시 팩토리 빈.
- MethodInterceptor 구현체인 `Advice`로 순수 부가 기능을 제공한다.
  - 타깃 정보를 가지지 않아서 여러 프록시에 공유 가능하다.
  - 콜백 proceed()를 호출해서 타깃 메소드를 실행한다.
- `Advisor` = `advice`(순수 부가 기능) + `pointcut`(부가 기능을 제공할 메소드 선정)
- Advice, Pointcut, Advisor를 빈으로 등록한 다음, ProxyFactoryBean에 Advisor를 등록한다.
- Advice와 Pointcut : Advisor : ProxyFactoryBean : 타깃 = 1 : n : m : m

➡︎ Advice를 사용함으로써 부가 기능이 타겟 오브젝트마다 새로 만들어지는 한계를 해결.

### Pointcut의 역할 두 가지

```java
public interface Pointcut {
    ClassFilter getClassFilter(); // 프록시를 생성할 클래스인가 확인
    MethodMatcher getMethodMatcher(); // Advice를 적용할 메소드인가 확인
}
```

## 6. 빈 후처리기

- 빈 후처리기를 사용하여 ProxyFactoryBean의 설정없이 자동으로 프록시를 생성하로독 하자.
- 빈 후처리기 : `BeanPostProcessor` 인터페이스의 구현체를 빈으로 등록하면 된다.

### 빈 후처리기 동작 방법

1. 컨테이너가 빈을 생성하여 빈 후처리기에게 넘겨준다.
2. 빈 후처리기는 등록된 빈 중 Advisor 구현체를 찾아 모든 빈에 Advisor의 Pointcut을 적용한다.
3. 프록시 적용 대상이면 프록시 생성기를 통해 프록시를 생성한다. 이 때, Advisor와 연결한다.
4. 생성한 프록시를 컨테이너에게 반환하고, 컨테이너는 원래 빈 대신 이 프록시를 빈으로 등록하여 사용한다.
