## 🔴 빈 생명주기 콜백?

스프링 컨테이너는 다음과 같은 라이프사이클을 가진다.

```
스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 사용 -> 소멸 전 콜백 -> 종료
```
- 빈 생성하고 의존관계 주입한 후 `초기화 콜백`과 `종료 직전 콜백`이 있다.
- DB 커넥션 풀이나 네트워크 소켓 같은 경우 애플리케이션 시점에 미리 연결을 해두는 작업과 종료 시점에 연결을 종료하는 작업이 필요하다. 라이프사이클 콜백을 통해 이 작업들을 처리할 수 있다.

## 🔴 빈 생명주기 콜백 방법 3가지

### 🟣 1. 인터페이스 구현 - InitailizingBean, DisposableBean

- InitializingBean의 afterPropertiesSet() 메서드를 오버라이딩한다.
  - 빈을 호출한 다음 afterPropertiesSet()이 호출된다.
- DisposableBean의 destroy() 메서드를 오버라이딩한다.
  - 빈이 소멸되기 직전 종료 시점에 destroy()가 호출된다.

```java
public class NetworkClient implements InitializingBean, DisposableBean {
    private String url;

    public NetworkClient(){
        System.out.println("생성자 호출, url= "+url);
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void connect(){
        System.out.println("connect : "+url);
    }
    public void call(String message){
        System.out.println("call: "+url+" message="+message);
    }

    public void disConnect(){
        System.out.println("close="+url);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        connect();
        call("초기화 연결");
    }

   @Override
   public void destroy() throws Exception {
       disConnect();
   }
}
```

- 이 인터페이스는 스프링 인터페이스라 스프링 전용 인터페이스에 의존적이다.  
- 메서드 이름 변경을 못한다.  
- 외부 라이브러리에 적용 못한다.

### 🟣 2. 빈 등록 시 메서드 지정 - @Bean(initMethod = "init", destroyMethod = "close")

- 설정 정보 클래스에 빈 등록 할 때 `@Bean(initMethod = "init", destroyMethod = "close")` 초기화 메서드, 종료 메서드를 지정한다.
- 빈 생성 후에 `NetworkClient`의 `init()` 메서드가 실행되고, 종료 후엔 `close()` 메서드가 실행된다.
- destroyMethod는 디폴트값이 inferred(추론)이다. 생략하면 `close`,`shutdown`이라는 메서드 이름을 추론하여 종료 메서드로 호출한다. 추론 기능을 사용하지 않으려면 `destroyMethod=""`로 지정하면 된다.

```java

@Configuration
static class LifeCycleConfig {
    
    // 빈 등록할 때 설정
    @Bean(initMethod = "init", destroyMethod = "close")
    public NetworkClient networkClient() {

        NetworkClient networkClient = new NetworkClient();
        networkClient.setUrl("http://hello-spring.dev");
        return networkClient;

    }
}
```

```java
public class NetworkClient {
    //...생략...

    // init
    public void init() {
       System.out.println("NetworkClient.init");
       connect();
       call("초기화 연결 메시지");
    }

    // close
    public void close() {
       System.out.println("NetworkClient.close");
       disConnect();
    }
}
```

- 스프링에 의존하지 않는다.  
- 메서드 이름을 지정할 수 있다.  
- 코드가 아닌 설정 정보를 사용하기 때문에 외부 라이브러리에도 적용할 수 있다.

### 🟣3. @PostConstruct, @PreDestroy

- 메서드 위에 애노테이션을 붙여주기만 하면 된다.

```java
public class NetworkClient {
    //...생략...

    //init
    @PostConstruct
    public void init() {
       System.out.println("NetworkClient.init");
       connect();
       call("초기화 연결 메시지");
    }

    //close
    @PreDestroy
    public void close() {
       System.out.println("NetworkClient.close");
       disConnect();
    }
}
```

- 최신 스프링에서 권장하는 방법.  
- 하지만 외부 라이브러리에는 적용하지 못한다. 외부 라이브러리에 적용할 땐 @Bean 기능을 이용해야한다!
