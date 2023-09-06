## 🍎 IoC 컨테이너

- IoC 컨테이너를 만들려면 `POJO 클래스`, `설정 메타정보` 이 두 가지가 필요하다.
- POJO 클래스는 인터페이스를 구현하게 하여 유연하게 만든다.
- 설정 메타정보는 빈을 어떻게 만들고, 어떻게 동작할 것인지에 대한 정보이다.

### IoC 컨테이너의 설정 메타 정보는 BeanDefinition 인터페이스를 구현하여 만들어진다.

<img src="https://github.com/PSVM2022/Dopamin/assets/96059261/bc3c09ee-f72c-48db-86ec-71eb95b399cf" width="350"/>

- 메타 정보(빈 아이디,이름, 클래스 이름, 스코프, 프로퍼티 값 등)를 BeanDefinition 구현체에 정의하여 사용한다.
- `BeanDefinitionReader`는 원본의 설정 메타 정보를 읽어와, `BeanDefinition` 오브젝트로 변환해준다.
- 이 BeanDefinition 인터페이스 덕분에 자바는 설정 메타정보의 형태에 종속적이지 않게 되었다.
- 여러 개의 설정 메타 정보를 등록할 수 있는데, 여러 설정 메타 정보에서 같은 클래스를 빈으로 등록하더라도 다른 오브젝트를 갖는다.
  - 예를 들어 A,B라는 두 개의 설정 메타 정보에서 Hello.class를 빈으로 등록했다면, A의 Hello의 객체와 B의 Hello의 객체는 다르다.

## 🍎 IoC 컨테이너의 종류 - 스프링이 제공하는 ApplicationContext 구현체

- `StaticApplicationContext` : 거의 학습용.테스트 목적
- `GenericApplicationContext` : 직접 다룰 일은 없다. 다만 Jnit 테스트가 자동으로 만들어주는 스프링 테스트 애플리케이션 컨텍스트가 바로 이것이다.
- `GenericXmlApplicationContext` : XmlBeanDefinitionReader를 내장하고 있어서 BeanDefinitionReader를 따로 정의하지 않아도 된다.
- `WebApplicationContext` : 웹 환경에서 필요한 기능이 추가됨.

![ㅍ](https://github.com/PSVM2022/Dopamin/assets/96059261/d41f706d-c50d-4abc-878d-d332e59b8935)

## 🍎 IoC 컨테이너 계층 구조 - 부모 컨텍스트, 자식 컨텍스트

- 웹 애플리케이션이 생성하는 컨텍스트 : root ApplicationContext
  - 주로 공통된 빈을 다룸
- 서블릿이 생성하는 컨텍스트 : 자식 ApplicationContext
- 스프링이 컨텍스트를 실행할 때, 우선 자식 컨텍스트에서 찾은 다음 등록된 빈이 없으면 부모 컨텍스트에서 찾는다. 즉, 자식 우선.
- 주의할 점 : 컨텍스트의 실행 방향은 오로지 자식에서 부모로만 간다. 형제나 자식으로 가지 않는다!
- 이렇게 컨테이너를 계층구조로 만드는 이유는 웹 기술에 의존적인 부분과 그렇지 않은 부분을 구분하기 위해서이다.

```java
// 자식 컨텍스트 생성하는 법
GenericApplicationContext child = new GenericApplicationContext(parent);
```

## 🍎 웹 어플리케이션의 컨텍스트 구성 방법

1. 서블릿 컨텍스트, 루프 애플리케이션 컨텍스트

- 웹 관련 빈은 서블릿 컨텍스트에두고 나머지는 루트 애플리케이션 컨텍스트에 등록.

2. 루트 애플리케이션 컨텍스트 단일구조

- 스프링 웹 기술을 사용하지 않고 만들땐 루트 애플리케이션 컨텍스트만 필요

3. 서블릿 컨텍스트 단일구조

- 계층구조를 사용하면서 발생할 수 있는 혼란을 피한 단순한 설정 방법

## 🍎 루트 애플리케이션 컨텍스트 등록하기 - ContextLoaderListener

- 서블릿의 이벤트 리스너를 이용하자.
- 웹 애플리케이션의 시작과 종료 시 발생하는 이벤트를 처리하는 `ServeletContextListener` 인터페이스를 구현한다.
- `ContextLoaderListener` : 웹 애플리케이션이 시작할 때 루트 애플리케이션 컨텍스트를 생성 및 초기화한다. 또 종료할 땐 컨텍스트를 함께 종료시킨다. 이를 web.xml에 등록해서 사용한다.

```xml
<listener>
  <listener-class>org.springframework.web.context.ContextLoaderListener
  </listener-class>
</listener>
```

- `ContextLoaderListener`는 XmlWebApplicationContext를 만들고, /WEB-INF/applicationContext.xml 파일을 디폴트 설정파일로 사용한다.
- 다음과 같이 파일 위치를 지정, 컨텍스트 클래스 변경을 할 수 있다.

```xml
<context-param>
  <param-name>contextConfigLocation</param-name>
  <param-value>
    /WEB-INF/daoContext.xml
    /WEB-INF/applicationContext.xml
    <!-- 또는 classpath:applicationContext.xml -->
  </param-value>
</context-param>

<context-param>
  <param-name>contextClass</param-name>
  <param-value>
    org.springframework.web.context.support.AnnotationConfigWebApplicationContext
  </param-value>
</context-param>
```

## 🍎 서블릿 애플리케이션 컨텍스트 등록하기

- 스프링의 웹 기능을 지원하는 프론트 컨트롤러 서블릿은 DispatcherServlet

## @Configuration 없는 클래스의 @Bean

- 알다시피 @Configuration은 빈을 싱글톤으로 만들어 주므로 @Configuration이 없는 클래스의 @Bean은 싱글톤이 아니다.
- @Configuration이 없는 클래스의 @Bean은 언제 사용하나?
  - @Bean 메소드를 통해 정의되는 빈과 그 메소드를 감싸는 클래스로 만들어지는 빈이 밀접한 관계가 있을 때
  - @Bean 메소드는 클래스 내부에 있으므로 클래스의 모든 정보에 접근이 가능하다.
  - 설정 정보 등을 공유할 수 있는 동시에 외부로 빈의 존재가 노출되지 않는다.(물론 일반 빈으로 등록되므로 참조 가능하지만 싱글톤이 아님을 염두에 둘 것)
  - 밀접한 의존관계를 갖는 종속적인 빈을 정의할 때 유용하지만, 설정정보와 일반 애플리케이션 코드가 같이 있어 유연성이 떨어진다.

## 핵심 정리

- 스프링 애플리케이션은 설정 메타정보와 POJO 클래스로 구성된다.
- 빈 설정 메타정보는 특정 파일에 종속되지 않는다. 필요하다면 새로운 포맷을 만들어 사용할 수 있다. (BeanDefinition 인터페이스 구현하여)
- 빈 등록방법은 xml, 자동인식, 자바 코드가 있다.
- 빈 의존관계 설정 방법은 xml, 애노테이션, 자바 코드가 있다.
- 프로퍼티 값은 정보다. 환경에 따라 자주 바뀌는 프로퍼티 값은 별도의 파일로 분리해놓아야 한다.
- 빈의 스코프는 싱글톤, 프로토타입, 기타 스코프가 있다.
- 기타 스코프 빈은 DL 방식을 사용하거나, 스코프 프록시 빈을 DI 받는 방법을 사용한다.

## 1. IoC 컨테이너 : 빈 팩토리와 애플리케이션 컨텍스트

- 스프링에서는 객체를 생성, 삭제 등의 관리를 컨테이너가 한다. 객체의 제어를 코드에서 하는 게 아니라 컨테이너가 하는 것이다. 이를 제어의 역전이라고 하여 IoC 컨테이너라고 한다.
- 스프링의 IoC 컨테이너는 빈 팩토리, 애플리케이션 컨텍스트라고 한다.

- 스프링의 컨텍스트는 ApplicationContext로

### IoC 컨테이너로 애플리케이션 만들기

### IoC 컨테이너의 종류와 사용 방법

### IoC 컨테이너 계층 구조

### 웹 애플리케이션의 IoC 컨테이너 구성

## 2. IoC/DI를 위한 빈 설정 메타정보 작성

### 빈 설정 메타정보

### 빈 등록하기

### 빈 의존관계 설정하기

### 프로퍼티 값 설정하기

### 컨테이너가 자동등록하는 빈

## 3. 프로토타입과 스코프

### 프로토타입 스코프

### 스코프의 종류

- 요청 스코프
- 세션 스코프, 글로벌세션 스코프
- 애플리케이션 스코프

## 4. 기타 빈 설정 메타정보

### 빈 이름

### 빈 생명주기 메소드

### 팩토리 빈, 팩토리 메서드

## 5. 스프링 3.1의 IoC 컨테이너와 DI

### 빈의 역할과 구분
