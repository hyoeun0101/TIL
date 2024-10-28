## 🍎 Rest API
- REST (= REpresentational State Transfer)
    - 로이 필딩의 논문에서 등장한 Rest.
    - 인터넷 상의 시스템 사이의 상호 운용성을 제공하는 방법 중 하나.
    - 인테넷 상의 시스템들의 독립적인 진화를 위한 것.
- Rest API는 Rest를 따르는 API를 말한다.


### REST 아키텍쳐 스타일 규칙 6가지
1. Client-Server
2. Stateless
3. Cache
4. Uniform Interface
5. Layered System
6. Code-On-Demand(optional)

- HTTP만 잘 사용해도 client-server, stateless, cache, layered system은 잘 따른다.

### 그 중 Uniform Interface
1. Identification of resources
2. manipulation of resources through representation
3. __self-descriptive messages__
4. __HATEOAS__

➡︎ 결론: self-descriptive message, HATEOAS가 지켜지지 않고 있어서 시스템의 독립적인 진화가 불가능하며, 버저닝을 하는 일이 발생한다. 이 두 가지를 지켜야 rest api라 할 수 있다.

### self-descriptive message?
- 메세지는 자신을 설명할 수 있어야 한다. 
- 메세지 자체가 자신의 본문 자체를 설명하고 있기 때문에 메세지가 변하더라도 메세지를 받는 클라이언트는 언제나 해석이 가능하다. 서버가 메세지를 바꾸더라도 클라이언트는 그 바뀐 메세지에 알아서 대응할 수 있는 것. 메세지를 해석할 수 있는 정보가 그 메세지에 담겨 있기 떄문에.    


### HATEOAS?
- 응답에 애플리케이션의 상태 변화가 가능한 하이퍼미디어 정보가 들어있어야 한다. 그 하이퍼미디어를 통해 다음 상태로 전이한다. 

    
### self-descriptive message를 지키는 방법
1. Media type을 정의
    - 미디어 타입을 정의하고, IANA에 등록하고 그 미디어 타입을 리턴할 때 Content-type으로 사용한다.
    - 하지만 번거롭다^^
2. profile 링크 헤더를 추가
    - 브라우저들이 아직 스팩 지원을 잘 안한다.
    - 대안으로 HAL의 링크 데이터에 profile 링크를 추가할 수 있다.
- HAL? Hypertext Application Language
    - 리소스에 대한 링크와 같은 하이퍼미디어를 정의하기 위한 규칙.
    - 관련된 MIME
        - application/hal+xml
        - application/hal+json


### HATEOAS를 지키는 방법
- data에 링크 제공
    - JSON으로 하이퍼링크를 표현하는 방법을 정의한 명세들을 활용한다. ex) JSON API, HAL, UBER, Siren, Collection+json 등
    - 이 강의에선 HAL을 사용하여 링크를 제공한다.
-  HTTP 헤더인 Link, Loction을 제공한다.
- 결론은 data, 헤더 둘 다 사용하면 됨.


1. 개발자가 아닌 개발을 활용하는 사람되기
2. 개발이라는 행위 엔지니어로써 전문성 갖추기