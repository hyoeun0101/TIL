## 🍎 Spring boot에서 Log 사용하기

### @Slf4j

- 스프링 부트에선 기본적으로 Logback이 설정되어 있다. (spring-boot-starter-logging 라이브러리에 기본적으로 설치되어 있음, 아니면 org.slf4j 라이브러리를 추가하면 된다.)
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

- 출력 레벨의 설정에 따라 로그를 출력할 수 있다. 레벨을 설정하면 자신보다 높은 단계의 레벨을 출력한다.
  - ex) INFO로 설정하면 DEBUG, TRACE는 보이지 않음.

1. ERROR
2. WARN
3. INFO (default)
4. DEBUG
5. TRACE

- [코드1]에서 디폴트가 INFO이기 때문에 debug, trace 로그는 찍히지 않는다.
- 로그 레벨은 application.properties나 application.yml과 같은 설정 파일로 설정한다.

```yml
logging:
  level:
    root: debug
  config: classpath:logback-local.xml #이처럼 프로파일별로 설정파일을 관리할 수도 있음
```

## 🍎 logback-spring.xml로 로깅 설정하기
- logback-spring.xml 파일은 크게 appender, logger로 나눌 수 있다.

### Appender
- 로그의 출력 위치, 출력 형식 등을 설정한다. logback-core 모듈에는 다음 3가지 기본 Appender이 있다.
  - ConsoleAppender : 로그를 콘솔에 출력
  - FileAppender : 로그를 지정 파일에 출력
  - RollingFileAppender : FileAppender의 자식으로, 날짜와 용량 등을 설정해서 패턴에 따라 각각 다른 파일에 로그를 기록할 수 있음.
### Logger
- 실제 로그 기능을 수행하는 객체이다. 각 Logger마다 name을 통해 구분한다.
- 최상위 로거인 Root Logger를 설정하면 이를 계층적으로 활용할 수 있다.
- LogStash, SockerAppender, FILE 등 함께 설정하여 사용할 수 있다.

[logback.xml의 기본적인 구조]
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
 <!-- 이 곳에 추가할 기능을 넣는다. appender와 logger-->
    <appender> <!-- Appender 설정 --> </appender>
    <logger> <!-- Logger 설정 --> </logger>
</configuration>
```
### logback 설정하기
1. application.yml에 logging 설정을 넣는다.
```yml
logging:
  level:
```
2. - src/main/resources 위치에 logback-spring.xml 파일을 생성한다.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="30 seconds">
    <property name="LOGS_ABSOLUTE_PATH" value="./logs" />
  <!-- 콘솔에 로그 출력 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>[%d{yyyy-MM-dd HH:mm:ss}][%-5level][%logger{36}] - %msg%n</pattern>
        </encoder>
    </appender>

    <logger level="info">
        <appender-ref ref="STDOUT"/>
    </logger>

</configuration>
```
### pattern
- %logger : 패키지를 포함한 클래스명 출력
- %logger{0} : 패키지를 제외한 클래스명만 출력
- %logger{length} : Logger name을 축약할 수 있으며, length는 최대 자리 수
- %-5level : 로그 레벨
- ${PID:-} : 프로세스 아이디
- %d : 로그 기록 시간 출력
- %d{yyyy-MM-dd HH:mm:ss} : 로그 기록 날짜,시간 출력
- %thread : 스레드명 출력
- %m : 로그 메세지
- %msg : 로그 메시지
### MDC

- %X는 MDC에서 해당 키값을 가지는 value를 얻는다.
