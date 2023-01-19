## 🍎 빈 생명주기 콜백

스프링 컨테이너는 다음과 같은 라이프사이클을 가진다.

```
스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸 전 콜백 -> 종료
```

스프링 빈 생성 후 콜백과 종료 직전에 콜백을 한다.  
데이터베이스 커넥션 풀이나 네트워크 소켓 같은 경우에 애플리케이션 실행 시점에 미리 연결을 해두는 작업이 필요하다. 또한 애플리케이션 종료 시점에 연결을 종료하는 작업을 진행해야한다. 스프링에선 라이프사이클 콜백을 통해 이 작업들을 처리할 수 있다.

## 🍎 빈 생명주기 콜백 방법 3가지

### 1. 인터페이스 구현 - InitailizingBean, DisposableBean

- InitializingBean의 afterPropertiesSet() 메서드를 오버라이딩한다.
  - 빈을 호출한 다음 afterPropertiesSet()이 호출된다.
- DisposableBean의 destroy() 메서드를 오버라이딩한다.
  - 빈이 소멸되기 직전 종료 시점에 destroy()가 호출된다.

```java
public class NetworkClient implements InitializingBean, DisposableBean {
    private String url;

    public NetworkClient(){
        System.out.println("생성자 호출, url= "+url);
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void connect(){
        System.out.println("connect : "+url);
    }
    public void call(String message){
        System.out.println("call: "+url+" message="+message);
    }

    public void disConnect(){
        System.out.println("close="+url);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        connect();
        call("초기화 연결");
    }

   @Override
   public void destroy() throws Exception {
       disConnect();
   }
}
```

➡︎ 이 인터페이스는 스프링 인터페이스라 스프링 전용 인터페이스에 의존적이다.  
➡︎ 메서드 이름 변경을 못한다.  
➡︎ 외부 라이브러리에 적용 못힌디.

### 2. 빈 등록 시 메서드 지정 - @Bean(initMethod = "init", destroyMethod = "close")

- 설정 정보 클래스에 빈 등록 할 때 `@Bean(initMethod = "init", destroyMethod = "close")` 초기화 메서드, 종료 메서드를 지정해주기
- 빈 생성 후에 `NetworkClient`의 `init()` 메서드가 실행되고, 종료 후엔 `close()` 메서드가 실행된다.
- destroyMethod는 디폴트값이 inferred(추론)이다. 생략하면 `close`,`shutdown`이라는 메서드 이름을 추론하여 종료 메서드로 호출한다. 추론 기능을 사용하지 않으려면 `destroyMethod=""`로 지정하면 된다.

```java
@Configuration
static class LifeCycleConfig {
 @Bean(initMethod = "init", destroyMethod = "close")
 public NetworkClient networkClient() {
 NetworkClient networkClient = new NetworkClient();
 networkClient.setUrl("http://hello-spring.dev");
 return networkClient;
 }
}
```

```java
public class NetworkClient {
    ...위와 동일...
    public void init() {
       System.out.println("NetworkClient.init");
       connect();
       call("초기화 연결 메시지");
    }
    public void close() {
       System.out.println("NetworkClient.close");
       disConnect();
    }
}
```

➡︎ 스프링에 의존하지 않는다.  
➡︎ 메서드 이름을 지정할 수 있다.  
➡︎ 코드가 아닌 설정 정보를 사용하기 때문에 외부 라이브러리에도 적용할 수 있다.

### 3. @PostConstruct, @PreDestroy

- 메서드 위에 애노테이션을 붙여주기만 하면 된다.

```java
public class NetworkClient {
    ...위와 동일...
    @PostConstruct
    public void init() {
       System.out.println("NetworkClient.init");
       connect();
       call("초기화 연결 메시지");
    }
    @PreDestroy
    public void close() {
       System.out.println("NetworkClient.close");
       disConnect();
    }
}
```

➡︎ 최신 스프링에서 권장하는 방법.
➡︎ 하지만 외부 라이브러리에는 적용하지 못한다. 외부 라이브러리에 적용할 땐 @Bean 기능을 이용해야한다!

## 🍎 빈 스코프 종류

- singleton
- prototype
- 웹 관련 스코프
  - application
  - request
  - session

**스코프 지정하는 법**

1. 애노테이션

```java
//자동
@Scope("prototype")
@Component
public class HellBean{}

//수동
@Scope("prototype")
@Bean
PrototypeBean HelloBean(){
    return new PrototypeBean();
}
```

### 🍎 프로토타입 스코프

: 싱글톤 스코프는 스프링 컨테이너에 빈을 요청하면 스프링 컨테이너가 미리 생성하여 관리하는 빈을 반환해준다.

- 반면에 프로토타입 스코프는 빈을 요청할 때마다 새로운 객체를 생성을 하고 반환해준다.
- 스프링 컨테이너는 프로토타입 빈의 생성, 의존관계 주입, 초기화까지만 처리하고 생성된 프로토타입 빈을 관리하지 않는다. 프로토타입 빈을 관리할 책임은 빈을 받은 클라이언트에게 있다.
- 따라서 프로토타입 빈의 @PreDestroy는 실행하지 않는다. 종료 메서드에 대한 호출도 클라이언트가 직접 해야한다.

**싱글톤 빈과 프로토타입 빈의 비교**

```java
public class SingletonTest{
    @Test
    public void singletonBeanFind(){
        AnnotationConfigApplicationContext ac  = new AnnotationConfigApplicationContext(SingletonBean.class);

        SingletonBean s1 = ac.getBean(SingletonBean.class);
        SingletonBean s2 = ac.getBean(SingletonBean.class);

        System.out.println("s1 = " + s1);
        System.out.println("s2 = " + s2);
    }
    @Scope("singleton")
    public class SingletonBean{
        @PostConstruct
        public void init(){
            System.out.println("SingletonBean.init");
        }

        @PreDestroy
        public void destroy(){
            System.out.println("SingletonBean.destroy");
        }
    }
}
```

<결과>

```
SingletonBean.init
s1 = hello.core.scope.PrototypeTest$SingletonBean@54504ecd
s2 = hello.core.scope.PrototypeTest$SingletonBean@54504ecd
org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing
SingletonBean.destroy
```

- 스프링 컨테이너 생성할 때 스프링 빈이 생성되어 init이 호출된다.
- 같은 인스턴스의 빈을 조회한다
- 종료 후 destroy가 호출된다.

```java
public class PrototypeTest{
    @Test
    public void prototypeBeanFind(){
        AnnotationConfigApplicationContext ac  = new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean p1 = ac.getBean(PrototypeBean.class);
        PrototypeBean p2 = ac.getBean(PrototypeBean.class);

        System.out.println("p1 = " + p1);
        System.out.println("p2 = " + p2);
    }
    @Scope("prototype")
    public class PrototypeBean{
        @PostConstruct
        public void init(){
            System.out.println("PropotypeBean.init");
        }

        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy");
        }
    }
}
```

<결과>

```
PrototypeBean.init
PrototypeBean.init
p1 = hello.core.scope.PrototypeTest$PrototypeBean@13d4992d
p2 = hello.core.scope.PrototypeTest$PrototypeBean@302f7971
org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing
```

- 빈을 조회할 때마다 새로운 객체를 생성하고, 초기화 메서드가 실행한다.
- 그래서 p1과 p2의 인스턴스는 다르다.
- 종료 후 destroy를 호출하지 않는다.

## 🍎 싱글톤 빈에 프로토타입 빈 주입받을 때 문제점과 해결법

**문제점**

- 싱글톤 빈에 프로토타입 빈을 주입하면 처음에만 생성되기 때문에 계속 같은 프로토타입 빈을 사용하게 된다.
- 프로토타입 빈은 조회를 할 때마다 새로 생성해서 사용하는 것을 원한다!!

**해결법**

- `Dependency Lookup` (DL, 의존관계 조회) : 의존관계를 주입하는 것이 아니라 직접 필요한 의존관계를 조회하는 것.

```java
@Autowired
private ApplicationContext ac;

public void logic(){
    PrototypeBean p = ac.getBean(Prototype.class);// 싱글톤 빈 안에서 프로토타입 빈을 조회한다.
}
```

- `ObjectFactory`, `ObjectProvider` : 스프링에서 제공하는 의존관계 조회 클래스
  - ` prototypeBeanProvider.getObject()`를 통해 새로운 프로토타입 빈을 생성한다.

```java
@Autowired
private ObjectProvider<PrototypeBean> prototypeBeanProvider;

public void logic(){
    PrototypeBean p = prototypeBeanProvider.getObject(Prototype.class);// 싱글톤 빈 안에서 프로토타입 빈을 조회한다.
}
```

- JSR-330 Provider : 자바 표준 사용. `javax.inject:javax.inject:1` 라이브러리 추가해야함.

## 🍎 웹 스코프

- request
- session
- application

request 스코프는 웹 요청이 들어올 때 생성되는 스코프이다. 클래스의 스코프를 request로 지정하면?  
요청이 들어오지 않았을 때, 제대로 동작하지 않는다.  
따라서

```java
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
```

위와 같이 작성하면 CGLIB가 가짜 프록시 객체를 만들어 주입한다. 이를 통해 진짜 개체 조회가 필요한 시점까지 지연처리한다.
