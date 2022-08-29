# Element.classList
: 엘리먼트의 클래스 속성에 접근.
```javaScript
const elementClasses = elementNodeReference.classList;
```

## add, remove, toggle, item, contains, replace

- `add( String [, String [, ...]] )` : 지정한 클래스 값을 추가. 만약 추가하려는 클래스가 엘리먼트의 class 속성에 이미 존재한다면 무시됨. 
- `remove( String [, String [, ...]] )` : 지정한 클래스 값 제거. 존재하지 않는 클래스 제거는 에러X.
- `toggle( String [, force] )` : 하나의 인수 -> 클래스가 존재하면 제거 및 false 반환, 존재하지 않으면 추가 및 true 반환
                                두 개 인수 -> 두번째 인수 조건 성립하면, 위와 동일
- `contains(String)` : 지정한 클래스 값이 class 속성에 존재하나
- `replace(oldClass, newClass)` : 클래스 교체
``` javaScript
//div 생성, div의 class 이름은 foo
const div = document.createElement('div');
div.className='foo';

//<div class="foo"></div>
console.log(div.outerHTML);

//"foo"지우고 "anotherclass" 이거 추가
div.classList.remove("foo");
div.classList.add("anotherclass");

//<div class="anotherclass"></div>
console.log(div.outerHTML);

//visible이면 없애고, 아니면 생기고
div.classList.toggle("visible");

//i가 10보다 작으면 토글 실행
div.classList.toggle("visible", i<10);

//false
console.log(div.classList.contains("foo"));


// 여러개 추가 및 제거
div.classList.add("foo", "bar", "baz");
div.classList.remove("foo", "bar", "baz");


// 이렇게도 가능
const cls = ["foo", "bar"];
div.classList.add(...cls);
div.classList.remove(...cls);

```