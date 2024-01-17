의존성에서 test scope은 애플리케이션 실행 중에 사용되지 않는다. test를 실행할 때만 사용한다.

별 다른 데이터베이스 설정이 없으면(pom.xml에 데이터베이스 의존성) 기본으로 h2를 실행함.

pom.xml - 의존성 설정
@EnableAutoConfiguration = @SpringBootApplication에 있음. - 자동 설정
내장 웹 서버 (의존성과 자동 설정의 일부)
독립적으로 실행 가능한 JAR (pom.xml의 플러그인)


### Event 클래스 생성하기

1. 왜 @EqualsAndHasCode에서 of를 사용하는가
    - of를 사용하지 않으면 모든 필드에 대한 equals, hashCode를 생성한다. 엔티티가 서로 상호 참조를 한 경우 스택오버플로우가 발생할 수 있다.
2. 왜 @Builder를 사용할 때 @AllArgsConstructor가 필요한가
    - 필요없다. @Builder는 모든 필드를 가진 생성자를 자동으로 만들어준다. 기본 생성자가 없다.
3. @Data를 쓰지 않는 이유
    - @EqualsAndHashCode가 포함되어 있다. (첫 번째 질문과 동일한 이유) 이는 모든 필드에 대한 equals, hashCode이기 때문에 엔티티가 서로 상호 참조한 경우 스택 오버플로우가 발생한다.
4. 애노테이션 줄일 수 없나
    - 롬복은 메타 애노테이션을 지원하지 않는다. 따라서 롬복의 애노테이션을 묶어서 따로 메타 애노테이션을 만들 수 없다.



@WebMvcTest
- 웹과 관련된 빈들이 모두 등록된다.이를 통해 MockMvc를 주입받아 사용할 수 있는 것!
- MockMvc는 DispatcherServlet을 모킹한 것. 
- 웹과 관련된 것만 빈으로 등록해주기 떄문에 슬라이싱 테스트라고 함.
- 단위 테스트라 보긴 어려움. 딱 필요한 레이어만 테스트하는게 아니라 DispatcherServlet과 연관된 모든 빈들(Handler, Resolver 등)이 등록되어 있으니까.
- MockMvc는 가짜 요청,응답을 만들어 검증할 수 있음. Test에 핵심 클래스. 웹 서버 띄우지X. DispatcherServlet 구동. 단위 테스트보단 무겁고, 웹 테스트보단 가벼운 중간 정도


스프링 부트 슬라이스 테스트
@WebMvcTest
MockMvc 빈을 자동 설정 해준다. 따라서 그냥 가져와서 쓰면 됨.
웹 관련 빈만 등록해 준다. (슬라이스)


MockMvc
스프링 MVC 테스트 핵심 클래스
웹 서버를 띄우지 않고도 스프링 MVC (DispatcherServlet)가 요청을 처리하는 과정을 확인할 수 있기 때문에 컨트롤러 테스트용으로 자주 쓰임.


@WebMvcTest는 웹 관련 빈만 등록하기 때문에 repository는 빈으로 등록X. 따라서 직접 @MockBean으로 주입해야 함. 
그런데 Mock객체는 save나 다른 뭘 하더라도 리턴 값이 null이다.
