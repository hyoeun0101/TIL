## 🍎 @Deprecated
- 더 이상 사용하지 않는 필드, 메서드 등에 붙인다.
## 🍎 @SuppressWarnings
- 컴파일러가 보여주는 경고 메세지를 안보이도록 한다.
- `@SuppressWarngins({"deprecation", "unchecked"})` 와 같이 작성하면 여러 경고 메세지를 억제한다.
### @SuppressWarnings로 억제할 수 있는 경고 메세지 종류
- deprecation : @Deprecated가 붙은 대상을 사용했을 때 나타나는 경고 무시
- unchecked : 지네릭스로 타입 지정하지 않았을 때 나타나는 경고 무시
- rawtypes : 지네릭스 사용하지 않았을 때 발생하는 경고 무시    
등등...


## 🍎 매타 애너테이션
- @Target : 적용대상 지정
  - `@Target({TYPE, FIELD, METHOD,LOCAL_VARIABLE})`
- @Retention: 애너테이션 유지기간 지정
  - SOURCE : 소스 파일에만 존재
    - EX) @Override- `@Retention(RetentionPolicy.SOURCE)`
  - RUNTIME: 클래스 파일에도 존재, 실행 시 사용
    - EX) @FunctionalInterface - `@Retention(RetentionPolicy.RUNTIME)`
  - CLASS : 클래스 파일에도 존재, 실행 시 사용불가
- @Inherited : @Inherited가 붙은 애너테이션이 부모 클래스에 붙어있으면 자식 클래스에도 붙은 것으로 인식된다.
- @Repeatable : @Repeatable를 붙인 애너테이션은 하나의 대상에 여러 번 붙일 수 있다. 
- @Documented

## 🍎 애너테이션 타입 정의하기
```java
@interface TestInfo{
  int count() default 1; //다음과 같이 기본값 설정할 수 있음.
  String testedBy();
  String[] testTools();
}
...

@TestInfo( testedBy="test", testTools={"hello", "tests"})
public class TestClass{}
```