# props

: 상위 컴포넌트에서 하위 컴포넌트로 값을 전달할 때 사용하는 `단방향` 데이터 전달 방식

- 상위가 변경되면 자식의 모든 props도 최신 값으로 업데이트 된다.
- 따라서 자식에서 props를 변경하려 하면 안된다!!

## 예시

**상위 컴포넌트**

```javascript
<template>
  <ChildView child-props="this is child"></ChildView>
</template>

<script>
import ChildView from './components/ChildView'
export default {
  components: {
    ChildView
  }
}
</script>
```

**하위 컴포넌트**

```javascript
<template>
    <h1>{{childProps}}</h1>
</template>

<script>
export default {
    props:['childProps']
}
</script>
```

-> this is child 출력

## props 선언하기

```javascript
exprot default{
  props:['calmelCaseName'],
  // props:{//객체 속성과 타입을 명시해야함.
  //   title: String,
  //   likes: Number
  // },
  created(){
    console.log(this.calmelCaseName);//props는 this로 접근가능하다.!!
  }
}
```

- 케밥 케이스로 작성하면 따옴표 표시해야하므로 `카멜 케이스`사용한다.
- 하지만!! 자식 컴포넌트에 props를 전달할 때는 HTML 속성과 같이 `케밥 케이스`로 작성한다.

1. 문자열로 구성된 배열로 선언

```javascript
props: ["foo"];
```

2. 객체로 선언

```javascript
export default {
  props: {
    propA: Number,
    // 여러 타입 허용하기, 단 Boolean은 Boolean만 적용됨
    propB: [String, Number],
    // required로 필수 지정할 수 있음
    propC: {
      type: String,
      required: true,
    },
    // default 값도 지정할 수 있음
    propD: {
      type: Number,
      default: 100,
    },
    // 기본 값을 가지는 객체
    propE: {
      type: Object,
      // 객체 또는 배열 기본값은 팩토리 함수에서 반환되어야 합니다.
      // 함수는 컴포넌트에서 받은 rawProps를 인자로 받습니다.
      // (rawProps: 부모 컴포넌트에게 받은 props 전체 객체)
      default(rawProps) {
        return { message: "안녕!" };
      },
    },
    // 사용자 정의 유효성 검사 함수
    propF: {
      validator(value) {
        return ["성공", "경고", "위험"].includes(value);
      },
    },
    // 기본값이 있는 함수
    propG: {
      type: Function,
      // 기본값 객체나 배열을 정의하는 팩토리 함수가 아니라
      // 기본값으로 사용할 함수이다.
      default() {
        return "Default function";
      },
    },
  },
};
```

- 타입을 명시하여 유효성 검사를 할 수 있다.
- `null`과 `undefined`는 모든 타입에서 허용된다.
- 타입 종류
  - String, Number, Boolean, Array, Object, Date, Function, Symbol

## 부모 컴포넌트에서 자식 컨포넌트로 props 값 전달하기 - 정적 vs 동적

- 정적으로 값을 전달할 때는 `foo="this is foo"` 이렇게 작성, `문자열`만 가능하다!!
- 동적으로 값을 전달할 때는 :(v-bind) 붙히기.

1. 숫자 전달하기

```javascript
<BlogPost :likes="42" />

<BlogPost :likes="post.likes" />
```

- 42는 정적이지만 정적은 문자열로 인식하기 때문에 v-bind가 필요함

2. 불리언 전달하기

```javascript
<BlogPost is-published />

<!-- JavaScript 표현식임을 알려주려면 v-bind가 필요합니다. -->
<BlogPost :is-published="false" />

<BlogPost :is-published="post.isPublished" />
```

- 정적의 기본값은 true
- 직접 값 주려면 v-bind 필요

3. 배열 전달하기

```javascript
<BlogPost :comment-ids="[234, 266, 273]" />

<BlogPost :comment-ids="post.commentIds" />
```

4. 객체 전달하기

```javascript
<BlogPost
  :author="{
    name: '신형만',
    company: '떡잎 상사'
  }"
 />

<BlogPost :author="post.author" />
```

- 마찬가지로 정적값을 줄 때 v-bind 필요.
- 객체의 모든 속성을 전달하려면 다음과 같이 작성 가능.

```javascript
<ChildCom v-bind="testObj" />
```

## 자식에서 props 값을 사용할 때(단순히 값 뿌려주는 것이 아닌)

1. props 값을 data()로 선언하여 로컬 데이터로 사용

```javascript
export default {
  props: ["initialCounter"],
  data() {
    return {
      counter: this.initialCounter,
    };
  },
};
```

- 단 props가 갱신되어도 로컬 데이터는 변하지 않음. 즉, props의 초기값만 사용할 수 있음.

2. props의 값을 사용하는 computed 선언

```javascript
export default {
  props: ["size"],
  computed: {
    // prop이 변경될 때, 계산된 속성은 자동으로 업데이트 됩니다.
    normalizedSize() {
      return this.size.trim().toLowerCase();
    },
  },
};
```
