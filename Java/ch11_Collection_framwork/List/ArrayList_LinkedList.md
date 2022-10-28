# ArrayList

(메서드 예제)

```java
import java.util.ArrayList;
import java.util.Collections;

public class TestArrayList {
    public static void main(String[] args) {
        ArrayList list1 = new ArrayList<>(10);
        list1.add(new Integer(5));
        list1.add(new Integer(4));
        list1.add(new Integer(2));
        list1.add(new Integer(0));
        list1.add(new Integer(1));
        list1.add(new Integer(3));

        ArrayList list2 = new ArrayList<>(list1.subList(1, 4));
        print(list1,list2);
        //list1 : [5,4,2,0,1,3]
        //list2 : [4,2,0]

        Collections.sort(list1);
        Collections.sort(list2);
        print(list1, list2);
        //list1 : [0,1,2,3,4,5]
        //list2 : [0,2,4]

        System.out.println("list1.containsAll(list2):"+list1.containsAll(list2));//true

        list2.add("B");
        list2.add("C");
        list2.add(3,"A");
        print(list1, list2);
        //list2 : [0,2,4,A,B,C]

        list2.set(3, "AA");
        print(list1, list2);
        //list2 : [0,2,4,AA,B,C]
        System.out.println("list1.retainAll(list2):"+list1.retainAll(list2));//true
        print(list1, list2);
        //list1 : [0,2,4]
        //list2 : [0,2,4,AA,B,C]

        //list2에서 list1에 포함된 객체 삭제
        for(int i=list2.size()-1;i>=0;i--){
            if(list1.contains(list2.get(i))){
                System.out.println(i);
                list2.remove(i);
            }
        }
        print(list1,list2);
        //list1:[0, 2, 4]
        //list2:[AA, B, C]


    }

    static void print(ArrayList list1, ArrayList list2){
        System.out.println("list1:"+list1);
        System.out.println("list2:"+list2);
        System.out.println();
    }
}

```

| 컬렉션     | 읽기                                                                                   | 추가와 삭제                                        | 비고                                                            |
| ---------- | -------------------------------------------------------------------------------------- | -------------------------------------------------- | --------------------------------------------------------------- |
| ArrayList  | 연속적으로 메모리의 주소가 할당되어 인덱스로 데이터에 접근하기 때문에 조회 속도가 빠름 | 비순차적인 추가삭제 경우, 데이터의 이동때문에 느림 | 순차적인 추가삭제는 더 빠름, 비효율적인 메모리 사용, 크기 변경X |
| LinkedList | 처음부터 n번째 데이터까지 순차적으로 따라가서 조회하기 때문에 느림                     | 참조된 주소만 변경하면 되기 때문에 빠름            | 데이터가 많을수록 접근성 떨어짐                                 |

     인덱스가 n인 데이터의 주소 = 배열의 주소 + n * 데이터 타입의 크기

### Stack

LIFO이기 때문에 배열로 구현
ex) 수식 계산, 수식 괄호 검사, 웹브라우저의 뒤로/앞으로

### Queue

FIFO, 배열로 구현하게 되면 삭제 시 데이터 이동이 빈번하기 때문에 LinkedList가 더 적합.
ex) 최근사용문서, 인쇄작업 대기목록
