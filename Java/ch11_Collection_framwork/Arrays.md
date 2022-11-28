# Arrays 클래스
: 배열을 다루는데 유용한 메서드가 정의되어있음.

## 1. 배열 복사하기
- copyOf()
- copyOfRange()
```java
int[] arr = {0,1,2,3,4};
int[] arr2 = Arrays.copyOf(arr, arr.length); //[0,1,2,3,4]
int[] arr3 = Arrays.copyOf(arr,3);//[0,1,2]
int[] arr4 = Arryas.copyOf(arr,7);//[0,1,2,3,4,0,0] 나머지는 기본값으로 채운다
int[] arr5 = Arrays.copyOfRange(arr,2,4);//[2,3]
```

## 2. 채우기
- fill()
- setAll()
```java
int[] arr = new int[5];
Arrays.fill(arr,9);//[9,9,9,9,9]
Arrays.setAll(arr, (i)->(int)(Math.random()*5)+1);//[1,2,3,4,5]
```
## 3. 정렬, 검색
- sort()
- binarySearch(value):value가 있는 인덱스 반환
```java
int[] arr = {3,2,0,1,4};
int idx = Arrays.binarySearch(arr,2);//잘못된 결과!! 이진 탐색의 조건은 정렬이다!!

Arrays.sort(arr);//[0,1,2,3,4]
int idx = Arrays.binarySearch(2);//2
```

## 4. 변환
- asList(Object... a)
    - 배열을 List로 반환
    - 단 asList로 반환한 리스트는 크기를 변경할 수 없다!즉 추가, 삭제가 불가능하다!
    - 저장된 값 변경만 가능하다!
    - 크기 변경하려면 ArrayList에 담자.
```java
List list = Arrays.asList(new Integer[]{1,2,3,4,5});
//List list = Arrays.asList(1,2,3,4,5);
list.add(6);//UnSupportedOperationException 발생!

List list = new ArrayList(Arrays.asList(1,2,3,4,5));//크기 변경 가능

```

## 5. 비교와 출력
- equals()
- toString()
- deepToString() : 다차원 배열 출력은 이 메서드를 사용해야한다.
- deepEquals() : 다차원 배열 비교는 이 메서드를 사용해야한다.