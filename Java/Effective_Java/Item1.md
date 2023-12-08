## 🍎 생성자 대신 정적 팩터리 메서드를 고려하라.
- public 생성자 대신 정적 팩터리 메서드를 사용하여 객체를 생성할 수 있다.
- ex) Boolean.valueOf(false)
### 정적 팩터리 메서드의 장점 5가지
1. 반환될 객체의 특성을 이름으로 잘 표현할 수 있다.
    - BigInteger.probablePrime() 이라는 정적 팩터리 메서드는 소수인 BigInteger를 생성해준다. 
```java
BigInteger bigInteger = BigInteger.probablePrime(5, new Random());
```
2. 호출할 때마다 새 인스턴스를 생성하지 않아도 된다.
    - 불변 클래스는 인스턴스를 미리 만들어놓거나 새로 생성한 인스턴스를 캐싱하여 재활용한다.. 이 때 정적 팩터리 메서드를 사용하면 하나의 객체를 재활용할 수 있다.
    - 이는 플라이웨이트 패턴과 비슷하다.플라이웨이트 패턴? 미리 캐싱된 인스턴스를 반환하는 것.
```java
//Boolean 클래스
   public static final Boolean TRUE = new Boolean(true);

    public static final Boolean FALSE = new Boolean(false);
    
    public static Boolean valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }
```
```java
package org.effectvie.item01;

public class Exam01 {
    public static void main(String[] args) {
        // 동일한 객체가 반환된다.
        System.out.println(System.identityHashCode(Boolean.valueOf(false))); //713338599
        System.out.println(System.identityHashCode(Boolean.valueOf(false))); //713338599

        System.out.println(System.identityHashCode(Boolean.valueOf(true))); //168423058
        System.out.println(System.identityHashCode(Boolean.valueOf(true))); //168423058
       
        System.out.println(System.identityHashCode(Boolean.valueOf("true"))); //168423058
    }
}
```
3. 정적 팩터리 메서드는 인터페이스의 하위 타입 객체를 반환할 수 있다.(객체의 다형성을 지원한다)
- 추가로 설명하자면 자바 8 이후 인터페이스에서 정적 메서드를 지원하여 정적 팩터리 메서드를 인터페이스 내에서 선언할 수 있다.
```java
package org.effectvie.item01;

public interface HelloService {
    void hello();
}
```
```java
package org.effectvie.item01;

public class KoreaHelloService implements HelloService{
    @Override
    public void hello() {
        System.out.println("안녕하세요~");
    }
}
```
```java
package org.effectvie.item01;

public class EnglishHelloService implements HelloService{

    @Override
    public void hello() {
        System.out.println("hello");
    }
}

```
```java
public class HelloServiceFatory {

    // 예전 방식 : 정적 팩터리 메서드를 통해 하위 타입을 반환할 수 있다.
    public static HelloService of(String lang) {
        if(lang.equals("ko"))
            return new KoreaHelloService();
        else
            return new EnglishHelloService();
    }
}
```
```java
package org.effectvie.item01;

import java.math.BigInteger;
import java.util.Random;

public class Exam01 {
    public static void main(String[] args) {
        // 정적 팩터리 메서드는 내부 구현을 숨길 수 있다.
        HelloService helloService = HelloServiceFatory.of("ko");
        helloService.hello();
    }
}
```

[인터페이스의 정적 팩터리 메서드]
```java
package org.effectvie.item01;

public interface HelloService {

    void hello();
    
    static HelloService of(String lang) {
        if(lang.equals("ko"))
            return new KoreaHelloService();
        else
            return new EnglishHelloService();
    }
}
```
4. 매개변수에 따라 다른 객체를 반환할 수 있다.

5. 정적 팩토리 메서드를 호출하는 시점에 실제로 그 클래스가 존재하지 않아도 된다.
- 서비스 제공자 프레임워크?
### 정적 팩터리 메서드의 단점
1. 정적 팩터리 메서드를 통해서만 객체를 생성하면 상속이 안된다.
```java
package org.effectvie.item01;

public class NoContstructure {
    private NoContstructure() {}
    
    public static NoContstructure create() {
        return new NoContstructure();
    }
}

```
2. 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.
    - 따라서 API 문서를 잘 써놓고, 정적 팩터리 메서드명의 규약을 지키는 것이 좋다.
    - from : 매개변수 하나를 받아 해당 타입의 인스턴스를 반환 ex) Date d = Date.from(instant);
    - of : 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환 ex) Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);
    - valueOf : from과 of의 더 자세한 버전 ex) BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);
    - instance 또는 getInstance : 매개변수로 명시한 인스턴스 반환, 하지만 같은 인스턴스임을 보장 X ex) StackWalker luke = StackWalker.getInstance(options);
    - create 또는 newInstance : 위와 같지만 매번 새로운 인스턴스를 생성해 반환 ex) Object newArr = Array.newInstance(classObject, arrayLen);