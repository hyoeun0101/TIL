- 3장에서는 모든 클래스의 부모 클래스인 Object의 final이 아닌 메서드에 대해 다룬다.(final 메서드는 오버라이딩할 수 없다.)
- equals, hashCode, toString, clone, finalize

```java
package java.lang;

import jdk.internal.HotSpotIntrinsicCandidate;

public class Object {

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

    @Deprecated(since="9")
    protected void finalize() throws Throwable { }
}

```

## 🍎 Item10. equals는 일반 규약을 지켜 재정의하라.
### equals를 재정의하지 않아도 되는 경우
- 각 인스턴스가 고유한 경우
    - 값을 표현하는게 아닌 동작을 표현할 경우 ex) Thread
- 논리적 동치성(logical equality)이 필요없는 경우
- 상위 클래스의 equals를 사용하는 경우
    - List는 AbstractList의 equals를 사용한다.
- 클래스가 private이거나 package-private여서 equals를 호출할 일이 없는 경우

### equals를 재정의해야 하는 경우
