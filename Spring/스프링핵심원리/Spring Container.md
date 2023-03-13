## 🍎 스프링 컨테이너

- `ApplicationContext`, ` BeanFactory`가 스프링 컨테이너이다.
- 전에는 개발자가 직접 객체를 생성하고 관리했다면 스프링에서는 스프링 컨테이너가 객체를 생성하고 관리하여 그 객체를 주입받아 사용한다.
- 스프링 컨테이너에서 관리하는 객체를 `스프링 빈`이라고 하며, 의존관계 주입(DI)를 통해 객체들간의 관계를 맺고 관리한다.
- 스프링 컨테이너는 `싱글톤 컨테이너`이다. 객체를 하나만 생성하고 스프링 빈에 등록하여 등록한 이 빈을 공유하며 사용한다.
  - 빈은 공유되기 때문에 `stateless`해야한다.

## 🍎 스프링 컨테이너 생성 과정

### 1. 스프링 컨테이너 생성

```java
ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
```

- AppConfig라는 설정 정보 클래스로 스프링 컨테이너를 생성한다.

### 2. 스프링 빈 등록

- AppConfig에 @Bean이 붙어있는 클래스를 스프링 빈으로 등록한다.

### 3. 스프링 빈끼리의 의존관계 설정

```java
@Configuration
public class AppConfig {
  @Bean
  public MemberService memberService() {
    return new MemberServiceImpl(memberRepository());
  }
  @Bean
  public OrderService orderService() {
    return new OrderServiceImpl(memberRepository(), discountPolicy());
  }
  @Bean
  public MemberRepository memberRepository() {
    return new MemoryMemberRepository();
  }
  @Bean
  public DiscountPolicy discountPolicy() {
    return new RateDiscountPolicy();
  }
}
```

- 매개변수에 스프링 빈 객체를 주입함으로써 빈끼리의 의존관계를 설정한다.

## 🍎 BeanFactory, ApplicationContext

- BeanFactory는 스프링 컨테이너의 최상위 인터페이스이다.
- ApplicationContext는 BeanFactory에 부가 기능을 추가한 것이다.
- 스프링 컨테이너는 다양한 형식의 설정 방식을 지원한다.(xml, Annotation)

| ApplicationContext의 종류 | xml                          | java                                  |
| ------------------------- | ---------------------------- | ------------------------------------- |
| 빈 생성                   | `<bean>`태그 사용            | @Bean 애노테이션 사용                 |
| non-web                   | GenericXmlApplicationContext | AnnotationConfigApplicationContext    |
| web                       | XmlWebApplicationContext     | AnnotationConfigWebApplicationContext |

![s](https://user-images.githubusercontent.com/96059261/211357066-ee9ea127-818c-41ed-97f3-111db0f569cb.PNG)

### ** 스프링은 어떻게 다양한 설정 방식을 지원할까 **

- xml 또는 java 코드인 설정 클래스를 읽어서 `BeanDefinition`을 만들고, 이 `BeanDefinition`으로 스프링 컨테이너를 만든다. 따라서 스프링 컨테이너는 이 `BeanDefinition`만 알면 된다.
- `BeanDefinition`을 빈 설정 메타정보라고 한다.
- `AnnotationConfigApplicationContext`는 `AnnotatedBeanDefinitionReader`를 통해 AppConfig.class를 읽고 `BeanDefinition`을 생성한다.

### ** ApplicationContext 관련 메서드 **

| 메서드                                      | 설명                                               |
| ------------------------------------------- | -------------------------------------------------- |
| getBean("빈 이름")                          | 이름으로 빈 찾아 반환                              |
| getBeanDefinitionNames()                    | 정의된 빈의 이름 배열로 반환                       |
| getBeanDefinitionCount()                    | 정의된 빈의 개수 반환                              |
| containsBeanDefinitionNames("빈 이름")      | 해당 빈이 있나 확인. true/false                    |
| isSingleton("빈 이름")                      | 빈이 싱글톤인지 확인                               |
| isPrototype("빈 이름")                      | 빈이 프로토타입인지 확인                           |
| isTypeMatch("car",Car.class)                | "car"라는 이름의 빈의 타입이 Car인지 확인          |
| findAnnotationOnBean("car",Component.class) | 빈 "car"에 @Component가 붙어있으면 반환            |
| getBeanNamesForAnnotation(Component.class)  | @Component가 붙은 빈의 이름을 배열로 반환          |
| getBeanNamesForType(Engine.class)           | Engine 또는 그 자손 타입인 빈의 이름을 배열로 반환 |

## 🍎 싱글톤 컨테이너

- 스프링 컨테이너는 `싱글톤 컨테이너`이다.
- 어떻게 객체를 하나만 생성하고 관리하는 것일까?  
  ➡︎ `@Configuration`
- `@Configuration`은 설정 정보 클래스 AppConfig.class에 붙인다.
- `@Configuration`에는 @Component가 있어 AppConfig.class도 빈으로 등록한다.

  - `@Configuation`으로 인해 스프링 컨테이너는 설정 정보 클래스를 클래스의 바이트 코드 조작 라이브러리를 통해 빈으로 등록한다.
  - 일반적인 클래스가 아니라 AppConfig를 상속받은 `AppConfig$$CGLIB`가 빈으로 등록된다. 이 CGLIB를 통해 싱글톤을 보장하는 것이다.
  - 따라서 @Configuration을 붙이지 않으면 싱글톤을 보장되지 않는다.  
    **설정 정보는 꼭 @Configuration을 사용하여 싱글톤을 보장해야한다!**

  ```java

  ```
