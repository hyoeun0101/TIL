java.lang 패키지는 자바프로그래밍에 가장 기본이 되는 클래스를 포함하고 있다.  
import를 하지 않아도 사용할 수 있으며 String 클래스, System 클래스가 바로 java.lang 패키지에 속하기 때문에 import를 하지 않고 사용할 수 있던 것이다!!

# Object 클래스

: 모든 클래스의 조상 클래스이다.
총 11개의 메서드를 가지고 있다.

```java
package java.lang;

import jdk.internal.HotSpotIntrinsicCandidate;

public class Object {

    private static native void registerNatives();
    static {
        registerNatives();
    }

    @HotSpotIntrinsicCandidate
    public Object() {}

    @HotSpotIntrinsicCandidate
    public final native Class<?> getClass();

    @HotSpotIntrinsicCandidate
    public native int hashCode();

    public boolean equals(Object obj) {
        return (this == obj);
    }

    @HotSpotIntrinsicCandidate
    protected native Object clone() throws CloneNotSupportedException;

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    @HotSpotIntrinsicCandidate
    public final native void notify();

    @HotSpotIntrinsicCandidate
    public final native void notifyAll();

    public final native void wait(long timeoutMillis) throws InterruptedException;

    public final void wait(long timeoutMillis, int nanos) throws InterruptedException {
        if (timeoutMillis < 0) {
            throw new IllegalArgumentException("timeoutMillis value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos > 0) {
            timeoutMillis++;
        }

        wait(timeoutMillis);
    }

    @Deprecated(since="9")
    protected void finalize() throws Throwable { }
}

```

## equals()

```java
public boolean equals(Object obj) {
        return (this == obj);
    }

```

: 참조변수의 값이 같은지 비교한다. == 연산자와 같다.
**equals() 오버라이딩**
equals()는 나의 입맛에 맞게 오버라이딩하여 사용해야한다.

```java
public class Ex{
  public static void main(String[] args) {
    Person p1 = new Person(10);
    Person p2 = new Person(10);

    System.out.println(p1.equals(p2));

  }
}

class Person{
    long id;

    Person(long id){
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Person){
            return id==((Person)obj).id;
        }
        return false;
    }
}
```

equals()를 오버라이딩하여 두 객체의 id값을 비교하도록 했다.

## hashCode()

```java
    @HotSpotIntrinsicCandidate
    public native int hashCode();
```

- 객체의 주소값으로 해시코드를 만들어 반환한다.
- native 메서드는 OS의 메서드로 이미 작성되어 있는 OS의 메서드를 사용할 수 있게한다.
- hashCode() 메소드는 java.util 패키지의 HashMap이나 HashSet과 같이 해시 테이블을 구현하는 데에 사용된다.
- 객체의 주소로 해시코드를 만들기 때문에 객체마다 다른값을 가지고 있다.
- 다만 64bit JVM에서는 주소가 64bit(long) 이다. 해시코드는 32bit(int)이기 때문에 64bit JVM에서 다른 객체여도 같은 해시코드가 나올 수도 있다.
- equals 오버라이딩하면 hashcode도 오버라이딩 해야함. String에는 hashCode()가 오버라이딩 돼있어서 같은 값을 가진 객체들은 서로 같은 해시코드가 나온다.
