## 🍎 filter
```java
List<Dish> specialMenu = Arrays.asList(
    new Dish("A", true, 120, Dish.Type.OTHER),
    new Dish("B", false, 300, Dish.Type.FISH),
    new Dish("C", true, 350, Dish.Type.OTHER),
    new Dish("D", false, 400, Dish.Type.MEAT),
    new Dish("E", true, 530, Dish.Type.OTHER),
)
```
- filter는 모든 요소 돌면서 체크함.
### 슬라이싱 - takeWhile
- 정렬 되어있는 경우, 조건이 true면 작업 중단 후 요소 반환(앞으로 슬라이싱)
```java
List<Dish> sliceMenu = specialMenu.stream()
                                .takeWhile(dish -> dish.getCalories < 320)
                                .collect(toList());// A,B
```
### 슬라이싱 - dropWhile
- 정렬 되어있는 경우 조건이 false면 중단하고 남은 요소 반환(뒤로 슬라이싱)
```java
List<Dish> sliceMenu = specialMenu.stream()
                                .dropWhile(dish -> dish.getCalories < 320)
                                .collect(toList());// C,D,E
```

### limit, skip
## 🍎 map
- ["Hello", "World"]를 ["H","e""l","o","W","r","d"]로 만들기.

```java
words.stream()
    .map(word -> word.split(""));  //Stream<String[]>  ex) [H,e,l,l,o], [W,o,r,l,d]
    .map(Arrays::stream) //[H,e,l,l,o] 얘가 스트림 요소 하나
    .distinct()// [H,e,l,o]     [W,o,r,l,d]
    .collect(toList()); //H, e, l, o
```
## 🍎 flatMap

