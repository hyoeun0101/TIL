
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
