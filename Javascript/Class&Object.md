## 🍎 Class

- ES6에서 도입
- 프로토타입을 기본적으로 상속한다.

```javascript
class Person {
  constructor(name, age) {
    this.name = name;
    this.age = age;
  }

  speak() {
    console.log(`${this.name}, hello!`);
  }

  get age() {
    return this._age;
  }

  set age(value) {
    this._age = value;
  }
}

//Object 생성
const ellie = new Person("ellie", 20);
```

## 🍎 get, set

- `this.age = age`를 호출하면 this.age에 값이 할당되는 것이 아니라 `this.age`는 내부적으로 get을 호출하고, `= age`는 set을 호출한다.
- set에 `this.age = age`라고 작성하게 되면 값을 할당할 때 계속 set을 호출하기 때문에 무한 루프에 빠지게 된다.
- 그래서 \_agef라고 기호를 붙여야 한다.(?)

## 🍎 public, private

- 최신에 나와서 대부분 사용하지 않는다. 사파리에서 지원되지도 않는다.

## 🍎 static

- 공통으로 사용, 클래스 이름으로 접근할 수 있다.

```javascript
class Article {
  static publisher = "Dream Coding";
  constructor(articleNumber) {
    this.articleNumber = articleNumber;
  }

  static printPublisher() {
    console.log(Article.publisher);
  }
}

const article1 = new Article(1);
const article2 = new Article(2);
console.log(article1.publisher); //undefined
console.log(Article.publisher); //Dream Coding
```

## 🍎상속, 다형성

```javascript
class Shape {
  constructor(width, height) {
    this.width = width;
    this.height = height;
    this.color = color;
  }

  draw() {
    console.log(`drawing ${this.color} color!`);
  }

  getArea() {
    return this.width * this.height;
  }
}

class Rectangle extends Shape {}
```

### instanceof

```javascript
console.log(rectangle instanceof Rectangle); //true
console.log(rectangle instanceof Shape); //true
console.log(rectangle instanceof Object); //true
```

## 🍎 Object

- 키와 값으로 구성.

```java
// Object 리터럴 생성
const obj1 = {};
//Object 생성자로 생성
const obj2 = new Object();

const ellie = { name : 'ellie', age : 4};

// 자스는 런타임 언어여서 실행 중에도 동적으로 객체를 바꿀 수 있다.
//하지만 동적으로 만들면 유지보수하기 힘들기 때문에 이 방법은 피하는 것이 좋다.
ellie.hasJob = true;
```

- 오브젝트 value 출력

```javascript
// 일반적인 경우 .으로 작성
console.log(ellie.name);
// 런타임 시 어떤 키값인지 모를 때 사용
console.log(ellie["name"]); // 키는 String으로 작성.

function printValue(obj, key) {
  console.log(obj[key]);
}
```

### Constructor Function

```javascript
//기존 오브젝트 생성하는 법
function makePerson(name, age) {
  return {
    name, //name : name 축약
    age, // age : age
  };
}

// 클래스로 생성-Constructor Function
function Person(name, age) {
  // this = {}; //생략
  this.name = name;
  this.age = age;
  //return this; //생략
}
```

## for...in, for...of

- key 값 출력할 때 : for...in

```javascript
for (key in ellie) {
  console.log(key);
}
```

- 배열 출력할 때 : for...of
