싱글톤 패턴에 대해 알아보자. 싱글톤 패턴이란 객체를 하나밖에 생성하지 못하는 디자인 패턴이다.  
싱글톤 패턴의 핵심은 다음과 같다.

1. static 영역에 객체를 미리 하나 생성해서 올려둔다.
2. private 생성자를 사용해서 외부에서 객체의 생성을 막는다.
3. getter를 통해 만들어둔 하나의 객체를 반환한다.

```java
public class SingletonService{
    //1. static영역에 객체를 미리 하나 생성해서 올려둔다.
    private static final SingletonService instance = new SingletonService();

    //2. private 생성자를 사용해서 외부에서 객체의 생성을 막는다.
    private SingletonService(){}

    //3. getter를 통해 만들어둔 하나의 객체를 반환한다.
    public static SingletonService getInstance(){
        return instance;
    }
}

```

**싱글톤 패턴의 장점**

- 100개의 요청이 들어와도 100의 객체를 생성하는 것이 아니라 하나의 객체를 공유해서 사용하기 때문에 효율적으로 사용할 수 있다.  
  **싱글톤 패턴의 단점**
- 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
- 구체 클래스에 의존하기 때문에 DIP, OCP 위반한다.
- 내부를 변경이 어렵다.
- 유연성이 떨어진다.
  이런 단점들 때문에 `안티패턴`이라 불린다.
