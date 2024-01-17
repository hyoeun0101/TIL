로이 필딩이 발표한 논문 Rest API

- 어떻게 인터넷 상의 서로 다른 시스템들의 독립적인 진화를 보장할 것인가.
- 웹을 깨뜨리지 않으면서 HTTP 진화하기.

- 오늘 날 res

Rest APi
REpresentational State Transfer = REST
- 어떻게 인터넷에서 서로 다른 시스템 간의 독립적인 진화를 보장할 것인가.
웹을 깨뜨리지 않으면서 HTTP를 진화시키는 방법에 대한 눈문 = 거기서 나온 Rest API

오늘 날의 Rest API는 Rest API가 아니다.
그 이유는 Rest API는 
- Client-Server
- Stateless
- Cache
- Uniform Interface
- Layered System
- Code-On-Demand(optional)

이 아키텍쳐 스타일을 따라야 하는데, 특히 이 중 Uniform Interface 
Uniform Interface를 구성하는 4가지 중 3,4번을 만족하지 않는다.

1. Identification of resources
2. manipulation of resources through representations
3. self-descrive messages
4. hypermedia as the engine of application state(HATEOAS)


self-descriptive message
- 메세지 그 자체만으로 메세지를 설명할 수 있어야 한다.
- 서버가 변해서 메세지가 변하더라도 클라이언트는 그 메세지를 보고 해석할 수 있다.
- 즉 바뀐 메세지에도 클라이언트가 대응할 수 있다.
HATEOAS
- 하이퍼미디어(링크)를 통해 애플리케이션 상태 변화가 가능해야 한다.
- 응답에 애플리케이션 상태 변화가 가능한 하이퍼미디어 정보가 들어있어야 한다.
 하이퍼미디어 정보로 클라이언트가 다음 상태로 변할 수 있어야 한다.
 이 url을 보내면 이렇게, 이 url을 보내면 이렇게.
 버저닝 필요없음.








응답에 대한 미디어 타입을 정의





github api는 rest api임.


- self-descriptive message 해결
1. profile 헤더 추가 (아직 지원하지 않는 브라우저가 있음.클라이언트가 이해 X)
or 응답 본문에 HAL 작성


HAL을 사용하여 링크를 제공하자. -> HATEOAS 해결
헤더에 추가 or 응답 본문에 추가.


