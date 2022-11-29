![img](https://user-images.githubusercontent.com/96059261/204466993-7a1195e8-fd43-4450-9640-072e8b093862.jpg)

Collection 인터페이스의 메서드
|메서드|설명|
|------|-----|
|boolean add(Object o)|o를 Collection에 추가|
|boolean addAll(Collection c)|c의 객체들을 Collection에 모두 추가|
|void clear()|Collection의 모든 객체 삭제|
|boolean contains(Object o)|o가 Collection에 포함되어있나|
|bollean containsAll(Collection c)|c의 객체가 Collection에 포함되어있나|
|bollean equals(Object o)|동일한 Collection인지 확인|
|int hashCode()|Collection의 hash code 반환|
|boolean isEmpty()|Collection이 비어있는지 확인|
|Iterator iterator()|Collection의 Iterator를 반환|
|boolean remove(Object o)|지정된 객체를 삭제|
|boolean removeAll(Collection c)|지정된 Collection에 포함된 객체들을 삭제|
|boolean retainAll(Collection c)|c에 포함된 객체 남기고 나머지 삭제, 변화가 있으면 true, 없으면 false|
|int size()|Collection에 저장된 객체의 개수 반환|
|Object[] toArray()|Collecion에 저장된 객체를 객체배열로 반환|
|Object[] toArray(Object[] a)|a에 Collection의 객체를 저장 후 반환|
