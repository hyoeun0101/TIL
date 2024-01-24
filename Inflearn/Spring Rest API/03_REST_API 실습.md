## HATEOAS와 Self-Descriptive Message 적용
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

### Spring HATEOAS 기능 핵심
- 링크 만드는 기능
    - 문자열 가지고 만들기
    - 컨트롤러와 메소드로 만들기
- 리소스 만드는 기능
    - 리소스: 데이터 + 링크
- 링크 찾아주는 기능

### 링크
- href : hypermedia reference. url 설정
- rel : relation. 현재 리소스와의 관계를 표현
     - self : 자기 자신에 대한 링크
     - profile : 응답 본문에 대한 설명 문서 링크
     - 이외 deposite, transfer, withdraw 등

- event를 생성했을 떄 어떠한 링크 정보를 보여줄 수 있나?
    - self, profile
    - update-event : 이벤트 수정할 수 있는 링크
    - query-events : 이벤트를 조회할 수 있는 링크
    

### 링크 정보 넣는 법
1. ResourceSupport 상속받는 클래스 생성.
- Event를 EventResource로 변환.

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

- 레퍼런스에 나온 건 아님.
```java

```
 


 - BeanSerializer????? 


 @JsonUnwrapped
 : 직렬화할 때 매핑을 하지 않음. event : { id : "123"}
