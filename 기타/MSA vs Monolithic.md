### Monolithic vs MSA
- 모놀리틱은 모든 비즈니스 로직을 하나의 아키텍쳐로 통신하는 모습. UI, 비즈니스 로직, DB 모두 하나의 구조를 띄고 있다.
- MSA는 애플리케이션을 관심사마다 마이크로서비스로 쪼개어서 관리한다. 각각의 서비스는 API를 통해 호출이 되고 관리된다.
### 모놀리틱
- 애플리케이션이 하나로 관리되기 때문에 새로운 기능 추가, 업데이트가 어렵다. 
- 특정 부분에 문제 발생 시, 큰 장애로 이어질 수 있다.
- 스케일 아웃 시, 전체적으로 스케일 아웃해야 하기 때문에 필요 없는 자원이 함께 증가된다.

### MSA
- 민첩하고 손쉬운 배포 및 업데이트
- 필요한 부분만 스케일 아웃

### MSA 구성하는 주요 Component
1. Config Management
    - 서비스의 재빌드, 재부팅없이 설정사항을 반영
    - Netflix Archaius, Kubernetes COnfigmap
2. Service Discovery
    - MSA 기반 서비스 배포 시 서비스 검색 및 등록
    - Netflix Eureka, Kubernetes Service, Istio
3. API Management
    - 클라이언트 접근 요청을 일원화
    - Netflix Zuul, Kubernetes Ingress
4. Centralized Logging
    - 서비스별 로그의 중앙집중화
    - ELK Stack
5. Distributed Tracing
    - 마이크로서비스 간의 호출 추적
    - Spring Cloud Sleuth, Zipkin
6.  Centralized Monitoring
    - 서비스별 매트릭 정보의 중앙집중화
7. Resilience & Fault Tolerance
    - MSA 구조에서 하나의 실패하면 전체 서비스에 영향이 미치지 않도록 계단식 실패 방지 구조를 설정
8. Auto-Scaling & Self-Healing
    - 자동 스케일링, 복구 자동화를 통해 서비스 관리 효울화


### 스케일업 vs 스케일 다운
- 스케일 업은 기존의 서버의 사양을 업그레이드하는 것. 결국 하나의 서버의 능력을 증강하기 때문에 수직 스케일링이라 한다. AWS EC2 인스턴스 사양을 micro에서 small, medium으로 업그레이드 하는 것!
- 스케일 아웃은 서버를 추가해서 확장하는 것.

### MSA를 구현하는 기술
- Service Mesh Architecture??

### MSA 구축 어려움
- 관리하는데 엄청 복잡한게 아니면 모놀리틱이 낫다.

### Service Mesh 적용 시 고려사항
1. 복잡성 
    - service mesh를 사용하면 런타임 인스턴스 수 증가
2. 사이드카 컨테이너 수 증가
    - 각 서비스는 service mesh의 사이드카 프록시를 통해 호출되므로 개별 프록시 수가 증가한다. 이에 따른 부하로 서비스 운영에 문제 발생 가능성이 있는지 아키텍쳐 구조 측면에서 검토해야 한다.
3. 기술력의 미성숙
    - 빠르게 발전하곤 있지만 아직은 새롭고 미성숙한 기술. 아직 많은 기업에 보편화된 기술은 아님.

### 쿠버네티스의 핵심 개념
- Pod : 배포 가능한 가장 작은 단위
- Object와 Controller
    - Object : namespace, pod, volume 등
    - Controller : Object를 생성 관리하는 객체
- Service
    - 로드밸런서 : 외부에서 들어온 트래픽을 내부의 Pod으로 파싱 후 라우팅해줌.
- Ingress
    - url 기반의 라우팅
- Configmap
    - 컨테이너에 들어갈 어플리케이션의 설정 값을 외부에서 제어할 수 있도록 하는 객체.
- Monitoring
    - 분리되어 있는 각각의 서비스를 통합하여 모니터링하는 게 중요하다.
    - 총 4개의 모니터링이 필요하다. Host, Container, App(마이크로서비스), 쿠버네티스. 각각의 레이어 별로 모니터링이 필요