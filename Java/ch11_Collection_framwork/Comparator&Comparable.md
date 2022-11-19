# Comparator, Comaparable
: 컬렉션 정렬하는 데 사용.

```java
//java.util
//다른 정렬 기준 정할 때
public interface Comparator{
    int compare(Object o1, Object o2);
}

//java.lang
//기본 정렬 기준 정할 때
public interface Comparable{
    int compareTo(Object o);
}
```

예시
```java
import java.util.Arrays;
import java.util.Comparator;

public class Ex{
    public static void main(String[] args) {
        String[] strArr = {"cat","Dog","lion","tiger"};

        Arrays.sort(strArr);//String의 Comparable구현에 의한 정렬
        System.out.println("strArr="+Arrays.toString(strArr));//Dog, cat, lion, tiger

        Arrays.sort(strArr,String.CASE_INSENSITIVE_ORDER); //대소문자 구분x
        System.out.println("strArr="+Arrays.toString(strArr));//cat, Dog, lion, tiger

        Arrays.sort(strArr, new Descending());
        System.out.println("strArr="+Arrays.toString(strArr));//tiger,lion,cat,Dog
        
    }
}

class Descending implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        if(o1 instanceof Comparable && o2 instanceof Comparable){
            Comparable c1 = (Comparable)o1;
            Comparable c2 = (Comparable)o2;
            return c1.compareTo(c2) * -1;//역 정렬
            //return c2.compareTo(c1);
        }
        return -1;
    }

}
```
Arrays.sort()는 배열을 정렬할 때, Comparator를 지정해주지 않으면 객체에 구현된 Comparable에 따라 정렬된다.    
오름차순 : 공백, 숫자, 대문자, 소문자


## Integer
Integer는 Comparable의 구현 클래스이다. `오름차순`으로 compareTo가 정의되어있다.
```java
public final class Integer extends Number implements Comparable{
    public int compareTo(Integer anotherInteger){
        return this.value - anotherInteger.value;
    }
}
```
비교값이 크면 -1, 내값이 크면 1, 같으면 0    

```java
import java.util.Arrays;
import java.util.Comparator;

public class Ex{
    public static void main(String[] args) {
        Integer[] arr = {30,50,10,40,20};

        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));//[10, 20, 30, 40, 50]


        Arrays.sort(arr, new DescComp());
        System.out.println(Arrays.toString(arr));//[50, 40, 30, 20, 10]
    }
}

class DescComp implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        if(!(o1 instanceof Integer && o2 instanceof Integer))
            return -1;
        
        Integer i =(Integer) o1;
        Integer i2=(Integer)o2;
        return i.compareTo(i2) * -1;//기본 compareTo()의 역순 정렬, Integer의 compareTo()
    }

}
```