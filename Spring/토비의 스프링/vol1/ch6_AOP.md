## 정리

1. 트랜잭션 경계설정 코드 분리하기.
   - DI를 이용한 트랜잭션의 분리는 `데코레이터 패턴`, `프록시 패턴`으로 이해될 수 있다.
2. 다이내믹 프록시 사용하기.
3. 팩토리 빈으로 다이내믹 프록시를 빈으로 등록하기. 스프링에선 ProxyFactoryBean을 제공한다.
4. ProxyFactroyBean 설정의 반복되는 문제를 해결하기 위헤 자동 프록시 생성기와 포인트컷을 활용할 수 있다.
   - 자동 프록시 생성기는 부가 기능이 담긴 Advise를 제공하는 프록시를 빈 초기화 시점에 생성한다.
5. 포인트컷은 AspectJ 포인트컷 표현식을 사용하면 편리하다.
6. AOP는 OOP만으로 모듈화하기 힘든 부가기능을 효과적으로 모듈화해주는 기술이다.
7. AOP를 이용해 트랜잭션 속성을 지정하는 방법
   - 포인트컷 표현식
   - 메소드 이름 패턴을 이용하는 방법
   - 타깃에 @Transactional 애노테이션 붙이는 방법

---

## 1. 트랜잭션 코드 분리하기

### 코드 분리하는 법

1. 메서드로 분리
2. 클래스로 분리
3. 인터페이스 도입
4. DI 받기

- UserServiceImpl의 allUsersUpgradeLevel()에 트랜잭션 경계설정 기능 부가하기.
  - UserServiceTx에서 위임, 부가기능 추가를 해준다.

```java

@Service
public class UserServiceImpl implements UserService{
    private  UserDao userDao;
    private MailSender mailSender;

    /**
     * 트랜잭션 경계설정은 UserServiceTx로 분리하기.
     */
    @Override
    public void allUsersUpgradeLevel(){
        List<User> users = userDao.getAll();
        for(User user: users) {
            if(checkEnableLevelUp(user)) {
                upgradeLevel(user);

            }
        }
    }
    @Override
    public void update(User user) {
        userDao.update(user);

    }

}
```

```java
@Service
public class UserServiceTx implements UserService{
    UserService userService;
    PlatformTransactionManager transactionManager;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager
    }
    @Override
    public void allUsersUpgradeLevel(){
        // 메서드 구현, 부가기능 추가
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            userService.allUsersUpgradeLevel();
            transactionManager.commit(status);
        } catch(RuntimeException e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void update(User user) {
        userService.update(user);
    }

    @Override
    public List<User> getAllUser() {
        return userService.getAllUser();
    }
}

```

- 이렇게 같은 인터페이스(UserService)로 부가기능(UserServiceTx)과 핵심기능(UserServiceImpl)을 나눈 코드는 `프록시 패턴` 및 `데코레이터 패턴`을 나타낸다.

## 2. 프록시

- 클라이언트로부터 요청을 받아 대신 처리하며 보조 코드를 가진 클래스를 `프록시`라 하고, 프록시를 통해 최종 핵심 기능을 수행하는 클래스를 `타겟`이라고 한다.
- 프록시는 타겟의 인터페이스와 동일하며, 프록시를 통해 타겟을 제어한다.
- 정리하자면, 프록시 사용 목적은 다음 두 가지이다.
  1. 타겟에게 부가 기능을 추가하기 위해
  2. 타겟의 접근을 제어하기 위해

### 데코레이션 패턴

- 타깃에 부가 기능을 추가하기 위해 프록시를 사용하는 디자인 패턴.
- 프록시에 타겟이나 다른 프록시를 DI한다.
- 프록시와 타겟은 같은 인터페이스로 구현되어 있어 클라이언트, 타겟의 코드를 변경할 필요 없다.
- ex) BufferedInputStream : 프록시, FileInputStream: 타겟

### 프록시 패턴

- 타깃의 접근 제어를 위해 프록시를 사용하는 디자인 패턴.
- 필요한 시점에 객체를 생성할 때 사용한다.

- 프록시 패턴의 프록시는 클라이언트가 타겟에 접근하는 방식을 변경해준다. 즉 타겟의 접근 제어의 역할을 가진다.
- 많은 로직을 거쳐 타깃 오브젝트를 필요 시점에 생성하는 것이 좋은데, 이 때 프록시 패턴을 적용한다.
- 클라이언트에서 실제 타겟 오브젝트를 만드는 대신 프록시를 만들어 넘겨주는 것이다.
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
➡︎ But 타겟마다 ProxyFactoryBean을 설정해줘야 하는 번거로움이 아직 남아있음.

### Pointcut의 역할 두 가지

```java
public interface Pointcut {
    ClassFilter getClassFilter(); // 프록시를 생성할 클래스인가 확인
    MethodMatcher getMethodMatcher(); // Advice를 적용할 메소드인가 확인
}
```

## 6. 빈 후처리기(자동 프록시 생성), 포인트컷

- 빈 후처리기를 사용하여 **ProxyFactoryBean의 설정없이** 자동으로 프록시를 생성하로독 하자.
- 빈 후처리기 : `BeanPostProcessor` 인터페이스의 구현체를 빈으로 등록하면 된다.
- 트랜잭션 부가기능을 어디에 적용할지 정하는 포인트컷을 통해 독립적으로 분리할 수 있다.

### 빈 후처리기 동작 방법

1. 컨테이너가 빈을 생성하여 빈 후처리기에게 넘겨준다.
2. 빈 후처리기는 등록된 빈 중 Advisor 구현체를 찾아 모든 빈에 Advisor의 Pointcut을 적용한다.
3. 포인트컷을 통해 프록시 적용 대상인지 확인하고, 대상이면 프록시 생성기를 통해 프록시를 생성한다. 이 때, Advisor와 연결한다.
   - 해당 클래스가 프록시 적용 대상인지 확인할 때는 인터페이스 타입으로 확인하는 것이 좋다.
4. 생성한 프록시를 컨테이너에게 반환하고, 컨테이너는 원래 빈 대신 이 프록시를 빈으로 등록하여 사용한다.