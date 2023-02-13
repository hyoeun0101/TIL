## 🍎 xml 설정 파일로 빈 등록하기
- xml 파일 추가하기
  - src/main/resources > New> XML Configuration File > Spring Config 클릭
  - STS에서는 src/main/resources> new > Spring Bean Configuration File 클릭
[config.xml]
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- 사용할 빈 등록하기 -->
    <bean id="car" class="hello.core.prac.Car"/>
    <bean id="engine" class="hello.core.prac.Engine"/>
    <bean id="door" class="hello.core.prac.Door"/>
</beans>
```
- scope의 기본값은 singleton이다.(scope="singleton")

## 🍎 Propterty로 객체 초기화하는 방법
### 1. setter로 초기화하기
```xml
    <bean id="car" class="hello.core.prac.Car">
        <property name="color" value="red"/>
        <property name="oil" value="100"/>
        <property name="engine" ref="engine"/>
        <property name="doors">
            <array value-type="hello.core.prac.Door">
                <ref bean="door"/>
                <ref bean="door"/>
            </array>
        </property>
    </bean>
```
- setter를 호출하는 것이기 때문에 Car에 setter 작성이 필요하다.

### 2. 생성자로 초기화하기
```xml
    <bean id="car" class="hello.core.prac.Car">
        <constructor-arg name="color" value="black"/>
        <constructor-arg name="oil" value="100"/>
        <constructor-arg name="engine" ref="engine"/>
        <constructor-arg name="doors">
            <array value-type="hello.core.prac.Door">
                <ref bean="door"/>
                <ref bean="door"/>
            </array>
        </constructor-arg>
    </bean>
```
- 생성자를 호출하는 것이기 때문에 Car에 생성자 작성이 필요하다.


## 🍎 @Value
- 참조형 필드는 `@Autowired`로 주입을 하지만 기본형 필드일 경우 `@Value`를 사용하여 주입한다.
- `@PropertySource`로 프로퍼티 파일을 명시한다.
- systemProperties, systenEnvironment와 같이 시스템 변수 값을 주입할 때는 #{}를 사용한다.
- properties 파일에서 정보를 읽어올 때는 ${}를 사용한다.

```java
@Component
@PropertySource("setting.properties")
class SysInfo{
    @Value("#{systemProperties['user.timezone']}")
    String timeZone;

    @Value("#{systemEnvironment['PWD']}")
    String currDir;

    @Value("${autosaveDir}")
    String autosaveDir;

    @Value("${autosaveInterval}")
    int autosaveInterval;

    @Value("${autosave}")
    boolean autosave;
}
```
[main > resources> setting.properties]
```
autosaveDir=/autosave
autosave=true
autosaveInterval=30
```