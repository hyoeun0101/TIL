# Spring Boot에서 properties 값 주입받기

- `application-OOO.yml`
  - 애플리케이션을 실행할 때 VM arguments에 `-Dspring.profiles.active=OOO`을 작성한 후 실행하는데...
  - 애플리케이션이 실행할 때 OOO에 들어가는 값과 일치하는 파일을 사용한다.
  - 따라서 `application-OOO.yml` 형식으로 파일을 생성해야한다!
  - 예) `-Dspring.profiles.active=local`로 실행했다면 애플리케이션은 `application-local.yml` 파일을 사용한다.

## 1. @Value

: org.springframework.beans.factory.annotation.Value 사용하여 값 주입받기

- Value로 String, int 등의 타입 값을 주입할 수 있다.
- Value는 SpEL(Spring Expression Language)을 이용해 값을 주입할 수도 있다. SpEL를 사용할 땐 `#{}`를 사용해야한다.
- 문제점 : true값이 String, boolean으로도 사용될 수 있다. 즉 `타입 안정성을 보장하지 않는다!`, 불변이 아니다.
- 예시)

```java
import org.springframework.beans.factory.annotation.Value;

public class NettyProperties {
	@Value("${prelayserver.config.port}")
	private int port;

	@Value("${prelayserver.config.bCouponPort}")
    private int bCouponPort;

    @Value("${prelayserver.api.url}")
    private String url;

    //SpEL 사용. true가 주입된다.
    @Value("#{1 eq 1}")
    private boolean enable;
}
```

[application.yml]

```yml
prelayserver:
  config:
    port: 15011
    bCouponPort: 15012
  api:
    url: http://localhost:29090
```

## 2. @ConfigurationProperties

- prefix를 작성하면, 이름과 일치하는 값이 자동으로 주입된다.
- 아래 예시 `time-out`같은 경우 camelCase로 작성하면 된다.
- 문제점 : @Value와 마찬가지로 불변이 아니다.

```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="prelayserver.config")
public class NettyProperties {
	private int port;
    private boolean enable;
    private BCoupon bCoupon;

    @Getter
    @Setter // setter 작성 필요.
    public static class BCoupon {
    	private int port;
    	private boolean enable;
    }

    private int timeOut;
}
```

[application.yml]

```yml
prelayserver:
  config:
    port: 15011
    enable: true
    bCoupon:
      port: 15012
      enable: true
    time-out: 30000
```

## 3. @ConstructorBinding

- Spring Boot 2.3버전 이후 추가되었다.
- final 필드에 대해 값을 주입해준다.
- final 키워드를 명시하지 않으면 setter로 값을 바인딩한다.

```java

```
