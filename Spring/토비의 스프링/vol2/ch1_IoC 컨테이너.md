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
  -
- 스프링이 컨텍스트를 실행할 때, 우선 자식 컨텍스트에서 찾은 다음 등록된 빈이 없으면 부모 컨텍스트에서 찾는다. 즉, 자식 우선.
- 주의할 점 : 컨텍스트의 실행 방향은 오로지 자식에서 부모로만 간다. 형제나 자식으로 가지 않는다!

```java
// 자식 컨텍스트 생성하는 법
GenericApplicationContext child = new GenericApplicationContext(parent);
```

## @Configuration 없는 클래스의 @Bean

- 알다시피 @Configuration은 빈을 싱글톤으로 만들어 주므로 @Configuration이 없는 클래스의 @Bean은 싱글톤이 아니다.
- @Configuration이 없는 클래스의 @Bean은 언제 사용하나?
  - @Bean 메소드를 통해 정의되는 빈과 그 메소드를 감싸는 클래스로 만들어지는 빈이 밀접한 관계가 있을 때
  - @Bean 메소드는 클래스 내부에 있으므로 클래스의 모든 정보에 접근이 가능하다.
  - 설정 정보 등을 공유할 수 있는 동시에 외부로 빈의 존재가 노출되지 않는다.(물론 일반 빈으로 등록되므로 참조 가능하지만 싱글톤이 아님을 염두에 둘 것)
  - 밀접한 의존관계를 갖는 종속적인 빈을 정의할 때 유용하지만, 설정정보와 일반 애플리케이션 코드가 같이 있어 유연성이 떨어진다.

##
