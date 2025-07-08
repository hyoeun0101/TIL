## 🔴 @MapperScan, @Mapper
- 지정한 패키지를 스캔하여 @Mapper이 붙은 인터페이스를 빈으로 등록한다.

```java
@Configutation
@MapperScan(basePackages = "com.example.myapp.mapper")
public class MyBatisConfig {
    //...
}
```

```java
@Mapper
public interface ExamleMapper {
}
```

- 2.0.4 버전 이후로 특정 패키지를 지정하지 않으면 @MapperScan을 선언한 클래스의 패키지를 디폴트로 설정한다.

- @Mapper 대신 사용자 정의 애노테이션 지정할 수도 있다. 이를 위해선 `@MapperScan(annotationClass = 사용자 정의 애노테이션.class)`을 쓰면 된다.


```java
@MapperScan(annotationClass = ExampleAnnotation.class)
public class MyBatisConfig {
    //...
}
```

```java
@ExampleAnnotation
public interface ExamleMapper {
}
```

