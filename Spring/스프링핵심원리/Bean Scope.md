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

- 싱글톤 스코프는 스프링 컨테이너에 빈을 요청하면 스프링 컨테이너가 미리 생성하여 관리하는 빈을 반환해준다.
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

### 문제점

- 싱글톤 빈에 프로토타입 빈을 주입하면 처음에만 생성되기 때문에 계속 같은 프로토타입 빈을 사용하게 된다.
- 싱글톤 빈에 주입된 프로토타입 빈은 조회를 할 때마다 새로 생성해서 사용하는 것을 원한다!!

### 해결법

1. `Dependency Lookup` (DL, 의존관계 조회) : 의존관계를 주입하는 것이 아니라 직접 필요한 의존관계를 조회하는 것.
   - 싱글톤 빈에서 스프링 컨테이너에서 프로토타입 빈을 조회한 후 사용한다. 프로토타입 빈은 조회할 때마다 새로운 객체를 생성한다.
   - 하지만 이렇게 스프링 애플리케이션 컨텍스트 전체를 주입받게 되면, 스프링 컨테이너에 종속적이게 되고, 단위 테스트를 하기 어려워진다.

```java
    @Test
    @DisplayName("싱글톤 빈에 프로토타입 빈을 주입할 때 프로토타입 빈이 새로 생성되길 원한다")
    void SingletonBean_DI_PrototypeBean_Test() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class,ClientBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        System.out.println(count1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        System.out.println(count2);
    }

    @DisplayName("프로토타입 빈을 주입받는 싱글톤 빈")
    static class ClientBean {
        @Autowired
        private ApplicationContext ac;

        public int logic() {
            // login()을 호출할 때마다 스프링 컨테이너에서 새로 조회한다.
            PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
            prototypeBean.addCount();
            return prototypeBean.getCount();

        }
    }

    @DisplayName("프로토타입 빈")
    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public int getCount() {
            return count;
        }

        public void addCount() {
            count++;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean init: "+this);
        }

        // 프로토타입 빈의 PreDestroy는 호출되지 않는다.
        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean destroy");
        }
    }
```

2. 스프링에서 제공하는 DL의 기능 : `ObjectProvider` 사용하기

```java
@Autowired
private ObjectProvider<PrototypeBean> prototypeBeanProvider;

public void logic(){
    PrototypeBean p = prototypeBeanProvider.getObject(PrototypeBean.class);// 싱글톤 빈 안에서 프로토타입 빈을 조회한다.
}
```

- ObjectProvider의 getObject()를 호출하면 스프링 컨테이너에서 빈을 찾아 반환한다.
- 위의 `prototypeBeanProvider.getObject()`를 호출하면 새로운 프로토타입 빈을 생성하고 반환한다.
- 단위 테스트를 만들거나 mock 코드를 만들기 쉬워진다.
- ObjectFactory : 단순한 기능, 스프링에 의존, 라이브러리 필요X
- ObjectProvider : ObjectFactory 상속, 많은 편의 기능, 라이브러리 필요, 스프링에 의존

3. javax.inject.Provider (JSR-330 자바 표준)사용하기

- `javax.inject:javax.inject:1` 라이브러리 gradle에 추가.

```java
@Autowired
private Provider<PrototypeBean> provider;

public int logic() {
    PrototypeBean prototypeBean = provider.get();
    prototypeBean.addCount();
    return prototypeBean.getCount();
}
```

- provider.get()을 호출하면 항상 새로운 프로토타입 빈이 생성되어 반환된다.
- 자바 표준이고, 기능이 단순해서 단위테스트를 만들거나 mock 코드를 만들기 쉽다.

### 정리

- 프로토타입 빈은 언제 사용할까?
  - 웹 애플리케이션은 대부분 싱글톤 빈으로 문제를 해결할 수 있기 때문에 프로토타입 빈을 직접 사용하는 일은 드물다.
  - 매번 새로운 객체가 필요할 때 프로토타입 빈을 사용한다.

## 🍎 웹 스코프

- 웹 스코프는 웹 환경에서만 동작한다.

### 웹 스코프의 종류

- `request` : HTTP 요청이 들어오고 나갈 때까지 유지되는 스코프. HTTP 요청마다 별도의 빈 인스턴스가 생성된다.
- `session` : HTTP Session과 동일한 생명주기를 가진다. 클라이언트 당 1개
- `application` : 서블릿 컨텍스트와 동일한 생명주기를 가진다. 애플리케이션 당 1개
- `websocket` : 웹 소켓과 동일한 생명주기를 가진다.

request 스코프는 웹 요청이 들어올 때 생성되는 스코프이다. 클래스의 스코프를 request로 지정하면?  
요청이 들어오지 않았을 때, 제대로 동작하지 않는다.  
따라서

```java
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
```

위와 같이 작성하면 CGLIB가 가짜 프록시 객체를 만들어 주입한다. 이를 통해 진짜 개체 조회가 필요한 시점까지 지연처리한다.
