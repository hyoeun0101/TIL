## Spring boot에서 Log 사용하기

### @Slf4j

- 스프링 부트에선 기본적으로 Logback이 설정되어 있다. (spring-boot-starter-logging 라이브러리에 기본적으로 설치되어 있음)
- @Slf4j 애노테이션만 붙이면 로그 사용이 가능하다.  
  [코드1]

```java
@Slf4j
public class LogTest {
    public static void main(String[] args) {
		log.trace("trace message");
		log.debug("debug message");
		log.info("info message");
		log.warn("warn message");
		log.error("error message");
	}
}
```

### Log Level

- 출력 레벨의 설정에 따라 로그를 출력할 수 있다.

1. ERROR
2. WARN
3. INFO (default)
4. DEBUG
5. TRACE

- ex) 출력 레벨을 INFO로 설정하면 DEBUG, TRACE 레벨의 로그는 출력되지 않음.
- [코드1]에서 디폴트가 info이기 때문에 debug, trace 로그는 찍히지 않는 것을 볼 수 있다.
- 로그 레벨은 application.properties나 application.yml과 같은 설정 파일로 설정한다.

```yml
logging:
  level:
    root: debug
```

### logback-spring.xml

- src/main/resources 위치에 `logback-spring.xml` 파일을 추가해주자.
- 크게 `appender`, `logger`로 나눌 수 있다.
  - appender : logger를 어디에 출력할지 설정. 콘솔, 파일, DB 등 지정할 수 있다.
  - logger : LogStash, SockerAppender, FILE 등 함께 설정하여 사용할 수 있다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
 <!-- 이 곳에 추가할 기능을 넣는다. appender와 logger-->
</configuration>
```

### MDC

- %X는 MDC에서 해당 키값을 가지는 value를 얻는다.
