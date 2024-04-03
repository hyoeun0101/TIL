### HATEOAS와 Self-Descriptive Message 적용
Rest 리소스를 제공.
라이브러리.

### HATEOAS?
- hypermedia as the engine of application state. 어플리케이션 상태의 엔진으로서의 하이퍼미디어. 어플리케이션 상태를 바꾸는 하이퍼미디어.
- HATEOAS하다 = 하이퍼미디어로 애플리케이션의 상태를 바꿀 수 있다. = 화면을 바꿀 수 있다.
- 하이퍼미디어로 애플리케이션의 상태 전이가 가능하다.
- Rest application architecture의 컴포넌트 중 하나고, 
하이퍼미디어를 사용해서 동적으로 정보를 주고받음.

<link rel = "" href="">
- 서버와 클라이언트는 rel만 보고 처리를 한다. link는 바껴도 상관없음.
- 애플리케이션 상태에 따라 link 정보가 바뀜.

## 🍎 Spring HATEOAS 기능 핵심
- 링크 만드는 기능
    - 문자열 가지고 만들기
    - 컨트롤러와 메소드로 만들기
- 리소스 만드는 기능
    - 리소스: 데이터 + 링크
- 링크 찾아주는 기능
    - Tranverson
    - LinkDiscoverers
    - 강좌에서 다루진 X

### 링크에 들어가는 정보
- href : hypermedia reference. url 설정
- rel : relation. 현재 리소스와의 관계를 표현
     - self : 자기 자신에 대한 링크
     - profile : 응답 본문에 대한 설명 문서 링크
     - 이외 deposite, transfer, withdraw 등

- event를 생성했을 때 어떠한 링크 정보를 보여줄 수 있나?
    - self, profile
    - update-event : 이벤트 수정할 수 있는 링크
    - query-events : 이벤트를 조회할 수 있는 링크
    

## 🍎 링크 정보 넣는 법 - ResourceSupport 상속받는 클래스 생성.
- Event를 EventResource로 변환한다.
- @JsonUnwrapped : 직렬화할 때 매핑을 하지 않는다. `{"event" : { "id" : "123", "name":"test" }}` 이렇게 하지 않고, `{ "id" : "123", "name":"test" }` 

```java
public class EventResource extends ResourceSupport {
    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
```


- ObjectMapper가 직렬화할 때 BeanSerializer를 사용한다.
- 스프링에서 HATEOAS를 사용하려면 @EnableHypermediaSupport를 써야하는데, 스프링 부트가 자동으로 설정해줘서 아무 설정없이 바로 HATEOAS를 사용할 수 있었다.


- 레퍼런스에 나온 건 아님.
```java

```
 


 - BeanSerializer????? 


 @JsonUnwrapped
 : 직렬화할 때 매핑을 하지 않음. event : { id : "123"}












## Spring Rest Docs
- Spring MVC Test 를 사용해서 문서의 일부분을 생성해내는 라이브러리.

- Sweager보다 Rest Docs를 선호하는 이유 : api를 변경했을 때, 테스트 코드도 변경해야 하는데 그럼 문서도 같이 바뀐다. 코드는 변경되었는데, 문서에 반영안되는 경우를 방지할 수 있음.


### API 문서 만들기
- 요청 본문 문서화
- 응답 본문 문서화
- 링크 문서화
    - self, query-events, update-event 
    - profile 링크 추가
- 요청 헤더 문서화
- 요청 필드 문서화
- 응답 헤더 문서화
- 응답 필드 문서화


relaxed
장점 : 문서 일부분만 테스트할 수 있다.
단점 : 정확한 문서를 생성하지 못한다.

## Spring Docs 문서 빌드

mvn package
메이븐은 라이프사이클을 따라서 컴파일, 테스트 컴파일, 테스트, 패키징
테스트 실행하면 docs 조각들이 만들어지고, 플러그인 때문에 최종적으로 문서가 생성되고 그 문서가 스프링 부트가 기본적으로 지원하는 스타틱 디렉토리에 들어감. 웹 서버를 띄우면 api 문서 페이지를 바로 뷰에서 확인할 수 있음.

아스키다거 메이븐 플러그인으로 html생성. package할 떄, prepare-package가 asciidoc을 처리하라.

asciidoctor-maven-pulugin에 procss-asciidoc을 사용해서 패키징.
precess-asciidoc이 실행.
얘는 src/main/asciidoc에 들어있는 모든 아스키닥 문서를 html로 만들어준다. 그 위치는 target의 generated-docs


static 만들어준건. maven-resources-plugin 마찬가지로 prepare-package에 끼어넣어줌.
따라서 순서중여요.

copy-resources \

스프링 부트가 정적 리소스 지원하는 기능. static 아래 모든 파일은 웹서버로 접속 가능.

하이버네이트 : 이벤트에 대한 테이블을 만들수있도록, 쿼리를 볼 수 있도록


@SpringBootTest는 @SpringBootApplication이 붙어있는 곳 가서 빈들 다 등록하고, 마찬가지로 설정 파일도 씀.
테스트 시 설정 파일 다르게 하기

test> resource> application.yml 추가
project settings 가서 test의 resources를 test resource로 등록

그럼 main의 설정 파일과 test의 설정 파일 이름이 동일해서 test가 main을 덮어씀
컴파일, test쪽으로 resource 복사하고, test 컴파일 ,test resouce 복사함  

application-